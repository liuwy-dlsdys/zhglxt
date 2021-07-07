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
import com.zhglxt.file.manager.connector.data.XmlAttribute;
import com.zhglxt.file.manager.connector.data.XmlElementData;
import com.zhglxt.file.manager.connector.errors.ConnectorException;
import com.zhglxt.file.manager.connector.utils.AccessControlUtil;
import com.zhglxt.file.manager.connector.utils.FileUtils;
import com.zhglxt.file.manager.connector.utils.ImageUtils;
import org.w3c.dom.Element;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to handle <code>GetFiles</code> command.
 */
public class GetFilesCommand extends XMLCommand {

    /**
     * number of bytes in kilobyte.
     */
    private static final float BYTES = 1024f;
    /**
     * list of files.
     */
    private List<String> files;
    /**
     * temporary field to keep full path.
     */
    private String fullCurrentPath;
    /**
     * show thumb post param.
     */
    private String showThumbs;

    /**
     * initializing parameters for command handler.
     *
     * @param request       request
     * @param configuration connector configuration
     * @param params        execute additional params.
     * @throws ConnectorException when error occurs
     */
    @Override
    public void initParams(final HttpServletRequest request,
                           final IConfiguration configuration, final Object... params)
            throws ConnectorException {
        super.initParams(request, configuration);

        this.showThumbs = request.getParameter("showThumbs");
    }

    @Override
    protected void createXMLChildNodes(final int errorNum, final Element rootElement)
            throws ConnectorException {
        if (errorNum == Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE) {
            createFilesData(rootElement);
        }
    }

    /**
     * gets data to XML response.
     *
     * @return 0 if ok, otherwise error code
     */
    @Override
    protected int getDataForXml() {

        if (!checkIfTypeExists(this.type)) {
            this.type = null;
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE;
        }

        this.fullCurrentPath = configuration.getTypes().get(this.type).getPath()
                + this.currentFolder;

        if (!AccessControlUtil.getInstance().checkFolderACL(
                this.type, this.currentFolder, this.userRole,
                AccessControlUtil.CKFINDER_CONNECTOR_ACL_FILE_VIEW)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
        }

        File dir = new File(this.fullCurrentPath);
        try {
            if (!dir.exists()) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_FOLDER_NOT_FOUND;
            }
            files = FileUtils.findChildrensList(dir, false);
        } catch (SecurityException e) {
            if (configuration.isDebugMode()) {
                throw e;
            } else {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
            }
        }
        filterListByHiddenAndNotAllowed();
        Collections.sort(files);
        return Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE;
    }

    /**
     *
     */
    private void filterListByHiddenAndNotAllowed() {
        List<String> tmpFiles = new ArrayList<String>();
        for (String file : this.files) {
            if (FileUtils.checkFileExtension(file, this.configuration.getTypes().get(this.type)) == 0
                    && !FileUtils.checkIfFileIsHidden(file, this.configuration)) {
                tmpFiles.add(file);
            }
        }

        this.files.clear();
        this.files.addAll(tmpFiles);

    }

    /**
     * creates files data node in response XML.
     *
     * @param rootElement root element from XML.
     */
    private void createFilesData(final Element rootElement) {
        Element element = creator.getDocument().createElement("Files");
        for (String filePath : files) {
            File file = new File(this.fullCurrentPath, filePath);
            if (file.exists()) {
                XmlElementData elementData = new XmlElementData("File");
                XmlAttribute attribute = new XmlAttribute("name", filePath);
                elementData.getAttributes().add(attribute);
                attribute = new XmlAttribute("date",
                        FileUtils.parseLastModifDate(file));
                elementData.getAttributes().add(attribute);
                attribute = new XmlAttribute("size", getSize(file));
                elementData.getAttributes().add(attribute);
                if (ImageUtils.isImage(file) && isAddThumbsAttr()) {
                    String attr = createThumbAttr(file);
                    if (!attr.equals("")) {
                        attribute = new XmlAttribute("thumb", attr);
                        elementData.getAttributes().add(attribute);
                    }
                }
                elementData.addToDocument(this.creator.getDocument(), element);
            }
        }
        rootElement.appendChild(element);
    }

    /**
     * gets thumb attribute value.
     *
     * @param file file to check if has thumb.
     * @return thumb attribute values
     */
    private String createThumbAttr(final File file) {
        File thumbFile = new File(configuration.getThumbsPath()
                + File.separator + this.type + this.currentFolder,
                file.getName());
        if (thumbFile.exists()) {
            return file.getName();
        } else if (isShowThumbs()) {
            return "?".concat(file.getName());
        }
        return "";
    }

    /**
     * get file size.
     *
     * @param file file
     * @return file size
     */
    private String getSize(final File file) {
        if (file.length() > 0 && file.length() < BYTES) {
            return "1";
        } else {
            return String.valueOf(Math.round(file.length() / BYTES));
        }
    }

    /**
     * Check if show thumbs or not (add attr to file node with thumb file name).
     *
     * @return true if show thumbs
     */
    private boolean isAddThumbsAttr() {
        return configuration.getThumbsEnabled()
                && (configuration.getThumbsDirectAccess()
                || isShowThumbs());
    }

    /**
     * checks show thumb request attribute.
     *
     * @return true if is set.
     */
    private boolean isShowThumbs() {
        return (this.showThumbs != null && this.showThumbs.equals("1"));
    }
}
