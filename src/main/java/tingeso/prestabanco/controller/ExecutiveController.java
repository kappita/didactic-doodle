package tingeso.prestabanco.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tingeso.prestabanco.dto.LoginRequest;
import tingeso.prestabanco.dto.LoginResponse;
import tingeso.prestabanco.dto.RegisterResponse;
import tingeso.prestabanco.model.ClientModel;
import tingeso.prestabanco.model.ExecutiveModel;
import tingeso.prestabanco.service.ExecutiveService;
import tingeso.prestabanco.util.JwtUtil;

import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/executives")
public class ExecutiveController {
    @Autowired
    private ExecutiveService executiveService;

    @Autowired
    private JwtUtil jwtUtil;

    @CrossOrigin("*")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Optional<LoginResponse> res = executiveService.login(loginRequest);
        if (res.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(res.get());
    }

    @GetMapping("/me")
    public ResponseEntity<ExecutiveModel> getMe(@RequestHeader("Authorization") String authorization) {
        Optional<ExecutiveModel> executive = jwtUtil.validateExecutive(authorization);
        if (executive.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(executive.get());
    }

    @CrossOrigin("*")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody ExecutiveModel exec) {
        System.out.println("hola endpoint");
        Optional<RegisterResponse> res = executiveService.register(exec);
        if (res.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(res.get());
    }
}
