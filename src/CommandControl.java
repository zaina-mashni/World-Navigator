import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CommandControl {

  private ICommand command;
  private Map<String, ICommand> availableCommands;

  public CommandControl() {
    availableCommands = new HashMap<>();
  }

  public void loadRoomCommands() {
    clearCommands();
    availableCommands.put(new Inventory().getName(), new Inventory());
    availableCommands.put(new Backward().getName(), new Backward());
    availableCommands.put(new Forward().getName(), new Forward());
    availableCommands.put(new Left().getName(), new Left());
    availableCommands.put(new Look().getName(), new Look());
    availableCommands.put(new Right().getName(), new Right());
    availableCommands.put(new UseFlashlight().getName(), new UseFlashlight());
    availableCommands.put(new UseSwitch().getName(), new UseSwitch());
  }

  public void loadWallCommands() {
    clearCommands();
    availableCommands.put(new Trade().getName(), new Trade());
    availableCommands.put(new Inventory().getName(), new Inventory());
    availableCommands.put(new Check().getName(), new Check());
    availableCommands.put(new Open().getName(), new Open());
    availableCommands.put(new UseKey().getName(), new UseKey());
    availableCommands.put(new Look().getName(), new Look());
  }

  public void loadTradeCommands() {
    clearCommands();
    availableCommands.put(new Inventory().getName(), new Inventory());
    availableCommands.put(new Buy().getName(), new Buy());
    availableCommands.put(new Sell().getName(), new Sell());
    availableCommands.put(new ListSellerItems().getName(), new ListSellerItems());
  }

  public void loadObjectCommands() {
    clearCommands();
    availableCommands.put(new Take().getName(), new Take());
    availableCommands.put(new Inventory().getName(), new Inventory());
  }

  public void clearCommands() {
    availableCommands.clear();
  }

  public Map<String, ICommand> getAvailableCommands() {
    return availableCommands;
  }

  public String getCommandName(int commandIdx) {
    int i = 1;
    for (Map.Entry<String, ICommand> command : availableCommands.entrySet()) {
      if (i == commandIdx) return command.getKey();
      i++;
    }
    return "wrongCommand";
  }

  public void setCommand(ICommand command) {
    this.command = command;
  }

  public boolean executeActionCommands() {
    Objects.requireNonNull(command, "Command has to be set before execution in executeActionCommands.");
    try {
      return ((IActionCommand) command).execute();
    } catch (Exception e) {
      throw new IllegalStateException("Command being executed is not of type IActionCommand in executeActionCommands.");
    }
  }

  public List<Entity> executeEntityCommands() {
    Objects.requireNonNull(command, "Command has to be set before execution in executeEntityCommands.");
    try {
      return ((IEntityCommand) command).execute();
    } catch (Exception e) {
      throw new IllegalStateException("Command being executed is not of type IEntityCommand in executeEntityCommands.");
    }
  }

  public Map<String, Pair<Item, Integer>> executeItemCommands() {
    Objects.requireNonNull(command, "Command has to be set before execution in executeItemCommands.");
    try {
      return ((IItemCommand) command).execute();
    } catch (Exception e) {
      throw new IllegalStateException("Command being executed is not of type IItemCommand in executeItemCommands.");
    }
  }

  public void executeOrientationCommands() {
    Objects.requireNonNull(command, "Command has to be set before execution in executeOrientationCommands.");
    try {
      ((IOrientationCommand) command).execute();
    } catch (Exception e) {
      throw new IllegalStateException("Command being executed is not of type IOrientationCommand in executeOrientationCommands.");
    }
  }

  public String executeLightCommands() {
    Objects.requireNonNull(command, "Command has to be set before execution in executeLightCommands.");
    try {
      return ((ILightCommand) command).execute();
    } catch (Exception e) {
      throw new IllegalStateException("Command being executed is not of type ILightCommand in executeLightCommands.");
    }
  }
}
