import java.util.Objects;

public class Trade implements IActionCommand {

  private Wall wall;

  public Trade setCommand(Wall wall) {
    this.wall = Objects.requireNonNull(wall, "wall can not be null when performing trade command.");
    ;
    return this;
  }

  @Override
  public boolean execute() {
    if (wall == null) {
      throw new IllegalStateException("Trade command not set properly Trade.execute.");
    }
    return wall.containsEntity("seller");
  }

  @Override
  public String getName() {
    return "trade";
  }

  @Override
  public String getViewName() {
    return getName();
  }
}
