package Main;

import java.util.LinkedHashMap;
import java.util.List;

public class ControladorDomini {
    private ControladorUsuari controladorUsuari;
    private ControladorPartida controladorPartida;
    private ControladorRanking controladorRanking;

    public ControladorDomini() {
        controladorUsuari = new ControladorUsuari();
        controladorPartida = new ControladorPartida();
        controladorRanking = new ControladorRanking();
    }

    //gestio d'usuaris
    public Usuari crearUsuari(String username, String correu, String contrasenya, String edat, String pais) {
        return controladorUsuari.registrarPersona(username, correu, contrasenya,edat, pais);
    }

    public void afegirNouUsuariRanking(Persona persona) {
        controladorRanking.afegirNouUsuari(persona);
    }

    public boolean iniciarSessio(String username, String contrasenya) {
        return controladorUsuari.iniciarSessio(username, contrasenya);
    }

    public void eliminarCompte(String username) {
        controladorRanking.eliminarUsuari((Persona) controladorUsuari.getUsuari(username));
        controladorUsuari.eliminarCompte(username);
    }

    public void tancarSessio(String username) {
        controladorUsuari.tancarSessio(username);
    }

    public void restablirContrasenya(String username, String contrasenya, String contrasenyaNova) {
        controladorUsuari.restablirContrasenya(username, contrasenya, contrasenyaNova);
    }

    public void veurePerfil(String username) {
        controladorUsuari.veurePerfil(username);
    }

    public Usuari getUsuari(String username) {
        return controladorUsuari.existeixUsuari(username);
    }

    public List<Partida> getPartidesEnCurs(Usuari jugador) {
        return controladorUsuari.getPartides(jugador);
    }

    public Usuari getBot()
    {
        return Bot.getInstance();
    }

    public List<Persona> getRanking(int n) {
        return controladorRanking.getRanking(n);
    }


    /* public Map.Entry<LinkedHashMap<int[], Fitxa>, Boolean> getMillorJugada(Taulell taulell, Diccionari diccionari, ArrayList<Fitxa> atril, ArrayList<String> alfabet) */

    //gestio de partides
    public Partida crearPartida(String nomPartida, List<Usuari> jugadors, String idioma) {
        return controladorPartida.crearPartida(nomPartida, jugadors, idioma);
    }

    public Partida getPartida(String nomPartida) {
        return controladorPartida.getPartida(nomPartida);
    }

    public int jugarParaula(Partida partida, LinkedHashMap<int[], Fitxa> jugades, String across) {
        return controladorPartida.jugarParaula(partida, jugades, across);
    }

    public boolean canviDeFitxes(Partida partida, String[] fitxes)
    {
        return controladorPartida.canviDeFitxes(partida, fitxes);
    }

    public boolean esFinalPartida(Partida partida)
    {
        return controladorPartida.esFinalPartida(partida);
    }

    public Usuari tornDelJugador(String nomPartida)
    {
        return controladorPartida.tornDe(nomPartida);
    }

    public Taulell getTaulell(String nomPartida)
    {
        return controladorPartida.getPartida(nomPartida).getTaulell();
    }

    public List<Fitxa> getAtril(String nomPartida)
    {
        return controladorPartida.getPartida(nomPartida).getAtril();
    }

    public void acabarPartida(Partida partida)
    {
        controladorPartida.acabarPartida(partida);
    }

    public void posarParaulaBot(Partida partida, Usuari bot)
    {
        controladorPartida.getMillorJugada(partida, bot);
    }

}
