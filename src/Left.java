import java.util.Objects;

public class Left implements IOrientationCommand {

  private PlayerInfo playerInfo;

  public Left setCommand(PlayerInfo playerInfo) {
    this.playerInfo =
        Objects.requireNonNull(
            playerInfo, "PlayerInfo can not be null when performing left command.");
    return this;
  }

  @Override
  public void execute() {
    if (playerInfo == null) {
      throw new IllegalStateException("Left command not set properly in Left.execute.");
    }
    int tmpFacingDirection = (this.playerInfo.getFacingDirection() - 1) % 4;
    while (tmpFacingDirection < 0) {
      tmpFacingDirection += 4;
    }
    this.playerInfo.setFacingDirection(tmpFacingDirection);
  }

  @Override
  public String getName() {
    return "left";
  }

  @Override
  public String getViewName() {
    return getName();
  }
}
