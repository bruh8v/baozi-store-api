# Baozi Store API

API REST desenvolvida para a Atividade Prática da disciplina **Desenvolvimento Web Back-End** (CST em Análise e Desenvolvimento de Sistemas – UNINTER).

**Aluna:** Bruna Vargas – RU 4374748

A Baozi Store é uma pequena loja que vende pão chinês (baozi). A API controla o cadastro de **clientes**, **produtos** e **pedidos**, em que um cliente compra um único produto em determinada quantidade.

## Tecnologias

- Java 17
- Spring Boot 3.5 (Spring Web, Spring Data JPA, Validation)
- Banco de dados relacional H2 (padrão) ou MySQL (perfil opcional)
- Maven
- Postman (testes)

## Estrutura do projeto (padrão MVC)

```
src/main/java/br/com/baozistore
├── BaoziStoreApplication.java      -> classe principal
├── model                           -> entidades JPA: Cliente, Produto, Pedido
├── repository                      -> interfaces JpaRepository
└── controller                      -> endpoints REST (@RestController)
src/main/resources
├── application.properties          -> configuração do banco H2
└── application-mysql.properties    -> configuração alternativa para MySQL
postman
└── Baozi_Store.postman_collection.json
```

## Como executar

Requisito: **JDK 17 ou superior**.

1. Abrir a pasta do projeto na IDE (IntelliJ IDEA, Eclipse/STS ou VS Code) como projeto Maven.
2. Aguardar o download das dependências.
3. Executar a classe `BaoziStoreApplication`.

Pelo terminal (com o Maven instalado): `mvn spring-boot:run`

A API fica disponível em `http://localhost:8080`.

Console do H2: `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:baozistore`, usuário `sa`, senha em branco).

> O H2 está configurado em memória: ao reiniciar a aplicação o banco volta a ficar vazio e os ids recomeçam em 1.

### Usando MySQL (opcional)

Ajustar usuário e senha em `src/main/resources/application-mysql.properties` e executar com o perfil `mysql`:

```
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

O banco `baozistore` é criado automaticamente, caso não exista.

## Entidades

| Entidade | Campos |
|----------|--------|
| Cliente  | id (Long), nome (String), clienteDesde (LocalDate) |
| Produto  | id (Long), nome (String), preco (BigDecimal), estoque (Boolean) |
| Pedido   | id (Long), clienteId (Long), produtoId (Long), quantidade (Integer) |

## Endpoints

| Método | Endpoint | Descrição | Respostas |
|--------|----------|-----------|-----------|
| POST   | /clientes      | Cadastra um cliente           | 201, 400 |
| GET    | /clientes      | Lista todos os clientes       | 200 |
| GET    | /clientes/{id} | Consulta um cliente pelo id   | 200, 404 |
| PUT    | /clientes/{id} | Atualiza um cliente (opcional)| 200, 400, 404 |
| DELETE | /clientes/{id} | Exclui um cliente             | 204, 404, 409 |
| POST   | /produtos      | Cadastra um produto           | 201, 400 |
| GET    | /produtos      | Lista todos os produtos       | 200 |
| GET    | /produtos/{id} | Consulta um produto pelo id   | 200, 404 |
| PUT    | /produtos/{id} | Atualiza um produto (opcional)| 200, 400, 404 |
| DELETE | /produtos/{id} | Exclui um produto             | 204, 404, 409 |
| POST   | /pedidos       | Registra um pedido            | 201, 400 |
| GET    | /pedidos       | Lista todos os pedidos        | 200 |
| GET    | /pedidos/{id}  | Consulta um pedido pelo id    | 200, 404 |
| PUT    | /pedidos/{id}  | Atualiza um pedido (opcional) | 200, 400, 404 |
| DELETE | /pedidos/{id}  | Exclui um pedido              | 204, 404 |

Regras de negócio:

- um pedido só é registrado se o cliente e o produto informados existirem e se o produto estiver em estoque (caso contrário, 400);
- não é possível excluir um cliente ou produto que possua pedidos (409);
- se `clienteDesde` não for informado no cadastro, é usada a data atual.

## Exemplos de requisição

`POST /clientes`
```json
{
  "nome": "Bruna4374748",
  "clienteDesde": "2026-11-24"
}
```

`POST /produtos`
```json
{
  "nome": "Baozi Tradicional de Porco",
  "preco": 8.50,
  "estoque": true
}
```

`POST /pedidos`
```json
{
  "clienteId": 1,
  "produtoId": 1,
  "quantidade": 6
}
```

## Testes no Postman

Importar o arquivo `postman/Baozi_Store.postman_collection.json` no Postman e executar as requisições na ordem das pastas (1 a 4). A pasta 5 (PUT) é opcional.
