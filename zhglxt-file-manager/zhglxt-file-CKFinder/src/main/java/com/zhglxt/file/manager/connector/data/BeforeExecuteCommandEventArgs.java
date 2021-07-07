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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Event data for
 * {@link Events.EventTypes#BeforeExecuteCommand}
 * event.
 */
public class BeforeExecuteCommandEventArgs extends EventArgs {

    private String command;
    private HttpServletRequest request;
    private HttpServletResponse response;

    /**
     * @return the command
     */
    public final String getCommand() {
        return command;
    }

    /**
     * @param command the command to set
     */
    public final void setCommand(final String command) {
        this.command = command;
    }

    /**
     * @return the request
     */
    public final HttpServletRequest getRequest() {
        return request;
    }

    /**
     * @param request the request to set
     */
    public final void setRequest(final HttpServletRequest request) {
        this.request = request;
    }

    /**
     * @return the response
     */
    public final HttpServletResponse getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public final void setResponse(final HttpServletResponse response) {
        this.response = response;
    }
}
