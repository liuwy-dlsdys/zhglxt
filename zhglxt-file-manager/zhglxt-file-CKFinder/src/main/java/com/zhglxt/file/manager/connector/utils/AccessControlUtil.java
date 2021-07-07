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

import com.zhglxt.file.manager.connector.configuration.Configuration;
import com.zhglxt.file.manager.connector.configuration.IConfiguration;
import com.zhglxt.file.manager.connector.data.AccessControlLevel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to generate ACL values.
 */
public final class AccessControlUtil {

    /**
     * Folder view mask.
     */
    public static final int CKFINDER_CONNECTOR_ACL_FOLDER_VIEW = 1;
    /**
     * Folder create mask.
     */
    public static final int CKFINDER_CONNECTOR_ACL_FOLDER_CREATE = 2;
    /**
     * Folder rename mask.
     */
    public static final int CKFINDER_CONNECTOR_ACL_FOLDER_RENAME = 4;
    /**
     * Folder delete mask.
     */
    public static final int CKFINDER_CONNECTOR_ACL_FOLDER_DELETE = 8;
    /**
     * File view mask.
     */
    public static final int CKFINDER_CONNECTOR_ACL_FILE_VIEW = 16;
    /**
     * File upload mask.
     */
    public static final int CKFINDER_CONNECTOR_ACL_FILE_UPLOAD = 32;
    /**
     * File rename mask.
     */
    public static final int CKFINDER_CONNECTOR_ACL_FILE_RENAME = 64;
    /**
     * File delete mask.
     */
    public static final int CKFINDER_CONNECTOR_ACL_FILE_DELETE = 128;
    /**
     * acl configuration.
     */
    private List<ACLEntry> aclEntries;
    /**
     * connector configuration.
     */
    private IConfiguration configuration;
    /**
     * current instance.
     */
    private static AccessControlUtil util;

    /**
     * Gets current util instance.
     *
     * @return current instance
     */
    public static AccessControlUtil getInstance() {
        if (util == null) {
            util = new AccessControlUtil();
        }
        return util;
    }

    /**
     * private constructor.
     */
    private AccessControlUtil() {
    }

    /**
     * check ACL for folder.
     *
     * @param resourceType    resource type name
     * @param folder          folder name
     * @param acl             acl to check.
     * @param currentUserRole user role
     * @return true if acl flag is true
     */
    public boolean checkFolderACL(final String resourceType,
                                  final String folder, final String currentUserRole,
                                  final int acl) {
        return ((checkACLForRole(resourceType, folder, currentUserRole) & acl) == acl);
    }

    /**
     * Checks ACL for given role.
     *
     * @param resourceType    resource type
     * @param folder          current folder
     * @param currentUserRole current user role
     * @return acl value
     */
    public int checkACLForRole(final String resourceType, final String folder,
                               final String currentUserRole) {
        CheckEntry[] ce = new CheckEntry[currentUserRole != null ? 4 : 2];

        ce[0] = new CheckEntry("*", "*");
        ce[1] = new CheckEntry("*", resourceType);

        if (currentUserRole != null) {
            ce[2] = new CheckEntry(currentUserRole, "*");
            ce[3] = new CheckEntry(currentUserRole, resourceType);
        }

        int acl = 0;
        for (CheckEntry checkEntry : ce) {
            List<ACLEntry> aclEntrieForType = findACLEntryByRoleAndType(checkEntry.type,
                    checkEntry.role);

            for (ACLEntry aclEntry : aclEntrieForType) {
                String cuttedPath = folder;

                while (true) {
                    if (cuttedPath.length() > 1
                            && cuttedPath.lastIndexOf("/") == cuttedPath.length() - 1) {
                        cuttedPath = cuttedPath.substring(0, cuttedPath.length() - 1);

                    }
                    if (aclEntry.folder.equals(cuttedPath)) {
                        acl = checkACLForFolder(aclEntry, cuttedPath);
                        break;
                    } else {

                        if (cuttedPath.length() == 1) {
                            break;
                        }
                        if (cuttedPath.lastIndexOf("/") > -1) {
                            cuttedPath = cuttedPath.substring(0,
                                    cuttedPath.lastIndexOf("/") + 1);

                        } else {
                            break;
                        }
                    }
                }

            }
        }
        return acl;
    }

    /**
     * Resets the configuration and ACL Entries for this {@code AccessControlUtil} instance.<br>
     * This method is required due to dynamic nature of configuration object and need to avoid any unnecessary overhead related to loading
     * ACL settings.<br>
     * It should only be used when ACL's in configuration object were changed. It happens when new configuration is loaded (CKFinder handles
     * it) or whenever new {@code AccessControlLevelsList} is created in
     * {@link Configuration#prepareConfigurationForRequest(javax.servlet.http.HttpServletRequest)}
     * method.
     */
    public void resetConfiguration() {
        this.configuration = null;
        this.aclEntries = null;
    }

    /**
     * Loads configuration object and ACL entries for this {@code AccessControlUtil} instance.<br>
     * Configuration and ACL Entries will only be used if current configuration object for this {@code AccessControlUtil} instance is
     * {@code null}.
     *
     * @param configuration candidate for new configuration object
     */
    public void loadConfiguration(IConfiguration configuration) {
        if (this.configuration == null || this.aclEntries == null) {
            this.configuration = configuration;
            loadACLConfig();
        }
    }

    /**
     * Caches ACL configuration from connector's configuration in order to avoid. any unnecessary overhead related with reading/writing
     * ACL's on every request.
     */
    private void loadACLConfig() {
        aclEntries = new ArrayList<AccessControlUtil.ACLEntry>();
        for (AccessControlLevel item : this.configuration.getAccessConrolLevels()) {
            ACLEntry aclEntry = new ACLEntry();
            aclEntry.role = item.getRole();
            aclEntry.type = item.getResourceType();
            aclEntry.folder = item.getFolder();
            aclEntry.fileDelete = item.isFileDelete();
            aclEntry.fileRename = item.isFileRename();
            aclEntry.fileUpload = item.isFileUpload();
            aclEntry.fileView = item.isFileView();

            aclEntry.folderCreate = item.isFolderCreate();
            aclEntry.folderDelete = item.isFolderDelete();
            aclEntry.folderRename = item.isFolderRename();
            aclEntry.folderView = item.isFolderView();

            aclEntries.add(aclEntry);
        }
    }

    /**
     * Checks ACL for given folder.
     *
     * @param entry  current ACL entry
     * @param folder current folder
     * @return acl value
     */
    private int checkACLForFolder(final ACLEntry entry, final String folder) {
        int acl = 0;
        if (folder.contains(entry.folder) || entry.folder.equals(File.separator)) {
            acl = countAclByEntry(acl, entry);
        }
        return acl;
    }

    /**
     * counts ACL for entry.
     *
     * @param acl   parent acl
     * @param entry acl config
     * @return counted ACL for entry.
     */
    private int countAclByEntry(final int acl, final ACLEntry entry) {
        return entry.countACL() ^ acl;
    }

    /**
     * Gets a list of ACL entries for current role and resource type.
     *
     * @param type resource type
     * @param role current user role
     * @return list of ACL entries.
     */
    private List<ACLEntry> findACLEntryByRoleAndType(final String type,
                                                     final String role) {
        List<ACLEntry> res = new ArrayList<AccessControlUtil.ACLEntry>();
        for (ACLEntry item : aclEntries) {
            if (item.role.equals(role) && item.type.equals(type)) {
                res.add(item);
            }
        }
        return res;
    }

    /**
     * Simple ACL entry class.
     */
    private static class ACLEntry {

        /**
         * role name.
         */
        private String role;
        /**
         * resource type name.
         */
        private String type;
        /**
         * folder name.
         */
        private String folder;
        /**
         * folder view flag.
         */
        private boolean folderView;
        /**
         * folder create flag.
         */
        private boolean folderCreate;
        /**
         * folder rename flag.
         */
        private boolean folderRename;
        /**
         * folder delete flag.
         */
        private boolean folderDelete;
        /**
         * file view flag.
         */
        private boolean fileView;
        /**
         * file upload flag.
         */
        private boolean fileUpload;
        /**
         * file rename flag.
         */
        private boolean fileRename;
        /**
         * file delete flag.
         */
        private boolean fileDelete;

        /**
         * count entry ACL.
         *
         * @return entry acl
         */
        private int countACL() {
            int acl = 0;
            acl += (folderView) ? CKFINDER_CONNECTOR_ACL_FOLDER_VIEW : 0;
            acl += (folderCreate) ? CKFINDER_CONNECTOR_ACL_FOLDER_CREATE : 0;
            acl += (folderRename) ? CKFINDER_CONNECTOR_ACL_FOLDER_RENAME : 0;
            acl += (folderDelete) ? CKFINDER_CONNECTOR_ACL_FOLDER_DELETE : 0;

            acl += (fileView) ? CKFINDER_CONNECTOR_ACL_FILE_VIEW : 0;
            acl += (fileUpload) ? CKFINDER_CONNECTOR_ACL_FILE_UPLOAD : 0;
            acl += (fileRename) ? CKFINDER_CONNECTOR_ACL_FILE_RENAME : 0;
            acl += (fileDelete) ? CKFINDER_CONNECTOR_ACL_FILE_DELETE : 0;
            return acl;
        }

        @Override
        public String toString() {
            return role + " " + type + " " + folder;
        }
    }

    /**
     * simple check ACL entry.
     */
    class CheckEntry {

        private String role;
        private String type;

        /**
         * Constructor.
         *
         * @param role current user role.
         * @param type resource type.
         */
        public CheckEntry(final String role, final String type) {
            super();
            this.role = role;
            this.type = type;
        }
    }
}
