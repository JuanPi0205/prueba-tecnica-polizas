# 🏠 API de Gestión de Pólizas de Arrendamiento

> **Prueba Técnica — Desarrollador TI / Sénior · Módulo 2**

API RESTful desarrollada en **Spring Boot** que gestiona el ciclo de vida de pólizas y riesgos de arrendamiento, simulando la integración asíncrona con un sistema CORE legado mediante el patrón **Outbox** y un adaptador con **Circuit Breaker**.

---

## 📋 Tabla de Contenidos

- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Arquitectura](#-arquitectura)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Ejecución](#-instalación-y-ejecución)
- [Seguridad](#-seguridad)
- [Endpoints de la API](#-endpoints-de-la-api)
- [Detener la Aplicación](#-detener-la-aplicación)

---

## 🛠 Tecnologías Utilizadas

| Tecnología | Versión | Rol |
|---|---|---|
| Java | 17 | Lenguaje principal |
| Spring Boot | 3.x | Framework de aplicación |
| PostgreSQL | Latest | Base de datos relacional |
| Docker & Docker Compose | Latest | Contenerización del entorno |
| Maven | Wrapper incluido | Gestión de dependencias y build |

---

## 🏛 Arquitectura

El proyecto implementa los siguientes patrones de diseño:

- **Arquitectura Hexagonal (Ports & Adapters):** la lógica de negocio está completamente aislada del framework y las integraciones externas.
- **Patrón Outbox:** los eventos de sincronización con el CORE se persisten en la misma transacción de negocio, garantizando cero pérdida de mensajes.
- **Circuit Breaker (Resilience4j):** el adaptador `CoreIntegrationService` protege la API ante caídas del sistema legado (WebLogic).
- **Seguridad centralizada:** filtro `ApiKeyFilter` que valida el header `x-api-key` en todos los endpoints.

```
Cliente → API Gateway → Servicio Pólizas / Riesgos → PostgreSQL
                                    ↓
                              Event Bus (Outbox)
                             ↙               ↘
              Svc. Notificaciones    Adapter CORE → WebLogic
```

---

## ⚙️ Requisitos Previos

Asegúrate de tener instalado lo siguiente antes de continuar:

- [Java Development Kit (JDK) 17](https://adoptium.net/)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (o Docker Engine + Docker Compose)
- [Git](https://git-scm.com/)

---

## 🚀 Instalación y Ejecución

Sigue los pasos en orden para levantar el entorno completo.

### Paso 1 — Clonar el repositorio

```bash
git clone https://github.com/JuanPi0205/prueba-tecnica-polizas.git
cd prueba-tecnica-polizas
```

---

### Paso 2 — Levantar la Base de Datos con Docker

El archivo `docker-compose.yml` incluido en la raíz configura automáticamente un contenedor de **PostgreSQL** con todas las credenciales necesarias.

```bash
docker-compose up -d
```

> ⚠️ Asegúrate de tener el puerto **`5432`** libre en tu máquina antes de ejecutar este comando.

---

### Paso 3 — Configuración (Opcional)

La aplicación se conecta por defecto al contenedor de Docker usando los valores definidos en `src/main/resources/application.properties`:

```properties
# Base de datos
spring.datasource.url=jdbc:postgresql://localhost:5432/polizas_db
spring.datasource.username=postgres
spring.datasource.password=admin

# Seguridad
api.security.key=x-api-key: 123456
```

> Puedes modificar estos valores si tu entorno requiere una configuración diferente.

---

### Paso 4 — Compilar y Ejecutar la Aplicación

Puedes levantar el proyecto desde tu IDE ejecutando la clase principal, o desde la terminal:

**Linux / macOS:**
```bash
./mvnw clean install
./mvnw spring-boot:run
```

**Windows (CMD / PowerShell):**
```bash
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

Hibernate/JPA creará las tablas automáticamente al iniciar. La aplicación quedará disponible en:

```
http://localhost:8080
```

---

## 🔐 Seguridad

> ⚠️ **IMPORTANTE:** Todos los endpoints están protegidos por API Key.

Debes incluir el siguiente header en **todas** las peticiones HTTP (Postman, cURL, etc.):

```
x-api-key: 123456
```

**Ejemplo con cURL:**
```bash
curl -X GET http://localhost:8080/api/v1/polizas/1 \
  -H "x-api-key: 123456"
```

---

## 📡 Endpoints de la API

### Módulo de Pólizas

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/api/v1/polizas` | Crear una nueva póliza |
| `GET` | `/api/v1/polizas/{id}` | Consultar una póliza por ID |
| `PUT` | `/api/v1/polizas/{id}/renovar` | Renovar póliza (aplica cálculo de IPC) |
| `DELETE` | `/api/v1/polizas/{id}` | Cancelar póliza (soft delete) |

### Módulo de Riesgos

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/api/v1/riesgos` | Asociar un riesgo a una póliza ¹ |
| `DELETE` | `/api/v1/riesgos/{id}` | Eliminar un riesgo |

> ¹ Valida que las pólizas de tipo `INDIVIDUAL` solo tengan **1 riesgo activo** como máximo.

---

## 🛑 Detener la Aplicación

Para detener el servidor de Spring Boot presiona `Ctrl + C` en la terminal.

Para apagar y eliminar el contenedor de la base de datos:

```bash
docker-compose down
```

---

<div align="center">
  <sub>Desarrollado como solución a la Prueba Técnica · Módulo 2</sub>
</div>
