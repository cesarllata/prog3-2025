# ⚡ INICIO RÁPIDO - JDBC EN 5 MINUTOS

## 🎯 Para comenzar inmediatamente:

### 1. Descargar driver (2 min)
```powershell
# Descarga desde:
# https://dev.mysql.com/downloads/connector/j/

# Extrae y copia a tu proyecto:
# prog3-2025/lib/mysql-connector-java-8.0.33.jar
```

### 2. Crear carpeta lib
```powershell
cd "c:\ruta\a\prog3-2025"
New-Item -ItemType Directory -Name lib -Force
```

### 3. Copiar archivo JAR
```powershell
# Copia mysql-connector-java-8.0.33.jar a lib/
Copy-Item "c:\descargas\mysql-connector-java-8.0.33.jar" "lib/"
```

### 4. Compilar
```powershell
javac -cp "lib/mysql-connector-java-8.0.33.jar" -d bin src/**/*.java
```

### 5. Ejecutar prueba
```powershell
java -cp "bin;lib/mysql-connector-java-8.0.33.jar" BaseDatos.PruebaJDBC
```

---

## 📖 Documentación (elegir una):

| Necesidad | Documento |
|-----------|-----------|
| Instalación MySQL | `CONFIGURAR_MYSQL.md` |
| Instalar driver | `INSTALAR_DRIVER_JDBC.md` |
| Aprender JDBC | `GUIA_JDBC.md` |
| Integrar con Swing | `INTEGRACION_SWING.md` |
| Ver todo | `RESUMEN_IMPLEMENTACION.md` |
| Checklist | `CHECKLIST_FINAL.md` |

---

## 💡 Usar en tu código:

```java
// Login
Usuario u = GestorDatos.autenticar("email@mail.com", "password");

// Registrar usuario
GestorDatos.registrarUsuario("Nombre", "email", "pass", "666");

// Obtener rutas
List<Ruta> rutas = GestorDatos.obtenerRutas();

// Buscar rutas específicas
List<Ruta> rutas = GestorDatos.buscarRutas("Bilbao", "Madrid");

// Obtener bonos vigentes
List<Bono> bonos = GestorDatos.obtenerBonos();

// Calcular precio con descuento
double precio = GestorDatos.calcularPrecioConBono(100.0, bono);
```

---

## ✅ Hecho!

Todo está listo. Solo necesitas:
1. Descargar driver JDBC
2. Copiar a carpeta lib/
3. ¡Listo!

Para más detalles, lee los otros documentos.

---

**¿Problemas?** Lee `CONFIGURAR_MYSQL.md` e `INSTALAR_DRIVER_JDBC.md`
