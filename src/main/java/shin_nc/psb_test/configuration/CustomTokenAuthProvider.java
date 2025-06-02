package shin_nc.psb_test.configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

public class CustomTokenAuthProvider implements AuthenticationProvider{

    private final UserDetailsService userDetailsService;

    public CustomTokenAuthProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Tidak digunakan karena kita autentikasi via filter token
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
