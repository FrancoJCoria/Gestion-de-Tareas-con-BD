package GestiondeTareasconBD;

import java.util.List;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static UsuarioService usuarioService = new UsuarioService();
    private static TareaService tareaService = new TareaService();
    private static String usuarioActual = null;

    public static void main(String[] args) {

        // Intentar conectar a la base de datos
        try (Connection conn = Util.getConnection()) {
            if (conn != null) {
                System.out.println("Conexión exitosa a la base de datos!");
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }

        // Iniciar sistema
        while (true) {
            if (usuarioActual == null) {
                mostrarMenuPrincipal();
            } else {
                mostrarMenuUsuario();
            }
        }
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n--- Menú Principal ---");
        System.out.println("1. Registrarse");
        System.out.println("2. Iniciar sesión");
        System.out.println("3. Salir");
        System.out.print("Seleccione una opción: ");

        int opcion = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        switch (opcion) {
            case 1 -> registrarUsuario();
            case 2 -> iniciarSesion();

            case 3 -> {
                System.out.println("¡Hasta luego!");
                System.exit(0);
            }
            default -> System.out.println("Opción no válida. Intente de nuevo.");
        }
    }

    private static void mostrarMenuUsuario() {
        System.out.println("\n--- Menú de Usuario ---");
        System.out.println("1. Crear tarea");
        System.out.println("2. Ver tareas");
        System.out.println("3. Actualizar tarea");
        System.out.println("4. Eliminar tarea");
        System.out.println("5. Actualizar perfil");
        System.out.println("6. Cerrar sesión");
        System.out.print("Seleccione una opción: ");

        int opcion = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        int idUsuarioActual = usuarioService.obtenerUsuarioPorUsername(usuarioActual).getId();

        switch (opcion) {
            case 1 -> crearTarea(idUsuarioActual);
            case 2 -> verTareas(idUsuarioActual);
            case 3 -> actualizarTarea(idUsuarioActual);
            case 4 -> eliminarTarea(idUsuarioActual);
            case 5 -> actualizarPerfil();
            case 6 -> cerrarSesion();
            default -> System.out.println("Opción no válida. Intente de nuevo.");
        }
    }

    private static void registrarUsuario() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("Usuario: ");
        String username = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        if (usuarioService.registrarUsuario(nombre, apellido, username, contrasena)) {
            System.out.println("Usuario registrado con éxito.");
        } else {
            System.out.println("El nombre de usuario ya existe. Intente con otro.");
        }
    }


    private static void iniciarSesion() {
        System.out.print("Ingrese nombre de usuario: ");
        String username = scanner.nextLine();
        System.out.print("Ingrese contraseña: ");
        String contrasena = scanner.nextLine();

        if (usuarioService.autenticarUsuario(username, contrasena)) {
            usuarioActual = username;
            System.out.println("Sesión iniciada con éxito.");
        } else {
            System.out.println("Nombre de usuario o contraseña incorrectos.");
        }
    }

    private static void crearTarea(int idUsuarioActual) {
        System.out.print("Ingrese título de la tarea: ");
        String titulo = scanner.nextLine();
        System.out.print("Ingrese descripción de la tarea: ");
        String descripcion = scanner.nextLine();
        System.out.print("Ingrese estado de la tarea (Nuevo/Pendiente/Finalizado): ");
        String estado = scanner.nextLine();

        if(tareaService.crearTarea(titulo, descripcion, estado, idUsuarioActual)){
            System.out.println("Tarea creada con éxito.");
        } else {
            System.out.println("Hubo un problema con la creación de la tarea.");
        }
    }

    private static void verTareas(int idUsuarioActual) {
        List<Tarea> tareas = tareaService.obtenerTareasPorUsuario(idUsuarioActual);
        if (tareas.isEmpty()) {
            System.out.println("No hay tareas para mostrar.");
        } else {
            System.out.println(" Sus tareas:");
            // Definir el formato de la tabla
            String formato = "| %12s | %-75s |%n";

            for (Tarea tarea : tareas) {
                System.out.printf(formato, tarea.getId(), tarea.getTitulo());
                System.out.printf(formato, "Descripcion", tarea.getDescripcion());
                System.out.printf(formato, "Estado", tarea.getEstado());
                System.out.println("+--------------+-----------------------------------------------------------------------------+");
            }
        }
    }

    private static void actualizarTarea(int idUsuarioActual) {
        verTareas(idUsuarioActual);

        System.out.print("Ingrese ID de la tarea a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        // Veirificar si existe la tarea
        if (tareaService.obtenerTareaPorId(id) == null) {
            System.out.println("Prueba con otro ID."); //Avisar al usuario que intente otro ID
            return;
        }

        if (tareaService.obtenerTareaPorId(id).getIdUsuario() != idUsuarioActual) {
            System.out.println("La tarea seleccionada no te pertenece.");
        } else {
            Tarea tareaActualizarObj = tareaService.obtenerTareaPorId(id);

            if (tareaActualizarObj == null) {
                System.out.println("No se encontró la tarea.");
                return;
            }

            System.out.println("\n--- Actualizar tarea ---");
            System.out.println("\n(Presiona Enter para no modificar el campo)\n");

            System.out.print("Nuevo título (actual: " + tareaActualizarObj.getTitulo() + "): ");
            String nuevoTitulo = scanner.nextLine();
            if (nuevoTitulo.isEmpty()) {
                nuevoTitulo = tareaActualizarObj.getTitulo();
            }

            System.out.print("Nueva descripción: ");
            String nuevaDescripcion = scanner.nextLine();
            if (nuevaDescripcion.isEmpty()) {
                nuevaDescripcion = tareaActualizarObj.getDescripcion();
            }

            System.out.print("Nuevo estado (Nuevo/Pendiente/Finalizado): ");
            String nuevoEstado = scanner.nextLine();
            if (nuevoEstado.isEmpty()) {
                nuevoEstado = tareaActualizarObj.getEstado();
            }

            Tarea nuevaTarea = new Tarea(id, nuevoTitulo, nuevaDescripcion, nuevoEstado, idUsuarioActual);

            tareaActualizarObj = nuevaTarea;

            if (tareaService.actualizarTarea(tareaActualizarObj)) {
                System.out.println("Tarea actualizada con éxito.");
            } else {
                System.out.println("No se pudo actualizar la tarea. Verifique el ID.");
            }
        }
    }

    private static void eliminarTarea(int idUsuarioActual) {
        verTareas(idUsuarioActual);
        System.out.print("Ingrese ID de la tarea a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        // Veirificar si existe la tarea
        if (tareaService.obtenerTareaPorId(id) == null) {
            System.out.println("Prueba con otro ID."); //Avisar al usuario que intente otro ID
            return;
        }

        if (tareaService.obtenerTareaPorId(id).getIdUsuario() != idUsuarioActual) {
            System.out.println("La tarea seleccionada no te pertenece.");
        } else {
            if (tareaService.eliminarTarea(id)) {
                System.out.println("Tarea eliminada con éxito.");
            } else {
                System.out.println("No se pudo eliminar la tarea. Verifique el ID.");
            }
        }
    }

    private static void actualizarPerfil() {
        Usuario usuarioActualObj = usuarioService.obtenerUsuarioPorUsername(usuarioActual);

        if (usuarioActualObj == null) {
            System.out.println("No se encontró el usuario actual.");
            return;
        }

        System.out.println("\n--- Actualizar perfil ---");
        System.out.println("\n(Presiona Enter para no modificar el campo)\n");

        System.out.print("Nuevo nombre (actual: " + usuarioActualObj.getNombre() + "): ");
        String nuevoNombre = scanner.nextLine();
        if (nuevoNombre.isEmpty()) {
            nuevoNombre = usuarioActualObj.getNombre();  // Si se presiona Enter, mantiene el valor actual
        }

        System.out.print("Nuevo apellido (actual: " + usuarioActualObj.getApellido() + "): ");
        String nuevoApellido = scanner.nextLine();
        if (nuevoApellido.isEmpty()) {
            nuevoApellido = usuarioActualObj.getApellido();  // Si se presiona Enter, mantiene el valor actual
        }

        System.out.print("Nuevo nombre de usuario (actual: " + usuarioActualObj.getUsername() + "): ");
        String nuevoUsername = scanner.nextLine();
        if (nuevoUsername.isEmpty()) {
            nuevoUsername = usuarioActualObj.getUsername();  // Si se presiona Enter, mantiene el valor actual
        }

        System.out.print("Nueva contraseña: ");
        String nuevaContrasena = scanner.nextLine();
        if (nuevaContrasena.isEmpty()) {
            nuevaContrasena = usuarioActualObj.getContrasena();  // Si se presiona Enter, mantiene el valor actual
        }

        Usuario usuarioActualizado = new Usuario(
                usuarioActualObj.getId(),
                nuevoNombre,
                nuevoApellido,
                nuevoUsername,
                nuevaContrasena
        );

        // Actualizamos en el servicio y la base de datos
        usuarioService.actualizarUsuario(usuarioActualizado);
        System.out.println("Perfil actualizado con éxito.");
    }


    private static void cerrarSesion() {
        usuarioActual = null;
        System.out.println("Sesión cerrada.");
    }
}