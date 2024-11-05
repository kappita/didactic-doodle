package tingeso.prestabanco;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import tingeso.prestabanco.dto.LoginRequest;
import tingeso.prestabanco.dto.LoginResponse;
import tingeso.prestabanco.dto.RegisterResponse;
import tingeso.prestabanco.model.ClientModel;
import tingeso.prestabanco.model.ExecutiveModel;
import tingeso.prestabanco.model.RoleModel;
import tingeso.prestabanco.model.UserModel;
import tingeso.prestabanco.repository.ClientRepository;
import tingeso.prestabanco.repository.ExecutiveRepository;
import tingeso.prestabanco.repository.RoleRepository;
import tingeso.prestabanco.repository.UserRepository;
import tingeso.prestabanco.service.ClientService;
import tingeso.prestabanco.service.ExecutiveService;
import tingeso.prestabanco.util.JwtUtil;

import java.sql.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class ExecutiveServiceTest {
    @Mock
    private ExecutiveRepository executiveRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ExecutiveService executiveService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    private ExecutiveModel mockExecutive;
    private RoleModel mockRole;
    private UserModel mockUser;
    private LoginRequest mockLoginRequest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockRole = new RoleModel(1L, "EXECUTIVE");
        mockUser = new UserModel(1L, "ignacioladal@gmail.com", "ClaveCorrecta123.", mockRole);
        mockLoginRequest = new LoginRequest("ignacioladal@gmail.com", "ClaveCorrecta123.");
        mockExecutive = new ExecutiveModel(mockUser, "Ignacio");

    }

    @Test
    public void testRegister() {
        when(executiveRepository.save(mockExecutive)).thenReturn(mockExecutive);
        when(roleRepository.findByName("EXECUTIVE")).thenReturn(Optional.of(mockRole));
        Optional<RegisterResponse> result = executiveService.register(mockExecutive);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void testRegisterNoRole() {
        when(executiveRepository.save(mockExecutive)).thenReturn(mockExecutive);
        when(executiveRepository.findByEmail("ignacioladal@gmail.com")).thenReturn(Optional.empty());
        when(roleRepository.findByName("EXECUTIVE")).thenReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class, () -> {executiveService.register(mockExecutive);});
    }

    @Test
    public void testRegisterInvalidEmail() {
        when(executiveRepository.save(mockExecutive)).thenReturn(mockExecutive);
        when(userRepository.findByEmail("ignacioladal@gmail.com")).thenReturn(Optional.of(mockExecutive));
        when(roleRepository.findByName("EXECUTIVE")).thenReturn(Optional.of(mockRole));
        Assertions.assertThrows(RuntimeException.class, () -> {executiveService.register(mockExecutive);});
    }



    @Test
    public void testLoginNoEmail() {
        when(userRepository.findByEmail("ignacioladal@gmail.com")).thenReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class, () -> {executiveService.login(mockLoginRequest);});

    }

    @Test
    public void testLoginWrongPassword() {
        mockExecutive.setPassword("ClaveIncorrecta");
        String password = mockExecutive.getPassword();
        String encrypted_password = passwordEncoder.encode(password);
        mockExecutive.setPassword(encrypted_password);
        when(passwordEncoder.matches(password, mockExecutive.getPassword())).thenReturn(true);
        when(userRepository.findByEmail("ignacioladal@gmail.com")).thenReturn(Optional.of(mockExecutive));
        Assertions.assertThrows(RuntimeException.class, () -> {executiveService.login(mockLoginRequest);});
    }

    @Test
    public void testLoginNoClientAccount() {
        String password = mockExecutive.getPassword();
        String encrypted_password = passwordEncoder.encode(password);
        mockExecutive.setPassword(encrypted_password);
        when(userRepository.findByEmail("ignacioladal@gmail.com")).thenReturn(Optional.of(mockExecutive));
        when(executiveRepository.findByEmail("ignacioladal@gmail.com")).thenReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class, () -> {executiveService.login(mockLoginRequest);});
    }

    @Test
    public void testLogin() {
        String password = mockExecutive.getPassword();
        String encrypted_password = passwordEncoder.encode(password);
        mockExecutive.setPassword(encrypted_password);
        when(userRepository.findByEmail("ignacioladal@gmail.com")).thenReturn(Optional.of(mockExecutive));
        when(executiveRepository.findByEmail("ignacioladal@gmail.com")).thenReturn(Optional.of(mockExecutive));
        when(passwordEncoder.matches(password, mockExecutive.getPassword())).thenReturn(true);
        Optional<LoginResponse> result = executiveService.login(mockLoginRequest);
        Assertions.assertTrue(result.isPresent());
    }
}
