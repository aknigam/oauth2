package com.sample.oauth.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;


public class SampleClientDetailsService extends InMemoryClientDetailsService {

    @Autowired
    private BaseClientDetails sampleClientDetails;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return sampleClientDetails;
    }
}
