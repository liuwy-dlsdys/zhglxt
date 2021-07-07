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
package com.zhglxt.file.manager.connector;

import com.zhglxt.file.manager.connector.configuration.Constants;
import com.zhglxt.file.manager.connector.errors.ConnectorException;

import javax.servlet.ServletContext;

/**
 * Access to servlet context outside from servlet.
 */
public class ServletContextFactory {

    /**
     * ServletContext object.
     */
    private static ServletContext servletContext;

    /**
     * sets servlet context.
     *
     * @param servletContext1 context
     */
    static void setServletContext(final ServletContext servletContext1) {
        servletContext = servletContext1;
    }

    /**
     * Returns {@code ServletContext} instance.
     *
     * @return current {@code ServletContext} object
     * @throws ConnectorException when {@code ServletContext} is {@code null}.
     */
    public static ServletContext getServletContext() throws ConnectorException {
        if (servletContext != null) {
            return servletContext;
        } else {
            throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNKNOWN,
                    "Servlet contex is null. Try to restart server.");
        }

    }
}
