import javafx.util.Pair;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.DirectoryIteratorException;
import java.util.List;
import java.util.Map;

public class WorldNavigatorController {

  private static WorldNavigatorController controllerInstance = null;
  private WorldNavigatorModel worldModel;
  private WorldNavigatorView worldView;
  private File mapFile;
  private String playerName;
  private CommandControl commandControl;

  private WorldNavigatorController() {
    worldView = new WorldNavigatorView();
    commandControl = new CommandControl();
  }

  public static WorldNavigatorController getInstance() {
    if (controllerInstance == null) {
      controllerInstance = new WorldNavigatorController();
    }
    return controllerInstance;
  }

  private File[] getMapFiles(String dir) {
    File[] files = null;
    try {
      File mapsDirectory = new File(dir);
      files = mapsDirectory.listFiles();
    } catch (DirectoryIteratorException e) {
      worldView.errorNoDirectory(dir);
      System.exit(1);
    }
    return files;
  }

  private File chooseMap(File[] maps) {
    return worldView.mapChooser(maps);
  }

  public void runGame(String mapsDirectory) throws FileNotFoundException {
    playerName = worldView.welcome();
    File[] maps = getMapFiles(mapsDirectory);
    if (maps.length == 0) {
      worldView.mapLister(maps);
      return;
    }
    mapFile = chooseMap(maps);
    setUpGame();
  }

  public void setUpGame() throws FileNotFoundException {
    try {
      worldModel = new WorldBuilder(mapFile, playerName).decodeFile().buildWorld();
    } catch (Exception e) {
      worldView.errorInExecution(e.getMessage());
      System.exit(1);
    }
    worldView.afterSetUp(worldModel.getPlayerName());
    startGame();
  }

  public void startGame() throws FileNotFoundException {
    try {
      worldModel.getPlayerInfo().startTimer();
      infoCommand();
    } catch (Exception e) {
      worldView.errorInExecution(e.getMessage());
      System.exit(1);
    }
    decodeRoomCommands();
  }

  public void decodeRoomCommands() throws FileNotFoundException {
    commandControl.loadRoomCommands();
    worldView.locationInfo("room");
    try {
      String command = getCommand();
      while (!command.equals("quit")) {
        if (command.matches("commands(\\s.*|\\s*)")) {
          listCommands();
        } else if (command.matches("info(\\s.*|\\s*)")) {
          infoCommand();
        } else if (command.matches("look(\\s.*|\\s*)")) {
          lookCommand();
        } else if (command.matches("backward(\\s.*|\\s*)")) {
          moveCommand("backward");
        } else if (command.matches("forward(\\s.*|\\s*)")) {
          moveCommand("forward");
        } else if (command.matches("left(\\s.*|\\s*)")) {
          changeOrientationCommand("left");
        } else if (command.matches("right(\\s.*|\\s*)")) {
          changeOrientationCommand("right");
        } else if (command.matches("useFlashlight(\\s.*|\\s*)")) {
          useFlashlightCommand();
        } else if (command.matches("switchLights(\\s.*|\\s*)")) {
          switchLightsCommand();
        } else if (command.matches("inventory(\\s.*|\\s*)")) {
          listInventory();
        } else if (command.matches("restart(\\s.*|\\s*)")) {
          setUpGame();
        } else worldView.wrongInput();
        command = getCommand();
      }
    } catch (Exception e) {
      worldView.errorInExecution(e.getMessage());
      System.exit(1);
    }
    String[] args = new String[] {};
    StartGame.main(args);
  }

  public void infoCommand() {
    int secondsRemaining = worldModel.getPlayerInfo().getRemainingTime();
    worldView.gameInfo(
        secondsRemaining % 60,
        secondsRemaining / 60,
        worldModel.getPlayerInfo().getFacingDirection());
  }

  public boolean isNumberInput(String input) {
    try {
      Integer.parseInt(input);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public String getSecondaryObject(List<Entity> entities, String command) {
    if (isNumberInput(command)) {
      int objectIndex = Integer.parseInt(command);
      if (objectIndex >= 1 && objectIndex <= entities.size()) {
        return entities.get(objectIndex - 1).getName();
      }
    }
    return command;
  }

  public String getSecondaryItem(Container items, String command) {
    if (isNumberInput(command)) {
      int itemIndex = Integer.parseInt(command);
      if (itemIndex >= 1 && itemIndex <= items.getSize()) {
        return items.getItemName(itemIndex - 1);
      }
    }
    return command;
  }

  private void takeCommand(String objectName) {
    Container inventory = worldModel.getInventory();
    Container objectItems = worldModel.getFacingEntityInventory(objectName);
    if (!worldModel.hasEntity(objectName)) {
      worldView.objectNotFound();
      return;
    }
    try {
      commandControl.setCommand(new Take().setCommand(inventory, objectItems));
      if (commandControl.executeActionCommands()) {
        worldView.takeItems();
      } else {
        worldView.takeCommandFailed(objectName);
      }
    } catch (Exception e) {
      worldView.errorInExecution(e.getMessage());
      System.exit(1);
    }
  }

  private void decodeObjectCommands(String objectName) {
    commandControl.loadObjectCommands();
    worldView.locationInfo("object");
    worldView.finishInfo("wall");
    try {
      String command = getCommand();
      while (!command.equals("finish")) {
        switch (command) {
          case "commands":
            listCommands();
            break;
          case "take":
            takeCommand(objectName);
            break;
          case "inventory":
            listInventory();
            break;
          case "info":
            infoCommand();
            break;
          default:
            worldView.wrongInput();
        }
        command = getCommand();
      }
    } catch (Exception e) {
      worldView.errorInExecution(e.getMessage());
      System.exit(1);
    }
  }

  private boolean checkContainerCommand(String objectName) {
    Map<String, Pair<Item, Integer>> items = null;
    try {
      commandControl.setCommand(new Check().setCommand(worldModel.getFacingEntity(objectName)));
      items = commandControl.executeItemCommands();
    } catch (Exception e) {
      worldView.errorInExecution(e.getMessage());
      System.exit(1);
    }
    worldView.listItems(items);
    return items.isEmpty();
  }

  private boolean openStatus(String objectName) {
    if (!worldModel.isFacingEntity(objectName)) {
      worldView.objectNotFound();
      return false;
    }
    Entity entity = worldModel.getFacingEntity(objectName);
    if (!entity.hasFeature("lock")) return true;
    ILockable lock = ((ILockable) entity.getFeature("lock"));
    if (!lock.getOpenStatus()) {
      if (lock.isLocked()) worldView.closedObjectWithKey(objectName, lock.getKeyName());
      else worldView.closedObject(objectName);
      return false;
    }
    worldView.openObject(objectName);
    return true;
  }

  private void checkCommand(String entityName) {
    if (worldModel.isFacingEntity(entityName)) {
      if (openStatus(entityName)) {
        if (!entityName.equals("door")) {
          try {
            if (!checkContainerCommand(entityName) && !entityName.equals("seller")) {
              decodeObjectCommands(entityName);
              commandControl.loadWallCommands();
              worldView.locationInfo("wall");
            }
          } catch (Exception e) {
            worldView.errorInExecution(e.getMessage());
            System.exit(1);
          }
        }
      }
    } else {
      worldView.objectNotFound();
    }
  }

  public void foundSpecialRoom() {
    int minute = (worldModel.getPlayerInfo().getTimeInSeconds() - worldModel.getPlayerInfo().getRemainingTime()) / 60;
    int second = (60 - (worldModel.getPlayerInfo().getRemainingTime() % 60)) % 60;
    worldView.foundSpecialDoor(worldModel.getPlayerName(),minute, second);
    System.exit(0);
  }

  private void openCommand(String entityName) {
    if (entityName.equals("chest") || entityName.equals("door")) {
      try {
        Entity entity = worldModel.getFacingEntity(entityName);
        commandControl.setCommand(new Open().setCommand(entity));
        if (commandControl.executeActionCommands() && entityName.equals("door")) {
          worldView.openObject(entityName);
          if (!worldModel.hasOppositeDoor()) {
            if (worldModel.getOppositeRoomIndex()
                == worldModel.getMapInfo().getSpecialRoomIndex()) {
              foundSpecialRoom();
            } else {
              throw new InputFileException("Error in input file.");
            }
          } else {
            commandControl.setCommand(new Open().setCommand(worldModel.getOppositeDoor()));
            commandControl.executeActionCommands();
          }
        } else if (commandControl.executeActionCommands()) {
          worldView.openObject(entityName);
        } else {
          ILockable lockableObject = ((ILockable) entity.getFeature("lock"));
          worldView.closedObjectWithKey(entityName, lockableObject.getKeyName());
        }
      } catch (Exception e) {
        worldView.errorInExecution(e.getMessage());
        System.exit(1);
      }

    } else {
      worldView.wrongObject("open");
    }
  }

  public String getSubObject(String command, List<Entity> entities) {
    String subCommand = "";
    if (command.indexOf(' ') != -1) {
      subCommand = command.substring(command.indexOf(' '));
    }
    subCommand = subCommand.trim();
    subCommand = getSecondaryObject(entities, subCommand);
    return subCommand;
  }

  public String getSubItem(String command) {
    Container items;
    if (command.substring(0, command.indexOf(' ')).equals("buy")) {
      items = worldModel.getFacingEntityInventory("seller");
    } else {
      items = worldModel.getInventory();
    }

    String subCommand = "";
    if (command.indexOf(' ') != -1) {
      subCommand = command.substring(command.indexOf(' '));
    }
    subCommand = subCommand.trim();
    subCommand = getSecondaryItem(items, subCommand);
    return subCommand;
  }

  public void decodeWallCommands() {
    commandControl.loadWallCommands();
    worldView.locationInfo("wall");
    worldView.finishInfo("room");
    try {
      String command = getCommand();
      String subCommand = "";
      while (!command.equals("finish")) {
        if (command.matches("commands(\\s*|\\s*)")) {
          listCommands();
        } else if (command.matches("info(\\s.*|\\s*)")) {
          infoCommand();
        } else if (command.matches("check\\s\\w+")) {
          subCommand = getSubObject(command, worldModel.getFacingWallEntities());
          checkCommand(subCommand);
        } else if (command.matches("open\\s\\w+")) {
          subCommand = getSubObject(command, worldModel.getFacingWallEntities());
          openCommand(subCommand);
        } else if (command.matches("useKey\\s\\w+")) {
          subCommand = getSubObject(command, worldModel.getFacingWallEntities());
          useKeyCommand(subCommand);
        } else if (command.matches("inventory\\s*")) listInventory();
        else if (command.matches("look\\s*")) {
          lookCommand();
        } else if (command.matches("trade(\\s.*|\\s*)")) {
          tradeCommand();
        } else worldView.wrongInput();
        command = getCommand();
      }
    } catch (Exception e) {
      worldView.errorInExecution(e.getMessage());
      System.exit(1);
    }
  }

  private void useKeyCommand(String entityName) {
    if (entityName.equals("chest") || entityName.equals("door")) {
      try {
        Entity entity = worldModel.getFacingEntity(entityName);
        Container inventory = worldModel.getInventory();
        commandControl.setCommand(new UseKey().setCommand(entity, inventory));
        if (commandControl.executeActionCommands()) {
          if (worldModel.hasOppositeDoor()) {
            Door oppositeDoor = worldModel.getOppositeDoor();
            commandControl.setCommand(new UseKey().setCommand(oppositeDoor, inventory));
            if (!commandControl.executeActionCommands()) {
              throw new InputFileException("Error in input file.");
            }
            worldView.unlockedObject(entityName);
          } else if (entityName.equals("chest")) {
            worldView.unlockedObject(entityName);
          } else {
            worldView.unlockedObject(entityName);
          }
        } else {
          ILockable lockableObject = (ILockable) worldModel.getOppositeDoor().getFeature("lock");
          worldView.closedObjectWithKey(entityName, lockableObject.getKeyName());
        }
      } catch (Exception e) {
        worldView.errorInExecution(e.getMessage());
        System.exit(1);
      }
    } else {
      worldView.wrongObject("use a key on");
    }
  }

  public void listCommands() {
    try {
      worldView.listAvailableCommands(commandControl.getAvailableCommands());
    } catch (Exception e) {
      worldView.errorInExecution(e.getMessage());
      System.exit(1);
    }
  }

  public void buyCommand(String itemName) {
    if (!worldModel.isFacingEntity("seller")) {
      throw new IllegalArgumentException("Buy command is called but player not facing seller.");
    }
    Seller seller = (Seller) worldModel.getFacingEntity("seller");
    Container inventory = worldModel.getInventory();
    if (!seller.hasItem(itemName)) {
      worldView.itemNotFound();
    } else {
      try {
        commandControl.setCommand(new Buy().setCommand(seller, inventory, itemName));
        if (commandControl.executeActionCommands()) {
          worldView.successfulBuy(itemName);
        } else {
          worldView.unsuccessfulBuy();
        }
      } catch (Exception e) {
        worldView.errorInExecution(e.getMessage());
        System.exit(1);
      }
    }
  }

  public void sellCommand(String itemName) {
    if (!worldModel.isFacingEntity("seller")) {
      throw new IllegalArgumentException("Sell command is called but player not facing seller.");
    }
    Seller seller = (Seller) worldModel.getFacingEntity("seller");
    Container inventory = worldModel.getInventory();
    if (!inventory.containsItem(itemName)) {
      worldView.itemNotFound();
    } else {
      try {
        commandControl.setCommand(new Sell().setCommand(seller, inventory, itemName));
        if (itemName.equals("gold")) {
          worldView.wrongObject("sell");
          return;
        }
        if (commandControl.executeActionCommands()) {
          worldView.successfulSell(itemName);
        }
      } catch (Exception e) {
        worldView.errorInExecution(e.getMessage());
        System.exit(1);
      }
    }
  }

  public void tradeCommand() {
    Wall wall = worldModel.getFacingWall();
    try {
      commandControl.setCommand(new Trade().setCommand(wall));
      if (commandControl.executeActionCommands()) {
        decodeTradeCommands();
        commandControl.loadWallCommands();
        worldView.locationInfo("wall");
      } else {
        worldView.wrongTrade();
      }
    } catch (Exception e) {
      worldView.errorInExecution(e.getMessage());
      System.exit(1);
    }
  }

  public void listSellerItems() {
    if (!worldModel.isFacingEntity("seller")) {
      throw new IllegalArgumentException(
          "ListSellerItems command is called but player not facing seller.");
    }
    Seller seller = (Seller) worldModel.getFacingEntity("seller");
    try {
      commandControl.setCommand(new ListSellerItems().setCommand(seller));
      worldView.listInventoryItems(commandControl.executeItemCommands());
    } catch (Exception e) {
      worldView.errorInExecution(e.getMessage());
      System.exit(1);
    }
  }

  public void decodeTradeCommands() {
    commandControl.loadTradeCommands();
    worldView.locationInfo("seller");
    worldView.finishInfo("wall");
    listSellerItems();
    try {
      String command = getCommand();
      String subCommand = "";
      while (!command.equals("finish")) {
        if (command.matches("commands(\\s*|\\s*)")) {
          listCommands();
        } else if (command.matches("info(\\s.*|\\s*)")) {
          infoCommand();
        } else if (command.matches("buy\\s\\w+")) {
          subCommand = getSubItem(command);
          buyCommand(subCommand);
        } else if (command.matches("sell\\s\\w+")) {
          subCommand = getSubItem(command);
          sellCommand(subCommand);
        } else if (command.matches("list(\\s*|\\s*)")) {
          listSellerItems();
        } else if (command.matches("inventory(\\s*|\\s*)")) {
          listInventory();
        } else if (command.matches("info(\\s*|\\s*)")) {
          infoCommand();
        } else {
          worldView.wrongInput();
        }
        command = getCommand();
      }
    } catch (Exception e) {
      worldView.errorInExecution(e.getMessage());
      System.exit(1);
    }
  }

  private void listInventory() {
    PlayerInfo playerInfo = worldModel.getPlayerInfo();
    try {
      commandControl.setCommand(new Inventory().setCommand(playerInfo));
      worldView.listInventoryItems(commandControl.executeItemCommands());
    } catch (Exception e) {
      worldView.errorInExecution(e.getMessage());
      System.exit(1);
    }
  }

  private void lookCommand() {
    Room room = worldModel.getCurrentRoom();
    Container inventory = worldModel.getInventory();
    try {
      commandControl.setCommand(new Light().setCommand(room));
      boolean isDark = commandControl.executeLightCommands().matches("dark.*");
      boolean hasFlashlight = inventory.containsItem("flashlight");
      boolean flashlightStatus = false;
      if (hasFlashlight) {
        flashlightStatus = ((Flashlight) inventory.getItem("flashlight")).getItemStatus();
      }
      if (isDark && !flashlightStatus) {
        worldView.darkRoomInfo();
      } else {
        Wall wall = worldModel.getFacingWall();
        commandControl.setCommand(new Look().setCommand(wall));
        worldView.listObjects(commandControl.executeEntityCommands());
        decodeWallCommands();
        commandControl.loadRoomCommands();
        worldView.locationInfo("room");
      }
    } catch (Exception e) {
      worldView.errorInExecution(e.getMessage());
      System.exit(1);
    }
  }

  private void moveCommand(String direction) {
    boolean isMoveAllowed;
    PlayerInfo playerInfo = worldModel.getPlayerInfo();
    MapInfo mapInfo = worldModel.getMapInfo();
    ILockable lockableObject;
    try {
      if (direction.equals("forward")) {
        if (!worldModel.hasEntity("door")) {
          worldView.wrongMove(direction);
          return;
        }
        lockableObject = (ILockable) worldModel.getFacingEntity("door").getFeature("lock");
        commandControl.setCommand(new Forward().setCommand(playerInfo, mapInfo));
      } else {
        if (!worldModel.getAdjacentWall().containsEntity("door")) {
          worldView.wrongMove(direction);
          return;
        }
        lockableObject =
            (ILockable) worldModel.getAdjacentWall().getEntity("door").getFeature("lock");
        commandControl.setCommand(new Backward().setCommand(playerInfo, mapInfo));
      }
      isMoveAllowed = commandControl.executeActionCommands();
      if (isMoveAllowed) worldView.newRoom();
      else {
        if (lockableObject.isLocked()) {
          worldView.closedObjectWithKey("door", lockableObject.getKeyName());
        } else worldView.closedObject("door");
      }
    } catch (Exception e) {
      worldView.errorInExecution(e.getMessage());
      System.exit(1);
    }
  }

  private void changeOrientationCommand(String direction) {
    PlayerInfo playerInfo = worldModel.getPlayerInfo();
    try {
      if (direction.equals("left")) {
        commandControl.setCommand(new Left().setCommand(playerInfo));
        commandControl.executeOrientationCommands();
      } else if (direction.equals("right")) {
        commandControl.setCommand(new Right().setCommand(playerInfo));
        commandControl.executeOrientationCommands();
      } else {
        throw new IllegalArgumentException("wrong direction");
      }
    } catch (Exception e) {
      worldView.errorInExecution(e.getMessage());
      System.exit(1);
    }
    int facingDirection = worldModel.getFacingDirection();
    worldView.directionChange(facingDirection);
  }

  private void useFlashlightCommand() {
    Container inventory = worldModel.getInventory();
    try {
      commandControl.setCommand(new UseFlashlight().setCommand(inventory));
      String flashLightStatus = commandControl.executeLightCommands();
      boolean flashLightAvailable = flashLightStatus.matches("flashlight.*");
      boolean flashlightOn = flashLightStatus.matches(".*On");
      worldView.switchFlashlight(flashLightAvailable, flashlightOn);
    } catch (Exception e) {
      worldView.errorInExecution(e.getMessage());
      System.exit(1);
    }
  }

  public void switchLightsCommand() {
    Room room = worldModel.getCurrentRoom();
    try {
      commandControl.setCommand(new UseSwitch().setCommand(room));
      String switchLightStatus = commandControl.executeLightCommands();
      boolean switchAvailable = switchLightStatus.matches(".*WithSwitch");
      boolean switchOn = switchLightStatus.matches("lit.*");
      worldView.switchLight(switchAvailable, switchOn);
    } catch (Exception e) {
      worldView.errorInExecution(e.getMessage());
      System.exit(1);
    }
  }

  public String getCommand() {
    String command = worldView.getInput();
    String subCommand = command;
    if (command.indexOf(' ') != -1) {
      subCommand = command.substring(0, command.indexOf(' '));
      command = command.substring(command.indexOf(' '));
    } else {
      command = "";
    }
    if (isNumberInput(subCommand)) {
      int commandIdx = Integer.parseInt(subCommand);
      if (commandIdx >= 1 && commandIdx <= commandControl.getAvailableCommands().size()) {
        return commandControl.getCommandName(Integer.parseInt(subCommand)) + command;
      }
    }

    return subCommand + command;
  }
}
