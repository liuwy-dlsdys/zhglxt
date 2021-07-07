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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * XML element.
 */
public class XmlElementData {

    /**
     * node name.
     */
    private String name;
    /**
     * node text value.
     */
    private String value;
    /**
     * list of attributes of node.
     */
    private List<XmlAttribute> attributes;
    /**
     * list of child nodes.
     */
    private List<XmlElementData> childrens;

    /**
     * Constructor.
     *
     * @param name XML element name.
     */
    public XmlElementData(final String name) {
        super();
        this.name = name;
        this.attributes = new ArrayList<XmlAttribute>();
        this.childrens = new ArrayList<XmlElementData>();
    }

    /**
     * Adds element to the document.
     *
     * @param document XML document.
     * @param parent   Parent node for element.
     */
    public void addToDocument(final Document document,
                              final Element parent) {
        Element element = this.toElement(document);
        for (XmlElementData xmlElementData : this.childrens) {
            element.appendChild(xmlElementData.toElement(document));
        }
        for (XmlAttribute attribute : this.attributes) {
            element.setAttribute(attribute.getKey(), attribute.getValue());
        }
        if (parent != null) {
            parent.appendChild(element);
        } else {
            document.appendChild(element);
        }

    }

    /**
     * Creates this element in XML document.
     *
     * @param document XML document
     * @return creates a element in document
     */
    private Element toElement(final Document document) {
        return document.createElement(this.name);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * @return the attributes
     */
    public List<XmlAttribute> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(final List<XmlAttribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the children
     */
    public List<XmlElementData> getChildrens() {
        return childrens;
    }

    /**
     * @param childrens the children to set
     */
    public void setChildrens(final List<XmlElementData> childrens) {
        this.childrens = childrens;
    }
}
