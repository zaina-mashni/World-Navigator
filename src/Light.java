import java.util.Objects;

public class Light implements ILightCommand {
  private Room room;

  public Light setCommand(Room room) {
    this.room = Objects.requireNonNull(room, "room can not be null when performing light command.");
    return this;
  }

  @Override
  public String execute() {
    if (room == null) {
      throw new IllegalStateException("Light command not set properly in Light.execute.");
    }
    ILightable feature = (ILightable) room.getFeature("light");
    return feature.getFeatureType();
  }

  @Override
  public String getName() {
    return "light";
  }

  @Override
  public String getViewName() {
    return getName();
  }
}
