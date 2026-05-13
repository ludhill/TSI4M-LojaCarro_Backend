package br.org.edu.ifrn.LojaCarro;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class LojaCarroApplicationTests {

    @Test
    void contextLoads() {
        // Testa se o Spring sobe com o H2 (application-test.properties)
    }

    @Test
    void simularFalhaItem3() {
        // ITEM 3: Force a falha aqui para o vídeo
        assertEquals("Sucesso", "Falha", "Forçando erro para o pipeline do GitHub Actions");
        //assertEquals("Sucesso", "Sucesso");
    }
}