/**
 * Clase que representa un cliente HTTP para realizar llamadas a servicios de registro distribuidos.
 * Creado por Daniel Esteban Pérez Bohórquez el 2 de octubre de 2023.
 *
 * @author Daniel Esteban Pérez Bohórquez
 */
package co.edu.escuelaing.logroundrobin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRemoteCaller {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String[] LOG_SERVICES = new String[]{
    "http://firstlogservicecontainer:6000/logservice",
    "http://secondlogservicecontainer:6000/logservice",
    "http://thirdlogservicecontainer:6000/logservice"
};

    private static int currentServer = 0;

    /**
     * Realiza una llamada remota al servidor actual usando el método remoteHttpCall.
     *
     * @param message Mensaje a enviar al servicio remoto.
     * @return Respuesta del servicio remoto.
     * @throws IOException Si hay un error de entrada/salida durante la llamada remota.
     */
    public static String remoteLogCall(String message) throws IOException {
        return remoteHttpCall(LOG_SERVICES[currentServer], message);
    }

    /**
     * Realiza una llamada HTTP remota a la URL especificada con el mensaje proporcionado.
     *
     * @param url     URL del servicio remoto.
     * @param message Mensaje a enviar al servicio remoto.
     * @return Respuesta del servicio remoto.
     * @throws IOException Si hay un error de entrada/salida durante la llamada remota.
     */
    public static String remoteHttpCall(String url, String message) throws IOException {
        URL obj = new URL(url + "?message=" + message);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("Código de respuesta GET :: " + responseCode);
        StringBuffer response = new StringBuffer();
        System.out.println("RESPUESTA: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println(response.toString());
        } else {
            System.out.println("La solicitud GET no funcionó");
            return "404 error";
        }

        System.out.println("GET COMPLETADO");
        rotateRoundRobinServer();
        System.out.println("RESPUESTA: " + response.toString());
        return response.toString();
    }

    /**
     * Rota al siguiente servidor en el arreglo de servicios de registro.
     */
    public static void rotateRoundRobinServer() {
        currentServer = (currentServer + 1) % 3;
    }
}
