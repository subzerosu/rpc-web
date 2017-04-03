package cane.brothers.rpc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * Created by cane on 26.02.17.
 */
@EnableOAuth2Sso
//@EnableOAuth2Client
@EnableGlobalMethodSecurity(prePostEnabled=true)
@Configuration
public class OAuth2SsoSecurityConfig extends WebSecurityConfigurerAdapter {

    private static Logger logger = LoggerFactory.getLogger(OAuth2SsoSecurityConfig.class);

    // @Override
    // public void configure(WebSecurity web) {
    // web.ignoring().antMatchers("/console/**");
    // }

    @Bean
    public OAuth2RestOperations oauth2RestTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
        return new OAuth2RestTemplate(resource, context);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.debug("Настраиваем безопасность.");
        http
             //   .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login**", "/assets/**", "/app/**", "/webjars/**", "/api/**")
                .permitAll()
                .anyRequest()
                .authenticated()

                // logout
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll()

                // csrf
                .and()
                .csrf()
                //.disable();
               .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

                // X-Frame-Options
                //.and()
                //.headers()
               // .frameOptions()
               // .disable();
    }

//    @Bean
//    @ConfigurationProperties(prefix = "security.oauth2.client")
//    public AuthorizationCodeResourceDetails oauth2Client() {
//        return new AuthorizationCodeResourceDetails();
//    }

//    @Bean
//    @ConfigurationProperties("security.oauth2.resource")
//    public ResourceServerProperties oauth2Resource() {
//        return new ResourceServerProperties();
//    }

//    private Filter ssoFilter() {
//        OAuth2ClientAuthenticationProcessingFilter oaut2Filter = new OAuth2ClientAuthenticationProcessingFilter("/login");
//        OAuth2RestTemplate oauth2Template = new OAuth2RestTemplate(oauth2Client(), oauth2ClientContext());
//        oaut2Filter.setRestTemplate(oauth2Template);
//        UserInfoTokenServices tokenServices = new UserInfoTokenServices(
//                oauth2Resource().getUserInfoUri(),
//                oauth2Client().getClientId());
//        tokenServices.setRestTemplate(oauth2Template);
//        oaut2Filter.setTokenServices(tokenServices);
//        return oaut2Filter;
//    }

//    @Bean
//    public FilterRegistrationBean oauth2ClientFilterRegistration(
//            OAuth2ClientContextFilter filter, SecurityProperties security) {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(filter);
//        registration.setOrder(security.getFilterOrder() - 11);
//        return registration;
//    }
}
