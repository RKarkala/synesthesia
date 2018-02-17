package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import handlers.Logger;
import main.Game;

public class Player extends B2DSprite {
	private int numCrystals;
	private int totalCrystals;
	private Texture tex;
	Logger logger = new Logger();

	public Player(Body body) {
		super(body);
		Texture tex = Game.res.getTexture("yellowPlayer");
		this.tex = tex;
		logger.writeEvent("Player Texture Set");
		TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
		setAnimation(sprites, 1 / 6f);
	}
	public Player(Body body, Texture texture) {
		super(body);
		if(texture == null) {
			texture = Game.res.getTexture("yellowPlayer");
		}
		logger.writeEvent("Player Recreated with new texture");
		TextureRegion[] sprites = TextureRegion.split(texture, 32, 32)[0];
		setAnimation(sprites, 1 / 6f);
	}
	public void setTexture(String name) {
		Texture tex = Game.res.getTexture(name);
		this.tex = tex;
		logger.writeEvent("Player texture set");
		TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
		setAnimation(sprites, 1 / 6f);
	}
	public Texture getTexture() {
		return tex;
	}
}