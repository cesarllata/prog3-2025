package app.models;

public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String contrasena;
    private String telefono;
    private String numTarjeta; // NUEVO
    private double saldo;

    public Usuario() {}

    // Constructor para registro (saldo inicial 0, tarjeta obligatoria)
    public Usuario(String nombre, String email, String contrasena, String telefono, String numTarjeta) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.numTarjeta = numTarjeta;
        this.saldo = 0.0;
    }

    // Constructor completo desde BD
    public Usuario(int id, String nombre, String email, String contrasena, String telefono, String numTarjeta, double saldo) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.numTarjeta = numTarjeta;
        this.saldo = saldo;
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
    
    public String getNumTarjeta() { return numTarjeta; } // NUEVO
    public void setNumTarjeta(String numTarjeta) { this.numTarjeta = numTarjeta; }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }

    @Override
    public String toString() {
        return nombre + " (" + String.format("%.2f", saldo) + "€)";
    }
}
