package com.penglecode.xmodule.keycloak.storage.user.provider;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;

import com.penglecode.xmodule.keycloak.storage.user.model.UpmsUser;
import com.penglecode.xmodule.keycloak.storage.user.model.UpmsUserAdapter;
import com.penglecode.xmodule.keycloak.storage.user.repository.UpmsUserRepository;
import com.penglecode.xmodule.keycloak.storage.util.StorageUtils;

/**
 * 外部基于RBAC权限体系的只读UserStorageProvider
 * 
 * @author 	pengpeng
 * @date	2019年12月19日 下午7:05:44
 */
@SuppressWarnings("unchecked")
public class UpmsUserStorageProvider implements UserStorageProvider, UserLookupProvider, UserQueryProvider, CredentialInputValidator {

	private static final Logger LOGGER = Logger.getLogger(UpmsUserStorageProvider.class);
	
	private final KeycloakSession session;
	
	private final ComponentModel model;
	
	private final UpmsUserRepository repository;
	
	/**
	 * 用户登录时使用的UserModel缓存
	 * (注: 用户登录时会连续调用：getUserByUsername() -> getUserById() -> getUserById()，因此有使用缓存的必要)
	 */
	private final ConcurrentMap<String,UserModel> cachedUserModels = new ConcurrentHashMap<String,UserModel>();
	
	public UpmsUserStorageProvider(KeycloakSession session, ComponentModel model, UpmsUserRepository repository) {
		super();
		this.session = session;
		this.model = model;
		this.repository = repository;
	}

	@Override
	public void close() {
		cachedUserModels.clear();
		LOGGER.info(String.format(">>> Closing %s", UpmsUserStorageProvider.class.getSimpleName()));
	}
	
	@Override
	public UserModel getUserById(String id, RealmModel realm) {
		UserModel userModel = null;
		if((userModel = cachedUserModels.get(id)) == null) {
			Long userId = StorageUtils.getExternalId(id);
			UpmsUser user = repository.getUserById(userId);
			LOGGER.info(String.format(">>> getUserById(%s) = %s", userId, user));
			userModel = createAdminUserAdapter(session, realm, model, user);
			cachedUserModels.put(userModel.getId(), userModel);
		}
		return userModel;
	}

	@Override
	public UserModel getUserByUsername(String username, RealmModel realm) {
		UpmsUser user = repository.getUserByUsername(username);
		LOGGER.info(String.format(">>> getUserByUsername(%s) = %s", username, user));
		UserModel userModel = createAdminUserAdapter(session, realm, model, user);
		cachedUserModels.put(userModel.getId(), userModel);
		return userModel;
	}

	@Override
	public UserModel getUserByEmail(String email, RealmModel realm) {
		UpmsUser user = repository.getUserByEmail(email);
		LOGGER.info(String.format(">>> getUserByEmail(%s) = %s", email, user));
		UserModel userModel = createAdminUserAdapter(session, realm, model, user);
		cachedUserModels.put(userModel.getId(), userModel);
		return userModel;
	}

	@Override
	public int getUsersCount(RealmModel realm) {
		int count = repository.getUsersCount();
		LOGGER.info(String.format(">>> getUsersCount() = %s", count));
		return count;
	}

	@Override
	public List<UserModel> getUsers(RealmModel realm) {
		List<UpmsUser> users = repository.getUsers();
		LOGGER.info(String.format(">>> getUsers() = %s", users));
		return createAdminUserAdapters(session, realm, model, users);
	}

	@Override
	public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults) {
		List<UpmsUser> users = repository.getUsers(firstResult, maxResults);
		LOGGER.info(String.format(">>> getUsers(%s, %s) = %s", firstResult, maxResults, users));
		return createAdminUserAdapters(session, realm, model, users);
	}

	@Override
	public List<UserModel> searchForUser(String search, RealmModel realm) {
		List<UpmsUser> users = repository.searchForUser(search);
		LOGGER.info(String.format(">>> searchForUser(%s) = %s", search, users));
		return createAdminUserAdapters(session, realm, model, users);
	}

	@Override
	public List<UserModel> searchForUser(String search, RealmModel realm, int firstResult, int maxResults) {
		List<UpmsUser> users = repository.searchForUser(search, firstResult, maxResults);
		LOGGER.info(String.format(">>> searchForUser(%s, %s, %s) = %s", search, firstResult, maxResults, users));
		return createAdminUserAdapters(session, realm, model, users);
	}

	@Override
	public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm) {
		List<UpmsUser> users = repository.searchForUser(params);
		LOGGER.info(String.format(">>> searchForUser(%s) = %s", params, users));
		return createAdminUserAdapters(session, realm, model, users);
	}

	@Override
	public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm, int firstResult,
			int maxResults) {
		List<UpmsUser> users = repository.searchForUser(params, firstResult, maxResults);
		LOGGER.info(String.format(">>> searchForUser(%s, %s, %s) = %s", params, firstResult, maxResults, users));
		return createAdminUserAdapters(session, realm, model, users);
	}

	@Override
	public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group, int firstResult, int maxResults) {
		Long groupId = StorageUtils.getExternalId(group.getId());
		List<UpmsUser> users = repository.getGroupMembers(groupId, firstResult, maxResults);
		LOGGER.info(String.format(">>> getGroupMembers(%s, %s, %s) = %s", group.getId(), firstResult, maxResults, users));
		return createAdminUserAdapters(session, realm, model, users);
	}

	@Override
	public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group) {
		Long groupId = StorageUtils.getExternalId(group.getId());
		List<UpmsUser> users = repository.getGroupMembers(groupId);
		LOGGER.info(String.format(">>> getGroupMembers(%s) = %s", group.getId(), users));
		return createAdminUserAdapters(session, realm, model, users);
	}

	@Override
	public List<UserModel> searchForUserByUserAttribute(String attrName, String attrValue, RealmModel realm) {
		return Collections.EMPTY_LIST;
	}

	@Override
	public boolean supportsCredentialType(String credentialType) {
		return PasswordCredentialModel.TYPE.equals(credentialType);
	}

	@Override
	public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
		return supportsCredentialType(credentialType);
	}

	/**
	 * 校验密码
	 */
	@Override
	public boolean isValid(RealmModel realm, UserModel user, CredentialInput credentialInput) {
		if (!supportsCredentialType(credentialInput.getType()) || !(credentialInput instanceof UserCredentialModel)) {
            return false;
        }
        UserCredentialModel userCredentialInput = (UserCredentialModel) credentialInput;
        boolean valid = repository.validateCredentials(user.getUsername(), userCredentialInput.getChallengeResponse());
        LOGGER.info(String.format(">>> isValid(%s, %s) = %s", user.getUsername(), userCredentialInput.getChallengeResponse(), valid));
        return valid;
	}
	
	protected UserModel createAdminUserAdapter(KeycloakSession session, RealmModel realm, ComponentModel model, UpmsUser user) {
		if(user != null) {
			return new UpmsUserAdapter(session, realm, model, user, repository);
		}
		return null;
	}
	
	protected List<UserModel> createAdminUserAdapters(KeycloakSession session, RealmModel realm, ComponentModel model, List<UpmsUser> users) {
		if(users != null) {
			return users.stream().map(user -> new UpmsUserAdapter(session, realm, model, user, repository)).collect(Collectors.toList());
		}
		return Collections.EMPTY_LIST;
	}
	
	protected KeycloakSession getSession() {
		return session;
	}

	protected ComponentModel getModel() {
		return model;
	}

	protected UpmsUserRepository getRepository() {
		return repository;
	}

	
}
