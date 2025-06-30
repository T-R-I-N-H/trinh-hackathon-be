package trinh_be.modules.auth.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import trinh_be.modules.user.model.User;
import trinh_be.modules.user.service.UserService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = UserService.getInstance().getByEmail(email);
        return new CustomUserDetails(user);
    }
}
