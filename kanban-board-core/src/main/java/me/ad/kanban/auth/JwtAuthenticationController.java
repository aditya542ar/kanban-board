package me.ad.kanban.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import me.ad.kanban.auth.jwt.JwtTokenUtil;
import me.ad.kanban.auth.jwt.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
public class JwtAuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
                                       JwtUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping(path = "/authenticate", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUserName(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUserName());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponse(userDetails.getUsername(), token));
    }

    @PostMapping(path = "/revalidate")
    public ResponseEntity<?> revalidateToken(@RequestBody String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            String username = null;
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(username, jwtToken));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            } catch (MalformedJwtException e) {
                System.out.println("Malformed JWT Token");
                return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            System.out.println("JWT Token does not begin with Bearer String");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            System.out.println("password:" + password);
            String decodedPassword = new String(Base64.getDecoder().decode(password));
            System.out.println("decoded password:" + decodedPassword);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, decodedPassword));
        } catch (DisabledException e) {
            throw new IllegalArgumentException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("INVALID_CREDENTIALS", e);
        }
    }
}
