package br.org.edu.ifrn.LojaCarro;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "app.rate-limit.capacity=5",
                "app.rate-limit.duration-minutes=1"
        }
)
@ActiveProfiles("test")
public class SecurityIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @Test
    public void deveBloquearRequisicaoSemAutenticacao() {
        ResponseEntity<String> resposta = restTemplate.getForEntity(getBaseUrl() + "/carro", String.class);
        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void deveBloquearPorRateLimitAoExcederMaximoDeChamadasAutenticadas() {
        Map<String, String> credentials = Map.of("username", "root", "password", "root");
        ResponseEntity<Map> loginResponse = restTemplate.postForEntity(getBaseUrl() + "/auth/login", credentials, Map.class);

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        String token = (String) loginResponse.getBody().get("token");
        assertThat(token).isNotNull();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        String url = getBaseUrl() + "/carro";
        ResponseEntity<String> resposta = null;

        for (int i = 0; i < 10; i++) {
            resposta = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            if (resposta.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                break;
            }
        }

        assertThat(resposta).isNotNull();
        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
        assertThat(resposta.getBody()).contains("Limite de chamadas excedido");
    }
}