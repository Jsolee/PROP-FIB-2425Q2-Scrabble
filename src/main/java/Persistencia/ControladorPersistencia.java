package Persistencia;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Domini.Partida;
import Domini.Ranking;
import Domini.Usuari;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;

public class ControladorPersistencia {
    private static final String USUARI_JSON = "usuaris.json";
    private static final String PARTIDES_JSON = "partidas.json";
    private static final String RANKING_JSON = "rankings.json";

    private final Gson gson;

    public ControladorPersistencia() {
        this.gson = new Gson();
    }

    // Guardar Usuaris en un fichero JSON
    public void guardarUsuaris(HashMap<String, Usuari> Usuaris) throws IOException {
        try (Writer writer = new FileWriter(USUARI_JSON)) {
            gson.toJson(Usuaris, writer);
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
        try (Writer writer = new FileWriter(PARTIDES_JSON)) {
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