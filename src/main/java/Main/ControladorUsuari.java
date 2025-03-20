package Main;

import java.util.HashMap;

public class ControladorUsuari {
    private final HashMap<String, Usuari> usuaris;

    public ControladorUsuari() {
        this.usuaris = new HashMap<>();
    }

    public HashMap<String, Usuari> getUsuaris() {
        return usuaris;
    }

    private boolean existeixUsuari(String username)
    {
        if (!usuaris.containsKey(username)){
            System.out.println("Usuari no existeix");
            return false;
        }
        return true;

    }
    public boolean registrarPersona(String nom, String username, String password)
    {
        if (usuaris.containsKey(username))
        {
            System.out.println("Usuari ja existeix");
            return false;
        }

        Persona persona = new Persona(nom, username, password);
        usuaris.put(username, persona);
        return true;
    }

    public boolean iniciarSessio(String username, String password)
    {
        if (!existeixUsuari(username))
            return false;
        
        Usuari usuari = usuaris.get(username);

        if (usuari instanceof Persona) {
            Persona persona = (Persona) usuari; //cast a Persona (hereda de Usuari)
            if (persona.getContrasenya().equals(password))
            {
                persona.iniciarSessio();
                System.out.println("Sessio iniciada correctament");
                return true;
            }
            System.out.println("Contrasenya incorrecta");
        }
        return false;
    }

    public boolean eliminarCompte(String username)
    {
        if (!existeixUsuari(username))
            return false;

        Usuari usuari = usuaris.get(username);

        if (usuari.teSessioIniciada())
        {
            usuaris.remove(username);
            System.out.println("Compte eliminat correctament");
            return true;
        }
        return false;
    }

    public boolean tancarSessio(String username)
    {
        if (!existeixUsuari(username))
            return false;

        Usuari usuari = usuaris.get(username);
        if (usuari.teSessioIniciada())
        {
            usuari.tancarSessio();
            System.out.println("Sessio tancada correctament");
            return true;
        }
        return false;
    }

    public boolean restablirContrasenya(String username, String password, String password_nova)
    {
        if (!existeixUsuari(username))
            return false;

        Usuari usuari = usuaris.get(username);

        if (usuari instanceof Persona) {
            Persona persona = (Persona) usuari; //cast a Persona (hereda de Usuari)
            if (!persona.getContrasenya().equals(password))
            {
                System.out.println("Contrasenya incorrecta");
                return false;
            }
            if (password.equals(password_nova))
            {
                System.out.println("La contrasenya nova no pot ser igual a l'anterior");
                return false;
            }
            persona.setContrasenya(password_nova);
            System.out.println("Contrasenya canviada correctament");
            return true;
            
        } 
        return false;
    }


}