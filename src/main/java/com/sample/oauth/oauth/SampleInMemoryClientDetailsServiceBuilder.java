package com.sample.oauth.oauth;

import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetailsService;

public class SampleInMemoryClientDetailsServiceBuilder extends InMemoryClientDetailsServiceBuilder {



    @Override
    protected ClientDetailsService performBuild() {
        return new SampleClientDetailsService();
    }

    @Override
    public InMemoryClientDetailsServiceBuilder inMemory() throws Exception {
        return this;
    }
}
