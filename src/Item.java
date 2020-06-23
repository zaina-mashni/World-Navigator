public abstract class Item {

  private int cost;
  private String name;

  public Item(Builder<?> builder) {
    this.cost = builder.cost;
    this.name = builder.name;
  }

  public int getCost() {
    return this.cost;
  }

  public String getName() {
    return this.name;
  }

  public abstract static class Builder<T extends Builder> {

    private String name;
    private int cost = -1;

    public Builder(String name) {
      if (name.isEmpty()) {
        throw new IllegalArgumentException("item name can not be empty in Item.Builder.");
      }
      this.name = name;
    }

    public T cost(int cost) {
      if (cost < 0 && !name.equals("gold")) {
        throw new IllegalArgumentException("item cost "+cost+" can not be less than 0 in Item.Builder.cost.");
      }
      this.cost = cost;
      return self();
    }

    public abstract Item build();

    protected abstract T self();
  }
}
