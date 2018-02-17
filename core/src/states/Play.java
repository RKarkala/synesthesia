package states;

import static handlers.B2DVars.PPM;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import de.tomgrill.gdxdialogs.core.GDXDialogs;
import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;
import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;
import de.tomgrill.gdxdialogs.core.listener.ButtonClickListener;
import entities.HUD;
import entities.Player;
import handlers.B2DVars;
import handlers.GameStateManager;
import handlers.MyContactListener;
import handlers.MyInput;
import main.Game;

public class Play extends GameState {

	private boolean debug = false;

	private World world;
	private Box2DDebugRenderer b2dr;
	private OrthographicCamera b2dcam;

	private MyContactListener cl;
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer tmr;
	private int tileSize;
	private Player player;
	private final int maxSpeed = 5;
	private boolean keyPressed = false;

	private float startX = 0;
	private float startY = 0;

	private boolean isPaused = false;
	private boolean restart = false;

	private boolean first = true;
	private int lives = 3;
	Preferences preferences;
	private float sfxLevel;

	private HUD hud;
	String level;
	Texture background;

	public Play(GameStateManager gsm, String level) {

		super(gsm);
		preferences = Gdx.app.getPreferences("options");
		try {
			sfxLevel = preferences.getFloat("sfx");
		}catch(Exception e) {
			sfxLevel = .1f;
		}
		System.out.println("Play " + level);
		world = new World(new Vector2(0, -9.81f), true);
		cl = new MyContactListener();
		world.setContactListener(cl);
		b2dr = new Box2DDebugRenderer();
		FileHandle file = Gdx.files.internal("maps/" + level + ".txt");
		String info = file.readString();
		System.out.println(info);
		String parts[] = info.split("\n");
		System.out.println(Arrays.toString(parts));
		if (first) {
			startX = Float.parseFloat(parts[0]);
			startY = Float.parseFloat(parts[1]);
			first = false;
		}
		background = new Texture("images/background.jpg");
		background.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		this.level = level;

		createPlayer(B2DVars.BIT_YELLOW, new Texture("images/playerImages/yellow.png"));
		createTiles();
		createPlates();
		createDoor();
		createCheckpoints();

		b2dcam = new OrthographicCamera();
		b2dcam.setToOrtho(false, Game.width / PPM, Game.height / PPM);

		System.out.println("Var is " + this.level);
		hud = new HUD(player);

	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		if (MyInput.isPressed(MyInput.BUTTON3)) {
			if (cl.isPlayerOnGround()) {
				Sound sound = Gdx.audio.newSound(Gdx.files.internal("sfx/jump.wav"));
				player.getBody().applyForceToCenter(new Vector2(0, 750), true);
				sound.play(sfxLevel);

			}
		}
		if (MyInput.isDown(MyInput.BUTTON1) && Math.abs(player.getBody().getLinearVelocity().x) < maxSpeed) {
			player.getBody().applyForceToCenter(new Vector2(-6, 0), true);
			keyPressed = true;

		}
		if (MyInput.isDown(MyInput.BUTTON2) && Math.abs(player.getBody().getLinearVelocity().x) < maxSpeed) {
			player.getBody().applyForceToCenter(new Vector2(6, 0), true);
			keyPressed = true;

		}
		if (!MyInput.isDown(MyInput.BUTTON2) && !MyInput.isDown(MyInput.BUTTON1) && !MyInput.isDown(MyInput.BUTTON3)) {
			keyPressed = false;
		}
		if (MyInput.isPressed(MyInput.BUTTON7)) {
			isPaused = true;
			GDXDialogs dialogs = GDXDialogsSystem.install();
			GDXButtonDialog bDialog = dialogs.newDialog(GDXButtonDialog.class);
			bDialog.setTitle("Are You Sure?");
			bDialog.setMessage("Are You Sure You Want to Quit?");
			bDialog.setClickListener(new ButtonClickListener() {
				@Override
				public void click(int button) {
					if (button == 0) {
						isPaused = false;
					}
					if (button == 1) {
						isPaused = false;
						gsm.popState();
					}
				}
			});
			bDialog.addButton("No");
			bDialog.addButton("Yes");
			bDialog.build().show();
		}
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		handleInput();

		if (cl.touchedBlue()) {
			Filter filter = player.getBody().getFixtureList().first().getFilterData();
			short cur = filter.maskBits;
			short to = B2DVars.BIT_BLUE;
			switchBlocks(cur, to, "bluePlayer");
			cl.blueContact = 0;
		} else if (cl.touchedGreen()) {
			Filter filter = player.getBody().getFixtureList().first().getFilterData();
			short cur = filter.maskBits;
			short to = B2DVars.BIT_GREEN;
			switchBlocks(cur, to, "greenPlayer");
			cl.greenContact = 0;
		} else if (cl.touchedOrange()) {
			Filter filter = player.getBody().getFixtureList().first().getFilterData();
			short cur = filter.maskBits;
			short to = B2DVars.BIT_ORANGE;
			switchBlocks(cur, to, "orangePlayer");
			cl.orangeContact = 0;
		} else if (cl.touchedPurple()) {
			Filter filter = player.getBody().getFixtureList().first().getFilterData();
			short cur = filter.maskBits;
			short to = B2DVars.BIT_PURPLE;
			switchBlocks(cur, to, "purplePlayer");
			cl.purpleContact = 0;
		} else if (cl.touchedRed()) {
			Filter filter = player.getBody().getFixtureList().first().getFilterData();
			short cur = filter.maskBits;
			short to = B2DVars.BIT_RED;
			switchBlocks(cur, to, "redPlayer");
			cl.redContact = 0;
		} else if (cl.touchedYellow()) {
			Filter filter = player.getBody().getFixtureList().first().getFilterData();
			short cur = filter.maskBits;
			short to = B2DVars.BIT_YELLOW;
			switchBlocks(cur, to, "yellowPlayer");
			cl.yellowContact = 0;
		}
		if (cl.onCheckpoint()) {
			float x = player.getBody().getPosition().x;
			float y = player.getBody().getPosition().y;
			System.out.println("Original Respawn - " + x + " " + y);
			x *= PPM;
			y *= PPM;
			FileHandle handle = Gdx.files.local("maps/" + level + ".txt");
			String[] vals = handle.readString().split("\n");
			float orix = Float.parseFloat(vals[0]);
			float oriy = Float.parseFloat(vals[1]);
			handle.writeString(orix + "\n" + oriy + "\n" + x + "\n" + y, false);
			cl.checkPoint = 0;
		}
		if (!isPaused) {
			world.step(dt, 8, 3);
		}
		if (cl.isOver()) {
			FileHandle handle = Gdx.files.local("maps/" + level + ".txt");
			String[] vals = handle.readString().split("\n");
			float orix = Float.parseFloat(vals[0]);
			float oriy = Float.parseFloat(vals[1]);
			handle.writeString(orix + "\n" + oriy + "\n" + orix + "\n" + oriy, false);
			System.out.println("Win Level");
			cl.doorContacts = 0;
			handle = Gdx.files.local("allowed.txt");
			if (!level.contains("5")) {
				
				if(level.contains("tutorial")){
					handle.writeString("level1", true);
					gsm.setState(GameStateManager.play, "level1");
				}else {
					int l = Integer.parseInt(level.substring(level.length() - 1));
					handle.writeString(level.substring(0, level.length() - 1) + (l + 1), true);
					gsm.setState(GameStateManager.play, level.substring(0, level.length() - 1) + (l + 1));
				}
				
				
			}
		}
		if (player.getBody().getPosition().y < -3) {
			System.out.println("Loss of a life");
			lives--;
			FileHandle handle = Gdx.files.local("maps/" + level + ".txt");
			String[] vals = handle.readString().split("\n");
			float orix = Float.parseFloat(vals[0]);
			float oriy = Float.parseFloat(vals[1]);
			float x = Float.parseFloat(vals[2]);
			float y = Float.parseFloat(vals[3]);
			System.out.println(x + " " + y);
			startX = x;
			startY = y;
			if (x == orix && y == oriy) {
				createPlayer(B2DVars.BIT_YELLOW, new Texture("images/playerImages/yellow.png"));
			} else {
				createPlayer(player.getBody().getFixtureList().first().getFilterData().maskBits, player.getTexture());
			}
		}
		if (lives == 0) {
			FileHandle handle = Gdx.files.local("maps/" + level + ".txt");
			String[] vals = handle.readString().split("\n");
			float x = Float.parseFloat(vals[0]);
			float y = Float.parseFloat(vals[1]);
			handle.writeString(x + "\n" + y + "\n" + x + "\n" + y, false);
			gsm.setState(GameStateManager.play, level);
		}

		if (keyPressed) {
			player.update(dt);
		} else {
			if (player.isAnimationFinished() == false) {
				player.update(dt);

			}
		}

	}

	@Override
	public void render() {
		try {
			sfxLevel = preferences.getFloat("sfx");
		}catch(Exception e) {
			sfxLevel = .1f;
		}
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		sb.begin();
		sb.draw(background, 0, 0, 0, 0, Game.width * Game.scale, Game.height * Game.scale);
		sb.end();
		mainCamera.position.set(player.getPosition().x * PPM, player.getPosition().y * PPM, 0);
		mainCamera.zoom = .75f;
		mainCamera.update();
		// Draw tile map
		tmr.setView(mainCamera);
		tmr.render();

		sb.setProjectionMatrix(mainCamera.combined);
		player.render(sb);

		sb.setProjectionMatrix(hudCamera.combined);
		if (lives == 3) {
			hud.render(sb);
		} else if (lives == 2) {
			hud.renderTwo(sb);
		} else if (lives == 1) {
			hud.renderOne(sb);
		} else {
			hud.renderNone(sb);
		}
		// TODO Auto-generated method stub
		if (debug)
			b2dr.render(world, b2dcam.combined);

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void createPlayer(short bits, Texture tex) {
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		// Player
		bdef.position.set(startX / PPM, startY / PPM);
		bdef.type = BodyType.DynamicBody;
		Body body = world.createBody(bdef);
		shape.setAsBox(14 / PPM, 14 / PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = (short) (bits | 1);
		body.createFixture(fdef).setUserData("Box");

		// Foot Sensor
		shape.setAsBox(8 / PPM, 2 / PPM, new Vector2(0, -15 / PPM), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = (short) (bits | 1);
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("foot");

		player = new Player(body, tex);
		body.setUserData(player);
	}

	public void createTiles() {
		System.out.println("Path to File - maps/" + level + ".tmx");
		tileMap = new TmxMapLoader().load("maps/" + level + ".tmx");
		tmr = new OrthogonalTiledMapRenderer(tileMap);
		MapProperties prop = tileMap.getProperties();
		tileSize = prop.get("tilewidth", Integer.class);
		TiledMapTileLayer layer;
		layer = (TiledMapTileLayer) tileMap.getLayers().get("yellow");
		createLayer(layer, B2DVars.BIT_YELLOW);
		layer = (TiledMapTileLayer) tileMap.getLayers().get("white");
		createLayer(layer, B2DVars.BIT_WHITE);
		layer = (TiledMapTileLayer) tileMap.getLayers().get("red");
		createLayer(layer, B2DVars.BIT_RED);
		layer = (TiledMapTileLayer) tileMap.getLayers().get("purple");
		createLayer(layer, B2DVars.BIT_PURPLE);
		layer = (TiledMapTileLayer) tileMap.getLayers().get("orange");
		createLayer(layer, B2DVars.BIT_ORANGE);
		layer = (TiledMapTileLayer) tileMap.getLayers().get("green");
		createLayer(layer, B2DVars.BIT_GREEN);
		layer = (TiledMapTileLayer) tileMap.getLayers().get("blue");
		createLayer(layer, B2DVars.BIT_BLUE);
	}

	public void createLayer(TiledMapTileLayer layer, short bits) {
		System.out.println(bits);
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		for (int row = 0; row < layer.getHeight(); row++) {
			for (int col = 0; col < layer.getWidth(); col++) {
				Cell cell = layer.getCell(col, row);
				if (cell == null || cell.getTile() == null) {
					continue;
				}
				bdef.type = BodyType.StaticBody;
				bdef.position.set((col + 0.5f) * tileSize / PPM, (row + 0.5f) * tileSize / PPM);
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[5];
				v[0] = new Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM);
				v[1] = new Vector2(-tileSize / 2 / PPM, tileSize / 2 / PPM);
				v[2] = new Vector2(tileSize / 2 / PPM, tileSize / 2 / PPM);
				v[3] = new Vector2(tileSize / 2 / PPM, -tileSize / 2 / PPM);
				v[4] = new Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM);
				cs.createChain(v);
				fdef.shape = cs;
				fdef.filter.categoryBits = bits;
				fdef.filter.maskBits = B2DVars.BIT_PLAYER | 1;
				fdef.isSensor = false;
				fdef.friction = .7f;

				world.createBody(bdef).createFixture(fdef).setUserData("block");

			}
		}
	}

	public void createDoor() {
		TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get("door");
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		for (int row = 0; row < layer.getHeight(); row++) {
			for (int col = 0; col < layer.getWidth(); col++) {
				Cell cell = layer.getCell(col, row);
				if (cell == null || cell.getTile() == null) {
					continue;
				}
				bdef.type = BodyType.StaticBody;
				bdef.position.set((col + 0.5f) * tileSize / PPM, (row + 0.5f) * tileSize / PPM);
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[4];
				v[0] = new Vector2(-24 / PPM, -24 / PPM);
				v[1] = new Vector2(24 / PPM, -24 / PPM);
				v[2] = new Vector2(-24 / PPM, 24 / PPM);
				v[3] = new Vector2(24 / PPM, 24 / PPM);
				cs.createChain(v);
				fdef.shape = cs;
				fdef.isSensor = true;
				world.createBody(bdef).createFixture(fdef).setUserData("door");

			}
		}
	}

	private void switchBlocks(short current, short change, String texture) {
		Filter filter = player.getBody().getFixtureList().first().getFilterData();
		short bits = filter.maskBits;
		bits &= ~current;
		bits |= change;
		bits |= 1;
		player.setTexture(texture);
		System.out.println(bits);
		filter.maskBits = bits;
		System.out.println(bits);
		player.getBody().getFixtureList().first().setFilterData(filter);
		player.getBody().getFixtureList().get(1).setFilterData(filter);
	}

	public void createPlates() {
		String[] plates = new String[] { "blueplate", "greenplate", "orangeplate", "purpleplate", "redplate",
				"yellowplate" };
		for (String plate : plates) {
			System.out.println(plate);
			TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get(plate);

			BodyDef bdef = new BodyDef();
			FixtureDef fdef = new FixtureDef();
			for (int row = 0; row < layer.getHeight(); row++) {
				for (int col = 0; col < layer.getWidth(); col++) {
					Cell cell = layer.getCell(col, row);
					if (cell == null || cell.getTile() == null) {
						continue;
					}
					bdef.type = BodyType.StaticBody;
					bdef.position.set((col + 0.5f) * tileSize / PPM, (row + 0.15f) * tileSize / PPM);
					ChainShape cs = new ChainShape();
					Vector2[] v = new Vector2[4];
					v[0] = new Vector2(-12 / PPM, -3 / PPM);
					v[1] = new Vector2(12 / PPM, -3 / PPM);
					v[2] = new Vector2(-12 / PPM, 3 / PPM);
					v[3] = new Vector2(12 / PPM, 3 / PPM);
					cs.createChain(v);
					fdef.shape = cs;
					fdef.isSensor = true;
					world.createBody(bdef).createFixture(fdef).setUserData(plate);
				}
			}
		}
	}

	public void createCheckpoints() {
		TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get("checkpoint");

		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		for (int row = 0; row < layer.getHeight(); row++) {
			for (int col = 0; col < layer.getWidth(); col++) {
				Cell cell = layer.getCell(col, row);
				if (cell == null || cell.getTile() == null) {
					continue;
				}
				bdef.type = BodyType.StaticBody;
				bdef.position.set((col + 0.5f) * tileSize / PPM, (row + 0.5f) * tileSize / PPM);
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[4];
				v[0] = new Vector2(-16 / PPM, -16 / PPM);
				v[1] = new Vector2(16 / PPM, -16 / PPM);
				v[2] = new Vector2(-16 / PPM, 16 / PPM);
				v[3] = new Vector2(16 / PPM, 16 / PPM);
				cs.createChain(v);
				fdef.shape = cs;
				fdef.isSensor = true;
				world.createBody(bdef).createFixture(fdef).setUserData("checkpoint");
			}
		}
	}

}
