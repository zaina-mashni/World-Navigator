import java.util.Objects;

public class Forward implements IActionCommand {

  private PlayerInfo playerInfo;
  private MapInfo mapInfo;

  public Forward setCommand(PlayerInfo playerInfo, MapInfo mapInfo) {
    this.playerInfo =
        Objects.requireNonNull(
            playerInfo, "PlayerInfo can not be null when performing forward command.");
    this.mapInfo =
        Objects.requireNonNull(mapInfo, "MapInfo can not be null when performing forward command.");
    return this;
  }

  @Override
  public boolean execute() {
    if (playerInfo == null || mapInfo == null) {
      throw new IllegalStateException("Forward command not set properly in Forward.execute.");
    }
    int facingIdx = playerInfo.getFacingDirection();
    int roomIdx = playerInfo.getCurrentRoomIndex();
    ILockable lockableObject =
        (ILockable) playerInfo.getFacingWall().getEntity("door").getFeature("lock");
    if (lockableObject.getOpenStatus()) {
      playerInfo.setCurrentRoom(mapInfo.getAdjacentRoom(roomIdx, facingIdx));
      return true;
    }
    return false;
  }

  @Override
  public String getName() {
    return "forward";
  }

  @Override
  public String getViewName() {
    return getName();
  }
}
