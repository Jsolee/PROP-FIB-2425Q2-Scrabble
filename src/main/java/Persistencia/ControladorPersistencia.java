package Persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import Domini.Partida;
import Domini.Persona;
import Domini.Ranking;
import Domini.Usuari;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Controlador de persistencia que gestiona la lectura y escriptura de les dades en arxius JSON.
 * Utiliza Gson para serializar y deserializar objetos mitjançant els adaptadors corresponents.
 */
public class ControladorPersistencia {
    private static final String USUARI_JSON = "src/main/java/Persistencia/Usuaris.json";
    private static final String PARTIDES_JSON = "src/main/java/Persistencia/Partides.json";
    private static final String RANKING_JSON = "src/main/java/Persistencia/Ranking.json";

    private final Gson gson;

    public ControladorPersistencia() {
        // Crear GsonBuilder y registrar adaptadores
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        
        // Registrar el adaptador per la classe Usuari
        gsonBuilder.registerTypeAdapter(Usuari.class, new UsuariAdapter());
        gsonBuilder.registerTypeAdapter(Partida.class, new PartidaAdapter());
        gsonBuilder.registerTypeAdapter(Persona.class, new UsuariAdapter());

        // Registrar adaptador per la classe Bossa
        gsonBuilder.registerTypeAdapter(Ranking.class, new RankingAdapter());
        
        // Registrar adaptador pel mapa d'usuaris
        Type usuariMapType = new TypeToken<HashMap<String, Usuari>>() {}.getType();
        gsonBuilder.registerTypeAdapter(usuariMapType, new MapUsuariAdapter());
        
        // Registrar adaptador pel mapa de partides
        Type partidaMapType = new TypeToken<HashMap<String, Partida>>() {}.getType();
        gsonBuilder.registerTypeAdapter(partidaMapType, new MapPartidaAdapter());

        this.gson = gsonBuilder.create();
    }


    /**
     * Guarda els usuaris en un fitxer JSON.
     * @param usuarios Mapa d'usuaris a guardar, on la clau és el nom d'usuari i el valor és l'objecte Usuari.
     * @throws IOException
     */
    public void guardarUsuaris(HashMap<String, Usuari> usuarios) throws IOException {        
        try (Writer writer = new FileWriter(USUARI_JSON)) {
            gson.toJson(usuarios, writer);
        } catch (IOException e) {
            throw e;
        }
    }
    

    /**
     * Carrega els usuaris des d'un fitxer JSON.
     * @return Mapa d'usuaris carregats, on la clau és el nom d'usuari i el valor és l'objecte Usuari.
     * @throws IOException
     */
    public HashMap<String, Usuari> cargarUsuaris() throws IOException {
        try (Reader reader = new FileReader(USUARI_JSON)) {
            Type type = new TypeToken<HashMap<String, Usuari>>() {}.getType();
            return gson.fromJson(reader, type);
        } catch (FileNotFoundException e) {
            return new HashMap<>(); // Si el archivo no existe, devolvemos un HashMap vacío
        }
    }

    /**
     * Guarda les partides en un fitxer JSON.
     * @param partidas Mapa de partides a guardar, on la clau és l'identificador de la partida i el valor és l'objecte Partida.
     * @throws IOException
     */
    public void guardarPartides(HashMap<String, Partida> partidas) throws IOException {
        try (Writer writer = new FileWriter(PARTIDES_JSON )) {
            gson.toJson(partidas, writer);
        }
    }

    /**
     * Carrega les partides des d'un fitxer JSON.
     * @return Mapa de partides carregades, on la clau és l'identificador de la partida i el valor és l'objecte Partida.
     * @throws IOException
    */    
    public HashMap<String, Partida> cargarPartides() throws IOException {
        try (Reader reader = new FileReader(PARTIDES_JSON)) {
            Type type = new TypeToken<HashMap<String, Partida>>() {}.getType();
            return gson.fromJson(reader, type);
        } catch (FileNotFoundException e) {
            return new HashMap<>(); // Si el archivo no existe, devolvemos un HashMap vacío
        }
    }

    /**
     * Guarda el ranking en un fitxer JSON.
     * @param ranking Objecte Ranking a guardar.
     * @throws IOException
     */
    public void guardarRanking(Ranking ranking) throws IOException {
        try (Writer writer = new FileWriter(RANKING_JSON)) {
            gson.toJson(ranking, writer);
        }
    }

    /**
     * Carrega el ranking des d'un fitxer JSON.
     * @return Objecte Ranking carregat.
     * @throws IOException
     */
    public Ranking cargarRanking() throws IOException {
        try (Reader reader = new FileReader(RANKING_JSON)) {
            return gson.fromJson(reader, Ranking.class);
        } catch (FileNotFoundException e) {
            return new Ranking(); // Si el archivo no existe, devolvemos un Ranking vacío
        }
    }
}