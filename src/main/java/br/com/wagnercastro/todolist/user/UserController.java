package br.com.wagnercastro.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**Controller é um componente utilizado para ser a primeira camada de acesso
 entre a requisição do usuario e as demais camadas como negocios ou bd.
 É uma rota de acesso.


 * Metodos de acesso HTTP
 * GET - Buscar informação.
 * POST - Adicionar dados ou informações.
 * PUT - Alterar dados ou informações.
 * DELETE - Deletar dados ou informações.
 * PATCH - Alterar somente uma parte dos dados ou informações.
 *
 */

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel){
        var user = this.userRepository.findByUsername(userModel.getUsername());
        if (user != null){
            System.out.println("Usuario já existente no banco de dados");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario já existe no banco de dados");
        }

        var passwordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(passwordHashed);

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario salvo com sucesso!\n" + userCreated);
    }




}
