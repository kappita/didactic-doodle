package tingeso.prestabanco.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MortgageLoanRequest {
    private Long loan_type_id;
    private Integer payment_term;
    private Long financed_amount;
    private Float interest_rate;
}
