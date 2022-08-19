package nl.xpensoft.xpensoft.controller;

import nl.xpensoft.xpensoft.model.Header;
import nl.xpensoft.xpensoft.model.Response;
import nl.xpensoft.xpensoft.model.User;
import nl.xpensoft.xpensoft.service.HeaderService;
import nl.xpensoft.xpensoft.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/headers")
public class HeaderController {

    private final String PATH = "/headers";
    private final HeaderService headerService;
    private final Utils utils;

    @Autowired
    public HeaderController(HeaderService headerService, Utils utils) {
        this.headerService = headerService;
        this.utils = utils;
    }

    @GetMapping
    public List<Header> getHeaders() {
        return headerService.getByUser(utils.getCurrentUser());
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Header header, Errors errors) throws Exception {

        //Header can only be saved with POST request if there is no id included, such that existing headers
        //cannot be changed.
        if (header.getId() != 0) {
            Response response = new Response(LocalDateTime.now(), 403, "Forbidden",
                    "Gebruik PUT in plaats van POST om een bestaande rubriek te updaten", PATH,
                    null);
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        //If there are validation errors, return appropriate response.
        if (errors.hasErrors()) {
            return new ResponseEntity<>(utils.getValidationErrors(PATH, errors), HttpStatus.BAD_REQUEST);
        }

        //Set current user as foreign key for new header and attempt to save new header
        User user = utils.getCurrentUser();
        header.setUser(user);
        header = headerService.save(header);

        //If headerService returns null on save attempt, a header with this name already exists.
        if (header == null) {
            Response response = new Response(LocalDateTime.now(), 409, "Conflict",
                    "Er bestaat al een rubriek met deze naam", PATH,null);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(header, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Header header, Errors errors) throws Exception {

        //If there are validation errors, return appropriate response.
        if (errors.hasErrors()) {
            return new ResponseEntity<>(utils.getValidationErrors(PATH, errors), HttpStatus.BAD_REQUEST);
        }

        //Set current user as foreign key for updated header and attempt to update the header
        User user = utils.getCurrentUser();
        header.setUser(user);
        header = headerService.update(header);

        //If headerService returns null on update attempt, a header with this name already exists.
        if (header == null) {
            Response response = new Response(LocalDateTime.now(), 409, "Conflict",
                    "Er bestaat al een rubriek met deze naam", PATH, null);

            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        //If the returned header has id 0, the header does not exist.
        if (header.getId() == 0) {
            Response response = new Response(LocalDateTime.now(), 404, "Not Found",
                    "Rubriek niet gevonden voor deze gebruiker", PATH, null);

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(header, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id) {

        //Attempt to delete the header
        int deleted = headerService.deleteById(id);

        //If headerService returns 0, the header does not exist.
        if (deleted == 0) {
            Response response = new Response(LocalDateTime.now(), 404, "Not Found",
                    "Rubriek niet gevonden voor deze gebruiker", PATH, null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        //If headerService returns -1, the header still holds transactions.
        if (deleted == -1) {
            Response response = new Response(LocalDateTime.now(), 409, "Conflict",
                    "Rubriek bevat nog transacties", PATH, null);

            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
