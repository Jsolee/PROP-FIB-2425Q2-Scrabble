package Main;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Main {
  private ControladorUsuari controladorUsuari;
  private ControladorPartida controladorPartida;
  //private Partida partida;

  public Main() {
    controladorUsuari = new ControladorUsuari();
    controladorPartida = new ControladorPartida();
  }

  public void start() {
    Scanner scanner = new Scanner(System.in);
    boolean exit = false;

    while (!exit) {
      System.out.println("\n===== SCRABBLE GAME =====");
      System.out.println("1. Start a new game");
      System.out.println("2. Manage users");
      System.out.println("3. View statistics");
      System.out.println("5. Exit");
      System.out.print("Choose an option: ");

      int choice;
      try {
        choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
      } catch (Exception e) {
        scanner.nextLine(); // Clear invalid input
        System.out.println("Please enter a valid number.");
        continue;
      }

      switch (choice) {
        case 1:
          startNewGame(scanner);
          break;
        case 2:
          manageUsers(scanner);
          break;
        case 3:
          viewStatistics(scanner);
          break;
        case 5:
          exit = true;
          System.out.println("Thank you for playing Scrabble!");
          break;
        default:
          System.out.println("Invalid option. Please try again.");
      }
    }

    scanner.close();
  }

  private void selectLanguage(Scanner scanner) {
  }

  private void startNewGame(Scanner scanner) 
  {
    System.out.println("\n==== NEW GAME ====");
    System.out.print("Enter the game name: ");
    String gameName = scanner.nextLine();

    System.out.print("Enter timeout in seconds (default 60): ");
    int timeout;
    try {
      String input = scanner.nextLine().trim();
      timeout = input.isEmpty() ? 60 : Integer.parseInt(input);
    } catch (NumberFormatException e) {
      timeout = 60;
    }

    //partida = new Partida(timeout, gameName);
    List<Usuari> players = new ArrayList<>();
    players.add(new Persona("Player 1", "aaaa", "aaaa"));
    players.add(new Persona("Player 2", "bbbb", "bbbb"));


    playGame(scanner, gameName);
  }

  private void playGame(Scanner scanner, String nomPartida) 
  {
    System.out.println("\n==== PLAYING GAME ====");
    Partida partida = controladorPartida.getPartida(nomPartida);    
    // Show initial board state
    System.out.println("\nInitial board:");
    partida.getTaulell().mostrarTaulell();

    boolean playing = true;
    while (playing) 
    {
      System.out.println("\nTiles remaining: " + partida.getBossa().getQuantitatFitxes());
      System.out.println("\nCurrent player: " + partida.getJugadorActual().getNom());
      System.out.println("\nYour hand:");
      List<Fitxa> atrilActual = partida.getAtril();

      int i = 0;
      for (Fitxa f : atrilActual)
      {
        System.out.println(i + ": " + f.getLletra() + " (" + f.getValor() + ")");
        ++i;
      }
      
      System.out.println("\nGame options:");
      System.out.println("1. Play a word");
      System.out.println("2. Exchange tiles");
      System.out.println("3. Skip turn");
      System.out.println("4. End game");
      System.out.println("5. Save game");
      System.out.print("Choose an option: ");

      int choice;
      try {
        choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
      } catch (Exception e) {
        scanner.nextLine(); // Clear invalid input
        System.out.println("Please enter a valid number.");
        continue;
      }

      switch (choice) {
        case 1:
          if (playWord(scanner, atrilActual, partida))
            partida.passarTorn();
          break;
        case 2:
          exchangeTiles(scanner, atrilActual, partida);
          partida.passarTorn();
          break;
        case 3:
          System.out.println("Turn skipped.");
          partida.passarTorn();
          break;
        case 4:
          playing = false;
          partida.acabarPartida(); 
          System.out.println("Game ended.");
          break;
        case 5:
          System.out.println("Game saved.");
          partida.guardarPartida();
          playing = false;
          break;
        default:
          System.out.println("Invalid option.");
      }
      // Show the board after each action
      if (playing) {
        System.out.println("\nCurrent board state:");
        partida.getTaulell().mostrarTaulell();
      }
    }
  }

  //returns true if the word played was valid
  private boolean playWord(Scanner scanner, List<Fitxa> hand, Partida partida) {
    System.out.println("Enter coordinates to play (format: row column direction)");
    System.out.println("Direction: H for horizontal, V for vertical");
    System.out.print("> ");

    int row, col;
    String direction;
    try {
      String[] input = scanner.nextLine().split(" ");
      row = Integer.parseInt(input[0]);
      col = Integer.parseInt(input[1]);
      direction = input[2].toUpperCase();
    } catch (Exception e) {
      System.out.println("Invalid input format. Please try again.");
      return false;
    }

    System.out.print("Enter word to play: ");
    String word = scanner.nextLine().toUpperCase();

    // Validate if player has the tiles for this word
    boolean canForm = true;
    List<Fitxa> usedTiles = new ArrayList<>();
    List<Integer> usedIndices = new ArrayList<>();

    for (char c : word.toCharArray()) 
    {
      boolean found = false;
//      for (int i = 0; i < hand.size(); i++)
//      {
//        if (!usedIndices.contains(i) && hand.get(i).getLletra() == c)
//        {
//          usedTiles.add(hand.get(i));
//          usedIndices.add(i);
//          found = true;
//          break;
//        }
//      }

      if (!found) {
        canForm = false;
        break;
      }
    }

    if (!canForm) {
      System.out.println("You don't have the necessary tiles to form this word.");
      return false;
    }

    // Try to place the word on the board
    Taulell board = partida.getTaulell();
    boolean placed = true;
    List<int[]> positions = new ArrayList<>();

    for (int i = 0; i < word.length(); i++) 
    {
      int r = row;
      int c = col;
      if (direction.equals("H")) 
        c += i;
      else 
        r += i;

      if (!board.colocarFitxa(r, c, usedTiles.get(i))) {
        placed = false;
        break;
      }
      positions.add(new int[]{r, c});
    }

    if (placed) 
    {
      // Remove used tiles from hand
      usedIndices.sort(Collections.reverseOrder());
      for (int idx : usedIndices) {
        hand.remove(idx);
      }

      // Calculate score
      int score = board.calcularPuntuacioMoviment(usedTiles, positions);
      System.out.println("Word played successfully! Score: " + score);

      // Draw new tiles
      for (int i = 0; i < usedTiles.size() && !partida.getBossa().esBuida(); i++) {
        Fitxa newTile = partida.getBossa().agafarFitxa();
        if (newTile != null) {
          hand.add(newTile);
        }
      }
    } 
    else 
    {
      System.out.println("Cannot place word at that position. Please try again.");
      // If placement failed, remove any tiles that were placed
      for (int[] pos : positions) {
        board.retirarFitxa(pos[0], pos[1]);
      }
      return false;
    }

    return true;
  }

  private void exchangeTiles(Scanner scanner, List<Fitxa> hand, Partida partida) {
    if (partida.getBossa().getQuantitatFitxes() < 7) {
      System.out.println("Not enough tiles in the bag to exchange.");
      return;
    }

    System.out.print("Enter tile numbers to exchange (separated by spaces): ");
    String input = scanner.nextLine();
    String[] selections = input.split(" ");

    List<Integer> indices = new ArrayList<>();
    for (String s : selections) {
      try {
        int index = Integer.parseInt(s) - 1;
        if (index >= 0 && index < hand.size()) {
          indices.add(index);
        }
      } catch (NumberFormatException e) {
        // Ignore invalid input
      }
    }

    // Sort in descending order to avoid index shifting when removing
    indices.sort((a, b) -> b - a);

    // Exchange tiles
    for (int index : indices) {
      Fitxa removed = hand.remove(index);
      partida.getBossa().retornarFitxa(removed);
      Fitxa newTile = partida.getBossa().agafarFitxa();
      hand.add(newTile);
    }

    System.out.println(indices.size() + " tiles exchanged successfully.");
  }

  private void manageUsers(Scanner scanner) {
    System.out.println("\n==== MANAGE USERS ====");
    System.out.println("1. Add a new user");
    System.out.println("2. Remove a user");
    System.out.println("3. List users");
    System.out.println("4. Back to main menu");
    System.out.print("Choose an option: ");

    int choice;
    try {
      choice = scanner.nextInt();
      scanner.nextLine(); // Consume newline
    } catch (Exception e) {
      scanner.nextLine(); // Clear invalid input
      System.out.println("Please enter a valid number.");
      return;
    }

    switch (choice) {
      case 1:
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter user's name: ");
        String name = scanner.nextLine();
        // Create Persona object and add it via controladorUsuari
        System.out.println("User added successfully!");
        break;
      case 2:
        System.out.print("Enter username to remove: ");
        String userToRemove = scanner.nextLine();
        // Use controladorUsuari to remove the user
        System.out.println("User removed successfully!");
        break;
      case 3:
        System.out.println("List of users:");
        // Display users from controladorUsuari
        System.out.println("No users found."); // Placeholder
        break;
      case 4:
        return;
      default:
        System.out.println("Invalid option.");
    }
  }

  private void viewStatistics(Scanner scanner) {
    System.out.println("\n==== STATISTICS ====");
    System.out.println("1. Game statistics");
    System.out.println("2. User statistics");
    System.out.println("3. Back to main menu");
    System.out.print("Choose an option: ");

    int choice;
    try {
      choice = scanner.nextInt();
      scanner.nextLine(); // Consume newline
    } catch (Exception e) {
      scanner.nextLine(); // Clear invalid input
      System.out.println("Please enter a valid number.");
      return;
    }

    switch (choice) {
      case 1:
        System.out.print("Enter game name: ");
        String gameName = scanner.nextLine();
        Partida partida = controladorPartida.getPartida(gameName);
        if (partida != null) {
          System.out.println("Current game: " + partida.getNom());
          System.out.println("Tiles remaining: " + partida.getBossa().getQuantitatFitxes());
          System.out.println("Partida acabadada?: " + partida.getPartidaAcabada());
        } else {
          System.out.println("No active game found.");
        }
        break;
      case 2:
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.println("Statistics for " + username + ":");
        System.out.println("Games played: 0"); // Placeholder
        System.out.println("Highest score: 0"); // Placeholder
        break;
      case 3:
        return;
      default:
        System.out.println("Invalid option.");
    }
  }

  public static void main(String[] args) {
    Main main = new Main();
    main.start();
  }
}