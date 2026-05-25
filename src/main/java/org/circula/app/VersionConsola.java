package org.circula.app;

import java.util.Scanner;

public class VersionConsola {
    private static final Scanner scanner = new Scanner(System.in);
    private static String usuarioLogueado = null;
    private static String rolLogueado = null;

    public static void main(String[] args) {
        BaseDatos.inicializar();

        boolean salir = false;
        while (!salir) {
            System.out.println("\n=============================================");
            System.out.println("       SISTEMA DE RECICLAJE - CIRCULA        ");
            System.out.println("=============================================");

            if (usuarioLogueado == null) {
                System.out.println("1. Registrarse como Eco-Ciudadano");
                System.out.println("2. Registrarse como Héroe Reciclador");
                System.out.println("3. Iniciar Sesión");
                System.out.println("4. Salir");
                System.out.print("Seleccione una opción: ");

                int opcion = leerEntero();
                switch (opcion) {
                    case 1 -> registrar("CIUDADANO");
                    case 2 -> registrar("RECICLADOR");
                    case 3 -> iniciarSesion();
                    case 4 -> salir = true;
                    default -> System.out.println("Opción no válida.");
                }
            } else {
                if (rolLogueado.equals("CIUDADANO")) {
                    menuCiudadano();
                } else if (rolLogueado.equals("RECICLADOR")) {
                    menuReciclador();
                }
            }
        }
        System.out.println("¡Gracias por usar Circula! Aplicación finalizada.");
    }

    private static void registrar(String rol) {
        System.out.println("\n--- REGISTRO DE NUEVO " + rol + " ---");
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();

        System.out.print("Correo electrónico: ");
        String correo = scanner.nextLine();

        // 🛑 VALIDACIÓN NUEVA: El correo debe contener un '@' obligatoriamente
        if (!correo.contains("@")) {
            System.out.println(" ERROR: El correo electrónico no es válido. Debe incluir el carácter '@'.");
            return; // Detiene el proceso y regresa al menú para que lo intente de nuevo
        }

        System.out.print("Contraseña: ");
        String clave = scanner.nextLine();

        // Envía los datos a guardar
        boolean exito = BaseDatos.registrarUsuario(correo, clave, nombre, rol);

        if (exito) {
            System.out.println("¡Registro exitoso! Ya puedes iniciar sesión en el sistema.");
        } else {
            // Aquí entra si intentan clonar un correo que ya se usó (sea ciudadano o reciclador)
            System.out.println("❌ ERROR: Este correo ya se encuentra registrado con una cuenta existente.");
        }
    }

    private static void iniciarSesion() {
        System.out.println("\n--- INICIAR SESIÓN ---");
        System.out.print("Correo electrónico: ");
        String correo = scanner.nextLine();
        System.out.print("Contraseña: ");
        String clave = scanner.nextLine();

        String rol = BaseDatos.login(correo, clave);
        if (rol != null) {
            usuarioLogueado = correo;
            rolLogueado = rol;
            System.out.println("¡Ingreso exitoso! Bienvenido(a) " + correo);
        } else {
            System.out.println("Credenciales incorrectas o el usuario no existe.");
        }
    }

    private static void menuCiudadano() {
        System.out.println("\n--- MENÚ ECO-CIUDADANO ---");
        System.out.println("1. Solicitar Recogida de Residuos");
        System.out.println("2. Cerrar Sesión");
        System.out.print("Seleccione: ");

        int opcion = leerEntero();
        switch (opcion) {
            case 1 -> {
                System.out.print("Ingrese dirección de recogida: ");
                String dir = scanner.nextLine();
                System.out.print("Describa el material (ej: 4kg de plástico): ");
                String residuo = scanner.nextLine();
                BaseDatos.crearSolicitud(usuarioLogueado, dir, residuo);
            }
            case 2 -> {
                usuarioLogueado = null;
                rolLogueado = null;
                System.out.println("Sesión cerrada.");
            }
            default -> System.out.println("Opción no válida.");
        }
    }

    private static void menuReciclador() {
        System.out.println("\n--- PANEL DE LOGÍSTICA (RECICLADOR) ---");
        System.out.println("1. Ver Recolecciones Pendientes en la Ciudad");
        System.out.println("2. Cerrar Sesión");
        System.out.print("Seleccione: ");

        int opcion = leerEntero();
        switch (opcion) {
            case 1 -> BaseDatos.mostrarSolicitudesPendientes();
            case 2 -> {
                usuarioLogueado = null;
                rolLogueado = null;
                System.out.println("Sesión cerrada.");
            }
            default -> System.out.println("Opción no válida.");
        }
    }

    private static int leerEntero() {
        try {
            int numero = scanner.nextInt();
            scanner.nextLine();
            return numero;
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }
}
