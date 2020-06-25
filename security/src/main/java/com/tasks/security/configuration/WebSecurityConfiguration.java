package com.tasks.security.configuration;

import com.tasks.security.filter.JwtAuthenticationTokenFilter;
import com.tasks.security.utils.JwtAuthenticationEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        log.info("configureAuthentication jardel ");
        authenticationManagerBuilder.userDetailsService(this.userDetailsService).passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticatonTokenFilterBean() {
        return new JwtAuthenticationTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /**
         * Configurando authentication e liberando apenas para urls /auth
         */
//        http.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/auth/**").permitAll()
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .anyRequest().authenticated();


                http.authorizeRequests()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/order/url-magica-maluca").permitAll()
                .anyRequest().authenticated();

//
//        http.addFilterBefore(authenticatonTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
//        http.headers().cacheControl();
//
//        //Configuration to use ssl in Heroku
//        http.requiresChannel().requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
//                .requiresSecure();

    }
}
