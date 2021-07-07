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
package com.zhglxt.file.manager.connector.plugins;

import com.zhglxt.file.manager.connector.configuration.IConfiguration;
import com.zhglxt.file.manager.connector.data.PluginInfo;
import com.zhglxt.file.manager.connector.data.PluginParam;

import javax.servlet.ServletContext;

class WatermarkSettings {

    public static final String WATERMARK = "watermark";
    public static final String SOURCE = "source";
    public static final String TRANSPARENCY = "transparency";
    public static final String QUALITY = "quality";
    public static final String MARGIN_BOTTOM = "marginBottom";
    public static final String MARGIN_RIGHT = "marginRight";
    public static final String DEFULT_WATERMARK = "";
    private String source;
    private float transparency;
    private float quality;
    private int marginBottom;
    private int marginRight;

    public WatermarkSettings() {
        this.source = DEFULT_WATERMARK;
        this.marginRight = 0;
        this.marginBottom = 0;
        this.quality = 90;
        this.transparency = 1.0f;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the transparency
     */
    public float getTransparency() {
        return transparency;
    }

    /**
     * @param transparency the transparency to set
     */
    public void setTransprancy(float transparency) {
        this.transparency = transparency;
    }

    /**
     * @return the quality
     */
    public float getQuality() {
        return quality;
    }

    /**
     * @param quality the quality to set
     */
    public void setQuality(float quality) {
        this.quality = quality;
    }

    /**
     * @return the marginBottom
     */
    public int getMarginBottom() {
        return marginBottom;
    }

    /**
     * @param marginBottom the marginBottom to set
     */
    public void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }

    /**
     * @return the marginRight
     */
    public int getMarginRight() {
        return marginRight;
    }

    /**
     * @param marginRight the marginRight to set
     */
    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }

    /**
     * @param configuration
     * @param servletContext
     * @return
     * @throws Exception
     */
    public static WatermarkSettings createFromConfiguration(final IConfiguration configuration, ServletContext servletContext) throws Exception {
        WatermarkSettings settings = new WatermarkSettings();

        for (PluginInfo pluginInfo : configuration.getPlugins()) {
            if (WATERMARK.equals(pluginInfo.getName())) {
                for (PluginParam param : pluginInfo.getParams()) {
                    final String name = param.getName();
                    final String value = param.getValue();
                    if (SOURCE.equals(name)) {
                        settings.setSource(servletContext.getRealPath(value));
                    }
                    if (TRANSPARENCY.equals(name)) {
                        settings.setTransprancy(Float.parseFloat(value));
                    }
                    if (QUALITY.equals(name)) {
                        final int parseInt = Integer.parseInt(value);
                        final int name1 = parseInt % 101;
                        final float name2 = ((float) name1) / 100f;
                        settings.setQuality(name2);
                    }
                    if (MARGIN_BOTTOM.equals(name)) {
                        settings.setMarginBottom(Integer.parseInt(value));
                    }
                    if (MARGIN_RIGHT.equals(name)) {
                        settings.setMarginRight(Integer.parseInt(value));
                    }
                }
            }
        }
        return settings;
    }
}
