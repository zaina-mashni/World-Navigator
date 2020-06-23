import java.util.List;
import java.util.Objects;

public class WorldNavigatorModel {

  private IMapInfo mapInfo;
  private IPlayerInfo playerInfo;

  public WorldNavigatorModel(MapInfo mapInfo, PlayerInfo playerInfo) {
    this.mapInfo =
        Objects.requireNonNull(mapInfo, "mapInfo can not be null when setting WorldNavigatorModel");
    this.playerInfo =
        Objects.requireNonNull(
            playerInfo, "playerInfo can not be null when setting WorldNavigatorModel");
  }

  public String getPlayerName() {
    return this.playerInfo.getName();
  }

  public PlayerInfo getPlayerInfo() {
    return (PlayerInfo) playerInfo;
  }

  public MapInfo getMapInfo() {
    return (MapInfo) mapInfo;
  }

  public Entity getWallEntity(int wallIndex, String wallEntityName) {
    checkWallIndex(wallIndex);
    checkWallEntityName(wallEntityName);
    if (!hasEntity(wallEntityName)) {
      throw new IllegalArgumentException(wallEntityName+" does not exist in wall in WorldNavigatorModel.getWallEntity.");
    }
    return getCurrentRoom().getWall(wallIndex).getEntity(wallEntityName);
  }

  public List<Entity> getWallEntities(int wallIndex) {
    checkWallIndex(wallIndex);
    return getCurrentRoom().getWall(wallIndex).getEntities();
  }

  public Room getCurrentRoom() {
    return playerInfo.getCurrentRoom();
  }

  public int getFacingDirection() {
    return playerInfo.getFacingDirection();
  }

  public int getRoomIndex() {
    return getCurrentRoom().getRoomIndex();
  }

  public Wall getFacingWall() {
    return ((PlayerInfo) playerInfo).getFacingWall();
  }

  public List<Entity> getFacingWallEntities() {
    return getWallEntities(playerInfo.getFacingDirection());
  }

  public Entity getFacingEntity(String entityName) {
    checkWallEntityName(entityName);
    return getWallEntity(getFacingDirection(), entityName);
  }

  public Container getInventory() {
    return ((PlayerInfo) playerInfo).getInventory();
  }

  public Container getFacingEntityInventory(String entityName) {
    checkWallEntityName(entityName);
    Entity entity = getWallEntity(getFacingDirection(), entityName);
    if (entity.hasFeature("container")) {
      return (Container) entity.getFeature("container");
    }
    throw new IllegalArgumentException(entityName+" does not exist in wall WorldNavigatorModel.getFacingEntityInventory.");
  }

  public boolean isFacingEntity(String entityName) {
    checkWallEntityName(entityName);
    return getFacingWall().containsEntity(entityName);
  }

  public Wall getOppositeWall() {
    Room adjacentRoom = ((MapInfo) mapInfo).getAdjacentRoom(getRoomIndex(), getFacingDirection());
    return adjacentRoom.getWall((getFacingDirection() + 2) % 4);
  }

  public int getOppositeRoomIndex() {
    return ((MapInfo) mapInfo).getAdjacentRoom(getRoomIndex(), getFacingDirection()).getRoomIndex();
  }

  public Wall getAdjacentWall() {
    return getCurrentRoom().getWall((getFacingDirection() + 2) % 4);
  }

  public boolean hasOppositeDoor() {
    return getOppositeWall().containsEntity("door");
  }

  public Door getOppositeDoor() {
    if (!hasOppositeDoor()) {
      throw new IllegalStateException("Room does not have opposite door in WorldNavigatorModel.getOppositeDoor.");
    }
    return (Door) getOppositeWall().getEntity("door");
  }

  public boolean hasEntity(String entityName) {
    checkWallEntityName(entityName);
    return getFacingWall().containsEntity(entityName);
  }

  public void checkWallIndex(int wallIndex) {
    if (wallIndex < 0 || wallIndex > 4)
      throw new IndexOutOfBoundsException("wallIndex out of bound in WorldNavigatorModel.checkWallIndex.");
  }

  public void checkWallEntityName(String wallEntityName) {
    if (wallEntityName.isEmpty())
      throw new IllegalArgumentException("wallEntity can not be empty in checkWallEntityName.");
  }
}
