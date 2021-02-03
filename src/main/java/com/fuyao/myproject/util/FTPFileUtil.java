package com.fuyao.myproject.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * FTP工具类
 *
 * @date: 2019/7/3 14:37
 */
@Component
public class FTPFileUtil {
    private FTPClient ftpClient;
    private static final Logger logger = LoggerFactory.getLogger(FTPFileUtil.class);
    /**
     * @param hostname ftp主机地址
     * @param port     ftp端口号
     * @param username 用户名
     * @param password 密码
     * @param workPath ftp文件存放路径
     * @return
     */
    public boolean initFtpClient(String hostname, int port, String username, String password, String workPath) {
        ftpClient = new FTPClient();
        ftpClient.setControlEncoding("utf-8");
        try {
            ftpClient.connect(hostname, port); //连接ftp服务器
            ftpClient.login(username, password); //登录ftp服务器
            int replyCode = ftpClient.getReplyCode(); //是否成功登录服务器
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                return false;
            }
                String ftppath="/";
                String pathArry[] = workPath.split("/");
                for (int i=0;i<pathArry.length;i++){
                    if (pathArry[i].equals("")) {
                        continue;
                    }
                    ftppath=ftppath+pathArry[i]+"/";
                    if (!ftpClient.changeWorkingDirectory(ftppath)) {//目录不存在
                        ftpClient.makeDirectory(ftppath);
                        ftpClient.changeWorkingDirectory(ftppath);
                    } else {//进入下一级目录
                        ftpClient.changeWorkingDirectory(ftppath);
                    }
                }
//            boolean b = ftpClient.changeWorkingDirectory(workPath);
//
//            //  判断路径是否存在 不存在则创建并进入。
//            if (b != true) {
//                ftpClient.makeDirectory(workPath);
//                ftpClient.changeWorkingDirectory(workPath);
//            }
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();;
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * @param pathname       ftp服务器文件存放路径
     * @param inputStream    文件输入流
     * @param fileName       文件名
     * @param hostname       ftp地址
     * @param port           ftp主机端口
     * @param username       用户名
     * @param password       密码
     * @return
     */
    public boolean uploadFile(String pathname, InputStream inputStream,String fileName, String hostname, int port, String username, String password) {
        List<String> list = new ArrayList<String>();
        String regex = "";
        try {
            //  初始化ftp
            boolean b = initFtpClient(hostname, port, username, password, pathname);
            if (b != true) {
                return false;
            }
           //判断ftp路径是否存在
                //判断本地路径是否存在
//                File file = new File(originfilename);
//                    if (!file.exists()) {
//                        throw new RuntimeException("本地路径有误");
//                    }
//                    if(file.getName().indexOf(".zip")>0){
//                        list.add(file.getName());
//                    }else{
//                        File[] files = file.listFiles();
//                        for (File item : files) {
//                            if(item.isFile()){
//                                list.add(item.getName());
//                                continue;
//                            }
//                        }
//                }
//                        inputStream = new FileInputStream(new File(originfilename));
            ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
//            this.ftpClient.enterLocalPassiveMode();
            boolean flag = ftpClient.storeFile(fileName, inputStream);
            inputStream.close();
            boolean logout = ftpClient.logout();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    }

    /**
     * 根据文件ftp路径名称删除文件
     *
     * @param ftpFileName 文件ftp路径名称
     */
    public void deleteFile(String ftpFileName) throws IOException {
        if (!ftpClient.deleteFile(ftpFileName)) {
            throw new IOException("Can't remove file '" + ftpFileName + "' from FTP server.");
        }
    }

    /**
     * 改变工作目录
     *
     * @param dir ftp服务器上目录
     * @return boolean 改变成功返回true
     */
    public boolean changeWorkingDirectory(String dir) {
        if (!ftpClient.isConnected()) {
            return false;
        }
        try {
            return ftpClient.changeWorkingDirectory(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    /***************************************************************************
     * 下载文件
     *
     * @param outputStream
     * @param remoteFileName
     * @param fileType
     * @throws IOException
     */
    public boolean download(OutputStream outputStream, String remoteFileName, int fileType) throws IOException {
        //		isPositiveable();

        ftpClient.setControlEncoding("UTF-8");
        ftpClient.enterLocalPassiveMode();
        boolean flag = true;
        try {
//            ftpClient.setFileType(fileType);
            ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);

            flag = ftpClient.retrieveFile(new String(remoteFileName.getBytes("UTF-8"), "ISO-8859-1"),
                    outputStream);
        } catch (FileNotFoundException e) {
            logger.error("download", e);
            flag = false;
        } catch (IOException e) {
            logger.error("download", e);
            flag = false;
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
           ftpClient.disconnect();
        }
        return flag;
    }

    /***************************************************************************
     * 下载文件
     *
     * @param outputStream
     * @param remoteFileName
     * @throws IOException
     */
    public boolean download(OutputStream outputStream, String remoteFileName) throws IOException {
        return download(outputStream, remoteFileName, FTP.BINARY_FILE_TYPE);
    }

    /***************************************************************************
     *
     * @param file
     *            下载下来的文件
     * @param remoteFileName
     * @throws IOException
     */
    public boolean download(File file, String remoteFileName) throws IOException {
        OutputStream outputStream = new FileOutputStream(file);
        return download(outputStream, remoteFileName, FTP.BINARY_FILE_TYPE);
    }
    /**
     * @param path
     * @return function:读取指定目录下的文件名
     * @throws IOException
     */
    public List<String> getFileList(String path) {
        List<String> fileLists = new ArrayList<String>();
// 获得指定目录下所有文件名
        FTPFile[] ftpFiles = null;
        try {
            ftpClient.enterLocalPassiveMode();
            ftpFiles = ftpClient.listFiles(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; ftpFiles != null && i < ftpFiles.length; i++) {
            FTPFile file = ftpFiles[i];
            if (file.isFile()) {
                System.out.println("文件夹下面的文件====="+file.getName());
                fileLists.add(file.getName());
            }else if(file.isDirectory()){
                System.out.println("文件夹名称为====="+file.getName());
                List<String> childLists = getFileList(path + file.getName()+"/");
                for(String childFileName : childLists){
                    fileLists.add(childFileName);
                    String fileType = childFileName.substring(childFileName.lastIndexOf(".")+1,childFileName.length());
                    System.out.println("文件类型为："+fileType);
                    FTPFileUtil ftpFileUtil = new FTPFileUtil();
                    if(fileType.equals("txt")){
                        System.out.println("文件名为："+childFileName);
                        String content = "";
                        content = ftpFileUtil.readFile(path + file.getName()+"/"+childFileName);
                        System.out.println("文件内容为："+content);
                    }
                }
            }
        }
        return fileLists;
    }
    /**
     * @param fileName
     * @return function:从服务器上读取指定的文件
     * @throws IOException
     */
    public String readFile(String fileName) {
        InputStream ins = null;
        StringBuilder builder = null;
        try {
// 从服务器上读取指定的文件
            ins = ftpClient.retrieveFileStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
            String line;
            builder = new StringBuilder(150);
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                builder.append(line);
            }
            reader.close();
            if (ins != null) {
                ins.close();
            }
// 主动调用一次getReply()把接下来的226消费掉. 这样做是可以解决这个返回null问题
            ftpClient.getReply();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
    /**
     * 下载文件
     * @param fileName 本地文件路径
     * @param remoteFileName 远程文件路径
     * @return
     * @throws IOException
     */
    public boolean download(String fileName, String remoteFileName) throws IOException {
        File file = new File(fileName);
        OutputStream outputStream = new FileOutputStream(file);
        return download(outputStream, remoteFileName, FTP.BINARY_FILE_TYPE);
//        File file=new File(fileName);
//        if (!file.exists())file.mkdirs();
//        if (!ftpClient.isConnected()) {
//            return false;
//        }
//        ftpClient.enterLocalPassiveMode(); // Use passive mode as default
//        try {
//            // 将路径中的斜杠统一
//            char[] chars = remoteFileName.toCharArray();
//            StringBuffer sbStr = new StringBuffer(256);
//            for (int i = 0; i < chars.length; i++) {
//                if ('\\' == chars[i]) {
//                    sbStr.append('/');
//                } else {
//                    sbStr.append(chars[i]);
//                }
//            }
//            remoteFileName = sbStr.toString();
//            String filePath = remoteFileName.substring(0, remoteFileName.lastIndexOf("/"));
//            String ftpfileName = remoteFileName.substring(remoteFileName.lastIndexOf("/") + 1);
//            this.changeWorkingDirectory(filePath);
////            ftpClient.changeWorkingDirectory()
//            ftpClient.retrieveFile(new String(fileName.getBytes(), "iso-8859-1"),
//                    new FileOutputStream(fileName)); // download
//            // file
//            System.out.println(ftpClient.getReplyString()); // check result
//            System.out.println("线程"+Thread.currentThread().getName()+"从ftp服务器上下载文件：" + remoteFileName + "， 保存到：" + fileName);
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }

    }
    /**
     * 列出ftp上文件目录下的文件
     *
     * @param filePath ftp上文件目录
     * @return java.util.List<java.lang.String>
     */
    public List<String> listFileNames(String filePath) throws IOException {
        FTPFile[] ftpFiles = ftpClient.listFiles(filePath);
        List<String> fileList = new ArrayList<>();
        if (ftpFiles != null) {
            for (int i = 0; i < ftpFiles.length; i++) {
                FTPFile ftpFile = ftpFiles[i];
                if (ftpFile.isFile()) {
                    fileList.add(ftpFile.getName());
                }
            }
        }

        return fileList;
    }

    /**
     * 发送ftp命令到ftp服务器中
     *
     * @param args ftp命令
     */
    public void sendSiteCommand(String args) throws IOException {
        if (!ftpClient.isConnected()) {
            ftpClient.sendSiteCommand(args);
        }
    }

    /**
     * 获取当前所处的工作目录
     *
     * @return java.lang.String 当前所处的工作目录
     */
    public String printWorkingDirectory() {
        if (!ftpClient.isConnected()) {
            return "";
        }
        try {
            return ftpClient.printWorkingDirectory();
        } catch (IOException e) {
            // do nothing
        }

        return "";
    }

    /**
     * 切换到当前工作目录的父目录下
     *
     * @return boolean 切换成功返回true
     */
    public boolean changeToParentDirectory() {

        if (!ftpClient.isConnected()) {
            return false;
        }

        try {
            return ftpClient.changeToParentDirectory();
        } catch (IOException e) {
            // do nothing
        }

        return false;
    }

    /**
     * 返回当前工作目录的上一级目录
     *
     * @return java.lang.String 当前工作目录的父目录
     */
    public String printParentDirectory() {
        if (!ftpClient.isConnected()) {
            return "";
        }

        String w = printWorkingDirectory();
        changeToParentDirectory();
        String p = printWorkingDirectory();
        changeWorkingDirectory(w);

        return p;
    }

    /**
     * 创建目录
     *
     * @param pathname 路径名
     * @return boolean 创建成功返回true
     */
    public boolean makeDirectory(String pathname) throws IOException {
        return ftpClient.makeDirectory(pathname);
    }

    /**
     * 创建多个目录
     *
     * @param pathname 路径名
     */
    public void makeDirs(String pathname) throws IOException {
        pathname = pathname.replace("\\\\", "/");
        String[] pathnameArray = pathname.split("/");
        for (String each : pathnameArray) {
            if (StringUtils.isNotEmpty(each)) {
                ftpClient.makeDirectory(each);
                ftpClient.changeWorkingDirectory(each);
            }
        }
    }

    /**
     * 关闭流
     *
     * @param stream 流
     */
    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ex) {
                // do nothing
            }
        }
    }

    /**
     * 关闭ftp连接
     */
    public void disconnect() {
        if (null != ftpClient && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException ex) {
                // do nothing
            }
        }
    }
}