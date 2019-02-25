package se.miun.chrfin.foxgame;

import java.util.Arrays;
import ch.rfin.ai.games.EvalFunction;
import ch.rfin.foxgame.Foxgame;
import ch.rfin.foxgame.rules.State;

/**
 * Alpha Beta Cutoff search
 * 
 * @author Andreas Lind
 *
 */
public class AlphaBetaCutoff {
	
	static final double MAX_WIN = 1, MIN_WIN = -1;
	String player = null;
	String bestAction = null;
	int depthLimit = 0;
	double bestAlpha = 0, bestBeta = 0;


	/**
	 * Selects best move based on an Alpha Beta search with a
	 * cutoff-test and eval function
	 * 
	 * @param game
	 * @param eval 
	 * @param state 
	 * @param status 
	 * @param pDepthlimit
	 * @return bestAction a string on form "x,y"
	 */
	public String bestMove(Foxgame game, EvalFunction<State> eval, State state, int pDepthlimit) {
		
		depthLimit = pDepthlimit;
		int currentDepth = 0;
		
		if (player == "MAX")
			maxValue(game, eval, state, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true, currentDepth);
		if (player == "MIN")
			minValue(game, eval, state, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true, currentDepth);
		
		return bestAction;
	}
	
		
	/**
	 * 
	 * @param game
	 * @param eval
	 * @param state
	 * @param alpha
	 * @param beta
	 * @param onFirstLevel
	 * @param currentDepth
	 * @return
	 */
	private double maxValue(Foxgame game, EvalFunction<State> eval, State state, Double alpha, Double beta, boolean onFirstLevel, int currentDepth) {
		
		if(game.terminal(state)) {
			return game.utilityOf(state) * 100; //100p for MAX_WIN, -100p for MIN_WIN, 0 for DRAW
		}
		if(currentDepth == depthLimit) {
			if(peekOnNextLevel(game, state, "MAX") == MAX_WIN)
				return MAX_WIN * 100;
			return eval.utilityOf(state);
		}

		double v = Double.NEGATIVE_INFINITY;
		for (String action: game.possibleActionsIn(state)) {
			
			v = max(v, minValue(game, eval, game.transition(state, action), alpha, beta, false, currentDepth+1));
			
			if (v >= beta) {
				return v;
			}	
			if (onFirstLevel)
				if(v > alpha) {
					bestAction = action;
					bestAlpha = v;
				}
			
			alpha = max(alpha, v);
		}
		return v;
	}

		
	/**
	 * 
	 * @param game
	 * @param eval
	 * @param state
	 * @param alpha
	 * @param beta
	 * @param onFirstLevel
	 * @param currentDepth
	 * @return
	 */
	private double minValue(Foxgame game, EvalFunction<State> eval, State state, Double alpha, Double beta, boolean onFirstLevel, int currentDepth) {

		if(game.terminal(state)) {
			return game.utilityOf(state) * 100; //100p for MAX_WIN, -100p for MIN_WIN, 0 for DRAW
		}
		
		if(currentDepth == depthLimit) { 
			if(peekOnNextLevel(game, state, "MIN") == MIN_WIN)
				return MIN_WIN * 100;
			return eval.utilityOf(state);
		}

		double v = Double.POSITIVE_INFINITY;
		for (String action: game.possibleActionsIn(state)) {
			v = min(v, maxValue(game, eval, game.transition(state, action), alpha, beta, false, currentDepth+1));
			
			if (v <= alpha) {
				return v;
			}
			if (onFirstLevel)
				if(v < beta) {
					bestAction = action;
					bestBeta = v;
				}
			
			beta = min(beta, v);
		}
		return v;
	}
	
	/**
	 * Players might want to peek on the next level so that
	 * a devastating state is not found "around the corner"
	 * 
	 * @param game
	 * @param state
	 * @param role
	 * @return
	 */
	
	private double peekOnNextLevel(Foxgame game, State state, String role) {
		
		double result = 0;
		State nextState = null;
		
		for (String action: game.possibleActionsIn(state)) {
			nextState = game.transition(state, action);
			
			if(game.terminal(nextState)) {
				result = game.utilityOf(nextState);
				
				if(role == "MIN" && result == MIN_WIN)
					return result;
				if(role == "MAX" && result == MAX_WIN)
					return result;
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param args values that will be compared
	 * @return double with highest value
	 */
	private double max(double ...args) {	  
		Arrays.sort(args);
		return args[args.length - 1];
	}

	/**
	 * 
	 * @param args values that will be compared
	 * @return double with lowest value
	 */
	private double min (double ...args) {
		Arrays.sort(args);
		return args[0];
	}
	
	/**
	 * 
	 * @param bestAlpha
	 */
	public void setBestAlpha(double bestAlpha) {
		this.bestAlpha = bestAlpha;
	}

	
	/**
	 * 
	 * @param bestBeta
	 */
	public void setBestBeta(double bestBeta) {
		this.bestBeta = bestBeta;
	}

	
	/**
	 * 
	 * @param player
	 */
	public void setPlayer(String player) {
		this.player = player;
	}
	
}