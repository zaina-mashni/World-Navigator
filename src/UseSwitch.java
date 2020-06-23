import java.util.Objects;

public class UseSwitch implements ILightCommand {

  private Room room;

  public UseSwitch setCommand(Room room) {
    this.room =
        Objects.requireNonNull(room, "room can not be null when performing useSwitch command.");
    return this;
  }

  @Override
  public String execute() {
    if (room == null) {
      throw new IllegalStateException("useSwitch command not set properly in UseSwitch.execute.");
    }
    String lightStatus = new Light().setCommand(room).execute();
    if (lightStatus.equals("darkWithSwitch")) {
      room.editFeature(new LitWithSwitch());
      return "litWithSwitch";
    } else if (lightStatus.equals("litWithSwitch")) {
      room.editFeature(new DarkWithSwitch());
      return "darkWithSwitch";
    }
    return "noSwitch";
  }

  @Override
  public String getName() {
    return "switchLights";
  }

  @Override
  public String getViewName() {
    return getName();
  }
}
