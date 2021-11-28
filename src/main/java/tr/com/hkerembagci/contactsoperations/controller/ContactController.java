package tr.com.hkerembagci.contactsoperations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.hkerembagci.contactsoperations.dto.ContactRequestAndResponseDto;
import tr.com.hkerembagci.contactsoperations.exception.ContactOperationsException;
import tr.com.hkerembagci.contactsoperations.service.ContactService;

import java.util.List;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping(consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> postContactList(
            @RequestBody List<ContactRequestAndResponseDto> dtoList) {
        try {
            contactService.saveAll(contactService.convertDtoListToEntityList(dtoList));
        } catch (ContactOperationsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Kişiler veri tabanına başarıyla kaydedildi.");
    }

    @GetMapping(produces = "application/json;charset=UTF-8")
    public List<ContactRequestAndResponseDto> getContactListByName(@RequestParam String name) {
        return contactService.returnDtoByName(name);
    }
}
