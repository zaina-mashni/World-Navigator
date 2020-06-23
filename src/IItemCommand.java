import javafx.util.Pair;
import java.util.Map;

public interface IItemCommand extends ICommand {
  public Map<String, Pair<Item, Integer>> execute();
}
