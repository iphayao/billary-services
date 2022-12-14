package th.co.readypaper.billary.repo.entity.document;

import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.readypaper.billary.repo.entity.AuditableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class DocumentSerial extends AuditableEntity<UUID> {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "document_id")
    private UUID documentId;
    private int currentNumber;
    private int currentMonth;
    private int currentYear;
}
