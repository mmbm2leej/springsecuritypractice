package com.example.demo.security;

import com.example.demo.auth.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.concurrent.TimeUnit;

import static com.example.demo.security.ApplicationUserPermission.*;
import static com.example.demo.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passEncoder;
    private final ApplicationUserService appUserService;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passEncoder, ApplicationUserService appUserService) {
        this.passEncoder = passEncoder;
        this.appUserService = appUserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //this disables cross site request forgery security
                //withHttpOnlyFalse makes it so clients cant read cookies
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()

                //use this whenever u dont want to keep supplying csrf tokens
                .csrf().disable()

                //authorize requests
                //any requests must be authenticated
                //and the method this is done with is basic authentication
                .authorizeRequests()

                //whitelist the root url, index page, all css and js urls
                .antMatchers("/", "index", "/css/*", "/js/*")
                .permitAll()

                //more specific
                .antMatchers("/api/**")
                .hasRole(STUDENT.name())

                //permission based security with ant matchers
//                .antMatchers(HttpMethod.DELETE,"management/api/**")
//                .hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.POST,"management/api/**")
//                .hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.PUT,"management/api/**")
//                .hasAuthority(COURSE_WRITE.getPermission())
//
//                .antMatchers(HttpMethod.GET,"management/api/**")
//                .hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())

                .anyRequest()
                .authenticated()
                .and()

                //for form based authentication
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/courses", true)
                .passwordParameter("password")
                .usernameParameter("username")

                .and()//.rememberMe(); //default is 2 weeks
                .rememberMe()
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                .key("somethingSecured")
                .rememberMeParameter("remember-me")

                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .logoutSuccessUrl("/login");


                /*
                LOGOUTS can be GET requests if CSRF protection is disabled
                otherwise it has to be POST request
                 */

        //.tokenRepository() //if you're storing tokens in a DB

        //for basic auth
        //.httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passEncoder);
        provider.setUserDetailsService(appUserService);
        return provider;
    }

   // for in-memory user store, commented because its getting fetched from repo now
    /*
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {

    was in memory in the beginning, now fetching from repo

        UserDetails miraJonesUser = User.builder()
                .username("mirajones")
                .password(passEncoder.encode("notthebees"))
                //.roles("STUDENT") // will be saved as ROLE_STUDENT
                //.roles(STUDENT.name())
                .authorities(STUDENT.getGrantedAuthorities())

                //return IMUDM wont work without this
                .build();

        UserDetails lindaAdminUser = User.builder()
                .username("linda")
                .password(passEncoder.encode("password123"))
                //.roles("ADMIN")
                //.roles(ADMIN.name())
                .authorities(ADMIN.getGrantedAuthorities())

                .build();

        UserDetails tomTraineeAdminUser = User.builder()
                .username("tomtrainee")
                .password(passEncoder.encode("thisisapass"))
                //.roles("ADMIN")
                //.roles(ADMINTRAINEE.name())
                .authorities(ADMINTRAINEE.getGrantedAuthorities())

                .build();

        return new InMemoryUserDetailsManager(
                miraJonesUser,
                lindaAdminUser,
                tomTraineeAdminUser
        );

    }
*/


}

