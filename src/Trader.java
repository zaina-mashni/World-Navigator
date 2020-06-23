public class Trader implements ITradable {

  @Override
  public String getName() {
    return this.NAME;
  }

  @Override
  public String getFeatureType() {
    return "trader";
  }
}
