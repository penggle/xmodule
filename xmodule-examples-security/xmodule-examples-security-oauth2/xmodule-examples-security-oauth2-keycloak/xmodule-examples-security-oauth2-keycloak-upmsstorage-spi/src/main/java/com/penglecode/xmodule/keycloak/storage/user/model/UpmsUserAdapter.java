package com.penglecode.xmodule.keycloak.storage.user.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapter;

import com.penglecode.xmodule.keycloak.storage.user.repository.UpmsUserRepository;
import com.penglecode.xmodule.keycloak.storage.util.StorageUtils;

@SuppressWarnings("unchecked")
public class UpmsUserAdapter extends AbstractUserAdapter {
	private static final Logger LOGGER = Logger.getLogger(UpmsUserAdapter.class);
	
	private final KeycloakSession session;
	private final RealmModel realm;
	private final ComponentModel model;
	private final UpmsUser user;
    private final String kcId;
    private final UpmsUserRepository repository;
    private Set<RoleModel> cachedUserRoles;
	
	public UpmsUserAdapter(KeycloakSession session, RealmModel realm, ComponentModel model, UpmsUser user, UpmsUserRepository repository) {
		super(session, realm, model);
		this.session = session;
		this.realm = realm;
		this.model = model;
		this.user = user;
		this.kcId = StorageId.keycloakId(model, user.getUserId().toString());
		this.repository = repository;
	}
	
	@Override
	public String getId() {
		return kcId;
	}

	@Override
	public String getUsername() {
		return user.getUserName();
	}

	@Override
	public boolean isEnabled() {
		return user.getStatus() == 1;
	}

	@Override
	public Long getCreatedTimestamp() {
		try {
			if(user.getCreateTime() != null) {
				return LocalDateTime.parse(user.getCreateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			}
		} catch (Exception e) {}
		return null;
	}

	@Override
	public String getEmail() {
		return user.getEmail();
	}

	protected boolean appendDefaultGroups() {
        return false;
    }
	
	@Override
	protected boolean appendDefaultRolesToRoleMappings() {
        return false;
    }
	
	protected Set<RoleModel> getRoleMappingsInternal() {
		if(cachedUserRoles == null) {
			Long userId = StorageUtils.getExternalId(kcId);
	        List<UpmsRole> userRoles = getRepository().getUserRoles(userId);
	        LOGGER.info(String.format(">>> getUserRoles(%s) = %s", userId, userRoles));
	        if(userRoles != null) {
	        	cachedUserRoles = userRoles.stream().map(role -> new UpmsRoleAdapter(session, realm, model, role)).collect(Collectors.toSet());
	        } else {
	        	cachedUserRoles = Collections.EMPTY_SET;
	        }
		}
        return cachedUserRoles;
    }

	protected KeycloakSession getSession() {
		return session;
	}

	protected RealmModel getRealm() {
		return realm;
	}

	protected ComponentModel getModel() {
		return model;
	}

	protected UpmsUser getUser() {
		return user;
	}

	protected UpmsUserRepository getRepository() {
		return repository;
	}
	
}
