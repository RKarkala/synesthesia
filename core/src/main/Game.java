package main;

import java.awt.Desktop;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.URI;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import handlers.Content;
import handlers.GameStateManager;
import handlers.Logger;
import handlers.MyInput;
import handlers.MyInputProcessor;

public class Game extends ApplicationAdapter implements UncaughtExceptionHandler{

	//Sets up Instance Variables
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
	Logger logger;
	
	public SpriteBatch getSpriteBatch() {
		return sb;
	}
	
	public OrthographicCamera getMainCamera() {
		return mainCamera;
		
	}
	
	public OrthographicCamera getHudCamera() {
		return hudCamera;
	}
	//Sets up game by loadings assests
	@Override
	public void create () {
		logger.writeEvent("Game has Begun");
		Gdx.input.setInputProcessor(new MyInputProcessor());
		res = new Content();
		res.loadTexture("images/playerImages/blue.png", "bluePlayer");
		res.loadTexture("images/playerImages/green.png", "greenPlayer");
		res.loadTexture("images/playerImages/orange.png", "orangePlayer");
		res.loadTexture("images/playerImages/purple.png", "purplePlayer");
		res.loadTexture("images/playerImages/red.png", "redPlayer");
		res.loadTexture("images/playerImages/yellow.png", "yellowPlayer");
		res.loadTexture("images/heart.png", "hud");
		logger.writeEvent("All static textures loaded");
		
		
		sb = new SpriteBatch();
		logger.writeEvent("Sprite Batch Created");
		
		mainCamera = new OrthographicCamera();
		mainCamera.setToOrtho(false, width, height);
		logger.writeEvent("Main Camera Created");
		hudCamera = new OrthographicCamera();
		hudCamera.setToOrtho(false, width, height);
		logger.writeEvent("Hud Camera Created");
		gsm = new GameStateManager(this);
		logger.writeEvent("Game State Manager Created");
		
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
	public Game() {
		logger = new Logger();
	}
	@Override
	public void dispose () {
		
	}

	//Catches any game breaking error, logs it, and opens an email to report the bug
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		logger.writeError("Uncaught Exception" + e.getMessage());
		Desktop desktop = Desktop.getDesktop();
        String message = "mailto:rkarkala10@gmail.com?subject=fatalError&body="+e.getMessage();
        URI uri = URI.create(message);
        try {
			desktop.mail(uri);
		} catch (IOException e1) {
			logger.writeError("Error Sending Email, manually send to rkarkala10@gmail.com");
		}
		
	}
}