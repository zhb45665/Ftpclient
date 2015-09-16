package com.zhb.Ftp;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import sun.net.ftp.FtpClient;

import com.zhb.tools.ExecuteSql;
import com.zhb.tools.FTPUtil;

public class Ftpclient extends JFrame {

	private static final long serialVersionUID = 0x607904c422b5d922L;
	private JPanel ftpclient;
	private JTextField tf_localpath;
	private JTextArea ta_Info;
	private FtpClient ftp;
	private JTextField tf_projectid;
	private String regex;
	private JTextField tf_StartTime;
	private JTextField tf_EndTime;
	private JTextField tf_DbIP;
	private JTextField tf_DbName;

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					Ftpclient frame = new Ftpclient();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}

	public Ftpclient() {
		regex = "/";
		setTitle("FtpClient");
		setDefaultCloseOperation(3);
		setBounds(100, 100, 741, 465);
		ftpclient = new JPanel();
		ftpclient.setToolTipText("FtpClient");
		setContentPane(ftpclient);
		ftpclient.setLayout(null);
		JLabel lblLocalpath = new JLabel("LocalPath");
		lblLocalpath.setBounds(27, 187, 61, 17);
		ftpclient.add(lblLocalpath);
		tf_localpath = new JTextField();
		tf_localpath.setBounds(125, 185, 255, 27);
		ftpclient.add(tf_localpath);
		tf_localpath.setColumns(10);
		JButton bt_select = new JButton("select");
		bt_select.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(2);
				jfc.showDialog(new JLabel(), "选择");
				File file = jfc.getSelectedFile();
				if (file.isDirectory()) {
					System.out.println((new StringBuilder("文件夹:")).append(
							file.getAbsolutePath()).toString());
					tf_localpath.setText(file.getAbsolutePath());
				} else if (file.isFile())
					System.out.println((new StringBuilder("文件:")).append(
							file.getAbsolutePath()).toString());
				System.out.println(jfc.getSelectedFile().getName());
			}
		});
		bt_select.setBounds(397, 187, 107, 27);
		ftpclient.add(bt_select);
		JButton bt_login = new JButton("LogIn");
		bt_login.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String ip = "10.1.253.152";
				int port = 21;
				String username = "tone";
				String password = "sxdxtone";
				ftp = FTPUtil.connectFTP(ip, port, username, password);
				ta_Info.append(ftp.getWelcomeMsg());
			}

		});
		bt_login.setBounds(577, 65, 107, 27);
		ftpclient.add(bt_login);
		ta_Info = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(ta_Info);
		scrollPane.setBounds(37, 234, 657, 180);
		ftpclient.add(scrollPane);
		JLabel lb_projectid = new JLabel("ProjectId");
		lb_projectid.setBounds(27, 98, 61, 17);
		ftpclient.add(lb_projectid);
		tf_projectid = new JTextField();
		tf_projectid.setBounds(125, 95, 151, 27);
		ftpclient.add(tf_projectid);
		tf_projectid.setColumns(10);
		JButton bt_DownLoad = new JButton("DownLoad");
		bt_DownLoad.addActionListener(new ActionListener() {

			private String projectid;
			private String StartTime;
			private String EndTime;
			private String DbIP;
			private String DbName;
			private String ProjectName;
			private String startdate;
			private String enddate;
			private String counts;
			private java.util.List list;

			public void actionPerformed(ActionEvent e) {
				if (tf_localpath.getText().equals(null)
						|| tf_localpath.equals("")) {
					ta_Info.append("LocalPath is null");
				} else {
					FTPUtil.changeDirectory(ftp, regex);
					ExecuteSql es = new ExecuteSql();
					projectid = tf_projectid.getText();
					StartTime = tf_StartTime.getText();
					EndTime = tf_EndTime.getText();
					DbIP = tf_DbIP.getText();
					DbName = tf_DbName.getText();
					try {
						SimpleDateFormat format = new SimpleDateFormat(
								"yyyyMMdd");
						startdate = Long.toString(
								format.parse(StartTime).getTime()).substring(0,
								10);
						enddate = Long
								.toString(format.parse(EndTime).getTime())
								.substring(0, 10);
						ProjectName = es
								.getProjectName(projectid, DbIP, DbName);
						counts = es.getCount(projectid, startdate, enddate,
								DbIP, DbName);
						System.out.println((new StringBuilder("total:"))
								.append(counts).toString());
						list = es.getpath(projectid, startdate, enddate, DbIP,
								DbName);
						for (int i = 0; i < list.size(); i++) {
							String filepath = (String) list.get(i);
							System.out.println(filepath);
							String ph[] = filepath.split(regex);
							String pah = (new StringBuilder(String
									.valueOf(ph[2]))).append(regex)
									.append(ph[3]).append(regex).append(ph[4])
									.append(regex).append(ph[5]).append(regex)
									.append(ph[6]).append(regex).append(ph[7])
									.toString();
							String dirpah = (new StringBuilder(String
									.valueOf(ph[0]))).append(regex)
									.append(ph[1]).append(regex).append(pah)
									.toString();
							String local = ph[8];
							String localdir = (new StringBuilder(String
									.valueOf(tf_localpath.getText())))
									.append(regex).append(ProjectName)
									.append(regex).append("成功").append(regex)
									.toString();
							FTPUtil.changeDirectory(ftp, dirpah);
							FTPUtil.download(
									(new StringBuilder(String.valueOf(localdir)))
											.append(local).toString(), local,
									ftp);
							FTPUtil.changeDirectory(ftp, regex);
						}

						System.out.println((new StringBuilder("total:"))
								.append(counts).toString());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		bt_DownLoad.setBounds(577, 118, 107, 27);
		ftpclient.add(bt_DownLoad);
		JLabel lb_startTime = new JLabel("StartTime");
		lb_startTime.setBounds(294, 51, 61, 17);
		ftpclient.add(lb_startTime);
		tf_StartTime = new JTextField();
		tf_StartTime.setBounds(362, 49, 170, 27);
		ftpclient.add(tf_StartTime);
		tf_StartTime.setColumns(10);
		JLabel lb_EndTime = new JLabel("EndTime");
		lb_EndTime.setBounds(294, 98, 61, 17);
		ftpclient.add(lb_EndTime);
		tf_EndTime = new JTextField();
		tf_EndTime.setBounds(362, 96, 170, 26);
		ftpclient.add(tf_EndTime);
		tf_EndTime.setColumns(10);
		JLabel lb_Dbhostip = new JLabel("DB_HostIP");
		lb_Dbhostip.setBounds(27, 51, 84, 17);
		ftpclient.add(lb_Dbhostip);
		tf_DbIP = new JTextField();
		tf_DbIP.setBounds(126, 46, 150, 27);
		ftpclient.add(tf_DbIP);
		tf_DbIP.setColumns(10);
		JLabel lblDbname = new JLabel("DB_Name");
		lblDbname.setBounds(27, 146, 61, 17);
		ftpclient.add(lblDbname);
		tf_DbName = new JTextField();
		tf_DbName.setBounds(125, 146, 255, 27);
		ftpclient.add(tf_DbName);
		tf_DbName.setColumns(10);
	}

}