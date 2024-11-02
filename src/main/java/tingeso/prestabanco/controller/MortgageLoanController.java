package tingeso.prestabanco.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tingeso.prestabanco.dto.*;
import tingeso.prestabanco.model.ClientModel;
import tingeso.prestabanco.model.ExecutiveModel;
import tingeso.prestabanco.model.MortgageLoanModel;
import tingeso.prestabanco.service.MortgageLoanService;

@RestController
@RequestMapping("/mortgage_loan")
public class MortgageLoanController {
    @Autowired
    private MortgageLoanService mortgageService;

    @PostMapping("")
    public ResponseEntity<MortgageLoanModel> postMortgageLoan(@RequestBody MortgageLoanRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ClientModel client = (ClientModel) auth.getPrincipal();
        return ResponseEntity.ok(mortgageService.receiveMortgage(req, client));
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends MortgageLoanModel> getMortgageLoan(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(mortgageService.getMortgageLoan(id, auth));
    }
    @PostMapping("/{id}/add_documents")
    public ResponseEntity<SimpleResponse> addDocuments(@PathVariable Long id, @RequestParam("files") MultipartFile[] files) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ClientModel client = (ClientModel) auth.getPrincipal();
        return ResponseEntity.ok(mortgageService.addDocuments(id, files, client));
    }

    @PostMapping("/{id}/set_final_approval")
    public ResponseEntity<SimpleResponse> setFinalApproval(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ClientModel client = (ClientModel) auth.getPrincipal();
        return ResponseEntity.ok(mortgageService.setFinalApproval(id, client));
    }

    @PostMapping("/{id}/cancel_mortgage")
    public ResponseEntity<SimpleResponse> cancelMortgage(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ClientModel client = (ClientModel) auth.getPrincipal();
        return ResponseEntity.ok(mortgageService.cancelMortgageByClient(id, client));
    }

    @PostMapping("/{id}/set_pending_documentation")
    public ResponseEntity<SimpleResponse> setPendingDocumentation(@PathVariable Long id, @RequestBody PendingDocumentationRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ExecutiveModel executive = (ExecutiveModel) auth.getPrincipal();
        return ResponseEntity.ok(mortgageService.setPendingDocumentation(id, req, executive));
    }

    @PostMapping("/{id}/set_in_evaluation")
    public ResponseEntity<SimpleResponse> setInEvaluation(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ExecutiveModel executive = (ExecutiveModel) auth.getPrincipal();
        return ResponseEntity.ok(mortgageService.setInEvaluation(id, executive));
    }

    @PostMapping("/{id}/evaluate_mortgage")
    public ResponseEntity<SimpleResponse> evaluateMortgage(@PathVariable Long id, @RequestBody CreditEvaluation credit_evaluation) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ExecutiveModel executive = (ExecutiveModel) auth.getPrincipal();
        return ResponseEntity.ok(mortgageService.evaluateMortgage(id, credit_evaluation, executive));
    }

    @PostMapping("/{id}/set_approved")
    public ResponseEntity<SimpleResponse> setApproved(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ExecutiveModel executive = (ExecutiveModel) auth.getPrincipal();
        return ResponseEntity.ok(mortgageService.setApproved(id, executive));
    }


    @PostMapping("/{id}/set_outgo")
    public ResponseEntity<SimpleResponse> setOutgo(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ExecutiveModel executive = (ExecutiveModel) auth.getPrincipal();
        return ResponseEntity.ok(mortgageService.setInOutgo(id, executive));
    }


}
