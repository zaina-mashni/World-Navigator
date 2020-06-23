import javafx.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Container implements IContainerizable {
  private Map<String, Pair<Item, Integer>> items;

  public Container() {
    items = new HashMap<>();
  }

  public Map<String, Pair<Item, Integer>> getInventory() {
    return items;
  }

  public List<Pair<Item, Integer>> getItems() {
    List<Pair<Item, Integer>> tmpItems = new ArrayList<>();
    for (Map.Entry<String, Pair<Item, Integer>> item : items.entrySet()) {
      tmpItems.add(new Pair<>(item.getValue().getKey(), item.getValue().getValue()));
    }
    return tmpItems;
  }

  public int getAmount(String itemName) {
    checkItemName(itemName);
    checkItemAvailability(itemName);
    int itemAmount = 0;
    for (Map.Entry<String, Pair<Item, Integer>> item : items.entrySet()) {
      if (item.getKey().equals(itemName)) {
        itemAmount = item.getValue().getValue();
      }
    }
    return itemAmount;
  }

  public boolean containsItem(String itemName) {
    checkItemName(itemName);
    return items.containsKey(itemName);
  }

  public Item getItem(String itemName) {
    checkItemName(itemName);
    checkItemAvailability(itemName);
    return items.get(itemName).getKey();
  }

  public void replaceItem(String itemName, int amount) {
    checkItemName(itemName);
    checkItemAvailability(itemName);
    items.replace(itemName, new Pair<>(getItem(itemName), amount));
    if (getAmount(itemName) <= 0 && !itemName.equals("gold")) {
      items.remove(itemName);
    } else if (getAmount(itemName) < 0 && itemName.equals("gold")) {
      throw new IndexOutOfBoundsException(
          "Gold amount should never be less than zero in Container.replaceItem");
    }
  }

  public void removeAll() {
    items.clear();
  }

  public int getItemCost(String itemName) {
    checkItemName(itemName);
    checkItemAvailability(itemName);
    if (itemName.equals("gold")) {
      throw new IllegalArgumentException("You cant getItemCost for gold in Container.getItemCost.");
    }
    return getItem(itemName).getCost();
  }

  public String getItemName(int index) {
    if (index < 0 || index >= items.size()) {
      throw new IndexOutOfBoundsException();
    }
    return getItems().get(index).getKey().getName();
  }

  public int getSize() {
    return items.size();
  }

  public void addItems(List<Pair<Item, Integer>> items) {
    for (Pair<Item, Integer> item : items) {
      addItem(item.getKey(), item.getValue());
    }
  }

  @Override
  public void addItem(Item item, int amount) {
    Objects.requireNonNull(
        item, "Item can not be null when adding to container in Container.addItem.");
    if (!item.getName().equals("gold")) {
      checkItemAmount(amount);
    } else {
      if (amount < 0) {
        throw new IllegalArgumentException(
            "gold amount "
                + amount
                + " can not be a negative integer when adding to container in Container.addItem");
      }
    }
    if (!items.containsKey(item.getName())) items.put(item.getName(), new Pair<>(item, amount));
    else items.replace(item.getName(), new Pair<>(item, getAmount(item.getName()) + amount));
  }

  @Override
  public String getName() {
    return this.NAME;
  }

  @Override
  public String getFeatureType() {
    return "container";
  }

  private void checkItemName(String itemName) {
    if (itemName.isEmpty()) {
      throw new IllegalArgumentException(
          itemName + " can not be empty when getting amount from container");
    }
  }

  private void checkItemAvailability(String itemName) {
    if (!items.containsKey(itemName))
      throw new IllegalArgumentException(
          itemName
              + " does not exist in container in Container.checkIemAvailability in Container.checkItemAvailability");
  }

  private void checkItemAmount(int amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException(
          amount
              + " has to be a non negative integer when adding to container in Container.checkItemAmount.");
    }
  }
}
