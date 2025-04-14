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

    public void iniciarSessio(String username, String contrasenya) {
        controladorUsuari.iniciarSessio(username, contrasenya);
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

    //gestio de partides
    public Partida crearPartida(String nomPartida, List<Usuari> jugadors, String idioma) {
        return controladorPartida.crearPartida(nomPartida, jugadors, idioma);
    }

    public void finalitzarPartida(String nomPartida) {
        controladorPartida.finalitzarPartida(nomPartida);
    }

    public void getPartida(String nomPartida) {
        controladorPartida.getPartida(nomPartida);
    }

    public void mostrarEstatPartida(String nomPartida) {
        controladorPartida.mostrarEstatPartida(nomPartida);
    }

    public void jugarParaula(String nomPartida, String paraula, int f, int col, String orientacion) {
        controladorPartida.jugarParaula(nomPartida, paraula, f, col, orientacion);
    }

    public void canviDeFitxes(String nomPartida, String[] fitxes)
    {
        controladorPartida.canviDeFitxes(nomPartida, fitxes);
    }

    public void esFinalPartida(String nomPartida)
    {
        controladorPartida.esFinalPartida(nomPartida);
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
}
