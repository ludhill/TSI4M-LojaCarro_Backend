package br.org.edu.ifrn.lojacarro.repository;

import br.org.edu.ifrn.lojacarro.model.Carro;
import org.javers.spring.annotation.JaversSpringDataAuditable; // Import do JaVers
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@JaversSpringDataAuditable // Ativa a auditoria automática de criação, edição e exclusão de carros
public interface CarroRepository extends JpaRepository<Carro, Long> {

}