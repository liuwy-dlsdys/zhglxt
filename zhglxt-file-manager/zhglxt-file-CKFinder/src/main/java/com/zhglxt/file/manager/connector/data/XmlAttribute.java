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

/**
 * XML attribute.
 */
public class XmlAttribute {

    /**
     *
     */
    private String key;
    /**
     *
     */
    private String value;

    /**
     * Constructor.
     *
     * @param key1   XML attribute key.
     * @param value1 XML attribute value.
     */
    public XmlAttribute(final String key1, final String value1) {
        super();
        this.key = key1;
        this.value = value1;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key1 the key to set
     */
    public void setKey(final String key1) {
        this.key = key1;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value1 the value to set
     */
    public void setValue(final String value1) {
        this.value = value1;
    }
}
