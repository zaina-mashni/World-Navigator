import java.util.List;

public interface IEntityCommand extends ICommand {
  public List<Entity> execute();
}
