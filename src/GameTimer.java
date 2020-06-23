import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {

  private long finishTime;
  private Timer timer;

  public GameTimer() {
    timer = new Timer();
  }

  TimerTask timerTask =
      new TimerTask() {
        public void run() {
          System.out.println("Game Over!");
          System.out.println("You ran out of time.");
          System.out.println("Better Luck Next Time");
          System.exit(0);
        }
      };

  public void schedule(long finishTime) {
    if (finishTime <= 0) {
      throw new IllegalArgumentException("finish time can not be less than or equal to zero in GameTimer.schedule.");
    }
    this.finishTime = System.currentTimeMillis() + finishTime * 1000;
    timer.schedule(timerTask, new Date(this.finishTime));
  }

  public long GetRemainingTime() {
    long timeLeft = Math.max(0, finishTime - System.currentTimeMillis());
    return timeLeft / 1000;
  }
}
