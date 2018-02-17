package handlers;

import java.io.File;

import com.badlogic.gdx.files.FileHandle;

public class Logger {
	FileHandle errorHandler;
	FileHandle eventHandler;

	public Logger() {
		errorHandler = new FileHandle(new File("errors.txt"));
		eventHandler = new FileHandle(new File("events.txt"));
	}

	public void writeError(String message) {
		errorHandler.writeString("Error at time " + System.currentTimeMillis() + " : " + message, true);
	}

	public void writeEvent(String message) {
		eventHandler.writeString("Event at time " + System.currentTimeMillis() + " : " + message +"\n", true);
	}


}
