package tingeso.prestabanco.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/utils")
public class UtilsController {


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
