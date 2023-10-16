package br.com.wagnercastro.todolist.filter;


import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.wagnercastro.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;


@Component
public class FilterTaskAuth extends OncePerRequestFilter {


    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var servletPath = request.getServletPath();

        if(servletPath.startsWith("/tasks/")) {


            //Pega os dados enviados para autenticação (Usuario e Senha)
            var authEncoded = request.getHeader("Authorization");
            // Apaga as informações desnecessarias, no caso o "Basic" que faz referencia ao tipo de autenticação
            authEncoded = authEncoded.substring("Basic".length()).trim();

            // Decodifica a autenticação para um array de bytes
            byte[] authDecode = Base64.getDecoder().decode(authEncoded);

            // Transforma a autenticação decodificada para texto.
            var authString = new String(authDecode);

            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            //Validar usuario.
            var user = this.userRepository.findByUsername(username);
            System.out.println(user);
            if (user == null) {
                response.sendError(401);
            } else {
                //Validar Senha.
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passwordVerify.verified) {
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }
            }
        } else{
            filterChain.doFilter(request, response);
        }


    }
}

