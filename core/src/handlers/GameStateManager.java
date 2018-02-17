package handlers;

import java.util.Stack;

import main.Game;
import states.GameState;
import states.LevelSelectState;
import states.MenuState;
import states.Play;

public class GameStateManager {
	private Game game;
	
	private Stack<GameState> gameStates;
	
	
	public static final int menu = 0;
	public static final int levelSelect = 1;
	public static final int  play = 123;
	
	public GameStateManager(Game game) {
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(menu, "");
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
	private GameState getState(int state, String level) {
		if(state == play) {
			System.out.println(level);
			return new Play(this, level);
		}
		if(state == menu) {
			return new MenuState(this);
		}
		if(state == levelSelect) {
			return new LevelSelectState(this);
		}
		return null;
	}
	public void setState(int state, String level) {
		popState();
		pushState(state, level);
	}
	public void pushState(int state, String level) {
		gameStates.push(getState(state, level));
	}
	public void popState() {
		GameState g = gameStates.pop();
		g.dispose();
	}
	
	
}
