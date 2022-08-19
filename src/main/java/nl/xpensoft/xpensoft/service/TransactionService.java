package nl.xpensoft.xpensoft.service;

import nl.xpensoft.xpensoft.model.Header;
import nl.xpensoft.xpensoft.model.Transaction;
import nl.xpensoft.xpensoft.model.User;
import nl.xpensoft.xpensoft.repository.HeaderRepository;
import nl.xpensoft.xpensoft.repository.TransactionRepository;
import nl.xpensoft.xpensoft.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final HeaderRepository headerRepository;
    private final TransactionRepository transactionRepository;
    private final Utils utils;

    @Autowired
    public TransactionService(HeaderRepository headerRepository, TransactionRepository transactionRepository, Utils utils) {
        this.headerRepository = headerRepository;
        this.transactionRepository = transactionRepository;
        this.utils = utils;
    }

    public List<Transaction> getByUser(User user) {
        return transactionRepository.getByUser(user);
    }

    public Transaction save(Transaction transaction) {
        //Check if the user has the included header. If so, save the transaction. Otherwise, return null.
        List<Header> existingHeaders = headerRepository.getByUser(utils.getCurrentUser());
        for (Header existingHeader : existingHeaders) {
            if (transaction.getHeader().getId() == existingHeader.getId()) {
                return transactionRepository.save(transaction);
            }
        }
        return null;
    }

    public Transaction update(Transaction transaction) {
        //If the transaction id is known for this user, update the transaction. Otherwise, return a new transaction with id 0.
        List<Transaction> existingTransactions = getByUser(utils.getCurrentUser());
        for (Transaction existingTransaction : existingTransactions) {
            if (transaction.getId() == existingTransaction.getId()) {
                return save(transaction);
            }
        }
        return new Transaction();
    }

    public int deleteById(int id) {
        //Check if the transaction id is known for this user. If so, delete the transaction. Otherwise, return 0.
        List<Transaction> existingTransactions = getByUser(utils.getCurrentUser());
        for (Transaction existingTransaction : existingTransactions) {
            if (id == existingTransaction.getId()) {
                transactionRepository.deleteById(id);
                return id;
            }
        }
        return 0;
    }
}
