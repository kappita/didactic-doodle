package tingeso.prestabanco.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tingeso.prestabanco.model.LoanTypeModel;
import tingeso.prestabanco.service.LoanTypeService;

import java.util.List;

@RestController
@RequestMapping("/utils")
public class UtilsController {
    @Autowired
    LoanTypeService loanTypeService;

//    @GetMapping("/loan_types")
//    public ResponseEntity<List<LoanTypeModel>> getLoanTypes() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        return ResponseEntity.ok(loanTypeService.getAllLoanTypes());
//    }
}
