package tingeso.prestabanco;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tingeso.prestabanco.dto.LoginRequest;
import tingeso.prestabanco.dto.LoginResponse;
import tingeso.prestabanco.dto.RegisterResponse;
import tingeso.prestabanco.model.ClientModel;
import tingeso.prestabanco.model.RoleModel;
import tingeso.prestabanco.model.UserModel;
import tingeso.prestabanco.repository.ClientRepository;
import tingeso.prestabanco.repository.MortgageLoanRepository;
import tingeso.prestabanco.repository.RoleRepository;
import tingeso.prestabanco.repository.UserRepository;
import tingeso.prestabanco.service.ClientService;
import org.junit.jupiter.api.Assertions;
import tingeso.prestabanco.util.JwtUtil;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;


public class ClientServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MortgageLoanRepository mortgageLoanRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private ClientService clientService;

    private ClientModel mockClient;
    private RoleModel mockRole;
    private LoginRequest mockLoginRequest;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockRole = new RoleModel(1L, "CLIENT");
        mockLoginRequest = new LoginRequest("ignacioladal@gmail.com", "ClaveCorrecta123.");
        UserModel mockUser = new UserModel(1L, "ignacioladal@gmail.com", "ClaveCorrecta123.", mockRole);
        mockClient = new ClientModel(mockUser, "Ignacio", "Lara", new Date(2002, 1, 4), "otro", "Chilena", "Diinf usach", "1234567");
    }

    @Test
    public void testRegister() {
        when(clientRepository.save(mockClient)).thenReturn(mockClient);
        when(userRepository.findByEmail("ignacioladal@gmail.com")).thenReturn(Optional.empty());
        when(roleRepository.findById(1L)).thenReturn(Optional.of(mockRole));
        when(passwordEncoder.encode("ClaveCorrecta123.")).thenReturn("ClaveCorrecta123.");
        Optional<RegisterResponse> result = clientService.register(mockClient);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void testRegisterNoRole() {
        when(clientRepository.save(mockClient)).thenReturn(mockClient);
        when(userRepository.findByEmail("ignacioladal@gmail.com")).thenReturn(Optional.empty());
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());
        when(passwordEncoder.encode("ClaveCorrecta123.")).thenReturn("ClaveCorrecta123.");
        Assertions.assertThrows(RuntimeException.class, () -> {clientService.register(mockClient);});
    }

    @Test
    public void testRegisterInvalidEmail() {
        when(clientRepository.save(mockClient)).thenReturn(mockClient);
        when(userRepository.findByEmail("ignacioladal@gmail.com")).thenReturn(Optional.of(mockClient));
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());
        when(passwordEncoder.encode("ClaveCorrecta123.")).thenReturn("ClaveCorrecta123.");
        Optional<RegisterResponse> result = clientService.register(mockClient);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testRegisterInvalidPassword() {
        mockClient.setPassword("123");
        when(clientRepository.save(mockClient)).thenReturn(mockClient);
        when(userRepository.findByEmail("ignacioladal@gmail.com")).thenReturn(Optional.of(mockClient));
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());
        when(passwordEncoder.encode("ClaveCorrecta123.")).thenReturn("ClaveCorrecta123.");
        Optional<RegisterResponse> result = clientService.register(mockClient);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testRegisterInvalidBirthdate() {
        mockClient.setBirth_date(new Date(1800, 1, 1));
        when(clientRepository.save(mockClient)).thenReturn(mockClient);
        when(userRepository.findByEmail("ignacioladal@gmail.com")).thenReturn(Optional.of(mockClient));
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());
        when(passwordEncoder.encode("ClaveCorrecta123.")).thenReturn("ClaveCorrecta123.");
        Optional<RegisterResponse> result = clientService.register(mockClient);
        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    public void testRegisterInvalidNationality() {
        mockClient.setNationality("USACHINA");
        when(clientRepository.save(mockClient)).thenReturn(mockClient);
        when(userRepository.findByEmail("ignacioladal@gmail.com")).thenReturn(Optional.of(mockClient));
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());
        when(passwordEncoder.encode("ClaveCorrecta123.")).thenReturn("ClaveCorrecta123.");
        Optional<RegisterResponse> result = clientService.register(mockClient);
        Assertions.assertTrue(result.isEmpty());

    }


    @Test
    public void testLoginNoEmail() {
        when(userRepository.findByEmail("ignacioladal@gmail.com")).thenReturn(Optional.empty());
        Optional<LoginResponse> res = clientService.login(mockLoginRequest);
        Assertions.assertTrue(res.isEmpty());

    }

    @Test
    public void testLoginWrongPassword() {
        mockLoginRequest.setPassword("ClaveIncorrecta");
        String password = mockClient.getPassword();
        String encrypted_password = passwordEncoder.encode(password);
        mockClient.setPassword(encrypted_password);
        when(userRepository.findByEmail("ignacioladal@gmail.com")).thenReturn(Optional.of(mockClient));
        when(passwordEncoder.matches("ClaveIncorrecta", encrypted_password)).thenReturn(false);
        Optional<LoginResponse> res = clientService.login(mockLoginRequest);
        Assertions.assertTrue(res.isEmpty());
    }

    @Test
    public void testLoginNoClientAccount() {
        String password = mockClient.getPassword();
        String encrypted_password = passwordEncoder.encode(password);
        mockClient.setPassword(encrypted_password);
        when(userRepository.findByEmail("ignacioladal@gmail.com")).thenReturn(Optional.of(mockClient));
        when(clientRepository.findByEmail("ignacioladal@gmail.com")).thenReturn(Optional.empty());
        Optional<LoginResponse> result = clientService.login(mockLoginRequest);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testLogin() {
        String password = mockClient.getPassword();
        String encrypted_password = passwordEncoder.encode(password);
        mockClient.setPassword(encrypted_password);
        when(userRepository.findByEmail("ignacioladal@gmail.com")).thenReturn(Optional.of(mockClient));
        when(clientRepository.findByEmail("ignacioladal@gmail.com")).thenReturn(Optional.of(mockClient));
        when(passwordEncoder.matches("ClaveCorrecta123.", encrypted_password)).thenReturn(true);
        Optional<LoginResponse> result = clientService.login(mockLoginRequest);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void testGetMortgageRequests() {
        when(mortgageLoanRepository.findAllByClientId(mockClient.getId())).thenReturn(new ArrayList<>());
        Assertions.assertTrue(clientService.getMortgageRequests(mockClient).isEmpty());
    }
}
