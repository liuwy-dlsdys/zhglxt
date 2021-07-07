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
import com.zhglxt.file.manager.connector.data.*;
import com.zhglxt.file.manager.connector.errors.ConnectorException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ImageResizeInitCommandEventHandler implements IEventHandler {

    private PluginInfo pluginInfo;

    public ImageResizeInitCommandEventHandler(PluginInfo info) {
        this.pluginInfo = info;
    }

    @Override
    public boolean runEventHandler(EventArgs eventArgs, IConfiguration arg1)
            throws ConnectorException {
        InitCommandEventArgs args = (InitCommandEventArgs) eventArgs;
        NodeList list = args.getRootElement().getElementsByTagName("PluginsInfo");
        if (list.getLength() > 0) {
            Node node = list.item(0);
            Element pluginElem = args.getXml().getDocument().createElement(pluginInfo.getName());
            for (PluginParam param : pluginInfo.getParams()) {
                pluginElem.setAttribute(param.getName(), param.getValue());
            }
            node.appendChild(pluginElem);
        }
        return false;
    }
}
