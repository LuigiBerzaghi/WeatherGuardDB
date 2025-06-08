# 🌪️ WeatherGuard API

Sistema de Alerta Climático Inteligente

A API WeatherGuard monitora variáveis meteorológicas em tempo real (via OpenWeatherMap), identifica condições de risco (como vendavais, chuvas intensas ou calor extremo) em uma determinada frequência e gera alertas automáticos para os usuários cadastrados conforme sua cidade.

Com base nesses dados, o sistema identifica condições críticas e gera automaticamente alertas para cada usuário. Esses alertas são exibidos em um aplicativo mobile intuitivo.

Mais do que uma aplicação técnica, o WeatherGuard é uma iniciativa com impacto social, que busca informar, prevenir e salvar vidas por meio da tecnologia.

---

## 📌 Tecnologias Utilizadas

* Java 17
* Spring Boot 
* Spring Data JPA + Postgree DB
* Spring Security + JWT
* OpenWeatherMap API (clima em tempo real)
* Swagger (SpringDoc OpenAPI)
* Maven
* Integração com o Banco de dados Oracle

---

## 🚀 Como executar a aplicação

1. Acesse o [link para o index do Swagger](https://weatherguard-1ylt.onrender.com/swagger-ui/index.html)

---

## 🚀 Como se conectar com seu Oracle

1. basta trocar o schema pela sua identificação (login e senha) oracle do arquivo Aplication.Propieties da rota:

```
src/main/resources
```

ele vai estar assim:

```
spring.datasource.username=<SEU_LOGIN_ORACLE>
spring.datasource.password=<SUA_SENHA_ORACLE>

```

## 🔒 Autenticação

Para acessar os endpoints protegidos, utilize o token JWT obtido ao fazer login em:

```
POST /api/auth/login
```

Exemplo de token:

```json
{
  "token": "Bearer eyJhbGciOiJIUzI1NiJ9..."
}
```

Utilize este token no botão "Authorize" do Swagger ou no header:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## 📚 Endpoints da API

| Método | Endpoint                                   | Descrição                                      | Corpo da Requisição                                                    | Resposta de Sucesso                        |
| ------ | ------------------------------------------ | ---------------------------------------------- | ---------------------------------------------------------------------- | ------------------------------------------ |
| POST   | `/api/auth/login`                          | Autentica o usuário e retorna o token JWT      | `{ "email": "user@email.com", "senha": "123456" }`                     | `{ "token": "Bearer eyJhbGci..." }`        |
| POST   | `/api/usuarios`                            | Cadastra um novo usuário                       | `{ "nome": "Luigi", "email": "...", "senha": "...", "cidade": "..." }` | JSON do usuário criado (sem senha)         |
| GET    | `/api/usuarios`                            | Lista todos os usuários com paginação e filtro | (query params: `page`, `size`, `sort`, `cidade`)                       | Lista paginada de `UsuarioDTO`             |
| GET    | `/api/usuarios/{id}`                       | Retorna um usuário por ID                      | N/A                                                                    | JSON de `UsuarioDTO`                       |
| PUT    | `/api/usuarios/{id}`                       | Atualiza os dados de um usuário                | Mesma estrutura do POST `/usuarios`                                    | JSON atualizado do usuário                 |
| DELETE | `/api/usuarios/{id}`                       | Deleta um usuário                              | N/A                                                                    | Status `204 No Content`                    |
| GET    | `/api/usuarios/me`                         | Retorna os dados do usuário logado             | Header: `Authorization: Bearer <token>`                                | JSON com `UsuarioDTO`                      |
| GET    | `/api/usuario-alertas/usuario/{usuarioId}` | Lista os alertas recebidos por um usuário      | Header: JWT                                                            | Lista de `AlertaDTO`                       |
| POST   | `/api/alertas`                             | Cria um novo alerta manualmente                | `{ "tipo": "Vendaval", "descricao": "...", "cidade": "..." }`          | JSON do alerta criado                      |
| GET    | `/api/alertas`                             | Lista todos os alertas                         | N/A                                                                    | Lista de `Alerta`                          |
| GET    | `/api/alertas/{id}`                        | Retorna um alerta por ID                       | N/A                                                                    | JSON de `Alerta`                           |
| DELETE | `/api/alertas/{id}`                        | Deleta um alerta                               | N/A                                                                    | Status `204 No Content`                    |
| GET    | `/api/alertas/cidade/{cidade}`             | Lista alertas por cidade                       | N/A                                                                    | Lista de `Alerta`                          |
| GET    | `/api/clima/analisar?lat=...&lon=...`      | Analisa o clima da localização e gera alertas  | Query params: `lat`, `lon`                                             | "Análise climática concluída com sucesso." |

---

## 📦 Funcionalidades implementadas

* [x] API REST com Spring Boot
* [x] CRUD completo de Usuário e Alertas
* [x] Autenticação segura com JWT
* [x] Integração com OpenWeatherMap
* [x] Lógica de risco climático automatizada
* [x] Agendamento com `@Scheduled`
* [x] Associação de alertas a usuários por cidade
* [x] Documentação Swagger/OpenAPI
* [x] Paginação, ordenação e filtro por cidade
* [x] Dockerfile
* [x] Deploy em nuvem

---

## 👨‍💻 Autores

**Luigi Berzaghi** - RM555516

**Guilherme Pelissari** - RM558445

**Cauã dos Santos** - RM559093

---
