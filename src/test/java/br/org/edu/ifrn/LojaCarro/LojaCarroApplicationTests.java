package br.org.edu.ifrn.LojaCarro;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class LojaCarroApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void simularFalhaItem3() {

        assertEquals("Sucesso", "Sucesso");

    }
}