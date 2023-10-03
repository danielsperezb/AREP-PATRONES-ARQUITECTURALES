/**
 * Clase principal que inicia un servidor Spark para el servicio de registro distribuido.
 * Creado por Daniel Esteban Pérez Bohórquez el 2 de octubre de 2023.
 *
 * @author Daniel Esteban Pérez Bohórquez
 */
package co.edu.escuelaing.logroundrobin;

import java.io.IOException;

import static spark.Spark.*;

public class LogRoundRobin {

    /**
     * Punto de entrada principal para iniciar el servidor Spark y configurar rutas.
     *
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        port(getPort());

        staticFiles.location("/public");

        get("/log", (req, res) -> {
            String val = req.queryParams("message");
            System.out.println("VALOR" + val);
            return logMessage(val);
        });
    }

    /**
     * Obtiene el puerto del entorno o utiliza el puerto predeterminado si no está configurado.
     *
     * @return Puerto a utilizar.
     */
    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }

    /**
     * Llama al método remoto de registro utilizando la clase HttpRemoteCaller.
     *
     * @param val Mensaje de registro a enviar.
     * @return Respuesta del servicio remoto.
     * @throws IOException Si hay un error de entrada/salida durante la llamada remota.
     */
    private static String logMessage(String val) throws IOException {
        return HttpRemoteCaller.remoteLogCall(val);
    }
}
