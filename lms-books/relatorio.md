# ODSOFT24-25_1201152_1230205
Desenvolvimento do trabalho de ODSOFT 24/25

Relatório Parte 1


# Índice

- [Development of CI/CD/CD pipeline (description and argumentation)](#1-development-of-cicd-pipeline)
  - [1.1 - Development of CI/CD/CD pipeline (description and argumentation)](#11---jenkins-deployment-on-localhost-and-deis-remote-servers)
  - [1.2 - Independent CI/CD/CD pipeline per microservice](#12---cicd-pipeline-stages-not-only-running-but-also-description-and-critical-analysis)
  - [1.3 - Consumer-Driven Contract Tests](#13-pipeline-specification-read-checkout-from-scm)
  - [1.4 - Container image build](#14-build-and-package)
  - [1.5 - Container image push to repository](#15-static-code-analysis)
  - [1.6 - Deployment on the Docker service on other DEl's virtual servers (or other cloud's remote servers)
    ](#16-tests-by-type)

- [2 DDeployment process (description and argumentation)](#2-improvement-of-automated-functional-software-tests-including-not-only-coverage-but-also-quality)
  - [2.1 - Adoption of Development, Testing and Production environments](#21---unit-opaque-box-tests-domain-classes)
  - [2.2 - Deployment is decided by the human trigering the pipeline after receiving the email or other type of messa](#22---unit-transparent-box-tests-domain-classes)
  - [2.3 - Automatically roll-back each service to a previous version](#23-mutation-tests-domain-classes)
  - [2.4 - Tests are run against the container](#24---integration-tests-controller-service-domain-repository-gateways-adapters)
  - [2.5 - Acceptance tests](#25---acceptance-tests)
  - [2.6 - Scale up and down each service](#25---acceptance-tests)
  - [2.7 - Performance tests](#25---acceptance-tests)
  - [2.8 - System do not have a downtime when updating a service](#25---acceptance-tests)



## 1. Desenvolvimento da Pipeline CI/CD/CD

### 1.1 Descrição e Argumentação

A pipeline CI/CD/CD (Integração Contínua, Entrega Contínua e Desdobramento Contínuo) automatiza o processo de entrega de software, garantindo:

- Integração de alterações no código com frequência.
- Testes rigorosos para validação.
- Desdobramentos ágeis e sem interrupções.

**Benefícios:**

- Minimiza intervenção humana.
- Reduz erros.
- Assegura consistência no desdobramento.

### 1.2 Pipeline CI/CD/CD Independente para Cada Microserviço

Cada microserviço tem sua própria pipeline, permitindo:

- Testes e desdobramentos independentes.
- Concentração dos programadores em serviços específicos.
- Redução de impacto entre serviços.

Essa abordagem promove agilidade e escalabilidade alinhadas à arquitetura de microserviços.

### 1.3 Testes Baseados em Contratos de Consumidor

Os testes baseados em contratos (CDC) verificam que os serviços atendem às expectativas dos consumidores.

**Vantagens:**

- Garante compatibilidade entre serviços.
- Reduz problemas de integração.
- Permite desenvolvimento paralelo.

### 1.4 Construção de Imagem de Contêiner 

A pipeline automatiza a criação de imagens de contêiner com:

- Estrutura e dependências definidas em Dockerfiles.
- Configurações consistentes para reprodutibilidade.
- Tags para rastreabilidade de versões.

### 1.5 Publicação de Imagens de Contêiner num Repositório

As imagens de contêiner são publicadas em repositórios centralizados e seguros, como Docker Hub ou registros privados, garantindo:

- Disponibilidade para desdobramento.
- Controle de versão.
- Reversão confiável.

### 1.6 Desdobramento em Serviço Docker em Servidores Virtuais ou Remotos

Os contêineres são desdobrados em:

- Servidores virtuais no DEI.
- Fornecedores de cloud, como AWS, GCP ou Azure.

Configurações de desdobramento incluem:

- Alocação de recursos.
- Variáveis de ambiente e segredos.
- Configurações de rede.

---

## 2. Processo de Desdobramento

### 2.1 Adoção de Ambientes de Desenvolvimento, Testes e Produção

A pipeline opera em três ambientes distintos:

- **Desenvolvimento:** Para codificação e testes iniciais.
- **Testes:** Para validação de integração, contratos e desempenho.
- **Produção:** Para desdobramentos em ambiente real.

Essa separação garante estabilidade na produção.

### 2.2 Desdobramento Decidido por Ação Humana

Desdobramentos são iniciados manualmente após a aprovação humana, garantindo:

- Validação do momento do desdobramento.
- Prevenção de desdobramentos acidentais.

### 2.3 Reversão Automática para Versão Anterior

A pipeline inclui reversão automática para a última versão estável em caso de falhas, com:

- Interrupção do contêiner com falhas.
- Redesdobramento da imagem anterior.
- Notificação aos intervenientes.

### 2.4 Testes Executados nos Contêiners

Testes abrangentes são executados nos contêineres:

- Unitários, de integração, funcionais e de segurança.

Isso garante que o contêiner está pronto para produção.

### 2.5 Escalabilidade de Serviços

A pipeline suporta escalabilidade dinâmica, como:

- **Horizontal:** Adicionar instâncias de um serviço.
- **Vertical:** Alocar mais recursos para uma instância.

### 2.6 Testes de Desempenho

Os testes de desempenho avaliam o sistema sob diferentes condições de carga, analisando:

- Tempo de resposta.
- Taxa de transferência.
- Utilização de recursos.

### 2.7 Atualizações Sem Tempo de Inatividade

Atualizações utilizam estratégias como blue-green ou rolling updates, assegurando:

- Zero tempo de inatividade.
- Disponibilidade contínua do serviço.

---

## Conclusão

A pipeline CI/CD/CD proposta estabelece um processo robusto, automatizado e escalável, alinhado às práticas modernas de DevOps. Ela garante desdobramentos rápidos, confiáveis e de alta qualidade, promovendo a eficiência no desenvolvimento e na entrega de software.
