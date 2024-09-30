package GestiondeTareasconBD;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TareaService {
    private Map<Integer, Tarea> tareas = new HashMap<>();
    private TareaDAO tareaDAO = new TareaDAO();

    public TareaService() {
        cargarTareasDesdeBD();
    }

    private void cargarTareasDesdeBD() {
        List<Tarea> tareasList = tareaDAO.obtenerTodasTareas();
        for (Tarea tarea : tareasList) {
            tareas.put(tarea.getId(), tarea);
        }
    }

    public boolean crearTarea(String titulo, String descripcion, String estado, int idUsuario) {
        Tarea nuevaTarea = new Tarea(titulo, descripcion, estado, idUsuario);
        try {
            int idGenerado = tareaDAO.agregarTarea(nuevaTarea);
            nuevaTarea.setId(idGenerado);  // Establece el ID generado
            tareas.put(idGenerado, nuevaTarea);
            System.out.println("Tarea añadida a la Base de Datos.");
            return true;
        } catch (Exception e) {
            System.err.println("Error al añadir tarea a la Base de Datos: " + e.getMessage());
            return false;
        }
    }

    public Tarea obtenerTareaPorId(int id) {
        return tareaDAO.obtenerTareaPorId(id);
    }

    public List<Tarea> obtenerTareasPorUsuario(int idUsuario) {
        return tareaDAO.obtenerTareasPorUsuario(idUsuario);
    }

    public boolean actualizarTarea(Tarea tarea) {
        try {
            tareaDAO.actualizarTarea(tarea);
            tareas.put(tarea.getId(), tarea);  // Actualiza en el mapa también
            return true;
        } catch (Exception e) {
            System.err.println("Error al actualizar tarea: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarTarea(int id) {
        try {
            tareas.remove(id);
            tareaDAO.eliminarTarea(id);
            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar tarea: " + e.getMessage());
            return false;
        }
    }
}