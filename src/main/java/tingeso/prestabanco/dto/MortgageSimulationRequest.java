package tingeso.prestabanco.dto;


import lombok.Data;


@Data
public class MortgageSimulationRequest {
    private Long loan_type_id;
    private Integer payment_term;
    private Long financed_amount;
    private Float interest_rate;
    private Long client_income;
    private Long monthly_debt;
    private Long property_value;
}
