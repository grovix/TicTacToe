package com.grovix.tictactoe.field;

public class FieldMatrix {
	private int field_size = 49;
	private boolean[][] is_filled = new boolean[field_size][field_size];
	//x - true, o - false
	private boolean[][] is_cross = new boolean[field_size][field_size];
	public boolean isFieldEmpty;
	
	public FieldMatrix(){
		for(int i =0; i < field_size; i++){
			for(int j =0; j < field_size; j++){
				is_filled[i][j] = false;
			}
		}
		isFieldEmpty = true;
	}
	
	public void fillCell(int x, int y, boolean cell_type){
		if(isFieldEmpty){
			is_filled[x][y] = true;
			is_cross[x][y] = cell_type;
			isFieldEmpty = false;
		}
		else if(!is_filled[x][y]){
			boolean is_near_filled = false;
			for(int i = (x - 1 >= 0)?(x-1):0; i <= ((x + 1 < field_size)?(x+1):field_size); ++i){
				for(int j = (y - 1 >= 0)?(y-1):0; j <= ((y+1 < field_size)?(y+1):field_size); ++j){
					is_near_filled|=is_filled[i][j];
				}
			}
			if(is_near_filled){
				is_filled[x][y]= true;
				is_cross[x][y]=cell_type;
			}
		}
	}
	
	public int checkWin(int x, int y){
		boolean token = false;
		int counter = 0;
		for(int i = (x-4 >= 0)?(x-4):0; i <= ((x+4 < field_size)?(x+4):field_size);++i){
			if(is_filled[i][i]){
				if(token == is_cross[i][i])
					counter++;
				else{
					counter = 1;
					token = is_cross[i][i];
				}
			}
			if(counter == 5){
				if(token == Args.zero)
					return 1;
				else
					return 2;
			}
		}
		
		return 0;
	}
	

}
