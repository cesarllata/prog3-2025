package BaseDatos;

import Principal.Ruta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para la tabla Ruta
 * Maneja todas las operaciones de base de datos relacionadas con rutas
 */
public class RutaDAO {
    private Connection conexion;

    public RutaDAO() {
        this.conexion = ConexionDB.getInstance().getConexion();
    }

    /**
     * Inserta una nueva ruta en la base de datos
     *
     * @param ruta Ruta a insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean insertar(Ruta ruta) {
        String sql = "INSERT INTO rutas (nombre, descripcion, origen, destino, duracion, precio) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, ruta.getNombre());
            stmt.setString(2, ruta.getDescripcion());
            stmt.setString(3, ruta.getOrigen());
            stmt.setString(4, ruta.getDestino());
            stmt.setInt(5, ruta.getDuracion());
            stmt.setDouble(6, ruta.getPrecio());
            
            stmt.executeUpdate();
            System.out.println("Ruta insertada exitosamente");
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar ruta: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene una ruta por su ID
     *
     * @param id ID de la ruta
     * @return Ruta si existe, null en caso contrario
     */
    public Ruta obtenerPorId(int id) {
        String sql = "SELECT * FROM rutas WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ruta: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene todas las rutas
     *
     * @return Lista de todas las rutas
     */
    public List<Ruta> obtenerTodas() {
        List<Ruta> rutas = new ArrayList<>();
        String sql = "SELECT * FROM rutas";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                rutas.add(mapearResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener rutas: " + e.getMessage());
        }
        return rutas;
    }

    /**
     * Obtiene rutas por origen y destino
     *
     * @param origen Origen de la ruta
     * @param destino Destino de la ruta
     * @return Lista de rutas que coinciden
     */
    public List<Ruta> obtenerPorOrigenDestino(String origen, String destino) {
        List<Ruta> rutas = new ArrayList<>();
        String sql = "SELECT * FROM rutas WHERE origen = ? AND destino = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, origen);
            stmt.setString(2, destino);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                rutas.add(mapearResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener rutas: " + e.getMessage());
        }
        return rutas;
    }

    /**
     * Actualiza los datos de una ruta
     *
     * @param ruta Ruta con datos actualizados
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizar(Ruta ruta) {
        String sql = "UPDATE rutas SET nombre = ?, descripcion = ?, origen = ?, destino = ?, duracion = ?, precio = ? WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, ruta.getNombre());
            stmt.setString(2, ruta.getDescripcion());
            stmt.setString(3, ruta.getOrigen());
            stmt.setString(4, ruta.getDestino());
            stmt.setInt(5, ruta.getDuracion());
            stmt.setDouble(6, ruta.getPrecio());
            stmt.setInt(7, ruta.getId());
            
            stmt.executeUpdate();
            System.out.println("Ruta actualizada exitosamente");
            return true;
        } catch (SQLException e) {
            System.err.println("Error al actualizar ruta: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina una ruta de la base de datos
     *
     * @param id ID de la ruta a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM rutas WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Ruta eliminada exitosamente");
            return true;
        } catch (SQLException e) {
            System.err.println("Error al eliminar ruta: " + e.getMessage());
            return false;
        }
    }

    /**
     * Mapea un ResultSet a un objeto Ruta
     *
     * @param rs ResultSet con los datos de la ruta
     * @return Objeto Ruta
     * @throws SQLException Si hay error al acceder a los datos
     */
    private Ruta mapearResultSet(ResultSet rs) throws SQLException {
        Ruta ruta = new Ruta();
        ruta.setId(rs.getInt("id"));
        ruta.setNombre(rs.getString("nombre"));
        ruta.setDescripcion(rs.getString("descripcion"));
        ruta.setOrigen(rs.getString("origen"));
        ruta.setDestino(rs.getString("destino"));
        ruta.setDuracion(rs.getInt("duracion"));
        ruta.setPrecio(rs.getDouble("precio"));
        return ruta;
    }
}
