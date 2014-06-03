package com.grovix.tictactoe.screens;

import com.badlogic.gdx.Screen;
import com.grovix.tictactoe.field.Field;

public class GameScreen implements Screen {
	Field field;
	@Override
	public void render(float delta) {
		field.render();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		field = new Field();
		field.create();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
