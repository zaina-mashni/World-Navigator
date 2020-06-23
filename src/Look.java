import java.util.List;
import java.util.Objects;

public class Look implements IEntityCommand {

  private Wall wall;

  public Look setCommand(Wall wall) {
    this.wall = Objects.requireNonNull(wall, "wall can not be null when performing Look command.");
    return this;
  }

  @Override
  public List<Entity> execute() {
    if (wall == null) {
      throw new IllegalStateException("Look command not set properly in Look.execute.");
    }
    return wall.getEntities();
  }

  @Override
  public String getName() {
    return "look";
  }

  @Override
  public String getViewName() {
    return getName();
  }
}
