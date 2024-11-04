//package tingeso.prestabanco.security;
//
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AnonymousAuthenticationToken;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import tingeso.prestabanco.model.ClientModel;
//import tingeso.prestabanco.model.ExecutiveModel;
//import tingeso.prestabanco.repository.ClientRepository;
//import tingeso.prestabanco.repository.ExecutiveRepository;
//import tingeso.prestabanco.util.JwtUtil;
//
//import java.io.IOException;
//import java.util.Optional;
//
//@Component
//public class ExecutiveJwtAuthenticationFilter extends OncePerRequestFilter {
//    @Autowired
//    JwtUtil jwtUtil;
//
//    @Autowired
//    ExecutiveRepository executiveRepository;
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
//        System.out.println("Filtrando ejecutivo");
//        String auth_header = req.getHeader("Authorization");
//        String jwt = null;
//        Long id = null;
//        Integer role = null;
//
//        if (auth_header != null && auth_header.startsWith("Bearer ")) {
//            System.out.println("Inicia con bearer");
//            jwt = auth_header.substring(7);
//            id = Long.parseLong(jwtUtil.extractClaim(jwt, Claims::getSubject));
//            role = jwtUtil.extractClaim(jwt, claims -> (Integer) claims.get("role"));
//        }
//
//        System.out.println(SecurityContextHolder.getContext().getAuthentication());
//        Boolean is_not_authenticated = SecurityContextHolder.getContext().getAuthentication() == null;
//        Boolean is_client = role != null && role == 2;
//        if (is_client && is_not_authenticated) {
//            Optional<ExecutiveModel> executive = executiveRepository.findById(id);
//            if (executive.isEmpty()) {
//                throw new ServletException("Not found");
//            }
//            UsernamePasswordAuthenticationToken authenticationToken =
//                    new UsernamePasswordAuthenticationToken(executive.get(), null, executive.get().getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//        }
//        chain.doFilter(req, res);
//    }
//
//}
