public class Key extends Item {

  public Key(Builder builder) {
    super(builder);
  }

  public static class Builder extends Item.Builder<Key.Builder> {

    public Builder(String name) {
      super(name);
    }

    @Override
    public Key build() {
      return new Key(this);
    }

    @Override
    protected Builder self() {
      return this;
    }
  }
}
