public interface IContainerizable extends IFeature {

  String NAME = "container";

  void addItem(Item item, int amount);

  int getAmount(String itemName);
}
