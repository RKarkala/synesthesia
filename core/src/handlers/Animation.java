package handlers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*
 * Creates the framework to allow for the animation of the player with variable timing
 */
public class Animation {
	private TextureRegion[] frames;
	private float time;
	private float delay;
	private int currentFrame;
	private int timesPlayed;

	public Animation() {

	}

	public Animation(TextureRegion[] frames) {
		this(frames, 1 /6f);
	}

	public Animation(TextureRegion[] frames, float delay) {
		setFrames(frames, delay);
	}
	public void setFrames(TextureRegion[] frames, float delay) {
		this.frames = frames;
		this.delay = delay;
		time = 0;
		currentFrame = 0;
		timesPlayed = 0;
	}
	public boolean isAnimationFinished() {
		if(currentFrame == 0) {
			return true;
		}
		return false;
	}
	public void update(float dt) {
		if(delay <= 0) return;
		time += dt;
		while(time >= delay) {
			step();
		}
	}
	private void step() {
		time -= delay;
		currentFrame++;
		if(currentFrame == frames.length) {
			currentFrame = 0;
			timesPlayed++;
		}
	}
	public TextureRegion getFrame() {
		return frames[currentFrame];
	}
	public int getTimesPlayed() {
		return timesPlayed;
	}
}