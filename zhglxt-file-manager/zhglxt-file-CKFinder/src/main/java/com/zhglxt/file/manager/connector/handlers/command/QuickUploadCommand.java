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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhglxt.file.manager.connector.utils.FileUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to handle <code>QuickUpload</code> command.
 */
public class QuickUploadCommand extends FileUploadCommand {

    @Override
    protected void handleOnUploadCompleteResponse(final OutputStream out,
                                                  final String errorMsg) throws IOException {
        if (this.responseType != null && this.responseType.equalsIgnoreCase("json")) {
            handleJSONResponse(out, errorMsg, null);
        } else {
            out.write("<script type=\"text/javascript\">".getBytes("UTF-8"));
            out.write("window.parent.OnUploadCompleted(".getBytes("UTF-8"));
            out.write(("" + this.errorCode + ", ").getBytes("UTF-8"));
            if (uploaded) {
                out.write(("\'" + configuration.getTypes().get(this.type).getUrl()
                        + this.currentFolder
                        + FileUtils.backupWithBackSlash(FileUtils.encodeURIComponent(this.newFileName), "'")
                        + "\', ").getBytes("UTF-8"));
                out.write(("\'" + FileUtils.backupWithBackSlash(this.newFileName, "'")
                        + "\', ").getBytes("UTF-8"));
            } else {
                out.write("\'\', \'\', ".getBytes("UTF-8"));
            }
            out.write("\'\'".getBytes("UTF-8"));
            out.write(");".getBytes("UTF-8"));
            out.write("</script>".getBytes("UTF-8"));
        }
    }

    @Override
    protected void handleOnUploadCompleteCallFuncResponse(final OutputStream out,
                                                          final String errorMsg, final String path) throws IOException {
        if (this.responseType != null && this.responseType.equalsIgnoreCase("json")) {
            handleJSONResponse(out, errorMsg, path);
        } else {
            out.write("<script type=\"text/javascript\">".getBytes("UTF-8"));
            this.ckEditorFuncNum = this.ckEditorFuncNum.replaceAll(
                    "[^\\d]", "");
            out.write(("window.parent.CKEDITOR.tools.callFunction("
                    + this.ckEditorFuncNum + ", '"
                    + path
                    + FileUtils.backupWithBackSlash(FileUtils.encodeURIComponent(this.newFileName), "'")
                    + "', '" + errorMsg + "');").getBytes("UTF-8"));
            out.write("</script>".getBytes("UTF-8"));
        }
    }

    @Override
    protected boolean checkFuncNum() {
        return this.ckEditorFuncNum != null;
    }

    @Override
    public void setResponseHeader(final HttpServletResponse response,
                                  final ServletContext sc) {
        response.setCharacterEncoding("utf-8");
        if (this.responseType != null && this.responseType.equalsIgnoreCase("json")) {
            response.setContentType("application/json");
        } else {
            response.setContentType("text/html");
        }
    }

    /**
     * Writes JSON object into response stream after uploading file which was
     * dragged and dropped in to CKEditor 4.5 or higher.
     *
     * @param out      the response stream
     * @param errorMsg string representing error message which indicates that
     *                 there was an error during upload or uploaded file was renamed
     * @param path     path to uploaded file
     */
    private void handleJSONResponse(final OutputStream out,
                                    final String errorMsg, final String path) throws IOException {

        Gson gson = new GsonBuilder().serializeNulls().create();
        Map<String, Object> jsonObj = new HashMap<String, Object>();

        jsonObj.put("fileName", this.newFileName);
        jsonObj.put("uploaded", this.uploaded ? new Integer(1) : new Integer(0));

        if (uploaded) {
            if (path != null && !path.equals("")) {
                jsonObj.put(
                        "url",
                        path
                                + FileUtils.backupWithBackSlash(FileUtils
                                        .encodeURIComponent(this.newFileName),
                                "'"));
            } else {
                jsonObj.put(
                        "url",
                        configuration.getTypes().get(this.type).getUrl()
                                + this.currentFolder
                                + FileUtils.backupWithBackSlash(FileUtils
                                        .encodeURIComponent(this.newFileName),
                                "'"));
            }
        }

        if (errorMsg != null && !errorMsg.equals("")) {
            Map<String, Object> jsonErrObj = new HashMap<String, Object>();
            jsonErrObj.put("number", this.errorCode);
            jsonErrObj.put("message", errorMsg);
            jsonObj.put("error", jsonErrObj);
        }

        out.write((gson.toJson(jsonObj)).getBytes("UTF-8"));
    }
}
