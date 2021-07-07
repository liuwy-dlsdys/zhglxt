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

import java.util.List;

/**
 *
 */
public class PluginInfo {

    private String name;
    private String className;
    private List<PluginParam> params;
    private boolean enabled;
    private boolean internal;

    /**
     * @return the enabled
     */
    public final boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public final void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the internal
     */
    public final boolean isInternal() {
        return internal;
    }

    /**
     * @param internal the internal to set
     */
    public final void setInternal(final boolean internal) {
        this.internal = internal;
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
     * @return the className
     */
    public final String getClassName() {
        return className;
    }

    /**
     * @param className the className to set
     */
    public final void setClassName(final String className) {
        this.className = className;
    }

    /**
     * @return the params
     */
    public final List<PluginParam> getParams() {
        return params;
    }

    /**
     * @param params the params to set
     */
    public final void setParams(final List<PluginParam> params) {
        this.params = params;
    }

    /**
     * copy constructor.
     *
     * @param info source plugin info
     */
    public PluginInfo(final PluginInfo info) {
        super();
        this.name = info.name;
        this.className = info.className;
        this.params = info.params;
        this.enabled = info.enabled;
        this.internal = info.internal;
    }

    /**
     * standard constructor.
     */
    public PluginInfo() {
    }
}
