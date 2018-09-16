package com.sample.oauth.oauth;

import org.springframework.security.oauth2.provider.client.BaseClientDetails;

public class SampleClientDetails extends BaseClientDetails {

    @Override
    public boolean isAutoApprove(String scope) {
        return true;
    }
}
