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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.HashMap;
import java.util.Map;

/**
 * Source code of
 * <code>&lt;ckfinder:setupCKEditor /&gt;</code> tag.
 */
public class CKFinderSetupCKEditor extends TagSupport {

    private static final String CKFINDER_UPLOAD_URL = "/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=";
    private static final String CKFINDER_PAGE = "/ckfinder.html";
    private static final String DEFAULT_IMAGE_TYPE = "Images";
    private static final String DEFAULT_FLASH_TYPE = "Flash";
    /**
     *
     */
    private static final long serialVersionUID = 3947714242365900445L;
    private String basePath;
    private String editor;
    private String imageType;
    private String flashType;

    @Override
    public int doStartTag() throws JspException {
        if (imageType == null || imageType.equals("")) {
            imageType = DEFAULT_IMAGE_TYPE;
        }
        if (flashType == null || flashType.equals("")) {
            flashType = DEFAULT_FLASH_TYPE;
        }
        Map<String, Map<String, String>> attr = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("filebrowserBrowseUrl", buildBrowseUrl(null));
        params.put("filebrowserUploadUrl", buildUploadUrl("Files"));
        params.put("filebrowserImageBrowseUrl", buildBrowseUrl(imageType));
        params.put("filebrowserImageUploadUrl", buildUploadUrl(imageType));
        params.put("filebrowserFlashBrowseUrl", buildBrowseUrl(flashType));
        params.put("filebrowserFlashUploadUrl", buildUploadUrl(flashType));

        if (editor == null || editor.equals("")) {
            attr.put("*", params);
        } else {
            attr.put(editor, params);
        }
        pageContext.setAttribute("ckeditor-params", attr);
        return EVAL_PAGE;
    }

    /**
     * Builds browse url by type and basePath.
     *
     * @param type current type
     * @return full browse url.
     */
    private String buildBrowseUrl(final String type) {
        return PathUtils.escape(basePath.concat(CKFINDER_PAGE).concat(type == null ? "" : "?type=".concat(type)));
    }

    /**
     * Builds upload URL by type.
     *
     * @param type current type.
     * @return full CKFinder upload URL.
     */
    private String buildUploadUrl(final String type) {
        if (pageContext.getRequest() instanceof HttpServletRequest) {
            return ((HttpServletRequest) pageContext.getRequest()).getContextPath().concat(CKFINDER_UPLOAD_URL).concat(type);
        } else {
            return CKFINDER_UPLOAD_URL.concat(type);
        }

    }

    /**
     * @param basePath the basePath to set
     */
    public final void setBasePath(final String basePath) {
        this.basePath = basePath;
    }

    /**
     * @param imageType the imageType to set
     */
    public final void setImageType(final String imageType) {
        this.imageType = imageType;
    }

    /**
     * @param flashType the flashType to set
     */
    public final void setFlashType(final String flashType) {
        this.flashType = flashType;
    }

    /**
     * @param editor the editor to set
     */
    public final void setEditor(final String editor) {
        this.editor = editor;
    }
}
