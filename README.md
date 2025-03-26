# CRUD de Usuarios con Spring Boot

Aplicación web para gestión de usuarios con operaciones CRUD, paginación y validaciones.

## 🚀 Requisitos

- Java 17+
- Maven 3.8+
- Spring Boot 3.1+
- H2 Database (embebida)

## ⚙️ Configuración

1. Clonar repositorio:
   ```bash
   git clone [url-del-repositorio]
   ```

2. Instalar dependencias:
   ```bash
   mvn clean install
   ```

3. Configuración de base de datos (en `application.properties`):
   ```properties
   spring.datasource.url=jdbc:h2:mem:userdb
   spring.h2.console.enabled=true
   spring.sql.init.mode=always
   ```

## 🏃 Ejecución

```bash
mvn spring-boot:run
```

La aplicación estará disponible en:  
[http://localhost:8080](http://localhost:8080)

Consola H2 disponible en:  
[http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
(JDBC URL: `jdbc:h2:mem:userdb`, User: `sa`, Password: vacío)

## 📥 Carga Manual de Datos en H2

Para cargar los datos de prueba manualmente en la base de datos H2, sigue estos pasos:

### 1. Acceder a la Consola H2
1. Inicia la aplicación Spring Boot
2. Abre tu navegador en:  
   [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
3. Ingresa las credenciales:
   - JDBC URL: `jdbc:h2:mem:userdb`
   - User Name: `sa`
   - Password: (dejar vacío)

![image](https://github.com/user-attachments/assets/3b735086-ee26-47bd-9dab-4462003955ac)

### 2. Ejecutar Consulta SQL
1. Copia el siguiente script SQL completo:
```sql
INSERT INTO APP_USER (id, nombre, apellido1, apellido2, telefono, email, active, last_access, last_modified)
VALUES 
(1, 'Juan', 'Perez', 'Gomez', '+5491122334455', 'juan@example.com', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), 
(2, 'Maria', 'Gonzalez', NULL, '+5491166778899', 'maria@example.com', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Carlos', 'Lopez', 'Martinez', +5491122334455, 'carlos@example.com', false, NULL, CURRENT_TIMESTAMP),
(4, 'Lucia', 'Fernandez', 'Diaz', '+5491133445566', 'lucia@example.com', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'Pedro', 'Ramirez', NULL, '+5491144556677', 'pedro@example.com', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'Ana', 'Sanchez', 'Lopez', +5491122334455, 'ana@example.com', false, NULL, CURRENT_TIMESTAMP),
(7, 'Diego', 'Torres', 'Gomez', '+5491155667788', 'diego@example.com', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'Carla', 'Vazquez', NULL, '+5491166778899', 'carla@example.com', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 'Roberto', 'Gutierrez', 'Fernandez', +5491122334455, 'roberto@example.com', false, NULL, CURRENT_TIMESTAMP),
(10, 'Sofia', 'Morales', 'Perez', '+5491177889900', 'sofia@example.com', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
```

2. Pega el script en el área de consultas de H2
3. Haz clic en "Run"

![image](https://github.com/user-attachments/assets/3ff7c22a-92fc-4937-8dfb-8d14276bf44c)

### 3. Verificar Datos Cargados
Ejecuta esta consulta para confirmar:
```sql
SELECT * FROM APP_USER;
```

Deberías ver una tabla con los 10 usuarios de prueba:

![image](https://github.com/user-attachments/assets/786df994-beba-495a-8c82-92386f859581)

## 💡 Recomendaciones

1. **Para evitar conflictos con IDs**:
   - Si la tabla ya existe, ejecuta primero:
   ```sql
   DELETE FROM APP_USER;
   ALTER TABLE APP_USER ALTER COLUMN id RESTART WITH 1;
   ```

2. **Para probar la paginación**:
   - La aplicación está configurada para mostrar 5 usuarios por página
   - Con 10 usuarios tendrás 2 páginas de prueba

## 🛠️ Solución de Problemas

Si recibes errores de clave duplicada:
1. Verifica que no existan datos previos:
   ```sql
   SELECT COUNT(*) FROM APP_USER;
   ```
2. Si hay datos, elimínalos primero:
   ```sql
   DELETE FROM APP_USER;
   ```

## 📝 Notas Adicionales

- Los usuarios con `active=false` están marcados como inactivos
- Algunos usuarios no tienen segundo apellido (NULL)
- Los campos `last_access` son NULL para usuarios inactivos

![image](https://github.com/user-attachments/assets/f0d4570d-4d32-480f-9cbb-78413307a1e9)
*Vista esperada del listado de usuarios después de cargar los datos*

## 🔍 Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── user/
│   │           ├── controller/  # Controladores
│   │           ├── dto/         # Entidades DTO
│   │           ├── exception/   # Excepciones personalizadas
│   │           ├── mapper/      # Las clases mappers
│   │           ├── model/       # Entidades
│   │           ├── repository/  # Repositorios
│   │           └── service/     # Lógica de negocio
│   └── resources/
│       ├── templates/           # Vistas Thymeleaf
│       └── import.sql           # Datos que podes importar
```

## 🛠️ Funcionalidades Clave

- **Creación de usuarios** con validación de email único  
  ![image](https://github.com/user-attachments/assets/e59f0d6e-7195-4ef8-980a-462475bde004)

- **Edición** con actualización automática de lastModified  
  ![image](https://github.com/user-attachments/assets/efdd9616-3ae0-4cd4-955d-c5c97fc2f225)

- **Filtrado** por estado (activos/inactivos)  
  ![image](https://github.com/user-attachments/assets/6fb9245b-0ab8-4f0a-9519-f4e317ee0d2c)

- **Paginación** básica  
  ![image](https://github.com/user-attachments/assets/9aff21a3-86c1-4949-b24e-dd8f15bcbbb4)
