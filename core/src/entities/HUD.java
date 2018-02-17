package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import handlers.B2DVars;
import handlers.Logger;
import main.Game;

public class HUD {
	private Player player;
	private TextureRegion heart;
	Logger logger = new Logger();
	public HUD(Player player) {
		this.player = player;
		Texture text = Game.res.getTexture("hud");
		heart = new TextureRegion(text, 100, 100);
		logger.writeEvent("Hud Created");
	}
	public void render(SpriteBatch sb) {
		sb.begin();
		sb.draw(heart, 0, Game.height*Game.scale-100);
		sb.draw(heart, 110, Game.height*Game.scale-100);
		sb.draw(heart, 220, Game.height*Game.scale-100);
		sb.end();
	}
	public void renderTwo(SpriteBatch sb) {
		sb.begin();
		sb.draw(heart, 0, Game.height*Game.scale-100);
		sb.draw(heart, 110, Game.height*Game.scale-100);
		sb.end();
	}
	public void renderOne(SpriteBatch sb) {
		sb.begin();
		sb.draw(heart, 0, Game.height*Game.scale-100);
		sb.end();

	}
	public void renderNone(SpriteBatch sb) {
		sb.begin();
		sb.end();

	}
}
