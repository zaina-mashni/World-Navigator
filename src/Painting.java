public class Painting extends Entity {

  public Painting(Builder builder) {
    super(builder);
  }

  public static class Builder extends Entity.Builder<Painting.Builder> {

    public Builder() {
      super("painting");
    }

    @Override
    public Painting build() {
      return new Painting(this);
    }

    @Override
    protected Builder self() {
      return this;
    }
  }
}
