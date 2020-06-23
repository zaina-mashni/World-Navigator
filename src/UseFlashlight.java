import java.util.Objects;

public class UseFlashlight implements ILightCommand {

  private Container inventory;

  public UseFlashlight setCommand(Container inventory) {
    this.inventory =
        Objects.requireNonNull(
            inventory, "inventory can not be null when performing useFlashlight command.");
    return this;
  }

  @Override
  public String execute() {
    if (inventory == null) {
      throw new IllegalStateException("useFlashlight command not set properly in UseFlashlight.execute");
    }
    if (!inventory.containsItem("flashlight")) return "noFlashlight";
    Flashlight flashlight = ((Flashlight) inventory.getItem("flashlight"));
    flashlight.flipItemStatus();
    if (flashlight.getItemStatus()) {
      return "flashlightOn";
    } else {
      return "flashlightOff";
    }
  }

  @Override
  public String getName() {
    return "useFlashlight";
  }

  @Override
  public String getViewName() {
    return getName();
  }
}
