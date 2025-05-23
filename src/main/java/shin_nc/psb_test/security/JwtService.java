package shin_nc.psb_test.security;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JwtService {

    // @Value("${jwt.secret}")
    private String secret = "40de55f2312a93dca4aaab2384bf2d77ae35f3a84b015914ea2afc9599615b6a01c18fb0f0ae456faf5be1d75859c7b89949e749ace9f9ced228dbb9d08e3bafb840d7f035c1a345b04ba3d321844ecd4b4c70d743b8286185d8b4a1914950dab36fc08d35eb798f51bcc62bf3b7264a4e587f5048fe4d73baef074d2d813ba48365a9175751f2887b5cd7ed9771e666e3fb6294bde41956080e4c9e5ada5cd6f1d99acb6ad955a229aed5947e935e1bce99acd29b33349a6d436941e8c688040e383b9145c90de14befa4bc9f9d2cff16921670269520d7b6dca074392a6e6882883b6fabd526736f7f57786d12871278579976a1fbbb05f8777f380af76927";

    // @Value("${jwt.expiration}")
    private long jwtExpiration = 1000 * 60 * 60 * 24; // 24 hours

    private Key key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public Long tokenExpiredAt(){
        return System.currentTimeMillis() + jwtExpiration;
    }
}
