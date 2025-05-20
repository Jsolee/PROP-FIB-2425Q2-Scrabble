package Persistencia;

import com.google.gson.*;
import Domini.Taulell;
import Domini.Casella;
import Domini.Fitxa;

import java.lang.reflect.Type;

/**
 * Adaptador para serializar y deserializar objetos de tipo Taulell.
 * Se encarga de convertir la estructura del tablero a JSON y viceversa,
 * preservando todas las propiedades y relaciones de casillas.
 */
public class TaulellAdapter implements JsonSerializer<Taulell>, JsonDeserializer<Taulell> {
    
    @Override
    public JsonElement serialize(Taulell src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        
        if (src == null) {
            return result;
        }
        
        // Serializar propiedades básicas del tablero
        result.addProperty("primerMoviment", src.esPrimerMoviment());
        
        // Serializar la matriz de casillas
        Casella[][] caselles = src.getCaselles();
        if (caselles != null) {
            JsonArray casellesArray = new JsonArray();
            
            for (int i = 0; i < caselles.length; i++) {
                JsonArray filaArray = new JsonArray();
                for (int j = 0; j < caselles[i].length; j++) {
                    Casella casella = caselles[i][j];
                    if (casella != null) {
                        // Serializar cada casilla individualmente
                        JsonObject casellaObj = new JsonObject();
                        
                        // Propiedades básicas de la casilla
                        casellaObj.addProperty("x", casella.getX());
                        casellaObj.addProperty("y", casella.getY());
                        casellaObj.addProperty("multiplicadorLetra", casella.getMultiplicadorLetra());
                        casellaObj.addProperty("multiplicadorParaula", casella.getMultiplicadorParaula());
                        casellaObj.addProperty("ocupada", casella.isOcupada());
                        casellaObj.addProperty("esCasellaInicial", casella.isEsCasellaInicial());
                        
                        // Si la casilla tiene una ficha, serializar también la ficha
                        if (casella.isOcupada() && casella.getFitxa() != null) {
                            JsonObject fitxaObj = new JsonObject();
                            Fitxa fitxa = casella.getFitxa();
                            
                            fitxaObj.addProperty("lletra", fitxa.getLletra());
                            fitxaObj.addProperty("valor", fitxa.getValor());
                            
                            casellaObj.add("fitxa", fitxaObj);
                        }
                        
                        filaArray.add(casellaObj);
                    } else {
                        // Si la casilla es null, añadir un objeto vacío
                        filaArray.add(new JsonObject());
                    }
                }
                casellesArray.add(filaArray);
            }
            
            result.add("caselles", casellesArray);
        }
        
        return result;
    }
    
    @Override
    public Taulell deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
            throws JsonParseException {
        
        if (json == null || !json.isJsonObject()) {
            throw new JsonParseException("JSON inválido para deserializar a Taulell");
        }
        
        JsonObject jsonObject = json.getAsJsonObject();
        
        // Crear un nuevo tablero
        Taulell taulell = new Taulell();
        
        // Establecer el estado de primer movimiento
        if (jsonObject.has("primerMoviment")) {
            boolean primerMoviment = jsonObject.get("primerMoviment").getAsBoolean();
            taulell.setPrimerMoviment(primerMoviment);
        }
        
        // Deserializar la matriz de casillas
        if (jsonObject.has("caselles")) {
            JsonArray casellesArray = jsonObject.getAsJsonArray("caselles");
            int filas = casellesArray.size();
            
            if (filas > 0) {
                JsonArray primeraFila = casellesArray.get(0).getAsJsonArray();
                int columnas = primeraFila.size();
                
                // Crear una nueva matriz de casillas
                Casella[][] caselles = new Casella[filas][columnas];
                
                // Llenar la matriz con las casillas deserializadas
                for (int i = 0; i < filas; i++) {
                    JsonArray filaArray = casellesArray.get(i).getAsJsonArray();
                    
                    for (int j = 0; j < Math.min(columnas, filaArray.size()); j++) {
                        JsonElement casellaElement = filaArray.get(j);
                        
                        if (casellaElement.isJsonObject()) {
                            JsonObject casellaObj = casellaElement.getAsJsonObject();
                            
                            // Extraer propiedades de la casilla
                            int x = casellaObj.has("x") ? casellaObj.get("x").getAsInt() : i;
                            int y = casellaObj.has("y") ? casellaObj.get("y").getAsInt() : j;
                            int multiplicadorLetra = casellaObj.has("multiplicadorLetra") ? 
                                casellaObj.get("multiplicadorLetra").getAsInt() : 1;
                            int multiplicadorParaula = casellaObj.has("multiplicadorParaula") ? 
                                casellaObj.get("multiplicadorParaula").getAsInt() : 1;
                            
                            // Crear la casilla
                            Casella casella = new Casella(x, y, multiplicadorLetra, multiplicadorParaula);
                            
                            // Establecer si es casilla inicial
                            if (casellaObj.has("esCasellaInicial")) {
                                boolean esCasellaInicial = casellaObj.get("esCasellaInicial").getAsBoolean();
                                casella.setEsCasellaInicial(esCasellaInicial);
                            }
                            
                            // Si la casilla tiene una ficha, crear y colocar la ficha
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
                            // Si no es un objeto JSON válido, crear una casilla por defecto
                            caselles[i][j] = new Casella(i, j, 1, 1);
                        }
                    }
                }
                
                // Establecer la matriz de casillas en el tablero
                taulell.setCaselles(caselles);
            }
        }
        
        return taulell;
    }
}