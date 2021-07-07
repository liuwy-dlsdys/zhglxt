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

import com.zhglxt.file.manager.connector.utils.PathUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Source code of <code>&lt;ckfinder:ckfinder /&gt;</code> tag.
 */
public class CKFinderTag extends TagSupport {

    /**
     *
     */
    private static final long serialVersionUID = -9024559933529738729L;
    /**
     *
     */
    private static final String CKFINDER_DEFAULT_BASEPATH = "/ckfinder/";
    private static final String CKFINDER_DEFAULT_PAGE = "ckfinder.html";
    private static final String DEFAULT_HEIGHT = "400";
    private static final String DEFAULT_WIDTH = "100%";
    private String basePath;
    private String width;
    private String height;
    private String selectFunction;
    private String selectFunctionData;
    private String selectThumbnailFunction;
    private String selectThumbnailFunctionData;
    private boolean disableThumbnailSelection;
    private String className;
    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    private String id;
    private String startupPath;
    private String resourceType;
    private boolean rememberLastFolder = true;
    private boolean startupFolderExpanded;

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        try {

            out.write("<iframe src=\"" + buildUrl() + "\" width=\"" + this.getWidth()
                    + "\" height=\"" + this.getHeight() + "\" " + this.getClassName()
                    + this.getId() + " frameborder=\"0\" scrolling=\"no\"></iframe>");
        } catch (Exception e) {
            try {
                HttpServletResponse resp = (HttpServletResponse) pageContext.getResponse();
                resp.reset();
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Problem with creating tag");
            } catch (IOException e1) {
                throw new JspException(e1);
            }
        }
        return EVAL_PAGE;
    }

    /**
     * build CKFinder URL.
     *
     * @return CKFinder URL.
     */
    private String buildUrl() {
        String url = getBasePath();
        String qs = "";

        if (isNullOrEmpty(url)) {
            url = CKFINDER_DEFAULT_BASEPATH;
        }
        url = PathUtils.addSlashToEnd(url);
        url = url.concat(CKFINDER_DEFAULT_PAGE);

        if (this.selectFunction != null && !this.selectFunction.equals("")) {
            qs += "?action=js&amp;func=" + this.selectFunction;
        }

        if (this.selectFunctionData != null && !this.selectFunctionData.equals("")) {
            qs += !isNullOrEmpty(qs) ? "&amp;" : "?";
            try {
                qs += "data=" + URLEncoder.encode(this.selectFunctionData, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                qs += "data=" + this.selectFunctionData;
            }
        }

        if (this.disableThumbnailSelection) {
            qs += !isNullOrEmpty(qs) ? "&amp;" : "?";
            qs += "dts=1";
        } else if (!isNullOrEmpty(this.selectThumbnailFunction)
                || !isNullOrEmpty(this.selectFunction)) {
            qs += !isNullOrEmpty(qs) ? "&amp;" : "?";
            qs += "thumbFunc=" + (!isNullOrEmpty(this.selectThumbnailFunction)
                    ? this.selectThumbnailFunction : this.selectFunction);
            if (!isNullOrEmpty(this.selectThumbnailFunctionData)) {
                try {
                    qs += "&amp;tdata="
                            + URLEncoder.encode(this.selectThumbnailFunctionData, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    qs += "&amp;tdata=" + this.selectThumbnailFunctionData;
                }
            } else if (isNullOrEmpty(this.selectThumbnailFunction)
                    && !isNullOrEmpty(this.selectFunctionData)) {
                try {
                    qs += "&amp;tdata="
                            + URLEncoder.encode(this.selectFunctionData, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    qs += "&amp;tdata=" + this.selectFunctionData;
                }

            }
        }

        if (!isNullOrEmpty(this.startupPath)) {
            qs += !isNullOrEmpty(qs) ? "&amp;" : "?";
            try {
                qs += "start="
                        + URLEncoder.encode(this.startupPath
                        + (this.startupFolderExpanded ? ":1" : ":0"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                qs += "start=" + (this.startupFolderExpanded ? ":1" : ":0");
            }
        }

        if (!isNullOrEmpty(this.resourceType)) {
            qs += !isNullOrEmpty(qs) ? "&amp;" : "?";
            try {
                qs += "type=" + URLEncoder.encode(this.resourceType, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                qs += "type=" + this.resourceType;
            }
        }

        if (this.rememberLastFolder) {
            qs += !isNullOrEmpty(qs) ? "&amp;" : "?";
            qs += "rlf=0";
        }

        if (!isNullOrEmpty(this.id)) {
            qs += !isNullOrEmpty(qs) ? "&amp;" : "?";
            try {
                qs += "id=" + URLEncoder.encode(this.id, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                qs += "id=" + this.id;
            }
        }
        return url.concat(qs);
    }

    /**
     * @return the basePath
     */
    public final String getBasePath() {
        return basePath;
    }

    /**
     * @param basePath the basePath to set
     */
    public final void setBasePath(final String basePath) {
        this.basePath = basePath;
    }

    /**
     * @return the width
     */
    public final String getWidth() {
        if (isNullOrEmpty(width)) {
            return DEFAULT_WIDTH;
        }
        return width;
    }

    /**
     * @param width the width to set
     */
    public final void setWidth(final String width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public final String getHeight() {
        if (isNullOrEmpty(height)) {
            return DEFAULT_HEIGHT;
        }
        return height;
    }

    /**
     * @param height the height to set
     */
    public final void setHeight(final String height) {
        this.height = height;
    }

    /**
     * @return the selectFunction
     */
    public final String getSelectFunction() {
        return selectFunction;
    }

    /**
     * @param selectFunction the selectFunction to set
     */
    public final void setSelectFunction(final String selectFunction) {
        this.selectFunction = selectFunction;
    }

    /**
     * @return the selectFunctionData
     */
    public final String getSelectFunctionData() {
        return selectFunctionData;
    }

    /**
     * @param selectFunctionData the selectFunctionData to set
     */
    public final void setSelectFunctionData(final String selectFunctionData) {
        this.selectFunctionData = selectFunctionData;
    }

    /**
     * @return the selectThumbnailFunction
     */
    public final String getSelectThumbnailFunction() {
        return selectThumbnailFunction;
    }

    /**
     * @param selectThumbnailFunction the selectThumbnailFunction to set
     */
    public final void setSelectThumbnailFunction(final String selectThumbnailFunction) {
        this.selectThumbnailFunction = selectThumbnailFunction;
    }

    /**
     * @return the selectThumbnailFunctionData
     */
    public final String getSelectThumbnailFunctionData() {
        return selectThumbnailFunctionData;
    }

    /**
     * @param selectThumbnailFunctionData the selectThumbnailFunctionData to set
     */
    public final void setSelectThumbnailFunctionData(
            final String selectThumbnailFunctionData) {
        this.selectThumbnailFunctionData = selectThumbnailFunctionData;
    }

    /**
     * @return the disableThumbnailSelection
     */
    public final boolean isDisableThumbnailSelection() {
        return disableThumbnailSelection;
    }

    /**
     * @param disableThumbnailSelection the disableThumbnailSelection to set
     */
    public final void setDisableThumbnailSelection(
            final boolean disableThumbnailSelection) {
        this.disableThumbnailSelection = disableThumbnailSelection;
    }

    /**
     * @return the className
     */
    public final String getClassName() {
        if (!isNullOrEmpty(className)) {
            return " class=\"" + className + "\"";
        } else {
            return "";
        }
    }

    /**
     * @param className the className to set
     */
    public final void setClassName(final String className) {
        this.className = className;
    }

    /**
     * @return the id
     */
    @Override
    public final String getId() {
        if (!isNullOrEmpty(id)) {
            return " id=\"" + id + "\"";
        } else {
            return "";
        }
    }

    /**
     * @param id the id to set
     */
    @Override
    public final void setId(final String id) {
        this.id = id;
    }

    /**
     * @return the startupPath
     */
    public final String getStartupPath() {
        return startupPath;
    }

    /**
     * @param startupPath the startupPath to set
     */
    public final void setStartupPath(final String startupPath) {
        this.startupPath = startupPath;
    }

    /**
     * @return the resourceType
     */
    public final String getResourceType() {
        return resourceType;
    }

    /**
     * @param resourceType the resourceType to set
     */
    public final void setResourceType(final String resourceType) {
        this.resourceType = resourceType;
    }

    /**
     * @return the rememberLastFolder
     */
    public final boolean isRememberLastFolder() {
        return rememberLastFolder;
    }

    /**
     * @param rememberLastFolder the rememberLastFolder to set
     */
    public final void setRememberLastFolder(final boolean rememberLastFolder) {
        this.rememberLastFolder = rememberLastFolder;
    }

    /**
     * @return the startupFolderExpanded
     */
    public final boolean isStartupFolderExpanded() {
        return startupFolderExpanded;
    }

    /**
     * @param startupFolderExpanded the startupFolderExpanded to set
     */
    public final void setStartupFolderExpanded(final boolean startupFolderExpanded) {
        this.startupFolderExpanded = startupFolderExpanded;
    }

    /**
     * checks if string is null or empty.
     *
     * @param string input string
     * @return true if is empty or null
     */
    private static boolean isNullOrEmpty(final String string) {
        return (string == null || string.equals(""));
    }
}
