package com.penglecode.xmodule.common.support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterDisposer;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;

/**
 * 自定义的JdbcTemplate，增加了一些增强的方法
 * 
 * @author 	pengpeng
 * @date 	2018年6月9日 下午12:38:11
 */
public class CustomJdbcTemplate extends JdbcTemplate {

	public CustomJdbcTemplate() {
		super();
	}

	public CustomJdbcTemplate(DataSource dataSource, boolean lazyInit) {
		super(dataSource, lazyInit);
	}

	public CustomJdbcTemplate(DataSource dataSource) {
		super(dataSource);
	}

	public int[] batchUpdate(final String sql,final BatchPreparedStatementSetter pss,
			final KeyHolder generatedKeyHolder) throws DataAccessException {
		return execute(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				return conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			}
		}, new PreparedStatementCallback<int[]>() {
			@Override
			public int[] doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				if (logger.isDebugEnabled()) {
					logger.debug("Executing SQL batch update and returning generated keys [" + sql + "]");
				}
				try {
					int batchSize = pss.getBatchSize();
					int totalRowsAffected = 0;
					int[] rowsAffected = new int[batchSize];
					List<Map<String,Object>> generatedKeys = generatedKeyHolder.getKeyList();
					generatedKeys.clear();
					
					if (JdbcUtils.supportsBatchUpdates(ps.getConnection())) {
						for (int i = 0; i < batchSize; i++) {
							pss.setValues(ps, i);
							ps.addBatch();
						}
						rowsAffected = ps.executeBatch();
						for(int i = 0, len = rowsAffected.length; i < len; i++) {
							totalRowsAffected += rowsAffected[i];
						}
						extractGeneratedKeys(ps, generatedKeys, (int) (rowsAffected.length * 1.5));
					} else {
						for (int i = 0; i < batchSize; i++) {
							pss.setValues(ps, i);
							rowsAffected[i] = ps.executeUpdate();
							totalRowsAffected += rowsAffected[i];
							extractGeneratedKeys(ps, generatedKeys, 1);
						}
					}
					if (logger.isDebugEnabled()) {
						logger.debug("SQL batch update affected " + totalRowsAffected + " rows and returned " + generatedKeys.size() + " keys");
					}
					return rowsAffected;
				} finally {
					if (pss instanceof ParameterDisposer) {
						((ParameterDisposer) pss).cleanupParameters();
					}
				}
			}
		});
	}
	
	protected void extractGeneratedKeys(PreparedStatement ps, List<Map<String,Object>> generatedKeys, int extractKeySize) throws SQLException {
		try (ResultSet keys = ps.getGeneratedKeys()) {
			if (keys != null) {
				RowMapper<Map<String,Object>> rowMapper = new ColumnMapRowMapper();
				RowMapperResultSetExtractor<Map<String,Object>> rse = new RowMapperResultSetExtractor<>(rowMapper, extractKeySize);
				generatedKeys.addAll(rse.extractData(keys));
			}
		}
	}
	
}
