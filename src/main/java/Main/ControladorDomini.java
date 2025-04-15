package Main;

import java.util.List;

public class ControladorDomini {
    private ControladorUsuari controladorUsuari;
    private ControladorPartida controladorPartida;

    public ControladorDomini() {
        controladorUsuari = new ControladorUsuari();
        controladorPartida = new ControladorPartida();
    }

    //gestio d'usuaris
    public Usuari crearUsuari(String nom, String username, String contrasenya) {
        return controladorUsuari.registrarPersona(nom, username, contrasenya);
    }

    public boolean iniciarSessio(String username, String contrasenya) {
        return controladorUsuari.iniciarSessio(username, contrasenya);
    }

    public void eliminarCompte(String username) {
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

    //gestio de partides
    public Partida crearPartida(String nomPartida, List<Usuari> jugadors, String idioma) {
        return controladorPartida.crearPartida(nomPartida, jugadors, idioma);
    }

    public void getPartida(String nomPartida) {
        controladorPartida.getPartida(nomPartida);
    }

    public int jugarParaula(Partida partida, String paraula, int f, int col, String orientacion) {
        return controladorPartida.jugarParaula(partida, paraula, f, col, orientacion);
    }

    public void canviDeFitxes(Partida partida, String[] fitxes)
    {
        controladorPartida.canviDeFitxes(partida, fitxes);
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

}
