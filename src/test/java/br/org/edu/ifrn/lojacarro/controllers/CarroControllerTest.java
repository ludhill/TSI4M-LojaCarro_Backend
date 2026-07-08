package br.org.edu.ifrn.lojacarro.controllers;
import br.org.edu.ifrn.lojacarro.model.Carro;
import br.org.edu.ifrn.lojacarro.security.JwtFilter;
import br.org.edu.ifrn.lojacarro.services.CarroService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = CarroController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class
        },
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = JwtFilter.class
                )
        }
)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class CarroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings({"removal", "unused"})
    @MockBean
    private CarroService carroService;

    @SuppressWarnings({"removal", "unused"})
    @MockBean
    private JwtFilter jwtFilter;


    @Test
    void deveRetornarAnoEId()
            throws Exception {

        Carro carro =
                new Carro();

        carro.setId(1L);
        carro.setMarca("Toyota");
        carro.setModelo("Corolla");
        carro.setAno(2023);

        Mockito
                .when(
                        carroService.findById(1L))
                .thenReturn(
                        Optional.of(carro));

        mockMvc.perform(
                        get("/carro/1"))

                .andExpect(
                        status().isOk())

                .andExpect(
                        jsonPath("$.id")
                                .value(1))

                .andExpect(
                        jsonPath("$.ano")
                                .value(2023));
    }

    @Test
    void deveRetornarListaDeCarros()
            throws Exception {

        Carro carro =
                new Carro();

        carro.setId(1L);
        carro.setMarca("Toyota");
        carro.setModelo("Corolla");
        carro.setAno(2023);

        Mockito
                .when(
                        carroService.findAll())
                .thenReturn(
                        List.of(carro));

        mockMvc.perform(
                        get("/carro"))

                .andExpect(
                        status().isOk())

                .andExpect(
                        jsonPath("$[0].marca")
                                .value("Toyota"))

                .andExpect(
                        jsonPath("$[0].modelo")
                                .value("Corolla"))

                .andExpect(
                        jsonPath("$[0].ano")
                                .value(2023));
    }
    @Test
    void deveRetornarCarroPorIdComSucesso() throws Exception {
        Carro carroSimulado = new Carro();
        carroSimulado.setId(1L);
        carroSimulado.setModelo("Corolla");
        carroSimulado.setMarca("Toyota");
        carroSimulado.setAno(2023);

        Mockito.when(carroService.findById(1L)).thenReturn(Optional.of(carroSimulado));

        mockMvc.perform(get("/carro/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelo").value("Corolla"))
                .andExpect(jsonPath("$.marca").value("Toyota"));
    }

    @Test
    void deveRetornarNotFoundQuandoCarroNaoExistir() throws Exception {
        Mockito.when(carroService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/carro/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    void deveCadastrarCarro() throws Exception {

        String json =
                """
                {
                  "marca":"Toyota",
                  "modelo":"Corolla",
                  "ano":2023
                }
                """;

        Carro carro = new Carro();

        carro.setId(1L);
        carro.setMarca("Toyota");
        carro.setModelo("Corolla");
        carro.setAno(2023);

        Mockito.when(carroService.save(Mockito.any()))
                .thenReturn(carro);

        mockMvc.perform(
                        post("/carro/salvar")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.marca").value("Toyota"))
                .andExpect(jsonPath("$.modelo").value("Corolla"))
                .andExpect(jsonPath("$.ano").value(2023));
    }

    @Test
    void naoDevePermitirXss() throws Exception {

        String json =
                """
                {
                 "marca":"<script>alert('XSS')</script>",
                 "modelo":"Corolla",
                 "ano":2023
                }
                """;

        mockMvc.perform(
                        post("/carro/salvar")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))

                .andExpect(status().isBadRequest());
    }

    @Test
    void naoDevePermitirAnoInvalido() throws Exception {

        String json =
                """
                {
                 "marca":"Toyota",
                 "modelo":"Corolla",
                 "ano":1000
                }
                """;

        mockMvc.perform(
                        post("/carro/salvar")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))

                .andExpect(status().isBadRequest());
    }
    @Test
    void deveAtualizarCarro() throws Exception {

        String json =
                """
                {
                  "marca":"Toyota",
                  "modelo":"Corolla",
                  "ano":2024
                }
                """;

        Carro carro = new Carro();
        carro.setId(1L);
        carro.setMarca("Toyota");
        carro.setModelo("Corolla");
        carro.setAno(2024);

        Mockito.when(
                        carroService.update(Mockito.any()))
                .thenReturn(carro);

        mockMvc.perform(
                        put("/carro/1")
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id")
                                .value(1))
                .andExpect(
                        jsonPath("$.marca")
                                .value("Toyota"))
                .andExpect(
                        jsonPath("$.modelo")
                                .value("Corolla"))
                .andExpect(
                        jsonPath("$.ano")
                                .value(2024));
    }

    @Test
    void deveExcluirCarro() throws Exception {

        Mockito.doNothing()
                .when(carroService)
                .deleteById(1L);

        mockMvc.perform(
                        delete("/carro/1"))
                .andExpect(
                        status().isNoContent());
    }
}