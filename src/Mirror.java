public class Mirror extends Entity {

  public Mirror(Builder builder) {
    super(builder);
  }

  public static class Builder extends Entity.Builder<Mirror.Builder> {

    public Builder() {
      super("mirror");
    }

    @Override
    public Mirror build() {
      return new Mirror(this);
    }

    @Override
    protected Builder self() {
      return this;
    }
  }
}
