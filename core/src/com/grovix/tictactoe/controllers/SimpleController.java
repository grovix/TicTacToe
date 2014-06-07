package com.grovix.tictactoe.controllers;

import com.badlogic.gdx.math.Vector2;
import com.grovix.tictactoe.field.FieldMatrix;

public class SimpleController {
	
	FieldMatrix field;
	
	public SimpleController(FieldMatrix in_field){
		field = in_field;
	}
	
	public void do_move(boolean figure){	
		int x =0,y = 0;
		
		field.moveList.add(new Vector2(x,y));
		field.fieldScreen.set_access_token(true);
	}

}
