package com.common;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.server.S3Server;
import com.server.S3ServerIF;

public abstract class S3DBTask implements S3TaskIF {
	private List<HashMap<String, Object>> retList = null;
	private String sqlStatement = "";
	
	public Object getResult() {
		return retList;
	}

	@Override
	public void run(S3ServerIF server) throws Exception {
		// TODO Auto-generated method stub

			String sql = getSQLStatement();
			ResultSet rs = null;
			if (sql != null&& !sql.equals("")){ 
				rs = ((S3Server) server).getDBMan().doQuery(sql); 
			}
			
			if (rs != null) {
				ResultSetMetaData md = rs.getMetaData();
				int columnCount = md.getColumnCount();
				retList = new ArrayList<HashMap<String, Object>>();
				while (rs.next()) {
					HashMap<String, Object> rowData = new HashMap<String, Object>(columnCount);
					for (int i = 1; i <= columnCount; i++) {
						rowData.put(md.getColumnName(i), rs.getObject(i));
					}
					retList.add(rowData);
				}

				rs.close();
			}
	}

	public void setSQLStatement(String sql) {
		sqlStatement = sql;
	}
	
	protected String getSQLStatement() {
		return sqlStatement;
	}
}
