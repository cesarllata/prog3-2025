# GUÍA DE IMPLEMENTACIÓN JDBC

## 📋 Descripción
Se ha creado una capa de base de datos completa usando JDBC para tu proyecto de gestión de rutas y reservas.

## 📁 Estructura de carpetas creadas
```
src/
└── BaseDatos/
    ├── ConexionDB.java      (Gestión de conexión)
    ├── UsuarioDAO.java      (CRUD de usuarios)
    ├── RutaDAO.java         (CRUD de rutas)
    ├── BonoDAO.java         (CRUD de bonos)
    └── ScriptsSQL.java      (Scripts SQL para inicializar BD)
```

## 🔧 Paso 1: Descargar el driver JDBC

### Para MySQL (recomendado):
1. Descarga **MySQL Connector/J** desde: https://dev.mysql.com/downloads/connector/j/
2. Extrae el archivo JAR
3. Copia `mysql-connector-java-x.x.x.jar` a tu proyecto

### Para SQL Server:
- Descarga desde: https://github.com/microsoft/mssql-jdbc

### Para PostgreSQL:
- Descarga desde: https://jdbc.postgresql.org/

## 📦 Paso 2: Agregar el JAR a tu proyecto

### Si usas un IDE (Eclipse, NetBeans, IntelliJ):
1. Click derecho en el proyecto → Build Path → Add External Archives
2. Selecciona el archivo JAR descargado
3. Click OK

### Si compilas manualmente:
```powershell
javac -cp "ruta/al/driver.jar" src/**/*.java
```

## ⚙️ Paso 3: Configurar la conexión

En `ConexionDB.java`, actualiza estas constantes según tu base de datos:

```java
private static final String URL = "jdbc:mysql://localhost:3306/prog3_2025";
private static final String USUARIO = "root";
private static final String CONTRASENA = "";
```

### Para diferentes motores:

**MySQL:**
```java
String URL = "jdbc:mysql://localhost:3306/nombre_bd?useSSL=false&serverTimezone=UTC";
```

**SQL Server:**
```java
String URL = "jdbc:sqlserver://localhost:1433;databaseName=nombre_bd";
String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
```

**PostgreSQL:**
```java
String URL = "jdbc:postgresql://localhost:5432/nombre_bd";
String DRIVER = "org.postgresql.Driver";
```

## 🗄️ Paso 4: Crear la base de datos

### Opción A: Ejecutar desde Java
```java
// En tu Main.java o clase de inicialización
ConexionDB db = ConexionDB.getInstance();
if (db.conectar()) {
    Statement stmt = db.getConexion().createStatement();
    stmt.execute(ScriptsSQL.CREAR_TABLAS);
    stmt.execute(ScriptsSQL.CREAR_INDICES);
    System.out.println("Base de datos creada exitosamente");
}
```

### Opción B: Ejecutar manualmente en MySQL
```sql
CREATE DATABASE prog3_2025;
USE prog3_2025;
-- Luego pega el contenido de ScriptsSQL.CREAR_TABLAS
```

## 🔌 Paso 5: Actualizar tus clases modelo

Tus clases `Usuario`, `Ruta` y `Bono` necesitan getters y setters.

**Ejemplo para Usuario.java:**
```java
package Principal;

public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String contrasena;
    private String telefono;

    // Constructor vacío (requerido por DAO)
    public Usuario() {}

    // Constructor con parámetros
    public Usuario(String nombre, String email, String contrasena, String telefono) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.telefono = telefono;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
```

## 💡 Paso 6: Usar los DAOs en tu código

### Insertar un usuario:
```java
Usuario usuario = new Usuario("Juan", "juan@email.com", "pass123", "123456789");
UsuarioDAO usuarioDAO = new UsuarioDAO();
if (usuarioDAO.insertar(usuario)) {
    System.out.println("Usuario creado exitosamente");
}
```

### Obtener todos los usuarios:
```java
UsuarioDAO usuarioDAO = new UsuarioDAO();
List<Usuario> usuarios = usuarioDAO.obtenerTodos();
for (Usuario u : usuarios) {
    System.out.println(u.getNombre() + " - " + u.getEmail());
}
```

### Actualizar un usuario:
```java
Usuario usuario = usuarioDAO.obtenerPorId(1);
if (usuario != null) {
    usuario.setTelefono("987654321");
    usuarioDAO.actualizar(usuario);
}
```

### Buscar usuario por email:
```java
Usuario usuario = usuarioDAO.obtenerPorEmail("juan@email.com");
if (usuario != null) {
    // Hacer algo con el usuario
}
```

### Eliminar un usuario:
```java
usuarioDAO.eliminar(1);
```

## 🔄 Crear un DAO para Reservas

Próximamente puedes crear `ReservaDAO.java` siguiendo el mismo patrón para la tabla de reservas.

## 🚨 Manejo de excepciones

Los DAOs ya incluyen try-catch para manejar excepciones SQL. Considera usar transacciones:

```java
Connection conn = ConexionDB.getInstance().getConexion();
try {
    conn.setAutoCommit(false);
    
    // Tus operaciones aquí
    usuarioDAO.insertar(usuario);
    rutaDAO.insertar(ruta);
    
    conn.commit();
} catch (Exception e) {
    conn.rollback();
    System.err.println("Error: " + e.getMessage());
}
```

## 🔐 Seguridad - Prevenir SQL Injection

✅ **CORRECTO** (usa PreparedStatement):
```java
String sql = "SELECT * FROM usuarios WHERE email = ?";
PreparedStatement stmt = conexion.prepareStatement(sql);
stmt.setString(1, email);
```

❌ **INCORRECTO** (SQL injection):
```java
String sql = "SELECT * FROM usuarios WHERE email = '" + email + "'";
```

## 📊 Pruebas

Crea una clase de prueba:
```java
public class PruebaJDBC {
    public static void main(String[] args) {
        ConexionDB db = ConexionDB.getInstance();
        if (db.conectar()) {
            UsuarioDAO dao = new UsuarioDAO();
            
            // Prueba insertar
            Usuario u = new Usuario("Test", "test@test.com", "123", "555");
            dao.insertar(u);
            
            // Prueba obtener todos
            dao.obtenerTodos().forEach(usuario -> 
                System.out.println(usuario.getNombre())
            );
            
            db.desconectar();
        }
    }
}
```

## 🐛 Troubleshooting

| Problema | Solución |
|----------|----------|
| No encuentra el driver | Verifica que el JAR esté en el classpath |
| No puede conectar | Verifica URL, usuario y contraseña de BD |
| Tabla no existe | Ejecuta el script CREAR_TABLAS |
| SQL Injection | Usa siempre PreparedStatement con ? |
| Conexión cerrada | Verifica que ConexionDB.conectar() fue exitoso |

## 📚 Recursos adicionales

- [JDBC API Documentation](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/package-summary.html)
- [MySQL Connector/J](https://dev.mysql.com/doc/connector-j/en/)
- [Tutorial JDBC Oracle](https://docs.oracle.com/javase/tutorial/jdbc/index.html)

---

¡JDBC está listo para usar! 🚀
