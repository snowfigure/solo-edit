/*
 * Solo - A small and beautiful blogging system written in Java.
 * Copyright (c) 2010-2018, b3log.org & hacpai.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.b3log.solo.processor;

import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import jodd.io.FileUtil;
import jodd.io.upload.FileUpload;
import jodd.io.upload.MultipartStreamParser;
import jodd.io.upload.impl.MemoryFileUploadFactory;
import jodd.net.MimeTypes;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.b3log.latke.Latkes;
import org.b3log.latke.ioc.BeanManager;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.util.URLs;
import org.b3log.solo.SoloServletListener;
import org.b3log.solo.model.Option;
import org.b3log.solo.service.OptionQueryService;
import org.b3log.solo.util.Solos;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * File upload processor.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.1.2, Aug 2, 2018
 * @since 2.8.0
 */
@RequestProcessor
public class FileUploadProcessor {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(FileUploadProcessor.class);

    /**
     * Qiniu enabled.
     */
    private static final Boolean QN_ENABLED = StringUtils.isBlank(Solos.UPLOAD_DIR_PATH);

    static {
        if (!QN_ENABLED) {
            final File file = new File(Solos.UPLOAD_DIR_PATH);
            if (!FileUtil.isExistingFolder(file)) {
                try {
                    FileUtil.mkdirs(Solos.UPLOAD_DIR_PATH);
                } catch (IOException ex) {
                    LOGGER.log(Level.ERROR, "Init upload dir error", ex);

                    System.exit(-1);
                }
            }

            LOGGER.info("Uses dir [" + file.getAbsolutePath() + "] for saving files uploaded");
        }
    }


    private void getFileCommon(final HttpServletRequest req, final HttpServletResponse resp, final  String key) throws Exception {
        if (QN_ENABLED) {
            return;
        }

        String path = Solos.UPLOAD_DIR_PATH + key;
        path = URLs.decode(path);

        if (!FileUtil.isExistingFile(new File(path))) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);

            return;
        }

        final byte[] data = IOUtils.toByteArray(new FileInputStream(path));

        final String ifNoneMatch = req.getHeader("If-None-Match");
        final String etag = "\"" + DigestUtils.md5Hex(new String(data)) + "\"";

        resp.addHeader("Cache-Control", "public, max-age=31536000");
        resp.addHeader("ETag", etag);
        resp.setHeader("Server", "Latke Static Server (v" + SoloServletListener.VERSION + ")");
        final String ext = StringUtils.substringAfterLast(path, ".");
        final String mimeType = MimeTypes.getMimeType(ext);
        resp.addHeader("Content-Type", mimeType);

        if (etag.equals(ifNoneMatch)) {
            resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);

            return;
        }

        try (final OutputStream output = resp.getOutputStream()) {
            IOUtils.write(data, output);
            output.flush();
        }
    }

    /**
     * Gets file by the specified URL.
     *
     * @param req  the specified request
     * @param resp the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/file/*", method = HTTPRequestMethod.GET)
    public void getFileWithFile(final HttpServletRequest req, final HttpServletResponse resp) throws Exception {


        final String uri = req.getRequestURI();
        String key = StringUtils.substringAfter(uri, "/file/");
        key = StringUtils.substringBeforeLast(key, "?"); // Erase Qiniu template
        key = StringUtils.substringBeforeLast(key, "?"); // Erase Qiniu template

        getFileCommon(req,resp,key);
    }
    /**
     * Gets file by the specified URL.
     *
     * @param req  the specified request
     * @param resp the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/upload/*", method = HTTPRequestMethod.GET)
    public void getFile(final HttpServletRequest req, final HttpServletResponse resp) throws Exception {

        final String uri = req.getRequestURI();
        String key = StringUtils.substringAfter(uri, "/upload/");
        key = StringUtils.substringBeforeLast(key, "?"); // Erase Qiniu template
        key = StringUtils.substringBeforeLast(key, "?"); // Erase Qiniu template


        getFileCommon(req,resp,key);
    }

    /**
     * Uploads file.
     *
     * @param req the specified reuqest
     * @throws Exception exception
     */
    @RequestProcessing(value = "/upload", method = HTTPRequestMethod.POST)
    public void uploadFile(final HTTPRequestContext context, final HttpServletRequest req) throws Exception {

        if(!uploadCheckIsLogin(context)){
            return;
        }

        /*判断是否可以使用七牛CDN上传文件*/
        if (QN_ENABLED){
            uploadFileWithQiniu(context,req);
        }else{
            uploadFileLocal(context,req);
        }
    }

    /**
     * 检查用户是否登录
     * @param context
     * @return
     */
    private boolean uploadCheckIsLogin(final HTTPRequestContext context) {
        HttpServletRequest request = context.getRequest();
        HttpServletResponse response = context.getResponse();

        if (!Solos.isLoggedIn(request, response)){
            final String msg = "Users are not logged in, no permission to upload file，please login with admin user.";
            LOGGER.log(Level.ERROR, msg);
            context.renderMsg(msg);
            return false;
        }else{
            JSONObject currentUser = Solos.getCurrentUser(request, response);
            String userRole = currentUser.optString("userRole");
            if ("visitorRole".equals(userRole)) {
                final String msg = "Visitor user forbidden upload file，please login with admin user.";
                LOGGER.log(Level.ERROR, msg);
                context.renderMsg(msg);
                return false;
            }
        }

        return true;
    }

    /**
     * 根据时间错、文件信息，获取格式化的文件名
     * @param date
     * @param file
     * @return
     */
    private String getFormateFileName(String date, FileUpload file){

        String fileName = file.getHeader().getFileName();
        String suffix = StringUtils.substringAfterLast(fileName, ".");
        final String contentType = file.getHeader().getContentType();

        if (StringUtils.isBlank(suffix)) {
            String[] exts = MimeTypes.findExtensionsByMimeTypes(contentType, false);
            if (null != exts && 0 < exts.length) {
                suffix = exts[0];
            } else {
                suffix = StringUtils.substringAfter(contentType, "/");
            }
        }

        final String name = StringUtils.substringBeforeLast(fileName, ".");
        final String processName = name.replaceAll("\\W", "");
        final String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        /*随机文件名*/
        fileName = uuid + '_' + processName + "." + suffix;

        /*时间 + 随机文件名*/
        fileName = date + "/" + fileName;

        return fileName;
    }

    /**
     * 文件上传到七牛CDN
     * @param context
     * @param req
     * @throws Exception
     */
    private void uploadFileWithQiniu(final HTTPRequestContext context, final HttpServletRequest req) throws Exception {
        context.renderJSON();

        final int maxSize = 1024 * 1024 * 100;
        final MultipartStreamParser parser = new MultipartStreamParser(new MemoryFileUploadFactory().setMaxFileSize(maxSize));
        parser.parseRequestStream(req.getInputStream(), "UTF-8");
        final List<String> errFiles = new ArrayList();
        final Map<String, String> succMap = new LinkedHashMap<>();
        final FileUpload[] files = parser.getFiles("file[]");
        final String[] names = parser.getParameterValues("name[]");
        String fileName;


        final String date = DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM");

        try {
            final BeanManager beanManager = BeanManager.getInstance();
            final OptionQueryService optionQueryService = beanManager.getReference(OptionQueryService.class);
            JSONObject qiniu = optionQueryService.getOptions(Option.CATEGORY_C_QINIU);
            if (null == qiniu) {
                final String msg = "Qiniu settings failed, please set it first. ";
                LOGGER.log(Level.ERROR, msg);
                context.renderMsg(msg);

                return;
            }

            Auth auth = Auth.create(qiniu.optString(Option.ID_C_QINIU_ACCESS_KEY), qiniu.optString(Option.ID_C_QINIU_SECRET_KEY));
            String uploadToken = auth.uploadToken(qiniu.optString(Option.ID_C_QINIU_BUCKET), null, 3600 * 6, null);
            UploadManager uploadManager = new UploadManager(new Configuration());

            /*七牛的image_view*/
            String image_view = qiniu.optString((Option.ID_C_QINIU_IMAGE_VIEW)) + "";

            for (int i = 0; i < files.length; i++) {
                final FileUpload file = files[i];

                String originalName = file.getHeader().getFileName();
                String contentType = file.getHeader().getContentType();
                originalName = originalName.replaceAll("\\W", "");
                try {
                    fileName = getFormateFileName(date, file);

                    if (!ArrayUtils.isEmpty(names)) {
                        fileName = names[i];
                    }
                    uploadManager.put(file.getFileInputStream(), fileName, uploadToken, null, contentType);
                    String qiniu_file_name = "";
                    if(qiniu.optString(Option.ID_C_QINIU_DOMAIN).endsWith("/")){
                        qiniu_file_name = qiniu.optString(Option.ID_C_QINIU_DOMAIN) + fileName;
                    }else{
                        qiniu_file_name = qiniu.optString(Option.ID_C_QINIU_DOMAIN) + "/" + fileName;
                    }
                    /*如果是图片文件*/
                    if(     fileName.toLowerCase().endsWith(".jpg") ||
                            fileName.toLowerCase().endsWith(".png") ||
                            fileName.toLowerCase().endsWith(".gif") ||
                            fileName.toLowerCase().endsWith(".bpm"))
                    {

                        qiniu_file_name = qiniu_file_name + "?" + image_view;
                    }
                    succMap.put(originalName, qiniu_file_name);

                } catch (final Exception e) {
                    LOGGER.log(Level.WARN, "Uploads file failed", e);
                    errFiles.add(originalName);
                }
            }
        } catch (final Exception e) {
            final String msg = "Qiniu settings failed, please check it.";
            LOGGER.log(Level.ERROR, msg);
            context.renderMsg(msg);
            return;
        }

        final JSONObject data = new JSONObject();
        data.put("errFiles", errFiles);
        data.put("succMap", succMap);
        context.renderJSONValue("data", data).renderTrueResult();

    }


    /**
     * 文件上传到本地服务器
     * @param context
     * @param req
     * @throws Exception
     */
    private void uploadFileLocal(final HTTPRequestContext context, final HttpServletRequest req) throws Exception {

        context.renderJSON();

        final int maxSize = 1024 * 1024 * 100;
        final MultipartStreamParser parser = new MultipartStreamParser(new MemoryFileUploadFactory().setMaxFileSize(maxSize));
        parser.parseRequestStream(req.getInputStream(), "UTF-8");
        final List<String> errFiles = new ArrayList();
        final Map<String, String> succMap = new LinkedHashMap<>();
        final FileUpload[] files = parser.getFiles("file[]");
        final String[] names = parser.getParameterValues("name[]");
        String fileName;

        final String date = DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM");


        for (int i = 0; i < files.length; i++) {
            final FileUpload file = files[i];

            String originalName = file.getHeader().getFileName();
            originalName = originalName.replaceAll("\\W", "");
            try {
                fileName = getFormateFileName(date, file);

                FileUtils.forceMkdir(new File(Solos.UPLOAD_DIR_PATH + date));
                try (final OutputStream output = new FileOutputStream(Solos.UPLOAD_DIR_PATH + fileName);
                     final InputStream input = file.getFileInputStream())
                {
                    IOUtils.copy(input, output);
                }
                succMap.put(originalName, Latkes.getServePath() + "/upload/" + fileName);

            } catch (final Exception e) {
                LOGGER.log(Level.WARN, "Uploads file failed", e);

                errFiles.add(originalName);
            }
        }

        final JSONObject data = new JSONObject();
        data.put("errFiles", errFiles);
        data.put("succMap", succMap);
        context.renderJSONValue("data", data).renderTrueResult();
    }

}
