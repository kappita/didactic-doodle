package tingeso.prestabanco.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimulationStep {
    private String name;
    private String description;
    private Boolean success;
}
