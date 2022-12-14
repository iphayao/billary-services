package th.co.readypaper.billary.repo.entity.document;

import lombok.*;
import th.co.readypaper.billary.repo.entity.AuditableEntity;
import th.co.readypaper.billary.repo.entity.company.Company;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Document extends AuditableEntity<UUID> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @OneToOne
    @JoinColumn(name = "company_id")
    private Company company;
    @OneToOne
    @JoinColumn(name = "document_type_id")
    private DocumentType documentType;
    private int startNumber;
    private int startMonth;
    private int startYear;
    @OneToMany
    @JoinColumn(name = "document_id")
    private List<DocumentSerial> documentSerials;
}
