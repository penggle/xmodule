package com.penglecode.xmodule.keycloak.storage.user.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleContainerModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.utils.RoleUtils;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.federated.UserFederatedStorageProvider;

@SuppressWarnings("unchecked")
public class UpmsRoleAdapter implements RoleModel {

	private final KeycloakSession session;
	private final RealmModel realm;
	private final ComponentModel model;
	private final UpmsRole role;
	private final String kcId;
	
	public UpmsRoleAdapter(KeycloakSession session, RealmModel realm, ComponentModel model, UpmsRole role) {
			this.session = session;
			this.realm = realm;
			this.model = model;
			this.role = role;
			this.kcId = StorageId.keycloakId(model, role.getRoleId().toString());
		}

	@Override
	public String getName() {
		return role.getRoleCode();
	}

	@Override
	public String getDescription() {
		return role.getDescription();
	}

	@Override
	public void setDescription(String description) {
		role.setDescription(description);
	}

	@Override
	public String getId() {
		return kcId;
	}

	@Override
	public void setName(String name) {
		role.setRoleCode(name);
	}

	@Override
	public boolean isComposite() {
		return false;
	}

	@Override
	public void addCompositeRole(RoleModel role) {
		//NOOP
	}

	@Override
	public void removeCompositeRole(RoleModel role) {
		//NOOP
	}

	@Override
	public Set<RoleModel> getComposites() {
		return Collections.EMPTY_SET;
	}

	@Override
	public boolean isClientRole() {
		return false;
	}

	@Override
	public String getContainerId() {
		return realm.getId();
	}

	@Override
	public RoleContainerModel getContainer() {
		return realm;
	}

	@Override
	public boolean hasRole(RoleModel role) {
		return RoleUtils.hasRole(getComposites(), role);
	}

	@Override
	public void setSingleAttribute(String name, String value) {
		getFederatedStorage().setSingleAttribute(realm, this.getId(), name, value);
	}

	@Override
	public void setAttribute(String name, Collection<String> values) {
		getFederatedStorage().setAttribute(realm, this.getId(), name, new ArrayList<>(values));
	}

	@Override
	public void removeAttribute(String name) {
		getFederatedStorage().removeAttribute(realm, this.getId(), name);
	}

	@Override
	public String getFirstAttribute(String name) {
		return getFederatedStorage().getAttributes(realm, this.getId()).getFirst(name);
	}

	@Override
	public List<String> getAttribute(String name) {
		return getFederatedStorage().getAttributes(realm, this.getId()).get(name);
	}

	@Override
	public Map<String, List<String>> getAttributes() {
		 return getFederatedStorage().getAttributes(realm, this.getId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kcId == null) ? 0 : kcId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UpmsRoleAdapter other = (UpmsRoleAdapter) obj;
		if (kcId == null) {
			if (other.kcId != null)
				return false;
		} else if (!kcId.equals(other.kcId))
			return false;
		return true;
	}

	protected UserFederatedStorageProvider getFederatedStorage() {
        return session.userFederatedStorage();
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

	protected UpmsRole getRole() {
		return role;
	}
	
}
