package br.org.edu.ifrn.LojaCarro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Carro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "O modelo é obrigatório")
    @Size(min = 2, max = 50, message = "O modelo deve ter entre 2 e 50 caracteres")
    @Pattern(
            regexp = "^[a-zA-ZÀ-ÿ0-9\\s-]+$",
            message = "Modelo inválido!"
    )
    private String modelo;

    @NotBlank(message = "A marca é obrigatória")
    @Size(min = 2, max = 50, message = "A marca deve ter entre 2 e 50 caracteres")
    @Pattern(
            regexp = "^[a-zA-ZÀ-ÿ0-9\\s-]+$",
            message = "Marca inválida"
    )
    private String marca;

    @Min(value = 1886, message = "O ano deve ser maior que 1886")
    @Max(2100)
    private int ano;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }
}