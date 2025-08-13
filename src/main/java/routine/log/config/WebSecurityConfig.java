package routine.log.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import routine.log.config.jwt.JwtBlacklist;
import routine.log.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Slf4j
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {


    private final JwtTokenProvider jwtTokenProvider;
    private final JwtBlacklist jwtBlacklist;

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "http://127.0.0.1:*",
                "http://165.246.207.8:3000",
                "https://*.trycloudflare.com"
        ));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        cfg.setAllowedHeaders(List.of("*"));

        cfg.setExposedHeaders(List.of("Authorization"));

        cfg.setAllowCredentials(false);

        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);

        return source;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/static/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   routine.log.service.UserDetailService userService )
        throws Exception{
        return http
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .headers(h -> h.frameOptions(f -> f.sameOrigin()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/auth/signup", "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/message").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, jwtBlacklist, userService),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }


}
