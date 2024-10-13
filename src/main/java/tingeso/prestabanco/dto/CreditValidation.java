package tingeso.prestabanco.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class CreditValidation {
    private Long client_income;
    private Boolean has_acceptable_credit_history;
    private Boolean has_financial_stability;
    private Long monthly_debt;
    private Long property_value;
    private Date validated_birthdate;
}
