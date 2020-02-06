package com.penglecode.xmodule.common.security.oauth2.client.servlet.support;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.security.oauth2.client.support.ClientRegistrationRepositoryAdapter;

public class ServletClientRegistrationRepositoryAdapter implements ClientRegistrationRepositoryAdapter {

	private final ClientRegistrationRepository clientRegistrationRepository;
	
	public ServletClientRegistrationRepositoryAdapter(ClientRegistrationRepository clientRegistrationRepository) {
		super();
		Assert.notNull(clientRegistrationRepository, "Parameter 'clientRegistrationRepository' must be required!");
		this.clientRegistrationRepository = clientRegistrationRepository;
	}

	@Override
	public ClientRegistration findByRegistrationId(String registrationId) {
		return clientRegistrationRepository.findByRegistrationId(registrationId);
	}

	protected ClientRegistrationRepository getClientRegistrationRepository() {
		return clientRegistrationRepository;
	}

}
