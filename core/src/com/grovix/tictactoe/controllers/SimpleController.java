package com.grovix.tictactoe.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.grovix.tictactoe.field.Args;
import com.grovix.tictactoe.field.FieldMatrix;

public class SimpleController {
	
	public int zero_wins = 10000;
	public int cross_wins = 1000;
	public int val_factor = 1;
	public int field_size;
	FieldMatrix field;
	public boolean[][] free_cells;
	private int attack_factor = 1;
	public int cell_counter;
	public List<Integer> variants;
	public List<Integer> max_var;
	public List<Vector2> ind_vars;
	Random rand = new Random();
	public boolean AI_figure = Args.zero;

	public SimpleController(FieldMatrix in_field){
		field = in_field;
		field_size = field.fieldScreen.field_size -1;
		free_cells = new boolean[field_size][field_size];
		variants = new ArrayList<Integer>();
		ind_vars = new ArrayList<Vector2>();
		max_var = new ArrayList<Integer>();
	}
	
	public void do_move(boolean figure){	
		findFreeCells();

		for(int i =0; i < field_size; i++){
			for(int j =0; j < field_size; j++){
				if(free_cells[i][j]){
					variants.add(calcWeightFun(i, j, figure)+calcWeightFun(i, j, !figure) * attack_factor);
					ind_vars.add(new Vector2(i,j));
				}
			}
		}
		Integer max= variants.get(0);
		for(int i =1; i < variants.size(); ++i){
			if(variants.get(i) > max)
				max = variants.get(i);
		}
		for(int i = 0; i < variants.size(); ++i){
			if((int)variants.get(i) == (int)max){
				max_var.add(i);
			}
		}
		int rand_ind;
		if(max_var.size() > 1){
			rand_ind = rand.nextInt(max_var.size());
		}
		else
			rand_ind=0;
		field.fillCell((int)ind_vars.get(max_var.get(rand_ind)).x, (int)ind_vars.get(max_var.get(rand_ind)).y, figure);
		int check = field.checkWin((int)ind_vars.get(max_var.get(rand_ind)).x, (int)ind_vars.get(max_var.get(rand_ind)).y);
		if(check == 0)
			field.fieldScreen.set_access_token(true);
		else
			field.fieldScreen.check = check;
		variants.clear();
		max_var.clear();
		ind_vars.clear();
	}

	public int getAttack_factor() {
		return attack_factor;
	}

	public void setAttack_factor(int in_attack_factor) {
		if(in_attack_factor < 1)
			attack_factor = 1;
		else
			attack_factor = in_attack_factor;
	}
	
	public void incAttack_factor(){
		attack_factor++;
	}
	
	public void decAttack_factor(){
		if(attack_factor <= 1)
			attack_factor = 1;
		else
			attack_factor--;
	}
	
	public void findFreeCells(){
		cell_counter =0;
		for(int i =0; i < field_size; ++i)
			for(int j =0; j < field_size; ++j)
				free_cells[i][j] = false;
		for(int i =0; i < field.moveList.size(); ++i){
			int x = (int)field.moveList.get(i).x;
			int y = (int)field.moveList.get(i).y;
			for(int j = ((x - 1 > 0)?x-1:0);j <= ((x+1 <field_size-1)?x+1:field_size-1); j++){
				for(int k = ((y - 1 > 0)?y-1:0);k <= ((y+1 <field_size-1)?y+1:field_size-1); k++){
					if(!field.is_filled[j][k])
						free_cells[j][k]=true;
				}
			}
		}
		
	}
	
	public int calcWeightFun(int x,int y, boolean figure){
		
		int quad = 0;
		field.is_filled[x][y] = true;
		field.is_cross[x][y] = figure;
		int series_length = 0;
		int sum =0;
		int pow_st;
		//сверху вниз
		for(int i =0; i < 5; i++){
			if(x-4+i < 0) continue;
			if ((x+i) > (field_size - 1)) break;
			for (int j=0;j<5;j++){
				if(field.is_filled[x-4+i+j][y]){
					if(field.is_cross[x-4+i+j][y] != figure){
						series_length = 0;
						quad = 0;
						break;
					}
					else{
						series_length++;
						quad++;
					}
				}
				else{
					quad=0;
				}
			}
			if (series_length == 1) series_length = 0;//Ряд из самой клетки не учитываем
			//Плюсуем серию к общей сумме
			pow_st = val_factor;
			if (series_length == 5){
				if (figure == AI_figure)
					pow_st = 20000;//Большое значение при своем выигрыше
				else
					pow_st = 2000; //Большое значение при выигрыше соперника, но меньшее, чем при своем
			}
			else if(quad == 4 && figure == !AI_figure){
				pow_st = 1000;
			}
			else if(quad == 4 && figure == AI_figure){
				pow_st = 1500;
				System.out.println("quad");
			}
			else{
				pow_st=(int)Math.pow(val_factor, series_length);
			}
			sum += pow_st;
			series_length = 0;
			
		}
		quad =0;
///////////Расчет слева направо/////////
		for (int i = 0;i<5;i++){
			if ((y-4+i) < 0) continue;
			if ((y+i) > (field_size - 1)) break;
			for (int j=0;j<5;j++){
				if ((field.is_cross[x][y-4+i+j] != figure) && (field.is_filled[x][y-4+i+j])){
					series_length = 0;
					quad = 0;
					break;
				}
				if (field.is_filled[x][y-4+i+j]){
					series_length++; //Ряд увеличивается
					quad++;
				}
				else
					quad = 0;
			}
			if (series_length == 1) series_length = 0; //Ряд из самой клетки не учитываем
			//Плюсуем серию к общей сумме
			pow_st = val_factor;
			if (series_length == 5){
				if (figure = AI_figure)
					pow_st = 20000;//Большое значение при своем выигрыше
				else
					pow_st = 2000; //Большое значение при выигрыше соперника, но меньшее, чем при своем
			}
			else if(quad == 4 && figure == !AI_figure){
				pow_st = 1000;
			}
			else if(quad == 4 && figure == AI_figure){
				pow_st = 1500;
				System.out.println("quad");
			}
			else{
				pow_st=(int)Math.pow(val_factor, series_length);
			}
			sum += pow_st;
			series_length = 0;
		}
		quad = 0;
///////////Расчет по диагонали с левого верхнего/////////
		for (int i = 0;i<5;i++){
			//Проверка, не вышли ли за границы поля
			if ((y-4+i) < 0) continue;
			if ((x-4+i) < 0) continue;
			if ((x+i) > (field_size - 1)) break;
			if ((y+i) > (field_size - 1)) break;
			//Проход по всем возможным рядам, отстоящим от клетки не более чем на 5
			for (int j=0;j<5;j++){
				if ((field.is_cross[x-4+i+j][y-4+i+j] != figure) && (field.is_filled[x-4+i+j][y-4+i+j])){
				series_length = 0;
				quad = 0;
				break;
				}
				if (field.is_filled[x-4+i+j][y-4+i+j]){
					quad++;
					series_length++; //Ряд увеличивается
				}
				else 
					quad = 0;
			}
			if (series_length == 1) series_length = 0; //Ряд из самой клетки не учитываем
			//Плюсуем серию к общей сумме
			pow_st = val_factor;
			if (series_length == 5){
				if (figure == AI_figure)
					pow_st = 20000;//Большое значение при своем выигрыше
				else
					pow_st = 2000; //Большое значение при выигрыше соперника, но меньшее, чем при своем
			}
			else if(quad == 4 && figure == !AI_figure){
				pow_st = 1000;
			}
			else if(quad == 4 && figure == AI_figure){
				pow_st = 1500;
				System.out.println("quad");
			}
			else{
				pow_st=(int)Math.pow(val_factor, series_length);
			}
			sum += pow_st;
			series_length = 0;
		}
///////////Расчет по диагонали с левого нижнего/////////
//Проход по каждой клетки, которая может входить в ряд
		quad = 0;
		for (int i = 0;i<5;i++){
		//Проверка, не вышли ли за границы поля
			if ((y-4+i) < 0) continue;
			if ((x+4-i) > (field_size - 1)) continue;
			if ((x-i) < 0) break;
			if ((y+i) > (field_size - 1)) break;
			//Проход по всем возможным рядам, отстоящим от клетки не более чем на 5
			for (int j=0;j<5;j++){
				if ((field.is_cross[x+4-i-j][y-4+i+j] != figure) && (field.is_filled[x+4-i-j][y-4+i+j])){
				//Конец ряда
				series_length = 0;
				quad = 0;
				break;
				}
				if (field.is_filled[x+4-i-j][y-4+i+j]) {
					quad++;
					series_length++; //Ряд увеличивается
				}
				else
					quad = 0;
			}
			if (series_length == 1) series_length = 0; //Ряд из самой клетки не учитываем
			//Плюсуем серию к общей сумме
			pow_st = val_factor;
			if (series_length == 5){
				if (figure == AI_figure)
					pow_st = 20000;//Большое значение при своем выигрыше
				else
					pow_st = 2000; //Большое значение при выигрыше соперника, но меньшее, чем при своем
			}
			else if(quad == 4 && figure == !AI_figure){
				pow_st = 1000;
			}
			else if(quad == 4 && figure == AI_figure){
				pow_st = 1500;
				System.out.println("quad");
			}
			else{
				pow_st=(int)Math.pow(val_factor, series_length);
			}
			sum += pow_st;
			series_length = 0;
		}
//Возвращаем исходное значение
		field.is_filled[x][y] = false;	
		return sum;
	}
	
	public double getRandomDouble(int Min, int Max){
		return Min + (int)(Math.random() * ((Max - Min) + 1));
	}
	
}
