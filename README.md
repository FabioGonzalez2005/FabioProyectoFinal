# ![Logo](logo)  
# Proyecto Final - CanariaSS

**CanariaSS** es una aplicación para gestionar citas médicas entre pacientes y doctores. Permite agendar, modificar y cancelar citas, además de gestionar horarios y enviar recordatorios automáticos.

---

## 👨‍💻 Autor
**Fabio González Waschkowitz**

---

## 💡 Concepto del Proyecto

- **Nombre**: CanariaSS  
- **Descripción**: Aplicación móvil que facilita la gestión de citas médicas. Pacientes y doctores pueden interactuar de forma eficiente a través de funcionalidades clave como recordatorios, historial médico, y administración de horarios.

---

## ✨ Funcionalidades Clave

### 1. Gestión de Usuarios
- Registro e inicio de sesión para pacientes y doctores.
- Creación y gestión de perfiles.
- Gestión de usuarios por parte del administrador.

### 2. Gestión de Citas
- Búsqueda de doctores por especialidad y disponibilidad.
- Solicitud, reprogramación y cancelación de citas.
- Confirmaciones automáticas y recordatorios.

### 3. Historial y Expediente Médico
- Visualización de citas pasadas y futuras.
- Los doctores pueden añadir notas médicas a los expedientes.

### 4. Notificaciones
- Envío de recordatorios por email, SMS o push notification antes de las citas.

### 5. Panel de Administración
- Reportes de uso, estadísticas y gestión global de citas.

---

## 🛠️ Tecnologías Utilizadas

### Backend
- **Lenguaje**: Python  
- **Framework**: Flask (API REST)  
- **Cliente HTTP**: Retrofit

### Frontend
- **Plataforma**: Android  
- **UI**: Jetpack Compose

### Base de Datos
- **Motor**: PostgreSQL  
- **API de prueba**: Postman

### Notificaciones
- **Push Notifications**: Firebase Cloud Messaging (FCM)  
- **SMS (opcional)**: Twilio

---

## 📊 Diagrama ER - Entidades Principales

### Entidades:
- **Clinica**: `id_clinica`  
- **Usuario**: `id_usuario`, `nombre`, `email`, `contraseña`, `rol` *(paciente, doctor, administrador)*  
- **Doctor**: `id_doctor`, `id_usuario`, `especialidad`, `horario_disponible`  
- **Paciente**: `id_paciente`, `id_usuario`  
- **Cita**: `id_cita`, `id_paciente`, `id_doctor`, `fecha`, `hora`, `estado` *(pendiente, confirmada, cancelada)*  
- **Expediente Médico**: `id_expediente`, `id_paciente`, `id_doctor`, `diagnóstico`, `tratamiento`, `fecha`  
- **Notificación**: `id_notificacion`, `id_usuario`, `mensaje`, `fecha_envio`, `tipo` *(email, SMS, push notification)*

### Relaciones:
- **Clínica - Doctor**: N:M  
- **Usuario - Doctor**: 1:1  
- **Usuario - Paciente**: 1:1  
- **Paciente - Citas**: 1:N  
- **Doctor - Citas**: 1:N  
- **Paciente - Expediente Médico**: 1:1  
- **Doctor - Expediente Médico**: 1:N  
- **Usuario - Notificaciones**: 1:N

---

## 📌 Nota Final
CanariaSS busca mejorar la experiencia del paciente y la eficiencia del personal médico mediante una solución integral de gestión de citas médicas.

