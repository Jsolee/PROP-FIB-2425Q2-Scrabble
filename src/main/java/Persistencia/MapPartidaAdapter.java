package Persistencia;

import com.google.gson.*;
import Domini.Partida;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Adaptador per serialitzar y deserialitzar objectes de tipus HashMap<String, Partida>.
 */
public class MapPartidaAdapter implements JsonSerializer<HashMap<String, Partida>>, JsonDeserializer<HashMap<String, Partida>> {
    
    /**
     * Funció per serialitzar un objecte de tipus HashMap<String, Partida> a JSON.
     */
    @Override
    public JsonElement serialize(HashMap<String, Partida> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        
        for (Map.Entry<String, Partida> entry : src.entrySet()) {
            String key = entry.getKey();
            Partida partida = entry.getValue();
            
            if (partida != null) {
                JsonElement partidaJson = context.serialize(partida, Partida.class);
                result.add(key, partidaJson);
            }
        }
        
        return result;
    }

    /**
     * Funció per deserialitzar un JSON a un objecte de tipus HashMap<String, Partida>.
     */
    @Override
    public HashMap<String, Partida> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
            throws JsonParseException {
        
        HashMap<String, Partida> result = new HashMap<>();
        
        if (json == null || !json.isJsonObject()) {
            return result;
        }
        
        JsonObject jsonObject = json.getAsJsonObject();
        
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            
            try {
                Partida partida = context.deserialize(value, Partida.class);
                
                if (partida != null) {
                    // Si el nombre de la partida no está definido, usar la clave como nombre
                    if (partida.getNom() == null || partida.getNom().isEmpty()) {
                        partida.setNom(key);
                    }
                    
                    result.put(key, partida);
                }
            } catch (Exception e) {
                System.err.println("Error al deserializar partida con clave " + key + ": " + e.getMessage());
            }
        }
        
        return result;
    }
}