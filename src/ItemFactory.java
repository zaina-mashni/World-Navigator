public class ItemFactory {

  private int cost;

  public void setCost(int cost) {
    this.cost = cost;
  }

  Item createItem(String itemName) {
    switch (itemName) {
      case "flashlight":
        return new Flashlight.Builder().cost(cost).build();
      case "gold":
        return new Gold.Builder().build();
      default:
        if (itemName.matches("[a-z]+Key")) {
          return new Key.Builder(itemName).cost(cost).build();
        }
        throw new IllegalArgumentException(itemName+" does not match any existing items in ItemFactory.createItem.");
    }
  }
}
