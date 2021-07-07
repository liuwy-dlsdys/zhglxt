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

import com.zhglxt.file.manager.connector.data.PluginInfo;

/**
 * Base class for plugins.
 */
public abstract class Plugin {

    protected PluginInfo pluginInfo;

    /**
     * register event handlers for plugin.
     *
     * @param eventHandler available event handlers.
     */
    public abstract void registerEventHandlers(Events eventHandler);

    /**
     * @param pluginInfo the pluginInfo to set
     */
    public final void setPluginInfo(final PluginInfo pluginInfo) {
        this.pluginInfo = pluginInfo;
    }
}
