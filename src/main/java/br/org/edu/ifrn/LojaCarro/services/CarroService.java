
package br.org.edu.ifrn.LojaCarro.services;

import br.org.edu.ifrn.LojaCarro.model.Carro;
import br.org.edu.ifrn.LojaCarro.repository.CarroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarroService {

    @Autowired
    public CarroRepository carroRepository;

    public Carro save(Carro c) {
        if (c == null || c.getModelo() == null || c.getModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("O modelo do carro não pode ser vazio!");
        }
        return carroRepository.save(c);
    }


    public void deleteById(Long id) {
        if (!carroRepository.existsById(id)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND,
                    "Operação inválida: O carro com o ID " + id + " não existe na base de dados."
            );
        }
        carroRepository.deleteById(id);
    }


    public Optional<Carro> findById(Long id) {
        return carroRepository.findById(id);
    }


    public List<Carro> findAll() {
        return carroRepository.findAll();
    }


    public Carro update(Carro c) {
        return this.save(c);
    }
}