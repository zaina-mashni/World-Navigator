import java.util.Objects;

public class Seller extends Entity {

  public Seller(Builder builder) {
    super(builder);
  }

  public Container getSellerItems() {
    return ((Container) features.get("container"));
  }

  public boolean hasItem(String itemName) {
    return ((Container) features.get("container")).containsItem(itemName);
  }

  public int getItemCost(String itemName) {
    checkItemName(itemName);
    return ((Container) features.get("container")).getItemCost(itemName);
  }

  public Item getItem(String itemName) {
    checkItemName(itemName);
    return ((Container) features.get("container")).getItem(itemName);
  }

  public void addItem(Item item, int amount) {
    Objects.requireNonNull(item, "Item added to seller can not be null in Seller.addItem.");
    if (amount <= 0) {
      throw new IllegalArgumentException("item amount "+amount+" can not be less than or equal to 0 in Seller.addItem.");
    }
    ((Container) features.get("container")).addItem(item, amount);
  }

  public static class Builder extends Entity.Builder<Seller.Builder> {

    public Builder() {
      super("seller");
    }

    @Override
    public Seller build() {
      return new Seller(this);
    }

    @Override
    protected Builder self() {
      return this;
    }
  }

  private void checkItemName(String itemName) {
    if (!hasItem(itemName)) {
      throw new IllegalArgumentException(itemName+" does not exist in seller inventory in Seller.checkItemName.");
    }
  }
}
