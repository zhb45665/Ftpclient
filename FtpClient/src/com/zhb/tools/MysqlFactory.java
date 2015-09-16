package com.zhb.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MysqlFactory
{

	private Connection conn;
	private PreparedStatement stmt;
	private ResultSet rs;

	public MysqlFactory()
	{
		conn = null;
		stmt = null;
		rs = null;
	}

	public void MysqlConn(String DbIP, String DbName)
		throws Exception
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = (new StringBuilder("jdbc:mysql://")).append(DbIP).append(":3306/").append(DbName).toString();
			String user = "root";
			String password = "root";
			conn = DriverManager.getConnection(url, user, password);
		}
		catch (Exception e)
		{
			System.out.println((new StringBuilder("创建链接抛出异常为：")).append(e.getMessage()).toString());
		}
	}

	public ResultSet mysql_executeQuery(String sql)
		throws Exception
	{
		try
		{
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery(sql);
		}
		catch (Exception e)
		{
			System.out.println((new StringBuilder("执行查询抛出的异常为:")).append(e.getMessage()).toString());
		}
		return rs;
	}

	public void close()
		throws Exception
	{
		try
		{
			rs.close();
			stmt.close();
			conn.close();
		}
		catch (Exception e)
		{
			System.out.println((new StringBuilder("关闭对象抛出的异常：")).append(e.getMessage()).toString());
		}
	}
}