package com.grovix.tictactoe.field;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class FieldMatrix {
	private int field_size = 49;
	public boolean[][] is_filled = new boolean[field_size][field_size];
	//x - true, o - false
	public boolean[][] is_cross = new boolean[field_size][field_size];
	public boolean isFieldEmpty;
	public FieldScreen fieldScreen;
	List<Vector2> winList;
	List<Vector2> moveList;
	
	public FieldMatrix(FieldScreen f_screen){
		fieldScreen = f_screen;
		for(int i =0; i < field_size; i++){
			for(int j =0; j < field_size; j++){
				is_filled[i][j] = false;
			}
		}
		isFieldEmpty = true;
		winList = new ArrayList<Vector2>();
		moveList = new ArrayList<Vector2>();
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
		winList.clear();
		boolean token = false;
		int counter = 0;
		int j = (y-4 >= 0)?(y-4):0;
		for(int i = (x-4 >= 0)?(x-4):0; 
		(i <= ((x+4 < field_size)?(x+4):field_size)) && (j <= ((y + 4 < field_size)?(y+4):field_size));++i){
			if(is_filled[i][j]){
				if(token == is_cross[i][j]){
					winList.add(new Vector2(i,j));
					counter++;
				}
				else{
					winList.clear();		
					winList.add(new Vector2(i,j));
					counter = 1;
					token = is_cross[i][j];
				}
			}
			j++;
			if(counter == 5){
				if(token == Args.zero)
					return 1;
				else
					return 2;
			}
		}
		
		winList.clear();		
		counter = 0;
		j = ((y + 4 < field_size)?(y+4):field_size);
		for(int i = (x-4 >= 0)?(x-4):0; 
		(i <= ((x+4 < field_size)?(x+4):field_size)) && (j >= ((y-4 >= 0)?(y-4):0));++i){
			if(is_filled[i][j]){
				if(token == is_cross[i][j]){
					winList.add(new Vector2(i,j));
					counter++;
				}
				else{
					winList.clear();		
					winList.add(new Vector2(i,j));
					counter = 1;
					token = is_cross[i][j];
				}
			}
			j--;
			if(counter == 5){
				if(token == Args.zero)
					return 1;
				else
					return 2;
			}
		}
		
		winList.clear();		
		counter = 0;
		j = y;
		for(int i = (x-4 >= 0)?(x-4):0; 
		(i <= ((x+4 < field_size)?(x+4):field_size));++i){
			if(is_filled[i][j]){
				if(token == is_cross[i][j]){
					winList.add(new Vector2(i,j));
					counter++;
				}
				else{
					winList.clear();		
					winList.add(new Vector2(i,j));
					counter = 1;
					token = is_cross[i][j];
				}
			}
			if(counter == 5){
				if(token == Args.zero)
					return 1;
				else
					return 2;
			}
		}
		
		winList.clear();		
		counter = 0;
		int i = x;
		j = (y-4 >= 0)?(y-4):0;
		for(;(j <= ((y+4 < field_size)?(y+4):field_size));++j){
			if(is_filled[i][j]){
				if(token == is_cross[i][j]){
					winList.add(new Vector2(i,j));
					counter++;
				}
				else{
					winList.clear();		
					winList.add(new Vector2(i,j));
					counter = 1;
					token = is_cross[i][j];
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

	public List<Vector2> getWinList(){
		return winList;
	}
	
	public void clear(){
		moveList.clear();
		for(int i =0; i < field_size; ++i){
			for(int j =0; j < field_size; ++j){
				is_filled[i][j] = false;
			}
		}
	}

}
