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

import com.zhglxt.file.manager.connector.configuration.Events;

import java.io.File;

/**
 * Event data for {@link Events.EventTypes#AfterFileUpload}
 * event.
 */
public class AfterFileUploadEventArgs extends EventArgs {

    private String currentFolder;
    private byte[] fileContent;
    private File file;

    /**
     * @return the currentFolder
     */
    public final String getCurrentFolder() {
        return currentFolder;
    }

    /**
     * @param currentFolder the currentFolder to set
     */
    public final void setCurrentFolder(final String currentFolder) {
        this.currentFolder = currentFolder;
    }

    /**
     * @return the fileContent
     */
    public final byte[] getFileContent() {
        return fileContent;
    }

    /**
     * @param fileContent the fileContent to set
     */
    public final void setFileContent(final byte[] fileContent) {
        this.fileContent = fileContent;
    }

    /**
     * @return the file
     */
    public final File getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public final void setFile(final File file) {
        this.file = file;
    }
}
