package nl.xpensoft.xpensoft.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import nl.xpensoft.xpensoft.constants.SecurityConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

    private static final long DURATION = 3600 * 1000;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Long expires = System.currentTimeMillis() + DURATION;
            SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
            String jwt = Jwts.builder().setIssuer("Xpensoft").setSubject("JWT")
                    .claim("email", authentication.getName())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + DURATION))
                    .signWith(key).compact();
            response.setHeader("Authorization", jwt);
            response.setHeader("Expires", expires.toString());
        }
        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/api/login");
    }
}
