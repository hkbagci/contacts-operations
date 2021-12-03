package tr.com.hkerembagci.contactsoperations.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tr.com.hkerembagci.contactsoperations.entity.Contact;

import org.springframework.core.io.Resource;
import tr.com.hkerembagci.contactsoperations.exception.ContactOperationsException;

import java.io.IOException;
import java.util.*;

@Service
@Data
@RequiredArgsConstructor
@Log4j2
public class InitializeJsonFileService {

    private final ContactService contactService;

    @Value("classpath:importData.json")
    private Resource resourceFile;

    public void fillJsonDataToList() throws IOException, ContactOperationsException {
        List<Contact> dataList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        List<Object> tempList = Arrays.asList(objectMapper.readValue(resourceFile.getFile(), Object[].class));
        tempList.forEach(temp -> {
            LinkedHashMap<String, Object> tempMap = (LinkedHashMap<String, Object>) temp;
            Contact contact = Contact.builder()
                    .name(tempMap.get("name").toString())
                    .lastName(tempMap.get("lastName").toString())
                    .phoneList(new ArrayList<>())
                    .build();
            try {
                contactService.fillContactPhoneList(contact, tempMap.get("phones"));
            } catch (ContactOperationsException e) {
                log.error(e.getMessage());
            }
            dataList.add(contact);
        });
        contactService.importDataToDb(dataList);
    }
}
