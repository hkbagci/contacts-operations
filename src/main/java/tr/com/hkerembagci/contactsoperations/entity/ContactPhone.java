package tr.com.hkerembagci.contactsoperations.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "contact_phone")
@Data
public class ContactPhone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

}
