package com.fuyao.myproject.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;

/**
 * FTP客户端工具类apache版
 * @author guoyj
 *
 */
@Component
public class ApacheFtpUtil {
    private static Logger log =  Logger.getLogger(ApacheFtpUtil.class);
    private FTPClient ftpClient;
    private String defaultEncoding = "UTF-8";

    //private static final

    public String getDefaultEncoding() {
        return defaultEncoding;
    }

    public void setDefaultEncoding(String defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
    }

    private ApacheFtpUtil() {

    }

    /**
     * 实例获取方法
     * @param server
     * @param port
     * @param loginName
     * @param loginPwd
     * @param encoding
     * @return
     */
    public static ApacheFtpUtil getInstance(String server, int port, String loginName, String loginPwd, String encoding) {
        ApacheFtpUtil ftpInstance = new ApacheFtpUtil();
        ftpInstance.init(server, port, loginName, loginPwd, encoding);
        return ftpInstance;
    }

    public static ApacheFtpUtil getInstance() {
        return new ApacheFtpUtil();
    }

    /**
     * @param server
     * @param port
     * @param loginName
     * @param loginPwd
     */
    public void init(String server, int port, String loginName, String loginPwd) {
        init(server, port, loginName, loginPwd, getDefaultEncoding());
    }

    /**
     * @param server
     * @param port
     * @param loginName
     * @param loginPwd
     */
    public void init(String server, int port, String loginName, String loginPwd, String encoding) {
        try {
            ftpClient = new FTPClient();
            //			ftpClient.configure(getFTPClientConfig());
            String encode = StringUtils.isEmpty(encoding) ? getDefaultEncoding() : encoding;
            log.debug("设置FTP客户端当前编码为:" + encoding);
            setDefaultEncoding(encode);
            ftpClient.setControlEncoding(encode);
//			ftpClient.setConnectTimeout(2000);
            ftpClient.setBufferSize(1024);

            ftpClient.connect(server, port);
            if (!ftpClient.login(loginName, loginPwd)) {
                ftpClient.logout();
                throw new Exception("登录FTP服务器失败,请检查![Server:" + server + "、" + "User:" + loginName + "、"
                        + "Password:" + loginPwd);
            }
            // 文件类型,默认是ASCII
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 设置被动模式
            ftpClient.enterLocalPassiveMode();
            // 响应信息
            if (!isPositive()) {
                throw new Exception("登录FTP服务器失败,请检查![Server:" + server + "、" + "User:" + loginName + "、"
                        + "Password:" + loginPwd);
            }
        } catch (Exception e) {
            log.error("", e);
            closeFtpConnection();
            ftpClient = null;
        }
    }


    /***************************************************************************
     * 连接是否有效的
     *
     * @return
     */
    public Boolean isPositive() {
        return FTPReply.isPositiveCompletion(ftpClient.getReplyCode());
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

        ftpClient.setControlEncoding(defaultEncoding);
        ftpClient.enterLocalPassiveMode();
        boolean flag = true;
        try {
            ftpClient.setFileType(fileType);
            flag = ftpClient.retrieveFile(new String(remoteFileName.getBytes(getDefaultEncoding()), "ISO-8859-1"),
                    outputStream);
        } catch (FileNotFoundException e) {
            log.error("", e);
            log.error(e);
            flag = false;
        } catch (IOException e) {
            log.error("", e);
            log.error(e);
            flag = false;
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            closeFtpConnection();
        }
        return flag;
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
    }

    /***************************************************************************
     * 上传文件
     *
     * @param remoteFileName
     * @throws IOException
     */
    public boolean upload(InputStream stream, String remoteFileName, Integer fileType) throws IOException {
        //		isPositiveable();
        boolean flag = true;
        try {
            ftpClient.setFileType(fileType);
            return ftpClient.storeFile(new String(remoteFileName.getBytes("GBK"), "ISO-8859-1"), stream);
        } catch (IOException e) {
            log.error("", e);
            log.error(e);
            flag = false;
        } finally {
            if (stream != null) {
                stream.close();
            }
            closeFtpConnection();
        }
        return flag;
    }


    /**
     * 上传文件
     * @param fileName
     * @param remoteFileName
     * @return
     * @throws IOException
     */
    public boolean upload(String fileName, String remoteFileName) throws IOException {
        File file = new File(fileName);
        InputStream inputStream = new FileInputStream(file);
        return upload(inputStream, remoteFileName, FTP.BINARY_FILE_TYPE);
    }



    /***************************************************************************
     * 删除单个文件
     *
     * @param pathName
     * @throws
     */
    public boolean delete(String pathName) {
        //		isPositiveable();
        boolean flag = true;
        try {
            return ftpClient.deleteFile(pathName);
        } catch (IOException e) {
            log.error("", e);
            flag = false;
        } finally {
            closeFtpConnection();
        }
        return flag;
    }



    /***************************************************************************
     * 移动文件
     *
     * @param remoteFileFrom
     *            目标文件
     * @param remoteFileTo
     *            移动至
     * @throws
     */
    public boolean move(String remoteFileFrom, String remoteFileTo) {
        //		isPositiveable();
        boolean flag = true;
        try {
            return ftpClient.rename(remoteFileFrom, remoteFileTo);
        } catch (IOException e) {
            log.error("", e);
            flag = false;
        } finally {
            closeFtpConnection();
        }
        return flag;
    }


    /**
     * 变更工作目录
     *
     * @param  --目录路径
     */
    public boolean changeDir(String remoteDirectory) {
        //		isPositiveable();
        boolean flag = true;
        try {
            flag = ftpClient.changeWorkingDirectory(new String(remoteDirectory.getBytes(getDefaultEncoding()),
                    "iso-8859-1"));
        } catch (IOException e) {
            log.error("", e);
            flag = false;
        }
        //		finally {
        //			closeFtpConnection();
        //		}
        return flag;
    }



    /***************************************************************************
     * 关闭FTP连接
     *
     * @throws
     */
    public void closeFtpConnection() {
        try {
            if (ftpClient != null && !isPositive()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }

    /**
     * 关闭连接
     */
    public void forceCloseConnection() {
        try {
            if (ftpClient != null && ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }


    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public static void main(String[] args) {
        ApacheFtpUtil ftp = ApacheFtpUtil.getInstance("10.1.252.197", 21, "crmtest", "crmtest", "gb2312");
        try {

            //ftp.changeDir("/ifdata1/data/F-IOP2.1-0005/20121117/23/");
            //ftp.download("D:/F_000IOP_F-IOP2.1-0005_000000_20121117230000.CHK", "F_000IOP_F-IOP2.1-0005_000000_20121117230000.CHK");
        } catch (Exception e) {
        }
		/*BufferedReader reader = null;
		try {
			File file = new File("D:/F_000IOP_F-IOP2.1-0005_000000_20121117230000.CHK");
			//		           log.debug("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				log.debug("line " + line + ": " + tempString);
				//"€^"分隔符解析
				log.debug(Arrays.toString(tempString.split("€", -1)));

				//		            	log.debug(Arrays.toString(tempString.split(new String(new byte[]{(byte)0x80}, -1))));
				line++;
			}
			reader.close();
		} catch (IOException e) {
		log.error("",e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		//		} catch (IOException e) {
		log.error("",e);
		//		}finally{
		//			ftp.forceCloseConnection();
		//		}
		*/}
}
