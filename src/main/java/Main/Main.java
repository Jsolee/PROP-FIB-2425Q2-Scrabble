package Main;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Main {
  private ControladorUsuari controladorUsuari;
  private Partida partida;
  private String selectedLanguage;

  public Main() {
    controladorUsuari = new ControladorUsuari();
    selectedLanguage = Bossa.CATALA; // Default language
  }

  public void start() {
    Scanner scanner = new Scanner(System.in);
    boolean exit = false;

    while (!exit) {
      System.out.println("\n===== SCRABBLE GAME =====");
      System.out.println("1. Start a new game");
      System.out.println("2. Manage users");
      System.out.println("3. View statistics");
      System.out.println("4. Select language (" + selectedLanguage + ")");
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
        case 4:
          selectLanguage(scanner);
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
    System.out.println("\n==== SELECT LANGUAGE ====");
    System.out.println("1. Català");
    System.out.println("2. Español");
    System.out.println("3. English");
    System.out.print("Choose language: ");

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
        selectedLanguage = Bossa.CATALA;
        break;
      case 2:
        selectedLanguage = Bossa.ESPANYOL;
        break;
      case 3:
        selectedLanguage = Bossa.ANGLES;
        break;
      default:
        System.out.println("Invalid option. Language not changed.");
        return;
    }
    System.out.println("Language set to: " + selectedLanguage);
  }

  private void startNewGame(Scanner scanner) {
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

    partida = new Partida(timeout, gameName);
    partida.setBossa(new Bossa(selectedLanguage));

    System.out.println("Game '" + gameName + "' started with language: " + selectedLanguage);
    playGame(scanner);
  }

  private void playGame(Scanner scanner) {
    System.out.println("\n==== PLAYING GAME ====");
    List<Fitxa> hand = new ArrayList<>();

    // Draw initial tiles
    for (int i = 0; i < 7; i++) {
      Fitxa fitxa = partida.getBossa().agafarFitxa();
      if (fitxa != null) {
        hand.add(fitxa);
      }
    }

    boolean playing = true;
    while (playing) {
      System.out.println("\nYour hand:");
      for (int i = 0; i < hand.size(); i++) {
        System.out.println((i + 1) + ". " + hand.get(i));
      }

      System.out.println("\nGame options:");
      System.out.println("1. Play a word");
      System.out.println("2. Exchange tiles");
      System.out.println("3. Skip turn");
      System.out.println("4. End game");
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
          playWord(scanner, hand);
          break;
        case 2:
          exchangeTiles(scanner, hand);
          break;
        case 3:
          System.out.println("Turn skipped.");
          break;
        case 4:
          playing = false;
          System.out.println("Game ended.");
          break;
        default:
          System.out.println("Invalid option.");
      }
    }
  }

  private void playWord(Scanner scanner, List<Fitxa> hand) {
    System.out.print("Enter word to play: ");
    String word = scanner.nextLine().toUpperCase();
    System.out.println("You played: " + word);

    // Mock implementation - in a real game, validate the word and update the board
    System.out.println("Word played successfully! Score: 10");

    // Replace used tiles
    for (int i = 0; i < word.length() && !partida.getBossa().esBuida(); i++) {
      Fitxa fitxa = partida.getBossa().agafarFitxa();
      if (fitxa != null) {
        hand.add(fitxa);
      }
    }
  }

  private void exchangeTiles(Scanner scanner, List<Fitxa> hand) {
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
        if (partida != null) {
          System.out.println("Current game: " + partida.getNom());
          System.out.println("Tiles remaining: " + partida.getBossa().getQuantitatFitxes());
          System.out.println("Language: " + partida.getBossa().getIdioma());
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