# ms-productos360

> **Evaluacion Parcial 1 - Cloud Native** | Microservicio de Productos - Spring Boot 3 + Oracle Autonomous DB + Azure Entra ID

## Descripcion
Microservicio REST para la gestion del catalogo de productos del sistema Pedidos360.
Complementa al `ms-pedidos360-bff` como segundo microservicio del backend.

## Tecnologias
- Spring Boot 3.3.x (Java 21)
- Spring Security OAuth2 Resource Server (JWT Azure)
- Spring Data JPA + Hibernate (Oracle Dialect)
- Oracle JDBC ojdbc11 + TNS Wallet

## Endpoints

| Metodo | Ruta | Scope requerido | Descripcion |
|--------|------|----------------|-------------|
| GET | `/api/productos` | SCOPE_Read / APPROLE_Operador | Lista todos los productos |
| GET | `/api/productos/{id}` | SCOPE_Read / APPROLE_Operador | Producto por ID |
| GET | `/api/productos/disponibles` | SCOPE_Read / APPROLE_Operador | Productos con stock > 0 |
| GET | `/api/productos/categoria/{cat}` | SCOPE_Read / APPROLE_Operador | Filtrar por categoria |
| POST | `/api/productos` | SCOPE_Write | Crear producto |
| PUT | `/api/productos/{id}` | SCOPE_Write | Actualizar producto |
| DELETE | `/api/productos/{id}` | SCOPE_Write | Eliminar producto |
| GET | `/actuator/health` | Publico | Health check |

## Estructura
```
ms-productos360/
├── src/main/java/cl/duoc/pedidos360/productos/
│   ├── config/SecurityConfig.java       # JWT validation + CORS
│   ├── controller/ProductController.java # CRUD endpoints
│   ├── dto/ProductDTO.java              # DTO con validaciones
│   ├── entity/Product.java              # @Entity -> tabla PRODUCTS
│   ├── repository/ProductRepository.java # JpaRepository
│   └── MsProductos360Application.java   # Main (puerto 8081)
├── src/main/resources/
│   ├── application.properties           # Config Oracle + Azure
│   ├── wallet/                          # Oracle Autonomous TNS Wallet
│   └── db/001_create_products_table.sql # DDL Oracle
└── pom.xml
```

## Ejecucion local
```bash
./mvnw spring-boot:run
# API disponible en http://localhost:8081
```
