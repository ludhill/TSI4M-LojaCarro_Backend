package br.org.edu.ifrn.LojaCarro.controllers;
import br.org.edu.ifrn.LojaCarro.model.Usuario;
import br.org.edu.ifrn.LojaCarro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @PostMapping
    public ResponseEntity<Usuario>
    cadastrarUsuario(
            @RequestBody Usuario usuario) {

        Usuario saved =
                repository.save(usuario);

        return ResponseEntity.ok(saved);
    }
}