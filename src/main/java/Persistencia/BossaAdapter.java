package Persistencia;

import com.google.gson.*;
import Domini.Bossa;
import Domini.Fitxa;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Set;

/**
 * Adaptador per serialitzar y deserialitzar objectes de tipus Bossa.
 */
public class BossaAdapter implements JsonSerializer<Bossa>, JsonDeserializer<Bossa> {
    
    /**
     * Funció per serialitzar un objecte de tipus Bossa a JSON.
     */
    @Override
    public JsonElement serialize(Bossa src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        
        if (src == null) {
            return result;
        }
        
        // Serializar propiedades básicas
        result.addProperty("nom", src.getNom());
        
        // Serializar la lista de fichas
        JsonArray fitxesArray = new JsonArray();
        for (Fitxa fitxa : src.getFitxes()) {
            fitxesArray.add(context.serialize(fitxa, Fitxa.class));
        }
        result.add("fitxes", fitxesArray);
        
        // Serializar el alfabeto si existe
        Set<String> alfabet = src.getAlfabet();
        if (alfabet != null && !alfabet.isEmpty()) {
            JsonArray alfabetArray = new JsonArray();
            for (String lletra : alfabet) {
                alfabetArray.add(lletra);
            }
            result.add("alfabet", alfabetArray);
        }
        
        return result;
    }
    
    /**
     * Funció per deserialitzar un JSON a un objecte de tipus Bossa.
     */
    @Override
    public Bossa deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
            throws JsonParseException {
        
        if (json == null || !json.isJsonObject()) {
            throw new JsonParseException("JSON inválido para deserializar a Bossa");
        }
        
        JsonObject jsonObject = json.getAsJsonObject();
        
        // Extraer nombre para crear la bossa
        String nom = jsonObject.has("nom") ? jsonObject.get("nom").getAsString() : "default";
        
        // Crear una nueva bossa
        Bossa bossa = new Bossa(nom);
        
        // Deserializar la lista de fichas
        if (jsonObject.has("fitxes")) {
            JsonArray fitxesArray = jsonObject.getAsJsonArray("fitxes");
            LinkedList<Fitxa> fitxes = new LinkedList<>();
            
            for (JsonElement element : fitxesArray) {
                Fitxa fitxa = context.deserialize(element, Fitxa.class);
                fitxes.add(fitxa);
            }
            
            bossa.setFitxes(fitxes);
        }
        
        return bossa;
    }
}