package com.grovix.tictactoe;

import com.badlogic.gdx.Game;
import com.grovix.tictactoe.screens.MainMenu;

public class MyGame extends Game {
	
	public static final String TITLE = "AwesomeStickman", VERSION = "0.5.0.0";

	
	@Override
	public void create () {
		setScreen(new MainMenu());	
	}
	@Override
	public void render () {
		super.render();
	}
}
