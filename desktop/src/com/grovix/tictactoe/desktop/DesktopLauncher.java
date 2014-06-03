package com.grovix.tictactoe.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.grovix.tictactoe.MyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 640;
		config.height = 640;
		config.resizable = false;
		config.useGL30 = true;
		config.title = "TicTacToe";
		new LwjglApplication(new MyGame(), config);
	}
}
