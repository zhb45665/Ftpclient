package com.zhb.tools;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.zhb.tools:
//			MysqlFactory

public class ExecuteSql
{

	public ExecuteSql()
	{
	}

	public List getpath(String projectid, String startTime, String EndTime, String DbIP, String DbName)
		throws Exception
	{
		MysqlFactory mf = new MysqlFactory();
		String path = null;
		List list = new ArrayList(0x100000);
		try
		{
			mf.MysqlConn(DbIP, DbName);
			String sql = (new StringBuilder("select b.initpath,b.recpath from zx_rec_info b ,call_session a where  a.bscallid=b.bscallid and a.projectid = ")).append(projectid).append("  and a.ring_start_time >= ").append(startTime).append(" and a.ring_start_time < ").append(EndTime).append(" and (case when a.quality_result is not null then a.quality_result else a.sale_result end)='1'").toString();
			System.out.println(sql);
			for (ResultSet rs = mf.mysql_executeQuery(sql); rs.next(); list.add(path))
				path = (new StringBuilder(String.valueOf(rs.getString(1)))).append(rs.getString(2)).toString();

			mf.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}

	public String getProjectName(String projectid, String DbIP, String DbName)
		throws Exception
	{
		MysqlFactory mf = new MysqlFactory();
		String projectName = null;
		try
		{
			mf.MysqlConn(DbIP, DbName);
			String sql = (new StringBuilder("select project_name from project where id=")).append(projectid).toString();
			for (ResultSet rs = mf.mysql_executeQuery(sql); rs.next();)
				projectName = rs.getString(1);

			mf.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return projectName;
	}

	public String getCount(String projectid, String startTime, String EndTime, String DbIP, String DbName)
		throws Exception
	{
		MysqlFactory mf = new MysqlFactory();
		String counts = null;
		try
		{
			mf.MysqlConn(DbIP, DbName);
			String sql = (new StringBuilder("select count(*) from zx_rec_info b ,call_session a where  a.bscallid=b.bscallid and a.projectid = ")).append(projectid).append("  and a.ring_start_time >= ").append(startTime).append(" and a.ring_start_time < ").append(EndTime).append(" and (case when a.quality_result is not null then a.quality_result else a.sale_result end)='1'").toString();
			for (ResultSet rs = mf.mysql_executeQuery(sql); rs.next();)
				counts = rs.getString(1);

			mf.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return counts;
	}
}