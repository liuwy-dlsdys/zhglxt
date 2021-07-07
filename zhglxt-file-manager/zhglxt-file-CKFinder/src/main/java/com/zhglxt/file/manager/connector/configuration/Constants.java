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
package com.zhglxt.file.manager.connector.configuration;

/**
 * Class holding constants used by the CKFinder connector.
 */
public class Constants {

    /**
     * Regular expression to find images.
     */
    public static final String CKFINDER_REGEX_IMAGES_EXT = "([^\\.]+(\\.(?i)(jpg|png|gif|bmp))$)";
    /**
     * Regular expression to identify invalid characters in file name.
     */
    public static final String INVALID_FILE_NAME_REGEX = "\\p{Cntrl}|[/\\\\\\:\\*\\?\"\\<\\>\\|]";
    /**
     * Regular expression to identify invalid characters in path.
     */
    public static final String INVALID_PATH_REGEX = "(/\\.|\\p{Cntrl}|//|\\\\|[:*?<>\"\\|])";
    /**
     * Regular expression to identify full URL.
     */
    public static final String URL_REGEX = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    public static final String CKFINDER_CHARS = "123456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    /**
     * Regular expression to find default language code in CKFinder.
     */
    public static final String DEFAULT_LANG_CODE = "en";
    /**
     * Regular expression to find baseDir configuration property placeholder.
     */
    public static final String BASE_DIR_PLACEHOLDER = "%BASE_DIR%";
    /**
     * Regular expression to find baseUrl configuration property placeholder.
     */
    public static final String BASE_URL_PLACEHOLDER = "%BASE_URL%";

    /**
     * Class holding CKFinder error codes.
     */
    public static class Errors {

        public static final int CKFINDER_CONNECTOR_ERROR_NONE = 0;
        public static final int CKFINDER_CONNECTOR_ERROR_CUSTOM_ERROR = 1;
        public static final int CKFINDER_CONNECTOR_ERROR_INVALID_COMMAND = 10;
        public static final int CKFINDER_CONNECTOR_ERROR_TYPE_NOT_SPECIFIED = 11;
        public static final int CKFINDER_CONNECTOR_ERROR_INVALID_TYPE = 12;
        public static final int CKFINDER_CONNECTOR_ERROR_INVALID_NAME = 102;
        public static final int CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED = 103;
        public static final int CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED = 104;
        public static final int CKFINDER_CONNECTOR_ERROR_INVALID_EXTENSION = 105;
        public static final int CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST = 109;
        public static final int CKFINDER_CONNECTOR_ERROR_UNKNOWN = 110;
        public static final int CKFINDER_CONNECTOR_ERROR_ALREADY_EXIST = 115;
        public static final int CKFINDER_CONNECTOR_ERROR_FOLDER_NOT_FOUND = 116;
        public static final int CKFINDER_CONNECTOR_ERROR_FILE_NOT_FOUND = 117;
        public static final int CKFINDER_CONNECTOR_ERROR_SOURCE_AND_TARGET_PATH_EQUAL = 118;
        public static final int CKFINDER_CONNECTOR_ERROR_UPLOADED_FILE_RENAMED = 201;
        public static final int CKFINDER_CONNECTOR_ERROR_UPLOADED_INVALID = 202;
        public static final int CKFINDER_CONNECTOR_ERROR_UPLOADED_TOO_BIG = 203;
        public static final int CKFINDER_CONNECTOR_ERROR_UPLOADED_CORRUPT = 204;
        public static final int CKFINDER_CONNECTOR_ERROR_UPLOADED_NO_TMP_DIR = 205;
        public static final int CKFINDER_CONNECTOR_ERROR_UPLOADED_WRONG_HTML_FILE = 206;
        public static final int CKFINDER_CONNECTOR_ERROR_MOVE_FAILED = 300;
        public static final int CKFINDER_CONNECTOR_ERROR_COPY_FAILED = 301;
        public static final int CKFINDER_CONNECTOR_ERROR_DELETE_FAILED = 302;
        public static final int CKFINDER_CONNECTOR_ERROR_UPLOADED_INVALID_NAME_RENAMED = 207;
        public static final int CKFINDER_CONNECTOR_ERROR_CONNECTOR_DISABLED = 500;
        public static final int CKFINDER_CONNECTOR_ERROR_THUMBNAILS_DISABLED = 501;
    }
}