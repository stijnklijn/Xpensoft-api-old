package nl.xpensoft.xpensoft.repository;

import nl.xpensoft.xpensoft.model.Header;
import nl.xpensoft.xpensoft.model.Transaction;
import nl.xpensoft.xpensoft.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT t " +
            "FROM Transaction t " +
            "JOIN Header h ON t.header = h " +
            "WHERE h.user = :user")
    List<Transaction> getByUser(User user);

    List<Transaction> getByHeader(Header header);
}
