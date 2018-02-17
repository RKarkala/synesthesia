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
import handlers.Logger;

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

	//Instance Variables
	private SpriteBatch sb;
	private BitmapFont titleFont;
	private BitmapFont font;
	Logger logger = new Logger();

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
		//Load Background Music
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/background.ogg"));
		backgroundMusic.setLooping(true);
		//Set volume based on user settings
		try {
			musicVolume = preferences.getFloat("bg");
		}catch(Exception e) {
			musicVolume = .1f;
		}
		backgroundMusic.setVolume(musicVolume);
		backgroundMusic.play();
		logger.writeEvent("Music Started");

		//Create and load all assessts
		sb = new SpriteBatch();
		logger = new Logger();
		background = new Texture("images/background.jpg");
		selectedPlay = new Texture("images/playselected.png");
		unSelectedPlay = new Texture("images/playnotselected.png");
		selectedOption = new Texture("images/optionselected.png");
		unSelectedOption = new Texture("images/optionnotselected.png");
		logger.writeEvent("Successfully loaded images");
		title = new Texture("images/title.png");
		preferences = Gdx.app.getPreferences("options");
		logger.writeEvent("Menu Assessts loaded");
	}

	//Handle keyboard movements on menu screen
	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		if (MyInput.isPressed(MyInput.BUTTON3)) {
			currentItem--;
			currentItem+=2;
			currentItem%=2;
			isPlaySelected = !isPlaySelected;
			logger.writeEvent("Down Clicked");
		}
		if (MyInput.isPressed(MyInput.BUTTON5)) {
			currentItem++;
			currentItem+=2;
			currentItem%=2;
			isPlaySelected = !isPlaySelected;
			logger.writeEvent("Up Clicked");
		}
		if (MyInput.isPressed(MyInput.BUTTON6)) {
			select();
			logger.writeEvent("Enter Pressed");
		}
		if (MyInput.isPressed(MyInput.BUTTON7)) {
			confirmExit();
			logger.writeEvent("Escape Pressed");
		}
	}

	@Override
	public void update(float dt) {
		handleInput();
	}

	//Draws assests
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
	//Allows user to set sfx and background volume
	private void select() {
		
		if (currentItem == 0) {
			logger.writeEvent("Loading Level Select");
			gsm.pushState(GameStateManager.levelSelect, "");

		} else if (currentItem == 1) {
			final GDXDialogs dialogs = GDXDialogsSystem.install();
			GDXButtonDialog bDialog = dialogs.newDialog(GDXButtonDialog.class);
			bDialog.setTitle("Options");
			logger.writeEvent("Loading Options");
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
									logger.writeEvent("BG Volume Changed");
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
									logger.writeEvent("SFX Volume Changed");
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
	//Confirms that user wants to exit
	private void confirmExit() {
		GDXDialogs dialogs = GDXDialogsSystem.install();
		GDXButtonDialog bDialog = dialogs.newDialog(GDXButtonDialog.class);
		bDialog.setTitle("Are You Sure?");
		bDialog.setMessage("Are You Sure You Want to Quit?");
		

		bDialog.setClickListener(new ButtonClickListener() {

			@Override
			public void click(int button) {
				if (button == 1) {
					logger.writeEvent("Gracefully exited game");
					Gdx.app.exit();
				}
			}
		});

		bDialog.addButton("No");
		bDialog.addButton("Yes");

		bDialog.build().show();
	}

}
