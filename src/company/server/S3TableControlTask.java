package company.server;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.common.S3DBTask;

import company.S3Const;
import company.S3TableOPType;

public class S3TableControlTask extends S3DBTask {
	private S3TableOPType opType;
	private Object opArgs0;
	private Object opArgs1;
	private String tableName;
	
	public S3TableControlTask(String tType, S3TableOPType type, Object args0, Object args1) {
		// TODO Auto-generated constructor stub
		opType = type;
		opArgs0 = args0;
		opArgs1 = args1;
		
		tableName = S3Const.TABLE_PREFIX + tType;
	}
	
	protected String getConditionStatement(Map<String, ?> conditions, String link) {
		String statement = "";
		
		if (conditions != null) {
			Iterator<String> itr = conditions.keySet().iterator();
			while(itr.hasNext()) {
				String k = itr.next();
				Object v = conditions.get(k);
				
				statement += k;
				statement += "=";
				
				if (v instanceof String) {
					statement += ("'" + v + "'");
				} else {
					statement += v;
				}
				
				if (itr.hasNext()) {
					statement += (" " + link + " ");
				}
			}
		}

		return statement;
	}
	
	protected String select( List<?> attributes, Map<String, ?> conditions ) {
		String attrs = null;
		if ( attributes != null ) {
			int aSize = attributes.size();
			for (int i = 0; i < aSize; i++) {
				attrs += attributes.get(0);
				if (i != aSize - 1) {
					attrs += ",";
				}
			}
		} else {
			attrs = "*";
		}
		
		String statement = String.format("SELECT %s FROM %s", attrs, tableName); 
		String conds = getConditionStatement(conditions, "AND");
		if (conds.length() > 0) {
			statement += (" WHERE " + conds);
		}
		
		return statement;
	}
	
	protected String update(Map<String, ?> data, Map<String, ?> conditions) {
		String sets = getConditionStatement(data, ",");
		String conds = getConditionStatement(conditions, "AND");
		String statement = String.format("UPDATE %s SET %s", tableName, sets); 
		
		if (conds.length() > 0) {
			statement += (" WHERE " + conds);
		}
		
		return statement;
	}
	
	protected String insert(List<?> data) {
		int size = data.size();
		String vs = "";
		
		for (int i = 0; i < size; i++) {
			Object v = data.get(i);
			
			if (v instanceof String) {
				vs += ("'" + v + "'");
			} else {
				vs += v;
			}
			if (i != (size - 1)) {
				vs += ",";
			}
		}
	
		return String.format("INSERT INTO %s VALUES(%s)", tableName, vs);
	}


	@Override
	protected String getSQLStatement() {
		// TODO Auto-generated method stub
		
		if (opType == S3TableOPType.UPDATE) {
			return update( (Map<String, ?>)opArgs0, (Map<String, ?>)opArgs1 );
		}
		
		if (opType == S3TableOPType.SELECT) {
			return select( (List<?>)opArgs0, (Map<String, ?>)opArgs1 );
		}
		
		if (opType == S3TableOPType.INSERT) {
			return insert( (List<?>)opArgs0 );
		}
		
		if (opType == S3TableOPType.COUNT) {
			return String.format("SELECT COUNT(*) FROM %s", tableName); 
		}
		
		return null;
	}

}
