import javafx.util.Pair;

import java.util.Objects;

public class UseKey implements IActionCommand {

  private Entity entity;
  private Container inventory;

  public UseKey setCommand(Entity entity, Container inventory) {
    this.entity =
        Objects.requireNonNull(entity, "entity can not be null when performing useKey command.");
    this.inventory =
        Objects.requireNonNull(
            inventory, "inventory can not be null when performing useKey command.");
    return this;
  }

  @Override
  public boolean execute() {
    if (entity == null || inventory == null) {
      throw new IllegalStateException("useKey command not set properly in UseKey.execute.");
    }
    if (entity.hasFeature("lock")) {
      String lockType = entity.getFeature("lock").getFeatureType();
      if (lockType.equals("lockedWithKey")) {
        LockedWithKey lock = (LockedWithKey) entity.getFeature("lock");
        for (Pair<Item, Integer> item : inventory.getItems()) {
          if (lock.isKeyMatched(item.getKey().getName())) {
            entity.editFeature(new UnlockedWithKey(lock.getKey()));
            return true;
          }
        }
        return false;
      } else return lockType.matches("unlocked.*");
    } else {
      throw new IllegalArgumentException(entity.getName()+" does not have lock feature in UseKey.execute.");
    }
  }

  @Override
  public String getName() {
    return "useKey";
  }

  @Override
  public String getViewName() {
    return getName() + " _ ";
  }
}
