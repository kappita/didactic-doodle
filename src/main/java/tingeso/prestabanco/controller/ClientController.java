package tingeso.prestabanco.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tingeso.prestabanco.dto.LoginRequest;
import tingeso.prestabanco.dto.LoginResponse;
import tingeso.prestabanco.dto.RegisterResponse;
import tingeso.prestabanco.model.ClientModel;
import tingeso.prestabanco.model.LoanTypeModel;
import tingeso.prestabanco.model.MortgageLoanModel;
import tingeso.prestabanco.repository.LoanTypeRepository;
import tingeso.prestabanco.service.ClientService;
import tingeso.prestabanco.util.JwtUtil;

import java.util.List;
import java.util.Optional;

@RestController()
@CrossOrigin("*")
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    private ClientService clientService;



    @Autowired
    private LoanTypeRepository loanTypeRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Optional<LoginResponse> res = clientService.login(loginRequest);
        if (res.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(res.get());
    }

    @GetMapping("/me")
    public ResponseEntity<ClientModel> getMe(@RequestHeader("Authorization") String authorization) {
        Optional<ClientModel> client = jwtUtil.validateClient(authorization);
        if (client.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(client.get());
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody ClientModel client) {
        System.out.println("hola endpoint");
        Optional<RegisterResponse> res = clientService.register(client);
        if (res.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(res.get());
    }

    @GetMapping("/me/mortgage_loans")
    public ResponseEntity<List<MortgageLoanModel>> getMortgageLoans(@RequestHeader("Authorization") String authorization) {
        Optional<ClientModel> client = jwtUtil.validateClient(authorization);
        if (client.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(clientService.getMortgageRequests(client.get()));
    }

    @GetMapping("/loan_types")
    public ResponseEntity<List<LoanTypeModel>> getLoanTypes(@RequestHeader("Authorization") String authorization) {
        Optional<ClientModel> client = jwtUtil.validateClient(authorization);
        if (client.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(loanTypeRepository.findAll());
    }

//    @GetMapping("")
//    public List<ClientModel> getAll() {
//        return clientService.getAll();
//    }
}
