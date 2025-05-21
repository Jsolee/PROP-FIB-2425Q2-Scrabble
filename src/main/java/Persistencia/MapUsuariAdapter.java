package Persistencia;

import com.google.gson.*;
import Domini.Persona;
import Domini.Usuari;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Adaptador per serialitzar y deserialitzar objectes de tipus HashMap<String, Usuari>.
 */
public class MapUsuariAdapter implements JsonSerializer<HashMap<String, Usuari>>, JsonDeserializer<HashMap<String, Usuari>> {
    
    /**
     * Serialitza un mapa d'usuaris a un objecte JSON.
     */
    @Override
    public JsonElement serialize(HashMap<String, Usuari> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        
        // Serializar cada entrada del mapa
        for (Map.Entry<String, Usuari> entry : src.entrySet()) {
            String key = entry.getKey();
            Usuari usuari = entry.getValue();
            
            if (usuari != null) {
                // Usar el contexto para serializar cada Usuari (esto usará UsuariAdapter)
                JsonElement usuariJson = context.serialize(usuari, Usuari.class);
                result.add(key, usuariJson);
            }
        }
        
        return result;
    }
    
    /**
     * Deserialitza un objeto JSON a un mapa d'usuaris.
     */
    @Override
    public HashMap<String, Usuari> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
            throws JsonParseException {
        
        HashMap<String, Usuari> result = new HashMap<>();
        
        // Verificar que el JSON es válido
        if (json == null || !json.isJsonObject()) {
            System.out.println("Error: JSON inválido para mapa de usuarios");
            return result;
        }
        
        JsonObject jsonObject = json.getAsJsonObject();
        
        // Procesar cada propiedad del objeto JSON como una entrada del mapa
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            
            if (value != null && value.isJsonObject()) {
                try {
                    // Deserializar el valor como un objeto Usuari (usando UsuariAdapter)
                    Usuari usuari = context.deserialize(value, Usuari.class);
                    
                    if (usuari != null) {
                        // Verificar y corregir el nombre del usuario si es necesario
                        if (usuari.getNom() == null || usuari.getNom().isEmpty()) {
                            usuari.setNom(key);
                        }
                        
                        // Añadir la entrada al mapa resultado
                        result.put(key, usuari);
                    }
                } catch (Exception e) {
                    // Continuar con la siguiente entrada
                }
            }
        }
        
        return result;
    }
}