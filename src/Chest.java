public class Chest extends Entity {

  public Chest(Builder builder) {
    super(builder);
  }

  public static class Builder extends Entity.Builder<Chest.Builder> {

    public Builder() {
      super("chest");
    }

    @Override
    public Chest build() {
      return new Chest(this);
    }

    @Override
    protected Builder self() {
      return this;
    }
  }
}
