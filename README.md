# **Relatório Técnico: Atividades de Testes de Integração**

**Componente Curricular:** Desenvolvimento de Sistemas Backend / Engenharia de Software

**Projeto:** LojaCarro

**Responsável:** Equipe de Engenharia de Software

## **1\. O que é um teste de integração?**

Um **Teste de Integração** é uma etapa crítica da pirâmide de testes cujo objetivo é verificar se diferentes unidades de código, módulos, camadas ou sistemas externos de uma aplicação funcionam de forma harmônica quando acoplados de ponta a ponta.

Diferente dos testes unitários (que isolam completamente uma função ou classe usando dados fictícios denominados *Mocks*), o teste de integração atua de forma real e vertical. No ecossistema Spring Boot, isso significa que o framework carrega o contexto completo da aplicação (@SpringBootTest) e levanta um servidor web embutido (Tomcat) em uma porta de rede real e aleatória (webEnvironment \= SpringBootTest.WebEnvironment.RANDOM\_PORT). As requisições HTTP simuladas atravessam fisicamente os Controllers, disparam as regras nos Services e persistem as modificações no banco de dados.

## **2\. Status de Requisitos da Atividade**

Abaixo está o checklist de entrega exigido no PDF da atividade, comparando o planejado com o executado:

* **Operações Base do CRUD** (Salvar, Atualizar, Buscar, Listar, Excluir) \- **Concluído**
* **Configuração do Perfil de Testes** (Isolamento com @ActiveProfiles("test")) \- **Concluído**
* **Teste Adicional 1:** Buscar um ID inexistente (Retorno 404\) \- **Concluído**
* **Teste Adicional 2:** Salvar carro com modelo inválido (Rejeição ativa) \- **Concluído**
* **Teste Adicional 3:** Deletar um carro inexistente (Tratamento de exceção customizada) \- **Concluído**
* **Atividade Complementar:** Execução dos testes via pipeline no GitHub Actions \- **Concluído**
* **Atividade Complementar (Opcional):** Medição de cobertura com JaCoCo \- **Pendente**

## **3\. Quais componentes foram integrados neste projeto?**

Neste projeto de gerenciamento de veículos, os testes integraram de ponta a ponta as seguintes camadas arquiteturais:

* **Camada de Apresentação e Controle (CarroController):** Responsável por expor os endpoints REST (ex: /carro, /carro/salvar, /carro/{id}).
* **Camada de Serviço (CarroService):** Onde residem as regras de negócio do sistema (ex: validações de formatos e de presença).
* **Camada de Acesso a Dados / Persistência (CarroRepository via Spring Data JPA / Hibernate):** Responsável por traduzir as instruções orientadas a objetos da aplicação em instruções SQL puras (INSERT, SELECT, UPDATE, DELETE).
* **O Banco de Dados de Testes (H2 Database):** Um banco relacional rápido executado inteiramente na memória RAM do computador durante a execução dos testes. Ele é isolado do banco MySQL de produção graças ao uso do perfil de testes ativo (@ActiveProfiles("test")).

## **4\. Diferença entre Teste Unitário e Teste de Integração**

| Característica | Teste Unitário | Teste de Integração (Nossa Solução) |
| :---- | :---- | :---- |
| **Escopo** | Focado em uma única classe isolada | Integração física de múltiplas camadas |
| **Velocidade** | Extremamente rápido (milissegundos) | Mais lento (segundos) |
| **Uso de Mocks** | Essencial (Mockito/Mocks artificiais) | Evitado (Uso de componentes e banco real) |
| **Erros Encontrados** | Quebras de lógica e fluxos condicionais | Conexões de rede, sintaxe SQL, serialização JSON |

## **5\. Quais problemas esse tipo de teste ajuda a identificar?**

A suíte de testes de integração implementada ajudou a mitigar problemas silenciosos que poderiam causar grandes instabilidades em produção:

1. **Mapeamentos Incorretos entre Java e Banco de Dados (ORM):** Garante que o Hibernate consiga gerar as tabelas físicas associando atributos como modelo e ano da entidade Carro sem gerar erros de DDL.
2. **Incompatibilidade na Conversão JSON/DTO:** Valida que o payload enviado pelo cliente em formato de texto estruturado seja corretamente convertido e compreendido pela API.
3. **Deleções Fantasmas:** Garante que o sistema reaja de forma honesta quando recebe ordens de remoção absurdas ou impossíveis de serem atendidas, impedindo que o sistema retorne um falso sucesso (status 200 OK).
4. **Bugs de Regressão de Configuração:** Identifica rapidamente se o sistema está configurado de forma errada para rodar em nuvem (como tentar ler o banco local MySQL em vez do H2 em memória na nuvem do GitHub Actions).

## **6\. Nossa Solução: Detalhes dos Testes de Integração Executados**

Após o refinamento das validações e injeções de dependências, a nossa suíte de testes de integração atingiu estabilidade absoluta com **8/8 testes verdes no painel do JUnit**.

### **Exemplo 1: Validação de Formato ao Salvar (Modelo Vazio)**

O teste deveDarErroAoSalvarObjetoInvalido\_ValidacaoCamadaServico simula um cadastro inválido em que o atributo modelo é omitido ("").

Para fazer o teste passar de forma robusta e garantir que a aplicação não aceite dados corrompidos na base, implementamos a validação de segurança ativa dentro do **CarroService.java**:

public Carro save(Carro c) {  
// Validação ativa contra formatos inconsistentes de dados  
if (c \== null || c.getModelo() \== null || c.getModelo().trim().isEmpty()) {  
throw new IllegalArgumentException("O modelo do carro não pode ser vazio\!");  
}  
return carroRepository.save(c);  
}

### **Exemplo 2: Tratamento de Deleção de Registro Inexistente**

No **CarroService.java**, o método de deleção foi programado para validar a existência do ID no banco e lançar uma exceção de status HTTP amigável para o cliente:

public void deleteById(Long id) {  
// 1\. Verifica se o registro realmente existe antes de tentar deletar  
if (\!carroRepository.existsById(id)) {  
// 2\. Lança uma ResponseStatusException amigável com código HTTP 404  
throw new org.springframework.web.server.ResponseStatusException(  
org.springframework.http.HttpStatus.NOT\_FOUND,   
"Operação inválida: O carro com o ID " \+ id \+ " não existe na base de dados."  
);  
}  
// 3\. Se tudo estiver correto, apaga o registro  
carroRepository.deleteById(id);  
}

E no arquivo **CarroIntegrationTest.java**, o teste de integração valida esse comportamento capturando o código de retorno HTTP exato gerado por essa exceção:

@Test  
public void deveDarErro\_AoTentarDeletarCarroInexistente() {  
Long idInexistente \= 9999L; // ID simulado

    ResponseEntity\<String\> resposta \= restTemplate.exchange(  
            getBaseUrl() \+ "/" \+ idInexistente,  
            HttpMethod.DELETE,  
            null,  
            String.class  
    );

    // Valida que a nossa exceção no Service resultou em uma resposta 404 NOT FOUND para o cliente  
    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NOT\_FOUND);  
}

## **7\. Automação de Testes e Pipeline CI (GitHub Actions)**

O arquivo .github/workflows/ci.yml do projeto foi mapeado para orquestrar e validar a integridade do código remotamente.

Toda vez que a equipe realiza um git push para as branches principais, o GitHub cria uma máquina virtual temporária, instala o JDK 18 e o Maven, e executa:

mvn clean test

Como os nossos testes de integração utilizam o banco de dados **H2 em memória**, os 8 testes rodam e passam com sucesso dentro dos servidores do GitHub, sem necessitar de qualquer infraestrutura física externa, garantindo que o projeto só possa ser colocado em produção após a validação total das regras de negócio mapeadas neste relatório.

*Todos os dados de testes e estados do banco H2 são apagados automaticamente ao final da execução da suite de testes.*