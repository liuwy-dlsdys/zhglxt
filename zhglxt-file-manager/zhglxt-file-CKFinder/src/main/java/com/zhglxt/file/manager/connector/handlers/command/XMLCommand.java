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
package com.zhglxt.file.manager.connector.handlers.command;

import com.zhglxt.file.manager.connector.configuration.Constants;
import com.zhglxt.file.manager.connector.configuration.IConfiguration;
import com.zhglxt.file.manager.connector.errors.ConnectorException;
import com.zhglxt.file.manager.connector.utils.AccessControlUtil;
import com.zhglxt.file.manager.connector.utils.XMLCreator;
import org.w3c.dom.Element;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Base class to handle XML commands.
 */
public abstract class XMLCommand extends Command {

    /**
     * util to create XML document.
     */
    protected XMLCreator creator;

    /**
     * sets response headers for XML response.
     *
     * @param response response
     * @param sc       servlet context
     */
    @Override
    public void setResponseHeader(final HttpServletResponse response,
                                  final ServletContext sc) {
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("utf-8");
    }

    /**
     * executes XML command. Creates XML response and writes it to response output stream.
     *
     * @param out response output stream
     * @throws ConnectorException to handle in error handler.
     */
    @Override
    public void execute(final OutputStream out) throws ConnectorException {
        try {
            createXMLResponse(getDataForXml());
            out.write(creator.getDocumentAsText().getBytes("UTF-8"));
        } catch (ConnectorException e) {
            throw e;
        } catch (IOException e) {
            throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED, e);
        }
    }

    /**
     * abstract method to create XML response in command.
     *
     * @param errorNum error code from method getDataForXml()
     * @throws ConnectorException to handle in error handler.
     */
    private void createXMLResponse(final int errorNum) throws ConnectorException {
        if (configuration.isDebugMode() && this.exception != null) {
            throw new ConnectorException(this.exception);
        }
        Element rootElement = creator.getDocument().createElement("Connector");
        if (this.type != null && !type.equals("")) {
            rootElement.setAttribute("resourceType", this.type);
        }
        if (mustAddCurrentFolderNode()) {
            createCurrentFolderNode(rootElement);
        }
        creator.addErrorCommandToRoot(rootElement, errorNum, getErrorMsg(errorNum));
        createXMLChildNodes(errorNum, rootElement);
        creator.getDocument().appendChild(rootElement);
    }

    /**
     * gets error message if needed.
     *
     * @param errorNum error code
     * @return error message
     */
    protected String getErrorMsg(final int errorNum) {
        return null;
    }

    /**
     * abstract method to create XML nodes for commands.
     *
     * @param errorNum    error code
     * @param rootElement XML root node
     * @throws ConnectorException to handle in error handler.
     */
    protected abstract void createXMLChildNodes(final int errorNum,
                                                final Element rootElement)
            throws ConnectorException;

    /**
     * gets all necessary data to create XML response.
     *
     * @return error code {@link Constants.Errors} or
     * {@link Constants.Errors#CKFINDER_CONNECTOR_ERROR_NONE} if no error occurred.
     */
    protected abstract int getDataForXml();

    /**
     * creates <code>CurrentFolder</code> element.
     *
     * @param rootElement XML root node.
     */
    protected void createCurrentFolderNode(final Element rootElement) {
        Element element = creator.getDocument().createElement("CurrentFolder");
        element.setAttribute("path", this.currentFolder);
        element.setAttribute("url", configuration.getTypes().get(this.type).getUrl()
                + this.currentFolder);
        element.setAttribute("acl", String.valueOf(AccessControlUtil.getInstance().checkACLForRole(this.type,
                this.currentFolder, this.userRole)));
        rootElement.appendChild(element);
    }

    @Override
    public void initParams(final HttpServletRequest request,
                           final IConfiguration configuration, final Object... params)
            throws ConnectorException {
        super.initParams(request, configuration, params);
        creator = new XMLCreator();
        creator.createDocument();
    }

    /**
     * whether <code>CurrentFolder</code> element should be added to the XML response.
     *
     * @return true if must.
     */
    protected boolean mustAddCurrentFolderNode() {
        return this.type != null && this.currentFolder != null;
    }
}
