package com.machinations.game;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.machinations.game.Machinations;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		Graphics.DisplayMode displayMode = Lwjgl3ApplicationConfiguration.getDisplayMode();

		// Set windowed mode to your screen resolution
		config.setWindowedMode(displayMode.width, displayMode.height);

		// Remove window borders for borderless fullscreen look
		config.setDecorated(false);

		// Position window top-left corner
		config.setWindowPosition(0, 0);

		config.setForegroundFPS(60);
		config.setTitle("MOTSP");

		// Disable fullscreen mode
		config.setFullscreenMode(null);

		new Lwjgl3Application(new Machinations(), config);
	}
}