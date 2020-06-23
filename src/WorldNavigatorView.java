import javafx.util.Pair;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class WorldNavigatorView {

  public String welcome() {
    System.out.println("Welcome!");
    System.out.println("Enter your name to continue.");
    Scanner sc = new Scanner(System.in);
    return sc.next();
  }

  public void mapLister(File[] maps) {
    int noOfMaps = maps.length;
    if (noOfMaps == 0) {
      System.out.println("No maps to choose from");
      return;
    }
    System.out.println("Please choose one of the listed maps or 'exit' to leave game:");
    for (int i = 1; i <= noOfMaps; ++i) {
      if (maps[i - 1].isFile()) {
        System.out.print("(" + i + ") " + maps[i - 1].getName() + "  ");
      }
    }
    System.out.println("");
  }

  public void errorNoDirectory(String dir) {
    System.out.println("Error: " + dir + " directory not found!");
  }

  public File mapChooser(File[] maps) {
    int noOfMaps = maps.length;
    mapLister(maps);
    Scanner sc = new Scanner(System.in);
    String input = sc.next();
    while (!input.equals("exit")) {
      try {
        int mapIndex = Integer.parseInt(input);
        if (mapIndex >= 1 && mapIndex <= noOfMaps) {
          System.out.println(".\n.\n.");
          return maps[mapIndex - 1];
        } else {
          System.out.println(
              "Please enter a number between 1 and " + noOfMaps + " or 'exit' to quit the game");
        }
      } catch (NumberFormatException nfe) {
        System.out.println(
            "Please enter a number between 1 and " + noOfMaps + " or 'exit' to quit the game");
      }
      input = sc.next();
    }
    System.exit(0);
    throw new RuntimeException();
  }

  public void errorInExecution(String errorMessage) {
    System.out.println("Error occurred during execution: " + errorMessage);
  }

  public void afterSetUp(String playerName) {
    System.out.println("World Created...");
    System.out.println("Good Luck " + playerName + "!");
    separator();
  }

  public void separator() {
    System.out.println("-------------------------------------------------------------------");
  }

  public void gameInfo(int seconds, int minutes, int facingDirection) {
    System.out.println(minutes + " minutes and " + seconds + " seconds left.");
    System.out.println("Facing " + Direction.values()[facingDirection] + ".");
    System.out.println("Enter 'commands' to find out available commands.");
    System.out.println("Enter 'restart' to restart.");
    System.out.println("Enter 'quit' to return to main menu.");
    separator();
  }

  public void takeCommandFailed(String objectName) {
    System.out.println(objectName + " is empty!");
  }

  public String getInput() {
    Scanner scanner = new Scanner(System.in);
    return scanner.nextLine();
  }

  public void wrongInput() {
    System.out.println("Invalid command!");
    System.out.println("Enter 'commands' to see available commands.");
    separator();
  }

  public void directionChange(int newDirection) {
    System.out.println("Direction Changed!");
    System.out.println("New direction is " + Direction.values()[newDirection] + ".");
    separator();
  }

  public void listAvailableCommands(Map<String, ICommand> availableCommands) {
    System.out.println("Available commands: ");
    if (availableCommands.isEmpty()) {
      System.out.println("No Available Commands.");
    } else {
      int i = 1;
      for (Map.Entry<String, ICommand> command : availableCommands.entrySet()) {
        System.out.print("(" + i + ") " + command.getValue().getViewName() + "   ");
        i++;
      }
      System.out.println("");
    }
    separator();
  }

  public void listObjects(List<Entity> entities) {
    if (entities.isEmpty()) {
      emptyLists();
      return;
    }
    for (int i = 1; i <= entities.size(); ++i) {
      System.out.print("(" + i + ") " + entities.get(i - 1).getName() + "  ");
    }
    System.out.println("");
    separator();
  }

  public void emptyLists() {
    System.out.println("No items to show.");
    separator();
  }

  public void listInventoryItems(Map<String, Pair<Item, Integer>> items) {
    int i = 1;
    if (items.isEmpty()) {
      emptyLists();
      return;
    }
    for (Map.Entry<String, Pair<Item, Integer>> item : items.entrySet()) {
      System.out.print("(" + i + ") " + item.getValue().getValue() + " " + item.getKey() + " ");
      if (item.getKey() != "gold")
        System.out.print("| price = " + item.getValue().getKey().getCost() + " gold each  ");
      i++;
    }
    System.out.println("");
    separator();
  }

  public void listItems(Map<String, Pair<Item, Integer>> items) {
    int i = 1;
    if (items.isEmpty()) {
      emptyLists();
      return;
    }
    for (Map.Entry<String, Pair<Item, Integer>> item : items.entrySet()) {
      System.out.print("(" + i + ") " + item.getValue().getValue() + " " + item.getKey() + " ");
      i++;
    }
    System.out.println("");
    separator();
  }

  public void openObject(String objectName) {
    System.out.println(objectName + " is open.");
    separator();
  }

  public void unlockedObject(String objectName) {
    System.out.println(objectName + " is unlocked.");
  }

  public void closedObjectWithKey(String objectName, String keyName) {
    System.out.println(objectName + " closed '" + keyName + "' key is needed to unlock.");
    separator();
  }

  public void closedObject(String objectName) {
    System.out.println(objectName + " closed.");
  }

  public void darkRoomInfo() {
    System.out.println("Room is dark!");
    System.out.println("Look for a light source.");
    separator();
  }

  public void takeItems() {
    System.out.println("Items added to your inventory!");
    separator();
  }

  public void switchLight(boolean isSwitchAvailable, boolean switchStatus) {
    if (isSwitchAvailable && switchStatus) {
      System.out.println("Lights are on!");
    } else if (isSwitchAvailable) {
      System.out.println("Lights are off!");
    } else {
      System.out.println("No switch! Look for another light source.");
    }
    separator();
  }

  public void switchFlashlight(boolean isFlashlightAvailable, boolean flashlightStatus) {
    if (isFlashlightAvailable && flashlightStatus) {
      System.out.println("Flashlight is on!");
    } else if (isFlashlightAvailable) {
      System.out.println("Flashlight is off!");
    } else {
      System.out.println("You don't have a flashlight!");
    }
    separator();
  }

  public void wrongObject(String operation) {
    System.out.println("you can't " + operation + " this.");
    separator();
  }

  public void successfulBuy(String itemName) {
    System.out.println(itemName + " bought and acquired.");
    separator();
  }

  public void unsuccessfulBuy() {
    System.out.println("return when you have enough gold");
    separator();
  }

  public void successfulSell(String itemName) {
    System.out.println(itemName + " sold.");
    separator();
  }

  public void wrongMove(String direction) {
    System.out.println("You can't move " + direction + ".");
    separator();
  }

  public void newRoom() {
    System.out.println("You entered a new room!");
    separator();
  }

  public void itemNotFound() {
    System.out.println("Item not found!");
    System.out.println("Use 'list' or 'inventory' to see available items.");
    separator();
  }

  public void objectNotFound() {
    System.out.println("Object not found!");
    System.out.println("Use 'look' to see available objects.");
    separator();
  }

  public void foundSpecialDoor(String playerName, int minutes, int seconds) {
    System.out.println("Congratulations!");
    System.out.println(
        "You have found your way out of the maze in "
            + minutes
            + " minutes and "
            + seconds
            + " seconds!");
    separator();
  }

  public void wrongTrade() {
    System.out.println("You are not facing a seller!");
    separator();
  }

  public void locationInfo(String location) {
    System.out.println("You are now viewing the " + location + ".");
  }

  public void finishInfo(String location) {
    System.out.println("Enter 'finish' to return to " + location + ".");
    System.out.println("Enter 'commands' to find out available commands.");
    System.out.println("Enter 'info' to get game information.");
    separator();
  }
}
