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
package com.zhglxt.file.manager.connector.plugins;

import com.zhglxt.file.manager.connector.configuration.Constants;
import com.zhglxt.file.manager.connector.configuration.IConfiguration;
import com.zhglxt.file.manager.connector.data.BeforeExecuteCommandEventArgs;
import com.zhglxt.file.manager.connector.data.EventArgs;
import com.zhglxt.file.manager.connector.data.IEventHandler;
import com.zhglxt.file.manager.connector.errors.ConnectorException;
import com.zhglxt.file.manager.connector.handlers.command.XMLCommand;
import com.zhglxt.file.manager.connector.utils.AccessControlUtil;
import com.zhglxt.file.manager.connector.utils.FileUtils;
import org.w3c.dom.Element;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageResizeInfoCommand extends XMLCommand implements IEventHandler {

    private int imageWidth;
    private int imageHeight;
    private String fileName;

    @Override
    public boolean runEventHandler(EventArgs args, IConfiguration configuration1)
            throws ConnectorException {
        BeforeExecuteCommandEventArgs args1 = (BeforeExecuteCommandEventArgs) args;
        if ("ImageResizeInfo".equals(args1.getCommand())) {
            this.runCommand(args1.getRequest(), args1.getResponse(), configuration1);
            return false;
        }
        return true;
    }

    @Override
    protected void createXMLChildNodes(final int errorNum, final Element rootElement)
            throws ConnectorException {
        if (errorNum == Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE) {
            createImageInfoNode(rootElement);
        }

    }

    private void createImageInfoNode(Element rootElement) {
        Element element = creator.getDocument().createElement("ImageInfo");
        element.setAttribute("width", String.valueOf(imageWidth));
        element.setAttribute("height", String.valueOf(imageHeight));
        rootElement.appendChild(element);

    }

    @Override
    protected int getDataForXml() {
        if (!checkIfTypeExists(this.type)) {
            this.type = null;
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE;
        }

        if (!AccessControlUtil.getInstance().checkFolderACL(type, this.currentFolder,
                userRole, AccessControlUtil.CKFINDER_CONNECTOR_ACL_FILE_VIEW)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
        }

        if (fileName == null || fileName.equals("")
                || !FileUtils.checkFileName(this.fileName)
                || FileUtils.checkIfFileIsHidden(this.fileName, this.configuration)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
        }

        if (FileUtils.checkFileExtension(fileName, configuration.getTypes().get(type)) == 1) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
        }

        File imageFile = new File(configuration.getTypes().get(this.type).getPath()
                + this.currentFolder,
                this.fileName);

        try {
            if (!(imageFile.exists() && imageFile.isFile())) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_FILE_NOT_FOUND;
            }

            BufferedImage image = ImageIO.read(imageFile);
            this.imageWidth = image.getWidth();
            this.imageHeight = image.getHeight();
        } catch (SecurityException e) {
            if (configuration.isDebugMode()) {
                this.exception = e;
            }
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
        } catch (IOException e) {
            if (configuration.isDebugMode()) {
                this.exception = e;
            }
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
        }

        return Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE;
    }

    @Override
    public void initParams(HttpServletRequest request,
                           IConfiguration configuration, Object... params)
            throws ConnectorException {
        super.initParams(request, configuration, params);
        this.imageHeight = 0;
        this.imageWidth = 0;
        this.currentFolder = request.getParameter("currentFolder");
        this.type = request.getParameter("type");
        this.fileName = request.getParameter("fileName");
    }
}
