import java.util.Objects;

public class Backward implements IActionCommand {

  private PlayerInfo playerInfo;
  private MapInfo mapInfo;

  public Backward setCommand(PlayerInfo playerInfo, MapInfo mapInfo) {
    this.playerInfo =
        Objects.requireNonNull(
            playerInfo, "PlayerInfo can not be null when performing backward command.");
    this.mapInfo =
        Objects.requireNonNull(
            mapInfo, "MapInfo can not be null when performing backward command.");
    return this;
  }

  @Override
  public boolean execute() {
    if (playerInfo == null || mapInfo == null) {
      throw new IllegalStateException("Backward command not set properly in Backward command");
    }
    int facingIdx = (playerInfo.getFacingDirection() + 2) % 4;
    int roomIdx = playerInfo.getCurrentRoomIndex();
    ILockable lockableObject =
        (ILockable)
            mapInfo.getRoom(roomIdx).getWall(facingIdx).getEntity("door").getFeature("lock");
    if (lockableObject.getOpenStatus()) {
      playerInfo.setCurrentRoom(mapInfo.getAdjacentRoom(roomIdx, facingIdx));
      return true;
    }
    return false;
  }

  @Override
  public String getName() {
    return "backward";
  }

  @Override
  public String getViewName() {
    return getName();
  }
}
