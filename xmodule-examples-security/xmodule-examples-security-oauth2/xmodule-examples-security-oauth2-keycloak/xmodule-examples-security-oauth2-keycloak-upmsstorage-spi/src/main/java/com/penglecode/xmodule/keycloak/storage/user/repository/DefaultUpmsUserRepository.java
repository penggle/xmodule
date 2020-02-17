package com.penglecode.xmodule.keycloak.storage.user.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import com.penglecode.xmodule.keycloak.storage.user.model.UpmsRole;
import com.penglecode.xmodule.keycloak.storage.user.model.UpmsUser;

@SuppressWarnings("unchecked")
public class DefaultUpmsUserRepository implements UpmsUserRepository {

	private final PasswordEncoder passwordEncoder;
	
	private final JdbcTemplate jdbcTemplate;

	public DefaultUpmsUserRepository(JdbcTemplate jdbcTemplate) {
		this(jdbcTemplate, new BCryptPasswordEncoder());
	}
	
	public DefaultUpmsUserRepository(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
		super();
		this.jdbcTemplate = jdbcTemplate;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UpmsUser getUserById(Long userId) {
		StringBuilder sql = new StringBuilder("SELECT a.user_id	userId, " + 
						"a.user_name	userName, " + 
						"a.real_name	realName, " + 
						"a.password		password, " + 
						"a.user_type	userType, " + 
						"a.nick_name	nickName, " + 
						"a.mobile_phone	mobilePhone, " + 
						"a.email		email, " + 
						"a.user_icon	userIcon, " + 
						"a.status		status, " + 
						"DATE_FORMAT(a.last_login_time, '%Y-%m-%d %T')	lastLoginTime, " + 
						"a.login_times	loginTimes, " + 
						"DATE_FORMAT(a.create_time, '%Y-%m-%d %T')	createTime, " + 
						"a.create_by	createBy, " + 
						"DATE_FORMAT(a.update_time, '%Y-%m-%d %T')	updateTime, " + 
						"a.update_by	updateBy " + 
				  " FROM upms_user a " + 
				  "WHERE a.user_id = ?");
		return jdbcTemplate.queryForObject(sql.toString(), new Object[] {userId}, new BeanPropertyRowMapper<>(UpmsUser.class));
	}

	@Override
	public UpmsUser getUserByUsername(String username) {
		StringBuilder sql = new StringBuilder("SELECT a.user_id	userId, " + 
						"a.user_name	userName, " + 
						"a.real_name	realName, " + 
						"a.password		password, " + 
						"a.user_type	userType, " + 
						"a.nick_name	nickName, " + 
						"a.mobile_phone	mobilePhone, " + 
						"a.email		email, " + 
						"a.user_icon	userIcon, " + 
						"a.status		status, " + 
						"DATE_FORMAT(a.last_login_time, '%Y-%m-%d %T')	lastLoginTime, " + 
						"a.login_times	loginTimes, " + 
						"DATE_FORMAT(a.create_time, '%Y-%m-%d %T')	createTime, " + 
						"a.create_by	createBy, " + 
						"DATE_FORMAT(a.update_time, '%Y-%m-%d %T')	updateTime, " + 
						"a.update_by	updateBy " + 
				  " FROM upms_user a " + 
				  "WHERE a.user_name = ?");
		return jdbcTemplate.queryForObject(sql.toString(), new Object[] {username}, new BeanPropertyRowMapper<>(UpmsUser.class));
	}

	@Override
	public UpmsUser getUserByEmail(String email) {
		throw new IllegalStateException("Not Supported");
	}
	
	@Override
	public List<UpmsRole> getUserRoles(Long userId) {
		StringBuilder sql = new StringBuilder("SELECT a.role_id	roleId, " + 
					"a.role_name	roleName, " + 
					"a.role_code	roleCode, " + 
					"a.role_type	roleType, " + 
					"a.description	description, " + 
					"DATE_FORMAT(a.create_time, '%Y-%m-%d %T')	createTime, " + 
					"a.create_by	createBy, " + 
					"DATE_FORMAT(a.update_time, '%Y-%m-%d %T')	updateTime, " + 
					"a.update_by	updateBy " + 
				" FROM upms_role a, upms_user_role b " + 
				"WHERE a.role_id = b.role_id " + 
				"  AND b.user_id = ? " + 
				"ORDER BY a.role_id DESC");
		return jdbcTemplate.query(sql.toString(), new Object[] {userId}, new BeanPropertyRowMapper<>(UpmsRole.class));
	}

	@Override
	public int getUsersCount() {
		return jdbcTemplate.queryForObject("SELECT count(*) FROM upms_user", Integer.class);
	}

	@Override
	public List<UpmsUser> getUsers() {
		return getUsers(null, null);
	}

	@Override
	public List<UpmsUser> getUsers(Integer offset, Integer limit) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("SELECT a.user_id	userId, " + 
						"a.user_name	userName, " + 
						"a.real_name	realName, " + 
						"a.password		password, " + 
						"a.user_type	userType, " + 
						"a.nick_name	nickName, " + 
						"a.mobile_phone	mobilePhone, " + 
						"a.email		email, " + 
						"a.user_icon	userIcon, " + 
						"a.status		status, " + 
						"DATE_FORMAT(a.last_login_time, '%Y-%m-%d %T')	lastLoginTime, " + 
						"a.login_times	loginTimes, " + 
						"DATE_FORMAT(a.create_time, '%Y-%m-%d %T')	createTime, " + 
						"a.create_by	createBy, " + 
						"DATE_FORMAT(a.update_time, '%Y-%m-%d %T')	updateTime, " + 
						"a.update_by	updateBy " + 
				  " FROM upms_user a " + 
				  "WHERE 1=1 " +
				  "ORDER BY a.user_id DESC ");
		if(offset != null && offset >= 0 && limit != null && limit > 0) {
			sql.append("LIMIT ? OFFSET ?");
			paramList.add(limit);
			paramList.add(offset);
		}
		return jdbcTemplate.query(sql.toString(), paramList.toArray(), new BeanPropertyRowMapper<>(UpmsUser.class));
	}

	@Override
	public List<UpmsUser> searchForUser(String search) {
		return searchForUser(search, null, null);
	}

	@Override
	public List<UpmsUser> searchForUser(String search, Integer offset, Integer limit) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("SELECT a.user_id	userId, " + 
						"a.user_name	userName, " + 
						"a.real_name	realName, " + 
						"a.password		password, " + 
						"a.user_type	userType, " + 
						"a.nick_name	nickName, " + 
						"a.mobile_phone	mobilePhone, " + 
						"a.email		email, " + 
						"a.user_icon	userIcon, " + 
						"a.status		status, " + 
						"DATE_FORMAT(a.last_login_time, '%Y-%m-%d %T')	lastLoginTime, " + 
						"a.login_times	loginTimes, " + 
						"DATE_FORMAT(a.create_time, '%Y-%m-%d %T')	createTime, " + 
						"a.create_by	createBy, " + 
						"DATE_FORMAT(a.update_time, '%Y-%m-%d %T')	updateTime, " + 
						"a.update_by	updateBy " + 
				  " FROM upms_user a " + 
				  "WHERE 1=1 ");
		if(StringUtils.hasText(search)) {
			sql.append("AND (a.user_name like CONCAT('%', ?, '%') OR a.email like CONCAT('%', ?, '%'))");
			paramList.add(search);
			paramList.add(search);
		}
		sql.append("ORDER BY a.user_id DESC ");
		if(offset != null && offset >= 0 && limit != null && limit > 0) {
			sql.append("LIMIT ? OFFSET ?");
			paramList.add(limit);
			paramList.add(offset);
		}
		return jdbcTemplate.query(sql.toString(), paramList.toArray(), new BeanPropertyRowMapper<>(UpmsUser.class));
	}

	@Override
	public List<UpmsUser> searchForUser(Map<String, String> params) {
		return searchForUser(params, null, null);
	}


	@Override
	public List<UpmsUser> searchForUser(Map<String, String> params, Integer offset, Integer limit) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("SELECT a.user_id	userId, " + 
						"a.user_name	userName, " + 
						"a.real_name	realName, " + 
						"a.password		password, " + 
						"a.user_type	userType, " + 
						"a.nick_name	nickName, " + 
						"a.mobile_phone	mobilePhone, " + 
						"a.email		email, " + 
						"a.user_icon	userIcon, " + 
						"a.status		status, " + 
						"DATE_FORMAT(a.last_login_time, '%Y-%m-%d %T')	lastLoginTime, " + 
						"a.login_times	loginTimes, " + 
						"DATE_FORMAT(a.create_time, '%Y-%m-%d %T')	createTime, " + 
						"a.create_by	createBy, " + 
						"DATE_FORMAT(a.update_time, '%Y-%m-%d %T')	updateTime, " + 
						"a.update_by	updateBy " + 
				  " FROM upms_user a " + 
				  "WHERE 1=1 ");
		if(params != null && params.containsKey("username") && StringUtils.hasText(params.get("username"))) {
			sql.append("AND a.user_name like CONCAT('%', ?, '%')");
			paramList.add(params.get("username"));
		}
		if(params != null && params.containsKey("email") && StringUtils.hasText(params.get("email"))) {
			sql.append("AND a.email like CONCAT('%', ?, '%')");
			paramList.add(params.get("email"));
		}
		sql.append("ORDER BY a.user_id DESC ");
		if(offset != null && offset >= 0 && limit != null && limit > 0) {
			sql.append("LIMIT ? OFFSET ?");
			paramList.add(limit);
			paramList.add(offset);
		}
		return jdbcTemplate.query(sql.toString(), paramList.toArray(), new BeanPropertyRowMapper<>(UpmsUser.class));
	}

	@Override
	public List<UpmsUser> getGroupMembers(Long groupId) {
		return getGroupMembers(groupId, null, null);
	}

	@Override
	public List<UpmsUser> getGroupMembers(Long groupId, Integer offset, Integer limit) {
		return Collections.EMPTY_LIST;
	}

	@Override
	public boolean validateCredentials(String username, String password) {
		UpmsUser user = getUserByUsername(username);
		return passwordEncoder.matches(password, user.getPassword());
	}
	
	protected JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	
	protected PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}
	
}
