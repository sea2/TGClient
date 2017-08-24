package com.tangguo.tangguoxianjin.util;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by lhy on 2017/4/18.
 */

public class FileUtil {

    /**
     * 文件大小
     *
     * @param file
     * @return
     */
    public static String getFileSize(File file) {
        String size = "";
        if (file.exists() && file.isFile()) {
            long fileS = file.length();
            DecimalFormat df = new DecimalFormat("#.00");
            if (fileS < 1024) {
                size = df.format((double) fileS) + "BT";
            } else if (fileS < 1048576) {
                size = df.format((double) fileS / 1024) + "KB";
            } else if (fileS < 1073741824) {
                size = df.format((double) fileS / 1048576) + "MB";
            } else {
                size = df.format((double) fileS / 1073741824) + "GB";
            }
        } else if (file.exists() && file.isDirectory()) {
            size = "";
        } else {
            size = "0BT";
        }
        return size;
    }


    /**
     * 获取文件后缀名
     *
     * @param file
     * @return
     */
    public static String getImgSuffix(File file) {
        String imgSuffix = "";
        if (file != null) {
            String fileName = file.getName();
            if (!StringUtils.isEmpty(fileName)) imgSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return imgSuffix;
    }

}
