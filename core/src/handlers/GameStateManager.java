package handlers;

import java.util.Stack;

import main.Game;
import states.EndState;
import states.GameState;
import states.LevelSelectState;
import states.MenuState;
import states.Play;
/*
 * Creates framework for loading gamestates as well as setting game states
 */
public class GameStateManager {
	private Game game;
	
	private Stack<GameState> gameStates;
	
	Logger logger = new Logger();
	public static final int menu = 0;
	public static final int levelSelect = 1;
	public static final int end = 2;
	public static final int  play = 123;
	
	public GameStateManager(Game game) {
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(menu, "");
		logger.writeEvent("Menu State Loaded");
	}
	
	public Game game() {
		return game;
	}
	
	public void update(float dt) {
		gameStates.peek().update(dt);
		
	}
	public void render() {
		gameStates.peek().render();
		
	}
	//Gets the current state
	private GameState getState(int state, String level) {
		if(state == play) {
			logger.writeEvent("Play State Loaded ("+  level + ")" );
			return new Play(this, level);

		}
		if(state == menu) {
			logger.writeEvent("Menu State Loaded");
			return new MenuState(this);
		}
		if(state == levelSelect) {
			logger.writeEvent("Level Select State State Loaded");
			return new LevelSelectState(this);
		}
		if(state == end) {
			logger.writeEvent("Level Select State State Loaded");
			return new EndState(this);
		}
		return null;
	}
	public void setState(int state, String level) {
		popState();
		pushState(state, level);	
	}
	public void pushState(int state, String level) {
		logger.writeEvent( "State with id " + state +"is being pushed");
		gameStates.push(getState(state, level));
	}
	//Removes the current state from the stack
	public void popState() {
		GameState g = gameStates.pop();
		logger.writeEvent("Previous State Removed");
		g.dispose();
	}
	
	
}
