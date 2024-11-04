package tingeso.prestabanco.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditEvaluation {
    private CreditValidation credit_validation;
    private SavingCapacity saving_capacity;
}
