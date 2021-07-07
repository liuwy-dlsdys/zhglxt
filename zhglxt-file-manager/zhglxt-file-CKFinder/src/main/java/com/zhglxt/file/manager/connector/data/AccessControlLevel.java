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
package com.zhglxt.file.manager.connector.data;

/**
 * Access control level entity.
 */
public class AccessControlLevel {

    /**
     *
     */
    private String role;
    /**
     *
     */
    private String resourceType;
    /**
     *
     */
    private String folder;
    /**
     *
     */
    private boolean folderView;
    /**
     *
     */
    private boolean folderCreate;
    /**
     *
     */
    private boolean folderRename;
    /**
     *
     */
    private boolean folderDelete;
    /**
     *
     */
    private boolean fileView;
    /**
     *
     */
    private boolean fileUpload;
    /**
     *
     */
    private boolean fileRename;
    /**
     *
     */
    private boolean fileDelete;

    /**
     * @return the role
     */
    public final String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public final void setRole(final String role) {
        this.role = role;
    }

    /**
     * @return the resourceType
     */
    public final String getResourceType() {
        return resourceType;
    }

    /**
     * @param resourceType the resourceType to set
     */
    public final void setResourceType(final String resourceType) {
        this.resourceType = resourceType;
    }

    /**
     * @return the folder
     */
    public final String getFolder() {
        return folder;
    }

    /**
     * @param folder the folder to set
     */
    public final void setFolder(final String folder) {
        this.folder = folder;
    }

    /**
     * @return the folderView
     */
    public final boolean isFolderView() {
        return folderView;
    }

    /**
     * @param folderView the folderView to set
     */
    public final void setFolderView(final boolean folderView) {
        this.folderView = folderView;
    }

    /**
     * @return the folderCreate
     */
    public final boolean isFolderCreate() {
        return folderCreate;
    }

    /**
     * @param folderCreate the folderCreate to set
     */
    public final void setFolderCreate(final boolean folderCreate) {
        this.folderCreate = folderCreate;
    }

    /**
     * @return the folderRename
     */
    public final boolean isFolderRename() {
        return folderRename;
    }

    /**
     * @param folderRename the folderRename to set
     */
    public final void setFolderRename(final boolean folderRename) {
        this.folderRename = folderRename;
    }

    /**
     * @return the folderDelete
     */
    public final boolean isFolderDelete() {
        return folderDelete;
    }

    /**
     * @param folderDelete the folderDelete to set
     */
    public final void setFolderDelete(final boolean folderDelete) {
        this.folderDelete = folderDelete;
    }

    /**
     * @return the fileView
     */
    public final boolean isFileView() {
        return fileView;
    }

    /**
     * @param fileView the fileView to set
     */
    public final void setFileView(final boolean fileView) {
        this.fileView = fileView;
    }

    /**
     * @return the fileUpload
     */
    public final boolean isFileUpload() {
        return fileUpload;
    }

    /**
     * @param fileUpload the fileUpload to set
     */
    public final void setFileUpload(final boolean fileUpload) {
        this.fileUpload = fileUpload;
    }

    /**
     * @return the fileRename
     */
    public final boolean isFileRename() {
        return fileRename;
    }

    /**
     * @param fileRename the fileRename to set
     */
    public final void setFileRename(final boolean fileRename) {
        this.fileRename = fileRename;
    }

    /**
     * @return the fileDelete
     */
    public final boolean isFileDelete() {
        return fileDelete;
    }

    /**
     * @param fileDelete the fileDelete to set
     */
    public final void setFileDelete(final boolean fileDelete) {
        this.fileDelete = fileDelete;
    }

    /**
     * copy constructor.
     *
     * @param acl source acl
     */
    public AccessControlLevel(final AccessControlLevel acl) {
        this.role = acl.role;
        this.resourceType = acl.resourceType;
        this.folder = acl.folder;
        this.folderView = acl.folderView;
        this.folderCreate = acl.folderCreate;
        this.folderRename = acl.folderRename;
        this.folderDelete = acl.folderDelete;
        this.fileView = acl.fileView;
        this.fileUpload = acl.fileUpload;
        this.fileRename = acl.fileRename;
        this.fileDelete = acl.fileDelete;
    }

    /**
     * standard constructor.
     */
    public AccessControlLevel() {
    }
}
