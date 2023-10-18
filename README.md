# RevisiaBank  - APP

O ReviziaBank é uma solução de gerenciamento bancário digital.


## Características do Projeto Backend
- CRUD de Contas;
- Transferência de valores;
- Extrato da conta;

## Características do Projeto Frontend
- Dashboard(Diferencial)
- Listagem e Filtros
- Gráficos com ou sem filtragem(Diferencial)
- CRUD

### Pré-requisitos:
- Java 17 para o backend
- Angular 16 para o frontend
- Opcional
    - Docker -> PostgreSQL (ReviziaBankBackend/compose.yaml)

### Configuração:
#### Clone o repositório
```shell
git clone git@github.com:willianganzert/java-test-aiziver.git
cd java-test-aiziver
```
#### Inicialize o Postgres
```shell
cd ReviziaBankBackend
docker-compose up -d
```

### Documentação da API
A documentação da API pode ser obtida atravez do arquivo estático gerado em:
```shell
mvn clean install
```

```
Diretório:
/ReviziaBankBackend/targer/generated-docs/index.html 
```

### TODO
- [X] Tarefa 1 - Backend Development (Spring Boot)
- [X] Tarefa 1.1 - Testes unitários (JUnit)
- [X] Tarefa 1.1.1 - Testes para a documentação da API (Spring Rest Docs)
- [X] Tarefa 1.1.2 - Testes controllers
- [ ] Tarefa 1.1.3 - Testes services
- [ ] Tarefa 1.1.4 - Implementar relátorio de covertura de testes
- [X] Tarefa 1.2 - Documentação da API (Spring Rest Docs)
- [ ] Tarefa 1.2.1 - Extrair documentação da API para arquivo estático e disponibilizar no projeto
- [X] Tarefa 2 - Frontend Development (Angular)
- [ ] Tarefa 2.1 - Testes unitários (Jasmine)
- [ ] Tarefa 2.2 - Garantir responsividade em todos os dispositivos
- [X] Tarefa * - Docker (Docker Compose)
- [X] Tarefa * - Versionamento da API
- [X] Tarefa * - Versionamento do Banco de dados
- [ ] Tarefa * - Autenticação
 