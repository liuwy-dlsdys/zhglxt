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
package com.zhglxt.file.manager.connector.errors;

import com.zhglxt.file.manager.connector.configuration.Constants;

/**
 * Connector Exception.
 */
public class ConnectorException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -8643752550259111562L;
    private int errorCode;
    private String errorMsg;
    private boolean addCurrentFolder;
    private Exception exception;

    /**
     * standard constructor.
     *
     * @param errorCode error code number
     */
    public ConnectorException(final int errorCode) {
        super();
        addCurrentFolder = true;
        this.errorCode = errorCode;
    }

    /**
     * standard constructor.
     *
     * @param addCurrentFolder add current node flag
     * @param errorCode        error code number
     */
    public ConnectorException(final int errorCode, final boolean addCurrentFolder) {
        this(errorCode);
        this.addCurrentFolder = addCurrentFolder;
    }

    /**
     * constructor with error code and error message parameters.
     *
     * @param errorCode error code number
     * @param errorMsg  error text message
     */
    public ConnectorException(final int errorCode, final String errorMsg) {
        this(errorCode);
        this.errorMsg = errorMsg;
    }

    /**
     * constructor with error code and error message parameters.
     *
     * @param errorCode error code number
     * @param e         exception
     */
    public ConnectorException(final int errorCode, final Exception e) {
        this(errorCode);
        this.exception = e;
        addCurrentFolder = false;
        this.errorMsg = e.getMessage();
    }

    /**
     * constructor with exception param.
     *
     * @param e Exception
     */
    public ConnectorException(final Exception e) {
        this.exception = e;
        if (e instanceof ConnectorException) {
            errorCode = ((ConnectorException) e).getErrorCode();
            addCurrentFolder = ((ConnectorException) e).addCurrentFolder;
        } else {
            addCurrentFolder = false;
            errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNKNOWN;
            this.errorMsg = e.getMessage();
        }
    }

    /**
     * gets error code.
     *
     * @return error code.
     */
    public int getErrorCode() {
        return this.errorCode;
    }

    /**
     * gets error message.
     *
     * @return error message.
     */
    public String getErrorMessage() {
        return this.errorMsg;
    }

    /**
     * add current folder node.
     *
     * @return true if add current folder node
     */
    public boolean isAddCurrentFolder() {
        return addCurrentFolder;
    }

    /**
     * @return the errorMsg
     */
    public final String getErrorMsg() {
        return errorMsg;
    }

    /**
     * @param errorMsg the errorMsg to set
     */
    public final void setErrorMsg(final String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * @return the exception
     */
    public final Exception getException() {
        return exception;
    }

    /**
     * @param exception the exception to set
     */
    public final void setException(final Exception exception) {
        this.exception = exception;
    }
}
