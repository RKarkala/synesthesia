package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

import de.tomgrill.gdxdialogs.core.GDXDialogs;
import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;
import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;
import de.tomgrill.gdxdialogs.core.dialogs.GDXTextPrompt;
import de.tomgrill.gdxdialogs.core.listener.ButtonClickListener;
import de.tomgrill.gdxdialogs.core.listener.TextPromptListener;
import handlers.GameStateManager;
import handlers.MyInput;
import main.Game;

public class MenuState extends GameState {

	private SpriteBatch sb;
	private BitmapFont titleFont;
	private BitmapFont font;

	// private final String title = "Synesthesia";

	private int currentItem;

	private Texture background;
	private Texture selectedPlay;
	private Texture unSelectedPlay;
	private Texture selectedOption;
	private Texture unSelectedOption;
	private Texture title;
	private boolean isPlaySelected = true;
	Preferences preferences;
	private float musicVolume;

	private Music backgroundMusic;

	public MenuState(GameStateManager gsm) {
		super(gsm);
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/background.ogg"));
		backgroundMusic.setLooping(true);
		try {
			musicVolume = preferences.getFloat("bg");
		}catch(Exception e) {
			musicVolume = .1f;
		}
		backgroundMusic.setVolume(musicVolume);
		backgroundMusic.play();

		sb = new SpriteBatch();
		/*
		 * font = new BitmapFont(); font.setColor(Color.BLUE); menuItems = new String[]
		 * { "Play", "Quit" }; FreeTypeFontGenerator titleGenerator = new
		 * FreeTypeFontGenerator(Gdx.files.internal("titleFont.ttf"));
		 * FreeTypeFontParameter titleParamater = new FreeTypeFontParameter();
		 * titleParamater.size = 75; titleParamater.color = Color.WHITE; titleFont =
		 * titleGenerator.generateFont(titleParamater); titleGenerator = new
		 * FreeTypeFontGenerator(Gdx.files.internal("textFont.ttf"));
		 * FreeTypeFontParameter textParameter = new FreeTypeFontParameter();
		 * textParameter.size = 45; textParameter.color = Color.WHITE; font =
		 * titleGenerator.generateFont(textParameter); titleGenerator.dispose();
		 */
		background = new Texture("images/background.jpg");
		selectedPlay = new Texture("images/playselected.png");
		unSelectedPlay = new Texture("images/playnotselected.png");
		selectedOption = new Texture("images/optionselected.png");
		unSelectedOption = new Texture("images/optionnotselected.png");
		title = new Texture("images/title.png");
		preferences = Gdx.app.getPreferences("options");
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		if (MyInput.isPressed(MyInput.BUTTON3)) {
			currentItem--;
			currentItem+=2;
			currentItem%=2;
			isPlaySelected = !isPlaySelected;
		}
		if (MyInput.isPressed(MyInput.BUTTON5)) {
			currentItem++;
			currentItem+=2;
			currentItem%=2;
			isPlaySelected = !isPlaySelected;
		}
		if (MyInput.isPressed(MyInput.BUTTON6)) {
			select();
		}
		if (MyInput.isPressed(MyInput.BUTTON7)) {
			confirmExit();
		}
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		handleInput();
	}

	@Override
	public void render() {
		int width = Game.width * Game.scale;
		int height = Game.height * Game.scale;
		try {
			musicVolume = preferences.getFloat("bg");
		}catch(Exception e) {
			musicVolume = .1f;
		}
		backgroundMusic.setVolume(musicVolume);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		GlyphLayout gl = new GlyphLayout();
		sb.begin();
		sb.draw(background, 0, 0, width, height);
		sb.draw(title, 0, 0);
		if(isPlaySelected) {
			sb.draw(selectedPlay, 0, 0);
			sb.draw(unSelectedOption, 0, 0);
		}else {
			System.out.println((width-unSelectedPlay.getWidth())/2);
			sb.draw(unSelectedPlay, 0, 0);
			sb.draw(selectedOption, 0,0);
		}
		
		sb.end();

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	private void select() {
		
		if (currentItem == 0) {
			gsm.pushState(GameStateManager.levelSelect, "");

		} else if (currentItem == 1) {
			final GDXDialogs dialogs = GDXDialogsSystem.install();
			GDXButtonDialog bDialog = dialogs.newDialog(GDXButtonDialog.class);
			bDialog.setTitle("Options");

			bDialog.setClickListener(new ButtonClickListener() {

				@Override
				public void click(int button) {
					if (button == 0) {
						GDXTextPrompt music = dialogs.newDialog(GDXTextPrompt.class);
						music.setTitle("Change Volume");
						music.setMessage("Enter Volume (0-100).");

						music.setCancelButtonLabel("Cancel");
						music.setConfirmButtonLabel("Change");

						music.setTextPromptListener(new TextPromptListener() {
							@Override
							public void confirm(String text) {
								try {
									int vol = Integer.parseInt(text);
									if(vol < 0) {
										vol = 0;
									}
									if(vol > 100) {
										vol = 100;
									}
									preferences.putFloat("bg", vol/100f);
									preferences.flush();
								}catch(Exception exception) {
									exception.printStackTrace();
									cancel();
								}
							}
							@Override
							public void cancel() {
							  
							}
						});

						music.build().show();
					}
					if(button == 1) {
						GDXTextPrompt music = dialogs.newDialog(GDXTextPrompt.class);
						music.setTitle("Change Volume");
						music.setMessage("Enter Volume (0-100).");

						music.setCancelButtonLabel("Cancel");
						music.setConfirmButtonLabel("Change");

						music.setTextPromptListener(new TextPromptListener() {
							@Override
							public void confirm(String text) {
								try {
									int vol = Integer.parseInt(text);
									if(vol < 0) {
										vol = 0;
									}
									if(vol > 100) {
										vol = 100;
									}
									preferences.putFloat("sfx", vol/100f);
									preferences.flush();
								}catch(Exception exception) {
									exception.printStackTrace();
									cancel();
								}
							}
							@Override
							public void cancel() {
							  
							}
						});

						music.build().show();
					}
				}
			});

			bDialog.addButton("Music Level");
			bDialog.addButton("SFX Level");

			bDialog.build().show();
			
		}
	}

	private void confirmExit() {
		GDXDialogs dialogs = GDXDialogsSystem.install();
		GDXButtonDialog bDialog = dialogs.newDialog(GDXButtonDialog.class);
		bDialog.setTitle("Are You Sure?");
		bDialog.setMessage("Are You Sure You Want to Quit?");
		

		bDialog.setClickListener(new ButtonClickListener() {

			@Override
			public void click(int button) {
				if (button == 1) {
					Gdx.app.exit();
				}
			}
		});

		bDialog.addButton("No");
		bDialog.addButton("Yes");

		bDialog.build().show();
	}

}
