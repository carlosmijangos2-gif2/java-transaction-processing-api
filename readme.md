# Transaction Processing - Prueba Técnica

Aplicación desarrollada con **Java 17 y Spring Boot** para el procesamiento y almacenamiento de transacciones mediante una arquitectura basada en APIs REST.

El proyecto está compuesto por dos servicios independientes:

- **API1 - Transaction Processing API**
- **API2 - Transaction Storage API**

La solución implementa validación de datos, descifrado **AES-256/GCM**, comunicación entre APIs mediante `RestTemplate`, persistencia mediante JPA/H2 y una interfaz web básica para realizar pruebas end-to-end.

---

## 1. Arquitectura

```text
                    FRONTEND
                        |
                        | HTTP POST
                        v
        +--------------------------------+
        | API1 - Transaction Processing  |
        | Puerto 8080                    |
        +--------------------------------+
                        |
                        | Validación
                        | Descifrado AES-256/GCM
                        | RestTemplate
                        | HTTP POST
                        v
        +--------------------------------+
        | API2 - Transaction Storage     |
        | Puerto 8081                    |
        +--------------------------------+
                        |
                        | JPA
                        v
                 +--------------+
                 | H2 Database  |
                 +--------------+
```

### Flujo de procesamiento

1. El cliente envía una transacción a la API1.
2. La API1 valida los atributos recibidos.
3. El atributo `secreto` es descifrado mediante AES-256/GCM.
4. Si la información es válida, API1 envía la transacción a API2 mediante `RestTemplate`.
5. API2 recibe la información.
6. La transacción es almacenada mediante JPA.
7. Se devuelve la respuesta correspondiente al cliente.

---

## 2. Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Web
- Jakarta Validation
- Spring Data JPA
- H2 Database
- RestTemplate
- JUnit 5
- Maven
- AES-256
- GCM (Galois/Counter Mode)
- HTML / JavaScript

---

## 3. API1 - Transaction Processing API

La API1 es responsable de recibir y procesar las transacciones.

### Responsabilidades

- Recibir la solicitud HTTP.
- Validar los atributos del request.
- Descifrar el atributo `secreto`.
- Manejar errores de validación.
- Comunicarse con API2.
- Retornar la respuesta al cliente.

### Puerto

```text
8080
```

### Endpoint

```http
POST /api/v1/transactions
```

URL local:

```text
http://localhost:8080/api/v1/transactions
```

---

## 4. API2 - Transaction Storage API

La API2 es responsable de recibir la información procesada y realizar su persistencia.

### Responsabilidades

- Recibir la transacción enviada por API1.
- Persistir la información mediante JPA.
- Utilizar H2 como base de datos.
- Retornar el resultado de la operación.

### Puerto

```text
8081
```

---

## 5. Request

Ejemplo de solicitud:

```json
{
  "operacion": "venta",
  "importe": "100.00",
  "cliente": "Angelito",
  "secreto": "<valor-cifrado-AES-256-GCM>"
}
```

### Campos

| Campo | Descripción |
|---|---|
| `operacion` | Tipo de operación de la transacción |
| `importe` | Importe de la operación |
| `cliente` | Nombre del cliente |
| `secreto` | Información cifrada mediante AES-256/GCM |

---

## 6. Validaciones

La API utiliza Jakarta Validation para validar la información recibida.

Entre las validaciones implementadas se encuentran:

- Campos obligatorios.
- Validación del formato de `operacion`.
- Validación del formato monetario de `importe`.
- Validación del nombre del `cliente`.
- Validación del contenido recibido antes de continuar con el procesamiento.

Los errores de validación son gestionados de forma centralizada mediante:

```java
@RestControllerAdvice
```

---

## 7. Seguridad - AES-256/GCM

El atributo `secreto` se maneja mediante cifrado simétrico **AES-256**.

La implementación utiliza:

```text
AES/GCM/NoPadding
```

Configuración criptográfica:

| Parámetro | Valor |
|---|---|
| Algoritmo | AES |
| Tamaño de clave | 256 bits / 32 bytes |
| Modo | GCM |
| IV | 12 bytes |
| Authentication Tag | 128 bits |
| Padding | NoPadding |

El IV se genera de forma aleatoria durante el proceso de cifrado.

El resultado transportado contiene:

```text
IV + contenido cifrado + Authentication Tag
```

y posteriormente se codifica en Base64 para su transporte.

### Clave AES

La clave **no se almacena directamente en el código fuente**.

`application.properties` utiliza:

```properties
security.aes.key=${AES_SECRET_KEY}
```

Por lo tanto, antes de ejecutar la aplicación debe configurarse la variable de entorno:

```text
AES_SECRET_KEY
```

La clave utilizada debe tener exactamente **32 bytes**.

> No se deben almacenar claves reales, contraseñas ni secretos dentro del repositorio.

---

## 8. Comunicación entre APIs

API1 se comunica con API2 mediante `RestTemplate`.

La URL del servicio de almacenamiento se configura externamente:

```properties
transaction.storage.url=http://localhost:8081/api/v1/transactions
```

Flujo:

```text
Frontend
   |
   v
API1 :8080
   |
   | RestTemplate
   v
API2 :8081
   |
   v
H2 Database
```

---

## 9. Manejo de errores

La aplicación utiliza manejo centralizado de excepciones mediante:

```java
@RestControllerAdvice
```

Esto permite retornar respuestas HTTP controladas ante errores como:

- JSON inválido.
- Campos obligatorios faltantes.
- Formato incorrecto.
- Error durante el descifrado.
- Información cifrada inválida.
- Errores durante el procesamiento.

---

## 10. Configuración

Configuración principal de API1:

```properties
spring.application.name=transaction-processing-api
server.port=8080

security.aes.key=${AES_SECRET_KEY}

transaction.storage.url=http://localhost:8081/api/v1/transactions
```

La variable `AES_SECRET_KEY` debe configurarse en el entorno de ejecución y **no debe incluirse directamente en `application.properties`**.

---

## 11. Ejecución

### Paso 1 - Configurar AES_SECRET_KEY

Configurar una clave AES de 32 bytes como variable de entorno.

Ejemplo conceptual:

```text
AES_SECRET_KEY=<clave-de-32-bytes>
```

No utilizar este ejemplo como clave de producción.

### Paso 2 - Ejecutar API2

Iniciar:

```text
transaction-storage-api
```

Debe quedar disponible en:

```text
http://localhost:8081
```

### Paso 3 - Ejecutar API1

Iniciar:

```text
transaction-processing-api
```

Debe quedar disponible en:

```text
http://localhost:8080
```

### Paso 4 - Abrir la interfaz

Acceder desde el navegador a:

```text
http://localhost:8080/index.html
```

---

## 12. Interfaz de prueba

El proyecto incluye una interfaz web sencilla para probar el flujo completo.

Permite capturar:

- Operación
- Importe
- Cliente
- Secreto cifrado AES-256

La interfaz consume:

```http
POST /api/v1/transactions
```

y muestra la respuesta obtenida de la aplicación.

---

## 13. Pruebas

El proyecto contiene pruebas unitarias con **JUnit 5**.

Por ejemplo, `AesServiceTest` verifica el ciclo:

```text
Texto original
      |
      v
   encrypt()
      |
      v
Texto cifrado
      |
      v
   decrypt()
      |
      v
Texto original
```

La prueba comprueba que:

```java
assertNotEquals(textoOriginal, textoCifrado);
assertEquals(textoOriginal, textoDescifrado);
```

De esta manera se valida que el texto es cifrado y posteriormente recuperado correctamente utilizando la misma clave AES.

---

## 14. Estructura del repositorio

```text
.
├── README.md
├── .gitignore
│
├── transaction-processing-api/
│   ├── pom.xml
│   └── src/
│       ├── main/
│       └── test/
│
└── transaction-storage-api/
    ├── pom.xml
    └── src/
        ├── main/
        └── test/
```

---

## 15. Consideraciones de seguridad

Para mantener buenas prácticas de seguridad:

- No se hardcodean claves AES en el código fuente.
- La clave AES se obtiene mediante una variable de entorno.
- Se utiliza AES-256/GCM.
- Se utiliza un IV aleatorio para cada operación de cifrado.
- GCM proporciona confidencialidad e integridad/autenticidad del contenido cifrado.
- Los errores se gestionan de forma centralizada.
- Los secretos reales no deben almacenarse en Git.

Para un ambiente productivo se recomienda utilizar un gestor de secretos y HTTPS/TLS para las comunicaciones entre clientes y servicios.

---

## Autor

**Carlos Mijangos Jiménez**

Prueba técnica desarrollada con Java 17 y Spring Boot.