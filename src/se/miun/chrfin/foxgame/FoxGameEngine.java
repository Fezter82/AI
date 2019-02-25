package se.miun.chrfin.foxgame;

import ch.rfin.ai.games.EvalFunction;
import ch.rfin.foxgame.Foxgame;
import ch.rfin.foxgame.rules.State;
import se.miun.chrfin.foxgame.com.GameStatus;
import se.miun.chrfin.foxgame.setup.PlayerSetup;

/**
 * @author Christoffer Fink
 */
public class FoxGameEngine implements AiGameEngine {

  private final PlayerSetup setup;
  private final Foxgame game;
  private final AlphaBetaCutoff algo;
  private State state;
  private EvalFunction<State> eval;
  private String chosenMove;

  public FoxGameEngine(PlayerSetup setup) {
    this.setup = setup;
    this.game = new Foxgame(); 
    this.algo = new AlphaBetaCutoff();
    this.state = game.getRoot();
    this.eval = new FoxgameEval();
    this.chosenMove = null;    
  }
   

  /**
   * Return a move of the form "x1,y1 x2,y2".
   */
  @Override
  public String getMove(GameStatus status) {
	    
	  if(status.playerRole.equals("FOX"))
		  algo.setPlayer("MAX");
	  if(status.playerRole.equals("SHEEP"))
		  algo.setPlayer("MIN");
	   
	  
	  for(int depth = 1; depth<3; depth++) {
		  chosenMove = algo.bestMove(game, eval, state, depth);
	  }  
	  
	  algo.setBestAlpha(0);
	  algo.setBestBeta(0);
	  
	  return chosenMove;
  }

  @Override
  public void updateState(String move) {
    state = game.transition(state, move);
  }

  @Override
  public String getPlayerName() {
    return setup.playerName;
  }
}
