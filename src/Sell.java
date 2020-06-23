import java.util.Objects;

public class Sell implements IActionCommand {

  private Seller seller;
  private Container inventory;
  private String itemName;

  public Sell setCommand(Seller seller, Container inventory, String itemName) {
    this.seller =
        Objects.requireNonNull(seller, "Seller can not be null when performing Sell command.");
    this.inventory =
        Objects.requireNonNull(
            inventory, "Inventory can not be null when performing Sell command.");
    this.itemName = itemName;
    return this;
  }

  @Override
  public boolean execute() {
    if (seller == null || inventory == null || itemName.isEmpty()) {
      throw new IllegalStateException("Sell command not set properly in Sell.execute.");
    }
    if (!inventory.containsItem("gold")) {
      throw new IllegalStateException("Inventory does not have gold in Sell.execute.");
    }
    int cost = inventory.getItemCost(itemName);
    seller.addItem(inventory.getItem(itemName), 1);
    inventory.replaceItem("gold", inventory.getAmount("gold") + cost);
    inventory.replaceItem(itemName, inventory.getAmount(itemName) - 1);
    return true;
  }

  @Override
  public String getName() {
    return "sell";
  }

  @Override
  public String getViewName() {
    return getName() + " _ ";
  }
}
