package Persistencia;

import com.google.gson.*;
import Domini.Partida;
import Domini.Persona;
import Domini.Usuari;
import Domini.Taulell;
import Domini.Fitxa;
import Domini.Bossa;
import Domini.Bot;

import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;

/**
 * Adaptador per serialitzar y deserialitzar objectes de tipus Partida. Per a serialitzar guarda els noms dels
 * jugadors (i no l'objecte Usuari La resta d'atributs es guarden de manera estandard, utilitzant els adaptadors 
 * necessaris per atributs que representen objectes.
 * Per deserialitzar, es reconstrueixen els objectes de tipus Usuari a partir dels noms guardats en el JSON i afegeix 
 * uns Usuaris dummy inexistents, que desprès son reconstruits al ControladorDomini.
 */
public class PartidaAdapter implements JsonSerializer<Partida>, JsonDeserializer<Partida> {
    
    /**
     * Funció per serialitzar un objecte de tipus Partida a JSON.
     */
    @Override
    public JsonElement serialize(Partida src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        
        if (src == null) {
            return result;
        }
        
        // Serializar datos básicos de la partida
        result.addProperty("nom", src.getNom());
        result.addProperty("idioma", src.getIdioma());
        result.addProperty("partidaAcabada", src.getPartidaAcabada());
        result.addProperty("partidaPausada", src.getPartidaPausada());
        result.addProperty("tornActual", src.getTornActualIndex());
            
        
        // Serializar puntuaciones
        JsonArray puntuacionsArray = new JsonArray();
        List<Integer> puntuacions = src.getPuntuacions();
        for (Integer puntuacio : puntuacions) {
            puntuacionsArray.add(puntuacio);
        }
        result.add("puntuacions", puntuacionsArray);
        
        // Serializar jugadores (sólo IDs/nombres para evitar recursión)
        JsonArray jugadorsArray = new JsonArray();
        List<Usuari> jugadors = src.getJugadors();
        for (Usuari jugador : jugadors) {
            if (jugador != null) {
                jugadorsArray.add(jugador.getNom());
            }
        }
        result.add("jugadorsIds", jugadorsArray);
        
        // Serializar el taulell
        Taulell taulell = src.getTaulell();
        if (taulell != null) {
            JsonElement taulellJson = context.serialize(taulell);
            result.add("taulell", taulellJson);
        }
        
        // Serializar los atriles (fitxes de cada jugador)
        JsonArray atrilsArray = new JsonArray();
        List<List<Fitxa>> atrils = src.getAtrils();
        for (List<Fitxa> atril : atrils) {
            JsonArray atrilArray = new JsonArray();
            for (Fitxa fitxa : atril) {
                JsonElement fitxaJson = context.serialize(fitxa);
                atrilArray.add(fitxaJson);
            }
            atrilsArray.add(atrilArray);
        }
        result.add("atrils", atrilsArray);
        
        // Serializar la bossa de fitxes
        Bossa bossa = src.getBossa();
        if (bossa != null) {
            JsonElement bossaJson = context.serialize(bossa);
            result.add("bossa", bossaJson);
        }
        
        return result;
    }
    
    /**
     * Funció per deserialitzar un JSON a un objecte de tipus Partida.
     */
    @Override
    public Partida deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
            throws JsonParseException {
        
        if (json == null || !json.isJsonObject()) {
            throw new JsonParseException("JSON inválido para deserializar a Partida");
        }
        
        JsonObject jsonObject = json.getAsJsonObject();
        
        // Extraer datos básicos
        String nom = jsonObject.has("nom") ? jsonObject.get("nom").getAsString() : "";
        String idioma = jsonObject.has("idioma") ? jsonObject.get("idioma").getAsString() : "catalan";
        
        // Crear la partida con nombre e idioma
        Partida partida = new Partida(nom, idioma);
        
        // Configurar estado de la partida
        if (jsonObject.has("partidaAcabada")) {
            boolean acabada = jsonObject.get("partidaAcabada").getAsBoolean();
            if (acabada) partida.acabarPartida();
        }
        
        if (jsonObject.has("partidaPausada")) {
            boolean pausada = jsonObject.get("partidaPausada").getAsBoolean();
            if (pausada) partida.guardarPartida();
        }
        
        // Establecer turno actual
        if (jsonObject.has("tornActual")) {
            int tornActual = jsonObject.get("tornActual").getAsInt();
            partida.setTornActual(tornActual);
        }
        
        // Cargar puntuaciones
        if (jsonObject.has("puntuacions")) {
            JsonArray puntuacionsArray = jsonObject.getAsJsonArray("puntuacions");
            List<Integer> puntuacions = new ArrayList<>();
            for (JsonElement element : puntuacionsArray) {
                puntuacions.add(element.getAsInt());
            }
            partida.setPuntuacions(puntuacions);
        }
        
        // Los jugadorsIds se procesarán después en el método de reconexión
        
        // Cargar el taulell
        if (jsonObject.has("taulell")) {
            Taulell taulell = context.deserialize(
                jsonObject.get("taulell"), 
                Taulell.class);
            partida.setTaulell(taulell);
        }
        
        // Cargar los atriles
        if (jsonObject.has("atrils")) {
            JsonArray atrilsArray = jsonObject.getAsJsonArray("atrils");
            List<List<Fitxa>> atrils = new ArrayList<>();
            
            for (JsonElement atrilElement : atrilsArray) {
                JsonArray atrilArray = atrilElement.getAsJsonArray();
                List<Fitxa> atril = new ArrayList<>();
                
                for (JsonElement fitxaElement : atrilArray) {
                    Fitxa fitxa = context.deserialize(fitxaElement, Fitxa.class);
                    atril.add(fitxa);
                }
                
                atrils.add(atril);
            }
            
            partida.setAtrils(atrils);
        }
        
        // Cargar la bossa
        if (jsonObject.has("bossa")) {
            Bossa bossa = context.deserialize(
                jsonObject.get("bossa"), 
                Bossa.class);
            partida.setBossa(bossa);
        }
        
        // Guardar IDs de jugadores para reconectar después
        if (jsonObject.has("jugadorsIds")) {
            JsonArray jugadorsArray = jsonObject.getAsJsonArray("jugadorsIds");
            List<String> jugadorsIds = new ArrayList<>();
            
            for (JsonElement element : jugadorsArray) {
                String name = element.getAsString();
                Usuari jugador;
                if (name.equals("bot"))
                    jugador = Bot.getInstance(); 
                else jugador = new Persona(name, "", "", "", "");
                partida.addJugadorPersistencia(jugador);
            }
            
        }        
        return partida;
    }
}