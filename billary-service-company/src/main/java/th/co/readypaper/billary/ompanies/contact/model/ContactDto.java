package th.co.readypaper.billary.ompanies.contact.model;

import lombok.Data;

@Data
public class ContactDto {
    private String id;
    private String name;
    private String address;
    private String zipCode;
    private String taxId;
    private String office;
    private String email;
    private String person;
    private String phone;
    private int typeId;
}
