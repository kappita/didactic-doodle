package tingeso.prestabanco.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "preapproved_mortgage_loan")
@NoArgsConstructor
@Data

public class PreApprovedMortgageLoanModel extends MortgageLoanModel {
    private Long administration_fee;
    private Long credit_insurance_fee;
    private Long fire_insurance_fee = 20000L;
    private Long total_monthly_cost;
    private Long total_cost;

    public PreApprovedMortgageLoanModel(MortgageLoanModel mortgage) {
        super(mortgage);
        this.administration_fee = Math.round(mortgage.getFinanced_amount() * 0.01);
        this.credit_insurance_fee = Math.round(mortgage.financed_amount * 0.0003);
        this.total_monthly_cost = mortgage.getMonthlyQuota() + fire_insurance_fee + credit_insurance_fee;
        this.total_cost = (total_monthly_cost * (payment_term * 12)) + administration_fee;
    }
}
