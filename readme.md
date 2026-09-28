# Evaluación Desarrollador Java Senior

## Sistema de Procesamiento de Transacciones

Proyecto desarrollado como parte de una evaluación técnica para **Desarrollador Java Senior**.

La solución implementa una arquitectura basada en APIs REST para recibir, validar, procesar y almacenar transacciones.

El sistema está dividido principalmente en dos APIs:

- **API1 – Transaction Processing API:** recibe y valida la transacción, realiza el descifrado AES-256 y procesa la solicitud.
- **API2 – Transaction Persistence API:** recibe la información procesada por API1 y realiza la persistencia de la transacción.

La comunicación entre ambas APIs se realiza mediante **Spring `RestTemplate`**.

Adicionalmente, se implementó un mecanismo de **autenticación mediante login**, utilizando Spring Security y almacenamiento de usuarios en H2.

---

# 1. Arquitectura

El flujo general de la aplicación es:

```text
                USUARIO / FRONTEND
                       |
                       v
                 +-----------+
                 |   LOGIN   |
                 +-----------+
                       |
                       v
              +-------------------+
              |       API1        |
              | Transaction       |
              | Processing API    |
              +-------------------+
                       |
                       | 1. Validación JSON
                       | 2. Validación de datos
                       | 3. Descifrado AES-256
                       | 4. Procesamiento
                       |
                       v
                  RestTemplate
                       |
                       v
              +-------------------+
              |       API2        |
              | Transaction       |
              | Persistence API   |
              +-------------------+
                       |
                       v
                  +---------+
                  |   H2    |
                  |Database |
                  +---------+
```

---

# 2. Tecnologías utilizadas

El proyecto utiliza principalmente:

- Java
- Spring Boot
- Spring Web
- Spring Validation
- Spring Security
- Spring Data JPA
- RestTemplate
- Maven
- H2 Database
- AES-256
- HTML
- REST
- JSON

---

# 3. API1 – Transaction Processing API

API1 es responsable de recibir la solicitud inicial de una transacción.

Ejemplo de solicitud:

```json
{
  "operacion": "venta",
  "importe": "100.00",
  "cliente": "Angel",
  "secreto": "VALOR_CIFRADO_AES"
}
```

La API realiza diferentes validaciones antes de permitir el procesamiento de la transacción.

---

# 4. Validación de datos

Los datos recibidos son validados mediante **Bean Validation** y la anotación:

```java
@Valid
```

Las principales validaciones realizadas son:

### operacion

Debe contener únicamente caracteres válidos para representar el tipo de operación.

Ejemplo:

```json
"operacion": "venta"
```

### importe

Debe cumplir con un formato monetario válido.

Ejemplo:

```json
"importe": "100.00"
```

### cliente

Debe contener un nombre válido respetando las restricciones de longitud y formato definidas en la aplicación.

Ejemplo:

```json
"cliente": "Angel"
```

### secreto

Contiene la información cifrada enviada hacia la API.

Este valor será procesado mediante el mecanismo de descifrado AES implementado en el backend.

---

# 5. Manejo global de errores

La aplicación utiliza:

```java
@RestControllerAdvice
```

para centralizar el manejo de excepciones y errores de validación.

Esto permite devolver respuestas controladas cuando los datos enviados no cumplen con las reglas establecidas.

Ejemplo conceptual:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Error de validación"
}
```

El objetivo es evitar respuestas internas poco claras y proporcionar al consumidor de la API información adecuada sobre el error.

---

# 6. Cifrado y descifrado AES-256

El atributo `secreto` es enviado cifrado desde el cliente.

API1 realiza el proceso de descifrado utilizando **AES-256**.

Flujo:

```text
Frontend
   |
   | secreto cifrado
   v
API1
   |
   | AES-256
   v
Descifrado
   |
   v
Validación
```

La clave criptográfica no debe almacenarse directamente en el código fuente.

En un ambiente productivo debería administrarse mediante mecanismos seguros como:

- Variables de entorno.
- Secret Manager.
- Vault.
- Servicios especializados de administración de secretos.

Esto evita exponer información sensible dentro del repositorio.

---

# 7. Procesamiento de la transacción

Una vez realizadas las validaciones correspondientes, API1 determina si la transacción puede continuar.

El flujo general es:

```text
Request
   |
   v
Validación JSON
   |
   v
Validación de atributos
   |
   v
Descifrado AES-256
   |
   v
Procesamiento
   |
   +---- Error ----> Respuesta controlada
   |
   v
Transacción válida
   |
   v
API2
```

---

# 8. Comunicación entre API1 y API2

La comunicación entre los dos servicios se implementó utilizando:

```java
RestTemplate
```

API1 construye la solicitud correspondiente y realiza una llamada HTTP hacia API2.

Ejemplo conceptual:

```java
restTemplate.postForEntity(
    api2Url,
    request,
    Response.class
);
```

De esta manera se mantiene separada la responsabilidad de procesamiento de la responsabilidad de persistencia.

```text
API1
 |
 | HTTP Request
 |
 | RestTemplate
 v
API2
```

---

# 9. API2 – Persistencia de transacciones

API2 tiene como responsabilidad recibir los datos enviados por API1 y almacenarlos en la base de datos.

El flujo es:

```text
API1
 |
 v
API2 Controller
 |
 v
Service
 |
 v
Repository
 |
 v
H2 Database
```

La separación de responsabilidades permite mantener desacoplada la lógica de procesamiento de la lógica de persistencia.

---

# 10. Base de datos H2

Para efectos de la evaluación técnica se utiliza **H2 Database**.

H2 permite ejecutar una base de datos ligera durante el desarrollo y las pruebas sin necesidad de instalar un motor de base de datos externo.

Se utiliza para almacenar la información necesaria para el funcionamiento de la aplicación, incluyendo las transacciones y los datos requeridos por el mecanismo de autenticación.

---

# 11. Login

El proyecto incluye una interfaz de autenticación mediante:

```text
login.html
```

El flujo de autenticación es:

```text
Usuario
   |
   v
login.html
   |
   | Usuario + contraseña
   v
Spring Security
   |
   v
Validación de credenciales
   |
   +---- Incorrectas ----> Login Error
   |
   v
Usuario autenticado
   |
   v
Acceso al sistema
```

---

# 12. Seguridad del Login

La autenticación se administra mediante **Spring Security**.

Los usuarios son almacenados en H2.

Las contraseñas no deben almacenarse directamente en texto plano.

El mecanismo de autenticación permite restringir el acceso a recursos que requieren que el usuario se encuentre autenticado.

---

# 13. Consideraciones de seguridad

Durante el desarrollo se consideran buenas prácticas relacionadas con **OWASP Top 10**.

Entre ellas:

### Control de acceso

Los recursos protegidos requieren autenticación.

### Protección de contraseñas

Las contraseñas no deben almacenarse en texto plano.

### Protección de información sensible

Las claves AES y otros secretos no deben estar hardcodeados dentro del código fuente.

### Validación de entradas

Todas las entradas recibidas desde el cliente deben validarse antes de ser procesadas.

### Manejo de errores

Los errores internos de la aplicación no deben exponer información sensible, stack traces o detalles innecesarios de implementación.

### Comunicación segura

En ambientes productivos las comunicaciones entre cliente y APIs deben realizarse mediante:

```text
HTTPS / TLS
```

---

# 14. Estructura general del proyecto

La organización lógica sigue una separación por responsabilidades.

Ejemplo:

```text
src/
└── main/
    ├── java/
    │   └── ...
    │       ├── controller/
    │       ├── service/
    │       ├── repository/
    │       ├── model/
    │       ├── dto/
    │       ├── config/
    │       ├── exception/
    │       └── security/
    │
    └── resources/
        ├── application.properties
        ├── static/
        └── templates/
            └── login.html
```

La estructura exacta puede variar entre API1 y API2 dependiendo de las responsabilidades de cada servicio.

---

# 15. Compilación

El proyecto utiliza Maven.

Para limpiar y compilar:

```bash
mvn clean install
```

También puede utilizarse:

```bash
mvn clean package
```

---

# 16. Ejecución

Cada API debe iniciarse con su configuración correspondiente.

Desde Maven:

```bash
mvn spring-boot:run
```

También puede ejecutarse desde el IDE iniciando la clase principal anotada con:

```java
@SpringBootApplication
```

Es necesario verificar que API1 tenga configurada correctamente la URL de API2 utilizada por `RestTemplate`.

---

# 17. Pruebas

Las APIs pueden probarse utilizando herramientas como:

- Postman
- Swagger/OpenAPI, cuando se encuentre habilitado.
- Navegador para las interfaces HTML.
- H2 Console para verificar los registros almacenados.

Ejemplo de prueba de una transacción:

```http
POST /api/v1/transactions
Content-Type: application/json
```

Body:

```json
{
  "operacion": "venta",
  "importe": "100.00",
  "cliente": "Angel",
  "secreto": "VALOR_CIFRADO_AES"
}
```

Durante las pruebas se recomienda validar tanto escenarios correctos como incorrectos:

```text
✓ Transacción válida
✓ Operación válida
✓ Importe válido
✓ Cliente válido
✓ Secreto cifrado válido
✓ Descifrado AES correcto
✓ Comunicación API1 → API2
✓ Persistencia en H2
✓ Login correcto

✗ JSON inválido
✗ Operación inválida
✗ Importe inválido
✗ Cliente inválido
✗ Secreto inválido
✗ Credenciales incorrectas
```

---

# 18. Flujo completo

```text
                     USUARIO
                        |
                        v
                  +-----------+
                  |   LOGIN   |
                  +-----------+
                        |
                        v
                Spring Security
                        |
                        v
                 Usuario válido
                        |
                        v
              +-------------------+
              |       API1        |
              | Transaction API   |
              +-------------------+
                        |
                        v
                  @Valid / DTO
                        |
                        v
                Validación datos
                        |
                        v
                  AES-256
                   Descifrado
                        |
                        v
                 Procesamiento
                        |
                        v
                  RestTemplate
                        |
                        v
              +-------------------+
              |       API2        |
              | Persistence API   |
              +-------------------+
                        |
                        v
                  Spring Data JPA
                        |
                        v
                       H2
```

---

# 19. Buenas prácticas aplicadas

Durante el desarrollo se buscó aplicar:

- Separación de responsabilidades.
- Arquitectura por capas.
- Validación de datos de entrada.
- Manejo centralizado de excepciones.
- Uso adecuado de códigos HTTP.
- Cifrado AES-256 para la información definida como sensible.
- Comunicación REST entre servicios.
- Persistencia mediante JPA.
- Autenticación mediante Spring Security.
- Protección de credenciales.
- Principios de desarrollo seguro.
- Consideraciones basadas en OWASP Top 10.

---

# Autor

**Carlos Mijangos Jiménez**

Java Backend Developer | API Designer | Technical Lead

Proyecto desarrollado como parte de una evaluación técnica para **Desarrollador Java Senior**.