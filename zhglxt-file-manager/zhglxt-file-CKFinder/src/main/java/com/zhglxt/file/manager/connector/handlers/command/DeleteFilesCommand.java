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
import com.zhglxt.file.manager.connector.configuration.IConfiguration;
import com.zhglxt.file.manager.connector.data.FilePostParam;
import com.zhglxt.file.manager.connector.errors.ConnectorException;
import com.zhglxt.file.manager.connector.utils.AccessControlUtil;
import com.zhglxt.file.manager.connector.utils.FileUtils;
import org.w3c.dom.Element;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Class used to handle <code>DeleteFiles</code> command.
 */
public class DeleteFilesCommand extends XMLCommand implements IPostCommand {

    private List<FilePostParam> files;
    private int filesDeleted;
    private boolean addDeleteNode;

    @Override
    protected void createXMLChildNodes(int errorNum, Element rootElement) throws ConnectorException {
        if (creator.hasErrors()) {
            Element errorsNode = creator.getDocument().createElement("Errors");
            creator.addErrors(errorsNode);
            rootElement.appendChild(errorsNode);
        }

        if (this.addDeleteNode) {
            createDeleteFielsNode(rootElement);
        }
    }

    /**
     * Adds file deletion node in XML response.
     *
     * @param rootElement root element in XML response
     */
    private void createDeleteFielsNode(Element rootElement) {
        Element element = creator.getDocument().createElement("DeleteFiles");
        element.setAttribute("deleted", String.valueOf(this.filesDeleted));
        rootElement.appendChild(element);
    }

    /**
     * Prepares data for XML response.
     *
     * @return error code or 0 if action ended with success.
     */
    @Override
    protected int getDataForXml() {

        this.filesDeleted = 0;

        this.addDeleteNode = false;

        if (!checkIfTypeExists(this.type)) {
            this.type = null;
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE;
        }

        for (FilePostParam fileItem : this.files) {
            if (!FileUtils.checkFileName(fileItem.getName())) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            }

            if (configuration.getTypes().get(fileItem.getType()) == null) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            }

            if (fileItem.getFolder() == null || fileItem.getFolder().equals("")
                    || Pattern.compile(Constants.INVALID_PATH_REGEX).matcher(
                    fileItem.getFolder()).find()) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            }

            if (FileUtils.checkIfDirIsHidden(fileItem.getFolder(), this.configuration)) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            }

            if (FileUtils.checkIfFileIsHidden(fileItem.getName(), this.configuration)) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            }

            if (FileUtils.checkFileExtension(fileItem.getName(), this.configuration.getTypes().get(fileItem.getType())) == 1) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;

            }

            if (!AccessControlUtil.getInstance().checkFolderACL(fileItem.getType(), fileItem.getFolder(), this.userRole,
                    AccessControlUtil.CKFINDER_CONNECTOR_ACL_FILE_DELETE)) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
            }

            File file = new File(configuration.getTypes().get(fileItem.getType()).getPath() + fileItem.getFolder(), fileItem.getName());

            try {
                this.addDeleteNode = true;
                if (!file.exists()) {
                    creator.appendErrorNodeChild(
                            Constants.Errors.CKFINDER_CONNECTOR_ERROR_FILE_NOT_FOUND,
                            fileItem.getName(), fileItem.getFolder(), fileItem.getType());
                    continue;
                }

                if (FileUtils.delete(file)) {
                    File thumbFile = new File(configuration.getThumbsPath()
                            + File.separator + fileItem.getType() + this.currentFolder, fileItem.getName());
                    this.filesDeleted++;

                    try {
                        FileUtils.delete(thumbFile);
                    } catch (Exception exp) {
                        // No errors if we are not able to delete the thumb.
                    }
                } else { //If access is denied, report error and try to delete rest of files.
                    creator.appendErrorNodeChild(
                            Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED,
                            fileItem.getName(), fileItem.getFolder(), fileItem.getType());
                }
            } catch (SecurityException e) {
                if (configuration.isDebugMode()) {
                    throw e;
                } else {
                    return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
                }
            }
        }
        if (creator.hasErrors()) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_DELETE_FAILED;
        } else {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE;
        }
    }

    /**
     * Initializes parameters for command handler.
     *
     * @param request       current response object
     * @param configuration connector configuration object
     * @param params        additional parameters
     * @throws ConnectorException when initialization parameters can't be loaded for command handler.
     */
    @Override
    public void initParams(HttpServletRequest request,
                           IConfiguration configuration,
                           Object... params) throws ConnectorException {
        super.initParams(request, configuration);
        if (this.configuration.isEnableCsrfProtection() && !checkCsrfToken(request, null)) {
            throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST, "CSRF Attempt");
        }
        this.files = new ArrayList<FilePostParam>();
        getFilesListFromRequest(request);
    }

    /**
     * Gets list of files from request.
     *
     * @param request current request object
     */
    private void getFilesListFromRequest(HttpServletRequest request) {
        int i = 0;
        String paramName = "files[" + i + "][name]";
        while (request.getParameter(paramName) != null) {
            FilePostParam file = new FilePostParam();
            file.setName(getParameter(request, paramName));
            file.setFolder(getParameter(request, "files[" + i + "][folder]"));
            file.setOptions(getParameter(request, "files[" + i + "][options]"));
            file.setType(getParameter(request, "files[" + i + "][type]"));
            this.files.add(file);
            paramName = "files[" + (++i) + "][name]";
        }
    }
}
