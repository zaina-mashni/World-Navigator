import java.util.Objects;

public class Open implements IActionCommand {
  private Entity entity;

  public Open setCommand(Entity entity) {
    this.entity =
        Objects.requireNonNull(entity, "entity can not be null when performing open command.");
    return this;
  }

  @Override
  public boolean execute() {
    if (entity == null) {
      throw new IllegalStateException("Open command not set properly in Open.execute");
    }
    ILockable lockable = (ILockable) entity.getFeature("lock");
    if (lockable.isLocked()) return false;
    lockable.setOpenStatus(true);
    return true;
  }

  @Override
  public String getName() {
    return "open";
  }

  @Override
  public String getViewName() {
    return getName() + " _ ";
  }
}
