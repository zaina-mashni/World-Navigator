public class Flashlight extends Item {

  private boolean itemStatus;

  private Flashlight(Builder builder) {
    super(builder);
    this.itemStatus = builder.itemStatus;
  }

  public boolean getItemStatus() {
    return itemStatus;
  }

  public void flipItemStatus() {
    itemStatus = !itemStatus;
  }

  public static class Builder extends Item.Builder<Builder> {

    private boolean itemStatus = false;

    public Builder() {
      super("flashlight");
    }

    public Builder status(boolean itemStatus) {
      this.itemStatus = itemStatus;
      return this;
    }

    @Override
    public Flashlight build() {
      return new Flashlight(this);
    }

    @Override
    protected Builder self() {
      return this;
    }
  }
}
