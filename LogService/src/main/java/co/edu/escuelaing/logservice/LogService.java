/**
 * Clase principal que representa un servicio de registro almacenando mensajes en MongoDB.
 * Creado por Daniel Esteban Pérez Bohórquez el 2 de octubre de 2023.
 *
 * @author Daniel Esteban Pérez Bohórquez
 */
package co.edu.escuelaing.logservice;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

import static spark.Spark.*;

public class LogService {

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;

    /**
     * Punto de entrada principal para iniciar el servicio de registro.
     *
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        try {
            // Crear la conexión a MongoDB una vez
            mongoClient = MongoClients.create("mongodb://db:27017");
            database = mongoClient.getDatabase("chat_bd");

            // Verificar y crear la colección si no existe
            if (!collectionExists("mensajes")) {
                database.createCollection("mensajes");
            }

        } catch (Exception e) {
            System.err.println("Error interacting with MongoDB: " + e.getMessage());
        }

        System.out.println("Log Service Server");

        port(getPort());

        get("/logservice", (req, res) -> {
            String val = req.queryParams("message");
            System.out.println("MENSAJE " + val);

            // Insertar el documento en la colección de MongoDB
            insertDocument("mensajes", val);

            // Obtener los últimos 10 mensajes de la colección
            String last10Messages = getLast10Messages("mensajes");

            return last10Messages;
        });
    }

    /**
     * Obtiene el puerto del entorno o utiliza el puerto predeterminado si no
     * está configurado.
     *
     * @return Puerto a utilizar.
     */
    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4568;
    }

    /**
     * Llama al método remoto de registro utilizando la clase HttpRemoteCaller.
     *
     * @param val Mensaje de registro a enviar.
     */
    private static String logMessage(String val) {
        // Puedes agregar lógica adicional aquí si es necesario

        // Obtener los últimos 10 mensajes de la colección
        return getLast10Messages("mensajes");
    }

    /**
     * Inserta un documento en la colección de MongoDB.
     *
     * @param collectionName Nombre de la colección.
     * @param val Mensaje a insertar en el documento.
     */
    private static void insertDocument(String collectionName, String val) {
        try {
            // Obtener la colección existente
            collection = database.getCollection(collectionName);

            // Crear un documento con el parámetro 'val' y la fecha/hora actual
            Document document = new Document("message", val)
                    .append("timestamp", getCurrentTimestamp());

            // Insertar el documento en la colección
            collection.insertOne(document);

            System.out.println("Document inserted into MongoDB collection.");
        } catch (Exception e) {
            System.err.println("Error inserting document into MongoDB: " + e.getMessage());
        }
    }

    /**
     * Obtiene la fecha y hora actual en un formato específico.
     *
     * @return Fecha y hora actual como cadena de texto.
     */
    private static String getCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    /**
     * Obtiene los últimos 10 mensajes de la colección de MongoDB.
     *
     * @param collectionName Nombre de la colección.
     * @return Últimos 10 mensajes en formato JSON.
     */
    private static String getLast10Messages(String collectionName) {
        try {
            // Obtener la colección existente
            collection = database.getCollection(collectionName);

            // Encontrar los últimos 10 documentos en la colección
            List<Document> last10Documents = collection.find().sort(Sorts.descending("_id")).limit(10).into(new ArrayList<>());

            // Convertir los documentos al formato JSON
            List<String> messages = new ArrayList<>();
            for (Document document : last10Documents) {
                messages.add(document.toJson());
            }

            // Unir los mensajes en una sola cadena
            return String.join(",", messages);
        } catch (Exception e) {
            System.err.println("Error retrieving last 10 messages from MongoDB: " + e.getMessage());
            return "Error retrieving messages.";
        }
    }

    /**
     * Verifica si la colección especificada existe en la base de datos.
     *
     * @param collectionName Nombre de la colección.
     * @return `true` si la colección existe, `false` de lo contrario.
     */
    private static boolean collectionExists(String collectionName) {
        for (String name : database.listCollectionNames()) {
            if (name.equals(collectionName)) {
                return true;
            }
        }
        return false;
    }
}
