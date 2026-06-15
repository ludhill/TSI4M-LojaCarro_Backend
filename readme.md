# LojaCarro 🚗

Sistema desenvolvido em Java com Spring Boot para gerenciamento de uma loja de carros.

## Tecnologias Utilizadas

* Java 17
* Spring Boot 3.5.7
* Spring Web
* Spring Data JPA
* Spring Validation
* Spring Security
* JWT (JSON Web Token)
* MySQL
* H2 Database (Testes)
* JUnit 5
* Mockito
* PIT Mutation Testing
* Maven

---

# Pré-requisitos

Antes de executar o projeto, instale:

## Java 17

Verificar instalação:

```bash
java -version
javac -version
```

Deve aparecer algo semelhante:

```bash
openjdk version "17"
```

---

## Maven

O projeto utiliza Maven Wrapper (`mvnw`), portanto não é necessário instalar o Maven globalmente.

Verificar:

Windows:

```powershell
./mvnw -v
```

Linux:

```bash
./mvnw -v
```

---

## Banco de Dados

### Produção

MySQL 8+

Configurar:

* URL do banco
* Usuário
* Senha

em:

```properties
src/main/resources/application.properties
```

Exemplo:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/lojacarro
spring.datasource.username=root
spring.datasource.password=123456
spring.jpa.hibernate.ddl-auto=update
```

---

### Testes

Os testes utilizam:

* H2 Database (em memória)
* Mockito
* Spring Test

Não é necessário instalar o H2.

---

# Clonar o Projeto

```bash
git clone <url-do-repositorio>
cd LojaCarro
```

---

# Instalar Dependências

Windows:

```powershell
./mvnw clean install
```

Linux:

```bash
./mvnw clean install
```

---

# Executar o Projeto

Windows:

```powershell
./mvnw spring-boot:run
```

Linux:

```bash
./mvnw spring-boot:run
```

A aplicação iniciará normalmente na porta:

```text
http://localhost:8080
```

---

# Executar Testes Unitários

Executa todos os testes JUnit e Mockito:

Windows:

```powershell
./mvnw clean test
```

Linux:

```bash
./mvnw clean test
```

O comando:

1. Limpa arquivos compilados antigos (`clean`);
2. Compila novamente o projeto;
3. Executa todos os testes em `src/test/java`;
4. Gera os relatórios de testes.

Exemplo de saída:

```text
BUILD SUCCESS
Tests run: X
Failures: 0
Errors: 0
Skipped: 0
```

---

# Executar Mutation Testing (PIT)

Windows:

```powershell
./mvnw pitest:mutationCoverage
```

Linux:

```bash
./mvnw pitest:mutationCoverage
```

O PIT realiza:

1. Criação de mutações no código;
2. Alteração de operadores;
3. Alteração de retornos;
4. Alteração de condicionais;
5. Execução automática dos testes;
6. Verificação se os testes detectam as alterações.

Métricas geradas:

* Line Coverage
* Mutation Coverage
* Test Strength
* Mutants Killed
* Mutants Survived
* No Coverage

---

# Relatórios do PIT

Após a execução:

```text
target/pit-reports/
```

Abrir:

```text
target/pit-reports/index.html
```

No navegador.

---

# Estrutura do Projeto

```text
src
├── main
│   ├── java
│   │   └── br.org.edu.ifrn.LojaCarro
│   │       ├── controllers
│   │       ├── services
│   │       ├── model
│   │       ├── repository
│   │       └── security
│   └── resources
│
└── test
    └── java
        └── br.org.edu.ifrn.LojaCarro
            ├── controllers
            └── services
```

---

# Testes Implementados

## CarroControllerTest

* GET /carro
* GET /carro/{id}
* POST /carro/salvar
* Validação de dados
* Validação contra XSS
* Retorno 404

## CarroServiceTest

* save()
* findAll()
* findById()
* update()
* deleteById()

---

# Comandos Mais Utilizados

Compilar:

```bash
./mvnw compile
```

Executar:

```bash
./mvnw spring-boot:run
```

Testar:

```bash
./mvnw clean test
```

Mutation Testing:

```bash
./mvnw pitest:mutationCoverage
```

Empacotar:

```bash
./mvnw package
```

Gerar JAR:

```bash
./mvnw clean package
```

Executar JAR:

```bash
java -jar target/LojaCarro-0.0.1-SNAPSHOT.jar
```
