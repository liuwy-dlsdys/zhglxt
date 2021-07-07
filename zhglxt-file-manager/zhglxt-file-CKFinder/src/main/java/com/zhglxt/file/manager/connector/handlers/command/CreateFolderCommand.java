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
import org.w3c.dom.Element;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * Class to handle <code>CreateFolder</code> command.
 */
public class CreateFolderCommand extends XMLCommand implements IPostCommand {

    /**
     * new folder name request param.
     */
    private String newFolderName;

    @Override
    protected void createXMLChildNodes(final int errorNum, final Element rootElement)
            throws ConnectorException {
        if (errorNum == Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE) {
            createNewFolderElement(rootElement);
        }

    }

    /**
     * creates current folder XML node.
     *
     * @param rootElement XML root element.
     */
    private void createNewFolderElement(final Element rootElement) {
        Element element = creator.getDocument().createElement("NewFolder");
        element.setAttribute("name", this.newFolderName);
        rootElement.appendChild(element);

    }

    /**
     * gets data for xml. Not used in this handler.
     *
     * @return always 0
     */
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

        if (!AccessControlUtil.getInstance().checkFolderACL(
                this.type, this.currentFolder, this.userRole,
                AccessControlUtil.CKFINDER_CONNECTOR_ACL_FOLDER_CREATE)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
        }

        if (configuration.forceASCII()) {
            this.newFolderName = FileUtils.convertToASCII(this.newFolderName);
        }

        if (!FileUtils.checkFolderName(this.newFolderName, configuration)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_NAME;
        }
        if (FileUtils.checkIfDirIsHidden(this.currentFolder, configuration)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
        }
        if (FileUtils.checkIfDirIsHidden(newFolderName, configuration)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_NAME;
        }

        try {
            if (createFolder()) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE;
            } else {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
            }

        } catch (SecurityException e) {
            if (configuration.isDebugMode()) {
                throw e;
            } else {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
            }
        } catch (ConnectorException e) {
            return e.getErrorCode();
        }
    }

    /**
     * creates folder. throws Exception when security problem occurs or folder already exists
     *
     * @return true if folder is created correctly
     * @throws ConnectorException when error occurs or dir exists
     */
    private boolean createFolder() throws ConnectorException {
        File dir = new File(configuration.getTypes().get(this.type).getPath()
                + currentFolder + newFolderName);
        if (dir.exists()) {
            throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_ALREADY_EXIST);
        } else {
            return dir.mkdir();
        }
    }

    @Override
    public void initParams(final HttpServletRequest request,
                           final IConfiguration configuration, final Object... params)
            throws ConnectorException {
        super.initParams(request, configuration, params);
        if (this.configuration.isEnableCsrfProtection() && !checkCsrfToken(request, null)) {
            throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST, "CSRF Attempt");
        }
        this.newFolderName = getParameter(request, "NewFolderName");
    }
}
