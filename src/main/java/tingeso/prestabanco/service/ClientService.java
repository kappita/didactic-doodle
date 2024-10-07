package tingeso.prestabanco.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tingeso.prestabanco.dto.LoginRequest;
import tingeso.prestabanco.dto.LoginResponse;
import tingeso.prestabanco.dto.RegisterResponse;
import tingeso.prestabanco.model.ClientModel;
import tingeso.prestabanco.model.RoleModel;
import tingeso.prestabanco.model.UserModel;
import tingeso.prestabanco.repository.ClientRepository;
import tingeso.prestabanco.repository.RoleRepository;
import tingeso.prestabanco.repository.UserRepository;
import tingeso.prestabanco.util.JwtUtil;
import tingeso.prestabanco.util.Nationalities;

import java.sql.Date;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;


    public Optional<RegisterResponse> register(ClientModel client) {
        System.out.println("holaregister");
        Boolean details_validation = validateClientDetails(client);
        if (!details_validation) {
            return Optional.empty();
        }

        String password = client.getPassword();
        String encrypted_password = passwordEncoder.encode(password);
        client.setPassword(encrypted_password);
        Optional<RoleModel> role = roleRepository.findById(Long.parseLong("1"));
        if (role.isEmpty()) {
            throw new RuntimeException("Role not found");
        }
        client.setRole(role.get());
        System.out.println(client.toString());
        clientRepository.save(client);
        RegisterResponse registerResponse = new RegisterResponse("Cliente creado satisfactoriamente");

        return Optional.of(registerResponse);
    }

    public Optional<LoginResponse> login(LoginRequest req) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_CLIENT"));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(),
                        req.getPassword(),
                        authorities


                )
        );

        Optional<ClientModel> client = clientRepository.findByEmail(req.getEmail());
        if(client.isEmpty()) {
            return Optional.empty();
        }


        Map<String, Object> claims = new HashMap<>();
        claims.put("role", 1);
        String token = jwtUtil.createToken(claims, client.get().getId().toString());
        LoginResponse res = new LoginResponse(token, client.get().getName());
        return Optional.of(res);

    };


//    public Optional<ClientModel> login(String username, String password) {
//        return
//    };

    private Boolean validateClientDetails(ClientModel client) {
        System.out.println("Validando mail");
        Boolean email_validation = validateEmail(client.getEmail());

        if (!email_validation) {
            return false;
        }
        System.out.println("validando pass");
        String password = client.getPassword();
        Boolean password_validation = validatePasswordRequirements(password);
        if (!password_validation) {
            return false;
        }
        System.out.println("validando birth");
        Date birthdate = client.getBirth_date();
        Boolean birthdate_validation = validateBirthdate(birthdate);
        if (!birthdate_validation) {
            return false;
        }
        System.out.println("Validando nationality");
        String nationality = client.getNationality();
        Boolean nationality_validation = validateNationality(nationality);
        if (!nationality_validation) {
            return false;
        }

        return true;
    }

    private Boolean validatePasswordRequirements(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[.@#$%^&+=])(?=\\S+$).{8,}$";
        return password != null && Pattern.matches(passwordRegex, password);
    }

    private Boolean validateEmail(String email) {
        Optional<UserModel> existing = userRepository.findByEmail(email);
        return existing.isEmpty();
    }

    private Boolean validateBirthdate(Date birthdate) {
        int year = birthdate.toLocalDate().getYear();
        System.out.println(birthdate.toString());
        System.out.println(year);
        return year > 1900;
    };

    private Boolean validateNationality(String nationality) {
        return Nationalities.NATIONALITIES.contains(nationality);
    }
}
