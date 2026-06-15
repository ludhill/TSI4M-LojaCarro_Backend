package br.org.edu.ifrn.LojaCarro.services;

import br.org.edu.ifrn.LojaCarro.model.Carro;
import br.org.edu.ifrn.LojaCarro.repository.CarroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarroServiceTest {

    @Mock
    private CarroRepository repository;

    @InjectMocks
    private CarroService service;

    @Test
    void deveBuscarTodos() {

        List<Carro> carros = List.of(
                new Carro(),
                new Carro()
        );

        when(repository.findAll())
                .thenReturn(carros);

        List<Carro> resultado = service.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(repository).findAll();
    }

    @Test
    void deveSalvarCarro() {

        Carro carro = new Carro();
        carro.setMarca("Ford");

        when(repository.save(any()))
                .thenReturn(carro);

        Carro resultado = service.save(carro);

        assertNotNull(resultado);
        assertEquals("Ford", resultado.getMarca());

        verify(repository).save(carro);
    }

    @Test
    void deveBuscarPorId() {

        Carro carro = new Carro();
        carro.setMarca("Ford");

        when(repository.findById(1L))
                .thenReturn(Optional.of(carro));

        Optional<Carro> resultado =
                service.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(
                "Ford",
                resultado.get().getMarca());

        verify(repository)
                .findById(1L);
    }

    @Test
    void deveRetornarOptionalVazioQuandoNaoEncontrar() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        Optional<Carro> resultado =
                service.findById(1L);

        assertTrue(resultado.isEmpty());

        verify(repository)
                .findById(1L);
    }

    @Test
    void deveAtualizarCarro() {

        Carro carro = new Carro();
        carro.setId(1L);
        carro.setMarca("Toyota");

        when(repository.save(any()))
                .thenReturn(carro);

        Carro resultado =
                service.update(carro);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(
                "Toyota",
                resultado.getMarca());

        verify(repository)
                .save(carro);
    }

    @Test
    void deveExcluirCarroPorId() {

        doNothing()
                .when(repository)
                .deleteById(1L);

        service.deleteById(1L);

        verify(repository)
                .deleteById(1L);
    }
}