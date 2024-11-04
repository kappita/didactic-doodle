package tingeso.prestabanco.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tingeso.prestabanco.dto.MortgageSimulationRequest;
import tingeso.prestabanco.dto.SimulationResponse;
import tingeso.prestabanco.model.ClientModel;
import tingeso.prestabanco.service.MortgageLoanSimulationService;

@RestController
@CrossOrigin("*")
@RequestMapping("/simulator")
public class MortgageSimulationController {
    @Autowired
    MortgageLoanSimulationService mortgageLoanSimulationService;

    @PostMapping("")
    public ResponseEntity<SimulationResponse> simulate(@RequestBody MortgageSimulationRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return ResponseEntity.badRequest().build();
        }
        ClientModel client = (ClientModel) auth.getPrincipal();
        return ResponseEntity.ok(mortgageLoanSimulationService.simulate(req, client));
    }
}
