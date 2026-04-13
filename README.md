# Parking Top 🚗🅿️

**Parking Top** es una solución integral de movilidad inteligente diseñada para conectar a conductores con propietarios de estacionamientos. La plataforma permite a los usuarios localizar, reservar y pagar espacios de estacionamiento de forma anticipada, mientras que ofrece a los propietarios herramientas avanzadas para la gestión profesional de sus negocios.

---

## 🚀 Características Principales

### 👤 Para Conductores (Clientes)
- **Búsqueda Inteligente:** Localiza estacionamientos cercanos mediante geolocalización en tiempo real.
- **Reservas Anticipadas:** Asegura tu lugar antes de llegar al destino.
- **Gestión de Vehículos:** Registra múltiples vehículos para agilizar el proceso de reserva.
- **Pagos Seguros:** Integración con pasarelas de pago externas (MercadoPago) para transacciones rápidas.
- **Reseñas y Calificaciones:** Califica tu experiencia y consulta la reputación de los estacionamientos.
- **Notificaciones en Tiempo Real:** Alertas sobre el estado de tus reservas y recordatorios de tiempo.

### 💼 Para Propietarios (Owners)
- **Panel de Control:** Gestión completa de múltiples lotes de estacionamiento.
- **Control de Espacios (Spots):** Configura y monitorea la disponibilidad de cada cajón individualmente.
- **Suscripciones Flexibles:** Elige entre planes Básico, Premium o Empresarial para potenciar tu negocio.
- **Gestión Financiera:** Visualiza tu balance, historial de ganancias y solicita retiros (payouts).
- **Disponibilidad Dinámica:** Publica o pausa la visibilidad de tus lotes con un solo toque.

---

## 🛠️ Stack Tecnológico

- **Lenguaje:** [Kotlin 2.0+](https://kotlinlang.org/)
- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material Design 3)
- **Arquitectura:** MVVM (Model-View-ViewModel) + Clean Architecture
- **Inyección de Dependencias:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- **Persistencia Local:** [Room Database](https://developer.android.com/training/data-storage/room)
- **Networking:** [Retrofit](https://square.github.io/retrofit/) + [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
- **Gestión de Tokens:** [Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
- **Procesamiento en Segundo Plano:** [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
- **Notificaciones:** [Firebase Cloud Messaging (FCM)](https://firebase.google.com/docs/cloud-messaging)

---

## 📂 Estructura del Proyecto

El proyecto sigue una estructura modular basada en características (*features*) y una capa central de servicios:

```text
com.parking.parkingtop
├── core                # Red, Base de Datos, DI, Navegación, Utilidades
├── features
│   ├── login           # Autenticación y Bienvenida
│   ├── register        # Registro de Clientes y Propietarios
│   ├── cliente         # Pantallas y lógica para el usuario final (Busqueda, Reserva, Perfil)
│   ├── propetario      # Panel de control, Gestión de Lotes y Ganancias
│   ├── subscritionplan # Selección y pago de planes de suscripción
│   └── notifications   # Sistema de notificaciones push y locales
└── ui.theme            # Configuración de colores, tipografía y formas (M3)
```

---

## ⚙️ Instalación y Configuración

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/tu-usuario/parking-top.git
   ```
2. **Configurar Firebase:**
   - Agrega tu archivo `google-services.json` en la carpeta `app/`.
3. **Sincronizar Gradle:**
   - Abre el proyecto en **Android Studio Jellyfish** o superior y sincroniza los archivos de Gradle.
4. **Ejecutar:**
   - Selecciona un emulador o dispositivo físico y presiona `Run`.

---

## 📝 Requerimientos Técnicos
- **Min SDK:** 24 (Android 7.0 Nougat)
- **Target SDK:** 34 (Android 14)
- **Android Studio:** Jellyfish o superior.

---

## 💡 Trabajo Futuro (v2.0)
- Integración con sensores IoT para detección automática de ocupación.
- Algoritmos de precios dinámicos basados en demanda (IA).
- Navegación con Realidad Aumentada hasta el cajón seleccionado.
- Panel administrativo web para grandes corporativos.

---

Desarrollado con ❤️ por el equipo de **Parking Top**.
