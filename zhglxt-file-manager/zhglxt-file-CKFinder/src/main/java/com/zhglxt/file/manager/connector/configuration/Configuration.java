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

import com.zhglxt.file.manager.connector.data.AccessControlLevel;
import com.zhglxt.file.manager.connector.data.PluginInfo;
import com.zhglxt.file.manager.connector.data.PluginParam;
import com.zhglxt.file.manager.connector.data.ResourceType;
import com.zhglxt.file.manager.connector.errors.ConnectorException;
import com.zhglxt.file.manager.connector.utils.FileUtils;
import com.zhglxt.file.manager.connector.utils.PathUtils;
import org.w3c.dom.*;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class loads configuration from XML file.
 */
public class Configuration implements IConfiguration {

    protected static final int MAX_QUALITY = 100;
    protected static final float MAX_QUALITY_FLOAT = 100f;
    private long lastCfgModificationDate;
    protected boolean enabled;
    protected String xmlFilePath;
    protected String baseDir;
    protected String baseURL;
    protected String licenseName;
    protected String licenseKey;
    protected Integer imgWidth;
    protected Integer imgHeight;
    protected float imgQuality;
    protected Map<String, ResourceType> types;
    protected boolean thumbsEnabled;
    protected String thumbsURL;
    protected String thumbsDir;
    protected String thumbsPath;
    protected boolean thumbsDirectAccess;
    protected Integer thumbsMaxHeight;
    protected Integer thumbsMaxWidth;
    protected float thumbsQuality;
    protected AccessControlLevelsList<AccessControlLevel> accessControlLevels;
    protected List<String> hiddenFolders;
    protected List<String> hiddenFiles;
    protected boolean doubleExtensions;
    protected boolean forceASCII;
    protected boolean checkSizeAfterScaling;
    protected String uriEncoding;
    protected String userRoleSessionVar;
    protected List<PluginInfo> plugins;
    protected boolean secureImageUploads;
    protected List<String> htmlExtensions;
    protected Set<String> defaultResourceTypes;
    protected IBasePathBuilder basePathBuilder;
    protected boolean disallowUnsafeCharacters;
    protected boolean enableCsrfProtection;
    private boolean loading;
    private Events events;
    private boolean debug;
    protected ServletConfig servletConf;
    private static final Logger configurationLogger = Logger.getLogger(Configuration.class.getName());

    /**
     * Constructor.
     *
     * @param servletConfig ServletConfig object used to get configuration parameters from web-xml.
     */
    public Configuration(final ServletConfig servletConfig) {
        this.servletConf = servletConfig;
        this.xmlFilePath = servletConfig.getInitParameter("XMLConfig");
        this.plugins = new ArrayList<PluginInfo>();
        this.htmlExtensions = new ArrayList<String>();
        this.hiddenFolders = new ArrayList<String>();
        this.hiddenFiles = new ArrayList<String>();
        this.defaultResourceTypes = new LinkedHashSet<String>();
    }

    /**
     * Resets all configuration values.
     */
    private void clearConfiguration() {
        this.debug = false;
        this.enabled = false;
        this.baseDir = "";
        this.baseURL = "";
        this.licenseName = "";
        this.licenseKey = "";
        this.imgWidth = DEFAULT_IMG_WIDTH;
        this.imgHeight = DEFAULT_IMG_HEIGHT;
        this.imgQuality = DEFAULT_IMG_QUALITY;
        this.types = new LinkedHashMap<String, ResourceType>();
        this.thumbsEnabled = false;
        this.thumbsURL = "";
        this.thumbsDir = "";
        this.thumbsPath = "";
        this.thumbsQuality = DEFAULT_IMG_QUALITY;
        this.thumbsDirectAccess = false;
        this.thumbsMaxHeight = DEFAULT_THUMB_MAX_HEIGHT;
        this.thumbsMaxWidth = DEFAULT_THUMB_MAX_WIDTH;
        this.accessControlLevels = new AccessControlLevelsList<AccessControlLevel>(true);
        this.hiddenFolders = new ArrayList<String>();
        this.hiddenFiles = new ArrayList<String>();
        this.doubleExtensions = false;
        this.forceASCII = false;
        this.checkSizeAfterScaling = false;
        this.uriEncoding = DEFAULT_URI_ENCODING;
        this.userRoleSessionVar = "";
        this.plugins = new ArrayList<PluginInfo>();
        this.secureImageUploads = false;
        this.htmlExtensions = new ArrayList<String>();
        this.defaultResourceTypes = new LinkedHashSet<String>();
        this.events = new Events();
        this.basePathBuilder = null;
        this.disallowUnsafeCharacters = false;
        this.enableCsrfProtection = true;
    }

    /**
     * Initializes configuration from XML file.
     *
     * @throws Exception when error occurs.
     */
    @Override
    public void init() throws Exception {
        clearConfiguration();
        this.loading = true;
        File file = new File(getFullConfigPath());
        this.lastCfgModificationDate = file.lastModified();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.normalize();
        Node node = doc.getFirstChild();
        if (node != null) {
            NodeList nodeList = node.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node childNode = nodeList.item(i);
                if (childNode.getNodeName().equals("enabled")) {
                    this.enabled = Boolean.valueOf(nullNodeToString(childNode));
                }
                if (childNode.getNodeName().equals("baseDir")) {
                    this.baseDir = nullNodeToString(childNode);
                    this.baseDir = PathUtils.escape(this.baseDir);
                    this.baseDir = PathUtils.addSlashToEnd(this.baseDir);
                }
                if (childNode.getNodeName().equals("baseURL")) {
                    this.baseURL = nullNodeToString(childNode);
                    this.baseURL = PathUtils.escape(baseURL);
                    this.baseURL = PathUtils.addSlashToEnd(this.baseURL);
                }
                if (childNode.getNodeName().equals("licenseName")) {
                    this.licenseName = nullNodeToString(childNode);
                }
                if (childNode.getNodeName().equals("licenseKey")) {
                    this.licenseKey = nullNodeToString(childNode);
                }
                if (childNode.getNodeName().equals("imgWidth")) {
                    String width = nullNodeToString(childNode);
                    width = width.replaceAll("//D", "");
                    try {
                        this.imgWidth = Integer.valueOf(width);
                    } catch (NumberFormatException e) {
                        this.imgWidth = null;
                    }
                }
                if (childNode.getNodeName().equals("imgQuality")) {
                    String quality = nullNodeToString(childNode);
                    quality = quality.replaceAll("//D", "");
                    this.imgQuality = adjustQuality(quality);
                }

                if (childNode.getNodeName().equals("imgHeight")) {
                    String height = nullNodeToString(childNode);
                    height = height.replaceAll("//D", "");
                    try {
                        this.imgHeight = Integer.valueOf(height);
                    } catch (NumberFormatException e) {
                        this.imgHeight = null;
                    }
                }
                if (childNode.getNodeName().equals("thumbs")) {
                    setThumbs(childNode.getChildNodes());
                }
                if (childNode.getNodeName().equals("accessControls")) {
                    setACLs(childNode.getChildNodes());
                }
                if (childNode.getNodeName().equals("hideFolders")) {
                    setHiddenFolders(childNode.getChildNodes());
                }
                if (childNode.getNodeName().equals("hideFiles")) {
                    setHiddenFiles(childNode.getChildNodes());
                }
                if (childNode.getNodeName().equals("checkDoubleExtension")) {
                    this.doubleExtensions = Boolean.valueOf(nullNodeToString(childNode));
                }
                if (childNode.getNodeName().equals("disallowUnsafeCharacters")) {
                    this.disallowUnsafeCharacters = Boolean.valueOf(nullNodeToString(childNode));
                }
                if (childNode.getNodeName().equals("forceASCII")) {
                    this.forceASCII = Boolean.valueOf(nullNodeToString(childNode));
                }
                if (childNode.getNodeName().equals("checkSizeAfterScaling")) {
                    this.checkSizeAfterScaling = Boolean.valueOf(nullNodeToString(childNode));
                }
                if (childNode.getNodeName().equals("enableCsrfProtection")) {
                    this.enableCsrfProtection = Boolean.valueOf(nullNodeToString(childNode));
                }
                if (childNode.getNodeName().equals("htmlExtensions")) {
                    String htmlExt = nullNodeToString(childNode);
                    Scanner scanner = new Scanner(htmlExt).useDelimiter(",");
                    while (scanner.hasNext()) {
                        String val = scanner.next();
                        if (val != null && !val.equals("")) {
                            htmlExtensions.add(val.trim().toLowerCase());
                        }

                    }
                }

                if (childNode.getNodeName().equals("secureImageUploads")) {
                    this.secureImageUploads = Boolean.valueOf(nullNodeToString(childNode));
                }

                if (childNode.getNodeName().equals("uriEncoding")) {
                    this.uriEncoding = nullNodeToString(childNode);
                }
                if (childNode.getNodeName().equals("userRoleSessionVar")) {
                    this.userRoleSessionVar = nullNodeToString(childNode);
                }
                if (childNode.getNodeName().equals("defaultResourceTypes")) {
                    String value = nullNodeToString(childNode);
                    Scanner sc = new Scanner(value).useDelimiter(",");
                    while (sc.hasNext()) {
                        this.defaultResourceTypes.add(sc.next());
                    }
                }
                if (childNode.getNodeName().equals("plugins")) {
                    setPlugins(childNode);
                }
                if (childNode.getNodeName().equals("basePathBuilderImpl")) {
                    setBasePathImpl(nullNodeToString(childNode));
                }
            }
        }
        setTypes(doc);
        this.events = new Events();
        registerEventHandlers();
        this.loading = false;
    }

    /**
     * Returns XML node contents or empty String instead of null if XML node is empty.
     */
    private String nullNodeToString(Node childNode) {
        return childNode.getTextContent() == null ? "" : childNode.getTextContent().trim();
    }

    /**
     * Gets absolute path to XML configuration file.
     *
     * @return absolute path to XML configuration file
     * @throws ConnectorException when absolute path cannot be obtained.
     */
    private String getFullConfigPath() throws ConnectorException {
        File cfgFile = null;
        String path = FileUtils.getFullPath(xmlFilePath, false, true);
        if (path == null) {
            throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_FILE_NOT_FOUND,
                    "Configuration file could not be found under specified location.");
        } else {
            cfgFile = new File(path);
        }

        if (cfgFile.exists() && cfgFile.isFile()) {
            return cfgFile.getAbsolutePath();
        } else {
            return xmlFilePath;
        }
    }

    /**
     * Sets user defined ConfigurationPathBuilder.
     *
     * @param value userPathBuilderImpl configuration value
     */
    private void setBasePathImpl(final String value) {
        try {
            @SuppressWarnings("unchecked")
            Class<IBasePathBuilder> clazz = (Class<IBasePathBuilder>) Class.forName(value);
            this.basePathBuilder = clazz.newInstance();
        } catch (Exception e) {
            this.basePathBuilder = new ConfigurationPathBuilder();
        }
    }

    /**
     * Adjusts image quality.
     *
     * @param imgQuality Image quality
     * @return Adjusted image quality
     */
    private float adjustQuality(final String imgQuality) {
        float helper;
        try {
            helper = Math.abs(Float.parseFloat(imgQuality));
        } catch (NumberFormatException e) {
            return DEFAULT_IMG_QUALITY;
        }
        if (helper == 0 || helper == 1) {
            return helper;
        } else if (helper > 0 && helper < 1) {
            helper = (Math.round(helper * MAX_QUALITY_FLOAT) / MAX_QUALITY_FLOAT);
        } else if (helper > 1 && helper <= MAX_QUALITY) {
            helper = (Math.round(helper) / MAX_QUALITY_FLOAT);
        } else {
            helper = DEFAULT_IMG_QUALITY;
        }
        return helper;
    }

    /**
     * Registers event handlers from all plugins.
     */
    protected void registerEventHandlers() {
        for (PluginInfo item : this.plugins) {
            try {
                @SuppressWarnings("unchecked")
                Class<Plugin> clazz = (Class<Plugin>) Class.forName(item.getClassName());
                Plugin plugin = (Plugin) clazz.newInstance();
                plugin.setPluginInfo(item);
                plugin.registerEventHandlers(this.events);
                item.setEnabled(true);
            } catch (ClassCastException e) {
                item.setEnabled(false);
            } catch (ClassNotFoundException e) {
                item.setEnabled(false);
            } catch (IllegalAccessException e) {
                item.setEnabled(false);
            } catch (InstantiationException e) {
                item.setEnabled(false);
            }
        }
    }

    /**
     * Sets hidden files list defined in XML configuration.
     *
     * @param childNodes list of files nodes.
     */
    private void setHiddenFiles(final NodeList childNodes) {
        for (int i = 0, j = childNodes.getLength(); i < j; i++) {
            Node node = childNodes.item(i);
            if (node.getNodeName().equals("file")) {
                String val = nullNodeToString(node);
                if (!val.equals("")) {
                    this.hiddenFiles.add(val.trim());
                }
            }
        }
    }

    /**
     * Sets hidden folders list defined in XML configuration.
     *
     * @param childNodes list of folder nodes.
     */
    private void setHiddenFolders(final NodeList childNodes) {
        for (int i = 0, j = childNodes.getLength(); i < j; i++) {
            Node node = childNodes.item(i);
            if (node.getNodeName().equals("folder")) {
                String val = nullNodeToString(node);
                if (!val.equals("")) {
                    this.hiddenFolders.add(val.trim());
                }
            }
        }
    }

    /**
     * Sets ACL configuration as a list of access control levels.
     *
     * @param childNodes nodes with ACL configuration.
     */
    private void setACLs(final NodeList childNodes) {
        for (int i = 0, j = childNodes.getLength(); i < j; i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeName().equals("accessControl")) {
                AccessControlLevel acl;
                acl = getACLFromNode(childNode);
                if (acl != null) {
                    this.accessControlLevels.addItem(acl, false);
                }
            }
        }
    }

    /**
     * Gets single ACL configuration from XML node.
     *
     * @param childNode XML accessControl node.
     * @return access control level object.
     */
    private AccessControlLevel getACLFromNode(final Node childNode) {
        AccessControlLevel acl = new AccessControlLevel();
        for (int i = 0, j = childNode.getChildNodes().getLength(); i < j; i++) {
            Node childChildNode = childNode.getChildNodes().item(i);
            if (childChildNode.getNodeName().equals("role")) {
                acl.setRole(nullNodeToString(childChildNode));
            }
            if (childChildNode.getNodeName().equals("resourceType")) {
                acl.setResourceType(nullNodeToString(childChildNode));
            }
            if (childChildNode.getNodeName().equals("folder")) {
                acl.setFolder(nullNodeToString(childChildNode));
            }
            if (childChildNode.getNodeName().equals("folderView")) {
                acl.setFolderView(Boolean.valueOf(nullNodeToString(childChildNode)));
            }
            if (childChildNode.getNodeName().equals("folderCreate")) {
                acl.setFolderCreate(Boolean.valueOf(nullNodeToString(childChildNode)));
            }
            if (childChildNode.getNodeName().equals("folderRename")) {
                acl.setFolderRename(Boolean.valueOf(nullNodeToString(childChildNode)));
            }
            if (childChildNode.getNodeName().equals("folderDelete")) {
                acl.setFolderDelete(Boolean.valueOf(nullNodeToString(childChildNode)));
            }
            if (childChildNode.getNodeName().equals("fileView")) {
                acl.setFileView(Boolean.valueOf(nullNodeToString(childChildNode)));
            }
            if (childChildNode.getNodeName().equals("fileUpload")) {
                acl.setFileUpload(Boolean.valueOf(nullNodeToString(childChildNode)));
            }
            if (childChildNode.getNodeName().equals("fileRename")) {
                acl.setFileRename(Boolean.valueOf(nullNodeToString(childChildNode)));
            }
            if (childChildNode.getNodeName().equals("fileDelete")) {
                acl.setFileDelete(Boolean.valueOf(nullNodeToString(childChildNode)));
            }
        }

        if (acl.getResourceType() == null || acl.getRole() == null) {
            return null;
        }

        if (acl.getFolder() == null || acl.getFolder().equals("")) {
            acl.setFolder("/");
        }

        return acl;
    }

    /**
     * creates thumb configuration from XML.
     *
     * @param childNodes list of thumb XML nodes
     */
    private void setThumbs(final NodeList childNodes) {
        for (int i = 0, j = childNodes.getLength(); i < j; i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeName().equals("enabled")) {
                this.thumbsEnabled = Boolean.valueOf(nullNodeToString(childNode));
            }
            if (childNode.getNodeName().equals("url")) {
                this.thumbsURL = nullNodeToString(childNode);
            }
            if (childNode.getNodeName().equals("directory")) {
                this.thumbsDir = nullNodeToString(childNode);
            }
            if (childNode.getNodeName().equals("directAccess")) {
                this.thumbsDirectAccess = Boolean.valueOf(nullNodeToString(childNode));
            }
            if (childNode.getNodeName().equals("maxHeight")) {
                String width = nullNodeToString(childNode);
                width = width.replaceAll("//D", "");
                try {
                    this.thumbsMaxHeight = Integer.valueOf(width);
                } catch (NumberFormatException e) {
                    this.thumbsMaxHeight = null;
                }
            }
            if (childNode.getNodeName().equals("maxWidth")) {
                String width = nullNodeToString(childNode);
                width = width.replaceAll("//D", "");
                try {
                    this.thumbsMaxWidth = Integer.valueOf(width);
                } catch (NumberFormatException e) {
                    this.thumbsMaxWidth = null;
                }
            }
            if (childNode.getNodeName().equals("quality")) {
                String quality = nullNodeToString(childNode);
                quality = quality.replaceAll("//D", "");
                this.thumbsQuality = adjustQuality(quality);
            }
        }

    }

    /**
     * Creates resource types configuration from XML configuration file (from XML element 'types').
     *
     * @param doc XML document.
     */
    private void setTypes(final Document doc) {
        types = new LinkedHashMap<String, ResourceType>();
        NodeList list = doc.getElementsByTagName("type");

        for (int i = 0, j = list.getLength(); i < j; i++) {
            Element element = (Element) list.item(i);
            String name = element.getAttribute("name");
            if (name != null && !name.equals("")) {
                ResourceType resourceType = createTypeFromXml(name, element.getChildNodes());
                types.put(name, resourceType);
            }
        }
    }

    /**
     * Creates single resource type configuration from XML configuration file (from XML element 'type').
     *
     * @param typeName   name of type.
     * @param childNodes type XML child nodes.
     * @return resource type
     */
    private ResourceType createTypeFromXml(final String typeName,
                                           final NodeList childNodes) {
        ResourceType resourceType = new ResourceType(typeName);
        for (int i = 0, j = childNodes.getLength(); i < j; i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeName().equals("url")) {
                String url = nullNodeToString(childNode);
                resourceType.setUrl(url);
            }
            if (childNode.getNodeName().equals("directory")) {
                String url = nullNodeToString(childNode);
                resourceType.setPath(url);
            }
            if (childNode.getNodeName().equals("maxSize")) {
                resourceType.setMaxSize(nullNodeToString(childNode));
            }
            if (childNode.getNodeName().equals("allowedExtensions")) {
                resourceType.setAllowedExtensions(nullNodeToString(childNode));
            }
            if (childNode.getNodeName().equals("deniedExtensions")) {
                resourceType.setDeniedExtensions(nullNodeToString(childNode));
            }
        }
        return resourceType;
    }

    /**
     * Checks if user is authenticated.
     *
     * @param request current request
     * @return true if user is authenticated and false otherwise.
     */
    @Override
    public boolean checkAuthentication(final HttpServletRequest request) {
        return DEFAULT_CHECKAUTHENTICATION;
    }

    /**
     * Checks if connector is enabled.
     *
     * @return if connector is enabled.
     */
    @Override
    public boolean enabled() {
        return this.enabled && !this.loading;
    }

    /**
     * Checks if disallowed characters in file and folder names are turned on.
     *
     * @return disallowUnsafeCharacters
     */
    @Override
    public boolean isDisallowUnsafeCharacters() {
        return this.disallowUnsafeCharacters;
    }

    /**
     * Returns flag indicating if Cross-site request forgery (CSRF) protection has been enabled.
     *
     * @return {@code boolean} flag indicating if CSRF protection has been enabled
     */
    @Override
    public boolean isEnableCsrfProtection() {
        return this.enableCsrfProtection;
    }

    /**
     * Gets location of ckfinder in application e.g. /ckfinder/.
     *
     * @return base directory.
     */
    @Override
    public String getBaseDir() {
        return this.baseDir;
    }

    /**
     * Returns path to ckfinder with application name e.g. /webapp/ckfinder/.
     *
     * @return base url.
     */
    @Override
    public String getBaseURL() {
        return this.baseURL;
    }

    /**
     * Gets image max height.
     *
     * @return max image height.
     */
    @Override
    public Integer getImgHeight() {
        if (this.imgHeight != null) {
            return this.imgHeight;
        } else {
            return DEFAULT_IMG_HEIGHT;
        }
    }

    /**
     * Gets image max width.
     *
     * @return max image width.
     */
    @Override
    public Integer getImgWidth() {
        if (this.imgWidth != null) {
            return this.imgWidth;
        } else {
            return DEFAULT_IMG_WIDTH;
        }
    }

    /**
     * Gets image quality.
     *
     * @return image quality.
     */
    @Override
    public float getImgQuality() {
        return this.imgQuality;
    }

    /**
     * Returns license key.
     *
     * @return license key.
     */
    @Override
    public String getLicenseKey() {
        return this.licenseKey;
    }

    /**
     * Returns license name.
     *
     * @return license name.
     */
    @Override
    public String getLicenseName() {
        return this.licenseName;
    }

    /**
     * Gets resource types map with resources names as map keys.
     *
     * @return resources map
     */
    @Override
    public Map<String, ResourceType> getTypes() {
        return this.types;
    }

    /**
     * Checks if thumbs are accessed directly.
     *
     * @return true if thumbs can be accessed directly.
     */
    @Override
    public boolean getThumbsDirectAccess() {
        return this.thumbsDirectAccess;
    }

    /**
     * Gets maximum height of thumb.
     *
     * @return maximum height of thumb.
     */
    @Override
    public int getMaxThumbHeight() {
        if (this.thumbsMaxHeight != null) {
            return this.thumbsMaxHeight;
        } else {
            return DEFAULT_THUMB_MAX_HEIGHT;
        }
    }

    /**
     * Gets maximum width of thumb.
     *
     * @return maximum width of thumb.
     */
    @Override
    public int getMaxThumbWidth() {
        if (this.thumbsMaxWidth != null) {
            return this.thumbsMaxWidth;
        } else {
            return DEFAULT_THUMB_MAX_WIDTH;
        }
    }

    /**
     * Check if thumbs are enabled.
     *
     * @return true if thumbs are enabled.
     */
    @Override
    public boolean getThumbsEnabled() {
        return this.thumbsEnabled;
    }

    /**
     * Gets url to thumbs directory (path from baseUrl).
     *
     * @return thumbs url.
     */
    @Override
    public String getThumbsURL() {
        return this.thumbsURL;
    }

    /**
     * Gets path to thumbs directory.
     *
     * @return thumbs directory.
     */
    @Override
    public String getThumbsDir() {
        return this.thumbsDir;
    }

    /**
     * Gets path to thumbs directory.
     *
     * @return thumbs directory.
     */
    @Override
    public String getThumbsPath() {
        return this.thumbsPath;
    }

    /**
     * gets thumbs quality.
     *
     * @return thumbs quality
     */
    @Override
    public float getThumbsQuality() {
        return this.thumbsQuality;
    }

    /**
     * Sets directory name for thumbnails.
     *
     * @param directory directory name for thumbnails.
     */
    @Override
    public void setThumbsPath(final String directory) {
        this.thumbsPath = directory;
    }

    /**
     * Returns list of access control levels.
     *
     * @return list of access control levels.
     */
    @Override
    public AccessControlLevelsList<AccessControlLevel> getAccessConrolLevels() {
        return this.accessControlLevels;
    }

    /**
     * Returns regex for hidden folders.
     *
     * @return regex for hidden folders
     */
    @Override
    public List<String> getHiddenFolders() {
        return this.hiddenFolders;
    }

    /**
     * Gets regex for hidden files.
     *
     * @return regex for hidden files
     */
    @Override
    public List<String> getHiddenFiles() {
        return this.hiddenFiles;
    }

    /**
     * Returns flag that determines whether double extensions should be checked.
     *
     * @return flag that determines whether double extensions should be checked.
     */
    @Override
    public boolean ckeckDoubleFileExtensions() {
        return this.doubleExtensions;
    }

    /**
     * Returns flag that determines whether ASCII should be forced.
     *
     * @return true ASCII should be forced.
     */
    @Override
    public boolean forceASCII() {
        return this.forceASCII;
    }

    /**
     * Returns flag that determines whether image size after resizing image should be checked.
     *
     * @return true if image size after resizing image should be checked.
     */
    @Override
    public boolean checkSizeAfterScaling() {
        return this.checkSizeAfterScaling;
    }

    /**
     * Returns URI encoding.
     *
     * @return URI encoding e.g. UTF-8.
     */
    @Override
    public String getUriEncoding() {
        if (this.uriEncoding == null || this.uriEncoding.length() == 0) {
            return DEFAULT_URI_ENCODING;
        }
        return this.uriEncoding;
    }

    /**
     * Gets user role name set in configuration.
     *
     * @return role name
     */
    @Override
    public String getUserRoleName() {
        return this.userRoleSessionVar;
    }

    /**
     * Gets list of available plugins.
     *
     * @return list of plugins.
     */
    @Override
    public List<PluginInfo> getPlugins() {
        return this.plugins;
    }

    /**
     * Returns flag that determines whether secure image uploads should be performed.
     *
     * @return true if secure image uploads should be performed.
     */
    @Override
    public boolean getSecureImageUploads() {
        return this.secureImageUploads;
    }

    /**
     * Returns HTML extensions list.
     *
     * @return HTML extensions list.
     */
    @Override
    public List<String> getHTMLExtensions() {
        return this.htmlExtensions;
    }

    /**
     * Returns events.
     *
     * @return Events object
     */
    @Override
    public Events getEvents() {
        return this.events;
    }

    /**
     * Gets default resource types list from configuration.
     *
     * @return default resource types list from configuration.
     */
    @Override
    public Set<String> getDefaultResourceTypes() {
        return this.defaultResourceTypes;
    }

    /**
     * Checks if debug mode is enabled.
     *
     * @return true if is debug mode
     */
    @Override
    public boolean isDebugMode() {
        return this.debug;
    }

    /**
     * Gets path builder for baseDir and baseURL.
     *
     * @return path builder.
     */
    @Override
    public IBasePathBuilder getBasePathBuilder() {
        if (this.basePathBuilder == null) {
            this.basePathBuilder = new ConfigurationPathBuilder();
        }
        return this.basePathBuilder;
    }

    /**
     * Checks if CKFinder configuration should be reloaded. It is reloaded when modification date is greater than the date of last
     * configuration initialization.
     *
     * @return true if reloading configuration is necessary.
     * @throws ConnectorException when configuration file cannot be reloaded.
     */
    @Override
    public boolean checkIfReloadConfig() throws ConnectorException {
        File cfgFile;
        String path = FileUtils.getFullPath(xmlFilePath, false, true);
        if (path == null) {
            if (this.debug) {
                throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_FILE_NOT_FOUND,
                        "Configuration file could not be found under specified location.");
            } else {
                configurationLogger.log(Level.SEVERE, "Configuration file could not be found under specified location.");
                return false;
            }
        } else {
            cfgFile = new File(path);
        }

        return (cfgFile.lastModified() > this.lastCfgModificationDate);
    }

    /**
     * Prepares configuration for single request. Empty method. It should be overridden if needed.
     *
     * @param request request
     */
    @Override
    public void prepareConfigurationForRequest(final HttpServletRequest request) {
    }

    /**
     * Sets plugins list from XML configuration file.
     *
     * @param childNode child of XML node 'plugins'.
     */
    private void setPlugins(final Node childNode) {
        NodeList nodeList = childNode.getChildNodes();
        for (int i = 0, j = nodeList.getLength(); i < j; i++) {
            Node childChildNode = nodeList.item(i);
            if (childChildNode.getNodeName().equals("plugin")) {
                this.plugins.add(createPluginFromNode(childChildNode));
            }
        }
    }

    /**
     * Creates plugin data from configuration file.
     *
     * @param element XML plugin node.
     * @return PluginInfo data
     */
    private PluginInfo createPluginFromNode(final Node element) {
        PluginInfo info = new PluginInfo();
        NodeList list = element.getChildNodes();
        for (int i = 0, l = list.getLength(); i < l; i++) {
            Node childElem = (Node) list.item(i);
            final String nodeName = childElem.getNodeName();
            final String textContent = nullNodeToString(childElem);
            if ("name".equals(nodeName)) {
                info.setName(textContent);
            }
            if ("class".equals(nodeName)) {
                info.setClassName(textContent);
            }
            if ("internal".equals(nodeName)) {
                info.setInternal(Boolean.parseBoolean(textContent));
            }
            if ("params".equals(nodeName)) {
                NodeList paramLlist = childElem.getChildNodes();
                if (list.getLength() > 0) {
                    info.setParams(new ArrayList<PluginParam>());
                }
                for (int j = 0, m = paramLlist.getLength(); j < m; j++) {
                    Node node = paramLlist.item(j);
                    if ("param".equals(node.getNodeName())) {
                        NamedNodeMap map = node.getAttributes();
                        PluginParam pp = new PluginParam();
                        for (int k = 0, o = map.getLength(); k < o; k++) {
                            if ("name".equals(map.item(k).getNodeName())) {
                                pp.setName(nullNodeToString(map.item(k)));
                            }
                            if ("value".equals(map.item(k).getNodeName())) {
                                pp.setValue(nullNodeToString(map.item(k)));
                            }
                        }
                        info.getParams().add(pp);
                    }
                }
            }
        }
        return info;
    }

    /**
     * Sets thumbs URL used by ConfigurationFacotry.
     *
     * @param url current thumbs url
     */
    @Override
    public void setThumbsURL(final String url) {
        this.thumbsURL = url;
    }

    /**
     * Sets thumbnails directory used by ConfigurationFacotry.
     *
     * @param dir current thumbs directory.
     */
    @Override
    public void setThumbsDir(final String dir) {
        this.thumbsDir = dir;
    }

    /**
     * Sets debug mode.
     *
     * @param mode current debug mode
     */
    @Override
    public final void setDebugMode(final boolean mode) {
        this.debug = mode;
    }

    /**
     * Clones current configuration instance and copies it's all fields.
     *
     * @return cloned configuration
     */
    @Override
    public final IConfiguration cloneConfiguration() {
        Configuration configuration = createConfigurationInstance();
        copyConfFields(configuration);
        return configuration;
    }

    /**
     * Creates current configuration class instance. In every subclass this method should be overridden and return new configuration
     * instance.
     *
     * @return new configuration instance
     */
    protected Configuration createConfigurationInstance() {
        return new Configuration(this.servletConf);
    }

    /**
     * Copies configuration fields.
     *
     * @param configuration destination configuration
     */
    protected void copyConfFields(final Configuration configuration) {
        configuration.loading = this.loading;
        configuration.xmlFilePath = this.xmlFilePath;
        configuration.debug = this.debug;
        configuration.lastCfgModificationDate = this.lastCfgModificationDate;
        configuration.enabled = this.enabled;
        configuration.xmlFilePath = this.xmlFilePath;
        configuration.baseDir = this.baseDir;
        configuration.baseURL = this.baseURL;
        configuration.licenseName = this.licenseName;
        configuration.licenseKey = this.licenseKey;
        configuration.imgWidth = this.imgWidth;
        configuration.imgHeight = this.imgHeight;
        configuration.imgQuality = this.imgQuality;
        configuration.thumbsEnabled = this.thumbsEnabled;
        configuration.thumbsURL = this.thumbsURL;
        configuration.thumbsDir = this.thumbsDir;
        configuration.thumbsDirectAccess = this.thumbsDirectAccess;
        configuration.thumbsMaxHeight = this.thumbsMaxHeight;
        configuration.thumbsMaxWidth = this.thumbsMaxWidth;
        configuration.thumbsQuality = this.thumbsQuality;
        configuration.doubleExtensions = this.doubleExtensions;
        configuration.forceASCII = this.forceASCII;
        configuration.disallowUnsafeCharacters = this.disallowUnsafeCharacters;
        configuration.enableCsrfProtection = this.enableCsrfProtection;
        configuration.checkSizeAfterScaling = this.checkSizeAfterScaling;
        configuration.secureImageUploads = this.secureImageUploads;
        configuration.uriEncoding = this.uriEncoding;
        configuration.userRoleSessionVar = this.userRoleSessionVar;
        configuration.events = this.events;
        configuration.basePathBuilder = this.basePathBuilder;

        configuration.htmlExtensions = new ArrayList<String>();
        configuration.htmlExtensions.addAll(this.htmlExtensions);
        configuration.hiddenFolders = new ArrayList<String>();
        configuration.hiddenFiles = new ArrayList<String>();
        configuration.hiddenFiles.addAll(this.hiddenFiles);
        configuration.hiddenFolders.addAll(this.hiddenFolders);
        configuration.defaultResourceTypes = new LinkedHashSet<String>();
        configuration.defaultResourceTypes.addAll(this.defaultResourceTypes);
        configuration.types = new LinkedHashMap<String, ResourceType>();
        configuration.accessControlLevels = new AccessControlLevelsList<AccessControlLevel>(false);
        configuration.plugins = new ArrayList<PluginInfo>();
        copyTypes(configuration.types);
        copyACls(configuration.accessControlLevels);
        copyPlugins(configuration.plugins);
    }

    /**
     * Copies plugins for new configuration.
     *
     * @param newPlugins new configuration plugins list.
     */
    private void copyPlugins(final List<PluginInfo> newPlugins) {
        for (PluginInfo pluginInfo : this.plugins) {
            newPlugins.add(new PluginInfo(pluginInfo));
        }
    }

    /**
     * Copies ACL for new configuration.
     *
     * @param newAccessControlLevels new configuration ACL list.
     */
    private void copyACls(final AccessControlLevelsList<AccessControlLevel> newAccessControlLevels) {
        for (AccessControlLevel acl : this.accessControlLevels) {
            newAccessControlLevels.addItem(new AccessControlLevel(acl), false);
        }
    }

    /**
     * Copies resource types for new configuration.
     *
     * @param newTypes new configuration resource types list.
     */
    private void copyTypes(final Map<String, ResourceType> newTypes) {
        for (String name : this.types.keySet()) {
            newTypes.put(name, new ResourceType(this.types.get(name)));
        }
    }
}
