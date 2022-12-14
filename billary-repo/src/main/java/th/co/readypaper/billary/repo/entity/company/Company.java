package th.co.readypaper.billary.repo.entity.company;

import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.readypaper.billary.repo.entity.AuditableEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Company extends AuditableEntity<UUID> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String code;
    private String name;
    private String nameEn;
    private String address;
    private String addressEn;
    private String taxId;
    private String phone;
    private String website;
    private String office;
    private byte[] companyLogo;
    private byte[] companyStamp;
    private byte[] companyAuthorizedSign;
    private byte[] companyLineQr;
}
