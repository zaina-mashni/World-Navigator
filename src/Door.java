public class Door extends Entity {

  public Door(Builder builder) {
    super(builder);
  }

  public static class Builder extends Entity.Builder<Door.Builder> {

    public Builder() {
      super("door");
    }

    @Override
    public Door build() {
      return new Door(this);
    }

    @Override
    protected Builder self() {
      return this;
    }
  }
}
