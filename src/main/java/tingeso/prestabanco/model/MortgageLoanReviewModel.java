package tingeso.prestabanco.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mortgage_loan_review")
@NoArgsConstructor
@Data
public class MortgageLoanReviewModel extends MortgageLoanModel {
    @ManyToOne
    @JoinColumn(name = "review_requester_id")
    private ExecutiveModel review_requester;
    private boolean has_been_reviewed = false;
    private boolean has_been_approved = false;

    public MortgageLoanReviewModel(MortgageLoanModel mortgage, ExecutiveModel executive) {
        super(mortgage);
        this.review_requester = executive;
    }

}
