package tr.com.hkerembagci.contactsoperations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tr.com.hkerembagci.contactsoperations.entity.ContactPhone;

import java.util.List;

@Repository
public interface ContactPhoneRepository extends JpaRepository<ContactPhone, Long> {
    List<ContactPhone> findByContactId(Long contactId);
    @Transactional
    void deleteByContactId(Long contactId);
}
