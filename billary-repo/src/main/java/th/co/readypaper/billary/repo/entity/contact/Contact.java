package th.co.readypaper.billary.repo.entity.contact;

import lombok.Data;
import th.co.readypaper.billary.repo.entity.AuditableCompanyEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
public class Contact extends AuditableCompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
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
