import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Room {

  private int roomIndex;
  private Map<String, IFeature> features;
  private List<Wall> walls;

  public Room() {
    walls = new ArrayList<>(Arrays.asList(new Wall(), new Wall(), new Wall(), new Wall()));
    features = new HashMap<>();
  }

  public void setWall(Wall wall, int wallIndex) {
    Objects.requireNonNull(wall, "Wall can not be null when you set room walls in Room.setWall.");
    checkWallIndex(wallIndex);
    if (wallIndex >= 0 && wallIndex <= 3) {
      walls.set(wallIndex, wall);
    }
  }

  public void addFeature(IFeature feature) {
    Objects.requireNonNull(feature, "feature can not be null when adding to room in Room.addFeature");
    if (!features.containsKey(feature.getName())) {
      features.put(feature.getName(), feature);
    }
    else throw new IllegalArgumentException("feature already added to room");
  }

  public void editFeature(IFeature feature) {
    checkFeatureName(feature.getName());
    features.replace(feature.getName(), feature);
  }

  public void addFeatures(Map<String, IFeature> features) {
    for (Map.Entry<String, IFeature> feature : features.entrySet()) {
      addFeature(feature.getValue());
    }
  }

  public IFeature getFeature(String featureName) {
    checkFeatureName(featureName);
    return features.get(featureName);
  }

  public Wall getWall(int wallIndex) {
    checkWallIndex(wallIndex);
    return walls.get(wallIndex);
  }

  public int getRoomIndex() {
    return roomIndex;
  }

  public void setRoomIndex(int index) {
    if (index < 0) throw new IndexOutOfBoundsException("index "+index+" of room can not be less than 0 in Room.setRoomIndex.");
    this.roomIndex = index;
  }

  private void checkWallIndex(int index) {
    if (index < 0 || index > 4) {
      throw new IndexOutOfBoundsException("wall index "+index+" can not be less than 0 or greater than 4 in Room.checkWallIndex.");
    }
  }

  private void checkFeatureName(String featureName) {
    if (!features.containsKey(featureName)) {
      throw new IllegalArgumentException(featureName+" feature does not exist in room in Room.checkFeatureName.");
    }
  }
}
