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
import tingeso.prestabanco.service.ClientService;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Optional<LoginResponse> res = clientService.login(loginRequest);
        if (res.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(res.get());
    }

    @GetMapping("/me")
    public ResponseEntity<ClientModel> getMe() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return ResponseEntity.badRequest().build();
        }
        System.out.println(auth.toString());
        ClientModel client = (ClientModel) auth.getPrincipal();
        return ResponseEntity.ok(client);
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

//    @GetMapping("")
//    public List<ClientModel> getAll() {
//        return clientService.getAll();
//    }
}
