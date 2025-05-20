package Persistencia;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import Domini.Partida;
import Domini.Persona;
import Domini.Usuari;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

/**
 * Adaptador para serializar y deserializar objetos de tipo Usuari.
 * Maneja la serialización/deserialización de las clases concretas (Persona, Bot)
 * que heredan de la clase abstracta Usuari.
 */
public class UsuariAdapter implements JsonSerializer<Usuari>, JsonDeserializer<Usuari> {
    
    /**
     * Serializa un objeto Usuari a formato JSON pasandolo a Persona (solo se almacenan objetos Persona)
     */
    @Override
    public JsonElement serialize(Usuari src, Type typeOfSrc, JsonSerializationContext context) {
        System.out.println("voy a serializar jejejje");
        JsonObject result = new JsonObject();
        
        
        result.addProperty("type", "Persona");
        
        // Serializar todos los campos de Persona
        Persona persona = (Persona) src;
        result.addProperty("nom", persona.getNom());
        result.addProperty("correu", persona.getCorreu());
        result.addProperty("contrasenya", persona.getContrasenya());
        result.addProperty("edat", persona.getEdat());
        result.addProperty("pais", persona.getPais());
        
        // Si Persona tiene un objeto Estadistiques, serializarlo también
        if (persona.getEstadistiques() != null) {
            JsonElement estadistiques = context.serialize(persona.getEstadistiques());
            result.add("estadistiques", estadistiques);
        }
        
        ArrayList<Partida> partidesEnCurs = persona.getPartidesEnCurs();
        JsonArray partidesEnCursArray = new JsonArray();
        if (!partidesEnCurs.isEmpty()) {
            
            for (Partida partida : partidesEnCurs)
                partidesEnCursArray.add(partida.getNom());
            
        }
    
        result.add("partidesEnCurs", partidesEnCursArray);
            
        return result;
    }
    
    /**
     * Deserializa un objeto JSON a un objeto Usuari.
     * Utiliza el campo "type" para determinar la clase concreta que debe instanciar.
     */
    @Override
    public Usuari deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
    {
        
        JsonObject jsonObject = json.getAsJsonObject();
        
    
        // Crear y configurar un objeto Persona
        Persona persona = new Persona();
        
        // Configurar campos básicos
        if (jsonObject.has("nom")) {
            persona.setNom(jsonObject.get("nom").getAsString());
        }
        
        if (jsonObject.has("correu")) {
            persona.setCorreu(jsonObject.get("correu").getAsString());
        }
        
        if (jsonObject.has("contrasenya")) {
            persona.setContrasenya(jsonObject.get("contrasenya").getAsString());
        }
        
        if (jsonObject.has("edat")) {
            persona.setEdat(jsonObject.get("edat").getAsString());
        }
        
        if (jsonObject.has("pais")) {
            persona.setPais(jsonObject.get("pais").getAsString());
        }
        
        // Si hay estadísticas, deserializarlas también
        if (jsonObject.has("estadistiques")) {
            persona.setEstadistiques(context.deserialize(
                jsonObject.get("estadistiques"), 
                Domini.Estadistiques.class));
        }

        if (jsonObject.has("partidesEnCurs")) 
        {
            JsonArray partidesEnCursArray = jsonObject.getAsJsonArray("partidesEnCurs");
            ArrayList<Partida> partidesEnCurs = new ArrayList<>();
            
            for (JsonElement partidaId : partidesEnCursArray) 
            {
                String id = partidaId.getAsString();
                Partida partida = new Partida();
                partida.setNom(id);
                partidesEnCurs.add(partida);
            }
            
            persona.setPartidesEnCurs(partidesEnCurs);
        }

        
        
        return persona;
                
        
    }
}