package com.grovix.tictactoe.field;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class FieldMatrix {
	public int field_size = 39;
	public boolean[][] is_filled = new boolean[field_size][field_size];
	//x - true, o - false
	public boolean[][] is_cross = new boolean[field_size][field_size];
	public boolean isFieldEmpty;
	public FieldScreen fieldScreen;
	List<Vector2> winList;
	public List<Vector2> moveList;
	
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
			moveList.add(new Vector2(x,y));
		}
		else if(!is_filled[x][y]){
			boolean is_near_filled = false;
			for(int i = (x - 1 >= 0)?(x-1):0; i <= ((x + 1 < field_size)?(x+1):field_size-1); ++i){
				for(int j = (y - 1 >= 0)?(y-1):0; j <= ((y+1 < field_size)?(y+1):field_size-1); ++j){
					is_near_filled|=is_filled[i][j];
				}
			}
			if(is_near_filled){
				is_filled[x][y]= true;
				is_cross[x][y]=cell_type;
				moveList.add(new Vector2(x,y));
			}
		}
	}
	
	public int checkWin(int x, int y){
		winList.clear();
		boolean token = false;
		int counter = 0;
		int j = (y-4 >= 0)?(y-4):0;
		for(int i = (x-4 >= 0)?(x-4):0; 
		(i <= ((x+4 < field_size)?(x+4):field_size-1)) && (j <= ((y + 4 < field_size)?(y+4):field_size-1));++i){
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
			else counter = 0;
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
		j = ((y + 4 < field_size)?(y+4):field_size-1);
		for(int i = (x-4 >= 0)?(x-4):0; 
		(i <= ((x+4 < field_size)?(x+4):field_size-1)) && (j >= ((y-4 >= 0)?(y-4):0));++i){
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
			else counter = 0;
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
		(i <= ((x+4 < field_size)?(x+4):field_size-1));++i){
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
			else counter = 0;
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
		for(int k = (y-4 >= 0)?(y-4):0;(k <= ((y+4 < field_size)?(y+4):field_size-1));++k){
			if(is_filled[i][k]){
				if(token == is_cross[i][k]){
					winList.add(new Vector2(i,k));
					counter++;
				}
				else{
					winList.clear();		
					winList.add(new Vector2(i,k));
					counter = 1;
					token = is_cross[i][k];
				}
			}
			else counter = 0;
			if(counter == 5){
				if(token == Args.zero)
					return 1;
				else
					return 2;
			}
		}
		winList.clear();		
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
	
	public void stepBack(){
		int tX=0, tY=0;
		int size = moveList.size();
		if(size != 0){
			tX = (int)moveList.get(size -1).x;
			tY = (int)moveList.get(size -1).y;
			is_filled[tX][tY] = false;
			if(size -1 >= 1){
				tX = (int)moveList.get(size -2).x;
				tY = (int)moveList.get(size -2).y;
				is_filled[tX][tY] = false;
			}
		}
		if(size != 0){
			moveList.remove(size-1);
			if(size - 1 >= 1){
				moveList.remove(size - 2);
			}
		}
		if(moveList.size() == 0)
			isFieldEmpty = true;
	}

}
