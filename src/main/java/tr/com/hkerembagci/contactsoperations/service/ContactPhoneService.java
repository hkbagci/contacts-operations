package tr.com.hkerembagci.contactsoperations.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.hkerembagci.contactsoperations.entity.ContactPhone;
import tr.com.hkerembagci.contactsoperations.repository.ContactPhoneRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class ContactPhoneService {

    private final ContactPhoneRepository contactPhoneRepository;

    protected List<ContactPhone> findByContactId(Long contactId) {
        return contactPhoneRepository.findByContactId(contactId);
    }

    @Transactional
    public void deleteByContactId(Long contactId) {
        contactPhoneRepository.deleteByContactId(contactId);
    }

    protected ContactPhone save(ContactPhone contactPhone) {
        return contactPhoneRepository.save(contactPhone);
    }
}