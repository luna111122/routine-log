package routine.log.config;

import routine.log.config.jwt.JwtBlacklist;
import routine.log.config.jwt.JwtTokenProvider;
import routine.log.service.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import org.springframework.http.HttpHeaders;

@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwt;
    private final JwtBlacklist blacklist;
    private final UserDetailService userDetailService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest req,
            @NonNull HttpServletResponse res,
            @NonNull FilterChain chain
            ) throws ServletException, IOException{
        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if(header != null  && header.startsWith("Bearer ")){
            String token = header.substring(7);

            if(!blacklist.contains(token) && jwt.validate(token)
            && SecurityContextHolder.getContext().getAuthentication() == null){
                String username = jwt.getUsername(token);
                UserDetails user = userDetailService.loadUserByUsername(username);

                var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(req, res);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        String p = req.getServletPath();

        return "OPTIONS".equalsIgnoreCase(req.getMethod())
                || p.startsWith("/auth/");
    }




}
