import java.io.FileNotFoundException;

public class StartGame {
  public static void main(String[] args) throws FileNotFoundException {
    WorldNavigatorController controller = WorldNavigatorController.getInstance();
    controller.runGame("Maps");
  }
}
