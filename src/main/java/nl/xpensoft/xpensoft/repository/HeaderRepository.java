package nl.xpensoft.xpensoft.repository;

import nl.xpensoft.xpensoft.model.Header;
import nl.xpensoft.xpensoft.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeaderRepository extends JpaRepository<Header, Integer> {

    List<Header> getByUser(User user);
}
