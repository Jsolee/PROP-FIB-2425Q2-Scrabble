package Main;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;


public class Main {
  private ControladorDomini cd;
  //private Partida partida;

  public Main() {
    cd = new ControladorDomini();
  }

  public void start() 
  {
      Scanner scanner = new Scanner(System.in);
      Usuari Jugador1 = cd.crearUsuari("Pep", "pep", "1234");
      cd.crearUsuari("Anna", "anna", "1234"); //prova per iniciar sessio 
      //borrar estos dos usuarios una vez ya este implementada la gestion de usuarios

      System.out.println("BENVINGUT A LA PARTIDA DE L'SCRABBLE!!");
      mostrarComandes();
      int opcio = scanner.nextInt();
      scanner.nextLine(); // Consumir el salt de línia
      while (opcio != 3) 
      {

        switch (opcio)
        {
          case 1:
          case 2:
            opcioJugarPartida(scanner, Jugador1);
            break;
          default:
            System.out.println("Opcio no valida");
            break;
        }
        mostrarComandes();
        opcio = scanner.nextInt();
        scanner.nextLine(); // Consumir el salt de línia
        
      }

      scanner.close();  
    }
        

  private Usuari registrarUsuari(Scanner scanner)
  {
    System.out.println("Has seleccionat registrar un nou usuari!");
    boolean registrat = false;
    Usuari Jugador2 = null;
    while (!registrat)
    {
      System.out.println("escriu EXIT per sortir del registre");
      System.out.println("Introdueix el nom d'usuari:");
      System.out.print("> ");
      String nomUsuari = scanner.nextLine();
      if (nomUsuari.equals("EXIT")) 
      {
        System.out.println("Sortint del registre...");
        return Jugador2;
      }
      System.out.println("Introdueix el nom:");
      System.out.print("> ");
      String nom = scanner.nextLine();
      System.out.println("Introdueix la contrasenya:");
      System.out.print("> ");
      String contrasenya = scanner.nextLine();
  
      try {
        Jugador2 = cd.crearUsuari(nom, nomUsuari, contrasenya);
        System.out.println("Usuari creat correctament");
        registrat = true;
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
    }
    return Jugador2;
  }

  private void opcioJugarPartida(Scanner scanner, Usuari Jugador1)
  {
    System.out.println("Has seleccionat jugar una partida!");
    mostrarOpcionsJugarPartida();
    int opcioPartida = scanner.nextInt();
    scanner.nextLine(); // Consumir el salt de línia

    switch (opcioPartida) 
    {
      case 1:
        System.out.println("Has seleccionat jugar una partida 1 vs 1");
        partida1vs1(scanner, Jugador1);
        break;
      case 2:
        System.out.println("Has seleccionat jugar una partida 1 vs BOT");
        partida1vsbot(scanner, Jugador1);
        break;
      case 3:
        List<Partida> partides = cd.getPartidesEnCurs(Jugador1);
        System.out.println("Aquestes son les partides actives de " + Jugador1.getNom() + " Total disponible: " + partides.size());
        if (partides.size() > 0){
          for (Partida partida : partides) 
          System.out.println(partida.getNom());

          System.out.println("Introdueix el nom de la partida a carregar:");
          System.out.print("> ");
          String nomPartida = scanner.nextLine();
          seleccionarIJugarPartida(scanner, nomPartida);
        }
        break;
      default:
        break;
    }
  }

  private void partida1vsbot(Scanner scanner, Usuari Jugador1)
  {
    Usuari Jugador2 = cd.getBot();
    System.out.println("Es jugara una partida entre " + Jugador1.getNom() + " i el BOT." );
    List<Usuari> Jugadors = List.of(Jugador1, Jugador2);
    Partida partida = inputJugarPartida(scanner, Jugadors);
    System.out.println("Partida amb nom  " + partida.getNom() +  " creada correctament");
    inputPartidaEnJoc(scanner, partida);
    return;
  }

  private void seleccionarIJugarPartida(Scanner scanner, String nomPartida)
  {
    System.out.println("Has seleccionat carregar la partida " + nomPartida);
    try {
      Partida partida = cd.getPartida(nomPartida);
      System.out.println("Partida carregada correctament");
      partida.setNoGuardada();
      inputPartidaEnJoc(scanner, partida);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  private void mostrarComandes()
  {
    System.out.println("Comandes disponibles (introdueix el numero):");
    System.out.println("1. Crear Usuari");
    System.out.println("2. Crear Partida");
    System.out.println("3. Sortir del joc");
    System.out.print("> ");
  }

  private void partida1vs1(Scanner scanner, Usuari Jugador1)
  {
    System.out.println("1. Registrar al segon jugador");
    System.out.println("2. Iniciar sessio del segon jugador");
    System.out.println(">= 3. Enrere");
    System.out.print("> ");
    int opcioJugador2 = scanner.nextInt();
    scanner.nextLine(); // Consumir el salt de línia
    Usuari Jugador2 = null;
    switch (opcioJugador2) 
    {
      case 1:
        Jugador2 = registrarUsuari(scanner);
        break;
      case 2:
        Jugador2 = inputIniciarSessio(scanner);
        break;
      default:
        return;
    }

    if (Jugador2 == null)
      return;
  
    System.out.println("Es jugara una partida entre " + Jugador1.getNom() + " i " + Jugador2.getNom());
    Partida partida = inputJugarPartida(scanner, List.of(Jugador1, Jugador2));
    System.out.println("Partida amb nom  " + partida.getNom() +  " creada correctament");
    inputPartidaEnJoc(scanner, partida);
    return;
  }


  private void mostrarOpcionsJugarPartida()
  {
    System.out.println("Opcions disponibles (introdueix el numero):");
    System.out.println("1. Nova partida 1 vs 1");
    System.out.println("2. Nova partida 1 vs BOT");
    System.out.println("3. Carregar partida");
    System.out.println(">= 4. Enrere");
    System.out.print("> ");
  }

  private Usuari inputIniciarSessio(Scanner scanner)
  {
    System.out.println("Has seleccionat iniciar sessio!");
    boolean sessioIniciada = false;
    String nomUsuari = "";
    while (!sessioIniciada)
    {
      System.out.println("escriu EXIT per sortir de l'inici de sessio");
      System.out.println("Introdueix el nom d'usuari:");
      System.out.print("> ");
      nomUsuari = scanner.nextLine();

      if (nomUsuari.equals("EXIT")) 
      {
        System.out.println("Sortint de l'inici de sessio...");
        return null;
      }

      System.out.println("Introdueix la contrasenya:");
      System.out.print("> ");
      String contrasenya = scanner.nextLine();
      try {
        sessioIniciada = cd.iniciarSessio(nomUsuari, contrasenya);
        System.out.println("Sessio iniciada correctament");
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
    }
    return cd.getUsuari(nomUsuari);
  }

  private Partida inputJugarPartida(Scanner scanner, List<Usuari> Jugadors)
  {
    Partida partida = null;
    boolean partidaCreada = false;

    while (!partidaCreada)
    {
      System.out.println("Introdueix el nom de la partida:");
      System.out.print("> ");
      String nomPartida = scanner.nextLine();
      System.out.println("Introdueix l'idioma (catalan/castellano/english):");
      System.out.print("> ");
      String idioma = scanner.nextLine();
      
      try {
        partida = cd.crearPartida(nomPartida, Jugadors, idioma);
        partidaCreada = true;
        System.out.println("Partida creada correctament");
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
    }

    return partida;
  }

  private void inputPartidaEnJoc(Scanner scanner, Partida partida)
  {
    boolean noMostrarDetallsFinals = false;
    while (!cd.esFinalPartida(partida))
    {
      Taulell taulell = partida.getTaulell();
      mostrarTaulell(taulell);

      
      System.out.println("Fitxes restants a la bossa: " + partida.getBossa().getQuantitatFitxes());
      
      System.out.println("PUNTUACIONS ACTUALS:");
      List<Integer> puntuacions = partida.getPuntuacions();
      List<Usuari> jugadors = partida.getJugadors();
      for (int i = 0; i < puntuacions.size(); i++) 
        System.out.println(jugadors.get(i).getNom() + ": " + puntuacions.get(i) + " punts");
      System.out.println();

      System.out.println("Torn de: " + partida.getJugadorActual().getNom());
      System.out.println();

      if (partida.getJugadorActual() instanceof Bot) 
      {
        cd.posarParaulaBot(partida, partida.getJugadorActual());
        continue;
      }

      mostrarAtrilActual(partida);
      System.out.println();
      
      System.out.println("OPCIONS DISPONIBLES (introdueix el numero):");
      System.out.println("1. Jugar paraula");
      System.out.println("2. Passar torn");
      System.out.println("3. Intercanviar fitxes");
      System.out.println("4. Veure perfil rival");
      System.out.println("5. Guardar partida");
      System.out.println("6. Rendir-se");
      System.out.println("7. Veure el taulell");
      System.out.print("> ");

      int opcio = scanner.nextInt();
      scanner.nextLine(); // Consumir el salt de línia
      switch (opcio)
      {
        case 1:
          jugarParaula(scanner, partida);
          break;
        case 2:
          partida.passarTorn();
          System.out.println("Torn passat correctament");
          break;
        case 3:
          canviarFitxes(scanner, partida);
          break;
        case 4:
          System.out.println("Veient perfil rival...");
          break;
        case 5:
          partida.guardarPartida();
          System.out.println("Partida guardada correctament");
          noMostrarDetallsFinals = true;
          break;  
        case 6:
          rendirse(scanner, partida);
          noMostrarDetallsFinals = true;
          break;
        case 7:
          mostrarTaulell(taulell);
          break;
        default:
          System.out.println("Opcio no valida");
          break;
      }
      System.out.println();
    }
    
    if (!noMostrarDetallsFinals) 
      finalitzarPartida(partida);
  }

  public void finalitzarPartida(Partida partida) 
    {
        cd.acabarPartida(partida);

        Usuari guanyador = partida.determinarGuanyador();
        System.out.println("Partida finalitzada!");
        System.out.println("Guanyador: " + guanyador.getNom());

        List<Integer> puntuacions = partida.getPuntuacions();
        List<Usuari> jugadors = partida.getJugadors();
        System.out.println("Puntuacions finals:");

        for (int i = 0; i < puntuacions.size(); i++) 
            System.out.println(jugadors.get(i).getNom() + ": " + puntuacions.get(i) + " punts");
    } 

  private void rendirse(Scanner scanner, Partida partida)
  {
    System.out.println(partida.getJugadorActual().getNom() + " s'ha rendit. Partida finalitzada.");
    cd.acabarPartida(partida);
    List<Usuari> jugadors = partida.getJugadors();
    for (Usuari jugador : jugadors) {
      if (jugador != partida.getJugadorActual()) 
        System.out.println("Guanyador: " + jugador.getNom());
    }
  }

  private void canviarFitxes(Scanner scanner, Partida partida)
  {
    System.out.println("Introdueix les fitxes a intercanviar (separades per espais):");
    System.out.print("> ");
    String fitxes = scanner.nextLine();
    String[] fitxesArray = fitxes.split(" ");
    
    try {
      cd.canviDeFitxes(partida, fitxesArray);
      System.out.println("Fitxes intercanviades correctament");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  private void jugarParaula(Scanner scanner, Partida partida)
  {
    System.out.println("Introdueix el numero de l'atril corresponent a la fitxa que vols jugar. Una vegada decideixis parar de afegir introdueix -1:");
    System.out.print("> ");
    int posicio = scanner.nextInt();
    LinkedHashMap<int[], Fitxa> jugades = new LinkedHashMap<>();
    List<Fitxa> atril = partida.getAtril();

    while (posicio >= 0)
    { 
      if (posicio >= atril.size())
      {
        System.out.println("Fitxa no disponible");
        continue;
      }
      else 
      {
        Fitxa fitxa = atril.get(posicio);
        if (fitxa.getLletra() == "#")
        {
          System.out.println("Especifica quina lletra vols que sigui el comodin:");
          System.out.print("> ");
          String lletra = scanner.nextLine();
          fitxa.setLletra(lletra);;
        }

        System.out.println("Introdueix la posicio on vols colocar la fitxa: " + atril.get(posicio).getLletra() + "  (format: primer numero de la fila i despres la columna): ");
        System.out.print("> ");
        int fila = scanner.nextInt();
        int columna = scanner.nextInt();
        int[] posicioFitxa = {fila, columna};
        
        jugades.put(posicioFitxa, fitxa);
      }
      System.out.println("Introdueix el numero de l'atril corresponent a la fitxa que vols jugar. Una vegada decideixis parar de afegir introdueix -1:");
      System.out.print("> ");
      posicio = scanner.nextInt();
    }

    scanner.nextLine(); // Consumir el salt de línia
    System.out.println("Introdueix l'orientacio (H/V):");
    System.out.print("> ");
    String orientacio = scanner.nextLine();
    try {
      int puntuacio = cd.jugarParaula(partida, jugades, orientacio);
      System.out.println("Paraula jugada correctament. Puntuacio total de la jugada: " + puntuacio);
    } catch (IllegalArgumentException e) {
      List<Fitxa> atrilActual = partida.getAtril();
      for (Fitxa fitxa : atrilActual) {
        if (fitxa.getValor() == 0) 
          fitxa.setLletra("#");
      }
      System.out.println(e.getMessage());
    }
    
  }

  private void mostrarAtrilActual(Partida partida)
  {
    List<Fitxa> atril = partida.getAtril();
    int i = 0;
    for (Fitxa fitxa : atril) {
      System.out.print(i + ": " + fitxa.getLletra() + " (" + fitxa.getValor() + ")  ");
      ++i;
    }
    System.out.println();
  }

  private void mostrarTaulell(Taulell taulell) 
  {
      System.out.print("  ");
      for (int i = 0; i < 15; i++) {
          switch (i) {
              case 0:
                  System.out.print("  " + i);
                  break;
              case 1:
                  System.out.print("    " + i);
                  break;
              case 2:
                  System.out.print("    " + i);
                  break;
              case 3:
                  System.out.print("    " + i);
                  break;
              case 4:
                  System.out.print("    " + i);
                  break;
              case 5:
                  System.out.print("    " + i);
                  break;
              case 6:
                  System.out.print("    " + i);
                  break; 
              case 7:
                  System.out.print("    " + i);
                  break;
              case 8:
                  System.out.print("    " + i);
                  break;
              case 9:
                  System.out.print("    " + i);
                  break; 
              case 10:
                  System.out.print("   " + i);
                  break;
              case 11:
                  System.out.print("   " + i);
                  break;
              case 12:
                  System.out.print("   " + i);
                  break;
              case 13:    
                  System.out.print("   " + i);
                  break;
              case 14:
                  System.out.print("   " + i);
                  break; 
          }
      }
      System.out.println();

      for (int i = 0; i < 15; i++) 
      {
          if (i < 10)
              System.out.print(" " + i);
          else
              System.out.print(i);
          for (int j = 0; j < 15; j++) {
            Casella casella = taulell.getCasella(i, j);
              System.out.print(casella.toString() + " ");
          }
          System.out.println();
      }
  }
  

  public static void main(String[] args) {
    Main main = new Main();
    main.start();
  }
}