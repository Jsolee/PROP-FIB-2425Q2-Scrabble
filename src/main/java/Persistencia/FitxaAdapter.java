package Persistencia;

import com.google.gson.*;
import Domini.Fitxa;

import java.lang.reflect.Type;

/**
 * Adaptador per serialitzar y deserialitzar objectes de tipus Fitxa.
 */
public class FitxaAdapter implements JsonSerializer<Fitxa>, JsonDeserializer<Fitxa> {
    
    /**
     * Funció per serialitzar un objecte de tipus Fitxa a JSON.
     */
    @Override
    public JsonElement serialize(Fitxa src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        
        if (src == null) {
            return result;
        }
        
        // Serializar propiedades básicas
        result.addProperty("lletra", src.getLletra());
        result.addProperty("valor", src.getValor());
        
        // Si Fitxa tiene la propiedad "blank", serializarla también
        // Esta propiedad puede no existir en tu implementación
        if (src.getClass().getDeclaredFields().length > 2) {
            try {
                boolean isBlank = (boolean) src.getClass().getMethod("isBlank").invoke(src);
                result.addProperty("blank", isBlank);
            } catch (Exception e) {
                // La propiedad o método no existe, simplemente no la serializamos
            }
        }
        
        return result;
    }
    
    /**
     * Funció per deserialitzar un JSON a un objecte de tipus Fitxa.
     */
    @Override
    public Fitxa deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
            throws JsonParseException {
        
        if (json == null || !json.isJsonObject()) {
            throw new JsonParseException("JSON inválido para deserializar a Fitxa");
        }
        
        JsonObject jsonObject = json.getAsJsonObject();
        
        // Extraer propiedades básicas
        String lletra = jsonObject.get("lletra").getAsString();
        int valor = jsonObject.get("valor").getAsInt();
        
        // Crear y devolver la ficha
        return new Fitxa(lletra, valor);
    }
}