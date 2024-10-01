# Proyecto Programación 2
Repositorio para alojar el proyecto que se desarrolló en la materia **Programación 2**.
## Proyecto

**Gestión de Tareas**:
    - **Objetivo**: Desarrollar una aplicación de consola en Java para gestionar tareas personales, donde los usuarios puedan registrarse y realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre sus tareas.
    - **Requerimientos**:
        1. **Registro de Usuarios**:
            - Los usuarios deben poder registrarse proporcionando un nombre de usuario y una contraseña.
            - No se permiten nombres de usuario duplicados.
            - La aplicación debe almacenar temporalmente los usuarios registrados en memoria.
        1. **Gestión de Tareas**:
            - **Crear Tarea**: Los usuarios deben poder crear nuevas tareas proporcionando un título, una descripción y asignando un estado inicial (Nuevo, Pendiente, Finalizado).
            - **Ver Tareas**: Los usuarios deben poder ver la lista de todas sus tareas con su ID, título, descripción y estado.
            - **Actualizar Tarea**: Los usuarios deben poder actualizar el título, la descripción y el estado de una tarea específica, identificándola por su ID.
            - **Eliminar Tarea**: Los usuarios deben poder eliminar una tarea específica, identificándola por su ID.
        1. **Cerrar sesión**:
            - Los usuarios deben poder cerrar sesión y regresar al menú principal.
1. **Implementar Base de Datos al Proyecto**:
    - **Objetivo**: Integrar la gestión de tareas con una base de datos relacional.
    - **Configuración de la Base de Datos**
        1. **Inicia `XAMPP`** y asegúrate de que los módulos de Apache y MySQL estén en ejecución.
        1. **Crea la base de datos** en MySQL:
            - Abre `phpMyAdmin` desde XAMPP.
            - creamos una base de datos con el nombre "gestion_tareas"
            ``` SQL
            CREATE DATABASE gestion_tareas;
            ```
        1. Creamos las tablas usuarios y tareas:
            ``` SQL
            USE gestion_tareas;

            CREATE TABLE usuarios (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(100) NOT NULL,
                apellido VARCHAR(100) NOT NULL,
                usuario VARCHAR(50) NOT NULL UNIQUE,
                contraseña VARCHAR(255) NOT NULL
            );

            CREATE TABLE tareas (
                id INT AUTO_INCREMENT PRIMARY KEY,
                titulo VARCHAR(255) NOT NULL,
                descripcion TEXT,
                estado ENUM('Nuevo', 'Pendiente', 'Finalizado') NOT NULL,
                idUsuario INT,
                FOREIGN KEY (idUsuario) REFERENCES usuarios(id)
            );
            ```
        1. Para poder conectarnos a la base datos MySQL es necesario que agregar la biblioteca de MySQL Connector/J. Para eso, lo primero que haremos es descargar el driver desde el siguiente [link](https://dev.mysql.com/downloads/connector/j/?os=26). Descargarnos el archivo ya se en TAR o ZIP.
        1. Luego agregamos esta librería a nuestro proyecto java.
            1. Hacemos click derecho en nuestro proyecto y nos dirigimos a `propiedades`:
            ![](https://kaizen-software.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F480959ae-2a3f-4d71-9285-c829eb239c67%2F3976fb3a-bb70-4878-9669-2e3ce0522c0a%2Fimage.png?table=block&id=fff240b2-54df-806b-99dc-d45338419b91&spaceId=480959ae-2a3f-4d71-9285-c829eb239c67&width=1020&userId=&cache=v2)
            1. Luego iremos a la opción de “libreriasˮ y seleccionaremos `Classpath` y agregaremos el .jar de la librería que descargamos.
            ![](https://kaizen-software.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F480959ae-2a3f-4d71-9285-c829eb239c67%2Ff0c35cab-f3df-431a-b420-e38695bbc321%2Fimage.png?table=block&id=e7f55689-79fa-42e3-a7e9-17384cb49224&spaceId=480959ae-2a3f-4d71-9285-c829eb239c67&width=1360&userId=&cache=v2)
        1. Dentro del proyecto java en netbeans vamos a crear una clase util para configurar la conexión a la base de datos o podemos crearla en misma clase principal.
            ``` java
            package gestiontareasconsola;

            import java.sql.Connection;
            import java.sql.DriverManager;
            import java.sql.SQLException;

            public class Util{
                private static final String URL = "jdbc:mysql://localhost:3306/gestion_tareas";// Cambia gestion_tareas por el nombre de tu db
                private static final String USER = "root"; // Cambia si tu usuario es diferente
                private static final String PASSWORD = ""; // Cambia si tienes una contraseña

                public static Connection getConnection() throws SQLException {
                    return DriverManager.getConnection(URL, USER, PASSWORD);
                }
            }
            ```
        1. Para probar si establecemos de manera exitosa la conexión con la base de datos podemos hacer una prueba desde nuestra función principal o main.
            ``` java
            package gestiontareasconsola;
            
            import java.sql.Connection;
            import java.sql.SQLException;

            public class GestionTareasConsola {
                public static void main(String[] args) {
                    // TODO code application logic here
                    
                    // Intentar conectar a la base de datos
                    try (Connection conn = Util.getConnection()) {
                        if (conn != null) {
                            System.out.println("Conexión exitosa a la base de datos!");
                        }
                    } catch (SQLException e) {
                        System.out.println("Error al conectar a la base de datos: " + e.getMessage());
                    }   
                }    
            }
            ```
        1. Si la conexión fue exitosa debería figurarnos por consola que “Conexión exitosa a la base de datos!”
    - Una vez finalizada la etapa de configuración ya podemos implementar las capas de integración con la base de datos. Para eso haremos uso de un patrón de diseño que se utiliza para separar la lógica de acceso a datos de la lógica de negocio en una aplicación. En el contexto de SQL, un DAO es una clase o componente que se encarga de realizar las operaciones de acceso a la base de datos (como consultas ``SELECT``, inserciones ``INSERT``, actualizaciones ``UPDATE`` y eliminaciones ``DELETE``) sin exponer los detalles del motor de base de datos o las consultas SQL al resto de la aplicación.<br>
    A modo de ejemplo mostraremos la integración la base de datos con la clase Usuario. Se creará una clase ``UsuarioDAO``.<br>
        ``` java
        package gestiontareasconsola;


        import java.sql.Connection;
        import java.sql.PreparedStatement;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.util.ArrayList;
        import java.util.List;


        public class UsuarioDAO {
            
            public void agregarUsuario(Usuario usuario) {
                String sql = "INSERT INTO usuarios (nombre, apellido, usuario, contraseña) VALUES (?, ?, ?, ?)";
                try (Connection conn = Util.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, usuario.getNombre());
                    stmt.setString(2, usuario.getApellido());
                    stmt.setString(3, usuario.getUsuario());
                    stmt.setString(4, usuario.getContraseña());
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            
            public Usuario obtenerUsuarioPorId(int id) {
                String sql = "SELECT * FROM usuarios WHERE id = ?";
                try (Connection conn = Util.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, id);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("usuario"),
                            rs.getString("contraseña")
                        );
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
            
            public List<Usuario> obtenerTodosUsuarios() {
                List<Usuario> usuarios = new ArrayList<>();
                String sql = "SELECT * FROM usuarios";
                try (Connection conn = Util.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Usuario usuario = new Usuario(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("usuario"),
                            rs.getString("contraseña")
                        );
                        usuarios.add(usuario);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return usuarios;
            }
            
            public void actualizarUsuario(Usuario usuario) {
                String sql = "UPDATE usuarios SET nombre = ?, apellido = ?, usuario = ?, contraseña = ? WHERE id = ?";
                try (Connection conn = Util.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, usuario.getNombre());
                    stmt.setString(2, usuario.getApellido());
                    stmt.setString(3, usuario.getUsuario());
                    stmt.setString(4, usuario.getContraseña());
                    stmt.setInt(5, usuario.getId());
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            
            public void eliminarUsuario(int id) {
                String sql = "DELETE FROM usuarios WHERE id = ?";
                try (Connection conn = Util.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            
        }  
        ```
        En la clase UsuarioDAO podemos visualizar las cuatro operaciones básicas que se pueden realizar en bases de datos o sistemas de gestión de información: **Create (Crear)**, **Read (Leer)**, **Update (Actualizar)** y **Delete (Eliminar).** <br>
        En el siguiente código podemos visualizar como se implementa una de estas operaciones básicas desde nuestra clase principal.<br>
        ``` java
        package gestiontareasconsola;
        /**
        * @author silva
        */
        import java.sql.Connection;
        import java.sql.SQLException;

        public class GestionTareasConsola {
            public static void main(String[] args) {
                // TODO code application logic here
                
                // Intentar conectar a la base de datos
                try (Connection conn = Util.getConnection()) {
                    if (conn != null) {
                        System.out.println("Conexión exitosa a la base de datos!");
                    }
                } catch (SQLException e) {
                    System.out.println("Error al conectar a la base de datos: " + e.getMessage());
                }   
                
                UsuarioDAO  usuarioDAO = new UsuarioDAO();
                // Creación de un nuevo usuario
                Usuario nuevoUsuario = new Usuario(null,"Matias","Endres","mendres","123456");
                usuarioDAO.agregarUsuario(nuevoUsuario);
                // Traer todos los usuarios 
                System.out.println(usuarioDAO.obtenerTodosUsuarios());
                
            
                
            }    
        }
        ```
        **Ejercicio**: Implementar la lógica necesaria para finalizar la persistencia de datos del gestor de tareas.
---
*Coria Franco Javier*
