import java.util.Objects;

public class LockedWithKey implements ILockable {

  private Key key;
  private boolean openStatus;

  public LockedWithKey(Key key) {
    this.key =
        Objects.requireNonNull(key, "key can not be null when setting up LockedWithKey feature.");
    this.openStatus = false;
  }

  public boolean isKeyMatched(String keyName) {
    return this.key.getName().equals(keyName);
  }

  @Override
  public boolean isLocked() {
    return true;
  }

  @Override
  public Key getKey() {
    return key;
  }

  @Override
  public String getKeyName() {
    return key.getName();
  }

  @Override
  public String getName() {
    return this.NAME;
  }

  @Override
  public String getFeatureType() {
    return "lockedWithKey";
  }

  @Override
  public boolean getOpenStatus() {
    return openStatus;
  }

  @Override
  public void setOpenStatus(boolean openStatus) {
    this.openStatus = openStatus;
  }
}
