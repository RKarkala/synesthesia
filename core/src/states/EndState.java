package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import handlers.GameStateManager;
import handlers.Logger;
import handlers.MyInput;
import main.Game;

/*
 * Displays end credit scene after user wins and returns to main menu on press of enter
 */
public class EndState extends GameState {

	private SpriteBatch sb;
	Logger logger = new Logger();

	private Texture background;
	private Texture endText;

	public EndState(GameStateManager gsm) {
		super(gsm);
		logger.writeEvent("USER HAS WON GAME!!!!!!!!!!!");
		sb = new SpriteBatch();
		background = new Texture("images/background.jpg");
		endText = new Texture("images/end.png");

	}

	@Override
	public void handleInput() {
		if (MyInput.isPressed(MyInput.BUTTON6)) {
			logger.writeEvent("Returning to Main Screen");
			gsm.popState();
		}

	}

	@Override
	public void update(float dt) {
		handleInput();
	}

	@Override
	public void render() {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		GlyphLayout gl = new GlyphLayout();
		sb.begin();
		sb.draw(background, 0, 0);
		sb.draw(endText, 0, 0);
		sb.end();

	}

	@Override
	public void dispose() {

	}

}
