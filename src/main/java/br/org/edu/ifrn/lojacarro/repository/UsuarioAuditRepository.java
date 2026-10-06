package br.org.edu.ifrn.lojacarro.repository;

import br.org.edu.ifrn.lojacarro.model.UsuarioAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioAuditRepository extends JpaRepository<UsuarioAudit, Long> {
}