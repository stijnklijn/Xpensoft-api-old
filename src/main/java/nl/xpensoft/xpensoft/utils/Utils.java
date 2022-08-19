package nl.xpensoft.xpensoft.utils;

import nl.xpensoft.xpensoft.model.Response;
import nl.xpensoft.xpensoft.model.SecurityUser;
import nl.xpensoft.xpensoft.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class Utils {

    public User getCurrentUser() {
        //Return the currently authenticated user
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = new User();
        user.setId(securityUser.getId());
        user.setEmail(securityUser.getUsername());
        return user;
    }

    public Response getValidationErrors(String path, Errors errors) {
        //Return response object including a list with all validation errors
        List<String> validationErrors = new ArrayList<>();
        for (ObjectError error : errors.getAllErrors()) {
            validationErrors.add(error.getDefaultMessage());
        }
        return new Response(LocalDateTime.now(), 400, "Bad Request", "Corrigeer de genoemde velden", path, validationErrors);
    }
}
