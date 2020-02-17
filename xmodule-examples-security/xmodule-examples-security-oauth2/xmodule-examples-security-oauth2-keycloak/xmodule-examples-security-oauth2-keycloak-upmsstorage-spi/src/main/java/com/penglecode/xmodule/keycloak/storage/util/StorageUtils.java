package com.penglecode.xmodule.keycloak.storage.util;

import org.keycloak.storage.StorageId;

public class StorageUtils {

	public static Long getExternalId(String id) {
		return Long.valueOf(StorageId.externalId(id));
	}
	
}
