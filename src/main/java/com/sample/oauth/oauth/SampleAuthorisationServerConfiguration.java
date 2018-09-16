package com.sample.oauth.oauth;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.OAuth2AuthorizationServerConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@Configuration
@EnableAuthorizationServer
public class SampleAuthorisationServerConfiguration extends OAuth2AuthorizationServerConfiguration {


    private final BaseClientDetails details;
    private final AuthorizationServerProperties properties;
    private final AuthenticationManager authenticationManager;
    private final TokenStore tokenStore;


    public SampleAuthorisationServerConfiguration(BaseClientDetails details, AuthenticationManager authenticationManager, ObjectProvider<TokenStore> tokenStoreProvider, AuthorizationServerProperties properties) {
        super(details, authenticationManager, tokenStoreProvider, properties);
        this.authenticationManager = authenticationManager;
        this.tokenStore = tokenStoreProvider.getIfAvailable();
        this.details = details;
        this.properties = properties;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        // IMPORTANT STEP to override the ClientDetailsService and BaseClientDetails
        clients.setBuilder(new SampleInMemoryClientDetailsServiceBuilder());


        ClientDetailsServiceBuilder<InMemoryClientDetailsServiceBuilder>.ClientBuilder builder = clients
                .inMemory().withClient(this.details.getClientId());

        builder.secret(this.details.getClientSecret())
                .resourceIds(this.details.getResourceIds().toArray(new String[0]))
                .authorizedGrantTypes(
                        this.details.getAuthorizedGrantTypes().toArray(new String[0]))
                .authorities(
                        AuthorityUtils.authorityListToSet(this.details.getAuthorities())
                                .toArray(new String[0]))
                .scopes(this.details.getScope().toArray(new String[0]));

        if (this.details.getAutoApproveScopes() != null) {
            builder.autoApprove(true);
        }
        if (this.details.getAccessTokenValiditySeconds() != null) {
            builder.accessTokenValiditySeconds(
                    this.details.getAccessTokenValiditySeconds());
        }
        if (this.details.getRefreshTokenValiditySeconds() != null) {
            builder.refreshTokenValiditySeconds(
                    this.details.getRefreshTokenValiditySeconds());
        }
        if (this.details.getRegisteredRedirectUri() != null) {
            builder.redirectUris(
                    this.details.getRegisteredRedirectUri().toArray(new String[0]));
        }
    }

    @Configuration
    protected static class BaseClientDetailsConfiguration {

        private final OAuth2ClientProperties client;

        protected BaseClientDetailsConfiguration(OAuth2ClientProperties client) {
            this.client = client;
        }

        @Bean
        @ConfigurationProperties("security.oauth2.client")
        public BaseClientDetails oauth2ClientDetails() {
            BaseClientDetails details = new BaseClientDetails();
            if (this.client.getClientId() == null) {
                this.client.setClientId(UUID.randomUUID().toString());
            }
            details.setClientId(this.client.getClientId());
            details.setClientSecret(this.client.getClientSecret());
            details.setAuthorizedGrantTypes(Arrays.asList("authorization_code",
                    "password", "client_credentials", "implicit", "refresh_token"));
            details.setAuthorities(
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
            details.setRegisteredRedirectUri(Collections.<String>emptySet());
            details.setAutoApproveScopes(Arrays.asList(new String[]{"scope"}) );
            return details;
        }

    }



}
