package com.grovix.tictactoe.field;

import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.grovix.tictactoe.InputController;
import com.grovix.tictactoe.controllers.SimpleController;
import com.grovix.tictactoe.screens.MainMenu;

public class FieldScreen implements ApplicationListener {
	
	private Viewport viewport;
	private OrthographicCamera camera;
	private Vector2 local_coord;
	private float stepX;
	private float stepY;
	private float zoomStep = 0.1f;
	private float cellWidth = 84.00551f;
	private float wallWidth = 8.100096f;
	private float startX = -42.0f;
	private float startY = 43.799927f;
	private int[] numCellsFromCenter = new int[4];
	private boolean access_token = true;
	private FieldMatrix field;
	public boolean figure = Args.cross;
	private Vector2[][] cellCords;
	public int field_size = 40;
	public boolean AI;
	public SimpleController ai_controller;
	public int check = 0;
	BitmapFont font;
	String str;
	public boolean is_highlighted = false;
	
    public class MyActor extends Actor {
        Texture texture = new Texture(Gdx.files.internal("img/field.jpg"));
        Texture zero_usual = new Texture(Gdx.files.internal("img/zero_usual.png"));
        Texture zero_win = new Texture(Gdx.files.internal("img/zero_win.png"));
        Texture cross_usual = new Texture(Gdx.files.internal("img/cross_usual.png"));
        Texture cross_win = new Texture(Gdx.files.internal("img/cross_win.png"));

        float actorX = 0, actorY = 0;
        public boolean started = false;

        public MyActor(){
            setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        }
        
        
        @Override
        public void draw(Batch batch, float alpha){
            batch.draw(texture,-Gdx.graphics.getWidth()/2,-Gdx.graphics.getHeight()/2);
            if(check == 0){
	            if(figure){
	            	str = "X";
	            	font.setColor(Color.RED);
	            }
	            else{
	            	str = "0";
	            	font.setColor(Color.BLUE);
	            }
            }
            font.setScale(5f*camera.zoom/5);
            for(int i =0; i < field_size -1; ++i){
            	for(int j = 0; j < field_size - 1; ++j){
            		if(field.is_filled[i][j]){
            			if(field.is_cross[i][j]){
            	            batch.draw(cross_usual,cellCords[i][j].x + cellWidth/2 + 1403f,
            	            		cellCords[i][j].y - cellWidth/2 + 1425f);
            			}
            			else{
            	            batch.draw(zero_usual,cellCords[i][j].x + cellWidth/2 + 1400f,
            	            		cellCords[i][j].y - cellWidth/2 + 1428f);
            			}
            		}
            	}
            	
            	if(is_highlighted){
            		int size = field.moveList.size();
            		int tX=0, tY=0;
            		if(size > 0){
            			tX = (int)field.moveList.get(size -1).x;
            			tY = (int)field.moveList.get(size -1).y;
            			if(field.is_cross[tX][tY]){
            	            batch.draw(cross_win,cellCords[tX][tY].x + cellWidth/2 + 1403f,
            	            		cellCords[tX][tY].y - cellWidth/2 + 1425f);
            			}
            			else{
            	            batch.draw(zero_win,cellCords[tX][tY].x + cellWidth/2 + 1400f,
            	            		cellCords[tX][tY].y - cellWidth/2 + 1428f);
            			}
            			if(size > 1){
                			tX = (int)field.moveList.get(size -2).x;
                			tY = (int)field.moveList.get(size -2).y;
                			if(field.is_cross[tX][tY]){
                	            batch.draw(cross_win,cellCords[tX][tY].x + cellWidth/2 + 1403f,
                	            		cellCords[tX][tY].y - cellWidth/2 + 1425f);
                			}
                			else{
                	            batch.draw(zero_win,cellCords[tX][tY].x + cellWidth/2 + 1400f,
                	            		cellCords[tX][tY].y - cellWidth/2 + 1428f);
                			}
            			}
            		}
            	}
            }
            if(check != 0){
            	List<Vector2> winList = field.getWinList();
            	for(int i =0; i< winList.size(); ++i){
            		if(check == 1){
            			int j =(int) winList.get(i).x;
            			int k = (int) winList.get(i).y;
        	            batch.draw(zero_win,cellCords[j][k].x + cellWidth/2 + 1400f,
        	            		cellCords[j][k].y - cellWidth/2 + 1428f);
            		}
            		else{
            			int j =(int) winList.get(i).x;
            			int k = (int) winList.get(i).y;
        	            batch.draw(cross_win,cellCords[j][k].x + cellWidth/2 + 1403f,
        	            		cellCords[j][k].y - cellWidth/2 + 1425f);
            		}
            	}
            	if(check == 1){
	            	str = "0 win!";
	            	font.setColor(Color.BLUE);
            	}
            	else{
	            	str = "X win!";
	            	font.setColor(Color.RED);
            	}
            }
            font.draw(batch, str, camera.position.x - 1400*camera.zoom/5, camera.position.y+1400*camera.zoom/5);
        }
    }
    
    private Stage stage;
    
    public FieldScreen(boolean is_AI_on){
    	AI = is_AI_on;
    }

    @Override
	public void render() {    
		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(check != 0){
			access_token = false;
		}
		camera.update();
	    stage.act(Gdx.graphics.getDeltaTime());
	    stage.draw();
	}
    
    @Override
    public void create() { 
    	numCellsFromCenter[Args.numFromStart.up] = 19;
    	numCellsFromCenter[Args.numFromStart.left] = 19;
    	numCellsFromCenter[Args.numFromStart.right] = 19;
    	numCellsFromCenter[Args.numFromStart.down] = 19;
    	
    	initializeCoordArray();

    	field = new FieldMatrix(this);
    	camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	viewport = new ScreenViewport(camera);
        stage = new Stage(viewport);
        final MyActor myActor = new MyActor();
        myActor.setTouchable(Touchable.enabled);
        stage.addActor(myActor);
        ai_controller = new SimpleController(field);
        camera.position.set(1470.0f, 1480.0f, 0);
        camera.zoom = 5.5f;
        stepX = 0;
        stepY = 0;
        
		font = new BitmapFont(Gdx.files.internal("font/white_64.fnt"));
		font.setColor(Color.BLACK);
		font.setScale(3f);
		
        Gdx.input.setInputProcessor(new InputController(){
        	@Override
        	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        		local_coord = myActor.screenToLocalCoordinates(new Vector2(screenX,screenY));
        		float cX,cY;
        		cX = local_coord.x - camera.position.x - stepX;
        		cY = local_coord.y - camera.position.y - stepY;
          		Vector2 h = getIndex(cX, cY);
          		mouseClickAction(h);
        		return true;
        	};
        	@Override
        	public boolean scrolled(int amount) {
        		camera.zoom+=(amount*zoomStep);
        		if(camera.zoom < zoomStep)
        			camera.zoom = zoomStep;
        		return true;
        	}
        	
        	@Override
        	public boolean keyTyped(char character) {
        		float step = 50;
        		switch(character){
        		case 's':
        			camera.position.set(camera.position.x, camera.position.y - step, 0);
        			stepY += step;
        			break;
				case 'w':
					camera.position.set(camera.position.x, camera.position.y + step, 0);
					stepY -= step;
					break;
				case 'a':
					camera.position.set(camera.position.x - step, camera.position.y, 0);
					stepX += step;
					break;
				case 'd':
					stepX -=step;
					camera.position.set(camera.position.x +step, camera.position.y, 0);
					break;
        		case 'v':
        	        camera.position.set(1470.0f, 1480.0f, 0);
        	        camera.zoom = 5.5f;
        	        stepY = 0;
        	        stepX = 0;
        			break;
        		case 'r':
        			reset();
        			break;
        		case 'f':
        			if(access_token || check != 0){
	        			field.stepBack();
	        			check = 0;
	        			access_token = true;
        			}
        			break;
        		case 'b':
        			ai_controller.findFreeCells();
        		case 'z':
	        		if(AI){
	        			if(!field.isFieldEmpty){
		        			figure =! figure;
		        			ai_controller.AI_figure = figure;
		        			access_token = false;
		        			ai_controller.do_move(figure);
		        			if(check == 0){
		        				figure = !figure;
		        			}
	        			}
	        			else{
		        			access_token = false;
	        				field.fillCell(20, 20, figure);
	        				figure = !figure;
		        			access_token = true;
	        			}
	        		}
        			break;
        		}
        		return true; 	
        	}
        	
        	@Override
        	public boolean keyDown(int keycode) {
        		// TODO Auto-generated method stub
        		switch(keycode){
        		case Keys.ESCAPE:
        			
					((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
        			break;
        		case Keys.C:
        			is_highlighted = true;
        			break;
        		}
        		return true;
        	}
        	
        	public boolean keyUp(int keycode) {
        		switch(keycode){
	    		case Keys.C:
	    			is_highlighted = false;
	    			break;
        		}
        		return true;
        	}
        });
        
    }
    public void mouseClickAction(Vector2 index){
    	if(access_token && !(((int)index.x == 0) || ((int)index.y == 0))){
    		if(!field.is_filled[(int)index.x-1][(int)(index.y-1)]){
    		field.fillCell((int)index.x -1, (int)index.y - 1, figure);
	    		if(field.is_filled[(int)index.x-1][(int)(index.y-1)] && 
	    				field.is_cross[(int)index.x -1][(int)index.y -1] == figure){
	    			check = field.checkWin((int)index.x-1, (int)index.y -1);
	    			if(check == 0){
	    				figure = !figure;
		        		if(AI){
		        			access_token = false;
		        			ai_controller.do_move(figure);
		        			if(check == 0){
		        				figure = !figure;
		        			}
		        		}
	    			}
	    		}
	    	}
    	}
    }
    
    public Vector2 getIndex(float x, float y){
    	int resI = 0; int resJ = 0;
    	boolean flag = false;
    	for(int i = 0; i < field_size && !flag; ++i){
    		for(int j = 0; j < field_size && !flag; ++j){
    			if(x <= cellCords[i][j].x && y >= cellCords[i][j].y){
    				resI = i; resJ = j;
    				flag = true;
    			}
    		}
    	}
    	
    	return new Vector2(resI,resJ);
    }
    
    public void initializeCoordArray(){
    	cellCords = new Vector2[field_size][field_size];
    	int cellI = numCellsFromCenter[Args.numFromStart.left];
    	int cellJ = numCellsFromCenter[Args.numFromStart.up];
    	cellCords[cellI][cellJ] = new Vector2(startX,startY);
    	float currentX = startX;
    	float currentY = startY;
    	for(int i = cellI; i >= 0; i--){
    		for(int j = cellJ; j >= 0; j--){
    			if(i == cellI && j == cellJ){
    				currentY = currentY + (cellWidth+wallWidth);
    			}
    			else{
    				cellCords[i][j] = new Vector2(currentX,currentY);
    				currentY = currentY + (cellWidth+wallWidth);
    			}
    		}
    		currentX = currentX - (cellWidth+wallWidth);
    		currentY = startY;
    	}
    	
    	currentX = cellCords[0][cellJ].x;
    	currentY = cellCords[0][cellJ].y - (cellWidth+wallWidth);
    	for(int i = 0; i <= cellI; i++){
    		for(int j = cellJ + 1; j < field_size; j++){
    			cellCords[i][j] = new Vector2(currentX,currentY);
    			currentY = currentY - (cellWidth+wallWidth);
    		}
    		currentX = currentX + (cellWidth+wallWidth);
        	currentY = cellCords[0][cellJ].y - (cellWidth+wallWidth);
    	}
    	
    	currentX = cellCords[cellI][0].x+(cellWidth+wallWidth);
    	currentY = cellCords[cellI][0].y;
    	for(int i = cellI+1; i < field_size; i++){
    		for(int j = 0; j < field_size; j++){
    			cellCords[i][j] = new Vector2(currentX,currentY);
    			currentY = currentY - (cellWidth+wallWidth);
    		}
    		currentX = currentX + (cellWidth+wallWidth);
        	currentY = cellCords[cellI][0].y;
    	}
    }
    
    public boolean get_access_token(){
    	return access_token;
    }
    
    public void set_access_token(boolean value){
    	access_token = value;
    }
    
    public void reset(){
    	field.clear();
    	field.isFieldEmpty = true;
    	figure = Args.cross;
    	check = 0;
    	access_token = true;
    }

    @Override
    public void dispose() {
    	stage.dispose();
    }


    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
