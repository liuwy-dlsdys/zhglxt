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
 * File from param entity.
 */
public class FilePostParam {

    /**
     *
     */
    private String folder;
    /**
     *
     */
    private String name;
    /**
     *
     */
    private String options;
    /**
     *
     */
    private String type;

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
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the options
     */
    public final String getOptions() {
        return options;
    }

    /**
     * @param options the options to set
     */
    public final void setOptions(final String options) {
        this.options = options;
    }

    /**
     * @return the type
     */
    public final String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public final void setType(final String type) {
        this.type = type;
    }
}
