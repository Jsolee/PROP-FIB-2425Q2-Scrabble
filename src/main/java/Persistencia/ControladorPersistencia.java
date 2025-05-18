package Persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import Domini.Partida;
import Domini.Ranking;
import Domini.Usuari;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;

public class ControladorPersistencia {
    private static final String USUARI_JSON = "src/main/java/Persistencia/Usuaris.json";
    private static final String PARTIDES_JSON = "src/main/java/Persistencia/Partides.json";
    private static final String RANKING_JSON = "src/main/java/Persistencia/Ranking.json";

    private final Gson gson;

    public ControladorPersistencia() {
        // Crear GsonBuilder y registrar adaptadores
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        
        // Registrar el adaptador para la clase Usuari
        gsonBuilder.registerTypeAdapter(Usuari.class, new UsuariAdapter());
        gsonBuilder.registerTypeAdapter(Partida.class, new PartidaAdapter());
        
        // Registrar adaptador para el mapa de usuarios
        Type usuariMapType = new TypeToken<HashMap<String, Usuari>>() {}.getType();
        gsonBuilder.registerTypeAdapter(usuariMapType, new MapUsuariAdapter());
        
        // Crear el objeto Gson con los adaptadores configurados
        this.gson = gsonBuilder.create();
    }


        /**
     * Guarda los usuarios en un archivo JSON.
     * 
     * @param usuarios Mapa de usuarios a guardar
     * @throws IOException Si ocurre un error de E/S
     */
    public void guardarUsuaris(HashMap<String, Usuari> usuarios) throws IOException {
        System.out.println("Guardando " + usuarios.size() + " usuarios en: " + USUARI_JSON);
        
        try (Writer writer = new FileWriter(USUARI_JSON)) {
            gson.toJson(usuarios, writer);
            System.out.println("Usuarios guardados correctamente.");
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
            throw e;
        }
    }
    

    // Cargar Usuaris desde un fichero JSON
    public HashMap<String, Usuari> cargarUsuaris() throws IOException {
        try (Reader reader = new FileReader(USUARI_JSON)) {
            Type type = new TypeToken<HashMap<String, Usuari>>() {}.getType();
            return gson.fromJson(reader, type);
        } catch (FileNotFoundException e) {
            return new HashMap<>(); // Si el archivo no existe, devolvemos un HashMap vacío
        }
    }

    // Guardar partidas en un fichero JSON
    public void guardarPartides(HashMap<String, Partida> partidas) throws IOException {
        try (Writer writer = new FileWriter(PARTIDES_JSON )) {
            gson.toJson(partidas, writer);
        }
    }

    // Cargar partidas desde un fichero JSON
    public HashMap<String, Partida> cargarPartides() throws IOException {
        try (Reader reader = new FileReader(PARTIDES_JSON)) {
            Type type = new TypeToken<HashMap<String, Partida>>() {}.getType();
            return gson.fromJson(reader, type);
        } catch (FileNotFoundException e) {
            return new HashMap<>(); // Si el archivo no existe, devolvemos un HashMap vacío
        }
    }

    // Guardar rankings en un fichero JSON
    public void guardarRanking(Ranking ranking) throws IOException {
        try (Writer writer = new FileWriter(RANKING_JSON)) {
            gson.toJson(ranking, writer);
        }
    }

    // Cargar rankings desde un fichero JSON
    public Ranking cargarRanking() throws IOException {
        try (Reader reader = new FileReader(RANKING_JSON)) {
            return gson.fromJson(reader, Ranking.class);
        } catch (FileNotFoundException e) {
            return new Ranking(); // Si el archivo no existe, devolvemos un Ranking vacío
        }
    }
}