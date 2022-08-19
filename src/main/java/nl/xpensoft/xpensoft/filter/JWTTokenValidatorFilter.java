package nl.xpensoft.xpensoft.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import nl.xpensoft.xpensoft.config.XpensoftUserDetails;
import nl.xpensoft.xpensoft.constants.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JWTTokenValidatorFilter extends OncePerRequestFilter {

    private final XpensoftUserDetails xpensoftUserDetails;

    @Autowired
    public JWTTokenValidatorFilter(XpensoftUserDetails xpensoftUserDetails) {
        this.xpensoftUserDetails = xpensoftUserDetails;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String jwt = request.getHeader(SecurityConstants.JWT_HEADER);
        if (jwt != null && jwt.split(" ").length == 2
                && jwt.split(" ")[0].equals("Bearer") && jwt.split(" ")[1] != null) {
            try {
                SecretKey key = Keys.hmacShaKeyFor(
                        SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt.split(" ")[1])
                        .getBody();
                String email = String.valueOf(claims.get("email"));
                UserDetails user = xpensoftUserDetails.loadUserByUsername(email);
                Authentication auth = new UsernamePasswordAuthenticationToken(user,null, null);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            catch (Exception e) {
                throw new BadCredentialsException("Ongeldig token");
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/login");
    }
}
