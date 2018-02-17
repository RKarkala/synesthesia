package main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import handlers.Content;
import handlers.GameStateManager;
import handlers.MyInput;
import handlers.MyInputProcessor;

public class Game extends ApplicationAdapter {

	
	public static final String title = "Synesthesia";
	public static final int width = 1920;
	public static final int height = 1080;
	public static final int scale = 1;
	
	public static final float step = 1 / 120f;
	private float accum;
	
	private SpriteBatch sb;
	private OrthographicCamera mainCamera;
	private OrthographicCamera hudCamera;
	
	private GameStateManager gsm;
	
	public static Content res;
	
	public SpriteBatch getSpriteBatch() {
		return sb;
	}
	
	public OrthographicCamera getMainCamera() {
		return mainCamera;
		
	}
	
	public OrthographicCamera getHudCamera() {
		return hudCamera;
	}
	
	@Override
	public void create () {
		
		Gdx.input.setInputProcessor(new MyInputProcessor());
		res = new Content();
		res.loadTexture("images/playerImages/blue.png", "bluePlayer");
		res.loadTexture("images/playerImages/green.png", "greenPlayer");
		res.loadTexture("images/playerImages/orange.png", "orangePlayer");
		res.loadTexture("images/playerImages/purple.png", "purplePlayer");
		res.loadTexture("images/playerImages/red.png", "redPlayer");
		res.loadTexture("images/playerImages/yellow.png", "yellowPlayer");
		res.loadTexture("images/heart.png", "hud");
		
		
		
		sb = new SpriteBatch();
		
		mainCamera = new OrthographicCamera();
		mainCamera.setToOrtho(false, width, height);
		
		hudCamera = new OrthographicCamera();
		hudCamera.setToOrtho(false, width, height);
		
		gsm = new GameStateManager(this);
	}

	@Override
	public void render () {
		accum += Gdx.graphics.getDeltaTime();
		while(accum >= step) {
			accum -= step;
			gsm.update(step);
			gsm.render();
			MyInput.update();
		}
		
	}
	
	@Override
	public void dispose () {
		
	}
}
