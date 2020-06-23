import java.util.Objects;

public class UnlockedWithKey implements ILockable {

  private Key key;
  private boolean openStatus;

  public UnlockedWithKey(Key key) {
    Objects.requireNonNull(key, "key in UnlockedWithKey can not be null.");
    this.key = key;
  }

  @Override
  public boolean isLocked() {
    return false;
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
  public boolean getOpenStatus() {
    return openStatus;
  }

  @Override
  public void setOpenStatus(boolean openStatus) {
    this.openStatus = openStatus;
  }

  @Override
  public String getName() {
    return this.NAME;
  }

  @Override
  public String getFeatureType() {
    return "unlockedWithKey";
  }
}
