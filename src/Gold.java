public class Gold extends Item {

  public Gold(Builder builder) {
    super(builder);
  }

  public static class Builder extends Item.Builder<Gold.Builder> {

    public Builder() {
      super("gold");
    }

    @Override
    public Gold build() {
      return new Gold(this);
    }

    @Override
    protected Builder self() {
      return this;
    }
  }
}
