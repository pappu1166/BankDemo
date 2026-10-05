package com.app.blog.reposistories;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MakeJNDICall {

	@Autowired(required = false)
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> selectMultipleResults(String sql, Object... args) {
		return jdbcTemplate.queryForList(sql, args);
	}

	public Map<String, Object> selectMultipleQuery(String sql, Object... args) {
		return jdbcTemplate.queryForMap(sql, args);
	}

	public Integer selectSingleResultInteger(String sql, Object... args) {
		return jdbcTemplate.queryForObject(sql, Integer.class, args);
	}

	public String selectSingleResultString(String sql, Object... args) {
		return jdbcTemplate.queryForObject(sql, String.class, args);
	}

	public int updateQuery(String sql, Object... args) {
		return jdbcTemplate.update(sql, args);
	}
}

