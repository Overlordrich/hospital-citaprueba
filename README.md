🏥 Sistema de Gestión de Citas Médicas
Este proyecto es una aplicación web construida con Spring Boot, Thymeleaf y utilizando H2 como base de datos en memoria para facilitar pruebas y desarrollo.

📦 Tecnologías utilizadas
Java 17+

Spring Boot

Spring Data JPA

H2 Database (modo en memoria)

Thymeleaf (Motor de plantillas HTML)

Bootstrap 5 (para el diseño visual)

🛢️ Base de Datos
Motor: H2 Database (en memoria)

Acceso a consola H2:

URL: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:medicdb

Usuario: sa

Contraseña: (vacía)

Nota: La base de datos se reinicia cada vez que se apaga o reinicia la aplicación, ya que es en memoria.

🌐 Rutas Principales del Sistema
Entidad	URL de acceso	Funcionalidad básica
Citas	/citas	Consultar, agendar, editar, cancelar citas
Consultorios	/consultorios	Alta, listado y eliminación de consultorios
Médicos	/medicos	Alta, listado y eliminación de médicos
Pacientes	/pacientes	Alta, listado y eliminación de pacientes

📋 Funcionalidades Clave
Consulta de Citas:
Puedes buscar citas por:

Fecha

Nombre del Médico

Número de Consultorio

Alta y Eliminación de Entidades:
Altas y bajas de médicos, pacientes y consultorios.

Edición y Cancelación de Citas:

Solo citas pendientes pueden editarse o cancelarse.

Se respetan reglas de validación para evitar duplicaciones.

🚀 Para correr el proyecto
Clona este repositorio.

Abre el proyecto en tu IDE preferido.

Ejecuta la clase principal @SpringBootApplication.

Accede en tu navegador a: http://localhost:8080/

📢 Notas importantes
El sistema no requiere configuración manual de la base de datos, ya que H2 se configura automáticamente en memoria.

Todas las relaciones entre entidades se gestionan vía Spring Data JPA.

El diseño está basado en Bootstrap para una vista simple y amigable.

