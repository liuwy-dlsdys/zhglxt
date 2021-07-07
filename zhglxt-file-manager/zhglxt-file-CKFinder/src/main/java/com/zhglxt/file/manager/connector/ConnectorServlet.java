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

import com.zhglxt.file.manager.connector.configuration.*;
import com.zhglxt.file.manager.connector.data.BeforeExecuteCommandEventArgs;
import com.zhglxt.file.manager.connector.errors.ConnectorException;
import com.zhglxt.file.manager.connector.handlers.command.*;
import com.zhglxt.file.manager.connector.utils.AccessControlUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main connector servlet for handling CKFinder requests.
 */
public class ConnectorServlet extends HttpServlet {

    /**
     * holds exception if any occurs during CKFinder start.
     */
    private Exception startException;
    /**
     *
     */
    private static final long serialVersionUID = 2960665641425153638L;

    /**
     * Handling get requests.
     *
     * @param request  request
     * @param response response
     * @throws IOException      .
     * @throws ServletException .
     */
    @Override
    protected void doGet(final HttpServletRequest request,
                         final HttpServletResponse response) throws ServletException,
            IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        getResponse(request, response, false);
    }

    /**
     * Handling post requests.
     *
     * @param request  request
     * @param response response
     * @throws IOException      .
     * @throws ServletException .
     */
    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response) throws ServletException,
            IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        getResponse(request, response, true);
    }

    /**
     * Creating response for every command in request parameter.
     *
     * @param request  request
     * @param response response
     * @param post     if it's post command.
     * @throws ServletException when error occurs.
     */
    private void getResponse(final HttpServletRequest request,
                             final HttpServletResponse response, final boolean post)
            throws ServletException {
        if (startException != null
                && Boolean.valueOf(getServletConfig().getInitParameter("debug"))) {
            throw new ServletException(startException);
        }
        boolean isNativeCommand;
        String command = request.getParameter("command");
        IConfiguration configuration = null;
        try {
            configuration = ConfigurationFactory.getInstace().getConfiguration(request);
            if (configuration == null) {
                throw new Exception("Configuration wasn't initialized correctly. Check server logs.");
            }
        } catch (Exception e) {
            if (Boolean.valueOf(getServletConfig().getInitParameter("debug"))) {
                Logger.getLogger(ConnectorServlet.class.getName()).log(Level.SEVERE, "Configuration wasn't initialized correctly. Check server logs.", e);
            }
            throw new ServletException(e);
        }
        try {

            if (command == null || command.equals("")) {
                throw new ConnectorException(
                        Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_COMMAND, false);
            }

            configuration.setDebugMode(
                    Boolean.valueOf(getServletConfig().getInitParameter("debug")));

            if (CommandHandlerEnum.contains(command.toUpperCase())) {
                CommandHandlerEnum cmd;
                isNativeCommand = true;
                cmd = CommandHandlerEnum.valueOf(command.toUpperCase());
                // checks if command should go via POST request or it's a post request
                // and it's not upload command
                if ((cmd.getCommand() instanceof IPostCommand || post)
                        && !CommandHandlerEnum.FILEUPLOAD.equals(cmd)
                        && !CommandHandlerEnum.QUICKUPLOAD.equals(cmd)) {
                    checkPostRequest(request);
                }
            } else {
                isNativeCommand = false;
            }

            BeforeExecuteCommandEventArgs args = new BeforeExecuteCommandEventArgs();
            args.setCommand(command);
            args.setRequest(request);
            args.setResponse(response);

            if (configuration.getEvents() != null) {
                if (configuration.getEvents().run(Events.EventTypes.BeforeExecuteCommand,
                        args, configuration)) {
                    if (!isNativeCommand) {
                        command = null;
                    }
                    executeNativeCommand(command, request, response, configuration, isNativeCommand);
                }
            } else {
                if (!isNativeCommand) {
                    command = null;
                }
                executeNativeCommand(command, request, response, configuration, isNativeCommand);
            }
        } catch (IllegalArgumentException e) {
            if (Boolean.valueOf(getServletConfig().getInitParameter("debug"))) {
                Logger.getLogger(ConnectorServlet.class.getName()).log(Level.SEVERE, "Couldn't execute native command.", e);
                response.reset();
                throw new ServletException(e);
            } else {
                handleError(
                        new ConnectorException(
                                Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_COMMAND, false),
                        configuration, request, response, command);
            }
        } catch (ConnectorException e) {
            if (Boolean.valueOf(getServletConfig().getInitParameter("debug"))) {
                Logger.getLogger(ConnectorServlet.class.getName()).log(Level.SEVERE,
                        e.getErrorMessage(), e.getException() != null ? e.getException() : e);
                response.reset();
                throw new ServletException(e.getException());
            } else {
                handleError(e, configuration, request, response, command);
            }
        }
    }

    /**
     * Executes one of connector's predefined commands specified as parameter.
     *
     * @param command         string representing command name
     * @param request         current request object
     * @param response        current response object
     * @param configuration   CKFinder connector configuration
     * @param isNativeCommand flag indicating whether command is available in enumeration object
     * @throws ConnectorException       when command isn't native
     * @throws IllegalArgumentException when provided command is not found in enumeration object
     */
    private void executeNativeCommand(String command, final HttpServletRequest request,
                                      final HttpServletResponse response, IConfiguration configuration,
                                      boolean isNativeCommand) throws IllegalArgumentException, ConnectorException {
        if (isNativeCommand) {
            CommandHandlerEnum cmd = CommandHandlerEnum.valueOf(command.toUpperCase());
            cmd.execute(
                    request, response, configuration, getServletContext());
        } else {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_COMMAND, false);
        }
    }

    /**
     * checks post request if it's ckfinder command.
     *
     * @param request request
     * @throws ConnectorException when param isn't set or has wrong value.
     */
    private void checkPostRequest(final HttpServletRequest request)
            throws ConnectorException {
        if (request.getParameter("CKFinderCommand") == null
                || !(request.getParameter("CKFinderCommand").equals("true"))) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST, true);
        }

    }

    /**
     * handles error from execute command.
     *
     * @param e              exception
     * @param request        request
     * @param response       response
     * @param configuration  connector configuration
     * @param currentCommand current command
     * @throws ServletException when error handling fails.
     */
    private void handleError(final ConnectorException e,
                             final IConfiguration configuration,
                             final HttpServletRequest request, final HttpServletResponse response,
                             final String currentCommand)
            throws ServletException {
        try {
            if (currentCommand != null && !currentCommand.equals("")) {
                Command command = CommandHandlerEnum.valueOf(
                        currentCommand.toUpperCase()).getCommand();
                if (command instanceof XMLCommand) {
                    CommandHandlerEnum.XMLERROR.execute(request, response, configuration,
                            getServletContext(), e);
                } else {
                    CommandHandlerEnum.ERROR.execute(request, response, configuration,
                            getServletContext(), e);
                }
            } else {
                CommandHandlerEnum.XMLERROR.execute(request, response, configuration,
                        getServletContext(), e);
            }

        } catch (Exception e1) {
            throw new ServletException(e1);
        }
    }

    @Override
    @SuppressWarnings("UseSpecificCatch")
    public void init() throws ServletException {
        ServletContextFactory.setServletContext(getServletContext());
        IConfiguration configuration;
        try {
            String className = getServletConfig().getInitParameter(
                    "configuration");
            if (className != null) {
                Class<?> clazz = Class.forName(className);

                if (clazz.getConstructor(ServletConfig.class) != null) {
                    configuration = (IConfiguration) clazz.getConstructor(
                            ServletConfig.class).newInstance(getServletConfig());

                } else {
                    configuration = (IConfiguration) clazz.newInstance();
                }
            } else {
                configuration = new Configuration(getServletConfig());
            }
        } catch (Exception e) {
            Logger.getLogger(ConnectorServlet.class.getName()).log(Level.SEVERE,
                    "Couldn't initialize custom configuration. Rolling back to the default one.", e);
            configuration = new Configuration(getServletConfig());
        }
        try {
            configuration.init();
            AccessControlUtil.getInstance().loadConfiguration(configuration);
        } catch (Exception e) {
            if (Boolean.valueOf(getServletConfig().getInitParameter("debug"))) {
                Logger.getLogger(ConnectorServlet.class.getName()).log(Level.SEVERE, "Couldn't initialize configuration object.", e);
            }
            this.startException = e;
            configuration = null;
        }
        ConfigurationFactory.getInstace().setConfiguration(configuration);
    }

    /**
     * Enum with all command handles by servlet.
     */
    private enum CommandHandlerEnum {

        /**
         * init command.
         */
        INIT(new InitCommand()),
        /**
         * get subfolders for selected location command.
         */
        GETFOLDERS(new GetFoldersCommand()),
        /**
         * get files from current folder command.
         */
        GETFILES(new GetFilesCommand()),
        /**
         * get thumbnail for file command.
         */
        THUMBNAIL(new ThumbnailCommand()),
        /**
         * download file command.
         */
        DOWNLOADFILE(new DownloadFileCommand()),
        /**
         * create subfolder.
         */
        CREATEFOLDER(new CreateFolderCommand()),
        /**
         * rename file.
         */
        RENAMEFILE(new RenameFileCommand()),
        /**
         * rename folder.
         */
        RENAMEFOLDER(new RenameFolderCommand()),
        /**
         * delete folder.
         */
        DELETEFOLDER(new DeleteFolderCommand()),
        /**
         * copy files.
         */
        COPYFILES(new CopyFilesCommand()),
        /**
         * move files.
         */
        MOVEFILES(new MoveFilesCommand()),
        /**
         * delete files.
         */
        DELETEFILES(new DeleteFilesCommand()),
        /**
         * file upload.
         */
        FILEUPLOAD(new FileUploadCommand()),
        /**
         * quick file upload.
         */
        QUICKUPLOAD(new QuickUploadCommand()),
        /**
         * XML error command.
         */
        XMLERROR(new XMLErrorCommand()),
        /**
         * error command.
         */
        ERROR(new ErrorCommand());
        /**
         * command class for enum field.
         */
        private Command command;
        /**
         * {@code Set} holding enumeration values,
         */
        private static final HashSet<String> enumValues = new HashSet<String>();

        /**
         * Enum constructor to set command.
         *
         * @param command1 command name
         */
        private CommandHandlerEnum(final Command command1) {
            this.command = command1;
        }

        /**
         * Fills in {@code Set} holding enumeration values for this {@code Enum}.
         */
        private static void setEnums() {
            for (CommandHandlerEnum enumValue : CommandHandlerEnum.values()) {
                enumValues.add(enumValue.name());
            }
        }

        /**
         * Checks whether enumeration object contains command name specified as parameter.
         *
         * @param enumValue string representing command name to check
         * @return {@code true} is command exists, {@code false} otherwise
         */
        public static boolean contains(String enumValue) {
            if (enumValues.isEmpty()) {
                setEnums();
            }
            for (String value : enumValues) {
                if (value.equals(enumValue)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Executes command.
         *
         * @param request       request
         * @param response      response
         * @param configuration connector configuration
         * @param sc            servletContext
         * @param params        params for command.
         * @throws ConnectorException when error occurs
         */
        private void execute(final HttpServletRequest request,
                             final HttpServletResponse response, final IConfiguration configuration,
                             final ServletContext sc, final Object... params)
                throws ConnectorException {
            Command com = null;
            try {
                com = command.getClass().newInstance();
            } catch (IllegalAccessException e1) {
                throw new ConnectorException(
                        Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_COMMAND);
            } catch (InstantiationException e1) {
                throw new ConnectorException(
                        Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_COMMAND);
            }
            if (com == null) {
                throw new ConnectorException(
                        Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_COMMAND);
            }
            com.runCommand(request, response, configuration, params);
        }

        /**
         * gets command.
         *
         * @return command
         */
        public Command getCommand() {
            return this.command;
        }
    }
}
