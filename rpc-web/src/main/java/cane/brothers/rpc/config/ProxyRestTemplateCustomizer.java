package cane.brothers.rpc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by mniedre on 04.04.2017.
 */
@Component
public class ProxyRestTemplateCustomizer implements UserInfoRestTemplateCustomizer {

    @Autowired
    public SimpleClientHttpRequestFactory requestFactory;

    @Override
    public void customize(OAuth2RestTemplate oAuth2RestTemplate) {
        oAuth2RestTemplate.setRequestFactory(requestFactory);
        oAuth2RestTemplate.setAccessTokenProvider(get());
    }

    private AccessTokenProvider get() {
        // 1
        AuthorizationCodeAccessTokenProvider p1 = new AuthorizationCodeAccessTokenProvider();
        p1.setRequestFactory(requestFactory);

        //2
        ImplicitAccessTokenProvider p2 = new ImplicitAccessTokenProvider();
        p2.setRequestFactory(requestFactory);

        // 3
        ResourceOwnerPasswordAccessTokenProvider p3 = new ResourceOwnerPasswordAccessTokenProvider();
        p3.setRequestFactory(requestFactory);

        // 4
        ClientCredentialsAccessTokenProvider p4 = new ClientCredentialsAccessTokenProvider();
        p4.setRequestFactory(requestFactory);

        AccessTokenProviderChain accessTokenProvider = new AccessTokenProviderChain(
                Arrays.<AccessTokenProvider> asList(p1, p2, p3, p4));
        accessTokenProvider.setRequestFactory(requestFactory);
        return accessTokenProvider;
    }
}
