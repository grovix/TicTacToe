package com.grovix.tictactoe.controllers;

import com.grovix.tictactoe.field.FieldMatrix;

public class SimpleController {
	
	FieldMatrix field;
	
	public SimpleController(FieldMatrix in_field){
		field = in_field;
	}
	
	public void do_move(boolean figure){	
		field.fieldScreen.set_access_token(true);
	}

}
