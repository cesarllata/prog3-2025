package BaseDatos;

import Principal.Bono;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para la tabla Bono
 * Maneja todas las operaciones de base de datos relacionadas con bonos
 */
public class BonoDAO {
    private Connection conexion;

    public BonoDAO() {
        this.conexion = ConexionDB.getInstance().getConexion();
    }

    /**
     * Inserta un nuevo bono en la base de datos
     *
     * @param bono Bono a insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean insertar(Bono bono) {
        String sql = "INSERT INTO bonos (nombre, descripcion, descuento, viajes_incluidos, fecha_expiracion) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, bono.getNombre());
            stmt.setString(2, bono.getDescripcion());
            stmt.setDouble(3, bono.getDescuento());
            stmt.setInt(4, bono.getVialesIncluidos());
            stmt.setDate(5, new java.sql.Date(bono.getFechaExpiracion().getTime()));
            
            stmt.executeUpdate();
            System.out.println("Bono insertado exitosamente");
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar bono: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene un bono por su ID
     *
     * @param id ID del bono
     * @return Bono si existe, null en caso contrario
     */
    public Bono obtenerPorId(int id) {
        String sql = "SELECT * FROM bonos WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener bono: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene todos los bonos
     *
     * @return Lista de todos los bonos
     */
    public List<Bono> obtenerTodos() {
        List<Bono> bonos = new ArrayList<>();
        String sql = "SELECT * FROM bonos";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                bonos.add(mapearResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener bonos: " + e.getMessage());
        }
        return bonos;
    }

    /**
     * Obtiene bonos vigentes
     *
     * @return Lista de bonos no expirados
     */
    public List<Bono> obtenerVigentes() {
        List<Bono> bonos = new ArrayList<>();
        String sql = "SELECT * FROM bonos WHERE fecha_expiracion >= CURDATE()";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                bonos.add(mapearResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener bonos vigentes: " + e.getMessage());
        }
        return bonos;
    }

    /**
     * Actualiza los datos de un bono
     *
     * @param bono Bono con datos actualizados
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizar(Bono bono) {
        String sql = "UPDATE bonos SET nombre = ?, descripcion = ?, descuento = ?, viajes_incluidos = ?, fecha_expiracion = ? WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, bono.getNombre());
            stmt.setString(2, bono.getDescripcion());
            stmt.setDouble(3, bono.getDescuento());
            stmt.setInt(4, bono.getVialesIncluidos());
            stmt.setDate(5, new java.sql.Date(bono.getFechaExpiracion().getTime()));
            stmt.setInt(6, bono.getId());
            
            stmt.executeUpdate();
            System.out.println("Bono actualizado exitosamente");
            return true;
        } catch (SQLException e) {
            System.err.println("Error al actualizar bono: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un bono de la base de datos
     *
     * @param id ID del bono a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM bonos WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Bono eliminado exitosamente");
            return true;
        } catch (SQLException e) {
            System.err.println("Error al eliminar bono: " + e.getMessage());
            return false;
        }
    }

    /**
     * Mapea un ResultSet a un objeto Bono
     *
     * @param rs ResultSet con los datos del bono
     * @return Objeto Bono
     * @throws SQLException Si hay error al acceder a los datos
     */
    private Bono mapearResultSet(ResultSet rs) throws SQLException {
        Bono bono = new Bono();
        bono.setId(rs.getInt("id"));
        bono.setNombre(rs.getString("nombre"));
        bono.setDescripcion(rs.getString("descripcion"));
        bono.setDescuento(rs.getDouble("descuento"));
        bono.setVialesIncluidos(rs.getInt("viajes_incluidos"));
        bono.setFechaExpiracion(new java.util.Date(rs.getDate("fecha_expiracion").getTime()));
        return bono;
    }
}
