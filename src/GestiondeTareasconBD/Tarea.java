package GestiondeTareasconBD;

public class Tarea {
    private int id;
    private String titulo;
    private String descripcion;
    private String estado;
    private int idUsuario;

    // Constructor para crear una tarea sin ID (para insertar en la base de datos)
    public Tarea(String titulo, String descripcion, String estado, int idUsuario) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.idUsuario = idUsuario;
    }

    // Constructor para crear una tarea con ID (para cuando se recupera de la base de datos)
    public Tarea(int id, String titulo, String descripcion, String estado, int idUsuario) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.idUsuario = idUsuario;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "ID : " + id + "\nTítulo : " + titulo + "\nDescripción : " + descripcion + "\nEstado : " + estado + "\nID Usuario : " + idUsuario + "\n";
    }
}