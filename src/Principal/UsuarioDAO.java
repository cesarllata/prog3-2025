package Principal;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsuarioDAO {

    private Connection conn;

    public UsuarioDAO() {
        GestorBD gestor = new GestorBD();
        this.conn = gestor.getConnection();
    }

    // 1. LOGIN
    public Usuario login(String emailOrNombre, String password) {
        String sql = "SELECT * FROM usuario WHERE (email = ? OR nombre = ?) AND contrasena = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, emailOrNombre);
            pstmt.setString(2, emailOrNombre);
            pstmt.setString(3, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("contrasena"),
                    rs.getString("telefono"),
                    rs.getString("num_tarjeta"),
                    rs.getDouble("saldo")
                );
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // 2. REGISTRO
    public Usuario registrar(Usuario u) {
        String sql = "INSERT INTO usuario (nombre, email, contrasena, telefono, num_tarjeta, saldo) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, u.getNombre());
            pstmt.setString(2, u.getEmail());
            pstmt.setString(3, u.getContrasena());
            pstmt.setString(4, u.getTelefono());
            pstmt.setString(5, u.getNumTarjeta());
            pstmt.setDouble(6, 50.00); 

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        u.setId(generatedKeys.getInt(1));
                        u.setSaldo(50.00);
                        return u;
                    }
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // 3. ACTUALIZAR SALDO
    public boolean actualizarSaldo(int usuarioId, double nuevoSaldo) {
        String sql = "UPDATE usuario SET saldo = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, nuevoSaldo);
            pstmt.setInt(2, usuarioId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // 4. COMPRAR BONO
    public boolean comprarBono(Usuario u, Bono b) {
        if (u.getSaldo() < b.getPrecio()) return false;

        String sqlCompra = "INSERT INTO usuario_bono (usuario_id, bono_id, nombre_bono, viajes_restantes, fecha_compra, fecha_caducidad) VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaHoy = sdf.format(new Date());
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.add(java.util.Calendar.DATE, b.getDuracionDias());
            String fechaCaducidad = sdf.format(c.getTime());

            PreparedStatement pstmt = conn.prepareStatement(sqlCompra);
            pstmt.setInt(1, u.getId());
            pstmt.setInt(2, b.getId());
            pstmt.setString(3, b.getNombre());
            pstmt.setInt(4, b.getViajesIncluidos());
            pstmt.setString(5, fechaHoy);
            pstmt.setString(6, fechaCaducidad);
            
            if (pstmt.executeUpdate() > 0) {
                double nuevoSaldo = u.getSaldo() - b.getPrecio();
                if (actualizarSaldo(u.getId(), nuevoSaldo)) {
                    u.setSaldo(nuevoSaldo);
                    return true;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // 5. GUARDAR RESERVA
    public boolean guardarReserva(Usuario u, String descripcionRuta, double precio) {
        if (u.getSaldo() < precio) return false;

        String sql = "INSERT INTO reserva (usuario_id, descripcion_ruta, precio_pagado, fecha_reserva) VALUES (?, ?, ?, ?)";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, u.getId());
            pstmt.setString(2, descripcionRuta);
            pstmt.setDouble(3, precio);
            pstmt.setString(4, sdf.format(new Date()));
            
            if(pstmt.executeUpdate() > 0) {
                double nuevoSaldo = u.getSaldo() - precio;
                if (actualizarSaldo(u.getId(), nuevoSaldo)) {
                    u.setSaldo(nuevoSaldo);
                    return true;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // 6. ACTUALIZAR DATOS DE PERFIL
    public boolean actualizarDatosUsuario(Usuario u) {
        String sql = "UPDATE usuario SET nombre = ?, email = ?, telefono = ?, num_tarjeta = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, u.getNombre());
            pstmt.setString(2, u.getEmail());
            pstmt.setString(3, u.getTelefono());
            pstmt.setString(4, u.getNumTarjeta());
            pstmt.setInt(5, u.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // === NUEVOS MÉTODOS PARA EL HISTORIAL ===

    // Obtener Historial de Bonos
    public List<String[]> obtenerHistorialBonos(int usuarioId) {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT nombre_bono, viajes_restantes, fecha_compra, fecha_caducidad FROM usuario_bono WHERE usuario_id = ? ORDER BY id DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                lista.add(new String[]{
                    rs.getString("nombre_bono"),
                    String.valueOf(rs.getInt("viajes_restantes")),
                    rs.getString("fecha_compra"),
                    rs.getString("fecha_caducidad")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    // Obtener Historial de Reservas (Billetes)
    public List<String[]> obtenerHistorialReservas(int usuarioId) {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT descripcion_ruta, precio_pagado, fecha_reserva FROM reserva WHERE usuario_id = ? ORDER BY id DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                lista.add(new String[]{
                    rs.getString("descripcion_ruta"),
                    String.format("%.2f €", rs.getDouble("precio_pagado")),
                    rs.getString("fecha_reserva")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}