# 📑 ÍNDICE DE DOCUMENTACIÓN JDBC

## 🎯 ¡EMPEZAR AQUÍ!

### Para usuarios que comienzan
**👉 [00_EMPEZAR_AQUI.md](00_EMPEZAR_AQUI.md)** - Resumen ejecutivo (5 min)

---

## 📚 DOCUMENTACIÓN COMPLETA

### Nivel 1: Inicio Rápido
| Documento | Tiempo | Para quién |
|-----------|--------|-----------|
| [INICIO_RAPIDO.md](INICIO_RAPIDO.md) | 5 min | Usuarios impacientes |
| [README_JDBC.md](README_JDBC.md) | 10 min | Visión general |

### Nivel 2: Instalación
| Documento | Tiempo | Para quién |
|-----------|--------|-----------|
| [CONFIGURAR_MYSQL.md](CONFIGURAR_MYSQL.md) | 20 min | Instalar MySQL |
| [INSTALAR_DRIVER_JDBC.md](INSTALAR_DRIVER_JDBC.md) | 10 min | Descargar driver |

### Nivel 3: Aprendizaje
| Documento | Tiempo | Para quién |
|-----------|--------|-----------|
| [GUIA_JDBC.md](GUIA_JDBC.md) | 30 min | Entender JDBC |
| [INTEGRACION_SWING.md](INTEGRACION_SWING.md) | 45 min | Integrar con UI |

### Nivel 4: Referencia
| Documento | Tiempo | Para quién |
|-----------|--------|-----------|
| [RESUMEN_IMPLEMENTACION.md](RESUMEN_IMPLEMENTACION.md) | 20 min | Revisión general |
| [CHECKLIST_FINAL.md](CHECKLIST_FINAL.md) | 15 min | Verificar todo |
| [IMPLEMENTACION_COMPLETA.txt](IMPLEMENTACION_COMPLETA.txt) | 10 min | Detalles técnicos |

---

## 💻 ARCHIVOS JAVA

### Carpeta: `src/BaseDatos/`

#### Conexión
- **ConexionDB.java** - Gestión de conexión única (Singleton)
  - `getInstance()` - Obtiene instancia única
  - `conectar()` - Conecta a BD
  - `getConexion()` - Retorna Connection
  - `estaConectado()` - Verifica estado
  - `desconectar()` - Cierra conexión

#### DAOs (Data Access Objects)
- **UsuarioDAO.java** - CRUD de usuarios
  - `insertar(usuario)` - Crear usuario
  - `obtenerPorId(id)` - Buscar por ID
  - `obtenerPorEmail(email)` - Buscar por email
  - `obtenerTodos()` - Listar todos
  - `actualizar(usuario)` - Actualizar
  - `eliminar(id)` - Eliminar

- **RutaDAO.java** - CRUD de rutas
  - `insertar(ruta)` - Crear ruta
  - `obtenerPorId(id)` - Buscar por ID
  - `obtenerTodas()` - Listar todas
  - `obtenerPorOrigenDestino(origen, destino)` - Buscar específicas
  - `actualizar(ruta)` - Actualizar
  - `eliminar(id)` - Eliminar

- **BonoDAO.java** - CRUD de bonos
  - `insertar(bono)` - Crear bono
  - `obtenerPorId(id)` - Buscar por ID
  - `obtenerTodos()` - Listar todos
  - `obtenerVigentes()` - Listar vigentes
  - `actualizar(bono)` - Actualizar
  - `eliminar(id)` - Eliminar

#### Utilidades
- **GestorDatos.java** - Métodos públicos para UI
  - `autenticar(email, password)` - Login
  - `registrarUsuario(...)` - Registro
  - `obtenerRutas()` - Todas las rutas
  - `buscarRutas(origen, destino)` - Búsqueda
  - `obtenerBonos()` - Bonos vigentes
  - `calcularPrecioConBono(precio, bono)` - Con descuento
  - `verificarConexion()` - Test de conexión
  - Y 10+ más

- **ScriptsSQL.java** - Scripts para BD
  - `CREAR_TABLAS` - Crea 4 tablas
  - `CREAR_INDICES` - Crea 5 índices
  - `DATOS_EJEMPLO` - Datos de prueba

- **InicializadorBD.java** - Inicialización
  - `inicializarBD()` - Crea tablas automáticamente
  - `insertarDatosEjemplo()` - Carga datos

- **PruebaJDBC.java** - Pruebas automatizadas
  - `main()` - Ejecuta todas las pruebas
  - `pruebaUsuarios()` - Test usuarios
  - `pruebaRutas()` - Test rutas
  - `pruebaBonos()` - Test bonos

- **VerificacionBD.java** - Herramienta de diagnóstico
  - `main()` - Verifica configuración
  - `verificarDriver()` - Test driver
  - `verificarConexion()` - Test conexión
  - `verificarTablas()` - Test tablas

### Carpeta: `src/Principal/` (Actualizados)

- **Usuario.java** ✅ 
  - Getters para: id, nombre, email, contrasena, telefono
  - Setters para todos
  - 3 constructores

- **Ruta.java** ✅
  - Getters para: id, nombre, descripcion, origen, destino, duracion, precio
  - Setters para todos
  - 3 constructores

- **Bono.java** ✅
  - Getters para: id, nombre, descripcion, descuento, vialesIncluidos, fechaExpiracion
  - Setters para todos
  - 3 constructores

---

## 🎯 RUTAS DE LECTURA POR PERFIL

### 👨‍💼 Jefe del Proyecto
```
1. Lee: 00_EMPEZAR_AQUI.md
2. Lee: RESUMEN_IMPLEMENTACION.md
3. Revisa: src/BaseDatos/GestorDatos.java
Tiempo: 20 minutos
```

### 👨‍💻 Desarrollador
```
1. Lee: 00_EMPEZAR_AQUI.md
2. Lee: GUIA_JDBC.md
3. Lee: INTEGRACION_SWING.md
4. Copia ejemplos a tu código
5. Ejecuta: VerificacionBD
6. Ejecuta: PruebaJDBC
Tiempo: 2 horas
```

### 🔧 DevOps/DBA
```
1. Lee: CONFIGURAR_MYSQL.md
2. Crea base de datos
3. Crea índices
4. Verifica con VerificacionBD
Tiempo: 30 minutos
```

### 📚 Estudiante
```
1. Lee: INICIO_RAPIDO.md
2. Lee: GUIA_JDBC.md
3. Estudia: ConexionDB.java
4. Estudia: UsuarioDAO.java
5. Estudia: GestorDatos.java
6. Experimenta: Modifica PruebaJDBC
Tiempo: 3 horas
```

---

## 🚀 FLUJO RECOMENDADO

### Día 1: Instalación (1 hora)
```
1. Lee: INICIO_RAPIDO.md (5 min)
2. Descarga: Driver JDBC (5 min)
3. Copia: lib/ (2 min)
4. Lee: CONFIGURAR_MYSQL.md (20 min)
5. Instala: MySQL Server (15 min)
6. Crea: Base de datos (10 min)
```

### Día 2: Pruebas (1 hora)
```
1. Compila: Proyecto (5 min)
2. Ejecuta: VerificacionBD (5 min)
3. Ejecuta: PruebaJDBC (5 min)
4. Lee: GUIA_JDBC.md (30 min)
5. Lee ejemplos: ConexionDB + GestorDatos (15 min)
```

### Día 3: Integración (2 horas)
```
1. Lee: INTEGRACION_SWING.md (45 min)
2. Integra: LoginPanel (30 min)
3. Integra: RegistroPanel (30 min)
4. Prueba: Todo funciona (15 min)
```

---

## 📊 TABLA DE CONTENIDOS

### Documentación Técnica
- Guías paso a paso
- Ejemplos de código
- SQL scripts
- Troubleshooting

### Código Java
- 9 clases principales
- 40+ métodos
- ~1,200 líneas

### Datos & Base de Datos
- 4 tablas
- 5 índices
- Scripts SQL

### Integración UI
- Ejemplos LoginPanel
- Ejemplos RegistroPanel
- Ejemplos NuevaReservaPanel

---

## 💡 RESUMEN RÁPIDO

**¿Cómo empezar?**
- Lee: `00_EMPEZAR_AQUI.md`

**¿Cómo instalar?**
- Lee: `CONFIGURAR_MYSQL.md` y `INSTALAR_DRIVER_JDBC.md`

**¿Cómo aprender?**
- Lee: `GUIA_JDBC.md`

**¿Cómo integrar?**
- Lee: `INTEGRACION_SWING.md`

**¿Cómo verificar?**
- Ejecuta: `VerificacionBD.java`

**¿Cómo probar?**
- Ejecuta: `PruebaJDBC.java`

---

## ✅ CHECKLIST DE LECTURA

- [ ] Leí: 00_EMPEZAR_AQUI.md
- [ ] Leí: README_JDBC.md
- [ ] Leí: INICIO_RAPIDO.md
- [ ] Leí: CONFIGURAR_MYSQL.md
- [ ] Leí: INSTALAR_DRIVER_JDBC.md
- [ ] Leí: GUIA_JDBC.md
- [ ] Leí: INTEGRACION_SWING.md
- [ ] Leí: RESUMEN_IMPLEMENTACION.md
- [ ] Leí: CHECKLIST_FINAL.md
- [ ] Revisé: src/BaseDatos/GestorDatos.java
- [ ] Revisé: Ejemplos de código
- [ ] Ejecuté: VerificacionBD
- [ ] Ejecuté: PruebaJDBC

---

## 🔍 BÚSQUEDA RÁPIDA

### Necesito...

| Necesidad | Ir a | Línea |
|-----------|------|------|
| Empezar | 00_EMPEZAR_AQUI.md | - |
| 5 minutos | INICIO_RAPIDO.md | - |
| Instalar MySQL | CONFIGURAR_MYSQL.md | - |
| Descargar driver | INSTALAR_DRIVER_JDBC.md | - |
| Entender JDBC | GUIA_JDBC.md | - |
| Código LoginPanel | INTEGRACION_SWING.md | - |
| Checklist | CHECKLIST_FINAL.md | - |
| Ejemplos | INTEGRACION_SWING.md | Sección 3-6 |
| Troubleshooting | GUIA_JDBC.md | Troubleshooting |
| Métodos GestorDatos | BaseDatos/GestorDatos.java | - |
| Métodos DAO | BaseDatos/*DAO.java | - |
| Scripts SQL | BaseDatos/ScriptsSQL.java | - |

---

## 📞 AYUDA

### ¿Dónde busco...?

**Para compilar:**
- Documentación: INICIO_RAPIDO.md
- Archivo: INSTALAR_DRIVER_JDBC.md

**Para conectar:**
- Documentación: CONFIGURAR_MYSQL.md
- Verificar: VerificacionBD.java

**Para programar:**
- Ejemplos: INTEGRACION_SWING.md
- API: GestorDatos.java
- Tutorial: GUIA_JDBC.md

**Para bugs:**
- Troubleshooting: GUIA_JDBC.md
- Diagnóstico: VerificacionBD
- Pruebas: PruebaJDBC.java

---

## 🎓 APRENDIZAJE

Después de leer toda la documentación habrás aprendido:

✅ Patrón DAO
✅ Patrón Singleton
✅ JDBC (Java Database Connectivity)
✅ MySQL
✅ PreparedStatement
✅ Try-with-resources
✅ CRUD operations
✅ Arquitectura en capas
✅ Separación de responsabilidades

---

## 📌 NOTAS IMPORTANTES

1. **Empieza con:** `00_EMPEZAR_AQUI.md`
2. **Después instala:** MySQL + Driver JDBC
3. **Luego aprende:** Lee GUIA_JDBC.md
4. **Finalmente integra:** Sigue INTEGRACION_SWING.md

---

**¿Listo? 👉 [00_EMPEZAR_AQUI.md](00_EMPEZAR_AQUI.md)**
