import java.util.Objects;

public class PlayerInfo implements IPlayerInfo {

  private String playerName;
  private GameTimer timer;
  private int timeInSeconds;
  private Container inventory;
  private int facingDirection;
  private Room currentRoom;

  public PlayerInfo(
      String playerName, int facingDirection, Container inventory, int time, Room currentRoom) {
    if (playerName.isEmpty()) {
      throw new IllegalArgumentException("playerName can not be empty in PlayerInfo.");
    }
    this.playerName = playerName;
    checkFacingDirection(facingDirection);
    this.facingDirection = facingDirection;
    this.inventory = Objects.requireNonNull(inventory, "inventory can not be null in PlayerInfo.");
    if (time <= 0) {
      throw new IllegalArgumentException("finish time "+ time+" can not be less than or equal to zero in PlayerInfo.");
    }
    this.timeInSeconds = time;
    this.currentRoom = Objects.requireNonNull(currentRoom, "currentRoom can not be null in PlayerInfo.");
  }

  public int getCurrentRoomIndex() {
    return currentRoom.getRoomIndex();
  }

  public Container getInventory() {
    return inventory;
  }

  public int getTimeInSeconds(){
    return timeInSeconds;
  }

  public void startTimer() {
    timer = new GameTimer();
    timer.schedule(timeInSeconds);
  }

  public int getRemainingTime() {
    return (int) timer.GetRemainingTime();
  }

  public Wall getFacingWall() {
    return currentRoom.getWall(facingDirection);
  }

  public void setFacingDirection(int facingDirection) {
    checkFacingDirection(facingDirection);
    this.facingDirection = facingDirection;
  }

  public void setCurrentRoom(Room currentRoom) {
    Objects.requireNonNull(currentRoom, "currentRoom can not be null when setting current room in PlayerInfo.setCurrentRoom.");
    this.currentRoom = currentRoom;
  }

  @Override
  public String getName() {
    return this.playerName;
  }

  @Override
  public int getFacingDirection() {
    return this.facingDirection;
  }

  @Override
  public Room getCurrentRoom() {
    return currentRoom;
  }

  private void checkFacingDirection(int facingDirection) {
    if (facingDirection < 0 || facingDirection > 4) {
      throw new IndexOutOfBoundsException(
          "facingDirection can not be less than 0 or greater than 4.");
    }
  }
}
