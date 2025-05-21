package Persistencia;

import com.google.gson.*;
import Domini.Taulell;
import Domini.Casella;
import Domini.Fitxa;

import java.lang.reflect.Type;

/**
 * Adaptador per serialitzar y deserialitzar objectes de tipus Taulell.
 */
public class TaulellAdapter implements JsonSerializer<Taulell>, JsonDeserializer<Taulell> {
    
    /**
     * Funció per serialitzar un objecte de tipus Taulell a JSON.
     */
    @Override
    public JsonElement serialize(Taulell src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        
        if (src == null) {
            return result;
        }
        
        result.addProperty("primerMoviment", src.esPrimerMoviment());
        
        Casella[][] caselles = src.getCaselles();
        if (caselles != null) {
            JsonArray casellesArray = new JsonArray();
            
            for (int i = 0; i < caselles.length; i++) {
                JsonArray filaArray = new JsonArray();
                for (int j = 0; j < caselles[i].length; j++) {
                    Casella casella = caselles[i][j];
                    if (casella != null) {
                        JsonObject casellaObj = new JsonObject();
                        
                        casellaObj.addProperty("x", casella.getX());
                        casellaObj.addProperty("y", casella.getY());
                        casellaObj.addProperty("multiplicadorLetra", casella.getMultiplicadorLetra());
                        casellaObj.addProperty("multiplicadorParaula", casella.getMultiplicadorParaula());
                        casellaObj.addProperty("ocupada", casella.isOcupada());
                        casellaObj.addProperty("esCasellaInicial", casella.isEsCasellaInicial());
                        
                        if (casella.isOcupada() && casella.getFitxa() != null) {
                            JsonObject fitxaObj = new JsonObject();
                            Fitxa fitxa = casella.getFitxa();
                            
                            fitxaObj.addProperty("lletra", fitxa.getLletra());
                            fitxaObj.addProperty("valor", fitxa.getValor());
                            
                            casellaObj.add("fitxa", fitxaObj);
                        }
                        
                        filaArray.add(casellaObj);
                    } else {
                        filaArray.add(new JsonObject());
                    }
                }
                casellesArray.add(filaArray);
            }
            
            result.add("caselles", casellesArray);
        }
        
        return result;
    }
    
    /**
     * Funció per deserialitzar un JSON a un objecte de tipus Taulell.
     */
    @Override
    public Taulell deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
            throws JsonParseException {
        
        if (json == null || !json.isJsonObject()) {
            throw new JsonParseException("JSON inválido para deserializar a Taulell");
        }
        
        JsonObject jsonObject = json.getAsJsonObject();
        
        Taulell taulell = new Taulell();
        
        if (jsonObject.has("primerMoviment")) {
            boolean primerMoviment = jsonObject.get("primerMoviment").getAsBoolean();
            taulell.setPrimerMoviment(primerMoviment);
        }
        
        if (jsonObject.has("caselles")) {
            JsonArray casellesArray = jsonObject.getAsJsonArray("caselles");
            int filas = casellesArray.size();
            
            if (filas > 0) {
                JsonArray primeraFila = casellesArray.get(0).getAsJsonArray();
                int columnas = primeraFila.size();
                
                Casella[][] caselles = new Casella[filas][columnas];
                
                for (int i = 0; i < filas; i++) {
                    JsonArray filaArray = casellesArray.get(i).getAsJsonArray();
                    
                    for (int j = 0; j < Math.min(columnas, filaArray.size()); j++) {
                        JsonElement casellaElement = filaArray.get(j);
                        
                        if (casellaElement.isJsonObject()) {
                            JsonObject casellaObj = casellaElement.getAsJsonObject();
                            
                            int x = casellaObj.has("x") ? casellaObj.get("x").getAsInt() : i;
                            int y = casellaObj.has("y") ? casellaObj.get("y").getAsInt() : j;
                            int multiplicadorLetra = casellaObj.has("multiplicadorLetra") ? 
                                casellaObj.get("multiplicadorLetra").getAsInt() : 1;
                            int multiplicadorParaula = casellaObj.has("multiplicadorParaula") ? 
                                casellaObj.get("multiplicadorParaula").getAsInt() : 1;
                            
                            Casella casella = new Casella(x, y, multiplicadorLetra, multiplicadorParaula);
                            
                            if (casellaObj.has("esCasellaInicial")) {
                                boolean esCasellaInicial = casellaObj.get("esCasellaInicial").getAsBoolean();
                                casella.setEsCasellaInicial(esCasellaInicial);
                            }
                            
                            if (casellaObj.has("ocupada") && casellaObj.get("ocupada").getAsBoolean() && 
                                casellaObj.has("fitxa")) {
                                
                                JsonObject fitxaObj = casellaObj.getAsJsonObject("fitxa");
                                String lletra = fitxaObj.get("lletra").getAsString();
                                int valor = fitxaObj.get("valor").getAsInt();
                                
                                Fitxa fitxa = new Fitxa(lletra, valor);
                                casella.colocarFitxa(fitxa);
                            }
                            
                            caselles[i][j] = casella;
                        } else {
                            caselles[i][j] = new Casella(i, j, 1, 1);
                        }
                    }
                }
                
                taulell.setCaselles(caselles);
            }
        }
        
        return taulell;
    }
}