import java.util.*;
import java.util.stream.Collectors;

public class Wall {

  private Map<String, Entity> entities;

  public Wall() {
    entities = new HashMap<>();
  }

  public List<Entity> getEntities() {
    return new ArrayList<>(entities.values());
  }

  public Entity getEntity(String entityName) {
    checkEntityName(entityName);
    return entities.get(entityName);
  }

  public void addEntities(List<Entity> entities) {
    for (Entity entity : entities) {
      Objects.requireNonNull(entity, "entity can not be null when adding to wall in Wall.addEntities.");
      this.entities.put(entity.getName(), entity);
    }
  }

  public boolean containsEntity(String entityName) {
    return entities.containsKey(entityName);
  }

  private void checkEntityName(String entityName) {
    if (!entities.containsKey(entityName)) {
      throw new IllegalArgumentException("entity does not exist in wall.");
    }
  }
}
