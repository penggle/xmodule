package com.penglecode.xmodule.keycloak.storage.user.repository;

import java.util.List;
import java.util.Map;

import com.penglecode.xmodule.keycloak.storage.user.model.UpmsRole;
import com.penglecode.xmodule.keycloak.storage.user.model.UpmsUser;

/**
 * AdminUser的Repository
 * 
 * @author 	pengpeng
 * @date	2019年12月20日 上午11:24:24
 */
public interface UpmsUserRepository {

	/**
	 * 根据用户ID查询用户信息
	 * @param userId			- 用户ID
	 * @return
	 */
	public UpmsUser getUserById(Long userId);
	
	/**
	 * 根据用户名查询用户信息
	 * @param username			- 用户名
	 * @return
	 */
	public UpmsUser getUserByUsername(String username);
	
	/**
	 * 根据用户EMAIL查询用户信息
	 * @param email				- 用户EMAIL
	 * @return
	 */
	public UpmsUser getUserByEmail(String email);
	
	/**
	 * 根据用户ID获取用户角色列表
	 * @param userId
	 * @return
	 */
	public List<UpmsRole> getUserRoles(Long userId);
	
	/**
	 * 获取用户总数
	 * @return
	 */
	public int getUsersCount();
	
	/**
	 * 获取所有用户列表
	 * @return
	 */
	public List<UpmsUser> getUsers();
	
	/**
	 * 获取用户列表(分页)
	 * @param offset	- 起始记录下标
	 * @param limit		- 获取个数
	 * @return
	 */
	public List<UpmsUser> getUsers(Integer offset, Integer limit);
	
	/**
	 * 按条件查询用户列表
	 * @param search	- userName或者email
	 * @return
	 */
	public List<UpmsUser> searchForUser(String search);
	
	/**
	 * 按条件查询用户列表(分页)
	 * @param search	- userName或者email
	 * @param offset	- 起始记录下标
	 * @param limit		- 获取个数
	 * @return
	 */
	public List<UpmsUser> searchForUser(String search, Integer offset, Integer limit);
	
	/**
	 * 按条件查询用户列表(分页)
	 * @param params	- [username=username, email=email, first=fisrt name, last=last name]
	 * @param offset	- 起始记录下标
	 * @param limit		- 获取个数
	 * @return
	 */
	public List<UpmsUser> searchForUser(Map<String,String> params);
	
	/**
	 * 按条件查询用户列表(分页)
	 * @param params	- [username=username, email=email, first=fisrt name, last=last name]
	 * @param offset	- 起始记录下标
	 * @param limit		- 获取个数
	 * @return
	 */
	public List<UpmsUser> searchForUser(Map<String,String> params, Integer offset, Integer limit);
	
	/**
	 * 获取某个组下的全部用户
	 * @param groupId	- 组ID
	 * @return
	 */
	public List<UpmsUser> getGroupMembers(Long groupId);
	
	/**
	 * 获取某个组下的全部用户(分页)
	 * @param groupId	- 组ID
	 * @param offset	- 起始记录下标
	 * @param limit		- 获取个数
	 * @return
	 */
	public List<UpmsUser> getGroupMembers(Long groupId, Integer offset, Integer limit);
	
	/**
	 * 校验用户名和密码是否匹配
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean validateCredentials(String username, String password);
	
}