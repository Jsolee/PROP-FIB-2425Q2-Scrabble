package Main;

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
        System.out.println("Has seleccionat jugar una partida!");
        mostrarOpcionsJugarPartida();
        int opcioPartida = scanner.nextInt();
        scanner.nextLine(); // Consumir el salt de línia

        switch (opcioPartida) 
        {
          case 1:
            System.out.println("Has seleccionat jugar una partida 1 vs 1");
            System.out.println("Inicia sessio del jugador 2:");
            Usuari Jugador2 = inputIniciarSessio(scanner);
            System.out.println("Es jugara una partida entre " + Jugador1.getNom() + " i " + Jugador2.getNom());
            Partida partida = inputJugarPartida(scanner, List.of(Jugador1, Jugador2));
            System.out.println("Partida amb nom  " + partida.getNom() +  " creada correctament");
            inputPartidaEnJoc(scanner, partida);
            break;
          case 2:

            break;
          case 3:

            break;
          default:

            break;
        }
      }

      scanner.close();  
    }
        

  private void mostrarComandes()
  {
    System.out.println("Comandes disponibles (introdueix el numero):");
    System.out.println("1. Crear Usuari");
    System.out.println("2. Crear Partida");
    System.out.println("3. Sortir");
    System.out.print("> ");
  }

  private void mostrarOpcionsJugarPartida()
  {
    System.out.println("Opcions disponibles (introdueix el numero):");
    System.out.println("1. Nova partida 1 vs 1");
    System.out.println("2. Nova partida 1 vs BOT");
    System.out.println("3. Carregar partida");
    System.out.print("> ");
  }

  private Usuari inputIniciarSessio(Scanner scanner)
  {
    boolean sessioIniciada = false;
    String nomUsuari = "";
    while (!sessioIniciada)
    {
      System.out.println("Introdueix el nom d'usuari:");
      System.out.print("> ");
      nomUsuari = scanner.nextLine();
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

      mostrarAtrilActual(partida);
      System.out.println();
      
      System.out.println("OPCIONS DISPONIBLES (introdueix el numero):");
      System.out.println("1. Jugar paraula");
      System.out.println("2. Passar torn");
      System.out.println("3. Intercanviar fitxes");
      System.out.println("4. Veure perfil rival");
      System.out.println("5. Guardar partida");
      System.out.println("6. Rendir-se");
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
    boolean paraulaJugada = false;
    
    while (!paraulaJugada)
    {
      System.out.println("Introdueix la paraula a jugar:");
      System.out.print("> ");
      String paraula = scanner.nextLine();
      System.out.println("Introdueix la fila i columna de la primera lletra de la paraula:");
      System.out.print("> ");
      int fila = scanner.nextInt();
      int columna = scanner.nextInt();
      scanner.nextLine(); // Consumir el salt de línia
      System.out.println("Introdueix l'orientacio (H/V):");
      System.out.print("> ");
      String orientacio = scanner.nextLine();
      try {
        int puntuacio = cd.jugarParaula(partida, paraula, fila, columna, orientacio);
        System.out.println("Paraula jugada correctament. Puntuacio total de la jugada: " + puntuacio);
        paraulaJugada = true;
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
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