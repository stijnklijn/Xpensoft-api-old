package nl.xpensoft.xpensoft.controller;

import nl.xpensoft.xpensoft.model.User;
import nl.xpensoft.xpensoft.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class LoginController {

    private final UserRepository userRepository;

    @Autowired
    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping("/login")
    public User login(Principal principal) {
        return userRepository.getByEmail(principal.getName());
    }
}
