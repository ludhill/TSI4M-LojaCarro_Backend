package br.org.edu.ifrn.lojacarro.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "usuario_auditoria")
@SuppressWarnings("unused") // Silencia avisos de métodos não utilizados diretamente no código
public class UsuarioAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_usuario")
    private Long idUsuario;

    private String nome;
    private String acao;
    private LocalDateTime timestamp;

    public UsuarioAudit() {}

    public UsuarioAudit(Long idUsuario, String nome, String acao) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.acao = acao;
        // Especifica o fuso horário da máquina explicitamente para remover o aviso do .now()
        this.timestamp = LocalDateTime.now(ZoneId.systemDefault());
    }

    public Long getId() { return id; }
    public Long getIdUsuario() { return idUsuario; }
    public String getNome() { return nome; }
    public String getAcao() { return acao; }
    public LocalDateTime getTimestamp() { return timestamp; }
}