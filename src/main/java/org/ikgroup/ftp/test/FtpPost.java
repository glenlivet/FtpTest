package org.ikgroup.ftp.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * 
 * 
 * @author glenlivet
 *
 */
public class FtpPost {

	public static void main(String[] args) {
		
		if (args.length % 2 != 0) {
			throw new IllegalArgumentException("参数错误！");
		}

		String server = null;
		int port = 21;
		String user = null;
		String pass = null;

		String localFile = null;

		String remoteFile = null;

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-S")) {
				// server
				server = args[++i];
			} else if (args[i].equals("-PORT")) {
				port = Integer.parseInt(args[++i]);
			} else if (args[i].equals("-U")) {
				user = args[++i].toString();
			} else if (args[i].equals("-P")) {
				pass = args[++i].toString();
			} else if (args[i].equals("-L")) {
				localFile = args[++i].toString();
			} else if (args[i].equals("-R")) {
				remoteFile = args[++i].toString();
			}
		}

		if (server == null || user == null || pass == null || localFile == null
				|| remoteFile == null) {
			throw new IllegalArgumentException("参数错误！");
		}

		FTPClient ftpClient = new FTPClient();

		try {
			ftpClient.connect(server, port);
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				throw new IOException("Exception in connecting to FTP Server");
			}
			
			boolean ss = ftpClient.login(user, pass);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
			ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);

			
			
			File localFileObj = new File(localFile);
			InputStream inputStream = new FileInputStream(localFileObj);

			System.out.println("开始传输文件！");
			String rr = "tt.php";
			boolean done = ftpClient.storeFile(rr, inputStream);
			
			reply = ftpClient.getReplyCode();
			
			inputStream.close();
			if (done) {
				System.out.println("传输完毕！");
			} else {
				System.out.println("传输失败！");
			}

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

}
