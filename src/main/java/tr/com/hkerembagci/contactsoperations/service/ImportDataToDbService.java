package tr.com.hkerembagci.contactsoperations.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.hkerembagci.contactsoperations.entity.Contact;

import org.springframework.core.io.Resource;
import tr.com.hkerembagci.contactsoperations.entity.ContactPhone;
import tr.com.hkerembagci.contactsoperations.exception.ContactOperationsException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Data
@RequiredArgsConstructor
public class ImportDataToDbService {

    private final ContactService contactService;
    private final ContactPhoneService contactPhoneService;

    @Value("classpath:importData.json")
    private Resource resourceFile;
    private List<Contact> dataList = new ArrayList<>();

    public void fillJsonDataToList() throws IOException, ContactOperationsException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        List tempList = Arrays.asList(objectMapper.readValue(resourceFile.getFile(), Object[].class));
        tempList.forEach(temp -> {
            LinkedHashMap<String, Object> tempMap = (LinkedHashMap<String, Object>) temp;
            Contact contact = createNewContact(tempMap.get("name").toString(), tempMap.get("lastName").toString());
            if (tempMap.get("phones") instanceof String) {
                contact.getPhoneList().add(createNewContactPhone(tempMap.get("phones").toString(), contact.getId()));
            } else if (tempMap.get("phones") instanceof List) {
                for (String s : (List<String>) tempMap.get("phones")) {
                    contact.getPhoneList().add(createNewContactPhone(s, contact.getId()));
                }
            }
            dataList.add(contact);
        });
        importDataToDb();
    }

    protected void importDataToDb() throws ContactOperationsException {
        for (Contact contact : dataList) {
            List<String> phoneStringList = new ArrayList<>();

            Contact oldContact = findOldContact(contact);
            if (null == oldContact) {
                contactService.save(contact);
            } else {
                oldContact.setPhoneList(contactPhoneService.findByContactId(oldContact.getId()));
                phoneStringList.addAll(addPhonesToSet(oldContact));
            }
            phoneStringList.addAll(addPhonesToSet(contact));
            Set<String> phoneSet = new HashSet<>(phoneStringList);

            if (null != oldContact) {
                contact = oldContact;
            }

            deleteContactPhones(contact.getId());
            for (String phone : phoneSet) {
                contactPhoneService.save(createNewContactPhone(phone, contact.getId()));
            }
        }
    }

    Contact findOldContact(Contact contact) {
        return contactService.findByNameAndLastName(contact.getName(), contact.getLastName());
    }

    List<String> addPhonesToSet(Contact contact) {
        return contact.getPhoneList().stream()
                .map(ContactPhone::getPhoneNumber).collect(Collectors.toList());
    }

    void deleteContactPhones(Long contactId) {
        contactPhoneService.deleteByContactId(contactId);
    }

    Contact createNewContact(String name, String lastName) {
        Contact contact = new Contact();
        contact.setName(name);
        contact.setLastName(lastName);
        return contact;
    }

    ContactPhone createNewContactPhone(String phone, Long contactId) {
        ContactPhone contactPhone = new ContactPhone();
        contactPhone.setContactId(contactId);
        contactPhone.setPhoneNumber(phone);
        return contactPhone;
    }
}
