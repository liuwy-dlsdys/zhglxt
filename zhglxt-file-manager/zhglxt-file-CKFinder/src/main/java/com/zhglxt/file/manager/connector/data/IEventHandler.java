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

import com.zhglxt.file.manager.connector.configuration.IConfiguration;
import com.zhglxt.file.manager.connector.errors.ConnectorException;

/**
 * Plugin event interface.
 */
public interface IEventHandler {

    /**
     * execute event handler.
     *
     * @param args          params for event handler.
     * @param configuration connector configuration
     * @return false if break executing command.
     * @throws ConnectorException when error occurs.
     */
    public boolean runEventHandler(EventArgs args, IConfiguration configuration)
            throws ConnectorException;
}
