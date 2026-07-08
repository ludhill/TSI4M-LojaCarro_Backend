# **LojaCarro Backend \- Sistema de Gerenciamento de Veículos**

Este projeto é uma API REST robusta desenvolvida em **Java** com o framework **Spring Boot**, projetada sob os princípios da engenharia de software moderna. A aplicação conta com um CRUD completo de veículos, controle estrito de segurança, autenticação baseada em tokens **JWT**, limitação de requisições de tráfego (*Rate Limiting*) e uma pirâmide abrangente de testes automatizados (unitários, de integração, de mutação e cobertura).

## **📋 Status do Checklist da Atividade**

Abaixo está o mapeamento completo dos requisitos obrigatórios da atividade e suas respectivas implementações:

| Item | Requisito Solicitado | Status | Implementação Técnica |
| :---- | :---- | :---- | :---- |
| **1** | Banco de Dados Configurado | **Concluído** ✅ | MySQL em produção (application.properties) e H2 em memória para isolamento de testes (@ActiveProfiles("test")). |
| **2** | CRUD de Carros Completo | **Concluído** ✅ | Endpoints funcionais para Salvar, Buscar, Listar, Atualizar e Excluir veículos. |
| **3** | Testes Unitários \+ JaCoCo | **Concluído** ✅ | Cobertura de código validada pelo plugin do JaCoCo garantindo estabilidade ![][image1]. |
| **4** | Testes de Integração | **Concluído** ✅ | Suítes de testes de fluxo de ponta a ponta na camada de banco de dados e rotas HTTP. |
| **5** | Testes de Segurança | **Concluído** ✅ | Garantia de bloqueio e descarte de requisições malformadas ou anônimas em endpoints privados. |
| **6** | Autenticação & Autorização (JWT) | **Concluído** ✅ | Filtro JwtFilter com validação de assinatura e atribuição automática do perfil ROLE\_GERENTE ao login root. |
| **7** | Limitação de Chamadas (*Rate Limiting*) | **Concluído** ✅ | Integração do algoritmo *Token Bucket* via **Bucket4j**, bloqueando abusos com status 429 Too Many Requests. |

## **🛠️ Tecnologias e Dependências Utilizadas**

* **Java 17** (Versão base de compilação)
* **Spring Boot 3.5.7** (Framework Web e de Injeção de Dependências)
* **Spring Security** (Controle de filtros de segurança e rotas)
* **JWT (JSON Web Token \- JJWT 0.11.5)** (Geração e decodificação de tokens seguros)
* **Bucket4j** (Controle e interceptação de tráfego de rede)
* **MySQL Connector J** (Driver JDBC de produção)
* **H2 Database** (Banco de dados SQL volátil de alto desempenho para testes)
* **JaCoCo** (Medição de cobertura de código \- Cobertura mínima de ![][image2])
* **Pitest (PIT)** (Engenharia de testes de mutação para validação de asserções)

## **🗺️ Estrutura de Pacotes do Projeto**

LojaCarro  
├── src  
│   ├── main  
│   │   ├── java  
│   │   │   └── br.org.edu.ifrn.lojacarro  
│   │   │       ├── controllers  
│   │   │       │   ├── CarroController.java  
│   │   │       │   └── AuthController.java             \<-- Endpoint de login e geração de Token  
│   │   │       ├── models  
│   │   │       │   └── Carro.java  
│   │   │       ├── repositories  
│   │   │       │   └── CarroRepository.java  
│   │   │       ├── services  
│   │   │       │   └── CarroService.java  
│   │   │       ├── config  
│   │   │       │   ├── SecurityConfig.java             \<-- Configuração de filtros HTTP do Spring Security  
│   │   │       │   ├── WebMvcConfig.java               \<-- Registro global do interceptor de tráfego  
│   │   │       │   └── RateLimitInterceptor.java       \<-- Algoritmo dinâmico de Rate Limiting  
│   │   │       ├── security  
│   │   │       │   ├── JwtUtil.java                    \<-- Utilitário de codificação e decodificação JWT  
│   │   │       │   └── JwtFilter.java                  \<-- Interceptor e validador de requisições de segurança  
│   │   │       └── LojaCarroApplication.java  
│   │   └── resources  
│   │       └── application.properties                  \<-- Configurações de banco MySQL e limites  
│   └── test  
│       └── java  
│           └── br.org.edu.ifrn.lojacarro  
│               ├── controllers  
│               │   └── CarroControllerTest.java        \<-- Testes mockados de API  
│               ├── services  
│               │   └── CarroServiceTest.java           \<-- Testes unitários de serviços  
│               ├── CarroIntegrationTest.java           \<-- Testes de integração de banco de dados  
│               └── SecurityIntegrationTest.java        \<-- Testes de segurança, JWT e estouro de Rate Limit

## **🚀 Como Executar o Projeto Localmente**

### **Pré-requisitos**

* Ter o **Java 17** (JDK) instalado localmente.

### **Passo 1: Clonar o Projeto**

git clone \<URL\_DO\_REPOSITORIO\>  
cd LojaCarro

### **Passo 2: Executar com Banco de Dados em Memória (H2 \- Recomendado)**

Se você não deseja ligar o MySQL local para rodar a aplicação, pode iniciá-la utilizando o perfil de testes, que criará e populará o banco H2 dinamicamente:  
\# No PowerShell (Windows):  
.\\mvnw spring-boot:run \-Dspring-boot.run.profiles=test

\# No CMD (Windows):  
mvnw spring-boot:run \-Dspring-boot.run.profiles=test

### **Passo 3: Executar com Banco de Dados de Produção (MySQL)**

Certifique-se de que o seu MySQL está ativo e crie um esquema com o nome de sua preferência. Em seguida, configure as credenciais no arquivo src/main/resources/application.properties:  
spring.datasource.url=jdbc:mysql://localhost:3306/seu\_nome?createDatabaseIfNotExist=true  
spring.datasource.username=root  
spring.datasource.password=root

E inicie a aplicação normalmente:  
.\\mvnw spring-boot:run

## **🧪 Como Rodar as Suítes de Testes e Ferramentas de Qualidade**

Para garantir que todas as implementações foram robustamente testadas, execute os comandos do Maven abaixo em seu terminal do PowerShell (certifique-se de que a variável JAVA\_HOME está apontando para o seu JDK 17):

### **1\. Rodar os 19 Testes de Integração e Segurança:**

Este comando limpa compilações anteriores e executa todas as classes de testes, validando rotas, segurança e limites de rede:  
.\\mvnw clean test

### **2\. Medir a Cobertura de Código (JaCoCo):**

O JaCoCo analisará os testes executados. Caso a cobertura de código seja **inferior a ![][image2]**, o build falhará automaticamente graças às regras configuradas no pom.xml:  
.\\mvnw jacoco:report

* **Onde visualizar o relatório:** Abra o arquivo target/site/jacoco/index.html em qualquer navegador.

### **3\. Rodar Testes de Mutação (Pitest):**

Este teste insere falhas intencionais (mutações) no código de produção para validar se a sua suíte de testes consegue matá-los:  
.\\mvnw pitest:mutationCoverage

* **Onde visualizar o relatório:** Abra o arquivo na pasta target/pit-reports/YYYYMMDDHHMM/index.html em qualquer navegador.

## **📦 Como Gerar o Arquivo .jar para Produção (AWS)**

Para empacotar a aplicação completa (com o servidor Tomcat embutido e todas as dependências) em um único arquivo executável para deploy na nuvem da AWS (Elastic Beanstalk, EC2, etc.):

1. Execute o comando de empacotamento:  
   .\\mvnw clean package

2. O arquivo será gerado na pasta:  
   target/LojaCarro-0.0.1-SNAPSHOT.jar

⚠️ **IMPORTANTE PARA IMPLANTAÇÃO NA AWS:**  
Ao configurar o seu ambiente na AWS, selecione **obrigatoriamente a plataforma Java 17** (ex: *Amazon Corretto 17*). O Spring Boot 3.x não aceita Java 8 e falhará com erro wrong version 61.0, should be 52.0 se a versão configurada na nuvem for obsoleta.

[image1]: <data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADQAAAAWCAYAAACPHL/WAAAAg0lEQVR4XmNgGAWjYBSMgpEAwoH4OhBnoEsMZSDEAPHQCSDugvKHDQB5DBRjw9JjoBhbCcTKaHJDGrgxDFOPgTz0mWGIewpWWAz5PAVyOMgDQ744HzYeAeULUB4BZX5QRTukAawUA9GjYBTQAYAKAlByIxYPegAqBEB5iFg8pCvWIQUAMtQf7cze4g0AAAAASUVORK5CYII=>

[image2]: <data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACYAAAAZCAYAAABdEVzWAAABsklEQVR4Xu3VYVHDQBCG4dOABTRgAQtYwAIWcIAEJOAABzjAAAIgz2S+stk2TTvDD37kndlpc7e3++3t5TLGzs4md5Pd9sHG1vyfcjPZ+2Qvk71N9ricPkD455j9z8LhaczBBL1fTh8wnqT8O89jFhYkj0BrGZ+vyR6K30lSZV38MY4TmxfQvDWvY/arVXuWOCiCAGacZQM24ShARf+/x29Sv0T11tiRKsSa+ux/3X1xejGr5Ex0JHEWQJDn3mJriQtdaBeWHbwIzpL6TSUWqyxo25ow44FfbVM9b9Ze1MKgbdokgYq1VsDsFrqAkPEITqvEIDKtv6qFlSqOqawGuVQYrMuLFIisLSRcm2vxR9QqWQTWCq8R1iGotpBIRpT1q7tosh5Yu5eEhCLPPciWsN5CYmocopNjQb8WKpLm8BJ+SsDaTgY7VdckTiU5FvQKKoLYcqhsTVh9eyvW9GvoYmEQuLYShHpDq5B+R/GRxEHv9BYGvlWYjqX4I/JBzVljgvaE/LwY5nPPrQXtLQyE5rOGfgGfhANHgnqlIVeBA7v2qhvvLayYV5SNOOe3s7Pzb/gBr9uOQRs/Kx8AAAAASUVORK5CYII=>