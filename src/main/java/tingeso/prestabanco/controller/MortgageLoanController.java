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
import tingeso.prestabanco.model.UserModel;
import tingeso.prestabanco.service.MortgageLoanService;
import tingeso.prestabanco.util.JwtUtil;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/mortgage_loan")
public class MortgageLoanController {
    @Autowired
    private MortgageLoanService mortgageService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("")
    public ResponseEntity<MortgageLoanModel> postMortgageLoan(@RequestBody MortgageLoanRequest req, @RequestHeader("Authorization") String authorization) {
        Optional<ClientModel> client = jwtUtil.validateClient(authorization);
        if (client.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(mortgageService.receiveMortgage(req, client.get()));
    }

    @GetMapping("/all_reviewable")
    public ResponseEntity<List<MortgageLoanModel>> getAllReviewableMortgageLoan(@RequestHeader("Authorization") String authorization) {
        Optional<ExecutiveModel> executive = jwtUtil.validateExecutive(authorization);
        if (executive.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(mortgageService.getAllReviewable());
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends MortgageLoanModel> getMortgageLoan(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        Optional<UserModel> user = jwtUtil.validateUser(authorization);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }


        return ResponseEntity.ok(mortgageService.getMortgageLoan(id, user.get()));
    }
    @PostMapping("/{id}/add_documents")
    public ResponseEntity<SimpleResponse> addDocuments(@PathVariable Long id, @RequestParam("files") MultipartFile[] files, @RequestHeader("Authorization") String authorization) {
        Optional<ClientModel> client = jwtUtil.validateClient(authorization);
        if (client.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(mortgageService.addDocuments(id, files, client.get()));
    }

    @PostMapping("/{id}/set_final_approval")
    public ResponseEntity<SimpleResponse> setFinalApproval(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        Optional<ClientModel> client = jwtUtil.validateClient(authorization);
        if (client.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(mortgageService.setFinalApproval(id, client.get()));
    }

    @PostMapping("/{id}/cancel_mortgage")
    public ResponseEntity<SimpleResponse> cancelMortgage(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        Optional<ClientModel> client = jwtUtil.validateClient(authorization);
        if (client.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(mortgageService.cancelMortgageByClient(id, client.get()));
    }

    @PostMapping("/{id}/set_pending_documentation")
    public ResponseEntity<SimpleResponse> setPendingDocumentation(@PathVariable Long id, @RequestBody PendingDocumentationRequest req, @RequestHeader("Authorization") String authorization) {
        Optional<ExecutiveModel> executive = jwtUtil.validateExecutive(authorization);
        if (executive.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(mortgageService.setPendingDocumentation(id, req, executive.get()));
    }

    @PostMapping("/{id}/set_in_evaluation")
    public ResponseEntity<SimpleResponse> setInEvaluation(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        Optional<ExecutiveModel> executive = jwtUtil.validateExecutive(authorization);
        if (executive.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(mortgageService.setInEvaluation(id, executive.get()));
    }

    @PostMapping("/{id}/evaluate_mortgage")
    public ResponseEntity<SimpleResponse> evaluateMortgage(@PathVariable Long id, @RequestBody CreditEvaluation credit_evaluation, @RequestHeader("Authorization") String authorization) {
        Optional<ExecutiveModel> executive = jwtUtil.validateExecutive(authorization);
        if (executive.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(mortgageService.evaluateMortgage(id, credit_evaluation, executive.get()));
    }

    @PostMapping("/{id}/set_approved")
    public ResponseEntity<SimpleResponse> setApproved(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        Optional<ExecutiveModel> executive = jwtUtil.validateExecutive(authorization);
        if (executive.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(mortgageService.setApproved(id, executive.get()));
    }


    @PostMapping("/{id}/set_outgo")
    public ResponseEntity<SimpleResponse> setOutgo(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        Optional<ExecutiveModel> executive = jwtUtil.validateExecutive(authorization);
        if (executive.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(mortgageService.setInOutgo(id, executive.get()));
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<SimpleResponse> reviewMortgage(@PathVariable Long id, @RequestBody MortgageReview req, @RequestHeader("Authorization") String authorization) {
        Optional<ExecutiveModel> executive = jwtUtil.validateExecutive(authorization);
        if (executive.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(mortgageService.reviewMortgage(id, req, executive.get()));
    }



}
