package tr.com.hkerembagci.contactsoperations.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import tr.com.hkerembagci.contactsoperations.dto.ContactRequestAndResponseDto;
import tr.com.hkerembagci.contactsoperations.entity.Contact;
import tr.com.hkerembagci.contactsoperations.entity.ContactPhone;
import tr.com.hkerembagci.contactsoperations.exception.ContactOperationsException;
import tr.com.hkerembagci.contactsoperations.repository.ContactRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class ContactService {

    private final ContactRepository contactRepository;
    private final ContactPhoneService contactPhoneService;

    // İsme göre model nesnesini döndürür.
    public List<ContactRequestAndResponseDto> returnDtoByName(String contactName) {
        List<Contact> contactList = contactRepository.findByName(contactName);
        contactList.forEach(contact -> {
            List<ContactPhone> tempContactPhoneList = contactPhoneService.findByContactId(contact.getId());
            tempContactPhoneList.forEach(tempContactPhone -> contact.getPhoneList().add(tempContactPhone.getPhoneNumber()));
        });
        return convertEntityListToDtoList(contactList);
    }

    protected Contact findByNameAndLastName(String name, String lastName) {
        return contactRepository.findByNameAndLastName(name, lastName);
    }

    protected void save(Contact contact) {
        contactRepository.save(contact);
    }

    public void saveAll(List<Contact> contactList) throws ContactOperationsException {
        importDataToDb(contactList);
    }

    // Model listesini entity listesine çevirir.
    public List<Contact> convertDtoListToEntityList(List<ContactRequestAndResponseDto> dtoList) {
        List<Contact> contactList = new ArrayList<>();
        dtoList.forEach(dto -> contactList.add(convertDtoToEntity(dto)));
        return contactList;
    }

    // Modelden entity'ye çevirir.
    private Contact convertDtoToEntity(ContactRequestAndResponseDto dto) {
        return Contact
                .builder()
                .name(dto.getName())
                .lastName(dto.getLastName())
                .phoneList(dto.getPhones())
                .build();
    }

    // Entity listesini model listesine çevirir.
    protected List<ContactRequestAndResponseDto> convertEntityListToDtoList(List<Contact> contactList) {
        List<ContactRequestAndResponseDto> dtoList = new ArrayList<>();
        contactList.forEach(entity -> dtoList.add(convertEntityToDto(entity)));
        return dtoList;
    }

    // Entity'den modele çevirir.
    private ContactRequestAndResponseDto convertEntityToDto(Contact entity) {
        return ContactRequestAndResponseDto
                .builder()
                .name(entity.getName())
                .lastName(entity.getLastName())
                .phones(entity.getPhoneList())
                .build();
    }

    // Daha önce kaydedilmiş kişiyi bulur.
    private Contact findExistingContact(Contact contact) {
        return findByNameAndLastName(contact.getName(), contact.getLastName());
    }

    // Kişi nesnesine telefon numarası bilgisini doldurur.
    private void fillContactPhoneNumberList(Contact contact) {
        contact.setPhoneList(contactPhoneService.findByContactId(contact.getId()).stream()
                .map(ContactPhone::getPhoneNumber)
                .collect(Collectors.toList()));
    }

    // Kişi listesini veri tabanına kaydeder.
    protected void importDataToDb(List<Contact> dataList) throws ContactOperationsException {
        for (Contact contact : dataList) {
            Contact existingContact = findExistingContact(contact);
            if (null != existingContact) {
                // Bulunan eski kaydın telefon numaralarını doldurur.
                fillContactPhoneNumberList(existingContact);
                contactPhoneService.deleteByContactId(existingContact.getId());
            }
            Set<String> phoneSet = existingContactPhoneOperations(contact, existingContact);
            for (String phone : phoneSet) {
                if (null != contact.getId()) {
                    contactPhoneService.save(ContactPhone.builder()
                            .contactId(contact.getId())
                            .phoneNumber(phone)
                            .build());
                } else if (null != existingContact.getId()) {
                    contactPhoneService.save(ContactPhone.builder()
                            .contactId(existingContact.getId())
                            .phoneNumber(phone)
                            .build());
                }
            }
        }
    }

    // Daha önce veri tabanında kayıtlı kişilerin eski telefon numaralarıyla yeni telefon numaralarını birleştirerek kaydeder.
    private Set<String> existingContactPhoneOperations(Contact contact, Contact existingContact) {
        List<String> phoneStringList = new ArrayList<>();
        if (null == existingContact) {
            save(contact);
        } else {
            phoneStringList.addAll(existingContact.getPhoneList());
        }
        phoneStringList.addAll(contact.getPhoneList());
        if (null != existingContact) {
            contact = existingContact;
        }
        return new HashSet<>(phoneStringList);
    }

}