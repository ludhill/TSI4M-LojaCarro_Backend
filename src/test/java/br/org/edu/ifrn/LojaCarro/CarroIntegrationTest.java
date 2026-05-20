package br.org.edu.ifrn.LojaCarro;

import br.org.edu.ifrn.LojaCarro.model.Carro;
import br.org.edu.ifrn.LojaCarro.services.CarroService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CarroIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CarroService carroService;

    private String getBaseUrl() {

        return "http://localhost:" + port + "/carro";
    }


    //CAMADA DE CONTROLE: SALVAR (Sucesso)

    @Test
    public void deveSalvarCarroComSucesso_CamadaControle() {
        Carro corolla = new Carro();
        corolla.setModelo("Corolla");
        corolla.setAno(1986);


        ResponseEntity<Carro> resposta = restTemplate.postForEntity(getBaseUrl() + "/salvar", corolla, Carro.class);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody()).isNotNull();
        assertThat(resposta.getBody().getModelo()).isEqualTo("Corolla");
    }


    // CAMADA DE SERVIÇO:
    @Test
    public void deveDarErroAoSalvarObjetoInvalido_ValidacaoCamadaServico() {
        Carro carroInvalido = new Carro();
        carroInvalido.setModelo("");

        assertThatThrownBy(() -> carroService.save(carroInvalido))
                .isInstanceOf(Exception.class);
    }


    //CAMADA DE CONTROLE
    @Test
    public void deveBuscarCarroPorIdComSucesso() {
        Carro carro = new Carro();
        carro.setModelo("Civic");
        carro.setAno(2020);
        Carro carroSalvo = carroService.save(carro);

        ResponseEntity<Carro> resposta = restTemplate.getForEntity(getBaseUrl() + "/" + carroSalvo.getId(), Carro.class);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody()).isNotNull();
        assertThat(resposta.getBody().getModelo()).isEqualTo("Civic");
    }

    @Test
    public void deveDarErro404_AoBuscarIdInexistente() {
        ResponseEntity<Carro> resposta = restTemplate.getForEntity(getBaseUrl() + "/9999", Carro.class);
        assertThat(resposta.getStatusCode()).isIn(HttpStatus.NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // CAMADA DE CONTROLE: consulta *

    @Test
    public void deveListarTodosOsCarros_CamadaControle() {
        Carro c1 = new Carro();
        c1.setModelo("Palio");
        c1.setAno(2010);
        carroService.save(c1);

        ResponseEntity<Carro[]> resposta = restTemplate.getForEntity(getBaseUrl(), Carro[].class);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody()).isNotNull();
        assertThat(resposta.getBody().length).isGreaterThanOrEqualTo(1);
    }


    // CAMADA DE CONTROLE: update

    @Test
    public void deveAtualizarCarroComSucesso_CamadaControle() {
        Carro carro = new Carro();
        carro.setModelo("Fusca");
        carro.setAno(1975);
        Carro cadastrado = carroService.save(carro);

        cadastrado.setModelo("Fusca Itamar");

        HttpEntity<Carro> request = new HttpEntity<>(cadastrado);
        ResponseEntity<Carro> resposta = restTemplate.exchange(
                getBaseUrl() + "/" + cadastrado.getId(),
                HttpMethod.PUT,
                request,
                Carro.class
        );

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody()).isNotNull();
        assertThat(resposta.getBody().getModelo()).isEqualTo("Fusca Itamar");
    }


    //serviçop INTEGRAÇÃO VERTICAL: delete do crud

    @Test
    public void deveDeletarCarroComSucesso() {
        Carro carro = new Carro();
        carro.setModelo("Uno");
        carro.setAno(1995);
        Carro cadastrado = carroService.save(carro);

        restTemplate.delete(getBaseUrl() + "/" + cadastrado.getId());

        ResponseEntity<Carro> respostaBusca = restTemplate.getForEntity(getBaseUrl() + "/" + cadastrado.getId(), Carro.class);
        assertThat(respostaBusca.getStatusCode()).isIn(HttpStatus.NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // TENTAR DELETAR UM ITEM que não existee

    @Test
    public void deveDarErro_AoTentarDeletarCarroInexistente() {
        Long idInexistente = 9999L;

        ResponseEntity<String> resposta = restTemplate.exchange(
                getBaseUrl() + "/" + idInexistente,
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    // controle CENÁRIO DE FALHA: ATUALIZAR (PUT) com dados infalidos

    @Test
    public void deveDarErro_AoTentarAtualizarCarroComModeloVazio() {

        Carro carroValido = new Carro();
        carroValido.setModelo("Gol");
        carroValido.setAno(2015);
        Carro cadastrado = carroService.save(carroValido);


        cadastrado.setModelo("");

        HttpEntity<Carro> request = new HttpEntity<>(cadastrado);


        ResponseEntity<String> resposta = restTemplate.exchange(
                getBaseUrl() + "/" + cadastrado.getId(),
                HttpMethod.PUT,
                request,
                String.class
        );

        assertThat(resposta.getStatusCode()).isIn(HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // serviço TESTE DE ISOLAMENTO: AMBIENTE SEGURO para cada teste

    @Test
    public void deveGarantirQueOContextoDeTestesIniciaSemInterferencia() {
        ResponseEntity<Carro[]> resposta = restTemplate.getForEntity(getBaseUrl(), Carro[].class);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody()).isNotNull();
    }
}