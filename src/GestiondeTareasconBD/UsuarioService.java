package GestiondeTareasconBD;


import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class UsuarioService {
    private Map<String, Usuario> usuarios = new HashMap<>();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public UsuarioService() {
        cargarUsuariosDesdeBD();
    }

    private void cargarUsuariosDesdeBD() {
        List<Usuario> usuariosList = usuarioDAO.obtenerTodosUsuarios();
        for (Usuario usuario : usuariosList) {
            usuarios.put(usuario.getUsername(), usuario);
        }
    }

    public boolean registrarUsuario(String nombre, String apellido, String username, String contrasena) {
        if (usuarios.containsKey(username)) {
            return false;
        }
        Usuario nuevoUsuario = new Usuario(nombre, apellido, username, contrasena);
        try {
            usuarioDAO.agregarUsuario(nuevoUsuario);
            usuarios.put(username, nuevoUsuario);
            System.out.println("Usuario añadido a la Base de Datos.");
            return true;
        } catch (Exception e) {
            System.err.println("Error al añadir usuario a la Base de Datos: " + e.getMessage());
            return false;
        }
    }

    public boolean autenticarUsuario(String username, String contrasena) {
        Usuario usuario = usuarioDAO.obtenerUsuarioPorUsername(username);
        return usuario != null && usuario.getContrasena().equals(contrasena);
    }

    public Usuario obtenerUsuarioPorUsername(String username) {
        return usuarioDAO.obtenerUsuarioPorUsername(username);
    }


    public List<Usuario> obtenerTodosUsuarios() {
        return usuarioDAO.obtenerTodosUsuarios();
    }

    public void actualizarUsuario(Usuario usuario) {
        usuarioDAO.actualizarUsuario(usuario);
        usuarios.put(usuario.getUsername(), usuario);  // Actualiza en el mapa también
    }


    public boolean eliminarUsuario(int id) {
        try {
            usuarios.remove(usuarioDAO.obtenerUsuarioPorId(id).getUsername());
            usuarioDAO.eliminarUsuario(id);
            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
}