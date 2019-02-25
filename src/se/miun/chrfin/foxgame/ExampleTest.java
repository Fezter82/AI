package se.miun.chrfin.foxgame;

import static net.finkn.foxgame.util.Role.FOX;
import static net.finkn.foxgame.util.Role.SHEEP;
import net.finkn.foxgame.PerformanceTest;
import net.finkn.foxgame.adapters.EngineFactory;
import se.miun.chrfin.foxgame.AiGameEngine;
import se.miun.chrfin.foxgame.setup.PlayerSetup;

public class ExampleTest {
  public static void main(String[] args) {
    EngineFactory factory = new MyFactory();
    boolean result;

    // Some example tests...

    // Use the default test parameters.
    result = PerformanceTest.test(factory).run();
    System.out.println("Result: " + result);

    // Tweaking each parameter.
    result = PerformanceTest.test(factory)
      .seeds(123, 234) // Use random() for a random seed (default is 1)
      .matches(5)       // Run 5 matches per level (default is 10)
      .levels(0, 2)     // Runs levels 0 and 2 (default is 0, 1, and 2) (max 3)
      .timeouts(200)    // Use 200 ms timeout (default is 500 and 300)
      .roles(FOX)       // Only play as fox (default is FOX and SHEEP)
      .agents(new MyFactory()) // Want to play against another agent? Put it
                        // here. Not used by default.
      .run(.5);         // Win threshold of 50% (default is 80%)
    System.out.println("Result: " + result);

  }
}

class MyFactory implements EngineFactory {
  @Override
  public AiGameEngine getEngine(String role) {
	  return new FoxGameEngine(new PlayerSetup("", 0, "FOX", "Andreas", ""));
  }

  @Override
  public String toString() {
    return "me"; // Or "AwesomeFoxgameEngine" if you want to. :)
  }
}
