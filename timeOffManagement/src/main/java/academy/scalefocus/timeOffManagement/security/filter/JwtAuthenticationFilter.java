package academy.scalefocus.timeOffManagement.security.filter;

import academy.scalefocus.timeOffManagement.security.UserPrincipal;
import academy.scalefocus.timeOffManagement.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";
    private static final String ROLE_ADMIN = "ADMIN";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION);

            if (authorizationHeaderIsInvalid(authorizationHeader)) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }

            UsernamePasswordAuthenticationToken token = createToken(authorizationHeader);
            SecurityContextHolder.getContext().setAuthentication(token);

        } catch (Exception e) {
            log.error("Cannot authenticate user.");
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private boolean authorizationHeaderIsInvalid(String authorizationHeader) {
        return authorizationHeader == null
                || !authorizationHeader.startsWith(BEARER);
    }

    private UsernamePasswordAuthenticationToken createToken(String authorizationHeader) {
        String token = authorizationHeader.replace(BEARER, "");
        UserPrincipal userPrincipal = tokenService.parseToken(token);

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (userPrincipal.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority(ROLE_ADMIN));
        }

        return new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);
    }
}
