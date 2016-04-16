package com.common;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.client.S3ClientIF;
import com.server.S3Server;
import com.server.S3ServerIF;

public abstract class S3Task implements S3TaskIF {
	protected String uuid = null;
	private List retList = null;

	public S3Task(String uuid) {
		this.uuid = uuid;
	}

	public List getRetList() {
		return retList;
	}

	@Override
	public void run(S3ClientIF client, S3ServerIF server) throws RemoteException, SQLException {
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

	protected abstract String getSQLStatement();
}
