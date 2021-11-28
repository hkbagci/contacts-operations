package tr.com.hkerembagci.contactsoperations;

import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tr.com.hkerembagci.contactsoperations.service.InitializeJsonFileService;

@SpringBootApplication
@Data
public class ContactsOperationsApplication implements CommandLineRunner {

    private final InitializeJsonFileService importDataToDbService;

    public static void main(String[] args) {
        SpringApplication.run(ContactsOperationsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        importDataToDbService.fillJsonDataToList();
    }
}
