package cane.brothers.rpc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.OAuth2AccessTokenSupport;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mniedre on 04.04.2017.
 */
@Component
public class ProxyRestTemplateCustomizer implements UserInfoRestTemplateCustomizer {


    // factory with proxy
    @Autowired
    public SimpleClientHttpRequestFactory requestFactory;

    @Override
    public void customize(OAuth2RestTemplate oAuth2RestTemplate) {
        if (requestFactory != null) {
            oAuth2RestTemplate.setRequestFactory(requestFactory);
            oAuth2RestTemplate.setAccessTokenProvider(getAccessTokenProvider());
        }
    }

    private AccessTokenProvider getAccessTokenProvider() {
        List<AccessTokenProvider> list = Arrays.asList(new AccessTokenProvider[]{
                new AuthorizationCodeAccessTokenProvider(),
                new ImplicitAccessTokenProvider(),
                new ResourceOwnerPasswordAccessTokenProvider(),
                new ClientCredentialsAccessTokenProvider()});

        // set own factory
        for(AccessTokenProvider provider: list) {
            if(provider instanceof OAuth2AccessTokenSupport) {
                ((OAuth2AccessTokenSupport) provider).setRequestFactory(requestFactory);
            }
        }

        AccessTokenProviderChain accessTokenProvider = new AccessTokenProviderChain(list);
        accessTokenProvider.setRequestFactory(requestFactory);
        return accessTokenProvider;
    }
}
