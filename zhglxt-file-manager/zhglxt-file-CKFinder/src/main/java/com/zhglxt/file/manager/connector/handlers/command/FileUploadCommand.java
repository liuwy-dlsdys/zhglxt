/*
 * CKFinder
 * ========
 * http://cksource.com/ckfinder
 * Copyright (C) 2007-2015, CKSource - Frederico Knabben. All rights reserved.
 *
 * The software, this file and its contents are subject to the CKFinder
 * License. Please read the license.txt file before using, installing, copying,
 * modifying or distribute this file or part of its contents. The contents of
 * this file is part of the Source Code of CKFinder.
 */
package com.zhglxt.file.manager.connector.handlers.command;

import com.zhglxt.file.manager.connector.configuration.Constants;
import com.zhglxt.file.manager.connector.configuration.Events;
import com.zhglxt.file.manager.connector.configuration.IConfiguration;
import com.zhglxt.file.manager.connector.data.AfterFileUploadEventArgs;
import com.zhglxt.file.manager.connector.data.ResourceType;
import com.zhglxt.file.manager.connector.errors.ConnectorException;
import com.zhglxt.file.manager.connector.errors.ErrorUtils;
import com.zhglxt.file.manager.connector.utils.AccessControlUtil;
import com.zhglxt.file.manager.connector.utils.FileUtils;
import com.zhglxt.file.manager.connector.utils.ImageUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadBase.IOFileUploadException;
import org.apache.commons.fileupload.FileUploadBase.InvalidContentTypeException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to handle <code>FileUpload</code> command.
 */
public class FileUploadCommand extends Command implements IPostCommand {

    /**
     * Uploading file name request.
     */
    protected String fileName;
    /**
     * File name after rename.
     */
    protected String newFileName;
    /**
     * Function number to call after file upload is completed.
     */
    protected String ckEditorFuncNum;
    /**
     * The selected response type to be used after file upload is completed.
     */
    protected String responseType;
    /**
     * Function number to call after file upload is completed.
     */
    protected String ckFinderFuncNum;
    /**
     * Language (locale) code.
     */
    private String langCode;
    /**
     * Flag informing if file was uploaded correctly.
     */
    protected boolean uploaded;
    /**
     * Error code number.
     */
    protected int errorCode;
    /**
     * Custom error message.
     */
    protected String customErrorMsg;
    /**
     * Array containing unsafe characters which can't be used in file name.
     */
    private static final char[] UNSAFE_FILE_NAME_CHARS = {':', '*', '?', '|', '/'};

    /**
     * default constructor.
     */
    public FileUploadCommand() {
        this.errorCode = 0;
        this.fileName = "";
        this.newFileName = "";
        this.type = "";
        this.uploaded = false;
    }

    /**
     * Executes file upload command.
     *
     * @param out the response output stream
     * @throws ConnectorException when error occurs.
     */
    @Override
    public void execute(final OutputStream out) throws ConnectorException {
        if (configuration.isDebugMode() && this.exception != null) {
            throw new ConnectorException(this.errorCode, this.exception);
        }
        try {
            String errorMsg = this.errorCode == Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE ? "" : (this.errorCode == Constants.Errors.CKFINDER_CONNECTOR_ERROR_CUSTOM_ERROR ? this.customErrorMsg
                    : ErrorUtils.getInstance().getErrorMsgByLangAndCode(this.langCode, this.errorCode, this.configuration));
            errorMsg = errorMsg.replaceAll("%1", Matcher.quoteReplacement(this.newFileName));
            String path = "";

            if (!uploaded) {
                this.newFileName = "";
                this.currentFolder = "";
            } else {
                path = configuration.getTypes().get(this.type).getUrl()
                        + this.currentFolder;
            }

            if (this.responseType != null && this.responseType.equals("txt")) {
                out.write((this.newFileName + "|" + errorMsg).getBytes("UTF-8"));
            } else {
                if (checkFuncNum()) {
                    handleOnUploadCompleteCallFuncResponse(out, errorMsg, path);
                } else {
                    handleOnUploadCompleteResponse(out, errorMsg);
                }
            }

        } catch (IOException e) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED, e);
        }

    }

    /**
     * check if func num is set in request.
     *
     * @return true if is.
     */
    protected boolean checkFuncNum() {
        return this.ckFinderFuncNum != null;
    }

    /**
     * return response when func num is set.
     *
     * @param out      response.
     * @param errorMsg error message
     * @param path     path
     * @throws IOException when error occurs.
     */
    protected void handleOnUploadCompleteCallFuncResponse(final OutputStream out,
                                                          final String errorMsg,
                                                          final String path)
            throws IOException {
        this.ckFinderFuncNum = this.ckFinderFuncNum.replaceAll(
                "[^\\d]", "");
        out.write("<script type=\"text/javascript\">".getBytes("UTF-8"));
        out.write(("window.parent.CKFinder.tools.callFunction("
                + this.ckFinderFuncNum + ", '"
                + path
                + FileUtils.backupWithBackSlash(this.newFileName, "'")
                + "', '" + errorMsg + "');").getBytes("UTF-8"));
        out.write("</script>".getBytes("UTF-8"));
    }

    /**
     * @param out      out put stream
     * @param errorMsg error message
     * @throws IOException when error occurs
     */
    protected void handleOnUploadCompleteResponse(final OutputStream out,
                                                  final String errorMsg) throws IOException {
        out.write("<script type=\"text/javascript\">".getBytes("UTF-8"));
        out.write("window.parent.OnUploadCompleted(".getBytes("UTF-8"));
        out.write(("\'" + FileUtils.backupWithBackSlash(this.newFileName, "'") + "\'").getBytes("UTF-8"));
        out.write((", \'"
                + (this.errorCode
                != Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE ? errorMsg
                : "") + "\'").getBytes("UTF-8"));
        out.write(");".getBytes("UTF-8"));
        out.write("</script>".getBytes("UTF-8"));
    }

    /**
     * initializing parametrs for command handler.
     *
     * @param request       request
     * @param configuration connector configuration.
     * @param params        execute additional params.
     * @throws ConnectorException when error occurs.
     */
    @Override
    public void initParams(final HttpServletRequest request,
                           final IConfiguration configuration, final Object... params)
            throws ConnectorException {
        super.initParams(request, configuration, params);
        this.ckFinderFuncNum = request.getParameter("CKFinderFuncNum");
        this.ckEditorFuncNum = request.getParameter("CKEditorFuncNum");
        this.responseType = request.getParameter("response_type") != null
                ? request.getParameter("response_type") : request.getParameter("responseType");
        this.langCode = request.getParameter("langCode");

        if (this.errorCode == Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE) {
            this.uploaded = uploadFile(request);
        }

    }

    /**
     * uploads file and saves to file.
     *
     * @param request request
     * @return true if uploaded correctly.
     */
    private boolean uploadFile(final HttpServletRequest request) {
        if (!AccessControlUtil.getInstance().checkFolderACL(
                this.type, this.currentFolder, this.userRole,
                AccessControlUtil.CKFINDER_CONNECTOR_ACL_FILE_UPLOAD)) {
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
            return false;
        }
        return fileUpload(request);
    }

    /**
     * @param request http request
     * @return true if uploaded correctly
     */
    @SuppressWarnings("unchecked")
    private boolean fileUpload(final HttpServletRequest request) {
        try {
            DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
            ServletFileUpload uploadHandler = new ServletFileUpload(
                    fileItemFactory);

            List<FileItem> items = uploadHandler.parseRequest(request);
            Collections.sort(items, new Comparator<FileItem>() {
                //This comparator moves tokenParamName to first position on the list.
                @Override
                public int compare(FileItem a, FileItem b) {
                    if (a.getFieldName().equals(tokenParamName)) {
                        return (-1);
                    }
                    if (b.getFieldName().equals(tokenParamName)) {
                        return (1);
                    }
                    return 0;
                }
            });

            for (int i = 0, j = items.size(); i < j; i++) {
                FileItem item = items.get(i);
                if (configuration.isEnableCsrfProtection() && (!items.get(0).getFieldName().equals(tokenParamName)
                        || (item.getFieldName().equals(tokenParamName) && !checkCsrfToken(request, item.getString())))) {
                    throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST, "CSRF Attempt");
                } else if (items.get(0).getFieldName().equals(tokenParamName) && items.size() == 1) {
                    //No file was provided. Only the CSRF token
                    throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_CUSTOM_ERROR, "No file provided in the request.");
                } else if (!item.isFormField()) {
                    String path = configuration.getTypes().get(this.type).getPath()
                            + this.currentFolder;
                    this.fileName = getFileItemName(item);

                    try {
                        if (validateUploadItem(item, path)) {
                            return saveTemporaryFile(path, item);
                        }
                    } finally {
                        item.delete();
                    }
                }
            }
            return false;
        } catch (InvalidContentTypeException e) {
            if (configuration.isDebugMode()) {
                this.exception = e;
            }
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_CORRUPT;
            return false;
        } catch (IOFileUploadException e) {
            if (configuration.isDebugMode()) {
                this.exception = e;
            }
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
            return false;
        } catch (SizeLimitExceededException e) {
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_TOO_BIG;
            return false;
        } catch (FileSizeLimitExceededException e) {
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_TOO_BIG;
            return false;
        } catch (ConnectorException e) {
            this.errorCode = e.getErrorCode();
            if (this.errorCode == Constants.Errors.CKFINDER_CONNECTOR_ERROR_CUSTOM_ERROR) {
                this.customErrorMsg = e.getErrorMsg();
            }
            return false;
        } catch (Exception e) {
            if (configuration.isDebugMode()) {
                this.exception = e;
            }
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;

            return false;
        }

    }

    /**
     * saves temporary file in the correct file path.
     *
     * @param path path to save file
     * @param item file upload item
     * @return result of saving, true if saved correctly
     * @throws Exception when error occurs.
     */
    private boolean saveTemporaryFile(final String path, final FileItem item)
            throws Exception {
        File file = new File(path, this.newFileName);

        AfterFileUploadEventArgs args = new AfterFileUploadEventArgs();
        args.setCurrentFolder(this.currentFolder);
        args.setFile(file);
        args.setFileContent(item.get());
        if (!ImageUtils.isImage(file)) {
            item.write(file);
            if (configuration.getEvents() != null) {
                configuration.getEvents().run(Events.EventTypes.AfterFileUpload,
                        args, configuration);
            }
            return true;
        } else if (ImageUtils.checkImageSize(item.getInputStream(), this.configuration)
                || configuration.checkSizeAfterScaling()) {
            ImageUtils.createTmpThumb(item.getInputStream(), file, getFileItemName(item),
                    this.configuration);
            if (!configuration.checkSizeAfterScaling()
                    || FileUtils.checkFileSize(configuration.getTypes().get(this.type), file.length())) {
                if (configuration.getEvents() != null) {
                    configuration.getEvents().run(Events.EventTypes.AfterFileUpload, args, configuration);
                }
                return true;
            } else {
                file.delete();
                this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_TOO_BIG;
                return false;
            }
        } else {
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_TOO_BIG;
            return false;
        }
    }

    /**
     * if file exists this method adds (number) to file.
     *
     * @param path folder
     * @param name file name
     * @return new file name.
     */
    private String getFinalFileName(final String path, final String name) {
        File file = new File(path, name);
        int number = 0;

        String nameWithoutExtension = FileUtils.getFileNameWithoutExtension(name, false);
        Pattern p = Pattern.compile("^(AUX|COM\\d|CLOCK\\$|CON|NUL|PRN|LPT\\d)$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(nameWithoutExtension);
        boolean protectedName = m.find();

        String ext = FileUtils.getFileExtension(name, false);

        while (true) {
            if (file.exists() || protectedName) {
                number++;
                StringBuilder sb = new StringBuilder();
                sb.append(FileUtils.getFileNameWithoutExtension(name, false));
                sb.append("(").append(number).append(")");

                if (ext != null && !ext.isEmpty() && !FileUtils.FILENAME_WITHOUT_EXTENSION.equals(ext)) {
                    sb.append(".").append(ext);
                }

                this.newFileName = sb.toString();
                file = new File(path, this.newFileName);
                this.errorCode
                        = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_FILE_RENAMED;
                protectedName = false;
            } else {
                return this.newFileName;
            }
        }
    }

    /**
     * validates uploaded file.
     *
     * @param item uploaded item.
     * @param path file path
     * @return true if validation
     */
    private boolean validateUploadItem(final FileItem item, final String path) {

        if (item.getName() != null && item.getName().length() > 0) {
            this.fileName = getFileItemName(item);
        } else {
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_INVALID;
            return false;
        }
        this.newFileName = this.fileName;

        for (char c : UNSAFE_FILE_NAME_CHARS) {
            this.newFileName = this.newFileName.replace(c, '_');
        }

        if (configuration.isDisallowUnsafeCharacters()) {
            this.newFileName = this.newFileName.replace(';', '_');
        }
        if (configuration.forceASCII()) {
            this.newFileName = FileUtils.convertToASCII(this.newFileName);
        }
        if (!this.newFileName.equals(this.fileName)) {
            this.errorCode
                    = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_INVALID_NAME_RENAMED;
        }

        if (FileUtils.checkIfDirIsHidden(this.currentFolder, configuration)) {
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            return false;
        }
        if (!FileUtils.checkFileName(this.newFileName)
                || FileUtils.checkIfFileIsHidden(this.newFileName,
                configuration)) {
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_NAME;
            return false;
        }
        final ResourceType resourceType = configuration.getTypes().get(this.type);
        int checkFileExt = FileUtils.checkFileExtension(this.newFileName, resourceType);
        if (checkFileExt == 1) {
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_EXTENSION;
            return false;
        }
        if (configuration.ckeckDoubleFileExtensions()) {
            this.newFileName = FileUtils.renameFileWithBadExt(resourceType, this.newFileName);
        }

        try {
            File file = new File(path, getFinalFileName(path, this.newFileName));
            if (!(ImageUtils.isImage(file) && configuration.checkSizeAfterScaling())
                    && !FileUtils.checkFileSize(resourceType, item.getSize())) {
                this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_TOO_BIG;
                return false;
            }

            if (configuration.getSecureImageUploads() && ImageUtils.isImage(file)
                    && !ImageUtils.checkImageFile(item)) {
                this.errorCode
                        = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_CORRUPT;
                return false;
            }

            if (!FileUtils.checkIfFileIsHtmlFile(file.getName(), configuration)
                    && FileUtils.detectHtml(item)) {
                this.errorCode
                        = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_WRONG_HTML_FILE;
                return false;
            }
        } catch (SecurityException e) {
            if (configuration.isDebugMode()) {
                this.exception = e;
            }
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
            return false;
        } catch (IOException e) {
            if (configuration.isDebugMode()) {
                this.exception = e;
            }
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
            return false;
        }

        return true;
    }

    /**
     * set response headers. Not user in this command.
     *
     * @param response response
     * @param sc       servlet context
     */
    @Override
    public void setResponseHeader(final HttpServletResponse response,
                                  final ServletContext sc) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");
    }

    /**
     * save if uploaded file item name is full file path not only file name.
     *
     * @param item file upload item
     * @return file name of uploaded item
     */
    private String getFileItemName(final FileItem item) {
		Pattern p = Pattern.compile("[^\\\\/]+$");
		Matcher m = p.matcher(item.getName());
		return (m.find()) ? m.group(0) : "";

        // 如果需要改变上传后的文件名称，可使用以下代码进行修改，上面的注释或删除掉即可
//        String origin = item.getName();
//        return new Date().getTime() + "." + FileUtils.getFileExtension(origin);
    }

    /**
     * check request for security issue.
     *
     * @param reqParam request param
     * @return true if validation passed
     * @throws ConnectorException if validation error occurs.
     */
    @Override
    protected boolean checkParam(final String reqParam)
            throws ConnectorException {
        if (reqParam == null || reqParam.equals("")) {
            return true;
        }
        if (Pattern.compile(Constants.INVALID_PATH_REGEX).matcher(reqParam).find()) {
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_NAME;
            return false;
        }
        return true;
    }

    @Override
    protected boolean checkHidden()
            throws ConnectorException {
        if (FileUtils.checkIfDirIsHidden(this.currentFolder, configuration)) {
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            return true;
        }
        return false;
    }

    @Override
    protected boolean checkConnector(final HttpServletRequest request)
            throws ConnectorException {
        if (!configuration.enabled() || !configuration.checkAuthentication(request)) {
            this.errorCode
                    = Constants.Errors.CKFINDER_CONNECTOR_ERROR_CONNECTOR_DISABLED;
            return false;
        }
        return true;
    }

    @Override
    protected boolean checkIfCurrFolderExists(final HttpServletRequest request)
            throws ConnectorException {
        String tmpType = getParameter(request, "type");
        if (checkIfTypeExists(tmpType)) {
            File currDir = new File(configuration.getTypes().get(tmpType).getPath()
                    + this.currentFolder);
            if (currDir.exists() && currDir.isDirectory()) {
                return true;
            } else {
                this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_FOLDER_NOT_FOUND;
                return false;
            }
        }
        return false;
    }

    @Override
    protected boolean checkIfTypeExists(final String type) {
        ResourceType testType = configuration.getTypes().get(type);
        if (testType == null) {
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE;
            return false;
        }
        return true;
    }
}
