package handlers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

/*
 * Sets keyDown and keyUp events for all keys needed for the game
 */
public class MyInputProcessor extends InputAdapter{
	
	public boolean keyDown(int k) {
		if(k == Keys.LEFT) {
			MyInput.setKey(MyInput.BUTTON1, true);
		}
		if(k == Keys.RIGHT) {
			MyInput.setKey(MyInput.BUTTON2, true);
		}
		if(k == Keys.UP) {
			MyInput.setKey(MyInput.BUTTON3, true);
		}
		if(k == Keys.Z) {
			MyInput.setKey(MyInput.BUTTON4, true);
		}
		if(k == Keys.DOWN) {
			MyInput.setKey(MyInput.BUTTON5, true);
		}
		if(k == Keys.ENTER) {
			MyInput.setKey(MyInput.BUTTON6, true);
		}
		if(k == Keys.ESCAPE) {
			MyInput.setKey(MyInput.BUTTON7, true);
		}
		return true;
	}
	public boolean keyUp(int k) {
		if(k == Keys.LEFT) {
			MyInput.setKey(MyInput.BUTTON1, false);
		}
		if(k == Keys.RIGHT) {
			MyInput.setKey(MyInput.BUTTON2, false);
		}
		if(k == Keys.UP) {
			MyInput.setKey(MyInput.BUTTON3, false);
		}
		if(k == Keys.Z) {
			MyInput.setKey(MyInput.BUTTON4, false);
		}
		if(k == Keys.DOWN) {
			MyInput.setKey(MyInput.BUTTON5, false);
		}
		if(k == Keys.ENTER) {
			MyInput.setKey(MyInput.BUTTON6, false);
		}
		if(k == Keys.ESCAPE) {
			MyInput.setKey(MyInput.BUTTON7, false);
		}
		return true;
	}

}
