package tr.com.hkerembagci.contactsoperations.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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
public class InitializeJsonFileService {

    private final ContactService contactService;

    @Value("classpath:importData.json")
    private Resource resourceFile;

    // Resource dizininin altındaki importData.json dosyasındaki bilgileri veri tabanına kaydeder.
    public void fillJsonDataToList() throws IOException, ContactOperationsException {
        List<Contact> dataList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        // Dosyadaki json stringini liste haline dönüştürüyoruz.
        List tempList = Arrays.asList(objectMapper.readValue(resourceFile.getFile(), Object[].class));
        tempList.forEach(temp -> {
            LinkedHashMap<String, Object> tempMap = (LinkedHashMap<String, Object>) temp;
            Contact contact = Contact.builder()
                    .name(tempMap.get("name").toString())
                    .lastName(tempMap.get("lastName").toString())
                    .phoneList(new ArrayList<>())
                    .build();
            // "phones": "+90 505 505 50 50" String tipindeyken
            // "phones": ["+90 505 505 50 50", "+90 555 555 55 55"] List tipindedir.
            // Hatayı önlemek adına aşağıdaki kontrol yapılmaktadır.
            if (tempMap.get("phones") instanceof String) {
                contact.getPhoneList().add(tempMap.get("phones").toString());
            } else if (tempMap.get("phones") instanceof List) {
                for (String phoneNumber : (List<String>) tempMap.get("phones")) {
                    contact.getPhoneList().add(phoneNumber);
                }
            }
            dataList.add(contact);
        });
        contactService.importDataToDb(dataList);
    }
}
