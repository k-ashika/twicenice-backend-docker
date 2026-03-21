package com.twicenice.twicenice_backend.config;

import com.twicenice.twicenice_backend.jwt.JwtAuthFilter;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
   
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
        		.requestMatchers("/images/**")
        		 .requestMatchers("/api/products/images/**")
        		.requestMatchers("/uploads/**");
        
    }

    
    @Bean
    public HttpFirewall allowDoubleSlashFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedDoubleSlash(true);
        firewall.setAllowSemicolon(true);
        return firewall;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthFilter jwtAuthFilter,
                                                   AuthenticationProvider authenticationProvider) throws Exception {
        http
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of(
    "http://localhost:4200",
    "https://twicenice.netlify.app"
));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(List.of("*"));
                config.setAllowCredentials(true);
                config.setExposedHeaders(List.of("Authorization")); 
                System.out.println("CORS Configuration applied");
                return config;
            }))
            .csrf(csrf -> csrf.disable())
           .authorizeHttpRequests(auth -> auth
    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
    .requestMatchers("/api/auth/**").permitAll()
    .requestMatchers("/api/products/**").permitAll()
    .requestMatchers("/api").permitAll()
    .requestMatchers("/images/**").permitAll()
    .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
    .requestMatchers("/api/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
    .requestMatchers("/api/reviews/admin/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
    .requestMatchers("/api/reviews/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
    .requestMatchers("/api/wishlist/**").authenticated()
    .requestMatchers(HttpMethod.DELETE, "/api/user/orders/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
    .requestMatchers("/api/user/returns/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
    .requestMatchers("/api/admin/returns/**").hasAuthority("ROLE_ADMIN")
    .requestMatchers("/api/user/orders/details/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
    .requestMatchers(HttpMethod.PUT, "/api/admin/returns/**").hasAuthority("ROLE_ADMIN")
    .requestMatchers(HttpMethod.GET, "/api/admin/returns").hasAuthority("ROLE_ADMIN")
    .anyRequest().authenticated()

)
            
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
