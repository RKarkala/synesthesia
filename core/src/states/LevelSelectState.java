package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.tomgrill.gdxdialogs.core.GDXDialogs;
import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;
import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;
import de.tomgrill.gdxdialogs.core.listener.ButtonClickListener;
import handlers.GameStateManager;
import handlers.MyInput;

public class LevelSelectState extends GameState {
	private SpriteBatch sb;
	private BitmapFont titleFont;
	private BitmapFont font;

	private int currentItem;
	private String[] menuItems;

	private Texture background;
	private Texture title;

	private Texture tutorialPressed;
	private Texture level1Pressed;
	private Texture level2Pressed;
	private Texture level3Pressed;
	private Texture level4Pressed;
	private Texture level5Pressed;
	private Texture backPressed;

	private Texture tutorialnotPressed;
	private Texture level1notPressed;
	private Texture level2notPressed;
	private Texture level3notPressed;
	private Texture level4notPressed;
	private Texture level5notPressed;
	private Texture backnotPressed;

	public LevelSelectState(GameStateManager gsm) {
		super(gsm);
		sb = new SpriteBatch();
		font = new BitmapFont();
		tutorialPressed = new Texture("images/levelSelectImages/tutorialpressed.png");
		level1Pressed = new Texture("images/levelSelectImages/level1pressed.png");
		level2Pressed = new Texture("images/levelSelectImages/level2pressed.png");
		level3Pressed = new Texture("images/levelSelectImages/level3pressed.png");
		level4Pressed = new Texture("images/levelSelectImages/level4pressed.png");
		level5Pressed = new Texture("images/levelSelectImages/level5pressed.png");
		backPressed = new Texture("images/levelSelectImages/backpressed.png");

		tutorialnotPressed = new Texture("images/levelSelectImages/tutorialnotpressed.png");
		level1notPressed = new Texture("images/levelSelectImages/level1notpressed.png");
		level2notPressed = new Texture("images/levelSelectImages/level2notpressed.png");
		level3notPressed = new Texture("images/levelSelectImages/level3notpressed.png");
		level4notPressed = new Texture("images/levelSelectImages/level4notpressed.png");
		level5notPressed = new Texture("images/levelSelectImages/level5notpressed.png");
		backnotPressed = new Texture("images/levelSelectImages/backnotpressed.png");

		background = new Texture("images/background.jpg");
		title = new Texture("images/levelSelectImages/title.png");

	}

	@Override
	public void handleInput() {
		if (MyInput.isPressed(MyInput.BUTTON3)) {
			currentItem--;
			currentItem += 7;
			currentItem %= 7;
		}
		if (MyInput.isPressed(MyInput.BUTTON5)) {
			currentItem++;
			currentItem += 7;
			currentItem %= 7;
		}
		if (MyInput.isPressed(MyInput.BUTTON6)) {
			select();
		}
		if (MyInput.isPressed(MyInput.BUTTON7)) {
			gsm.popState();
		}

	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		handleInput();
	}

	@Override
	public void render() {
		// Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		GlyphLayout gl = new GlyphLayout();
		sb.begin();
		sb.draw(background, 0, 0);
		sb.draw(title, 0, 0);
		sb.draw(tutorialnotPressed, 0, 0);
		sb.draw(level1notPressed, 0, 0);
		sb.draw(level2notPressed, 0, 0);
		sb.draw(level3notPressed, 0, 0);
		sb.draw(level4notPressed, 0, 0);
		sb.draw(level5notPressed, 0, 0);
		sb.draw(backnotPressed, 0, 0);
		switch (currentItem) {
		case 0:
			sb.draw(tutorialPressed, 0, 0);
			break;
		case 1:
			sb.draw(level1Pressed, 0, 0);
			break;
		case 2:
			sb.draw(level2Pressed, 0, 0);
			break;
		case 3:
			sb.draw(level3Pressed, 0, 0);
			break;
		case 4:
			sb.draw(level4Pressed, 0, 0);
			break;
		case 5:
			sb.draw(level5Pressed, 0, 0);
			break;
		case 6:
			sb.draw(backPressed, 0, 0);
			break;
		default:
			break;
		}
		sb.end();

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	private void select() {
		String target = "";
		boolean isLevel = false;
		if (currentItem == 0) {
			target = "tutorial";
			isLevel = true;
		} else if (currentItem == 6) {
			target = "back";

		} else {
			target = ("level" + (currentItem));
			isLevel = true;
		}
		if (isLevel) {
			FileHandle handle = Gdx.files.internal("allowed.txt");
			String allowed = handle.readString();
			if (allowed.contains(target)) {
				gsm.pushState(GameStateManager.play, target);
			} else {
				GDXDialogs dialogs = GDXDialogsSystem.install();
				GDXButtonDialog bDialog = dialogs.newDialog(GDXButtonDialog.class);
				bDialog.setTitle("Not Unlocked");
				bDialog.setMessage("Please Complete the Previous Levels");

				bDialog.setClickListener(new ButtonClickListener() {

					@Override
					public void click(int button) {
						if (button == 0) {

						}
					}
				});

				bDialog.addButton("Ok");

				bDialog.build().show();
			}
		} else {
			gsm.popState();
		}

	}

}
