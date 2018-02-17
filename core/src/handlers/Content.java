package handlers;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Content {
	
	private HashMap<String, Texture> textures;
	Logger logger = new Logger();
	public Content() {
		textures = new HashMap<String, Texture>();
	}
	public void loadTexture(String path, String key) {
		Texture tex = new Texture(Gdx.files.internal(path));
		textures.put(key, tex);
		logger.writeEvent("Texture" + key +"  has been loaded");
	}
	public Texture getTexture(String key) {
		logger.writeEvent("Texture" + key +"  has been retrieved");
		return textures.get(key);
		
	}
	public void dispose (String key) {
		logger.writeEvent("Texture" + key +"  has been disposed");
		Texture tex = textures.get(key);
		if(tex != null) {
			tex.dispose();
		}
	} 

}
