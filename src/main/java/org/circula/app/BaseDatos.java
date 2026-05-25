package org.circula.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class BaseDatos {
    private static final String URL = "jdbc:sqlite:circula.db";

    // ⚡ BLOQUE ESTÁTICO: Fuerza a NetBeans a cargar el Driver de SQLite apenas arranque la app
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("🛑 ERROR CRÍTICO: NetBeans aún no reconoce el archivo .jar de SQLite.");
            System.out.println("Por favor, revisa que esté en la pestaña de 'Libraries' del proyecto.");
        }
    }

    public static void inicializar() {
        try (Connection con = DriverManager.getConnection(URL); 
             Statement stmt = con.createStatement()) {
            
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS usuarios (
                    correo TEXT PRIMARY KEY,
                    password TEXT NOT NULL,
                    nombre TEXT NOT NULL,
                    rol TEXT NOT NULL
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS solicitudes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    correo_usuario TEXT,
                    direccion TEXT,
                    residuo TEXT,
                    estado TEXT DEFAULT 'Pendiente'
                );
            """);
            
            System.out.println("[SQLite] Base de datos conectada y verificada.");
        } catch (Exception e) {
            System.out.println("Error al inicializar la base de datos: " + e.getMessage());
        }
    }

    public static boolean registrarUsuario(String correo, String pass, String nombre, String rol) {
        String sql = "INSERT INTO usuarios(correo, password, nombre, rol) VALUES(?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(URL); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, correo.toLowerCase().trim());
            ps.setString(2, pass);
            ps.setString(3, nombre);
            ps.setString(4, rol);
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            return false; 
        }
    }

    public static String login(String correo, String pass) {
        String sql = "SELECT rol FROM usuarios WHERE correo = ? AND password = ?";
        try (Connection con = DriverManager.getConnection(URL); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, correo.toLowerCase().trim());
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getString("rol");
            }
        } catch (Exception e) {
            System.out.println("Error en el login: " + e.getMessage());
        }
        return null;
    }

    public static void crearSolicitud(String correo, String direccion, String residuo) {
        String sql = "INSERT INTO solicitudes(correo_usuario, direccion, residuo) VALUES(?, ?, ?)";
        try (Connection con = DriverManager.getConnection(URL); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, correo);
            ps.setString(2, direccion);
            ps.setString(3, residuo);
            ps.executeUpdate();
            
            System.out.println("¡Solicitud registrada con éxito en SQLite!");
        } catch (Exception e) {
            System.out.println("Error al crear solicitud: " + e.getMessage());
        }
    }

    public static void mostrarSolicitudesPendientes() {
        String sql = "SELECT * FROM solicitudes WHERE estado = 'Pendiente'";
        try (Connection con = DriverManager.getConnection(URL); 
             Statement stmt = con.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\n--- ÓRDENES DE RECOGIDA PENDIENTES ---");
            boolean ordenesDisponibles = false;
            while (rs.next()) {
                ordenesDisponibles = true;
                System.out.println("ID: #" + rs.getInt("id") + 
                                   " | Ciudadano: " + rs.getString("correo_usuario") + 
                                   " | Dirección: " + rs.getString("direccion") + 
                                   " | Detalles: " + rs.getString("residuo"));
            }
            if (!ordenesDisponibles) {
                System.out.println("No hay recolecciones pendientes en la ciudad.");
            }
        } catch (Exception e) {
            System.out.println("Error al consultar solicitudes: " + e.getMessage());
        }
    }

    public static java.util.List<String[]> obtenerSolicitudesPendientesG() {
        java.util.List<String[]> lista = new java.util.ArrayList<>();
        String sql = "SELECT * FROM solicitudes WHERE estado = 'Pendiente'";
        try (Connection con = DriverManager.getConnection(URL);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("correo_usuario"),
                        rs.getString("direccion"),
                        rs.getString("residuo")
                });
            }
        } catch (Exception e) {
            System.out.println("Error al empaquetar solicitudes para GUI: " + e.getMessage());
        }
        return lista;


    }
    public static java.sql.Connection conectar() {
        String url = "jdbc:sqlite:circula.db"; // O el nombre de tu archivo .db
        try {
            return java.sql.DriverManager.getConnection(url);
        } catch (java.sql.SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return null;
        }
    }

    public static int[] obtenerDatosUsuario(String correo) {
        int[] datos = new int[2]; // datos[0] = puntos, datos[1] = monedero/ganancias
        String sql = "SELECT puntos, monedero FROM usuarios WHERE correo = ?";

        try (java.sql.Connection conn = conectar();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, correo);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    datos[0] = rs.getInt("puntos");
                    datos[1] = rs.getInt("monedero"); // ⚠️ Nota: Si en tu tabla pusiste "ganancias", cambia este texto por "ganancias"
                }
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Error al obtener puntos/monedero: " + e.getMessage());
        }
        return datos;
    }


    public static boolean canjearPremio(String correo, int costoPuntos) {
        // 1. Obtener los puntos actuales que tiene el ciudadano
        int[] datos = obtenerDatosUsuario(correo);
        int puntosActuales = datos[0]; // Recuerda que el índice 0 son los Eco-Puntos

        // 2. Condición: Verificar si los puntos le alcanzan para el premio
        if (puntosActuales >= costoPuntos) {
            int nuevosPuntos = puntosActuales - costoPuntos;

            // 3. Si le alcanza, restamos los puntos en la base de datos
            String sql = "UPDATE usuarios SET puntos = ? WHERE correo = ?";

            try (java.sql.Connection conn = conectar();
                 java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, nuevosPuntos);
                pstmt.setString(2, correo);
                pstmt.executeUpdate();

                return true; // Retorna true si el canje fue exitoso

            } catch (java.sql.SQLException e) {
                System.out.println("Error al descontar puntos del premio: " + e.getMessage());
            }
        }

        return false; // Retorna false si no tenía puntos suficientes o hubo un error
    }
    public static boolean completarSolicitud(int idSolicitud, double peso, String material, String correoReciclador) {
        // 1. Calcular las ganancias (Ej: $2.000 COP por cada Kilogramo registrado)
        int pagoCalculado = (int) (peso * 2000);

        // 2. Sentencias SQL necesarias:
        // - Sumar el dinero al monedero del reciclador en la tabla 'usuarios'
        // - Eliminar la orden de la tabla 'solicitudes' para que desaparezca de la lista de pendientes
        String sqlMonedero = "UPDATE usuarios SET monedero = monedero + ? WHERE correo = ?";
        String sqlBorrarSolicitud = "DELETE FROM solicitudes WHERE id = ?";

        try (java.sql.Connection conn = conectar()) {
            if (conn == null) return false;

            // Activamos una transacción segura (o se ejecutan ambos cambios o ninguno)
            conn.setAutoCommit(false);

            // A. Pagarle al reciclador
            try (java.sql.PreparedStatement pstmtRec = conn.prepareStatement(sqlMonedero)) {
                pstmtRec.setInt(1, pagoCalculado);
                pstmtRec.setString(2, correoReciclador);
                pstmtRec.executeUpdate();
            }

            // B. Eliminar la solicitud ya atendida
            try (java.sql.PreparedStatement pstmtSol = conn.prepareStatement(sqlBorrarSolicitud)) {
                pstmtSol.setInt(1, idSolicitud);
                pstmtSol.executeUpdate();
            }

            // Guardar cambios en la base de datos de forma definitiva
            conn.commit();
            return true;

        } catch (java.sql.SQLException e) {
            System.out.println("Error crítico al completar la solicitud: " + e.getMessage());
            return false;
        }
    }
}