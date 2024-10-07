package tingeso.prestabanco.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tingeso.prestabanco.security.ClientAuthenticationProvider;
import tingeso.prestabanco.security.ClientJwtAuthenticationFilter;
//import tingeso.prestabanco.security.ExecutiveAuthenticationProvider;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private ClientAuthenticationProvider clientAuthenticationProvider;

    @Autowired
    private ClientJwtAuthenticationFilter clientJwtAuthenticationFilter;


    @Bean
    public AuthenticationManager clientAuthenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(clientAuthenticationProvider);
        return authenticationManagerBuilder.build();
    }


    @Bean
    public SecurityFilterChain clientSecurityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("hola1");
        http.cors(cors -> cors.disable());
        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/clients/register").permitAll());
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/clients/login").permitAll());
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());


        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }




//    @Bean
//    public SecurityFilterChain executiveSecurityFilterChain(HttpSecurity http) throws Exception {
//        http.authenticationProvider(executiveAuthenticationProvider);
//        http.authorizeHttpRequests(auth -> auth.requestMatchers("/executives/register").authenticated());
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain clientJwtSecurityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.disable());
        http.csrf(csrf -> csrf.disable());
//        http.authenticationProvider(clientAuthenticationProvider);
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/clients/me").authenticated());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(clientJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
