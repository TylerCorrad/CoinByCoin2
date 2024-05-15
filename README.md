
# Coin by Coin: Aplicación móvil de finanzas personales
CoinbyCoin es una aplicación móvil desarrollada en Android Studio cuyo objetivo es gestionar tus finanzas personales de manera rápida y accesible. Aquí puedes registrar tus ingresos y gastos según lo necesites y visualizar el estado de tus finanzas actuales de manera dinámica.
Figma con el prototipo de la aplicación: https://www.figma.com/file/qgx6PWhYO1YZ4VwTMtfYzC/Untitled?type=design&node-id=0%3A1&mode=design&t=ZNo1JdNkhJo3w1xb-1

## Estado del proyecto
Actualmente, el proyecto se encuentra en estado de desarrollo. Faltan algunas funciones extra.

## Instrucciones de uso
### Registro e inicio de sesión
Para registrarte por primera vez, selecciona el botón de registro y completa todos los campos requeridos.
Una vez registrado, inicia sesión con tus credenciales.
### Dashboard
En esta sección encontrarás gráficos que te informarán sobre tu estado financiero actual.
Puedes ingresar nuevos gastos, ver una lista de los gastos realizados este mes por categorías y modificar o eliminar gastos específicos.
### Menú de navegación
Accede al menú de navegación desde la esquina superior izquierda.
### Secciones disponibles:
- **Dashboard:** Gráficos y resumen financiero.
- **Ingresos:** Gestión de ingresos.
- **Reporte:** Análisis detallado del estado financiero.
- **Perfil:** Actualización de información personal y de cuenta.
## Librerías y dependencias
### Room
Biblioteca utilizada para crear una base de datos local y gestionar la información del usuario.
Se implementó un ViewModel conectado a un repositorio para acceder a la base de datos.
### HelloCharts
Utilizado para la implementación de gráficos y visualización de datos.
### Kapt
Procesador de anotaciones utilizado en conjunto con Room para la generación de código de base de datos.

