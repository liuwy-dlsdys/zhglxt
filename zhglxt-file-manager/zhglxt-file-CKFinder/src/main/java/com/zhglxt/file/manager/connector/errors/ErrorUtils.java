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
package com.zhglxt.file.manager.connector.errors;

import com.zhglxt.file.manager.connector.ConnectorServlet;
import com.zhglxt.file.manager.connector.configuration.Constants;
import com.zhglxt.file.manager.connector.configuration.IConfiguration;
import org.jboss.vfs.TempFileProvider;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VFSUtils;
import org.jboss.vfs.VirtualFile;
import org.jboss.vfs.spi.MountHandle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * Error utils.
 */
public final class ErrorUtils {

    private static ErrorUtils errorUtils;

    private static Map<String, Map<Integer, String>> langMap;

    /**
     * Gets error message by locale code.
     *
     * @param errorCode error number
     * @param lang      connector language code
     * @param conf      connector configuration object
     * @return localized error message.
     */
    public String getErrorMsgByLangAndCode(final String lang,
                                           final int errorCode,
                                           final IConfiguration conf) {
        if (lang != null && langMap.get(lang) != null) {
            return langMap.get(lang).get(errorCode);
        } else if (langMap.get(Constants.DEFAULT_LANG_CODE) != null) {
            return langMap.get(Constants.DEFAULT_LANG_CODE).get(errorCode);
        } else {
            if (conf.isDebugMode()) {
                return "Unable to load error message";
            } else {
                return "";
            }
        }
    }

    /**
     * Returns or creates and returns {@code ErrorUtils} instance.
     *
     * @return {@code ErrorUtils} instance.
     */
    public static ErrorUtils getInstance() {
        if (errorUtils == null) {
            errorUtils = new ErrorUtils();
        }
        return errorUtils;
    }

    /**
     * Default constructor which creates {@code ErrorUtils} instance and initializes list of available languages supported by CKFinder.
     */
    private ErrorUtils() {
        List<String> allAvailLangCodes = getLangCodeFromJars();
        langMap = new HashMap<String, Map<Integer, String>>();
        for (String langCode : allAvailLangCodes) {
            langMap.put(langCode, getMessagesByLangCode(langCode));
        }
    }

    /**
     * Gets language codes from jar file holding locale files.
     *
     * @return {@code List} with language codes.
     */
    private List<String> getLangCodeFromJars() {
        List<String> langFiles = new ArrayList<String>();
        try {
            URL dirURL = ConnectorServlet.class.getResource("/lang/");
            // #768 there was a problem that files were loaded from work not from jar
            // in work we can get list of files from standard directory
            String protocol = dirURL.getProtocol();
            if ("file".equalsIgnoreCase(protocol)) {
                // getPath returns path with spaces converted to "%20"
                // It's a Java feature: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4466485
                // The path needs to be decoded first with URLDecoder
                String path = URLDecoder.decode(dirURL.getPath(), "UTF-8");
                File f = new File(path);
                if (f.exists() && f.isDirectory()) {
                    for (File file : f.listFiles()) {
                        langFiles.add(file.getName().replaceAll(".xml", ""));
                    }
                }
            } else if ("jar".equalsIgnoreCase(protocol) || "zip".equalsIgnoreCase(protocol)) {
                String jarPath;
                // If url is path to jar content we have to get it other way. Remove "file:" and all chars after !
                // In Weblogic path starts with drive so we just have to remove chars after !
                jarPath = dirURL.getPath().substring("zip".equalsIgnoreCase(protocol) ? 0 : 5, dirURL.getPath().indexOf("!"));
                // change "%20" to " "
                JarFile jarFile = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
                Enumeration<JarEntry> entries = jarFile.entries();
                Pattern pattern = Pattern.compile("lang.+\\.xml");
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = (JarEntry) entries.nextElement();
                    if (checkJarEntry(jarEntry, pattern)) {
                        langFiles.add(jarEntry.getName().replaceAll("lang/", "").replaceAll(".xml", ""));
                    }
                }
            } else if ("vfs".equalsIgnoreCase(protocol)) {//JBoss vfs
                VirtualFile langDir = VFS.getChild(dirURL.getPath());
                List<VirtualFile> langFilesList = langDir.getChildren();
                if (langFilesList.size() > 0) { //This will work in JBOSS 7 with exploded and packed war.
                    for (VirtualFile vFile : langFilesList) {
                        File contentsFile = vFile.getPhysicalFile();
                        langFiles.add(contentsFile.getName().replaceAll(".xml", ""));
                    }
                } else { //It's JBOSS 6.
                    VirtualFile jarFile = VFS.getChild(dirURL.getPath().substring(1, dirURL.getPath().indexOf(".jar") + 4));
                    if (jarFile.exists()) { //war is exploded.
                        TempFileProvider tempFileProvider = TempFileProvider.create("tmpjar", Executors.newScheduledThreadPool(2));
                        MountHandle jarHandle = (MountHandle) VFS.mountZip(jarFile, jarFile, tempFileProvider);
                        File mountJar = jarHandle.getMountSource();
                        addFileNamesToList(mountJar, jarFile, langFiles);
                        VFSUtils.safeClose(jarHandle);
                    } else {//war is packed.
                        VirtualFile warFile = VFS.getChild(dirURL.getPath().substring(1, dirURL.getPath().indexOf(".war") + 4));
                        if (warFile.exists()) {
                            TempFileProvider tempWarFileProvider = TempFileProvider.create("tmpwar", Executors.newScheduledThreadPool(2));
                            MountHandle warHandle = (MountHandle) VFS.mountZip(warFile, warFile, tempWarFileProvider);
                            File mountWar = warHandle.getMountSource();
                            if (mountWar.exists()) {
                                TempFileProvider tempJarFileProvider = TempFileProvider.create("tmpjar", Executors.newScheduledThreadPool(2));
                                MountHandle jarHandle = (MountHandle) VFS.mountZip(jarFile, jarFile, tempJarFileProvider);
                                File mountJar = jarHandle.getMountSource();
                                addFileNamesToList(mountJar, jarFile, langFiles);
                                VFSUtils.safeClose(jarHandle);
                                VFSUtils.safeClose(warHandle);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            return null;
        }
        return langFiles;
    }

    /**
     * Adds language codes to {@code List}.
     *
     * @param mountJar  mounted source file
     * @param jarFile   virtual jar file
     * @param langFiles list of string codes representing supported languages.
     */
    private void addFileNamesToList(File mountJar, VirtualFile jarFile, List<String> langFiles) throws IOException {
        if (mountJar.exists()) {
            List<VirtualFile> jarLangFilesList = jarFile.getChild("lang").getChildrenRecursively();
            for (VirtualFile vFile : jarLangFilesList) {
                File contentsFile = vFile.getPhysicalFile();
                langFiles.add(contentsFile.getName().replaceAll(".xml", ""));
            }
        }
    }

    /**
     * Checks if jar entry, provided as parameter, is locale file.
     *
     * @param jarEntry jar entry to check
     * @return {@code true} if jar entry is locale file, {@code false} otherwise.
     */
    private boolean checkJarEntry(final JarEntry jarEntry, Pattern pattern) {
        return pattern.matcher(jarEntry.getName()).matches();
    }

    /**
     * Gets localized messages from locale file by language code provided as parameter.
     *
     * @param langCode language code used to get localized messages
     * @return {@code Map} of localized messages.
     */
    private Map<Integer, String> getMessagesByLangCode(final String langCode) {
        Map<Integer, String> langCodeMap = new HashMap<Integer, String>();
        try {
            InputStream is = ConnectorServlet.class.getResourceAsStream("/lang/" + langCode + ".xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(is);
            NodeList unkonwErrornodeList = doc.getElementsByTagName("errorUnknown");
            NodeList errorNodeList = doc.getElementsByTagName("error");
            Element unkonwErrorElem = (Element) unkonwErrornodeList.item(0);
            langCodeMap.put(1, unkonwErrorElem.getTextContent());

            for (int i = 0, j = errorNodeList.getLength(); i < j; i++) {
                Element element = (Element) errorNodeList.item(i);
                langCodeMap.put(Integer.valueOf(element.getAttribute("number")),
                        element.getTextContent());
            }
        } catch (Exception e) {
            return null;
        }
        return langCodeMap;
    }
}
