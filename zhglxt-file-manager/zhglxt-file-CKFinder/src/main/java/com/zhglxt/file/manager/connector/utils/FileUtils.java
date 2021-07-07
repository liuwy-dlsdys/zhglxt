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

import com.zhglxt.file.manager.connector.ServletContextFactory;
import com.zhglxt.file.manager.connector.configuration.Constants;
import com.zhglxt.file.manager.connector.configuration.IConfiguration;
import com.zhglxt.file.manager.connector.data.ResourceType;
import com.zhglxt.file.manager.connector.errors.ConnectorException;
import org.apache.commons.fileupload.FileItem;

import javax.servlet.ServletContext;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utils for files.
 */
public class FileUtils {
    public static final String FILENAME_WITHOUT_EXTENSION = "no_ext";

    /**
     * max read file buffer size.
     */
    private static final int MAX_BUFFER_SIZE = 1024;
    private static final Map<String, String> UTF8_LOWER_ACCENTS = new HashMap<String, String>();
    private static final Map<String, String> UTF8_UPPER_ACCENTS = new HashMap<String, String>();
    private static final Map<String, String> encodingMap;
    private static final Pattern drivePatt = Pattern.compile("^[a-zA-Z]{1}:[/\\\\]");
    private static final Pattern invalidFileNamePatt = Pattern.compile(Constants.INVALID_FILE_NAME_REGEX);
    private static final Logger fileUtilsLogger = Logger.getLogger(FileUtils.class.getName());
    private static final String WEB_INF_FOLDER_NAME = "/WEB-INF/";
    private static final String ROOT_FOLDER_NAME = "/ROOT";
    private static final String DOMAINS_FOLDER_NAME = "/domains/";
    private static final String DOCROOT_FOLDER_NAME = "/docroot";
    private static final String CKFINDER_FOLDER_NAME = "/ckfinder";
    private static String fuClassPath;

    static {
        Map<String, String> mapHelper = new HashMap<String, String>();
        mapHelper.put("%21", "!");
        mapHelper.put("%27", "'");
        mapHelper.put("%28", "(");
        mapHelper.put("%29", ")");
        mapHelper.put("%7E", "~");
        mapHelper.put("[+]", "%20");
        encodingMap = Collections.unmodifiableMap(mapHelper);
    }

    /**
     * Gets list of children folder or files for dir, according to searchDirs param.
     *
     * @param dir        folder to search.
     * @param searchDirs if true method return list of folders, otherwise list of files.
     * @return list of files or subdirectories in selected directory
     */
    public static List<String> findChildrensList(final File dir,
                                                 final boolean searchDirs) {
        List<String> files = new ArrayList<String>();
        for (String subFiles : dir.list()) {
            File file = new File(dir + "/" + subFiles);
            if ((searchDirs && file.isDirectory())
                    || (!searchDirs && !file.isDirectory())) {
                files.add(file.getName());
            }
        }
        return files;
    }

    /**
     * Gets file extension.
     *
     * @param fileName           name of file.
     * @param shortExtensionMode shortExtensionMode
     * @return file extension
     */
    public static String getFileExtension(final String fileName, final boolean shortExtensionMode) {
        if (shortExtensionMode) {
            return FileUtils.getFileExtension(fileName);
        }
        if (fileName == null
                || fileName.indexOf(".") == -1
                || fileName.indexOf(".") == fileName.length() - 1) {
            return FILENAME_WITHOUT_EXTENSION;
        }
        return fileName.substring(fileName.indexOf(".") + 1);
    }

    /**
     * Gets file last extension.
     *
     * @param fileName name of file.
     * @return file extension
     */
    public static String getFileExtension(final String fileName) {
        if (fileName == null
                || fileName.lastIndexOf(".") == -1
                || fileName.lastIndexOf(".") == fileName.length() - 1) {
            return FILENAME_WITHOUT_EXTENSION;
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * Gets file name without its extension.
     *
     * @param fileName           name of file
     * @param shortExtensionMode shortExtensionMode
     * @return file extension
     */
    public static String getFileNameWithoutExtension(final String fileName, final boolean shortExtensionMode) {
        if (shortExtensionMode) {
            return FileUtils.getFileNameWithoutExtension(fileName);
        }

        if (fileName == null) {
            return "";
        }

        if (fileName.indexOf(".") == -1) {
            return fileName;
        }

        return fileName.substring(0, fileName.indexOf("."));
    }

    /**
     * Gets file name without its last extension.
     *
     * @param fileName name of file
     * @return file extension
     */
    public static String getFileNameWithoutExtension(final String fileName) {
        if (fileName == null) {
            return null;
        }

        if (fileName.lastIndexOf(".") == -1) {
            return fileName;
        }

        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     * Print file content to outputstream.
     *
     * @param file file to be printed.
     * @param out  outputstream.
     * @throws IOException when io error occurs.
     */
    public static void printFileContentToResponse(final File file,
                                                  final OutputStream out) throws IOException {
        FileInputStream in = null;
        if (file.length() == 0) {
            return;
        }
        try {
            in = new FileInputStream(file);
            byte[] buf;
            if (file.length() < MAX_BUFFER_SIZE) {
                buf = new byte[(int) file.length()];
            } else {
                buf = new byte[MAX_BUFFER_SIZE];
            }

            int numRead;
            while ((numRead = in.read(buf)) != -1) {
                out.write(buf, 0, numRead);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                fileUtilsLogger.log(Level.SEVERE, "Error when closing stream.", e);
            }
        }
    }

    /**
     * @param sourceFile source file
     * @param destFile   destination file
     * @param move       if source file should be deleted.
     * @param conf       connector configuration
     * @return true if file moved/copied correctly
     * @throws IOException when IOerror occurs
     */
    public static boolean copyFromSourceToDestFile(final File sourceFile,
                                                   final File destFile,
                                                   final boolean move,
                                                   final IConfiguration conf)
            throws IOException {
        createPath(destFile, true);
        InputStream in = new FileInputStream(sourceFile);
        OutputStream out = new FileOutputStream(destFile);
        byte[] buf = new byte[MAX_BUFFER_SIZE];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
        if (move) {
            sourceFile.delete();
        }
        return true;

    }

    /**
     * Gets an absolute path to CKFinder file or folder for which path was provided as parameter.
     *
     * @param path        relative or absolute path to a CKFinder resource (file or folder).
     * @param isAbsolute  flag indicating if path to resource is absolute e.g. /usr/john/userfiles or "C:\\userfiles". If this parameter is
     *                    set to true path will be returned as is.
     * @param shouldExist flag indicating if resource, represented by path parameter, should exist (e.g. configuration file) in file system
     *                    or not (e.g. userfiles folder).<br>
     *                    If this parameter is set to true, path to file will only be returned if such file exists. If file can't be found, method will return
     *                    null.
     * @return an absolute path to a resource in CKFinder
     * @throws ConnectorException when {@code ServletContext} is {@code null} or full path to resource cannot be obtained.
     */
    public static String getFullPath(String path, boolean isAbsolute, boolean shouldExist) throws ConnectorException {
        if (path != null && !path.equals("")) {
            if (isAbsolute) {
                if (path.startsWith("/")) {
                    //Check if this isn't Windows Path.
                    String temporary = PathUtils.removeSlashFromBeginning(path);
                    if (isStartsWithPattern(drivePatt, temporary)) {
                        path = temporary;
                    }
                }
                return checkAndReturnPath(shouldExist, path);
            } else {
                ServletContext sc = ServletContextFactory.getServletContext();
                String tempPath = PathUtils.addSlashToEnd(PathUtils.addSlashToBeginning(path));
                try {
                    java.net.URL url = sc.getResource(tempPath);
                    //For srevers like Tomcat 6-7 the getResource method returns JNDI URL.
                    if (url != null && url.getProtocol() != null && url.getProtocol().equalsIgnoreCase("jndi")) {
                        //Assume file is inside application context and try to get path.
                        //This method will fail if war is not exploaded.
                        String result = sc.getRealPath(tempPath.replace(sc.getContextPath(), ""));
                        if (result != null) {
                            return result;
                        } else {
                            //If application is packed, we have to try constructing the path manually.
                            result = getClassPath();
                            if (tempPath.indexOf(sc.getContextPath() + "/") >= 0
                                    && result.indexOf(sc.getContextPath() + "/") >= 0) {
                                result = result.substring(0, result.indexOf(sc.getContextPath()));
                                result = result + tempPath;
                            } else if (result.indexOf(sc.getContextPath() + "/") >= 0) {
                                result = result.substring(0,
                                        result.indexOf(sc.getContextPath()) + sc.getContextPath().length());
                                result = result + tempPath;
                            }

                            result = checkAndReturnPath(shouldExist, result);
                            if (result != null) {
                                return result;
                            }
                        }

                        //At this stage path is not in application context and is not absolute.
                        //We need to reset url as we cannot determine path from it.
                        if (result == null) {
                            url = null;
                        }
                    }

                    //For servers like Tomact 8 getResource method should return file:// url.
                    if (path.startsWith("/") || isStartsWithPattern(drivePatt, path)) {
                        //This is most likely absolute path.
                        String absolutePath = checkAndReturnPath(shouldExist, path);
                        if (absolutePath != null && !absolutePath.equals("")) {
                            return absolutePath;
                        } else {
                            //If absolute path has failed, give it one last try with relative path.
                            //Either path or null will be returned.
                            return sc.getRealPath(path.replace(sc.getContextPath(), ""));
                        }
                    }
                } catch (IOException ioex) {
                    throw new ConnectorException(ioex);
                }
            }
        }
        return null;
    }

    /**
     * Checks and returns path to resource. If shouldExist parameter is set to true, resource represented by path will be checked for
     * existence. If resource exists path will be returned, {@code null} otherwise. If shouldExist parameter is set to false path will be
     * returned as is.
     *
     * @param shouldExist flag indicating if file/folder represented by path parameter should exist in file system
     * @param path        string representing path to file or folder
     * @return string representing path to file or null if file should exist in file system but can't be read with a given path.
     */
    private static String checkAndReturnPath(boolean shouldExist, String path) {
        if (!shouldExist) {
            return path;
        } else {
            if (isFileExist(path)) {
                return path;
            }
            return null;
        }
    }

    /**
     * Checks if file/folder specified by the path exists in file system.
     *
     * @param path absolute path to file/folder used to check file existence
     * @return {@code true} if file/folder exists, {@code false} otherwise.
     */
    private static boolean isFileExist(String path) {
        File f = new File(path);
        return f.exists();
    }

    /**
     * Gets an absolute path to CKFinder resource folder for which path is calculated from baseURL configuration property.<br>
     * This method has limited capabilities. First it will check is folder should be created in application context. If not it will try to
     * create folder in Tomcat ROOT folder or GlassFish docroot folder. If this fails this method will fall back and try to create folder
     * inside application context (features like gallery may not work in this case as most likely baseURL and baseDir point to different
     * locations).<br>
     * Finally if nothing worked this method returns {@code null}.
     *
     * @param path relative or absolute path to a CKFinder folder.
     * @return an absolute path to a folder in CKFinder
     * @throws ConnectorException when {@code ServletContext} is {@code null} or path to resource cannot be obtained.
     */
    public static String calculatePathFromBaseUrl(String path) throws ConnectorException {
        if (path != null && !path.equals("")) {
            ServletContext sc = ServletContextFactory.getServletContext();
            String tempPath = PathUtils.addSlashToBeginning(path);
            String finalPath;
            if (tempPath.startsWith(sc.getContextPath() + "/")) {
                //Try creating path relative to application context.
                if ((finalPath = sc.getRealPath(tempPath.replace(sc.getContextPath(), ""))) != null) {
                    return finalPath;
                } else if ((finalPath = sc.getRealPath(CKFINDER_FOLDER_NAME)) != null) {
                    //If above is null, try getting path to direct subfolder in application context.
                    finalPath = PathUtils.escape(finalPath);
                    return finalPath.substring(0, finalPath.lastIndexOf(CKFINDER_FOLDER_NAME)) + tempPath.replace(sc.getContextPath(), "");
                } else {
                    finalPath = getClassPath();
                    if (finalPath.indexOf(sc.getContextPath()) >= 0) {
                        finalPath = finalPath.substring(0, finalPath.indexOf(sc.getContextPath()));
                        finalPath = finalPath + tempPath;
                        return finalPath;
                    } else {
                        finalPath = null;
                    }
                }
            } else {
                //Try creating path to ROOT on TC or docroot on GF
                finalPath = getClassPath();

                String tcPath = getTomcatRootPath(sc, finalPath);
                String gfPath = getGlassFishRootPath(sc, finalPath);
                if (!tcPath.equals("")) {
                    tempPath = filterRelativePathChars(tempPath);
                    finalPath = tcPath + tempPath;
                } else if (!gfPath.equals("")) {
                    tempPath = filterRelativePathChars(tempPath);
                    finalPath = gfPath + tempPath;
                } else {
                    //Fall back and try creating path relative application context
                    String realPath = sc.getRealPath(tempPath);
                    if (realPath != null) {
                        return realPath;
                    } else if (finalPath.indexOf(sc.getContextPath() + "/") >= 0) {
                        finalPath = finalPath.substring(0, finalPath.indexOf(sc.getContextPath()) + sc.getContextPath().length());
                        tempPath = filterRelativePathChars(tempPath);
                        finalPath = finalPath + tempPath;
                    } else {
                        finalPath = null;
                    }
                }
            }
            return finalPath;
        }
        return null;
    }

    /**
     * Returns Path to Tomcat ROOT folder.
     *
     * @param sc   current {@code ServletContext} object.
     * @param path String from which path to Tomcat ROOT folder will be calculated
     * @return path to Tomcat ROOT folder or empty String is path can't be found.
     */
    private static String getTomcatRootPath(ServletContext sc, String path) {
        String finalPath = "";
        int index = path.indexOf(sc.getContextPath() + WEB_INF_FOLDER_NAME);
        if (index >= 0) {
            path = path.substring(0, index);
            path = path + ROOT_FOLDER_NAME;
            if (isFileExist(path)) {
                finalPath = path;
            }
        }
        return finalPath;
    }

    /**
     * Returns Path to GlassFish docroot folder.
     *
     * @param sc   current {@code ServletContext} object.
     * @param path String from which path to GlassFish docroot folder will be calculated
     * @return path to GlassFish docroot folder or empty String is path can't be found.
     */
    private static String getGlassFishRootPath(ServletContext sc, String path) {
        String finalPath = "";
        path = path.toLowerCase();
        int index = path.indexOf(DOMAINS_FOLDER_NAME);
        if (index >= 0) {
            path = PathUtils.addSlashToEnd(path);
            String key = path.substring(index + DOMAINS_FOLDER_NAME.length());
            if (!key.equals("")) {
                if (key.indexOf("/") > 0) {
                    key = key.substring(0, key.indexOf("/"));
                }
                path = path.substring(0, path.indexOf(key) + key.length()) + DOCROOT_FOLDER_NAME;
            }
            if (isFileExist(path)) {
                finalPath = path;
            }
        }
        return finalPath;
    }

    /**
     * This method removes any ".." characters from the path provided as parameter.
     *
     * @param path string representing path to remove unsafe characters from
     * @return filtered path without ".." characters.
     */
    private static String filterRelativePathChars(String path) {
        StringBuffer s = new StringBuffer(path);
        int index = s.indexOf("..");
        if (index >= 0) {
            s = s.delete(index, index + 2);
        }
        return s.toString();
    }

    /**
     * Checks if path starts with particular regular expression pattern.
     *
     * @param pattern the regular expression used to test the path
     * @param path    the string representing path to test against regular expression
     * @return {@code true} if path starts with given pattern, {@code false} otherwise.
     */
    private static boolean isStartsWithPattern(Pattern pattern, String path) {
        Matcher m = pattern.matcher(path);
        if (m.find()) {
            return m.start() == 0;
        }
        return false;
    }

    /**
     * Gets absolute path to FileUtils.java file. This path is later used to calculate absolute path to other resources inside application.
     *
     * @return absolute path to FileUtils.java file.
     */
    private static String getClassPath() throws ConnectorException {
        if (fuClassPath == null || fuClassPath.equals("")) {
            java.net.URL url = FileUtils.class.getResource("FileUtils.class");
            String finalPath = null;
            String filePathPrefix = "file:/";

            if ("vfs".equalsIgnoreCase(url.getProtocol())) {
                try {
                    org.jboss.vfs.VirtualFile vFile = org.jboss.vfs.VFS.getChild(url.getPath());
                    finalPath = org.jboss.vfs.VFSUtils.getPhysicalURI(vFile).getPath();
                } catch (IOException ioex) {
                    throw new ConnectorException(ioex);
                }
            } else {
                try {
                    finalPath = url.toURI().getSchemeSpecificPart();
                } catch (URISyntaxException ueex) {
                    throw new ConnectorException(ueex);
                }
            }

            if (finalPath != null && finalPath.startsWith(filePathPrefix)) {
                finalPath = finalPath.substring(filePathPrefix.length());
            }

            if (finalPath != null && finalPath.startsWith("/")) {
                //Check if this isn't Windows Path.
                String temporary = PathUtils.removeSlashFromBeginning(finalPath);
                if (isStartsWithPattern(drivePatt, temporary)) {
                    finalPath = temporary;
                }
            }
            fuClassPath = finalPath;
        }
        return fuClassPath;
    }

    /**
     * Parse date with pattern yyyyMMddHHmm. Pattern is used in get file command response XML.
     *
     * @param file input file.
     * @return parsed file modification date.
     */
    public static String parseLastModifDate(final File file) {
        Date date = new Date(file.lastModified());
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        return dateFormat.format(date);
    }

    /**
     * check if dirname matches configuration hidden folder regex.
     *
     * @param dirName dir name
     * @param conf    connector configuration
     * @return true if matches.
     */
    public static boolean checkIfDirIsHidden(final String dirName,
                                             final IConfiguration conf) {
        if (dirName == null || dirName.equals("")) {
            return false;
        }
        String dir = PathUtils.removeSlashFromEnd(PathUtils.escape(dirName));
        Scanner sc = new Scanner(dir).useDelimiter("/");
        while (sc.hasNext()) {
            boolean check = Pattern.compile(getHiddenFileOrFolderRegex(
                    conf.getHiddenFolders())).matcher(sc.next()).matches();
            if (check) {
                return true;
            }
        }
        return false;
    }

    /**
     * check if filename matches configuration hidden file regex.
     *
     * @param fileName file name
     * @param conf     connector configuration
     * @return true if matches.
     */
    public static boolean checkIfFileIsHidden(final String fileName,
                                              final IConfiguration conf) {
        return Pattern.compile(getHiddenFileOrFolderRegex(
                conf.getHiddenFiles())).matcher(fileName).matches();
    }

    /**
     * get hidden folder regex pattern.
     *
     * @param hiddenList list of hidden file or files patterns.
     * @return full folder regex pattern
     */
    private static String getHiddenFileOrFolderRegex(final List<String> hiddenList) {
        StringBuilder sb = new StringBuilder("(");
        for (String item : hiddenList) {
            if (sb.length() > 3) {
                sb.append("|");
            }

            item = item.replaceAll("\\.", "\\\\.");
            item = item.replaceAll("\\*", ".+");
            item = item.replaceAll("\\?", ".");

            sb.append("(");
            sb.append(item);
            sb.append(")");

        }
        sb.append(")+");
        return sb.toString();
    }

    /**
     * deletes file or folder with all subfolders and subfiles.
     *
     * @param file file or directory to delete.
     * @return true if all files are deleted.
     */
    public static boolean delete(final File file) {

        if (file.isDirectory()) {
            for (String item : file.list()) {
                File subFile = new File(file.getAbsolutePath()
                        + File.separator + item);
                if (!delete(subFile)) {
                    return false;
                }
            }
        }
        return file.delete();
    }

    /**
     * check if file or folder name doesn't match invalid name.
     *
     * @param fileName file name
     * @return true if file name is correct
     */
    public static boolean checkFileName(final String fileName) {
        return !(fileName == null || fileName.equals("")
                || fileName.charAt(fileName.length() - 1) == '.'
                || fileName.contains("..")
                || checkFolderNamePattern(fileName));
    }

    /**
     * check if new folder name contains disallowed chars.
     *
     * @param fileName file name
     * @return true if it does contain disallowed characters.
     */
    private static boolean checkFolderNamePattern(final String fileName) {
        return invalidFileNamePatt.matcher(fileName).find();
    }

    /**
     * checks if file extension is on denied list or isn't on allowed list.
     *
     * @param fileName filename
     * @param type     resource type
     * @return 0 if ok, 1 if not ok, 2 if rename required
     */
    public static int checkFileExtension(final String fileName,
                                         final ResourceType type) {
        if (type == null || fileName == null) {
            return 1;
        }

        if (fileName.indexOf('.') == -1) {
            return checkSingleExtension(null, type) ? 0 : 1;
        }

        return checkSingleExtension(getFileExtension(fileName), type) ? 0 : 1;
    }

    /**
     * Checks whether files extension is allowed.
     *
     * @param fileExt a string representing file extension to test
     * @param type    a {@code ResourceType} object holding list of allowed and denied extensions against which parameter fileExt will be
     *                tested
     * @return {@code true} is extension is on allowed extensions list or if allowed extensions is empty. The {@code false} is returned when
     * file is on denied extensions list or if none of the above conditions is met.
     */
    private static boolean checkSingleExtension(String fileExt, final ResourceType type) {
        if (FILENAME_WITHOUT_EXTENSION.equalsIgnoreCase(fileExt)) {
            return false;
        }

        if (fileExt == null || fileExt.trim().isEmpty()) {
            fileExt = FILENAME_WITHOUT_EXTENSION;
        }

        Scanner scanner = new Scanner(type.getDeniedExtensions()).useDelimiter(",");
        while (scanner.hasNext()) {
            if (scanner.next().equalsIgnoreCase(fileExt)) {
                return false;
            }
        }

        scanner = new Scanner(type.getAllowedExtensions()).useDelimiter(",");
        //The allowedExtensions is empty. Allow everything that isn't dissallowed.
        if (!scanner.hasNext()) {
            return true;
        }

        while (scanner.hasNext()) {
            if (scanner.next().equalsIgnoreCase(fileExt)) {
                return true;
            }
        }
        return false;
    }

    /**
     * converts filename to connector encoding.
     *
     * @param fileName      file name
     * @param configuration connector configuration
     * @return encoded file name
     */
    public static String convertFromUriEncoding(final String fileName,
                                                final IConfiguration configuration) {
        try {
            return new String(fileName.getBytes(configuration.getUriEncoding()), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return fileName;
        }
    }

    /**
     * converts filename to ASCII.
     *
     * @param fileName file name
     * @return encoded file name
     */
    public static String convertToASCII(final String fileName) {
        String newFileName = fileName;
        fillLowerAccents();
        fillUpperAccents();
        for (String s : UTF8_LOWER_ACCENTS.keySet()) {
            newFileName = newFileName.replace(s, UTF8_LOWER_ACCENTS.get(s));
        }

        for (String s : UTF8_UPPER_ACCENTS.keySet()) {
            newFileName = newFileName.replace(s, UTF8_UPPER_ACCENTS.get(s));
        }
        return newFileName;
    }

    /**
     * creates file and all above folders that do not exist.
     *
     * @param file   file to create.
     * @param asFile if it is path to folder.
     * @throws IOException when io error occurs.
     */
    public static void createPath(final File file, final boolean asFile) throws IOException {
        String path = file.getAbsolutePath();

        String dirPath;
        if (asFile) {
            dirPath = path.substring(0, path.lastIndexOf(File.separator));
        } else {
            dirPath = path;
        }

        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (asFile) {
            file.createNewFile();
        }
    }

    /**
     * check if file size isn't bigger then max size for type.
     *
     * @param type     resource type
     * @param fileSize file size
     * @return true if file size isn't bigger then max size for type.
     */
    public static boolean checkFileSize(final ResourceType type, final long fileSize) {
        final Long maxSize = type.getMaxSize();
        return (maxSize == null || maxSize > fileSize);
    }

    /**
     * check if file has html file extension.
     *
     * @param file          file name
     * @param configuration connector configuration
     * @return true if has
     */
    public static boolean checkIfFileIsHtmlFile(final String file,
                                                final IConfiguration configuration) {

        return configuration.getHTMLExtensions().contains(
                getFileExtension(file).toLowerCase());

    }

    /**
     * Detect HTML in the first KB to prevent against potential security issue with IE/Safari/Opera file type auto detection bug. Returns
     * true if file contain insecure HTML code at the beginning.
     *
     * @param item file upload item
     * @return true if detected.
     * @throws IOException when io error occurs.
     */
    public static boolean detectHtml(final FileItem item) throws IOException {
        byte[] buff = new byte[MAX_BUFFER_SIZE];
        InputStream is = null;
        try {
            is = item.getInputStream();
            is.read(buff, 0, MAX_BUFFER_SIZE);
            String content = new String(buff);
            content = content.toLowerCase().trim();

            if (Pattern.compile("<!DOCTYPE\\W+X?HTML.+",
                    Pattern.CASE_INSENSITIVE
                            | Pattern.DOTALL
                            | Pattern.MULTILINE).matcher(content).matches()) {
                return true;
            }

            String[] tags = {"<body", "<head", "<html", "<img", "<pre",
                    "<script", "<table", "<title"};

            for (String tag : tags) {
                if (content.indexOf(tag) != -1) {
                    return true;
                }
            }

            if (Pattern.compile("type\\s*=\\s*[\'\"]?\\s*(?:\\w*/)?(?:ecma|java)",
                    Pattern.CASE_INSENSITIVE
                            | Pattern.DOTALL
                            | Pattern.MULTILINE).matcher(content).find()) {
                return true;
            }

            if (Pattern.compile(
                    "(?:href|src|data)\\s*=\\s*[\'\"]?\\s*(?:ecma|java)script:",
                    Pattern.CASE_INSENSITIVE
                            | Pattern.DOTALL
                            | Pattern.MULTILINE).matcher(content).find()) {
                return true;
            }

            if (Pattern.compile("url\\s*\\(\\s*[\'\"]?\\s*(?:ecma|java)script:",
                    Pattern.CASE_INSENSITIVE
                            | Pattern.DOTALL
                            | Pattern.MULTILINE).matcher(content).find()) {
                return true;
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return false;
    }

    /**
     * Checks if folder has any subfolders but respects ACL and hideFolders setting from configuration.
     *
     * @param dirPath         path to current folder.
     * @param dir             current folder being checked. Represented by File object.
     * @param configuration   configuration object.
     * @param resourceType    name of resource type, folder is assigned to.
     * @param currentUserRole user role.
     * @return true if there are any allowed and non-hidden subfolders.
     */
    public static Boolean hasChildren(String dirPath, File dir, IConfiguration configuration, String resourceType, String currentUserRole) {
        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        };
        File[] subDirsList = dir.listFiles(fileFilter);

        if (subDirsList != null) {
            for (File subDirsList1 : subDirsList) {
                String subDirName = subDirsList1.getName();
                if (!FileUtils.checkIfDirIsHidden(subDirName, configuration)
                        && AccessControlUtil.getInstance().checkFolderACL(resourceType,
                        dirPath + subDirName, currentUserRole, AccessControlUtil.CKFINDER_CONNECTOR_ACL_FOLDER_VIEW)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * rename file with double extension.
     *
     * @param type     a {@code ResourceType} object holding list of allowed and denied extensions against which file extension will be tested.
     * @param fileName file name
     * @return new file name with . replaced with _ (but not last)
     */
    public static String renameFileWithBadExt(final ResourceType type, final String fileName) {
        if (type == null || fileName == null) {
            return null;
        }

        if (fileName.indexOf('.') == -1) {
            return fileName;
        }

        StringTokenizer tokens = new StringTokenizer(fileName, ".");
        String cfileName = tokens.nextToken();
        String currToken;
        while (tokens.hasMoreTokens()) {
            currToken = tokens.nextToken();
            if (tokens.hasMoreElements()) {
                cfileName = cfileName.concat(checkSingleExtension(currToken, type) ? "." : "_");
                cfileName = cfileName.concat(currToken);
            } else {
                cfileName = cfileName.concat(".".concat(currToken));
            }
        }
        return cfileName;
    }

    public static String encodeURIComponent(final String fileName) throws UnsupportedEncodingException {
        String fileNameHelper = URLEncoder.encode(fileName, "utf-8");
        for (Map.Entry<String, String> entry : encodingMap.entrySet()) {
            fileNameHelper = fileNameHelper.replaceAll(entry.getKey(), entry.getValue());
        }
        return fileNameHelper;
    }

    public static boolean checkFolderName(final String folderName, final IConfiguration configuration) {
        return !((configuration.isDisallowUnsafeCharacters()
                && (folderName.contains(".") || folderName.contains(";")))
                || FileUtils.checkFolderNamePattern(folderName));
    }

    public static boolean checkFileName(final String fileName, final IConfiguration configuration) {
        return !((configuration.isDisallowUnsafeCharacters() && fileName.contains(";"))
                || !FileUtils.checkFileName(fileName));
    }

    public static String backupWithBackSlash(final String fileName, final String toReplace) {
        return fileName.replaceAll(toReplace, "\\\\" + toReplace);
    }

    /**
     * fills data for upper accents map.
     */
    private static void fillUpperAccents() {
        if (UTF8_UPPER_ACCENTS.size() == 0) {
            UTF8_UPPER_ACCENTS.put("À", "A");
            UTF8_UPPER_ACCENTS.put("Ô", "O");
            UTF8_UPPER_ACCENTS.put("Ď", "D");
            UTF8_UPPER_ACCENTS.put("Ḟ", "F");
            UTF8_UPPER_ACCENTS.put("Ë", "E");
            UTF8_UPPER_ACCENTS.put("Š", "S");
            UTF8_UPPER_ACCENTS.put("Ơ", "O");
            UTF8_UPPER_ACCENTS.put("Ă", "A");
            UTF8_UPPER_ACCENTS.put("Ř", "R");
            UTF8_UPPER_ACCENTS.put("Ț", "T");
            UTF8_UPPER_ACCENTS.put("Ň", "N");
            UTF8_UPPER_ACCENTS.put("Ā", "A");
            UTF8_UPPER_ACCENTS.put("Ķ", "K");
            UTF8_UPPER_ACCENTS.put("Ŝ", "S");
            UTF8_UPPER_ACCENTS.put("Ỳ", "Y");
            UTF8_UPPER_ACCENTS.put("Ņ", "N");
            UTF8_UPPER_ACCENTS.put("Ĺ", "L");
            UTF8_UPPER_ACCENTS.put("Ħ", "H");
            UTF8_UPPER_ACCENTS.put("Ṗ", "P");
            UTF8_UPPER_ACCENTS.put("Ó", "O");
            UTF8_UPPER_ACCENTS.put("Ú", "U");
            UTF8_UPPER_ACCENTS.put("Ě", "E");
            UTF8_UPPER_ACCENTS.put("É", "E");
            UTF8_UPPER_ACCENTS.put("Ç", "C");
            UTF8_UPPER_ACCENTS.put("Ẁ", "W");
            UTF8_UPPER_ACCENTS.put("Ċ", "C");
            UTF8_UPPER_ACCENTS.put("Õ", "O");
            UTF8_UPPER_ACCENTS.put("Ṡ", "S");
            UTF8_UPPER_ACCENTS.put("Ø", "O");
            UTF8_UPPER_ACCENTS.put("Ģ", "G");
            UTF8_UPPER_ACCENTS.put("Ŧ", "T");
            UTF8_UPPER_ACCENTS.put("Ș", "S");
            UTF8_UPPER_ACCENTS.put("Ė", "E");
            UTF8_UPPER_ACCENTS.put("Ĉ", "C");
            UTF8_UPPER_ACCENTS.put("Ś", "S");
            UTF8_UPPER_ACCENTS.put("Î", "I");
            UTF8_UPPER_ACCENTS.put("Ű", "U");
            UTF8_UPPER_ACCENTS.put("Ć", "C");
            UTF8_UPPER_ACCENTS.put("Ę", "E");
            UTF8_UPPER_ACCENTS.put("Ŵ", "W");
            UTF8_UPPER_ACCENTS.put("Ṫ", "T");
            UTF8_UPPER_ACCENTS.put("Ū", "U");
            UTF8_UPPER_ACCENTS.put("Č", "C");
            UTF8_UPPER_ACCENTS.put("Ö", "Oe");
            UTF8_UPPER_ACCENTS.put("È", "E");
            UTF8_UPPER_ACCENTS.put("Ŷ", "Y");
            UTF8_UPPER_ACCENTS.put("Ą", "A");
            UTF8_UPPER_ACCENTS.put("Ł", "L");
            UTF8_UPPER_ACCENTS.put("Ų", "U");
            UTF8_UPPER_ACCENTS.put("Ů", "U");
            UTF8_UPPER_ACCENTS.put("Ş", "S");
            UTF8_UPPER_ACCENTS.put("Ğ", "G");
            UTF8_UPPER_ACCENTS.put("Ļ", "L");
            UTF8_UPPER_ACCENTS.put("Ƒ", "F");
            UTF8_UPPER_ACCENTS.put("Ž", "Z");
            UTF8_UPPER_ACCENTS.put("Ẃ", "W");
            UTF8_UPPER_ACCENTS.put("Ḃ", "B");
            UTF8_UPPER_ACCENTS.put("Å", "A");
            UTF8_UPPER_ACCENTS.put("Ì", "I");
            UTF8_UPPER_ACCENTS.put("Ï", "I");
            UTF8_UPPER_ACCENTS.put("Ḋ", "D");
            UTF8_UPPER_ACCENTS.put("Ť", "T");
            UTF8_UPPER_ACCENTS.put("Ŗ", "R");
            UTF8_UPPER_ACCENTS.put("Ä", "Ae");
            UTF8_UPPER_ACCENTS.put("Í", "I");
            UTF8_UPPER_ACCENTS.put("Ŕ", "R");
            UTF8_UPPER_ACCENTS.put("Ê", "E");
            UTF8_UPPER_ACCENTS.put("Ü", "Ue");
            UTF8_UPPER_ACCENTS.put("Ò", "O");
            UTF8_UPPER_ACCENTS.put("Ē", "E");
            UTF8_UPPER_ACCENTS.put("Ñ", "N");
            UTF8_UPPER_ACCENTS.put("Ń", "N");
            UTF8_UPPER_ACCENTS.put("Ĥ", "H");
            UTF8_UPPER_ACCENTS.put("Ĝ", "G");
            UTF8_UPPER_ACCENTS.put("Đ", "D");
            UTF8_UPPER_ACCENTS.put("Ĵ", "J");
            UTF8_UPPER_ACCENTS.put("Ÿ", "Y");
            UTF8_UPPER_ACCENTS.put("Ũ", "U");
            UTF8_UPPER_ACCENTS.put("Ŭ", "U");
            UTF8_UPPER_ACCENTS.put("Ư", "U");
            UTF8_UPPER_ACCENTS.put("Ţ", "T");
            UTF8_UPPER_ACCENTS.put("Ý", "Y");
            UTF8_UPPER_ACCENTS.put("Ő", "O");
            UTF8_UPPER_ACCENTS.put("Ÿ", "Y");
            UTF8_UPPER_ACCENTS.put("Ũ", "U");
            UTF8_UPPER_ACCENTS.put("Ŭ", "U");
            UTF8_UPPER_ACCENTS.put("Ư", "U");
            UTF8_UPPER_ACCENTS.put("Ţ", "T");
            UTF8_UPPER_ACCENTS.put("Ý", "Y");
            UTF8_UPPER_ACCENTS.put("Ő", "O");
            UTF8_UPPER_ACCENTS.put("Â", "A");
            UTF8_UPPER_ACCENTS.put("Ľ", "L");
            UTF8_UPPER_ACCENTS.put("Ẅ", "W");
            UTF8_UPPER_ACCENTS.put("Ż", "Z");
            UTF8_UPPER_ACCENTS.put("Ī", "I");
            UTF8_UPPER_ACCENTS.put("Ã", "A");
            UTF8_UPPER_ACCENTS.put("Ġ", "G");
            UTF8_UPPER_ACCENTS.put("Ṁ", "M");
            UTF8_UPPER_ACCENTS.put("Ō", "O");
            UTF8_UPPER_ACCENTS.put("Ĩ", "I");
            UTF8_UPPER_ACCENTS.put("Ù", "U");
            UTF8_UPPER_ACCENTS.put("Į", "I");
            UTF8_UPPER_ACCENTS.put("Ź", "Z");
            UTF8_UPPER_ACCENTS.put("Á", "A");
            UTF8_UPPER_ACCENTS.put("Û", "U");
            UTF8_UPPER_ACCENTS.put("Þ", "Th");
            UTF8_UPPER_ACCENTS.put("Ð", "Dh");
            UTF8_UPPER_ACCENTS.put("Æ", "Ae");
            UTF8_UPPER_ACCENTS.put("Ĕ", "E");
        }
    }

    /**
     * fills data for lower accents map.
     */
    private static void fillLowerAccents() {
        if (UTF8_LOWER_ACCENTS.size() == 0) {
            UTF8_LOWER_ACCENTS.put("à", "a");
            UTF8_LOWER_ACCENTS.put("ô", "o");
            UTF8_LOWER_ACCENTS.put("ď", "d");
            UTF8_LOWER_ACCENTS.put("ḟ", "f");
            UTF8_LOWER_ACCENTS.put("ë", "e");
            UTF8_LOWER_ACCENTS.put("š", "s");
            UTF8_LOWER_ACCENTS.put("ơ", "o");
            UTF8_LOWER_ACCENTS.put("ß", "ss");
            UTF8_LOWER_ACCENTS.put("ă", "a");
            UTF8_LOWER_ACCENTS.put("ř", "r");
            UTF8_LOWER_ACCENTS.put("ț", "t");
            UTF8_LOWER_ACCENTS.put("ň", "n");
            UTF8_LOWER_ACCENTS.put("ā", "a");
            UTF8_LOWER_ACCENTS.put("ķ", "k");
            UTF8_LOWER_ACCENTS.put("ŝ", "s");
            UTF8_LOWER_ACCENTS.put("ỳ", "y");
            UTF8_LOWER_ACCENTS.put("ņ", "n");
            UTF8_LOWER_ACCENTS.put("ĺ", "l");
            UTF8_LOWER_ACCENTS.put("ħ", "h");
            UTF8_LOWER_ACCENTS.put("ṗ", "p");
            UTF8_LOWER_ACCENTS.put("ó", "o");
            UTF8_LOWER_ACCENTS.put("ú", "u");
            UTF8_LOWER_ACCENTS.put("ě", "e");
            UTF8_LOWER_ACCENTS.put("é", "e");
            UTF8_LOWER_ACCENTS.put("ç", "c");
            UTF8_LOWER_ACCENTS.put("ẁ", "w");
            UTF8_LOWER_ACCENTS.put("ċ", "c");
            UTF8_LOWER_ACCENTS.put("õ", "o");
            UTF8_LOWER_ACCENTS.put("ṡ", "s");
            UTF8_LOWER_ACCENTS.put("ø", "o");
            UTF8_LOWER_ACCENTS.put("ģ", "g");
            UTF8_LOWER_ACCENTS.put("ŧ", "t");
            UTF8_LOWER_ACCENTS.put("ș", "s");
            UTF8_LOWER_ACCENTS.put("ė", "e");
            UTF8_LOWER_ACCENTS.put("ĉ", "c");
            UTF8_LOWER_ACCENTS.put("ś", "s");
            UTF8_LOWER_ACCENTS.put("î", "i");
            UTF8_LOWER_ACCENTS.put("ű", "u");
            UTF8_LOWER_ACCENTS.put("ć", "c");
            UTF8_LOWER_ACCENTS.put("ę", "e");
            UTF8_LOWER_ACCENTS.put("ŵ", "w");
            UTF8_LOWER_ACCENTS.put("ṫ", "t");
            UTF8_LOWER_ACCENTS.put("ū", "u");
            UTF8_LOWER_ACCENTS.put("č", "c");
            UTF8_LOWER_ACCENTS.put("ö", "oe");
            UTF8_LOWER_ACCENTS.put("è", "e");
            UTF8_LOWER_ACCENTS.put("ŷ", "y");
            UTF8_LOWER_ACCENTS.put("ą", "a");
            UTF8_LOWER_ACCENTS.put("ł", "l");
            UTF8_LOWER_ACCENTS.put("ų", "u");
            UTF8_LOWER_ACCENTS.put("ů", "u");
            UTF8_LOWER_ACCENTS.put("ş", "s");
            UTF8_LOWER_ACCENTS.put("ğ", "g");
            UTF8_LOWER_ACCENTS.put("ļ", "l");
            UTF8_LOWER_ACCENTS.put("ƒ", "f");
            UTF8_LOWER_ACCENTS.put("ž", "z");
            UTF8_LOWER_ACCENTS.put("ẃ", "w");
            UTF8_LOWER_ACCENTS.put("ḃ", "b");
            UTF8_LOWER_ACCENTS.put("å", "a");
            UTF8_LOWER_ACCENTS.put("ì", "i");
            UTF8_LOWER_ACCENTS.put("ï", "i");
            UTF8_LOWER_ACCENTS.put("ḋ", "d");
            UTF8_LOWER_ACCENTS.put("ť", "t");
            UTF8_LOWER_ACCENTS.put("ŗ", "r");
            UTF8_LOWER_ACCENTS.put("ä", "ae");
            UTF8_LOWER_ACCENTS.put("í", "i");
            UTF8_LOWER_ACCENTS.put("ŕ", "r");
            UTF8_LOWER_ACCENTS.put("ê", "e");
            UTF8_LOWER_ACCENTS.put("ü", "ue");
            UTF8_LOWER_ACCENTS.put("ò", "o");
            UTF8_LOWER_ACCENTS.put("ē", "e");
            UTF8_LOWER_ACCENTS.put("ñ", "n");
            UTF8_LOWER_ACCENTS.put("ń", "n");
            UTF8_LOWER_ACCENTS.put("ĥ", "h");
            UTF8_LOWER_ACCENTS.put("ĝ", "g");
            UTF8_LOWER_ACCENTS.put("đ", "d");
            UTF8_LOWER_ACCENTS.put("ĵ", "j");
            UTF8_LOWER_ACCENTS.put("ÿ", "y");
            UTF8_LOWER_ACCENTS.put("ũ", "u");
            UTF8_LOWER_ACCENTS.put("ŭ", "u");
            UTF8_LOWER_ACCENTS.put("ư", "u");
            UTF8_LOWER_ACCENTS.put("ţ", "t");
            UTF8_LOWER_ACCENTS.put("ý", "y");
            UTF8_LOWER_ACCENTS.put("ő", "o");
            UTF8_LOWER_ACCENTS.put("â", "a");
            UTF8_LOWER_ACCENTS.put("ľ", "l");
            UTF8_LOWER_ACCENTS.put("ẅ", "w");
            UTF8_LOWER_ACCENTS.put("ż", "z");
            UTF8_LOWER_ACCENTS.put("ī", "i");
            UTF8_LOWER_ACCENTS.put("ã", "a");
            UTF8_LOWER_ACCENTS.put("ġ", "g");
            UTF8_LOWER_ACCENTS.put("ṁ", "m");
            UTF8_LOWER_ACCENTS.put("ō", "o");
            UTF8_LOWER_ACCENTS.put("ĩ", "i");
            UTF8_LOWER_ACCENTS.put("ù", "u");
            UTF8_LOWER_ACCENTS.put("į", "i");
            UTF8_LOWER_ACCENTS.put("ź", "z");
            UTF8_LOWER_ACCENTS.put("á", "a");
            UTF8_LOWER_ACCENTS.put("û", "u");
            UTF8_LOWER_ACCENTS.put("þ", "th");
            UTF8_LOWER_ACCENTS.put("ð", "dh");
            UTF8_LOWER_ACCENTS.put("æ", "ae");
            UTF8_LOWER_ACCENTS.put("µ", "u");
            UTF8_LOWER_ACCENTS.put("ĕ", "e");

        }
    }
}
