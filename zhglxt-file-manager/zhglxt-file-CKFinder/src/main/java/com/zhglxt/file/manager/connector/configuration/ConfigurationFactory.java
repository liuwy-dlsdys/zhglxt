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

import com.zhglxt.file.manager.connector.data.ResourceType;
import com.zhglxt.file.manager.connector.errors.ConnectorException;
import com.zhglxt.file.manager.connector.utils.AccessControlUtil;
import com.zhglxt.file.manager.connector.utils.FileUtils;
import com.zhglxt.file.manager.connector.utils.PathUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * Factory returning configuration instance.
 */
public final class ConfigurationFactory {

    private static ConfigurationFactory instance;
    private IConfiguration configuration;

    /**
     * private constructor.
     */
    private ConfigurationFactory() {
    }

    /**
     * if instance is null creates one and returns it.
     *
     * @return configuration instance.
     */
    public static ConfigurationFactory getInstace() {
        if (instance == null) {
            instance = new ConfigurationFactory();
        }
        return instance;
    }

    /**
     * Gets base configuration prepared in {@code IConfiguration.init} method.
     *
     * @return CKFinder configuration object.
     * @throws Exception when configuration cannot be obtained.
     */
    public final IConfiguration getConfiguration() throws Exception {
        if (configuration != null
                && configuration.checkIfReloadConfig()) {
            configuration.init();
            AccessControlUtil.getInstance().resetConfiguration();
            AccessControlUtil.getInstance().loadConfiguration(configuration);
        }
        return configuration;
    }

    /**
     * Gets and prepares configuration.
     *
     * @param request request
     * @return the configuration
     * @throws Exception when error occurs
     */
    public final IConfiguration getConfiguration(final HttpServletRequest request)
            throws Exception {
        IConfiguration baseConf = getConfiguration();
        return prepareConfiguration(request, baseConf);

    }

    /**
     * Prepares configuration using request.
     *
     * @param request  request
     * @param baseConf base configuration initialized in IConfiguration.init()
     * @return prepared configuration
     * @throws Exception when error occurs
     */
    public IConfiguration prepareConfiguration(final HttpServletRequest request,
                                               final IConfiguration baseConf)
            throws Exception {
        if (baseConf != null) {
            IConfiguration conf = baseConf.cloneConfiguration();
            conf.prepareConfigurationForRequest(request);
            updateResourceTypesPaths(request, conf);
            AccessControlUtil.getInstance().loadConfiguration(conf);
            return conf;
        }
        return null;

    }

    /**
     * @param configuration the configuration to set
     */
    public final void setConfiguration(final IConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Updates resources types paths by request.
     *
     * @param request request
     * @param conf    connector configuration.
     * @throws Exception when error occurs
     */
    private void updateResourceTypesPaths(final HttpServletRequest request,
                                          final IConfiguration conf) throws Exception {

        String baseFolder = getBaseFolder(conf, request);
        baseFolder = conf.getThumbsDir().replace(Constants.BASE_DIR_PLACEHOLDER,
                baseFolder);
        baseFolder = PathUtils.escape(baseFolder);
        baseFolder = PathUtils.removeSlashFromEnd(baseFolder);
        baseFolder = FileUtils.getFullPath(baseFolder, true, false);
        if (baseFolder == null) {
            throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_FOLDER_NOT_FOUND,
                    "Thumbs directory could not be created using specified path.");
        }

        File file = new File(baseFolder);
        if (!file.exists() && !request.getParameter("command").equals("Init")) {
            file.mkdir();
        }
        conf.setThumbsPath(file.getAbsolutePath());

        String thumbUrl = conf.getThumbsURL();
        thumbUrl = thumbUrl.replaceAll(
                Constants.BASE_URL_PLACEHOLDER,
                conf.getBasePathBuilder().getBaseUrl(request));
        conf.setThumbsURL(PathUtils.escape(thumbUrl));

        for (ResourceType item : conf.getTypes().values()) {
            String url = item.getUrl();
            url = url.replaceAll(Constants.BASE_URL_PLACEHOLDER,
                    conf.getBasePathBuilder().getBaseUrl(request));
            url = PathUtils.escape(url);
            url = PathUtils.removeSlashFromEnd(url);
            item.setUrl(url);

            baseFolder = getBaseFolder(conf, request);
            baseFolder = item.getPath().replace(Constants.BASE_DIR_PLACEHOLDER, baseFolder);
            baseFolder = PathUtils.escape(baseFolder);
            baseFolder = PathUtils.removeSlashFromEnd(baseFolder);

            boolean isFromUrl = false;
            if (baseFolder == null || baseFolder.equals("")) {
                baseFolder = PathUtils.removeSlashFromBeginning(url);
                isFromUrl = true;
            }

            String resourcePath = isFromUrl
                    ? FileUtils.calculatePathFromBaseUrl(baseFolder) : FileUtils.getFullPath(baseFolder, true, false);
            if (resourcePath == null) {
                throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_FOLDER_NOT_FOUND,
                        "Resource directory could not be created using specified path.");
            }

            file = new File(resourcePath);
            if (!file.exists() && !request.getParameter("command").equals("Init")) {
                FileUtils.createPath(file, false);
            }
            item.setPath(file.getAbsolutePath());
        }

    }

    /**
     * Gets the path to base dir from configuration Crates the base dir folder if it doesn't exists.
     *
     * @param conf    connector configuration
     * @param request request
     * @return path to base dir from conf
     * @throws ConnectorException when error during creating folder occurs
     */
    private String getBaseFolder(final IConfiguration conf,
                                 final HttpServletRequest request)
            throws ConnectorException {
        String baseFolder = conf.getBasePathBuilder().getBaseDir(request);

        File baseDir = new File(baseFolder);
        if (!baseDir.exists()) {
            try {
                FileUtils.createPath(baseDir, false);
            } catch (IOException e) {
                throw new ConnectorException(e);
            }
        }

        return PathUtils.addSlashToEnd(baseFolder);
    }
}
