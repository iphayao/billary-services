package th.co.readypaper.billary.repo.entity.contact;

import lombok.Data;
import th.co.readypaper.billary.repo.entity.AuditableCompanyEntity;

import javax.persistence.*;
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
    @ManyToOne
    @JoinColumn(name = "contact_type_id")
    private ContactType type;
    @ManyToOne
    @JoinColumn(name = "business_type_id")
    private BusinessType businessType;
}
