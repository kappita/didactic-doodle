package tingeso.prestabanco.service;

import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tingeso.prestabanco.dto.LoginRequest;
import tingeso.prestabanco.dto.LoginResponse;
import tingeso.prestabanco.dto.RegisterResponse;
import tingeso.prestabanco.model.ClientModel;
import tingeso.prestabanco.model.ExecutiveModel;
import tingeso.prestabanco.model.RoleModel;
import tingeso.prestabanco.model.UserModel;
import tingeso.prestabanco.repository.ExecutiveRepository;
import tingeso.prestabanco.repository.RoleRepository;
import tingeso.prestabanco.repository.UserRepository;
import tingeso.prestabanco.util.JwtUtil;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ExecutiveService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ExecutiveRepository executiveRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    public Optional<RegisterResponse> register(ExecutiveModel executive) {

        Boolean email_validation = validateEmail(executive.getEmail());
        if (!email_validation) {
            throw new RuntimeException("Invalid email address or already registered");
        }

        String password = executive.getPassword();
        String encrypted_password = passwordEncoder.encode(password);
        executive.setPassword(encrypted_password);
        Optional<RoleModel> role = roleRepository.findByName("EXECUTIVE");
        if (role.isEmpty()) {
            throw new RuntimeException("Role not found");
        }
        executive.setRole(role.get());
        System.out.println(executive.toString());
        System.out.println(executive.getEmail() );
        ExecutiveModel ex = new ExecutiveModel();
        ex.setEmail("nashe");
        ex.setPassword("nashe");
        ex.setRole(role.get());
        ex.setName("nashe");

        try {
            executiveRepository.save(ex);
        } catch (Exception e) {
            // Log the error or handle it accordingly
            throw new RuntimeException("Error saving executive", e);
        }
        RegisterResponse registerResponse = new RegisterResponse("Ejecutivo creado satisfactoriamente");

        return Optional.of(registerResponse);
    }

    public Optional<LoginResponse> login(LoginRequest req) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_EXECUTIVE"));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(),
                        req.getPassword(),
                        authorities
                )
        );

        Optional<ExecutiveModel> client = executiveRepository.findByEmail(req.getEmail());
        if(client.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }


        Map<String, Object> claims = new HashMap<>();
        claims.put("role", 2);
        String token = jwtUtil.createToken(claims, client.get().getId().toString());
        LoginResponse res = new LoginResponse(token, client.get().getName());
        return Optional.of(res);

    };


//    public Optional<ClientModel> login(String username, String password) {
//        return
//    };

    private Boolean validateEmail(String email) {
        Optional<UserModel> user = userRepository.findByEmail(email);
        return user.isEmpty();
    }

}
