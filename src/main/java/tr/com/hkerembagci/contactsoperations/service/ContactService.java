package tr.com.hkerembagci.contactsoperations.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import tr.com.hkerembagci.contactsoperations.dto.ContactRequestAndResponseDto;
import tr.com.hkerembagci.contactsoperations.entity.Contact;
import tr.com.hkerembagci.contactsoperations.entity.ContactPhone;
import tr.com.hkerembagci.contactsoperations.exception.ContactOperationsException;
import tr.com.hkerembagci.contactsoperations.repository.ContactRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class ContactService {

    private final ContactRepository contactRepository;
    private final ContactPhoneService contactPhoneService;

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

    public List<Contact> convertDtoListToEntityList(List<ContactRequestAndResponseDto> dtoList) {
        List<Contact> contactList = new ArrayList<>();
        dtoList.forEach(dto -> {
            try {
                contactList.add(convertDtoToEntity(dto));
            } catch (ContactOperationsException e) {
                log.error(e.getMessage());
            }
        });
        return contactList;
    }

    private Contact convertDtoToEntity(ContactRequestAndResponseDto dto) throws ContactOperationsException {
        Contact contact = Contact
                .builder()
                .name(dto.getName())
                .lastName(dto.getLastName())
                .phoneList(Collections.singletonList(dto.getPhones().toString()))
                .build();
        fillContactPhoneList(contact, dto.getPhones());
        return contact;
    }

    protected List<ContactRequestAndResponseDto> convertEntityListToDtoList(List<Contact> contactList) {
        List<ContactRequestAndResponseDto> dtoList = new ArrayList<>();
        contactList.forEach(entity -> dtoList.add(convertEntityToDto(entity)));
        return dtoList;
    }

    private ContactRequestAndResponseDto convertEntityToDto(Contact entity) {
        return ContactRequestAndResponseDto
                .builder()
                .name(entity.getName())
                .lastName(entity.getLastName())
                .phones(entity.getPhoneList())
                .build();
    }

    private Contact findExistingContact(Contact contact) {
        return findByNameAndLastName(contact.getName(), contact.getLastName());
    }

    private void fillContactPhoneNumberList(Contact contact) {
        contact.setPhoneList(contactPhoneService.findByContactId(contact.getId()).stream()
                .map(ContactPhone::getPhoneNumber)
                .collect(Collectors.toList()));
    }

    protected void importDataToDb(List<Contact> dataList) throws ContactOperationsException {
        for (Contact contact : dataList) {
            Contact existingContact = findExistingContact(contact);
            if (null != existingContact) {
                fillContactPhoneNumberList(existingContact);
                contactPhoneService.deleteByContactId(existingContact.getId());
            }
            Set<String> phoneSet = existingContactPhoneOperations(contact, existingContact);
            for (String phone : phoneSet) {
                if (null != existingContact && null != existingContact.getId()) {
                    contactPhoneService.save(ContactPhone.builder()
                            .contactId(existingContact.getId())
                            .phoneNumber(phone)
                            .build());
                } else if (null != contact.getId()) {
                    contactPhoneService.save(ContactPhone.builder()
                            .contactId(contact.getId())
                            .phoneNumber(phone)
                            .build());
                }
            }
        }
    }

    private Set<String> existingContactPhoneOperations(Contact contact, Contact existingContact) throws ContactOperationsException {
        List<String> phoneStringList = new ArrayList<>();
        if (null == existingContact) {
            save(contact);
        } else {
            phoneStringList.addAll(existingContact.getPhoneList());
        }
        phoneStringList.addAll(contact.getPhoneList());
        return new HashSet<>(phoneStringList);
    }

    protected void fillContactPhoneList(Contact contact, Object phones) throws ContactOperationsException {
        if (phones instanceof String) {
            contact.setPhoneList(Collections.singletonList(phones.toString()));
        } else if (phones instanceof List) {
            contact.setPhoneList((List<String>) phones);
        }
    }

}