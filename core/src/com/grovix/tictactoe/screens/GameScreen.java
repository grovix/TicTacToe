package com.grovix.tictactoe.screens;

import com.badlogic.gdx.Screen;
import com.grovix.tictactoe.field.FieldScreen;

public class GameScreen implements Screen {
	FieldScreen field;
	boolean AI;
	
	public GameScreen(boolean is_AI_on){
		AI = is_AI_on;
	}
	public void render(float delta) {
		field.render();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		field = new FieldScreen(AI);
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
