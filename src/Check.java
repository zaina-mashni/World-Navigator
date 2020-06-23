import javafx.util.Pair;
import java.util.Map;
import java.util.Objects;

public class Check implements IItemCommand {

  private Entity entity;

  public Check setCommand(Entity entity) {
    this.entity =
        Objects.requireNonNull(entity, "Entity can not be null when performing Check command.");
    return this;
  }

  @Override
  public Map<String, Pair<Item, Integer>> execute() {
    if (entity == null) {
      throw new IllegalStateException("Check command not set properly in Check command.");
    }
    Container container = (Container) entity.getFeature("container");
    return container.getInventory();
  }

  @Override
  public String getName() {
    return "check";
  }

  @Override
  public String getViewName() {
    return getName() + " _ ";
  }
}
