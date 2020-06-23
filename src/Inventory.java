import javafx.util.Pair;
import java.util.Map;
import java.util.Objects;

public class Inventory implements IItemCommand {

  private PlayerInfo playerInfo;

  public Inventory setCommand(PlayerInfo playerInfo) {
    this.playerInfo =
        Objects.requireNonNull(
            playerInfo, "PlayerInfo can not be null when performing inventory command.");
    return this;
  }

  @Override
  public Map<String, Pair<Item, Integer>> execute() {
    if (playerInfo == null) {
      throw new IllegalStateException("Inventory command not set properly in Inventory.execute.");
    }
    return playerInfo.getInventory().getInventory();
  }

  @Override
  public String getName() {
    return "inventory";
  }

  @Override
  public String getViewName() {
    return getName();
  }
}
