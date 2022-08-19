package nl.xpensoft.xpensoft.controller;

import nl.xpensoft.xpensoft.model.Response;
import nl.xpensoft.xpensoft.model.Transaction;
import nl.xpensoft.xpensoft.service.TransactionService;
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
@RequestMapping("/transactions")
public class TransactionController {

    private final String PATH = "/transactions";
    private final TransactionService transactionService;
    private final Utils utils;

    @Autowired
    public TransactionController(TransactionService transactionService, Utils utils) {
        this.transactionService = transactionService;
        this.utils = utils;
    }

    @GetMapping
    public List<Transaction> getTransactions() {
        return transactionService.getByUser(utils.getCurrentUser());
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Transaction transaction, Errors errors) throws Exception {

        //Transaction can only be saved with POST request if there is no id included, such that existing transactions
        //cannot be changed.
        if (transaction.getId() != 0) {
            Response response = new Response(LocalDateTime.now(), 403, "Forbidden",
                    "Gebruik PUT in plaats van POST om een bestaande transactie te updaten", PATH,
                    null);

            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        //If there are validation errors, return appropriate response.
        if (errors.hasErrors()) {
            return new ResponseEntity<>(utils.getValidationErrors(PATH, errors), HttpStatus.BAD_REQUEST);
        }

        //Attempt to save new transaction
        transaction = transactionService.save(transaction);

        //If transactionService returns null, the user does not have the included header
        if (transaction == null) {
            Response response = new Response(LocalDateTime.now(), 404, "Not Found",
                    "Rubriek niet gevonden voor deze gebruiker", PATH,null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Transaction transaction, Errors errors) throws Exception {

        //If there are validation errors, return appropriate response.
        if (errors.hasErrors()) {
            return new ResponseEntity<>(utils.getValidationErrors(PATH, errors), HttpStatus.BAD_REQUEST);
        }

        //Attempt to update the transaction
        transaction = transactionService.update(transaction);

        //If transactionService returns null on update attempt, the user does not have the included header
        if (transaction == null) {
            Response response = new Response(LocalDateTime.now(), 404, "Not Found",
                    "Rubriek niet gevonden voor deze gebruiker", PATH,null);

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        //If the returned transaction has id 0, the transaction does not exist
        if (transaction.getId() == 0) {
            Response response = new Response(LocalDateTime.now(), 404, "Not Found",
                    "Transactie niet gevonden voor deze gebruiker", PATH,null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(transaction, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id) {

        //Attempt to delete the transaction
        int deleted = transactionService.deleteById(id);

        //If transactionService returns 0, the transaction does not exist
        if (deleted == 0) {
            Response response = new Response(LocalDateTime.now(), 404, "Not Found",
                    "Transactie niet gevonden voor deze gebruiker", PATH,null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
