package th.co.readypaper.billary.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class DocumentId {
    private String value;

    public static Optional<DocumentId> of(String val) {
        return Optional.of(new DocumentId(val));
    }

}
