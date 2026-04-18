# 🏠 Smart Home Prototype

Prototipo de Hogar Inteligente (Smart Home) que integra patrones de diseño de software para la automatización de luces y puertas, utilizando Arduino y simulación en Wokwi.

# 🚀 Descripción

Este proyecto combina hardware + buenas prácticas de software para demostrar cómo los patrones de diseño mejoran la organización, escalabilidad y mantenibilidad de un sistema domótico.

El sistema permite:

- Encender y apagar luces 💡
- Abrir y cerrar una puerta 🚪
- Notificar cambios de estado al usuario

# 🧠 Patrones de Diseño Implementados
- Command → Encapsula acciones (encender/apagar luz)
- Factory → Crea dispositivos dinámicamente (Luz, Puerta)
- Observer → Notifica cambios al usuario
- State → Controla estados de la puerta (abierta/cerrada)
# ⚙️ Tecnologías y Herramientas
- Arduino UNO / ESP32
- Wokwi (simulación)
- Java (estructura lógica del sistema)
- Electrónica básica

#🔌 Componentes
- LEDs
- Resistencias
- Servomotor
- Protoboard
  
#🧪 Funcionamiento
- El sistema crea dispositivos usando Factory
- Se ejecutan acciones mediante Command
- Los cambios se notifican con Observer
- La puerta cambia de comportamiento usando State
  

# ▶️ Cómo ejecutar
- Ejecutar el skech
- Cargar el código en Arduino
- Ejecutar la simulación
- Observar el comportamiento en el monitor serial


# 📈 Objetivo

Demostrar cómo el uso de patrones de diseño permite:

- Código más limpio
- Mayor reutilización
- Facilidad para agregar nuevos dispositivos
- Mejor mantenimiento del sistema

#🧠 Aprendizaje

Este proyecto refuerza conocimientos en:
- IoT
- Programación orientada a objetos
- Patrones de diseño
- Integración hardware + software
