package br.org.edu.ifrn.lojacarro.event;

@SuppressWarnings("unused")
public class UsuarioAlteradoEvent {
    private final Long idUsuario;
    private final String nome;
    private final String acao;

    public UsuarioAlteradoEvent(Long idUsuario, String nome, String acao) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.acao = acao;
    }

    public Long getIdUsuario() { return idUsuario; }
    public String getNome() { return nome; }
    public String getAcao() { return acao; }
}