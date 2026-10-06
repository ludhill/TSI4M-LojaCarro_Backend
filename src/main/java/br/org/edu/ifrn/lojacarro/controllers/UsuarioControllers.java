package br.org.edu.ifrn.lojacarro.controllers;

import br.org.edu.ifrn.lojacarro.event.UsuarioAlteradoEvent;
import br.org.edu.ifrn.lojacarro.model.Usuario;
import br.org.edu.ifrn.lojacarro.repository.UsuarioRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControllers {

    private final UsuarioRepository usuarioRepository;
    private final ApplicationEventPublisher eventPublisher; // Injeção do publicador de eventos do Spring

    // Injeção de dependência via Construtor
    public UsuarioControllers(UsuarioRepository usuarioRepository, ApplicationEventPublisher eventPublisher) {
        this.usuarioRepository = usuarioRepository;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {
        Usuario salvo = usuarioRepository.save(usuario);

        // 1. DISPARA EVENTO DE CRIAÇÃO
        eventPublisher.publishEvent(new UsuarioAlteradoEvent(salvo.getId(), salvo.getUsername(), "CRIACAO"));

        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioRepository.findById(id)
                .map(usuarioExistente -> {
                    usuarioExistente.setUsername(usuario.getUsername());
                    // atualize outros campos se necessário (ex: senha, perfil)

                    Usuario atualizado = usuarioRepository.save(usuarioExistente);

                    // 2. DISPARA EVENTO DE ATUALIZAÇÃO
                    eventPublisher.publishEvent(new UsuarioAlteradoEvent(atualizado.getId(), atualizado.getUsername(), "ATUALIZACAO"));

                    return ResponseEntity.ok(atualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuarioRepository.delete(usuario);

                    // 3. DISPARA EVENTO DE EXCLUSÃO
                    eventPublisher.publishEvent(new UsuarioAlteradoEvent(id, usuario.getUsername(), "EXCLUSAO"));

                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}