# Park Control

## Objetivo

Implementar um backend em Java 21 + Spring Boot + MySQL para controlar
um estacionamento, consumindo eventos de um simulador e expondo a
receita por setor.

## Stack

-   Java 21
-   Spring Boot 4
-   Spring Data JPA
-   MySQL
-   Flyway
-   Lombok
-   JUnit 5
-   Mockito
-   Testcontainers
-   RestClient (Spring 6)

## Arquitetura

    src/main/java
    ├── application
    │   ├── dto
    │   ├── service
    │   └── usecase
    ├── domain
    │   ├── exception
    │   ├── model
    │   ├── repository
    │   └── service
    ├── infrastructure
    │   ├── client
    │   ├── configuration
    │   ├── persistence
    │   │   ├── entity
    │   │   ├── mapper
    │   │   └── repository
    │   └── scheduler
    ├── api
    │   ├── controller
    │   ├── exception
    │   └── webhook
    └── ParkingApplication

## Entidades

### Garage

-   sector
-   basePrice
-   capacity

### Spot

-   id
-   sector
-   latitude
-   longitude
-   status (FREE/OCCUPIED)

### ParkingSession

-   id
-   plate
-   spotId
-   entryTime
-   parkedTime
-   exitTime
-   status (ENTRY/PARKED/EXIT)
-   amountPaid

### Revenue

-   id
-   date
-   sector
-   amount

## Banco

-   garage
-   spot
-   parking_session
-   revenue

## Inicialização

1.  Escutar `ApplicationReadyEvent`.
2.  Chamar `GET http://localhost:3000/garage`.
3.  Persistir garagens.
4.  Persistir vagas.
5.  Expor endpoints.

## Cliente HTTP

Utilizar `RestClient`.

## Fluxo ENTRY

1.  Verificar se existe vaga livre.
2.  Caso não exista, retornar conflito (garagem cheia).
3.  Reservar primeira vaga disponível.
4.  Marcar vaga como OCCUPIED.
5.  Criar ParkingSession.

## Fluxo PARKED

1.  Localizar Spot por latitude/longitude.
2.  Atualizar sessão para PARKED.

## Fluxo EXIT

1.  Buscar sessão aberta.
2.  Calcular permanência.
3.  Calcular valor.
4.  Liberar vaga.
5.  Atualizar Revenue.
6.  Encerrar sessão.

## Regras de preço

-   Até 30 minutos: grátis.
-   Acima de 30 minutos:
    -   horas = ceil(minutos/60)
    -   valor = horas × basePrice

### Preço dinâmico
```
  Ocupação         Ajuste
  ---------------- --------
  <25%             -10%
  25% até 50%       0%
  >50% até 75%     +10%
  >75% até 100%    +25%
```
## Regra de ocupação

ocupação = vagas ocupadas / capacidade

Com 100% de ocupação: - impedir novas entradas.

## Casos de Uso

-   InitializeGarageUseCase
-   HandleEntryUseCase
-   HandleParkedUseCase
-   HandleExitUseCase
-   GetRevenueUseCase

## Repositórios

-   GarageRepository
-   SpotRepository
-   ParkingSessionRepository
-   RevenueRepository

## Controllers

-   **POST /webhook**: recebe eventos ENTRY, PARKED e EXIT vindos do simulador.

**ENTRY**
```json
{
  "license_plate": "ZUL0001",
  "entry_time": "2025-01-01T12:00:00.000Z",
  "event_type": "ENTRY"
}
```
Response:
HTTP 200

**PARKED**
```json
{
  "license_plate": "ZUL0001",
  "lat": -23.561684,
  "lng": -46.655981,
  "event_type": "PARKED"
}
```
Response:
HTTP 200

**EXIT**
```json
{
  "license_plate": "ZUL0001",
  "exit_time": "2025-01-01T12:00:00.000Z",
  "event_type": "EXIT"
}
```
Response:
HTTP 200

-   **GET /revenue**: expõe a receita total por setor e até o momento para o dia atual.

Request
```JSON
{
  "date": "2025-01-01",
  "sector": "A"
}
```

Response
```JSON
{
  "amount": 0.00,
  "currency": "BRL",
  "timestamp": "2025-01-01T12:00:00.000Z"
}
```

## Concorrência

-   @Transactional
-   Lock pessimista (`PESSIMISTIC_WRITE`) ao reservar vaga.

## Idempotência

Ignorar: - ENTRY duplicado - PARKED duplicado - EXIT duplicado

Sempre verificar se existe sessão aberta antes de processar.

## Testes

### PricingService

-   30 min → R\$0
-   31 min → 1 hora
-   61 min → 2 horas
-   80% ocupação → +25%

### EntryUseCase

-   cria sessão quando há vaga
-   retorna garagem cheia quando não há

### ExitUseCase

-   libera vaga
-   gera receita

## Diferenciais

-   DDD leve
-   Clean Architecture
-   Flyway
-   RestClient
-   Transactions
-   Lock pessimista
-   Idempotência
-   Testcontainers
