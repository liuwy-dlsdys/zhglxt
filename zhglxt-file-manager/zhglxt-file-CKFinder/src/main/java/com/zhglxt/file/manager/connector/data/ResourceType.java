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

import com.zhglxt.file.manager.connector.configuration.Constants;

/**
 * Resource type entity.
 */
public class ResourceType {

    /**
     * bytes in KB.
     */
    private static final int BYTES = 1024;
    /**
     * resource name.
     */
    private String name;
    /**
     * resource url.
     */
    private String url;
    /**
     * resource directory.
     */
    private String path;
    /**
     * max file size in resource.
     */
    private String maxSize;
    /**
     * list of allowed extensions in resource (separated with comma).
     */
    private String allowedExtensions;
    /**
     * list of denied extensions in resource (separated with comma).
     */
    private String deniedExtensions;

    /**
     *
     */
    /**
     * Constructor.
     *
     * @param name              resource type name.
     * @param url               resource type url.
     * @param directory         resource type directory.
     * @param maxSize           Max size of file in the resource type.
     * @param allowedExtensions allowed extensions for resource type.
     * @param deniedExtensions  denied extensions for resource type.
     */
    public ResourceType(final String name, final String url,
                        final String directory, final String maxSize,
                        final String allowedExtensions,
                        final String deniedExtensions) {
        this.allowedExtensions = allowedExtensions;
        this.deniedExtensions = deniedExtensions;
        this.path = directory;
        this.maxSize = maxSize;
        this.name = name;
        this.url = url;
    }

    /**
     * constructor.
     *
     * @param name type name
     */
    public ResourceType(final String name) {
        this.name = name;
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
     * @return the url
     */
    public final String getUrl() {
        if (url == null) {
            return Constants.BASE_URL_PLACEHOLDER.concat("/").concat(this.name.toLowerCase()).concat("/");
        }
        return url;
    }

    /**
     * @param url the url to set
     */
    public final void setUrl(final String url) {
        this.url = url;
    }

    /**
     * @return the directory
     */
    public final String getPath() {
        if (path == null) {
            return Constants.BASE_DIR_PLACEHOLDER.concat(this.name.toLowerCase()).concat("/");
        }
        return path;
    }

    /**
     * @param directory the directory to set
     */
    public final void setPath(final String directory) {
        this.path = directory;
    }

    /**
     * @return the maxSize
     */
    public final Long getMaxSize() {
        try {
            //No XML node, no value, value equals 0 = no resource type maxSize
            if (maxSize == null
                    || maxSize.equals("")
                    || maxSize.equals("0")) {
                return null;
            }
            return parseMaxSize();
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * parses max size value from config (ex. 16M to number of bytes).
     *
     * @return number of bytes in max size.
     */
    private long parseMaxSize() {

        char lastChar = maxSize.toLowerCase().charAt(maxSize.length() - 1);
        int a;
        switch (lastChar) {
            case 'k':
                a = BYTES;
                break;
            case 'm':
                a = BYTES * BYTES;
                break;
            case 'g':
                a = BYTES * BYTES * BYTES;
                break;
            default:
                return 0;
        }
        long value = Long.valueOf(maxSize.substring(0, maxSize.length() - 1));
        return value * a;

    }

    /**
     * @param maxSize the maxSize to set
     */
    public final void setMaxSize(final String maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * @return the allowedExtensions
     */
    public final String getAllowedExtensions() {
        if (allowedExtensions == null) {
            return "";
        }
        return allowedExtensions;
    }

    /**
     * @param allowedExtensions the allowedExtensions to set
     */
    public final void setAllowedExtensions(final String allowedExtensions) {
        this.allowedExtensions = allowedExtensions;
    }

    /**
     * @return the deniedExtensions
     */
    public final String getDeniedExtensions() {
        if (deniedExtensions == null) {
            return "";
        }
        return deniedExtensions;
    }

    /**
     * @param deniedExtensions the deniedExtensions to set
     */
    public final void setDeniedExtensions(final String deniedExtensions) {
        this.deniedExtensions = deniedExtensions;
    }

    /**
     * clone constructor.
     *
     * @param type source type
     */
    public ResourceType(final ResourceType type) {
        super();
        this.name = type.name;
        this.url = type.url;
        this.path = type.path;
        this.maxSize = type.maxSize;
        this.allowedExtensions = type.allowedExtensions;
        this.deniedExtensions = type.deniedExtensions;
    }
}
