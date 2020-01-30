package com.penglecode.xmodule.hydra.oauth2.examples.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.penglecode.xmodule.hydra.oauth2.examples.model.User;

public class DefaultUserDetailsService implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUserDetailsService.class);
	
	@Resource(name="defaultJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return getUserByUserName(username);
	}

	protected User getUserByUserName(String userName) {
		String sql = "select a.user_id userId, a.user_name userName, a.password password, a.nick_name nickName, a.role_codes roleCodes, DATE_FORMAT(a.create_time, '%Y-%m-%d %T') createTime from ss_simple_user a where a.user_name = ?";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] {userName}, new BeanPropertyRowMapper<>(User.class));
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error(e.getMessage());
			throw new UsernameNotFoundException(String.format("User '%s' not found!", userName));
		} catch (DataAccessException e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}
	
}
