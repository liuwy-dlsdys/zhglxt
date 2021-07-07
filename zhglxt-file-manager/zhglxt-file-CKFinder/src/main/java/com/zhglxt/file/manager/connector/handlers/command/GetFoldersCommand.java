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
import com.zhglxt.file.manager.connector.data.XmlAttribute;
import com.zhglxt.file.manager.connector.data.XmlElementData;
import com.zhglxt.file.manager.connector.errors.ConnectorException;
import com.zhglxt.file.manager.connector.utils.AccessControlUtil;
import com.zhglxt.file.manager.connector.utils.FileUtils;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to handle
 * <code>GetFolders</code> command.
 */
public class GetFoldersCommand extends XMLCommand {

    /**
     * list of subdirectories in directory.
     */
    private List<String> directories;

    @Override
    protected void createXMLChildNodes(final int errorNum, final Element rootElement)
            throws ConnectorException {

        if (errorNum == Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE) {
            createFoldersData(rootElement);
        }
    }

    /**
     * gets data for response.
     *
     * @return 0 if everything went ok or error code otherwise
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
                AccessControlUtil.CKFINDER_CONNECTOR_ACL_FOLDER_VIEW)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
        }
        if (FileUtils.checkIfDirIsHidden(this.currentFolder, configuration)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
        }

        File dir = new File(configuration.getTypes().get(this.type).getPath()
                + this.currentFolder);
        try {
            if (!dir.exists()) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_FOLDER_NOT_FOUND;
            }

            directories = FileUtils.findChildrensList(dir, true);
        } catch (SecurityException e) {
            if (configuration.isDebugMode()) {
                throw e;
            } else {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
            }
        }
        filterListByHiddenAndNotAllowed();
        Collections.sort(directories);
        return Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE;
    }

    /**
     * filters list and check if every element is not hidden and have correct
     * ACL.
     */
    private void filterListByHiddenAndNotAllowed() {
        List<String> tmpDirs = new ArrayList<String>();
        for (String dir : this.directories) {
            if (AccessControlUtil.getInstance().checkFolderACL(this.type, this.currentFolder + dir,
                    this.userRole,
                    AccessControlUtil.CKFINDER_CONNECTOR_ACL_FOLDER_VIEW)
                    && !FileUtils.checkIfDirIsHidden(dir, this.configuration)) {
                tmpDirs.add(dir);
            }

        }

        this.directories.clear();
        this.directories.addAll(tmpDirs);

    }

    /**
     * creates folder data node in XML document.
     *
     * @param rootElement root element in XML document
     */
    private void createFoldersData(final Element rootElement) {
        Element element = creator.getDocument().createElement("Folders");
        for (String dirPath : directories) {
            File dir = new File(this.configuration.getTypes().get(this.type).getPath()
                    + this.currentFolder
                    + dirPath);
            if (dir.exists()) {
                XmlElementData xmlElementData = new XmlElementData("Folder");
                xmlElementData.getAttributes().add(new XmlAttribute("name", dirPath));

                xmlElementData.getAttributes().add(new XmlAttribute("hasChildren",
                        FileUtils.hasChildren(this.currentFolder + dirPath + "/", dir, configuration, this.type, this.userRole).toString()));


                xmlElementData.getAttributes().add(new XmlAttribute("acl",
                        String.valueOf(AccessControlUtil.getInstance().checkACLForRole(this.type,
                                this.currentFolder
                                        + dirPath,
                                this.userRole))));
                xmlElementData.addToDocument(creator.getDocument(), element);
            }

        }
        rootElement.appendChild(element);
    }
}
