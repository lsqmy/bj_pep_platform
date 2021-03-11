package com.aratek.framework.core.util;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.ObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author shijinlong
 * @date 2018-05-16
 * @description FTP帮助类
 */
public class FtpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FtpUtil.class);

    /**
     * ftpClient连接池初始化标志
     */
    private static volatile boolean hasInit = false;
    /**
     * ftpClient连接池
     */
    private static ObjectPool<FTPClient> ftpClientPool;

    /**
     * 初始化ftpClientPool
     *
     * @param ftpClientPool
     */
    public static void init(ObjectPool<FTPClient> ftpClientPool) {
        if (!hasInit) {
            synchronized (FtpUtil.class) {
                if (!hasInit) {
                    FtpUtil.ftpClientPool = ftpClientPool;
                    hasInit = true;
                }
            }
        }
    }

    /**
     * 按行读取FTP文件
     *
     * @param remoteFilePath 文件路径（path+fileName）
     * @return
     * @throws IOException
     */
    /*public static List<String> readFileByLine(String remoteFilePath) throws IOException {
        FTPClient ftpClient = getFtpClient();
        try (InputStream in = ftpClient.retrieveFileStream(encodingPath(remoteFilePath));
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            return br.lines().map(line -> StringUtils.trimToEmpty(line))
                    .filter(line -> StringUtils.isNotEmpty(line)).collect(Collectors.toList());
        } finally {
            ftpClient.completePendingCommand();
            releaseFtpClient(ftpClient);
        }
    }*/

    /**
     * 获取指定路径下FTP文件
     *
     * @param remotePath 路径
     * @return FTPFile数组
     * @throws IOException
     */
    /*public static FTPFile[] retrieveFTPFiles(String remotePath) throws IOException {
        FTPClient ftpClient = getFtpClient();
        try {
            return ftpClient.listFiles(encodingPath(remotePath + "/"),
                    file -> file != null && file.getSize() > 0);
        } finally {
            releaseFtpClient(ftpClient);
        }
    }*/

    /**
     * 获取指定路径下FTP文件名称
     *
     * @param remotePath 路径
     * @return ftp文件名称列表
     * @throws IOException
     */
    /*public static List<String> retrieveFileNames(String remotePath) throws IOException {
        FTPFile[] ftpFiles = retrieveFTPFiles(remotePath);
        if (ArrayUtils.isEmpty(ftpFiles)) {
            return new ArrayList<>();
        }
        return Arrays.stream(ftpFiles).filter(Objects::nonNull)
                .map(FTPFile::getName).collect(Collectors.toList());
    }*/

    /**
     * 编码文件路径
     */
    private static String encodingPath(String path) throws UnsupportedEncodingException {
        // FTP协议里面，规定文件名编码为iso-8859-1，所以目录名或文件名需要转码
        return new String(path.replaceAll("//", "/").getBytes("UTF-8"), "ISO-8859-1");
    }

    /**
     * 获取ftpClient
     *
     * @return
     */
    private static FTPClient getFtpClient() {
        checkFtpClientPoolAvailable();
        FTPClient ftpClient = null;
        Exception ex = null;
        // 获取连接最多尝试3次
        for (int i = 0; i < 3; i++) {
            try {
                ftpClient = ftpClientPool.borrowObject();
                ftpClient.changeWorkingDirectory("/");
                break;
            } catch (Exception e) {
                ex = e;
            }
        }
        if (ftpClient == null) {
            throw new RuntimeException("Could not get a ftpClient from the pool", ex);
        }
        return ftpClient;
    }

    /**
     * 释放ftpClient
     */
    private static void releaseFtpClient(FTPClient ftpClient) {
        if (ftpClient == null) {
            return;
        }

        try {
            //归还对象时需要清除client状态，比如还原到根目录
            ftpClient.changeWorkingDirectory("/");
            ftpClientPool.returnObject(ftpClient);
        } catch (Exception e) {
            LOGGER.error("Could not return the ftpClient to the pool", e);
            // destoryFtpClient
            if (ftpClient.isAvailable()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException io) {
                }
            }
        }
    }

    /**
     * 检查ftpClientPool是否可用
     */
    private static void checkFtpClientPoolAvailable() {
        Assert.state(hasInit, "FTP未启用或连接失败！");
    }

    /**
     * 从ftp下载文件
     *
     * @param fileFullPathName
     * @return
     * @throws IOException
     */
    public static byte[] download(String fileFullPathName) throws IOException {
        // 创建FTP客户端
        FTPClient ftpClient = null;
        // 输出流用于输出文件
        ByteOutputStream fos = new ByteOutputStream();
        try {
//            fileFullPathName = new String(fileFullPathName.getBytes("UTF-8"), "iso-8859-1");
            ftpClient = getFtpClient();
            ftpClient.setBufferSize(1024);
            boolean res = ftpClient.retrieveFile(fileFullPathName, fos);
            if (!res) {
                throw new FileNotFoundException("未找到文件");
            }
            return fos.getBytes();
        } catch (FileNotFoundException e) {
            LOGGER.error("ftp文件不存在:{}",fileFullPathName);
            throw e;
        } catch (Exception e) {
            LOGGER.error("ftp获取文件异常," + fileFullPathName, e);
            throw new IOException("因为不可知的原因，无法连接到FTP", e);
        } finally {
            releaseFtpClient(ftpClient);
        }
    }

    /**
     * 上传文件到FTP
     *
     * @param in
     * @param remoteFilePath
     * @throws IOException
     */
    public static boolean uploadFile2Ftp(InputStream in, String remoteFilePath)
            throws IOException {
        FTPClient ftpClient = getFtpClient();
        OutputStream out = null;
        try {
//            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF-8", "ON"))) {
//                //如果ftp服务器支持UTF-8
//                out = ftpClient.storeFileStream(remoteFilePath);
//            } else {
//                //如果ftp服务器不支持UTF-8则转换路径中文编码
//                out = ftpClient.storeFileStream(encodingPath(remoteFilePath));
//            }
            //create path
            String[] dir = remoteFilePath.split("/");
            int reply;
            //层层递进，没有就建
            for (int i = 0; i < dir.length - 1; i++) {
                if(dir[i].isEmpty()){
                    continue;
                }
                LOGGER.debug("change dir {}",dir[i]);
                if(ftpClient.changeWorkingDirectory("/"+dir[i])){
                    ftpClient.changeWorkingDirectory("/"+dir[i]);
                    continue;
                }
                if (!ftpClient.changeWorkingDirectory(dir[i])) {
                    reply = ftpClient.getReplyCode();
                    LOGGER.warn("FTP Change Working Directory reply {}",reply);
                    if (!ftpClient.makeDirectory(dir[i])) {
                        reply = ftpClient.getReplyCode();
                        LOGGER.error("FTP reply {}",reply);
                        throw new IOException("create folder error");
                    }else {
                        if(ftpClient.changeWorkingDirectory(dir[i])){
                            LOGGER.debug("change dir {}",dir[i]);
                        }else {
                            throw new IOException("Change working directory folder error "+remoteFilePath);
                        }
                    }
                }
            }
            ftpClient.makeDirectory(encodingPath(getPathWithOutFile(remoteFilePath)));
            //upload file
            out = ftpClient.storeFileStream(encodingPath(remoteFilePath));
            byte[] buffer = new byte[10240];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
                out.flush();
            }
            return true;
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            ftpClient.completePendingCommand();
            releaseFtpClient(ftpClient);
        }
    }

    /**
     * 从ftp下载文件
     *
     * @param remoteFilePath
     * @param response
     * @throws IOException
     */
    public static void downloadFile(String remoteFilePath, HttpServletResponse response)
            throws IOException {
        String fileName = remoteFilePath.substring(remoteFilePath.lastIndexOf("/") + 1);
        LOGGER.debug("FtpUtil.downloadFile.1.fileName={}", fileName);
//        fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
//        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        FTPClient ftpClient = getFtpClient();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = ftpClient.retrieveFileStream(encodingPath(remoteFilePath));
            out = response.getOutputStream();
            int size;
            byte[] buf = new byte[10240];
            while ((size = in.read(buf)) > 0) {
                out.write(buf, 0, size);
                out.flush();
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            ftpClient.completePendingCommand();
            releaseFtpClient(ftpClient);
        }
    }

    /**
     * 删除ftp文件
     *
     * @param remoteFilePath
     * @return
     * @throws IOException
     */
    public static boolean deleteFile(String remoteFilePath)
            throws IOException {
        LOGGER.debug("FtpUtil.deleteFile.remoteFilePath={}", remoteFilePath);
        FTPClient ftpClient = getFtpClient();
        return ftpClient.deleteFile(remoteFilePath);
    }

    /**
     * 获取path路径,去掉最后一格的文件名
     *
     * @param path
     * @return
     */
    public static String getPathWithOutFile(String path) {
        if (StringUtil.isBlank(path)) {
            return "";
        }
        if (path.contains("\\")) {
            path = path.replaceAll("\\\\", "/");
        }
        if (path.contains("//")) {
            path = path.replaceAll("//", "/");
        }
        path = path.substring(0, path.lastIndexOf("/"));
        return path;
    }

}
