package Domini;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Controlador principal que coordina les diferents parts del domini del joc.
 * Actua com a intermediari entre la capa de presentació i els controladors específics
 * per a usuaris, partides i rànquings.
 */
public class ControladorDomini {
    /** Controlador que gestiona la informació dels usuaris */
    private ControladorUsuari controladorUsuari;
    /** Controlador que gestiona les partides */
    private ControladorPartida controladorPartida;
    /** Controlador que gestiona els rànquings */
    private ControladorRanking controladorRanking;

    /**
     * Constructor per defecte.
     * Inicialitza els controladors d'usuari, partida i rànquing.
     */
    public ControladorDomini() {
        controladorUsuari = new ControladorUsuari();
        controladorPartida = new ControladorPartida();
        controladorRanking = new ControladorRanking();
    }

    /**
     * Crea un nou usuari i el registra al sistema.
     * 
     * @param username nom d'usuari
     * @param correu correu electrònic de l'usuari
     * @param contrasenya contrasenya de l'usuari
     * @param edat edat de l'usuari
     * @param pais país de residència de l'usuari
     * @return objecte Usuari creat
     */
    public Usuari crearUsuari(String username, String correu, String contrasenya, String edat, String pais) {
        return controladorUsuari.registrarPersona(username, correu, contrasenya,edat, pais);
    }

    /**
     * Afegeix un nou usuari al rànquing.
     * 
     * @param persona objecte Persona que es vol afegir al rànquing
     */
    public void afegirNouUsuariRanking(Persona persona) {
        controladorRanking.afegirNouUsuari(persona);
    }

    /**
     * Verifica les credencials d'usuari i inicia la sessió si són correctes.
     * 
     * @param username nom d'usuari
     * @param contrasenya contrasenya de l'usuari
     * @return true si s'ha iniciat la sessió correctament, false en cas contrari
     */
    public boolean iniciarSessio(String username, String contrasenya) {
        return controladorUsuari.iniciarSessio(username, contrasenya);
    }

    /**
     * Elimina un compte d'usuari del sistema i del rànquing.
     * 
     * @param username nom de l'usuari a eliminar
     */
    public void eliminarCompte(String username) {
        controladorRanking.eliminarUsuari((Persona) controladorUsuari.getUsuari(username));
        controladorUsuari.eliminarCompte(username);
    }

    /**
     * Tanca la sessió d'un usuari.
     * 
     * @param username nom de l'usuari que vol tancar la sessió
     */
    public void tancarSessio(String username) {
        controladorUsuari.tancarSessio(username);
    }

    /**
     * Canvia la contrasenya d'un usuari.
     * 
     * @param username nom d'usuari
     * @param contrasenya contrasenya actual
     * @param contrasenyaNova nova contrasenya
     */
    public void restablirContrasenya(String username, String contrasenya, String contrasenyaNova) {
        controladorUsuari.restablirContrasenya(username, contrasenya, contrasenyaNova);
    }

    /**
     * Mostra la informació del perfil d'un usuari.
     * 
     * @param username nom de l'usuari
     */
    public void veurePerfil(String username) {
        controladorUsuari.veurePerfil(username);
    }

    /**
     * Obté un usuari pel seu nom d'usuari.
     * 
     * @param username nom de l'usuari
     * @return objecte Usuari corresponent, o null si no existeix
     */
    public Usuari getUsuari(String username) {
        return controladorUsuari.existeixUsuari(username);
    }

    /**
     * Obté la llista de partides en curs d'un jugador.
     * 
     * @param jugador objecte Usuari del jugador
     * @return llista de partides en curs
     */
    public List<Partida> getPartidesEnCurs(Usuari jugador) {
        return controladorUsuari.getPartides(jugador);
    }

    /**
     * Obté la instància única del Bot del joc.
     * 
     * @return objecte Usuari que representa el bot
     */
    public Usuari getBot()
    {
        return Bot.getInstance();
    }

    /**
     * Obté els primers n usuaris del rànquing.
     * 
     * @param n nombre d'usuaris a obtenir
     * @return llista dels n millors usuaris del rànquing
     */
    public List<Persona> getRanking(int n) {
        return controladorRanking.getRanking(n);
    }

    /**
     * Crea una nova partida amb els jugadors i l'idioma especificats.
     * 
     * @param nomPartida nom per a la nova partida
     * @param jugadors llista de jugadors que participaran a la partida
     * @param idioma idioma de la partida (catalan, castellano o english)
     * @return objecte Partida creat
     */
    public Partida crearPartida(String nomPartida, List<Usuari> jugadors, String idioma) {
        return controladorPartida.crearPartida(nomPartida, jugadors, idioma);
    }

    /**
     * Obté una partida pel seu nom.
     * 
     * @param nomPartida nom de la partida
     * @return objecte Partida corresponent
     */
    public Partida getPartida(String nomPartida) {
        return controladorPartida.getPartida(nomPartida);
    }

    /**
     * Intenta jugar una paraula a la partida.
     * 
     * @param partida objecte partida on es vol jugar
     * @param jugades mapa de posicions i fitxes que formen la paraula
     * @param across orientació de la paraula ("H" per horitzontal o "V" per vertical)
     * @return puntuació obtinguda, o -1 si la jugada no és vàlida
     */
    public int jugarParaula(Partida partida, LinkedHashMap<int[], Fitxa> jugades, String across, List<Integer> indexs) {
        return controladorPartida.jugarParaula(partida, jugades, across, indexs);
    }

    /**
     * Canvia fitxes de l'atril del jugador actual.
     * 
     * @param partida objecte partida
     * @param fitxes array amb els índexs de les fitxes a canviar
     * @return true si s'han canviat les fitxes correctament, false en cas contrari
     */
    public boolean canviDeFitxes(Partida partida, String[] fitxes)
    {
        return controladorPartida.canviDeFitxes(partida, fitxes);
    }

    /**
     * Verifica si una partida ha finalitzat.
     * 
     * @param partida objecte partida a verificar
     * @return true si la partida ha finalitzat, false en cas contrari
     */
    public boolean esFinalPartida(Partida partida)
    {
        return controladorPartida.esFinalPartida(partida);
    }

    /**
     * Obté l'usuari a qui li toca jugar a la partida especificada.
     * 
     * @param nomPartida nom de la partida
     * @return objecte Usuari del jugador actual
     */
    public Usuari tornDelJugador(String nomPartida)
    {
        return controladorPartida.tornDe(nomPartida);
    }

    /**
     * Obté el taulell d'una partida.
     * 
     * @param nomPartida nom de la partida
     * @return objecte Taulell de la partida
     */
    public Taulell getTaulell(String nomPartida)
    {
        return controladorPartida.getPartida(nomPartida).getTaulell();
    }

    /**
     * Obté l'atril del jugador actual en una partida.
     * 
     * @param nomPartida nom de la partida
     * @return llista de fitxes de l'atril
     */
    public List<Fitxa> getAtril(String nomPartida)
    {
        return controladorPartida.getPartida(nomPartida).getAtril();
    }

    /**
     * Finalitza una partida i actualitza les estadístiques dels jugadors.
     * 
     * @param partida objecte partida a finalitzar
     */
    public void acabarPartida(Partida partida)
    {
        controladorPartida.acabarPartida(partida);
    }

    /**
     * Fa que el bot jugui la millor jugada possible a la partida.
     * @param partida objecte partida on jugarà el bot
     * @param bot objecte Usuari que representa el bot
     */
    public void posarParaulaBot(Partida partida, Usuari bot)
    {
        controladorPartida.getMillorJugada(partida, bot);
    }

    /**
     * Obté la llista de jugadors d'una partida.
     * 
     * @param nompartida nom de la partida
     * @return llista d'objectes Usuari que participen a la partida
     */
    public List<Usuari> getJugadors(String nompartida)
    {
        return controladorPartida.getJugadors(nompartida);
    }
}