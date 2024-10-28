# Backend - Asociación Yambo

Este es el backend de la aplicación web para la Asociación Yambo, construido con **Spring Boot**. Gestiona la API y la lógica de negocio de la aplicación, incluyendo autenticación con **JWT**.

## Requisitos

* **JDK 17** o superior
* **Maven**
* **MariaDB** (opcional si no se usa Docker)

## Configuración de Variables de Entorno (sin Docker)

Si ejecutas el backend sin Docker, configura las siguientes variables de entorno en un archivo `.env`:

```plaintext
SPRING_DATASOURCE_URL=jdbc:mariadb://localhost:3306/yambo_db
SPRING_DATASOURCE_USERNAME=db_user
SPRING_DATASOURCE_PASSWORD=db_password
JWT_SECRET=mysecretkey

# Configuración de directorios de subida de archivos
FILE_UPLOAD_DIR=/ruta/a/directorio/de/archivos
```

## Instalación y Ejecución (sin Docker)

**1. Instala las dependencias y compila el proyecto**:

```bash
mvn clean install
```

**2. Inicializa las variables del entorno**:

```bash
export $(cat .env | xargs)
```

**3. Ejecuta el backend**:

```bash
mvn spring-boot:run
```

La API estará disponible en `http://localhost:8080`.