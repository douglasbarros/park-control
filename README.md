# Park Control

Backend em Java 21 + Spring Boot 4 para consumir eventos de um simulador de estacionamento, persistir o estado do garage/spot/session/revenue e expor a receita por setor.

## Stack

- Java 21
- Spring Boot 4
- Spring Data JPA
- MySQL
- Flyway
- RestClient
- JUnit 5
- Mockito
- Lombok

## Estrutura e responsabilidades

A estrutura está coerente com uma arquitetura limpa e hexagonal leve:

- `api/controller`: endpoints HTTP. Ficam sem regra de negócio e apenas orquestram o uso dos casos de uso.
- `api/exception`: tradução centralizada de exceções para respostas HTTP padronizadas.
- `application/usecase`: regras de aplicação e orquestração transacional.
- `application/dto`: contratos de entrada e saída da API.
- `domain/model`: entidades e valores do domínio.
- `domain/service`: regras de negócio puras, como cálculo de preço.
- `domain/repository`: portas de persistência e integrações externas.
- `infrastructure/client`: adaptadores HTTP externos. `RestClientGarageCatalogClient` está corretamente aqui, não em `dto`.
- `infrastructure/client/dto`: DTOs do contrato externo do simulador.
- `infrastructure/configuration`: configuração de web, request logging, startup e RestClient.
- `infrastructure/persistence/entity`: entidades JPA.
- `infrastructure/persistence/mapper`: conversão entre domínio e persistência.
- `infrastructure/persistence/repository`: adapters JPA que implementam as portas do domínio.

## Fluxo principal

### Inicialização

Na inicialização da aplicação, o listener de startup chama o simulador em `GET /garage`, converte o payload e persiste garagens e vagas.

### Webhook

O endpoint `POST /webhook` recebe eventos `ENTRY`, `PARKED` e `EXIT`.

- `ENTRY`: reserva a primeira vaga livre, marca como `OCCUPIED` e cria uma sessão.
- `PARKED`: localiza a vaga por latitude e longitude e atualiza a sessão.
- `EXIT`: encerra a sessão, libera a vaga e atualiza a receita.

### Receita

O endpoint `GET /revenue` expõe a receita por setor para a data informada. Se `date` vier ausente, usa o dia atual em UTC.

## Regras de preço

- Até 30 minutos: grátis.
- Após 30 minutos: cobra hora cheia, arredondando para cima.
- A tarifa base é `basePrice` da garagem.
- Ajuste por ocupação:
  - menor que 25%: -10%
  - menor ou igual a 50%: 0%
  - menor ou igual a 75%: +10%
  - até 100%: +25%

## Banco de dados e índices

As migrations estão em `src/main/resources/db/migration`.

- `V1__init.sql`: schema inicial.
- `V2__add_garage_schedule_fields.sql`: campos de horário e limite de duração do catálogo.
- `V3__add_performance_indexes.sql`: índices de performance.

Índices aplicados:

- `parking_session(plate, status, entry_time)` para localizar sessão aberta rapidamente.
- `spot(status, id)` para reservar a primeira vaga livre.
- `spot(latitude, longitude)` para localizar a vaga no fluxo `PARKED`.

Não adicionei cache em memória por enquanto. O cliente externo do catálogo é usado no startup, não em loop de leitura, então o ganho de um cache local ainda é pequeno frente à complexidade adicional. Se futuramente houver reprocessamento, consulta repetida ou sincronização incremental do catálogo, aí faz sentido introduzir um cache com política clara de expiração.

## Endpoints

### `POST /webhook`

Recebe:

```json
{
  "license_plate": "ZUL0001",
  "entry_time": "2026-07-04T22:22:27",
  "event_type": "ENTRY"
}
```

```json
{
  "license_plate": "ZUL0001",
  "lat": -23.561684,
  "lng": -46.655981,
  "event_type": "PARKED"
}
```

```json
{
  "license_plate": "ZUL0001",
  "exit_time": "2026-07-04T22:22:27",
  "event_type": "EXIT"
}
```

Respostas:

- `200 OK` para eventos processados ou ignorados por idempotência.
- `400 Bad Request` para tipo de evento inválido.
- `404 Not Found` para recursos ausentes como sessão ou vaga.
- `409 Conflict` para garagem cheia.
- `422 Unprocessable Content` para validação do payload.

### `GET /revenue`

Exemplo:

`/revenue?date=2026-07-04&sector=A`

Resposta:

```json
{
  "amount": 0.0,
  "currency": "BRL",
  "timestamp": "2026-07-04T12:00:00Z",
  "sector": "A"
}
```

## Responsabilidades por classe principal

- `WebhookController`: traduz o payload do simulador para a chamada do caso de uso correto.
- `RevenueController`: expõe consulta de receita com fallback de data.
- `HandleEntryUseCase`: controla entrada e reserva de vaga.
- `HandleParkedUseCase`: valida e atualiza a sessão para estacionado.
- `HandleExitUseCase`: calcula tarifa, libera vaga e grava receita.
- `PricingService`: calcula o valor final com base em permanência e ocupação.
- `RestClientGarageCatalogClient`: adapta a resposta do simulador para o modelo interno.
- `ApiExceptionHandler`: padroniza o contrato de erro HTTP.

## Como rodar com Docker

A forma mais simples de rodar o ambiente completo é com o Docker Compose.

```bash
docker-compose up -d
```

Isso irá iniciar:
- A aplicação na porta 3003.
- O simulador na porta 3000.
- O banco de dados MySQL na porta 3306.

A aplicação estará disponível em `http://localhost:3003`.

## Como rodar sem Docker (híbrido)

Você também pode rodar a aplicação localmente e apenas o simulador e o banco de dados com Docker.

1.  **Iniciar o banco de dados:**
    ```bash
    docker-compose up -d mysql
    ```

2.  **Iniciar o simulador:**
    O simulador precisa conseguir acessar a sua aplicação na porta 3003 da sua máquina. Para isso, usamos `host.docker.internal` que aponta para o host.

    ```bash
    docker run -p 3000:3000 \
      -e WEBHOOK_URL=http://host.docker.internal:3003/webhook \
      -e GARAGE_API_PORT=3000 \
      --name parkcontrol-simulator \
      cfontes0estapar/garage-sim:1.0.0
    ```
    **Observação:** O uso da variável de ambiente `WEBHOOK_URL` é uma suposição. Se o simulador não enviar eventos para a aplicação, pode ser que a URL do webhook esteja configurada de outra forma.

3.  **Rodar a aplicação:**
    Configure a sua IDE para usar as seguintes variáveis de ambiente:
    - `MYSQL_HOST=localhost`
    - `PARKCONTROL_GARAGE_URL=http://localhost:3000/garage`

    E então inicie a aplicação:
    ```bash
    mvn spring-boot:run
    ```

A aplicação espera o simulador em `http://localhost:3000/garage` por padrão.

## Testes

Cobertura atual inclui:

- `PricingService`
- `HandleEntryUseCase`
- `HandleParkedUseCase`
- `HandleExitUseCase`
- `WebhookController`
- `RevenueController`
- `ApiExceptionHandler`

## Observações de arquitetura

A separação atual está adequada. O único ponto que eu manteria como melhoria futura, e não como mudança obrigatória agora, é extrair ainda mais o contrato do catálogo externo para fora do domínio se o projeto crescer e esse payload começar a ser reutilizado por outros fluxos.
