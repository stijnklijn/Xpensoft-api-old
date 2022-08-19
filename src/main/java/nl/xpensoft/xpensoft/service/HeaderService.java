package nl.xpensoft.xpensoft.service;

import nl.xpensoft.xpensoft.model.Header;
import nl.xpensoft.xpensoft.model.User;
import nl.xpensoft.xpensoft.repository.HeaderRepository;
import nl.xpensoft.xpensoft.repository.TransactionRepository;
import nl.xpensoft.xpensoft.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeaderService {

    private final HeaderRepository headerRepository;
    private final TransactionRepository transactionRepository;
    private final Utils utils;

    @Autowired
    public HeaderService(HeaderRepository headerRepository, TransactionRepository transactionRepository, Utils utils) {
        this.headerRepository = headerRepository;
        this.transactionRepository = transactionRepository;
        this.utils = utils;
    }

    public List<Header> getByUser(User user) {
        return headerRepository.getByUser(user);
    }

    public Header save(Header header) {
        //Check if a header with this name already exists for this user. If so, return null. Otherwise, save the header.
        List<Header> existingHeaders = getByUser(utils.getCurrentUser());
        for (Header existingHeader : existingHeaders) {
            if (header.getId() != existingHeader.getId() && header.getName().equalsIgnoreCase(existingHeader.getName())) {
                return null;
            }
        }
        return headerRepository.save(header);
    }

    public Header update(Header header) {
        //If the header id is known for this user, update the header. Otherwise, return a new header with id 0.
        List<Header> existingHeaders = getByUser(utils.getCurrentUser());
        for (Header existingHeader : existingHeaders) {
            if (header.getId() == existingHeader.getId()) {
                return save(header);
            }
        }
        return new Header();
    }

    public int deleteById(int id) {
        //Check if the header id is known for this user. If not, return 0.
        //Next, ensure that the header does not contain any transactions. If so, return -1. Otherwise, delete the header.
        List<Header> existingHeaders = getByUser(utils.getCurrentUser());
        for (Header existingHeader : existingHeaders) {
            if (id == existingHeader.getId()) {
                if (transactionRepository.getByHeader(existingHeader).isEmpty()) {
                    headerRepository.deleteById(id);
                    return id;
                }
                return -1;
            }
        }
        return 0;
    }
}
