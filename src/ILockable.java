public interface ILockable extends IFeature {

  boolean isLocked();

  Key getKey();

  String getKeyName();

  boolean getOpenStatus();

  void setOpenStatus(boolean openStatus);

  String NAME = "lock";
}
