import java.util.Objects;

public class Take implements IActionCommand {

  private Container inventory;
  private Container objectItems;

  public Take setCommand(Container inventory, Container objectItems) {
    this.inventory =
        Objects.requireNonNull(
            inventory, "inventory can not be null when performing take command.");
    this.objectItems =
        Objects.requireNonNull(
            objectItems, "objectItems can not be null when performing open command.");
    return this;
  }

  @Override
  public boolean execute() {
    if (inventory == null || objectItems == null) {
      throw new IllegalStateException("Take command not set properly in Take.execute.");
    }
    if (objectItems.getSize() == 0) return false;
    inventory.addItems(objectItems.getItems());
    objectItems.removeAll();
    return true;
  }

  @Override
  public String getName() {
    return "take";
  }

  @Override
  public String getViewName() {
    return getName();
  }
}
