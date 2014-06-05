package com.grovix.tictactoe;

import com.badlogic.gdx.Game;
import com.grovix.tictactoe.screens.GameScreen;

public class MyGame extends Game {
	
	public static final String TITLE = "AwesomeStickman", VERSION = "0.5.0.0";

	
	@Override
	public void create () {
		setScreen(new GameScreen(false));	
	}
	@Override
	public void render () {
		super.render();
	}
}
