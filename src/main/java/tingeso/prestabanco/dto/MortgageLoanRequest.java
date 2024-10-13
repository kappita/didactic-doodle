package tingeso.prestabanco.dto;


import lombok.Data;



@Data
public class MortgageLoanRequest {
    private Long loan_type_id;
    private Integer payment_term;
    private Long financed_amount;
    private Float interest_rate;
}
