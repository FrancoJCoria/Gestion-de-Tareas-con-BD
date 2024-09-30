package GestiondeTareasconBD;

public class Usuario {
    private int id;
    private String nombre;
    private String apellido;
    private String username;
    private String contrasena;

    // Constructor sin id (para nuevos usuarios)
    public Usuario(String nombre, String apellido, String username, String contrasena) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = username;
        this.contrasena = contrasena;
    }

    // Constructor con id (para usuarios existentes)
    public Usuario(int id, String nombre, String apellido, String username, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = username;
        this.contrasena = contrasena;
    }

    // Getters
    public int getId() {
        return this.id;
    }
    public String getNombre() {
        return this.nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public String getUsername() {
        return this.username;
    }

    public String getContrasena() {
        return this.contrasena;
    }

}