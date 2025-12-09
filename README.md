# DeustoBus — Manual de uso (Guía para el usuario)

Este documento explica, de forma práctica y paso a paso, cómo usar la aplicación DeustoBus una vez que está ejecutada. Está pensado para un usuario (o profesora) que quiere probar la app: qué hace cada parte, cómo realizar las acciones más importantes y cómo interpretar los resultados.

Contenido del manual

- Introducción rápida
- Requisitos mínimos
- Cómo arrancar la aplicación (resumen)
- Interfaz principal y navegación
- Flujos de uso (login, registro, comprar bono, reservar billete, editar perfil)
- Historial y cómo verificar datos persistidos
- Problemas comunes y soluciones
- Credenciales de ejemplo y recomendaciones para la evaluación

--------------------------------------------------------------------------------

Introducción rápida

DeustoBus es una aplicación de escritorio en Java (Swing) que simula la gestión de usuarios, rutas, bonos y reservas de un servicio de transporte. Los datos se guardan en un archivo SQLite local; la app incluye importación inicial desde CSV para poblar la base de datos.

Cuando la app arranca, en la consola verás mensajes de `GestorBD` indicando si creó tablas y si importó datos desde CSV. Si la base de datos ya existía, no volverá a importar para evitar duplicados.

Interfaz principal y navegación

Al iniciar la aplicación se muestra una ventana principal (`MainFrame`) con un menú o panel principal desde el que acceder a las diferentes secciones. Las secciones principales son:

- Iniciar sesión (Login)
- Registro (crear una nueva cuenta)
- Perfil de usuario (editar datos personales, ver saldo)
- Bonos (ver bonos disponibles y comprarlos)
- Rutas (ver rutas disponibles)
- Nueva reserva (comprar billete para una ruta)
- Historial (ver bonos y reservas previas)

Nota: la navegación exacta (botones/menús) aparece dentro de la interfaz; busca las etiquetas visibles en la ventana principal. Ejemplos de controles que sí están en la UI:

- En el panel de login: botón "ENTRAR" y enlace "¿No tienes cuenta? Regístrate".

Flujos de uso paso a paso

1) Iniciar sesión

- Abre la app y selecciona el panel de inicio de sesión.
- Introduce tu email o nombre de usuario en el campo "Usuario o Email".
- Introduce tu contraseña en "Contraseña".
- Haz clic en "ENTRAR".

Comportamiento esperado:
- Si las credenciales son válidas, verás un mensaje de bienvenida y el menú cambiará al estado autenticado (aparecerá el perfil del usuario, saldo, acceso a comprar bonos y reservar).
- Si las credenciales son incorrectas, verás una alerta indicando "Credenciales incorrectas".

2) Registro de nuevo usuario

- Desde el login haz clic en "¿No tienes cuenta? Regístrate" o abre el panel "Registro".
- Rellena los campos solicitados (nombre, email, contraseña, teléfono, etc.).
- Envía el formulario.

Comportamiento esperado:
- Se crea un nuevo usuario en la base de datos con un saldo inicial (en la implementación actual, se asigna 50.00 € como saldo inicial).
- Tras registrar, podrás iniciar sesión con las credenciales creadas.

3) Ver y comprar bonos

- Accede al panel "Bonos" desde el menú principal.
- Verás una lista de bonos (importados desde `resources/csv/bonos.csv` o creados anteriormente).
- Selecciona un bono para ver su descripción, precio y número de viajes incluidos.
- Si tu saldo es suficiente, elige comprar.

Comportamiento esperado:
- Al comprar un bono se crea un registro en la tabla `usuario_bono` con fecha de compra y caducidad calculada en función de la duración del bono.
- El saldo del usuario se reduce automáticamente por el precio del bono.

4) Ver rutas y hacer una reserva (comprar billete)

- Accede al panel "Rutas" o "Nueva reserva".
- Elige una ruta de la lista (información: origen, destino, duración, precio).
- Confirma la reserva si quieres pagar el billete.

Comportamiento esperado:
- Se guarda un registro en la tabla `reserva` con la descripción de la ruta, precio pagado y fecha de reserva.
- El saldo del usuario se reduce por el precio del billete y la reserva aparece en el historial.

5) Perfil de usuario y edición de datos

- Accede al panel "Perfil".
- Aquí puedes ver tu nombre, email, teléfono, número de tarjeta y saldo.
- Edita los campos disponibles y guarda los cambios para actualizar los datos en la base de datos.

6) Historial (bonos y reservas)

- En el panel de historial o perfil podrás ver:
  - Historial de bonos: nombre del bono, viajes restantes, fecha de compra, fecha de caducidad.
  - Historial de reservas: descripción de la ruta, precio pagado y fecha.

Verificación y persistencia

- Todos los cambios importantes (registro, compra de bonos, reservas, edición de perfil) se persisten en la base de datos SQLite (`resources/db/deustobus.db` por defecto).
- Para comprobar persistencia: cierra la aplicación y vuélvela a abrir; los cambios (usuarios creados, compras) deberán seguir ahí.

Credenciales de ejemplo (para probar rápidamente)

- admin@deustobus.com — admin123
- juan@gmail.com — 1234
- maria@hotmail.com — maria123

Usa una de esas cuentas para comprobar que la importación desde CSV ha funcionado correctamente.

Cómo usar los eventos (instrucciones simples)

- Registro (teclado): coloca el cursor en el campo "Nº Tarjeta" o en "CVV" y escribe; solo se aceptan dígitos. La tarjeta se limita a 16 dígitos y el CVV a 3: si intentas escribir letras o más dígitos no ocurrirá nada.
- Botón usuario / avatar (ratón): sitúa el ratón sobre el avatar o el texto del usuario; verás un efecto de resaltado (hover). Haz click sobre él para activar su acción (abre el menú lateral o ejecuta la acción asignada).
- Menú lateral (ratón): pasa el ratón por encima de las opciones para verlas resaltadas; haz click en una opción para que se ejecute (p. ej. "Perfil", "Historial", "Cerrar Sesión"). Haz click fuera del menú para cerrarlo (comportamiento "clic fuera cierra menú").
- Activación por teclado: si un botón tiene foco puedes pulsar ENTER o SPACE para activarlo; usa TAB para moverte entre controles del formulario.



