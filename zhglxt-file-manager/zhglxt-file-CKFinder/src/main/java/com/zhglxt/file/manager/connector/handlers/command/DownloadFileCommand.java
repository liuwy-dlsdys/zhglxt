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
import com.zhglxt.file.manager.connector.errors.ConnectorException;
import com.zhglxt.file.manager.connector.utils.AccessControlUtil;
import com.zhglxt.file.manager.connector.utils.FileUtils;

import javax.mail.internet.MimeUtility;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Class to handle <code>DownloadFile</code> command.
 */
public class DownloadFileCommand extends Command {

    /**
     * File to download.
     */
    private File file;
    /**
     * filename request param.
     */
    private String fileName;
    private Object format;
    private String newFileName;

    /**
     * executes the download file command. Writes file to response.
     *
     * @param out output stream
     * @throws ConnectorException when something went wrong during reading file.
     */
    @Override
    public void execute(final OutputStream out) throws ConnectorException {
        if (!checkIfTypeExists(this.type)) {
            this.type = null;
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE, false);
        }

        this.file = new File(configuration.getTypes().get(this.type).getPath()
                + currentFolder, fileName);

        if (!AccessControlUtil.getInstance().checkFolderACL(
                this.type, this.currentFolder, this.userRole,
                AccessControlUtil.CKFINDER_CONNECTOR_ACL_FILE_VIEW)) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED);
        }

        if (!FileUtils.checkFileName(this.fileName)
                || FileUtils.checkFileExtension(this.fileName,
                this.configuration.getTypes().get(this.type)) == 1) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST);
        }

        if (FileUtils.checkIfDirIsHidden(this.currentFolder, configuration)) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST);
        }
        try {
            if (!file.exists()
                    || !file.isFile()
                    || FileUtils.checkIfFileIsHidden(this.fileName,
                    this.configuration)) {
                throw new ConnectorException(
                        Constants.Errors.CKFINDER_CONNECTOR_ERROR_FILE_NOT_FOUND);
            }

            FileUtils.printFileContentToResponse(file, out);
        } catch (IOException e) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED, e);
        }

    }

    /**
     * inits params for download file command.
     *
     * @param request       request
     * @param configuration connector configuration
     * @param params        execute additional params.
     * @throws ConnectorException when error occurs.
     */
    @Override
    public void initParams(final HttpServletRequest request,
                           final IConfiguration configuration, final Object... params)
            throws ConnectorException {

        super.initParams(request, configuration, params);
        // problem with showing filename when dialog window appear
        this.newFileName = request.getParameter("FileName").replaceAll("\"", "\\\\\"");
        this.fileName = getParameter(request, "FileName");
        try {
            if (request.getHeader("User-Agent").indexOf("MSIE") != -1) {
                this.newFileName = URLEncoder.encode(this.newFileName, "UTF-8");
                this.newFileName = this.newFileName.replace("+", " ").replace("%2E", ".");
            } else {
                this.newFileName = MimeUtility.encodeWord(this.newFileName, "utf-8", "Q");
            }
        } catch (UnsupportedEncodingException ex) {
        }

    }

    /**
     * Sets response headers.
     *
     * @param response response
     * @param sc       servlet context
     */
    @Override
    public void setResponseHeader(final HttpServletResponse response,
                                  final ServletContext sc) {
        String mimetype = sc.getMimeType(fileName);
        response.setCharacterEncoding("utf-8");
        if (this.format != null && this.format.equals("text")) {
            response.setContentType("text/plain; charset=utf-8");
        } else {
            if (mimetype != null) {
                response.setContentType(mimetype);
            } else {
                response.setContentType("application/octet-stream");
            }
            if (file != null) {
                response.setContentLength((int) file.length());
            }

            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + this.newFileName + "\"");

        }

        response.setHeader("Cache-Control", "cache, must-revalidate");
        response.setHeader("Pragma", "public");
        response.setHeader("Expires", "0");

    }
}
