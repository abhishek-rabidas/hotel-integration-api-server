package com.github.abhishek_rabidas.Hotel_Integration_API.middlewares;

import com.github.abhishek_rabidas.Hotel_Integration_API.auth.AuthCacheProvider;
import com.github.abhishek_rabidas.Hotel_Integration_API.auth.JwtAuthToken;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.core.CurrentUser;
import com.github.abhishek_rabidas.Hotel_Integration_API.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtFilter extends GenericFilterBean {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Value("${secured.api.path}")
    private String apiprefix;

    @Value("${jwt.secret.key}")
    private String secretkey;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthCacheProvider authCache;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretkey.getBytes());
    }

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (!request.getRequestURI().startsWith(apiprefix)) {
            chain.doFilter(req, res);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        final String token = authHeader.substring(7); // The part after "Bearer "

        try {
            Claims claims;
            try {
                claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            } catch (IllegalArgumentException | JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            request.setAttribute("claims", claims);

            Authentication result = authCache.getAuthCache().getIfPresent(token);

            if (result == null || ((JwtAuthToken) result).isStale()) {
                logger.debug("User {} not found in cache", claims.getSubject());

                CurrentUser user;

                if (claims.getSubject() != null) {
                    try {
                        user = (CurrentUser) userService.loadUserByUsername(claims.getSubject());
                    } catch (UsernameNotFoundException e) {
                        logger.debug(e.getMessage());
                        user = null;
                    }

                    if (user == null || user.getUser() == null) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }

                    Set<String> userPrivileges = user.getUser().getPrivileges();
                    List<GrantedAuthority> authorities = userPrivileges.stream().map(up -> new SimpleGrantedAuthority("ROLE_" + up)).collect(Collectors.toList());

                    result = new JwtAuthToken(
                            user,
                            claims.getSubject(),
                            authorities);
                    authCache.getAuthCache().put(token, result);
                }
            }

            SecurityContextHolder.getContext().setAuthentication(result);

        } catch (JwtException e) {
            logger.error("Error parsing token", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        chain.doFilter(req, res);
    }

}
