package com.bpa.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import main.Game;

public class DesktopLauncher { 

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new Game();
		config.title = Game.title;
		config.width = Game.width * Game.scale;
		config.height = Game.height * Game.scale;
		config.fullscreen = false;
		new LwjglApplication(new Game(), config);
	}
}
	