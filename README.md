# Delivery Tech API

Sistema de delivery desenvolvido com **Spring Boot** e **Java 21**.

---

## 🚀 Tecnologias Utilizadas
- **Java 21 (LTS)**
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- Spring Cache (ConcurrentMapCache por padrão; Redis em container)
- (opcional) Spring Boot Actuator para /health e /info
- H2 (memória) — apenas dev/test local
- MySQL 8 — em containers
- Redis 7 — cache distribuído em containers
- Maven (com Maven Wrapper mvnw)
- Docker/Podman


---

## 🛠 Recursos Modernos
- Records (Java 14+)
- Text Blocks (Java 15+)
- Pattern Matching (Java 17+)
- Virtual Threads (Java 21)

---

## ▶️ Como executar a aplicação
1. **Pré-requisitos:** JDK 21 instalado
2. Clone o repositório:
   ```bash
   git clone https://github.com/lcs21/delivery-api.git
   cd delivery-api

---
## 📋 Endpoints
- GET /health - Status da aplicação (inclui versão Java)
- GET /info - Informações da aplicação
- GET /h2-console - Console do banco H2

---
## 🧪 Testes
- Comandos para rodar: ./mvnw test
- Opção de relatórios (Surefire) e cobertura (JaCoCo) — com instrução de como habilitar
- Exemplo de teste de integração com MockMvc (JUnit 5 + Spring Boot Test)

---
## 🧭 Estratégia de Testes Adotada
- Unitários (JUnit + Mockito)
- Integração (Spring Boot Test + MockMvc, usando H2) [br-prod.as...rosoft.com]
- Repositórios (Data JPA Test)
- E2E (REST Assured – opcional)
- Metas de cobertura (≥ 80% em domínio/serviços)

---
## 🏗️ Arquitetura & Perfis
- Local (sem containers)
- Usa H2 em memória (configurado em application.properties).
- Cache simples (spring.cache.type=simple usando ConcurrentMapCache).
- Containers (Docker/Podman)
- Usa MySQL (datasource sobrescrito por variáveis de ambiente).
- Cache Redis (habilitado por variáveis de ambiente).

---
## 🔧 Configuração
- Porta: 8080
- Banco: H2 em memória
- Profile: development

---
## 👨‍💻 Desenvolvedor
Lucas Affonso | 12624210495 - Arquitetura de sistemas API REST Full com Java Springboot

Desenvolvido com JDK 21 e Spring Boot 3.2.x