//package tingeso.prestabanco.security;
//
//import org.hibernate.event.spi.SaveOrUpdateEvent;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import tingeso.prestabanco.model.UserModel;
//import tingeso.prestabanco.repository.UserRepository;
//
//import java.util.Collection;
//import java.util.Optional;
//
//@Component
//public class UserAuthenticationProvider implements AuthenticationProvider {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Override
//    public Authentication authenticate(Authentication auth) throws AuthenticationException {
//        System.out.println("autenticando");
//        String email = auth.getName();
//        String password = auth.getCredentials().toString();
//        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
//
//
//
//        Optional<UserModel> user = userRepository.findByEmail(email);
//        if(user.isEmpty()) {
//            System.out.println("NO ACCOUNT");
//            throw new BadCredentialsException("Invalid email or password");
//        }
//
//        if (user.get().getAuthorities().containsAll(authorities)) {}
//
//        System.out.println("hay email");
//        if (!passwordEncoder.matches(password, user.get().getPassword())) {
//            throw new BadCredentialsException("Invalid email or password");
//        }
//
//        return new UsernamePasswordAuthenticationToken(user, password, user.get().getAuthorities());
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//}
