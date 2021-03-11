package com.aratek.framework.core.util;

import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * @Author 姜寄羽
 * 文件工具
 * @Date 2018/5/4 17:59
 */
public class AraFileUtils {

    public static String getUserDirectoryAbsolutePath() {
        String path = FileUtils.getUserDirectory()
                .getAbsolutePath();
        char[] p = path.toCharArray();
        if (p[p.length - 1] != File.separatorChar) {
            return path + File.separator;
        } else {
            return path;
        }
    }
}
