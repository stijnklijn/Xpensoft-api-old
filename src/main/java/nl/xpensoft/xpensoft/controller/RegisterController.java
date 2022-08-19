package nl.xpensoft.xpensoft.controller;

import nl.xpensoft.xpensoft.model.Response;
import nl.xpensoft.xpensoft.model.User;
import nl.xpensoft.xpensoft.service.UserService;
import nl.xpensoft.xpensoft.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/register")
public class RegisterController {

    private final String PATH = "/register";
    private final UserService userService;
    private final Utils utils;

    @Autowired
    public RegisterController(UserService userService, Utils utils) {
        this.userService = userService;
        this.utils = utils;
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody User user, Errors errors) throws Exception {

        //If there are validation errors, return appropriate response.
        if (errors.hasErrors()) {
            return new ResponseEntity<>(utils.getValidationErrors(PATH, errors), HttpStatus.BAD_REQUEST);
        }

        //Attempt to save new user
        user = userService.save(user);

        //If userService returns null on save attempt, a user with this email already exists
        if (user == null) {
            Response response = new Response(LocalDateTime.now(), 409, "Conflict",
                    "Er bestaat al een gebruiker met dit e-mailadres", PATH, null);

            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
