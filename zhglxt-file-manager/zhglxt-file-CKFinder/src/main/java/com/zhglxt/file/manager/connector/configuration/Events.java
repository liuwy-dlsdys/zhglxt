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

import com.zhglxt.file.manager.connector.data.EventArgs;
import com.zhglxt.file.manager.connector.data.EventCommandData;
import com.zhglxt.file.manager.connector.data.IEventHandler;
import com.zhglxt.file.manager.connector.data.PluginInfo;
import com.zhglxt.file.manager.connector.errors.ConnectorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides support for event handlers.
 */
public class Events {

    private Map<EventTypes, List<EventCommandData>> eventHandlers;

    /**
     * The list of events.
     */
    public enum EventTypes {

        BeforeExecuteCommand, AfterFileUpload, InitCommand;
    }

    /**
     * default constructor.
     */
    public Events() {
        eventHandlers = new HashMap<Events.EventTypes, List<EventCommandData>>();
        eventHandlers.put(EventTypes.AfterFileUpload, new ArrayList<EventCommandData>());
        eventHandlers.put(EventTypes.InitCommand, new ArrayList<EventCommandData>());
        eventHandlers.put(EventTypes.BeforeExecuteCommand,
                new ArrayList<EventCommandData>());
    }

    /**
     * register events handlers for event.
     *
     * @param event        selected event
     * @param eventHandler event class to register
     */
    public void addEventHandler(final EventTypes event,
                                final Class<? extends IEventHandler> eventHandler) {
        EventCommandData eventCommandData = new EventCommandData(eventHandler);
        eventHandlers.get(event).add(eventCommandData);
    }

    /**
     * register events handlers for event.
     *
     * @param event        selected event
     * @param eventHandler event class to register
     * @param pluginInfo   plugin info
     */
    public void addEventHandler(final EventTypes event,
                                final Class<? extends IEventHandler> eventHandler,
                                final PluginInfo pluginInfo) {
        EventCommandData eventCommandData = new EventCommandData(eventHandler);
        eventCommandData.setPluginInfo(pluginInfo);
        eventHandlers.get(event).add(eventCommandData);
    }

    /**
     * run event handlers for selected event.
     *
     * @param eventTyp      selected event.
     * @param args          event execute arguments.
     * @param configuration connector configuration
     * @return false when end executing command.
     * @throws ConnectorException when error occurs.
     */
    @SuppressWarnings("UseSpecificCatch")
    public boolean run(final EventTypes eventTyp, final EventArgs args,
                       final IConfiguration configuration)
            throws ConnectorException {
        for (EventCommandData eventCommandData : eventHandlers.get(eventTyp)) {
            try {
                IEventHandler events;
                if (eventCommandData.getPluginInfo() != null) {
                    events = eventCommandData.getEventListener().getConstructor(PluginInfo.class).newInstance(eventCommandData.getPluginInfo());
                } else {
                    events = eventCommandData.getEventListener().newInstance();
                }
                if (!events.runEventHandler(args, configuration)) {
                    return false;
                }
            } catch (ConnectorException ex) {
                throw ex;
            } catch (Exception e) {
                throw new ConnectorException(e);
            }

        }
        return true;
    }
}
