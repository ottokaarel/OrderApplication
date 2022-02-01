package conf;

import conf.security.handlers.ApiAccessDeniedHandler;
import conf.security.handlers.ApiEntryPoint;
import conf.security.jwt.JwtAuthenticationFilter;
import conf.security.jwt.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@PropertySource("classpath:/application.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${jwt.signing.key}")
    private String jwtKey;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http.formLogin();
        http.csrf().disable();


        // Ligipääsu puudumisel ei suunata ümber sisselogimise vormile
        http.exceptionHandling().authenticationEntryPoint(new ApiEntryPoint());
        http.exceptionHandling().accessDeniedHandler(new ApiAccessDeniedHandler());


        // Erandid
        http.authorizeRequests().antMatchers("/api/users").hasRole("ADMIN");
        http.authorizeRequests().antMatchers("/api/version").permitAll();
        http.authorizeRequests().antMatchers("/api/login").permitAll();
        http.authorizeRequests().antMatchers("/api/**").authenticated();


        // Filter, mis reageerib login urlile ja otsib sisselogimiseks vajaliku info päringust välja
        // Kui klient küsib kaitstud aadressilt infot, peab tal selleks õigus olema (sisselogitud olema).
        // Lisaks genereerib see filter token-i ja paneb selle Http vastuse päisesse.
        // See filter vajab ka võtit krüpteerimiseks. Võti on application.properties failis ja see on Spring-i konkfiguratsiooni süstitud.
        var apiLoginFilter = new JwtAuthenticationFilter(authenticationManager(), "/api/login", jwtKey);
        http.addFilterAfter(apiLoginFilter, LogoutFilter.class);


        // Filter, mis otsib Http päisest token-it ja kui leiab selle, autoriseerib päringu.
        var jwtAuthFilter = new JwtAuthorizationFilter(authenticationManager(), jwtKey);
        http.addFilterBefore(jwtAuthFilter, LogoutFilter.class);


        // Sessiooni kasutamise võib välja lülitada, kuna ligipääsu kontroll on lahendatud ilma selleta.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Väljalogimine
//        http.logout().logoutUrl("/api/logout");
//        http.logout().logoutSuccessHandler(new ApiLogoutSuccessHandler());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {

        builder.inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("user")
                .password("$2a$10$K0r0Gu72RqiTsG2THFyRi.CMSuXzZdg9CM3K7.Hj.ayk22LNnqYTy")
                .roles("USER")
                .and()
                .withUser("admin")
                .password("$2a$10$zU8EPnke0PqgQjCRjos81.9MgULj/1BnQRc3n0iMpnPy21t73GZ2e")
                .roles("USER", "ADMIN");
    }

}