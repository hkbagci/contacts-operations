package tr.com.hkerembagci.contactsoperations.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import tr.com.hkerembagci.contactsoperations.entity.Contact;
import tr.com.hkerembagci.contactsoperations.exception.ContactOperationsException;
import tr.com.hkerembagci.contactsoperations.repository.ContactRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class ContactService {

    private final ContactRepository contactRepository;

    public List<Contact> findByName(String contactName) {
        return contactRepository.findByName(contactName);
    }

    public Contact findByNameAndLastName(String name, String lastName) {
        return contactRepository.findByNameAndLastName(name, lastName);
    }

    public Contact save(Contact contact) throws ContactOperationsException {
        return contactRepository.save(contact);
    }

}