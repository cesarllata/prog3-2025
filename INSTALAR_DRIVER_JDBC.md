# 📦 INSTALACIÓN DEL DRIVER JDBC

## Opción 1: MySQL Connector/J (Recomendado)

### Descargar:
1. Ve a: https://dev.mysql.com/downloads/connector/j/
2. Selecciona la última versión (recomendado: 8.0.x)
3. Descarga el archivo `.tar.gz` o `.zip`

### Después de descargar:
1. Extrae el archivo
2. Busca el archivo `mysql-connector-java-8.0.xx.jar`
3. **OPCIÓN A - Si tienes proyecto en IDE:**
   - En Eclipse/NetBeans/IntelliJ:
     - Click derecho en tu proyecto → Build Path → Add External Archives
     - Selecciona el archivo `.jar`
   
4. **OPCIÓN B - Si compilo desde terminal:**
   - Copia el archivo `.jar` a una carpeta `lib/` en tu proyecto
   - Ejemplo estructura:
     ```
     prog3-2025/
     ├── src/
     ├── lib/
     │   └── mysql-connector-java-8.0.33.jar
     └── bin/
     ```

## Opción 2: Descargar desde Maven Central

Si usas Maven, agrega a tu `pom.xml`:
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

## Opción 3: Descargar desde Gradle

Si usas Gradle, agrega a tu `build.gradle`:
```gradle
dependencies {
    implementation 'mysql:mysql-connector-java:8.0.33'
}
```

## 🧪 Verificar instalación

### Compilar desde terminal (Windows PowerShell):
```powershell
# Navega al directorio del proyecto
cd "c:\ruta\a\prog3-2025"

# Compila con el driver en el classpath
javac -cp "lib/mysql-connector-java-8.0.33.jar" -d bin src/**/*.java

# Ejecuta la prueba
java -cp "bin;lib/mysql-connector-java-8.0.33.jar" BaseDatos.PruebaJDBC
```

### Si usas Eclipse/NetBeans/IntelliJ:
- El IDE compilará automáticamente si el JAR está en Build Path

## 🔍 Troubleshooting

### "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
- Solución: El JAR no está en el classpath
- Verifica que está en la carpeta `lib/` 
- En terminal, usa: `-cp "lib/mysql-connector-java-8.0.33.jar"`

### "Connection refused"
- El servidor MySQL no está corriendo
- Inicia: `mysql` o `MySQL Server` (según tu instalación)

### "Unknown database"
- Asegúrate de haber creado la BD: `CREATE DATABASE prog3_2025;`

## ✅ Próximos pasos

1. Descarga el driver
2. Colócalo en la carpeta `lib/` de tu proyecto
3. Ejecuta `PruebaJDBC` para verificar que todo funciona
4. Si todo va bien, podrás usar los DAOs en tu aplicación Swing

---

**¿Necesitas ayuda?** Consulta GUIA_JDBC.md
