package tingeso.prestabanco.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import tingeso.prestabanco.model.PreApprovedMortgageLoanModel;

import java.util.List;

@Data
@AllArgsConstructor
public class SimulationResponse {
    private Boolean success;
    private List<SimulationStep> steps;
    private PreApprovedMortgageLoanModel mortgage;
}


