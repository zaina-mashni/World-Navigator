public class Unlocked implements ILockable {

  private boolean openStatus;

  @Override
  public boolean isLocked() {
    return false;
  }

  @Override
  public String getName() {
    return this.NAME;
  }

  @Override
  public String getFeatureType() {
    return "unlocked";
  }

  @Override
  public Key getKey() {
    return new Key.Builder("noKey").build();
  }

  @Override
  public String getKeyName() {
    return "noKey";
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
