import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class Entity {

  protected Map<String, IFeature> features;
  protected String name;

  public Entity(Builder<?> builder) {
    this.name = builder.name;
    this.features = builder.features;
  }

  public String getName() {
    return name;
  }

  public IFeature getFeature(String featureName) {
    checkFeatureName(featureName);
    return features.get(featureName);
  }

  public void editFeature(IFeature feature) {
    checkFeatureName(feature.getName());
    features.replace(feature.getName(), feature);
  }

  public boolean hasFeature(String featureName) {
    return features.containsKey(featureName);
  }

  public abstract static class Builder<T extends Entity.Builder> {

    private String name;
    protected Map<String, IFeature> features;

    public Builder(String name) {
      this.name = name;
      features = new HashMap<>();
    }

    public T addFeature(IFeature feature) {
      Objects.requireNonNull(feature, "feature can not be null when adding to entity in Entity.Builder.addFeature.");
      if (!features.containsKey(feature.getName())) features.put(feature.getName(), feature);
      else throw new IllegalArgumentException(feature.getName()+" already added to entity in Entity.Builder.addFeature.");
      return self();
    }

    public T addFeatures(Map<String, IFeature> features) {
      for (Map.Entry<String, IFeature> feature : features.entrySet())
        addFeature(feature.getValue());
      return self();
    }

    public abstract Entity build();

    protected abstract T self();
  }

  private void checkFeatureName(String featureName) {
    if (!hasFeature(featureName))
      throw new IllegalArgumentException(featureName+" does not exist in entity in Entity.checkFeatureName.");
  }
}
