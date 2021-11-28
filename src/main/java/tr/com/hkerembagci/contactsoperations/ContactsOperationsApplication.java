package tr.com.hkerembagci.contactsoperations;

import lombok.Data;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tr.com.hkerembagci.contactsoperations.service.ImportDataToDbService;

@SpringBootApplication
@Data
public class ContactsOperationsApplication implements CommandLineRunner {

    private final ImportDataToDbService importDataToDbService;

    public static void main(String[] args) {
        SpringApplication.run(ContactsOperationsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        importDataToDbService.fillJsonDataToList();
    }
}
