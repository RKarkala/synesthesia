package entities;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class TitleBackground extends ApplicationAdapter {
	SpriteBatch batch;
	Animation<TextureRegion> animation;
	float elapsed;

	public TitleBackground(Body body) {
		batch = new SpriteBatch();
		animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("Title-Screen.gif").read());
	}

	@Override
	public void render() {
		elapsed += Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(1, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(animation.getKeyFrame(elapsed), 20.0f, 20.0f);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

}
