package tingeso.prestabanco.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendingDocumentationRequest {
    private List<Long> document_ids;
    private String details;
}
