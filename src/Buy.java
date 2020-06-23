import java.util.Objects;

public class Buy implements IActionCommand {

  private Seller seller;
  private Container inventory;
  private String itemName;

  public Buy setCommand(Seller seller, Container inventory, String itemName) {
    this.seller =
        Objects.requireNonNull(seller, "Seller can not be null when performing Buy command.");
    this.inventory =
        Objects.requireNonNull(inventory, "Inventory can not be null when performing Buy command.");
    this.itemName = itemName;
    return this;
  }

  @Override
  public boolean execute() {
    if (seller == null || inventory == null || itemName.isEmpty()) {
      throw new IllegalStateException("Buy command not set properly in Buy command.");
    }
    if (!inventory.containsItem("gold")) {
      throw new IllegalStateException("Inventory does not contain gold in Buy command.");
    }
    int cost = seller.getItemCost(itemName);
    if (inventory.getAmount("gold") >= cost) {
      inventory.addItem(seller.getItem(itemName), 1);
      seller
          .getSellerItems()
          .replaceItem(itemName, seller.getSellerItems().getAmount(itemName) - 1);
      inventory.replaceItem("gold", inventory.getAmount("gold") - cost);
      return true;
    }
    return false;
  }

  @Override
  public String getName() {
    return "buy";
  }

  @Override
  public String getViewName() {
    return getName() + " _ ";
  }
}
