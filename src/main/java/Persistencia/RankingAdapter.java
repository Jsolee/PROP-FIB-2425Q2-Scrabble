package Persistencia;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import Domini.Ranking;
import Domini.Persona;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RankingAdapter implements JsonSerializer<Ranking>, JsonDeserializer<Ranking> {
    
    @Override
    public JsonElement serialize(Ranking src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        
        if (src == null) return result;
        
        // Serializar puntsTotals (solo IDs)
        JsonArray puntsTotalsArray = new JsonArray();
        for (Persona persona : src.getRankingPuntsTotals()) {
            if (persona != null) {
                puntsTotalsArray.add(persona.getNom());
            }
        }
        result.add("rankingPuntsTotalsIds", puntsTotalsArray);
        
        // Serializar partidesJugades (solo IDs)
        JsonArray partidesJugadesArray = new JsonArray();
        for (Persona persona : src.getRankingPartidesJugades()) {
            if (persona != null) {
                partidesJugadesArray.add(persona.getNom());
            }
        }
        result.add("rankingPartidesJugadesIds", partidesJugadesArray);
        
        // Serializar partidesGuanyades (solo IDs)
        JsonArray partidesGuanyadesArray = new JsonArray();
        for (Persona persona : src.getRankingPartidesGuanyades()) {
            if (persona != null) {
                partidesGuanyadesArray.add(persona.getNom());
            }
        }
        result.add("rankingPartidesGuanyadesIds", partidesGuanyadesArray);
        
        // Serializar recordPersonal (solo IDs)
        JsonArray recordPersonalArray = new JsonArray();
        for (Persona persona : src.getRankingRecordPersonal()) {
            if (persona != null) {
                recordPersonalArray.add(persona.getNom());
            }
        }
        result.add("rankingRecordPersonalIds", recordPersonalArray);
        
        return result;
    }
    
    @Override
    public Ranking deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
            throws JsonParseException {
        
        if (json == null || !json.isJsonObject()) {
            throw new JsonParseException("JSON inválido para deserializar a Ranking");
        }
        
        JsonObject jsonObject = json.getAsJsonObject();
        Ranking ranking = new Ranking();

        List<String> puntsTotalsIds = new ArrayList<>();
        List<String> partidesJugadesIds = new ArrayList<>();
        List<String> partidesGuanyadesIds = new ArrayList<>();
        List<String> recordPersonalIds = new ArrayList<>();

        jsonObject.get("rankingPuntsTotalsIds").getAsJsonArray().forEach(element -> {
            puntsTotalsIds.add(element.getAsString());
        });

        jsonObject.get("rankingPartidesJugadesIds").getAsJsonArray().forEach(element -> {
            partidesJugadesIds.add(element.getAsString());
        });

        jsonObject.get("rankingPartidesGuanyadesIds").getAsJsonArray().forEach(element -> {
            partidesGuanyadesIds.add(element.getAsString());
        });

        jsonObject.get("rankingRecordPersonalIds").getAsJsonArray().forEach(element -> {
            recordPersonalIds.add(element.getAsString());
        });

        List<Persona> puntsTotals = new ArrayList<>();
        List<Persona> partidesJugades = new ArrayList<>();
        List<Persona> partidesGuanyades = new ArrayList<>();
        List<Persona> recordPersonal = new ArrayList<>();

        for (String id : puntsTotalsIds) {
            Persona persona = new Persona();
            persona.setNom(id);
            puntsTotals.add(persona);
        }

        ranking.setRankingPuntsTotals(puntsTotals);

        for (String id : partidesJugadesIds) {
            Persona persona = new Persona();
            persona.setNom(id);
            partidesJugades.add(persona);
        }
        ranking.setRankingPartidesJugades(partidesJugades);

        for (String id : partidesGuanyadesIds) {
            Persona persona = new Persona();
            persona.setNom(id);
            partidesGuanyades.add(persona);
        }
        ranking.setRankingPartidesGuanyades(partidesGuanyades);

        for (String id : recordPersonalIds) {
            Persona persona = new Persona();
            persona.setNom(id);
            recordPersonal.add(persona);
        }
        ranking.setRankingRecordPersonal(recordPersonal);        

        return ranking;
    }
}