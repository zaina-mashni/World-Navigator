import java.util.ArrayList;
import java.util.List;

public class MapInfo implements IMapInfo {

  private List<ArrayList<Room>> map;
  private List<Room> rooms;
  private int specialRoomIndex;

  public MapInfo(List<ArrayList<Room>> map, List<Room> rooms) {
    this.map = map;
    this.rooms = rooms;
    this.specialRoomIndex = rooms.size() - 1;
  }

  public Room getAdjacentRoom(int roomIndex, int facingWall) {
    return map.get(roomIndex).get(facingWall);
  }

  @Override
  public int getSpecialRoomIndex() {
    return specialRoomIndex;
  }

  @Override
  public Room getRoom(int roomIndex) {
    return rooms.get(roomIndex);
  }
}
