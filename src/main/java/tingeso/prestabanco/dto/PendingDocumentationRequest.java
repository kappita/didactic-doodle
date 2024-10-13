package tingeso.prestabanco.dto;

import lombok.Data;

import java.util.List;

@Data
public class PendingDocumentationRequest {
    private List<Long> document_ids;
    private String details;
}
