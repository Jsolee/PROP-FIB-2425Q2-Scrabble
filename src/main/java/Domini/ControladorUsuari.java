package Domini;

import java.util.HashMap;
import java.util.List;

/**
 * Controlador que gestiona els usuaris del joc.
 * S'encarrega del registre d'usuaris, inici i tancament de sessions, gestió de contrasenyes,
 * i visualització de perfils i partides.
 */
public class ControladorUsuari {
    /** Mapa que emmagatzema els usuaris registrats amb el seu nom d'usuari com a clau */
    private final HashMap<String, Usuari> usuaris;

    /**
     * Constructor per defecte.
     * Inicialitza un nou mapa d'usuaris buit.
     */
    public ControladorUsuari() {
        this.usuaris = new HashMap<>();
    }

    /**
     * Obté el mapa d'usuaris registrats.
     * 
     * @return mapa on la clau és el nom d'usuari i el valor és l'objecte Usuari
     */
    public HashMap<String, Usuari> getUsuaris() {
        return usuaris;
    }

    /**
     * Obté un usuari pel seu nom d'usuari.
     * 
     * @param username nom de l'usuari a buscar
     * @return objecte Usuari corresponent al nom d'usuari proporcionat
     * @throws IllegalArgumentException si no existeix cap usuari amb el nom especificat
     */
    public Usuari getUsuari(String username) {
        if (!usuaris.containsKey(username))
            throw new IllegalArgumentException("No existeix l'usuari amb el nom: " + username);

        return usuaris.get(username);
    }

    /**
     * Verifica si existeix un usuari amb el nom d'usuari especificat.
     * 
     * @param username nom de l'usuari a verificar
     * @return objecte Usuari si existeix
     * @throws IllegalArgumentException si no existeix cap usuari amb el nom especificat
     */
    public Usuari existeixUsuari(String username)
    {
        if (!usuaris.containsKey(username))
            throw new IllegalArgumentException("No existeix l'usuari amb el nom: " + username);

        return usuaris.get(username);

    }

    /**
     * Registra un nou usuari de tipus Persona al sistema.
     * 
     * @param username nom d'usuari
     * @param correu correu electrònic de l'usuari
     * @param password contrasenya de l'usuari
     * @param edat edat de l'usuari
     * @param pais país de residència de l'usuari
     * @return objecte Persona creat i registrat
     * @throws IllegalArgumentException si ja existeix un usuari amb el mateix nom
     */
    public Persona registrarPersona(String username, String correu, String password, String edat, String pais)
    {
        if (usuaris.containsKey(username))
            throw new IllegalArgumentException("Ja existeix un usuari amb el nom: " + username);

        Persona persona = new Persona(username, correu, password, edat, pais);
        usuaris.put(username, persona);
        return persona;
    }

    /**
     * Verifica les credencials d'un usuari i inicia la seva sessió.
     * 
     * @param username nom d'usuari
     * @param password contrasenya de l'usuari
     * @return true si les credencials són correctes i s'ha iniciat sessió, false en cas contrari
     * @throws IllegalArgumentException si la contrasenya és incorrecta
     */
    public boolean iniciarSessio(String username, String password)
    {
        Usuari usuari = existeixUsuari(username);

        if (usuari instanceof Persona) {
            Persona persona = (Persona) usuari; //cast a Persona (hereda de Usuari)
            if (persona.getContrasenya().equals(password))
            {
                persona.iniciarSessio();
                return true;
            }
            else throw new IllegalArgumentException("Contrasenya incorrecta");
        }
        return false;
    }

    /**
     * Elimina el compte d'un usuari del sistema.
     * 
     * @param username nom de l'usuari a eliminar
     * @return false sempre (comportament a revisar)
     * @throws IllegalArgumentException si no existeix cap usuari amb el nom especificat
     */
    public boolean eliminarCompte(String username)
    {

        Usuari usuari = existeixUsuari(username);

        usuaris.remove(username);
        return false;
    }

    /**
     * Tanca la sessió d'un usuari.
     * 
     * @param username nom de l'usuari
     * @return false sempre (comportament a revisar)
     * @throws IllegalArgumentException si no existeix cap usuari amb el nom especificat
     */
    public boolean tancarSessio(String username)
    {
        Usuari usuari = existeixUsuari(username);
        if (usuari instanceof Persona)
        {
            Persona persona = (Persona) usuari;
            persona.tancarSessio();
        }
        return false;
    }

    /**
     * Canvia la contrasenya d'un usuari.
     * 
     * @param username nom de l'usuari
     * @param password contrasenya actual
     * @param password_nova nova contrasenya
     * @return true si la contrasenya s'ha canviat correctament, false en cas contrari
     * @throws IllegalArgumentException si la contrasenya actual és incorrecta o si la nova contrasenya és igual a l'anterior
     */
    public boolean restablirContrasenya(String username, String password, String password_nova)
    {
        Usuari usuari = existeixUsuari(username);

        if (usuari instanceof Persona) {
            Persona persona = (Persona) usuari; //cast a Persona (hereda de Usuari)
            if (!persona.getContrasenya().equals(password))
                throw new IllegalArgumentException("Contrasenya incorrecta");

            if (password.equals(password_nova))
                throw new IllegalArgumentException("La nova contrasenya no pot ser la mateixa que l'anterior");

            persona.setContrasenya(password_nova);
            return true;

        }
        return false;
    }

    /**
     * Mostra la informació del perfil d'un usuari, incloent-hi les seves estadístiques.
     * 
     * @param username nom de l'usuari
     * @return true sempre
     * @throws IllegalArgumentException si no existeix cap usuari amb el nom especificat
     */
    public boolean veurePerfil(String username)
    {
        Usuari usuari = existeixUsuari(username);

        Persona persona = (Persona) usuari; //cast a Persona (hereda de Usuari)
        System.out.println("Nom: " + persona.getNom());
        System.out.println("Correu: " + persona.getCorreu());
        System.out.println("Estadistiques: ");

        Estadistiques e = persona.getEstadistiques();
        System.out.println("Partides guanyades: " + e.getPartidesGuanyades());
        System.out.println("Partides perdudes: " + e.getPartidesPerdudes());
        System.out.println("Partides jugades: " + e.getPartidesJugades());
        System.out.println("Puntuacio total: " + e.getPuntuacioTotal());
        System.out.println("Nivell de ranking: " + e.getNivellRanking());
        return true;
    }

    /**
     * Obté la llista de partides en curs d'un usuari.
     * 
     * @param usuari objecte Usuari del qual es volen obtenir les partides
     * @return llista de partides en curs, o null si l'usuari no és de tipus Persona
     */
    public List<Partida> getPartides(Usuari usuari)
    {
        if (usuari instanceof Persona) {
            Persona persona = (Persona) usuari; //cast a Persona (hereda de Usuari)
            return persona.getPartidesEnCurs();
        }
        return null;
    }
}