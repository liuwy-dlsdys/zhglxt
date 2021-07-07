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
 * Class to handle <code>DeleteFolder</code> command.
 */
public class DeleteFolderCommand extends XMLCommand implements IPostCommand {

    @Override
    public void initParams(final HttpServletRequest request,
                           final IConfiguration configuration, final Object... params)
            throws ConnectorException {

        super.initParams(request, configuration, params);
        if (this.configuration.isEnableCsrfProtection() && !checkCsrfToken(request, null)) {
            throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST, "CSRF Attempt");
        }
    }

    @Override
    protected void createXMLChildNodes(final int errorNum, final Element rootElement)
            throws ConnectorException {
    }

    /**
     * @return error code or 0 if ok. Deletes folder and thumb folder.
     */
    @Override
    protected int getDataForXml() {

        if (!checkIfTypeExists(this.type)) {
            this.type = null;
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE;
        }

        if (!AccessControlUtil.getInstance().checkFolderACL(this.type,
                this.currentFolder,
                this.userRole,
                AccessControlUtil.CKFINDER_CONNECTOR_ACL_FOLDER_DELETE)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
        }
        if (this.currentFolder.equals("/")) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
        }

        if (FileUtils.checkIfDirIsHidden(this.currentFolder, configuration)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
        }

        File dir = new File(configuration.getTypes().get(this.type).getPath()
                + this.currentFolder);

        try {
            if (!dir.exists() || !dir.isDirectory()) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_FOLDER_NOT_FOUND;
            }

            if (FileUtils.delete(dir)) {
                File thumbDir = new File(configuration.getThumbsPath()
                        + File.separator
                        + this.type
                        + this.currentFolder);
                FileUtils.delete(thumbDir);
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
}
