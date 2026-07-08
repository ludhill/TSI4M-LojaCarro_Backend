
package br.org.edu.ifrn.lojacarro.services;

import br.org.edu.ifrn.lojacarro.model.Carro;
import br.org.edu.ifrn.lojacarro.repository.CarroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarroService {

    @Autowired
    public CarroRepository carroRepository;

    public Carro save(Carro c) {
        return carroRepository.save(c);
    }


    public void deleteById(Long id) {
        carroRepository.deleteById(id);
    }


    public Optional<Carro> findById(Long id) {
        return carroRepository.findById(id);
    }


    public List<Carro> findAll() {
        return carroRepository.findAll();
    }


    public Carro update(Carro c) {
        return carroRepository.save(c);  // Retorna o carro salvo para feedback
    }

}