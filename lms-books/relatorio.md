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
   
    
