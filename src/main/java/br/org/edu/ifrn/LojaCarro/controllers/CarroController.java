package br.org.edu.ifrn.LojaCarro.controllers;

import br.org.edu.ifrn.LojaCarro.model.Carro;
import br.org.edu.ifrn.LojaCarro.services.CarroService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/carro")
public class CarroController {

    private static final Logger log =
            LoggerFactory.getLogger(CarroController.class);

    private final CarroService carroService;

    public CarroController(CarroService carroService) {
        this.carroService = carroService;
    }

    private String obterDadosUsuarioLogado() {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            return "Usuário: ["
                    + auth.getName()
                    + "] Papel: "
                    + auth.getAuthorities();
        }

        return "Usuário Anônimo";
    }

    @PostMapping("/salvar")
    public ResponseEntity<Carro> salvarCarro(
            @Valid @RequestBody Carro c) {

        log.info("{} cadastrando carro {} {}",
                obterDadosUsuarioLogado(),
                c.getMarca(),
                c.getModelo());

        Carro saved = carroService.save(c);

        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Carro> atualizarCarro(
            @PathVariable Long id,
            @Valid @RequestBody Carro c) {

        log.info("{} atualizando carro {}",
                obterDadosUsuarioLogado(),
                id);

        c.setId(id);

        Carro updated =
                carroService.update(c);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCarro(
            @PathVariable Long id) {

        log.warn("{} deletando carro {}",
                obterDadosUsuarioLogado(),
                id);

        carroService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carro> pesquisarCarroPorId(
            @PathVariable Long id) {

        log.info("{} consultando carro {}",
                obterDadosUsuarioLogado(),
                id);

        Optional<Carro> carro =
                carroService.findById(id);

        return carro
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Carro>> pesquisarTodosCarros() {

        log.info("{} listando carros",
                obterDadosUsuarioLogado());

        List<Carro> carros =
                carroService.findAll();

        return ResponseEntity.ok(carros);
    }
}