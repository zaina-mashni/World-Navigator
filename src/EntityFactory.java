import java.util.HashMap;
import java.util.Map;

public class EntityFactory {
  private Map<String, IFeature> features;

  public EntityFactory() {
    features = new HashMap<>();
  }

  public void addFeature(IFeature feature) {
    if (!features.containsKey(feature.getName())) features.put(feature.getName(), feature);
    else throw new IllegalArgumentException(feature.getName()+" already exists in EntityFactory.addFeature");
  }

  Entity createEntity(String entityName) {
    switch (entityName) {
      case "painting":
        return new Painting.Builder().addFeatures(features).build();
      case "chest":
        return new Chest.Builder().addFeatures(features).build();
      case "door":
        return new Door.Builder().addFeatures(features).build();
      case "mirror":
        return new Mirror.Builder().addFeatures(features).build();
      case "seller":
        return new Seller.Builder().addFeatures(features).build();
      default:
        throw new IllegalArgumentException(entityName+" does not match any existing entity in EntityFactory.createEntity.");
    }
  }
}
