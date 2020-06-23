import javafx.util.Pair;
import java.util.Map;
import java.util.Objects;

public class ListSellerItems implements IItemCommand {

  private Seller seller;

  public ListSellerItems setCommand(Seller seller) {
    this.seller =
        Objects.requireNonNull(
            seller, "seller can not be null when performing ListSellerItems command.");
    return this;
  }

  @Override
  public Map<String, Pair<Item, Integer>> execute() {
    if (seller == null) {
      throw new IllegalStateException("ListSellerItems command not set properly in ListSellerItems.execute.");
    }
    return seller.getSellerItems().getInventory();
  }

  @Override
  public String getName() {
    return "list";
  }

  @Override
  public String getViewName() {
    return getName();
  }
}
