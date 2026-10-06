package br.org.edu.ifrn.lojacarro.event;

import br.org.edu.ifrn.lojacarro.model.UsuarioAudit;
import br.org.edu.ifrn.lojacarro.repository.UsuarioAuditRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UsuarioAuditListener {

    private final UsuarioAuditRepository auditRepository;

    public UsuarioAuditListener(UsuarioAuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @EventListener
    public void processarAuditoria(UsuarioAlteradoEvent event) {
        UsuarioAudit audit = new UsuarioAudit(event.getIdUsuario(), event.getNome(), event.getAcao());
        auditRepository.save(audit);
    }
}