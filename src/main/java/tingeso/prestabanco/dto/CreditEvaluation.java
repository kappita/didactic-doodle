package tingeso.prestabanco.dto;


import lombok.Data;

@Data
public class CreditEvaluation {
    private CreditValidation credit_validation;
    private SavingCapacity saving_capacity;
}
