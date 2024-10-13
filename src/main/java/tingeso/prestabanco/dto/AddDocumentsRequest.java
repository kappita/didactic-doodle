package tingeso.prestabanco.dto;

import lombok.Data;

import java.io.File;
import java.util.List;

@Data
public class AddDocumentsRequest {
    private List<Long> document_type_ids;
}
