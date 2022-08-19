package nl.xpensoft.xpensoft.config;

import nl.xpensoft.xpensoft.model.SecurityUser;
import nl.xpensoft.xpensoft.model.User;
import nl.xpensoft.xpensoft.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class XpensoftUserDetails implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public XpensoftUserDetails(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Geen gegevens gevonden voor " + username);
        }
        return new SecurityUser(user);
    }
}
