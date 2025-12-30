# JavaJobs - Buscador de Vagas Java

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-green.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)
[![Render](https://img.shields.io/badge/Deployment-Render-blue.svg)](https://render.com/)

> Sistema educacional para busca e sincronização de oportunidades de emprego Java, desenvolvido com Spring Boot 4.0.1 e hospedado na plataforma Render com banco de dados Neon PostgreSQL.

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Configuração](#configuração)
- [API Endpoints](#api-endpoints)
- [Estrutura de Dados](#estrutura-de-dados)
- [Licença](#licença)
- [Autor](#autor)

## 🚀 Sobre o Projeto

O **JavaJobs** é uma aplicação educacional que demonstra o desenvolvimento de uma API REST completa com Spring Boot, integrando-se à [Adzuna API](https://developer.adzuna.com/) para buscar oportunidades de emprego Java. O projeto foi projetado para ser um exemplo prático de arquitetura de software moderna, com foco em boas práticas, performance e escalabilidade.

### 🎯 Objetivos Educacionais
- Demonstração de arquitetura hexagonal e clean architecture
- Uso de Spring Boot 4.0.1 com recursos modernos
- Integração com APIs externas
- Gerenciamento de banco de dados com JPA/Hibernate
- Implementação de camadas de serviço e repositório
- Tratamento de erros e validação
- Deploy em ambiente cloud (Render + Neon)

## ✨ Funcionalidades

### Principais
- ✅ **Busca Automática**: Sincroniza vagas de múltiplos países (Brasil, EUA, Reino Unido)
- ✅ **Filtragem Avançada**: Busca por título, empresa, tecnologia, país e tipo de regime
- ✅ **Armazenamento Local**: Persistência de dados em PostgreSQL (Neon)
- ✅ **Interface Web**: Frontend responsivo com HTML, CSS e JavaScript puro
- ✅ **Sincronização Inteligente**: Atualiza apenas vagas novas ou modificadas
- ✅ **Dashboard**: Visualização de status da sincronização e última atualização

### Recursos Técnicos
- 🔄 **Virtual Threads**: Performance aprimorada com Java 21
- 🗄️ **Database Migrations**: Validação de esquema com Hibernate
- 🔒 **Type Safety**: Uso extensivo de Records e Lombok
- 🎯 **Error Handling**: Global Exception Handler com respostas padronizadas

## 🛠️ Tecnologias

### Backend
- **Java 21** - Linguagem principal com virtual threads
- **Spring Boot 4.0.1** - Framework principal
  - Spring Web MVC
  - Spring Data JPA
  - Spring Validation
  - Spring DevTools (desenvolvimento)
- **PostgreSQL** - Banco de dados relacional
- **Lombok** - Redução de boilerplate
- **Maven** - Gerenciamento de dependências

### Frontend
- **HTML5** - Estrutura semântica
- **CSS3** - Estilização responsiva
- **JavaScript ES6+** - Lógica de interface
- **Google Fonts** - Tipografia (Inter)

### Infraestrutura
- **Render** - Hospedagem da aplicação
- **Neon** - Banco de dados PostgreSQL serverless
- **Docker** - Containerização para build
- **Adzuna API** - Fonte de dados de vagas

## 🏗️ Arquitetura

```
buscador-java/
├── src/main/java/com/vitorsaucedo/buscadorjava/
│   ├── client/           # Client HTTP para Adzuna API
│   ├── config/           # Configurações do Spring
│   ├── controller/       # Endpoints REST API
│   ├── dto/              # Data Transfer Objects
│   ├── exception/        # Tratamento global de erros
│   ├── mapper/           # Conversores de entidade/DTO
│   ├── model/            # Entidades JPA
│   ├── repository/       # Interfaces de persistência
│   └── service/          # Lógica de negócio
├── src/main/resources/
│   ├── static/           # Frontend (HTML, CSS, JS)
│   ├── templates/        # Templates (se necessário)
│   └── application*.properties
├── Dockerfile
└── pom.xml
```

## ⚙️ Configuração

### Pré-requisitos
- Java 21 JDK
- Maven 3.9+

### Variáveis de Ambiente

#### Desenvolvimento (`application.properties`)
```properties
spring.application.name=buscadorjava
spring.datasource.url=jdbc:postgresql://localhost:5432/SUA_DATABASE
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
spring.jpa.hibernate.ddl-auto=update

adzuna.api.base-url=https://api.adzuna.com/v1/api
adzuna.api.id=INSIRA_SEU_ID_AQUI
adzuna.api.key=INSIRA_SUA_KEY_AQUI

spring.threads.virtual.enabled=true
```

### Instalação Local

1. **Clone o repositório:**
```bash
git clone <repo-url>
cd buscador-java
```

2. **Configure o arquivo de propriedades:**
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
# Edite o arquivo com suas credenciais
```

3. **Build e execução:**
```bash
# Limpar e compilar
mvn clean package

# Executar
mvn spring-boot:run

# Ou executar o JAR
java -jar target/buscadorjava-0.0.1-SNAPSHOT.jar
```

4. **Acessar aplicação:**
- Frontend: `http://localhost:8080`
- API: `http://localhost:8080/api/jobs`

## 📡 API Endpoints

### `GET /api/jobs`
Retorna todas as vagas sincronizadas e status da última sincronização.

**Resposta:**
```json
{
  "jobs": [
    {
      "title": "Desenvolvedor Java Pleno",
      "companyName": "Tech Company",
      "locationName": "São Paulo, BR",
      "countryCode": "br",
      "description": "Vaga para desenvolvedor...",
      "technologies": "Java, Spring, SQL",
      "salaryRange": "R$ 8.000 - R$ 12.000",
      "jobType": "REMOTE",
      "url": "https://..."
    }
  ],
  "lastSync": "2024-01-15T10:30:00"
}
```

### `POST /api/jobs/fetch`
Aciona a sincronização manual com a Adzuna API.

**Resposta:** Mesmo formato de `GET /api/jobs`

## 📊 Estrutura de Dados

### Tabela `tb_jobs`
```sql
CREATE TABLE tb_jobs (
    id BIGSERIAL PRIMARY KEY,
    external_id VARCHAR(255) UNIQUE,
    title VARCHAR(255),
    description TEXT,
    company_name VARCHAR(255),
    location_name VARCHAR(255),
    country_code VARCHAR(10),
    url VARCHAR(512),
    salary_min DECIMAL(10,2),
    salary_max DECIMAL(10,2),
    contract_type VARCHAR(50),
    technologies VARCHAR(500),
    job_type VARCHAR(20),
    created_at TIMESTAMP,
    fetched_at TIMESTAMP
);
```

### Tabela `tb_sync_metadata`
```sql
CREATE TABLE tb_sync_metadata (
    id VARCHAR(50) PRIMARY KEY,
    last_sync_timestamp TIMESTAMP
);
```

## 📄 Licença

Este projeto é educacional e pode ser usado livremente para aprendizado e referência.

[![MIT License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## 👨‍💻 Autor

<div align="center">

### Vitor Saucedo
Desenvolvedor Java/Backend

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/vitor-saucedo-uggeri-641868365/)
[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/vitorsaucedo)
[![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:vitorsaucedo18@outlook.com)

</div>

---

<div align="center">

**Projeto Educacional** | **Spring Boot** | **Java 21** | **Render + Neon**

</div>