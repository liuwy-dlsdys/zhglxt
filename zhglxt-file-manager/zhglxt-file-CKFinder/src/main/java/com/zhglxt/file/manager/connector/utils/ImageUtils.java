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

import com.zhglxt.file.manager.connector.configuration.IConfiguration;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.FileItem;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Utils to operate on images.
 */
public class ImageUtils {

    /**
     * allowed image extensions.
     */
    private static final String[] ALLOWED_EXT = {"gif", "jpeg", "jpg", "png",
            "bmp", "xbm"};
    private static final int MAX_BUFF_SIZE = 1024;

    /**
     * Resizes the image and writes it to the disk.
     *
     * @param sourceImage original image file.
     * @param width       requested width
     * @param height      requested height
     * @param quality     requested destination file quality
     * @param destFile    file to write to
     * @throws IOException when error occurs.
     */
    private static void resizeImage(final BufferedImage sourceImage, final int width,
                                    final int height, final float quality,
                                    final File destFile) throws IOException {
        try {
            Thumbnails.of(sourceImage).size(width, height).keepAspectRatio(false).outputQuality(quality).toFile(destFile);
            // for some special files outputQuality couses error:
            //IllegalStateException inner Thumbnailator jar. When exception is thrown
            // image is resized without quality
            // When http://code.google.com/p/thumbnailator/issues/detail?id=9
            // will be fixed this try catch can be deleted. Only:
            //Thumbnails.of(sourceImage).size(width, height).keepAspectRatio(false)
            //	.outputQuality(quality).toFile(destFile);
            // should remain.
        } catch (IllegalStateException e) {
            Thumbnails.of(sourceImage).size(width, height).keepAspectRatio(false).toFile(destFile);
        }
    }

    /**
     * create thumb file.
     *
     * @param orginFile origin image file.
     * @param file      file to save thumb
     * @param conf      connector configuration
     * @throws IOException when error occurs.
     */
    public static void createThumb(final File orginFile, final File file,
                                   final IConfiguration conf) throws IOException {
        BufferedImage image = ImageIO.read(orginFile);
        if (image != null) {
            Dimension dimension = createThumbDimension(image,
                    conf.getMaxThumbWidth(), conf.getMaxThumbHeight());
            FileUtils.createPath(file, true);
            if (image.getHeight() == dimension.height
                    && image.getWidth() == dimension.width) {
                writeUntouchedImage(orginFile, file);
            } else {
                resizeImage(image, dimension.width, dimension.height,
                        conf.getThumbsQuality(), file);
            }
        } else {
            if (conf.isDebugMode()) {
                throw new IOException("Wrong image file");
            }
        }

    }

    /**
     * Uploads image and if the image size is larger than maximum allowed it resizes the image.
     *
     * @param stream   input stream.
     * @param file     file name
     * @param fileName name of file
     * @param conf     connector configuration
     * @throws IOException when error occurs.
     */
    public static void createTmpThumb(final InputStream stream,
                                      final File file, final String fileName, final IConfiguration conf)
            throws IOException {

        BufferedInputStream bufferedIS = new BufferedInputStream(stream);
        bufferedIS.mark(Integer.MAX_VALUE);
        BufferedImage image = ImageIO.read(bufferedIS);
        if (image == null) {
            throw new IOException("Wrong file");
        }
        Dimension dimension = createThumbDimension(image, conf.getImgWidth(),
                conf.getImgHeight());
        if (dimension.width == 0 || dimension.height == 0
                || (image.getHeight() == dimension.height && image.getWidth() == dimension.width)) {
            bufferedIS.reset();
            writeUntouchedImage(bufferedIS, file);
        } else {
            resizeImage(image, dimension.width, dimension.height,
                    conf.getImgQuality(), file);
        }
        stream.close();
    }

    /**
     * Creates image file with fixed width and height.
     *
     * @param sourceFile input file
     * @param destFile   file to save
     * @param width      image width
     * @param height     image height
     * @param quality    image quality
     * @throws IOException when error occurs.
     */
    public static void createResizedImage(final File sourceFile,
                                          final File destFile, final int width, final int height,
                                          final float quality) throws IOException {

        BufferedImage image = ImageIO.read(sourceFile);
        Dimension dimension = new Dimension(width, height);
        if (image.getHeight() == dimension.height
                && image.getWidth() == dimension.width) {
            writeUntouchedImage(sourceFile, destFile);
        } else {
            resizeImage(image, dimension.width, dimension.height, quality,
                    destFile);

        }

    }

    /**
     * creates dimension of thumb.
     *
     * @param image     original image.
     * @param maxWidth  max thumb width
     * @param maxHeight max thumb height
     * @return dimension of thumb image.
     */
    private static Dimension createThumbDimension(final BufferedImage image,
                                                  final int maxWidth, final int maxHeight) {
        Dimension dimension = new Dimension();
        if (image.getWidth() >= image.getHeight()) {
            if (image.getWidth() >= maxWidth) {
                dimension.width = maxWidth;
                dimension.height = Math.round(((float) maxWidth / image.getWidth()) * image.getHeight());
            } else {
                dimension.height = image.getHeight();
                dimension.width = image.getWidth();
            }
        } else {
            if (image.getHeight() >= maxHeight) {
                dimension.height = maxHeight;
                dimension.width = Math.round((((float) maxHeight / image.getHeight()) * image.getWidth()));
            } else {
                dimension.height = image.getHeight();
                dimension.width = image.getWidth();
            }
        }
        return dimension;
    }

    /**
     * checks if file is image.
     *
     * @param file file to check
     * @return true if file is image.
     */
    public static boolean isImage(final File file) {
        List<String> list = Arrays.asList(ALLOWED_EXT);
        String fileExt;
        if (file != null) {
            fileExt = FileUtils.getFileExtension(file.getName().toLowerCase());
            return (fileExt != null) ? list.contains(fileExt) : false;
        } else {
            return false;
        }
    }

    /**
     * check if image size isn't bigger then biggest allowed.
     *
     * @param stream temp file input stream.
     * @param conf   connector configuration.
     * @return true if image size isn't bigger then biggest allowed.
     * @throws IOException when error occurs during reading image.
     */
    public static boolean checkImageSize(final InputStream stream,
                                         final IConfiguration conf) throws IOException {
        final Integer maxWidth = conf.getImgWidth();
        final Integer maxHeight = conf.getImgHeight();
        if (maxHeight == 0 && maxWidth == 0) {
            return true;
        }
        BufferedImage bi = ImageIO.read(stream);
        stream.close();
        if (bi == null) {
            return false;
        }
        return (bi.getHeight() <= maxHeight && bi.getWidth() <= maxWidth);
    }

    /**
     * checks if image file is image.
     *
     * @param item file upload item
     * @return true if file is image.
     */
    public static boolean checkImageFile(final FileItem item) {
        BufferedImage bi;
        InputStream is = null;
        try {
            is = item.getInputStream();
            bi = ImageIO.read(is);
        } catch (IOException e) {
            return false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
        }
        return (bi != null);
    }

    /**
     * writes unchanged file to disk.
     *
     * @param sourceFile - file to read from
     * @param destFile   - file to write to
     * @throws IOException when error occurs.
     */
    private static void writeUntouchedImage(final File sourceFile, final File destFile)
            throws IOException {
        FileInputStream fileIS = new FileInputStream(sourceFile);
        writeUntouchedImage(fileIS, destFile);
    }

    /**
     * writes unchanged file to disk.
     *
     * @param stream   - stream to read the file from
     * @param destFile - file to write to
     * @throws IOException when error occurs.
     */
    private static void writeUntouchedImage(final InputStream stream, final File destFile)
            throws IOException {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        byte[] buffer = new byte[MAX_BUFF_SIZE];
        int readNum;
        while ((readNum = stream.read(buffer)) != -1) {
            byteArrayOS.write(buffer, 0, readNum);
        }
        byte[] bytes = byteArrayOS.toByteArray();
        byteArrayOS.close();
        FileOutputStream fileOS = new FileOutputStream(destFile);
        fileOS.write(bytes);
        fileOS.flush();
        fileOS.close();
    }
}
