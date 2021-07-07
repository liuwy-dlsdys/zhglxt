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
import com.zhglxt.file.manager.connector.utils.PathUtils;
import org.w3c.dom.Element;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * Class to handle <code>RenameFolder</code> command.
 */
public class RenameFolderCommand extends XMLCommand implements IPostCommand {

    private String newFolderName;
    private String newFolderPath;

    @Override
    protected void createXMLChildNodes(final int errorNum, final Element rootElement)
            throws ConnectorException {
        if (errorNum == Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE) {
            createRenamedFolderNode(rootElement);
        }

    }

    /**
     * creates XML node for renamed folder.
     *
     * @param rootElement XML root element.
     */
    private void createRenamedFolderNode(final Element rootElement) {
        Element element = creator.getDocument().createElement("RenamedFolder");
        element.setAttribute("newName", this.newFolderName);
        element.setAttribute("newPath", this.newFolderPath);
        element.setAttribute("newUrl", configuration.getTypes().get(this.type).getUrl() + this.newFolderPath);
        rootElement.appendChild(element);

    }

    @Override
    protected int getDataForXml() {

        try {
            checkParam(newFolderName);

        } catch (ConnectorException e) {
            return e.getErrorCode();
        }

        if (!checkIfTypeExists(this.type)) {
            this.type = null;
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE;
        }

        if (!AccessControlUtil.getInstance().checkFolderACL(this.type,
                this.currentFolder,
                this.userRole,
                AccessControlUtil.CKFINDER_CONNECTOR_ACL_FOLDER_RENAME)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
        }

        if (configuration.forceASCII()) {
            this.newFolderName = FileUtils.convertToASCII(this.newFolderName);
        }

        if (FileUtils.checkIfDirIsHidden(this.newFolderName, configuration)
                || !FileUtils.checkFolderName(this.newFolderName, configuration)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_NAME;
        }

        if (this.currentFolder.equals("/")) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
        }

        File dir = new File(configuration.getTypes().get(this.type).getPath()
                + this.currentFolder);
        try {
            if (!dir.isDirectory()) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            }
            setNewFolder();
            File newDir = new File(configuration.getTypes().get(this.type).getPath()
                    + this.newFolderPath);
            if (newDir.exists()) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ALREADY_EXIST;
            }
            if (dir.renameTo(newDir)) {
                renameThumb();
            } else {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
            }
        } catch (SecurityException e) {
            if (configuration.isDebugMode()) {
                throw e;
            } else {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
            }
        }

        return Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE;
    }

    /**
     * renames thumb folder.
     */
    private void renameThumb() {
        File thumbDir = new File(configuration.getThumbsPath()
                + File.separator
                + this.type
                + this.currentFolder);
        File newThumbDir = new File(configuration.getThumbsPath()
                + File.separator
                + this.type
                + this.newFolderPath);
        thumbDir.renameTo(newThumbDir);

    }

    /**
     * sets new folder name.
     */
    private void setNewFolder() {
        String tmp1 = this.currentFolder.substring(0,
                this.currentFolder.lastIndexOf('/'));
        this.newFolderPath = tmp1.substring(0,
                tmp1.lastIndexOf('/') + 1).concat(this.newFolderName);
        this.newFolderPath = PathUtils.addSlashToEnd(this.newFolderPath);

    }

    /**
     * @param request       request
     * @param configuration connector conf
     * @param params        execute additional params.
     * @throws ConnectorException when error occurs.
     */
    @Override
    public void initParams(final HttpServletRequest request,
                           final IConfiguration configuration,
                           final Object... params) throws ConnectorException {

        super.initParams(request, configuration);
        if (this.configuration.isEnableCsrfProtection() && !checkCsrfToken(request, null)) {
            throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST, "CSRF Attempt");
        }
        this.newFolderName = getParameter(request, "NewFolderName");

    }
}
