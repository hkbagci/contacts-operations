package tr.com.hkerembagci.contactsoperations.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequestAndResponseDto {

    private String name;
    private String lastName;
    private Object phones;

}
