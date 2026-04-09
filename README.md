# 🛠 API de Analise de Diagrama AI - Fase 4

[![Java](https://img.shields.io/badge/Java-17+-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.0-green?logo=spring)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14-blue?logo=postgresql)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-24.0+-blue?logo=docker)](https://www.docker.com/)
[![Kubernetes](https://img.shields.io/badge/Kubernetes-1.27+-blue?logo=kubernetes)](https://kubernetes.io/)
[![AWS](https://img.shields.io/badge/AWS-EKS-orange?logo=amazon-aws)](https://aws.amazon.com/eks/)
[![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-Automation-black?logo=githubactions)](https://github.com/thomaserick/archinsight-ai-api/actions/workflows/pipeline.yml)
[![New Relic](https://img.shields.io/badge/New_Relic-Monitoring-red?logo=newrelic)](https://newrelic.com/)
[![SonarQube](https://img.shields.io/badge/SonarQube-Quality_Gate-green?logo=sonarqube)](https://sonarcloud.io/dashboard)

API para analise de diagramas

## 🔗 Repositórios Relacionados

A arquitetura do **ArchInsight AI API** é composta por múltiplos módulos independentes, cada um versionado
em um repositório separado para facilitar a manutenção e o CI/CD.

| Módulo                           | Descrição                                                                                              | Repositório                                                                                 |
|:---------------------------------|:-------------------------------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------|
| 🧱 **Core Application**          | Aplicação principal responsável pelas regras de negócio, APIs REST e integração com os demais módulos. | [archinsight-ai-api](https://github.com/thomaserick/archinsight-ai-api)                     |
| ☸️ **Kubernetes Infrastructure** | Infraestrutura da aplicação no Kubernetes, incluindo manifests, deployments, ingress e autoscaling.    | [archinsight-ai-api-k8s-infra](https://github.com/thomaserick/archinsight-ai-k8s-infra) |
| 🗄️ **Database Infrastructure**  | Infraestrutura do banco de dados gerenciado (RDS PostgreSQL), versionada e automatizada via Terraform. | [archinsight-ai-api-db-infra](https://github.com/thomaserick/archinsight-ai-db-infra)   |
| 🗄️ **Analise AI **  | Microserviço responsavel por fazer o processamento de analise do diagrama via AI. | [archinsight-ai-analyzer]([https://github.com/thomaserick/archinsight-ai-db-infra](https://github.com/thomaserick/archinsight-ai-analyzer))   |

> 🔍 Cada repositório é autônomo, mas integra-se ao **Core** por meio de pipelines e configurações declarativas (
> Terraform e CI/CD).

## 📋 Índice

- [Tecnologias](#-tecnologias)
- [CI/CD Pipeline](#-cicd-pipeline--github-actions)
- [Kubernetes (EKS)](#-kubernetes-eks)
- [Monitoramento e Observabilidade](#-monitoramento-e-observabilidade-com-new-relic)
- [Instalação Local](#-instalação-local)
- [Instalação Aws](#-instalação-Aws)
- [Autenticação](#-autenticação)
- [Documentação APIs](#-documentação-da-api)
- [Documentação DDD](#-documentação-ddd)

## 🛠 Tecnologias

- **Java 17+** - Linguagem principal
- **Spring Boot 3.3** - Framework backend
- **Spring Security**
- **JWT**
- **JPA/Hibernate**
- **PostgreSQL** - Banco de dados
- **Docker** - Containerização
- **Flyway** - Migrações de banco
- **OpenAPI/Swagger** - Documentação APIs
- **Mockito** - Testes unitários
- **GitHub Actions** - Automação CI/CD
- **SonarQube** - Análise de qualidade e cobertura de código
- **Terraform** - Gerenciador de Infraestrutura IaC
- **Kubernetes (K8s)** - Deploy e escalonamento
- **AWS EKS** - Orquestração de containers
- **AWS RDS** - Banco de dados gerenciado (PostgreSQL)
- **AWS IAM** - Gerenciamento de permissões e segurança
- **AWS VPC** - Rede privada virtual
- **AWS EC2** - Instâncias de servidores
- **New Relic** - Monitoramento e observabilidade
- **RabbitMQ** - Gerenciamento de Mensageria

## 📝 Diagramas Tecnicos

![Diagrama de Componente](docs/assets/diagrama_componente.jpg)
![Diagrama de Cadastro Usuario](docs/assets/diagrama_sequencia_cadastro_usuario.jpg)
![Diagrama de Autenticacao](docs/assets/diagrama_sequencia_autenticacao.jpg)

## 🚀 Arquitetura

| Clean Architecture                                  |
|-----------------------------------------------------|
| ![Hexagonal](docs/assets/arquitetura-hexagonal.png) | ![Clean](docs/assets/clean-architecture.png) |

## ⚙️ Fluxo da Infraestrutura

![Terraform](docs/assets/terraform-fluxo-infra.jpg)

## 🚀 CI/CD Pipeline – GitHub Actions

Esta pipeline automatiza o processo de build, teste, análise, empacotamento e deploy da aplicação Gerenciador Oficina
Core.
Ela é executada automaticamente em eventos de push na branch main.

![Pipeline](docs/assets/ci-cd-fluxo-pipeline.jpg)

### Variaveis de Ambiente

A pipeline utiliza as seguintes variáveis de ambiente armazenadas como Secrets no GitHub:

| Nome                  | Descrição                                        |
|-----------------------|--------------------------------------------------|
| SONAR_TOKEN           | Token de autenticação para o SonarQube           |
| DOCKERHUB_USERNAME    | Nome de usuário do Docker Hub                    |
| DOCKERHUB_TOKEN       | Token de acesso do Docker Hub                    |
| AWS_ACCESS_KEY_ID     | Chave de acesso AWS                              |
| AWS_SECRET_ACCESS_KEY | Chave secreta AWS                                |
| NEW_RELIC_LICENSE_KEY | Chave de licença do New Relic para monitoramento |

### 🔨 Job: Build

Responsável por compilar o projeto e gerar o artefato `.jar`.

- Faz checkout do código fonte.
- Executa em um container Ubuntu com Java 17 e Maven pré-instalados.
- Executa o comando: - mvn -B clean package -DskipTests
- Faz upload do artefato gerado `(target/*.jar)` para ser reutilizado nos próximos jobs.

### ✅ Job: test

Executa os testes unitários:

- Faz checkout do código.
- Configura o Java 17.
- Executa `mvn test` para validar o código antes de seguir.

### 🔍 Job: SonarQube Analysis

Realiza a análise estática de código com o SonarQube:

- Faz checkout e configuração Java.
- Utiliza cache do SonarQube para otimizar execução.
- Executa:`
mvn -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
Dsonar.projectKey=archinsight-ai-api
`
- Autenticação via SONAR_TOKEN armazenado nos GitHub Secrets.

### 🐳 Job: docker

Cria e publica a imagem Docker da aplicação:

- Faz download do artefato .jar gerado no job Build.
- Faz login no Docker Hub usando secrets (DOCKERHUB_USERNAME e DOCKERHUB_TOKEN).
- Configura o ambiente Docker Buildx.
- Constrói e envia a imagem para o Docker Hub com as tags:
    - latest
    - run_number (versão incremental da execução da pipeline)
- Publica em: `docker.io/<usuario-dockerhub>/archinsight-ai-api`

### ☁️ Job: aws-deploy

Realiza o deploy automático no AWS EKS:

- Configura credenciais da AWS `(via AWS_ACCESS_KEY_ID_DEV e AWS_SECRET_ACCESS_KEY_DEV)`.
- Instala e configura o kubectl.
- Atualiza o kubeconfig para o cluster EKS
- Obtém automaticamente o endpoint do banco RDS e substitui no `ConfigMap`
- Executa o script `./devops/scripts/deploy-prod-k8s.sh
` para aplicar as configurações Kubernetes.

## ☸️ Kubernetes (EKS)

A pasta devops/k8s/prod contém os manifestos Kubernetes utilizados para implantar e gerenciar a aplicação no cluster
EKS (AWS).
Cada arquivo tem uma função específica dentro do fluxo de deploy e operação em produção.

### 📁 Estrutura

```plaintext
devops/
├─ k8s/
│   └─ prod/
│       ├─ configmap.yaml
│       ├─ deployment.yaml
│       ├─ hpa.yaml
│       ├─ namespace.yaml  
│       ├─ service.yaml
│       ├─ postgres-secret.yaml
│       └─ services.yaml
└─ scripts/
    └─ deploy-prod-k8s.sh
```

| Arquivo                  | Descrição                                                                                                                                                                                                  |
|--------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **namespace.yaml**       | Define o namespace onde os recursos da aplicação serão criados (isola o ambiente no cluster).                                                                                                              |
| **configmap.yaml**       | Contém variáveis de configuração da aplicação, incluindo o endpoint do RDS                                                                                                                                 |
| **postgres-secret.yaml** | Armazena de forma segura as credenciais de acesso ao banco de dados PostgreSQL (usuário e senha).                                                                                                          |
| **deployment.yaml**      | Define como o container da aplicação é executado — imagem Docker, réplicas, volumes e variáveis de ambiente.                                                                                               |
| **services.yaml**        | Expõe o deployment internamente ou externamente via LoadBalancer, tornando a aplicação acessível.                                                                                                          |
| **hpa.yaml**             | Configura o **Horizontal Pod Autoscaler**, responsável por escalar os pods automaticamente conforme CPU/memória.                                                                                           |
| **deploy-prod-k8s.sh**   | Script automatizado utilizado no pipeline de CI/CD para aplicar todos os manifests ( `kubectl apply -f`) no cluster EKS. Também atualiza o `ConfigMap` com o endpoint mais recente do RDS antes do deploy. |

## 📊 Monitoramento e Observabilidade com New Relic

Este projeto utiliza o New Relic para garantir observabilidade completa da aplicação, permitindo monitorar performance,
saúde, consumo de recursos e falhas operacionais em tempo real.

### Visão Geral (APM)

## ⚙️ Instalação Local

### Rodar o projeto local com Docker

#### Pré-requisitos

- Docker 24.0+
- Docker Compose 2.20+

#### Comandos

1. Suba os containers:

```bash
  docker-compose up 
```

### Rodar o projeto local com Kubernetes

### Rodar o projeto local

#### Pré-requisitos

- **Java** 17+
- **PostgreSQL** para banco de dados
- **Maven** para gerenciar as dependências do projeto

#### Comandos

1. Clone o repositório

   SSH

    ```
    git@github.com:thomaserick/archinsight-ai-api.git
    ```
   Http
    ```
    https://github.com/thomaserick/archinsight-ai-api.git
    ```
2. Configure o Banco de Dados
   ```
    psql -U postgres
    CREATE DATABASE archinsight-ai-api;
   ```
3. Configura o profile como `dev`

    ```
    spring.profiles.active=dev
    ```
4. Adicionar o agent do newrelic na execução da aplicação

    ```
    -javaagent:pathCompleto/archinsight-ai-api/newrelic/newrelic.jar
    ```
5. Configurar a key do newRelic ${NEW_RELIC_LICENSE_KEY} nas variaveis de ambiente da aplicação

O sistema rodará na porta `localhost:8081`.

## ⚙️ Instalação AWS

#### Pré-requisitos

- Docker 24.0+
- Terraform v1.13+
- AWS CLI v2+
- Kubectl v1.27+

Para subirmos todo o seviço na AWS para gerenciar nossa aplicação, precisamos executar alguns
passos para primeiro subir toda nossa infraestrura para AWS e depois executar
o CI/CD pelo github Actions pra fazer o deploy da aplicação.

### Rodar o projeto AWS

#### Comandos

1. Crie um usuario na AWS que contenha a policy AdministratorAccess
2. Gere as Secrets AWS-ACCESS-KEY-ID e AWS-SECRET-ACCESS-KEY (Guarde em um local seguro)
3. Autenticar o usuario pelo AWS CLI

  ```
    aws configure
    
    Ex: AWS 
      Access Key ID [****************2VXT]: 
      AWS Secret Access Key [****************B9uz]: 
      Default region name [us-east-1]: 
      Default output format [json]:
  ```

4. Rodaremos o commando para subir toda nossa infraestrutura no servidor da `AWS EKS` para orquestação de containers
   e RDS `postgres` para banco de dados relacional
    1. Abra um terminal na pasta ./infra/terraform/prod para inicializar o terraform
        ```
        terraform init   
        ```
    2. Para de fato subir precisamos rodar o commando
        ```
        terraform apply
        ```
    3. Se tudo der certo vai subir 38 recursos na Aws de infra da nossa aplicação
    4. Caso queira derrubar toda a infraestrura
        ```
        terraform destroy
        ```
5. Depois que toda infraestrutura estiver UP vamos para o proximo passo que é o deploy da aplicação
6. Utilizamos o gitHub Actions onde é feito automaticamente o deploy da aplição para a nossa infra na AWS

## 🔑 Autenticação

### Endpoints Públicos

- `/usuarios` (POST)
- `/usuarios/login`

Você precisará se autenticar no endpoint `/usuarios/login`, caso não tenha usuário cadastado
utilizar o endpoint `/usuarios`.

```
{
  "nome": "Severino",
  "sobreNome": "da silva",
  "email": "severino@fiap.com.br",
  "senha": "Fiap@1234",
  "ativo": true,
  "perfis": [
    "ADM"
  ]
}

```

## 📚 Documentação da API

### [Swagger](http://localhost:8081/swagger-ui/index.html)
