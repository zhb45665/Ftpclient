// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FTPUtil.java

package com.zhb.tools;

import java.io.*;
import java.net.InetSocketAddress;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;

public class FTPUtil {

	public FTPUtil() {
	}

	public static FtpClient connectFTP(String url, int port, String username,
			String password) {
		FtpClient ftp = null;
		try {
			java.net.SocketAddress addr = new InetSocketAddress(url, port);
			ftp = FtpClient.create();
			ftp.connect(addr);
			ftp.login(username, password.toCharArray());
			ftp.setBinaryType();
			System.out.println(ftp.getWelcomeMsg());
		} catch (FtpProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ftp;
	}

	public static void changeDirectory(FtpClient ftp, String path) {
		try {
			ftp.changeDirectory(path);
			System.out.println(ftp.getWorkingDirectory());
		} catch (FtpProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void disconnectFTP(FtpClient ftp) {
		try {
			ftp.close();
			System.out.println("disconnect success!!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void upload(String localFile, String ftpFile, FtpClient ftp) {
		try {
			OutputStream os;
			FileInputStream fis;
			os = null;
			fis = null;
			os = ftp.putFileStream(ftpFile);
			File file = new File(localFile);
			fis = new FileInputStream(file);
			byte bytes[] = new byte[1024];
			int c;
			while ((c = fis.read(bytes)) != -1)
				os.write(bytes, 0, c);
			System.out.println("upload success!!");

			if (os != null)
				os.close();
			if (fis != null)
				fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FtpProtocolException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	public static void download(String localFile, String ftpFile, FtpClient ftp) {
		try {
			InputStream is;
			FileOutputStream fos;
			is = null;
			fos = null;
			is = ftp.getFileStream(ftpFile);
			File file = new File(localFile);
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			byte bytes[] = new byte[0x100000];
			fos = new FileOutputStream(file);
			int i;
			while ((i = is.read(bytes)) != -1)
				fos.write(bytes, 0, i);

			if (fos != null)
				fos.close();
			if (is != null)
				is.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FtpProtocolException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	public void connFtp(String pah, String local, String localdir) {
		String ip = "10.1.253.152";
		int port = 21;
		String username = "tone";
		String password = "sxdxtone";
		FtpClient ftp = connectFTP(ip, port, username, password);
		System.out.println(ftp.getWelcomeMsg());
		changeDirectory(ftp, pah);
		System.out.println(pah);
		download((new StringBuilder(String.valueOf(localdir))).append(local)
				.toString(), local, ftp);
	}
}