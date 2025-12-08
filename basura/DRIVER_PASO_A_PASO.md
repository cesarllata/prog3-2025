# 🖼️ INSTALACIÓN DEL DRIVER - PASO A PASO VISUAL

## PASO 1: DESCARGAR (2 minutos)

### 1.1 Abre tu navegador y ve a:
```
https://dev.mysql.com/downloads/connector/j/
```

### 1.2 Verás esta pantalla:
```
┌─────────────────────────────────────────────────────┐
│  MySQL Connector/J                                  │
│                                                     │
│  Platform Independent  ← SELECCIONA ESTA           │
│  Windows (x86, 32-bit)                              │
│  Windows (x86, 64-bit)                              │
│  ...                                                │
│                                                     │
│  [Download]  ← HACES CLICK AQUI                    │
└─────────────────────────────────────────────────────┘
```

### 1.3 Haces click en "Download"
Descarga el archivo: `mysql-connector-java-8.0.33.zip` (~2 MB)

---

## PASO 2: EXTRAER EL ZIP (1 minuto)

### 2.1 El archivo se descarga en Descargas:
```
C:\Users\tuUsuario\Downloads\mysql-connector-java-8.0.33.zip
```

### 2.2 Haces click derecho → Extraer aquí
```
┌─ mysql-connector-java-8.0.33.zip (original)
├─ mysql-connector-java-8.0.33/  ← Se crea esta carpeta
   ├─ mysql-connector-java-8.0.33.jar  ← ESTE ES EL ARCHIVO
   ├─ CHANGES
   ├─ README.txt
   └─ LICENSE
```

---

## PASO 3: COPIAR A TU PROYECTO (1 minuto)

### 3.1 Estructura actual de tu proyecto:
```
C:\Users\julen.anda\OneDrive - Universidad de Deusto\Documentos\GitHub\prog3-2025
│
├── src/
│   ├── Principal/
│   │   ├── Main.java
│   │   ├── Usuario.java
│   │   └── ... otros archivos
│   │
│   └── BaseDatos/
│       ├── ConexionDB.java
│       ├── UsuarioDAO.java
│       └── ... otros archivos
│
├── bin/
│
└── oracleJdk-25/
```

### 3.2 Necesitas crear carpeta `lib/`:
```
Tu proyecto AHORA:
└── prog3-2025/
    ├── src/
    ├── bin/
    ├── lib/  ← CREAS ESTA CARPETA
    └── oracleJdk-25/

Cómo crear:
Click derecho en prog3-2025/
Nuevo → Carpeta
Nombre: lib
```

### 3.3 Copia el JAR a lib/:
```
De: C:\Users\tuUsuario\Downloads\mysql-connector-java-8.0.33\mysql-connector-java-8.0.33.jar
A:  C:\...\prog3-2025\lib\mysql-connector-java-8.0.33.jar

Tu proyecto DESPUÉS:
└── prog3-2025/
    ├── src/
    ├── bin/
    ├── lib/
    │   └── mysql-connector-java-8.0.33.jar  ← AQUÍ
    └── oracleJdk-25/
```

---

## PASO 4: INDICARLE A JAVA DÓNDE ESTÁ

### Opción A: Compilar desde terminal

```powershell
# Navega al proyecto
cd "C:\...\prog3-2025"

# Compila indicándole dónde está el driver
javac -cp "lib/mysql-connector-java-8.0.33.jar" -d bin src/**/*.java

# Ejecuta indicándole dónde está el driver
java -cp "bin;lib/mysql-connector-java-8.0.33.jar" Principal.Main
```

### Opción B: Usar Eclipse (si usas IDE)

```
1. Haz click derecho en el proyecto
   ↓
2. Build Path
   ↓
3. Configure Build Path
   ↓
4. Libraries tab (pestaña)
   ↓
5. Add External Archives
   ↓
6. Selecciona: lib/mysql-connector-java-8.0.33.jar
   ↓
7. Apply and Close
```

### Opción C: Usar NetBeans

```
1. Click derecho en proyecto → Properties
2. Libraries → Compile tab
3. Add JAR/Folder
4. Selecciona: lib/mysql-connector-java-8.0.33.jar
5. OK
```

### Opción D: Usar IntelliJ IDEA

```
1. File → Project Structure
2. Libraries → + (agregar)
3. Java
4. Selecciona: lib/mysql-connector-java-8.0.33.jar
5. OK
```

---

## PASO 5: VERIFICAR QUE ESTÁ INSTALADO

### Crea un archivo de prueba: `TestDriver.java`

```java
public class TestDriver {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ ¡Driver encontrado!");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Driver NO encontrado");
            System.out.println("Error: " + e.getMessage());
        }
    }
}
```

### Compila y ejecuta:
```powershell
javac -cp "lib/mysql-connector-java-8.0.33.jar" TestDriver.java

java -cp ".;lib/mysql-connector-java-8.0.33.jar" TestDriver
```

### Si ves esto:
```
✅ ¡Driver encontrado!
```

**¡Listo! El driver está instalado correctamente.**

### Si ves esto:
```
❌ Driver NO encontrado
Error: com.mysql.cj.jdbc.Driver
```

**El driver no está en el classpath. Revisa:**
- ¿Existe la carpeta lib/?
- ¿Está el archivo mysql-connector-java-8.0.33.jar en lib/?
- ¿Escribiste bien el comando -cp?

---

## 🎯 RESUMEN VISUAL

```
┌──────────────────────────────────────────────────────┐
│                                                      │
│  1. DESCARGAR                                       │
│     https://dev.mysql.com/downloads/connector/j/   │
│                     ↓                               │
│     mysql-connector-java-8.0.33.zip                │
│                                                      │
│  2. EXTRAER                                         │
│     Click derecho → Extraer aquí                    │
│                     ↓                               │
│     mysql-connector-java-8.0.33.jar                │
│                                                      │
│  3. COPIAR                                          │
│     A: lib/mysql-connector-java-8.0.33.jar         │
│                                                      │
│  4. INDICARLE A JAVA                                │
│     javac -cp "lib/mysql-connector-java-8.0.33.jar"│
│                                                      │
│  5. VERIFICAR                                       │
│     java TestDriver → ✅ Driver encontrado!         │
│                                                      │
└──────────────────────────────────────────────────────┘
```

---

## ❌ ERRORES COMUNES

### Error 1: ClassNotFoundException
```
Exception in thread "main" java.lang.ClassNotFoundException: 
com.mysql.cj.jdbc.Driver

❌ Significa: No encuentra el driver

✅ Solución:
- Verifica que lib/mysql-connector-java-8.0.33.jar existe
- Verifica que el comando -cp es correcto
- Verifica que NO hay espacios en el nombre del archivo
```

### Error 2: No está en el classpath
```
Compilas bien pero cuando ejecutas falla

❌ Significa: En compile incluiste -cp pero en run no

✅ Solución:
javac -cp "lib/mysql-connector-java-8.0.33.jar" ...  ← Incluir
java -cp "bin;lib/mysql-connector-java-8.0.33.jar" ... ← Incluir también
```

### Error 3: Archivo corrupto
```
Si el ZIP no se extrae correctamente

❌ Significa: La descarga falló

✅ Solución:
- Descarga nuevamente el ZIP
- Asegúrate de esperar a que termine
- Intenta de otro navegador
```

---

## ✅ CHECKLIST FINAL

- [ ] Descargué el ZIP
- [ ] Extraje el contenido
- [ ] Creé carpeta lib/ en el proyecto
- [ ] Copié mysql-connector-java-8.0.33.jar a lib/
- [ ] Compilé con -cp "lib/mysql-connector-java-8.0.33.jar"
- [ ] Ejecuté TestDriver
- [ ] Vi el mensaje ✅ Driver encontrado!

---

## 📞 SI ALGO FALLA

1. **¿El ZIP no descarga?**
   - Usa otro navegador
   - Intenta en otra red

2. **¿No puedo extraer el ZIP?**
   - Descargalo de nuevo
   - Usa WinRAR en lugar de el extracto de Windows

3. **¿No encuentro el archivo .jar?**
   - Busca "mysql-connector" en la carpeta
   - Verifica que no esté en subcarpetas

4. **¿El IDE no ve el JAR?**
   - Limpia el proyecto (Clean)
   - Refresca F5
   - Reinicia el IDE

---

**¡Listo! Ya tienes el driver instalado.** 🎉

Siguiente paso: Ve a `00_EMPEZAR_AQUI.md` para continuar.
