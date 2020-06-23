import java.util.Objects;

public class Right implements IOrientationCommand {

  private PlayerInfo playerInfo;

  public Right setCommand(PlayerInfo playerInfo) {
    this.playerInfo =
        Objects.requireNonNull(
            playerInfo, "playerInfo can not be null when performing right command.");
    ;
    return this;
  }

  @Override
  public void execute() {
    if (playerInfo == null) {
      throw new IllegalStateException("Right command not set properly in Right.execute.");
    }
    int tmpFacingDirection = (playerInfo.getFacingDirection() + 1) % 4;
    playerInfo.setFacingDirection(tmpFacingDirection);
  }

  @Override
  public String getName() {
    return "right";
  }

  @Override
  public String getViewName() {
    return getName();
  }
}
