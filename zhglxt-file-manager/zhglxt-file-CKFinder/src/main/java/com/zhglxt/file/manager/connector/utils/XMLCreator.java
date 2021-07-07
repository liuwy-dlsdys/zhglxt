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
package com.zhglxt.file.manager.connector.utils;

import com.zhglxt.file.manager.connector.configuration.Constants;
import com.zhglxt.file.manager.connector.errors.ConnectorException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to create XML document.
 */
public class XMLCreator {

    /**
     * dom4j document.
     */
    private Document document;
    /**
     * errors list.
     */
    private List<ErrorNode> errorList;

    /**
     * Creates document.
     *
     * @throws ConnectorException if a DocumentBuilder cannot be created which satisfies the configuration requested.
     */
    @SuppressWarnings("UseSpecificCatch")
    public void createDocument() throws ConnectorException {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.newDocument();
            document.setXmlStandalone(true);
        } catch (Exception e) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED, e);
        }

    }

    /**
     * standard constructor.
     */
    public XMLCreator() {
        this.errorList = new ArrayList<ErrorNode>();
    }

    /**
     * gets document.
     *
     * @return document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * @return XML as text
     * @throws ConnectorException If an unrecoverable error occurs during the course of the transformation or when it is not possible to
     *                            create a Transformer instance.
     */
    public String getDocumentAsText() throws ConnectorException {
        try {
            StringWriter stw = new StringWriter();
            Transformer serializer = TransformerFactory.newInstance().newTransformer();
            serializer.transform(new DOMSource(document), new StreamResult(stw));
            return stw.toString();
        } catch (TransformerException e) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED, e);
        }

    }

    /**
     * adds error node to root element with error code.
     *
     * @param rootElement XML root node.
     * @param errorNum    error code number.
     * @param errorText   error text.
     */
    public void addErrorCommandToRoot(final Element rootElement,
                                      final int errorNum, final String errorText) {
        // errors
        Element element = this.getDocument().createElement("Error");
        element.setAttribute("number", String.valueOf(errorNum));
        if (errorText != null) {
            element.setTextContent(errorText);
        }
        rootElement.appendChild(element);
    }

    /**
     * save errors node to list.
     *
     * @param errorCode error code
     * @param name      file name
     * @param path      current folder
     * @param type      resource type
     */
    public void appendErrorNodeChild(final int errorCode, final String name,
                                     final String path, final String type) {
        ErrorNode errorNode = new ErrorNode(path, type, name, errorCode);
        errorList.add(errorNode);
    }

    /**
     * add all error nodes from saved list to xml.
     *
     * @param errorsNode XML errors node
     */
    public void addErrors(final Element errorsNode) {
        for (ErrorNode item : this.errorList) {
            Element childElem = this.getDocument().createElement("Error");
            childElem.setAttribute("code", String.valueOf(item.errorCode));
            childElem.setAttribute("name", item.name);
            childElem.setAttribute("type", item.type);
            childElem.setAttribute("folder", item.folder);
            errorsNode.appendChild(childElem);
        }
    }

    /**
     * error node object.
     */
    private class ErrorNode {

        private String folder;
        private String type;
        private String name;
        private int errorCode;

        /**
         * @param folder    folder param
         * @param type      resource type param
         * @param name      file name param
         * @param errorCode error code param
         */
        public ErrorNode(final String folder, final String type,
                         final String name, final int errorCode) {
            super();
            this.folder = folder;
            this.type = type;
            this.name = name;
            this.errorCode = errorCode;
        }
    }

    /**
     * checks if error list contains errors.
     *
     * @return true if there are any errors.
     */
    public boolean hasErrors() {
        return !errorList.isEmpty();
    }
}
