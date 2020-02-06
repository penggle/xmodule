package com.penglecode.xmodule.common.security.oauth2.client.reactive.support;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.security.oauth2.client.support.ClientRegistrationRepositoryAdapter;

public class ReactiveClientRegistrationRepositoryAdapter implements ClientRegistrationRepositoryAdapter {

	private final ReactiveClientRegistrationRepository clientRegistrationRepository;
	
	public ReactiveClientRegistrationRepositoryAdapter(ReactiveClientRegistrationRepository clientRegistrationRepository) {
		super();
		Assert.notNull(clientRegistrationRepository, "Parameter 'clientRegistrationRepository' must be required!");
		this.clientRegistrationRepository = clientRegistrationRepository;
	}

	@Override
	public ClientRegistration findByRegistrationId(String registrationId) {
		return clientRegistrationRepository.findByRegistrationId(registrationId).block();
	}

	protected ReactiveClientRegistrationRepository getClientRegistrationRepository() {
		return clientRegistrationRepository;
	}

}
