import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class WorldBuilder {
  private MapInfo mapInfo;
  private String playerName;
  private PlayerInfo playerInfo;
  private Scanner scanner;

  public WorldBuilder(File mapFile, String playerName) throws FileNotFoundException {
    this.playerName = playerName;
    scanner = new Scanner(mapFile);
  }

  public WorldBuilder decodeFile() throws InputFileException {
    int timeInMinutes = scanner.nextInt();
    int timeInSeconds = timeInMinutes * 60;
    int noOfRooms = scanner.nextInt();
    Container inventory = readInventory();
    ArrayList<Room> rooms = readRooms(noOfRooms);
    List<ArrayList<Room>> mapGraph = readMapGraph(noOfRooms, rooms);
    int facingDirection = scanner.nextInt();
    int currentRoomIndex = scanner.nextInt();
    Room currentRoom = rooms.get(currentRoomIndex);
    setUpPlayerInfo(playerName, facingDirection, inventory, timeInSeconds, currentRoom);
    setUpMapInfo(mapGraph, rooms);
    return this;
  }

  private Container readInventory() throws InputFileException {
    int inventorySize = scanner.nextInt();
    return readContainer(inventorySize);
  }

  private Container readContainer(int noOfItems) throws InputFileException {
    if (noOfItems < 0) {
      throw new InputFileException(
          "Error in map file: number of items in inventory can not be less than 0 in WorldBuilder.readContainer.");
    }
    Container tmpContainer = new Container();
    while (noOfItems-- > 0) {
      String itemName = scanner.next();
      int itemCost = scanner.nextInt();
      int itemAmount = scanner.nextInt();
      ItemFactory factory = new ItemFactory();
      factory.setCost(itemCost);
      Item item = factory.createItem(itemName);
      tmpContainer.addItem(item, itemAmount);
    }
    return tmpContainer;
  }

  private ArrayList<Room> readRooms(int noOfRooms) throws InputFileException {
    if (noOfRooms <= 0) {
      throw new InputFileException(
          "Error in map file: number of rooms in map less than or equal to 0 in WorldBuilder.readRooms.");
    }
    ArrayList<Room> tmpRooms = new ArrayList<>(noOfRooms);
    for (int i = 0; i < noOfRooms; ++i) {
      int noOfFeatures = scanner.nextInt();
      Map<String, IFeature> features = new HashMap<>();
      while (noOfFeatures-- > 0) {
        String featureName = scanner.next();
        features.put(readFeatures(featureName).getName(), readFeatures(featureName));
      }
      Room room = new Room();
      room.addFeatures(features);
      room.setRoomIndex(i);
      for (int j = 0; j < 4; ++j) {
        Wall wall = new Wall();
        int noOfObjects = scanner.nextInt();
        wall.addEntities(readEntities(noOfObjects));
        room.setWall(wall, j);
      }
      tmpRooms.add(room);
    }
    Room specialRoom = new Room();
    specialRoom.setRoomIndex(noOfRooms);
    tmpRooms.add(specialRoom);
    return tmpRooms;
  }

  private IFeature readFeatures(String feature) throws InputFileException {
    switch (feature) {
      case "lockedWithKey":
        String lockItemName = scanner.next();
        int cost = scanner.nextInt();
        return new LockedWithKey(new Key.Builder(lockItemName).cost(cost).build());
      case "unlocked":
        return new Unlocked();
      case "container":
        int noOfItems = scanner.nextInt();
        return readContainer(noOfItems);
      case "litWithSwitch":
        return new LitWithSwitch();
      case "darkWithSwitch":
        return new DarkWithSwitch();
      case "darkWithNoSwitch":
        return new DarkWithNoSwitch();
      case "trader":
        return new Trader();
      default:
        throw new InputFileException(
            "Error in map file: Entity feature does not exist in WorldBuilder.readFeatures.");
    }
  }

  private ArrayList<Entity> readEntities(int noOfEntity) throws InputFileException {
    if (noOfEntity < 0) {
      throw new InputFileException(
          "Error in map file: number of entities in a wall can not be less than 0 in WorldBuilder.readEntities.");
    }
    ArrayList<Entity> tmpEntities = new ArrayList<>();
    while (noOfEntity-- > 0) {
      String objectName = scanner.next();
      EntityFactory factory = new EntityFactory();
      int noOfObjectFeatures = scanner.nextInt();
      while (noOfObjectFeatures-- > 0) {
        String feature = scanner.next();
        factory.addFeature(readFeatures(feature));
      }
      tmpEntities.add(factory.createEntity(objectName));
    }
    return tmpEntities;
  }

  private List<ArrayList<Room>> readMapGraph(int noOfRooms, ArrayList<Room> rooms) {
    List<ArrayList<Room>> tmpMapGraph = new ArrayList<>(noOfRooms);
    for (int i = 0; i < noOfRooms; ++i) {
      tmpMapGraph.add(
          new ArrayList<>(Arrays.asList(new NoRoom(), new NoRoom(), new NoRoom(), new NoRoom())));
      int noConnectedRooms = scanner.nextInt();
      for (int j = 0; j < noConnectedRooms; ++j) {
        int roomIndex = scanner.nextInt();
        int roomDirection = scanner.nextInt();
        tmpMapGraph.get(i).set(roomDirection, rooms.get(roomIndex));
      }
    }
    return tmpMapGraph;
  }

  private void setUpPlayerInfo(
      String name, int facingDirection, Container inventory, int time, Room currentRoom) {
    this.playerInfo = new PlayerInfo(name, facingDirection, inventory, time, currentRoom);
  }

  private void setUpMapInfo(List<ArrayList<Room>> mapGraph, ArrayList<Room> rooms) {
    this.mapInfo = new MapInfo(mapGraph, rooms);
  }

  public WorldNavigatorModel buildWorld() {
    if (mapInfo == null || playerInfo == null) {
      throw new IllegalStateException(
          "map file was not decoded before attempt to build world in WorldBuilder.buildWorld.");
    }
    return new WorldNavigatorModel(mapInfo, playerInfo);
  }
}
