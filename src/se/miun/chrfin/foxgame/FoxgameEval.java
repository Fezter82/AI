package se.miun.chrfin.foxgame;

import java.util.Collection;
import ch.rfin.ai.games.EvalFunction;
import ch.rfin.foxgame.Pos;
import ch.rfin.foxgame.Pos.Impl;
import ch.rfin.foxgame.rules.State;

/**
 * Evaluatues different states and returns points depending on different parameters.
 * Players are FOX and SHEEP and FOX will prefer high values and SHEEP low values
 * 
 * @author Andreas Lind
 */

public class FoxgameEval implements EvalFunction<State> {

	
	@Override
	public double utilityOf(State state) {
		
		int nrOfFoxes = 0;
		int nrOfSheep = 0;
		int nrOfSheepInPen = 0;
		int sheepNearby = 0;
		
		nrOfFoxes = state.getFoxes().size() * 10; // 2 foxes * 10 = 20p
		nrOfSheep = state.getSheep().size() * -1; //20 sheep * -1 = -20p
		
		//Check if sheep are in pen
		nrOfSheepInPen = sheepInPen(state);
		
		//Is fox/sheep moving away from each other
		sheepNearby = areSheepNearby(state);
		
						
		return nrOfFoxes + nrOfSheep + nrOfSheepInPen + sheepNearby;
	}

	/**
	 * Sheep will prefer a low score and the more sheep in pen,
	 * the lower the score will be
	 * 
	 * @param state Contains information on the state of the game
	 * @return nrOfSheepInPen
	 */
	
	private int sheepInPen(State state) {
		
		int nrOfSheepInPen = 0;
		
		for(int x=2; x<4; x++) {
			for(int y=0; y<3; y++) {		
				if(state.hasSheep(Impl.pos(x, y))) {
					nrOfSheepInPen -= 2;
				}
			}
		}
		
		return nrOfSheepInPen;
	}
	
	/**
	 * 10 points will be returned if a move will make at least one
	 * sheep and a fox directly adjacent, or -10 if they wont be. This
	 * is not perfect though since some diagonal jumps are not possible.
	 * 
	 * @param state Contains information on the state of the game
	 * @return 10 or -10
	 */
	private int areSheepNearby(State state) {
		
		Collection<Pos> foxes = state.getFoxes();
		for(Pos p : foxes) {
			int x = p.getX()-1; //chose start-X one step left of foxes position
			int y = p.getY()-1; //chose start-Y one step above foxes position
			
			for(int i=x; i<(x+3); i++) {
				for(int j=y; j<y+3; j++) {
					if(state.hasSheep(Impl.pos(i, j))) {
						return 10; //a sheep is nearby
					}
				}
			}			
		}
		
		return -10; //no sheep is nearby
	}
}
