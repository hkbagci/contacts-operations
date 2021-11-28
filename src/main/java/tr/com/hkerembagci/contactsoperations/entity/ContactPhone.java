package tr.com.hkerembagci.contactsoperations.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "contact_phone")
@Data
public class ContactPhone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JoinColumn(name = "contact_id", nullable = false)
    private Long contactId;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

}
