package com.grovix.tictactoe.field;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.grovix.tictactoe.InputController;

public class Field implements ApplicationListener {
	
	Viewport viewport;
	OrthographicCamera camera;
	Vector2 local_coord;
	Vector2 stage_coord;
    public class MyActor extends Actor {
        Texture texture = new Texture(Gdx.files.internal("img/field.png"));
        float actorX = 0, actorY = 0;
        public boolean started = false;

        public MyActor(){
            setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        }
        
        
        @Override
        public void draw(Batch batch, float alpha){
            batch.draw(texture,-Gdx.graphics.getWidth()/2,-Gdx.graphics.getHeight()/2);
        }
        
    }
    
    private Stage stage;

    @Override
	public void render() {    
		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
	    stage.act(Gdx.graphics.getDeltaTime());
	    stage.draw();
	}
    
    @Override
    public void create() { 
    	camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	viewport = new ScreenViewport(camera);
        stage = new Stage(viewport);
        final MyActor myActor = new MyActor();
        myActor.setTouchable(Touchable.enabled);
        stage.addActor(myActor);
        
        camera.position.set(1470.0f, 1480.0f, 0);
        camera.zoom = 5.5f;
        Gdx.input.setInputProcessor(new InputController(){
        	@Override
        	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        		local_coord = myActor.screenToLocalCoordinates(new Vector2(screenX,screenY));
          		System.out.println(local_coord.x +" "+local_coord.y);

        		return true;
        	};
        	@Override
        	public boolean scrolled(int amount) {
        		camera.zoom+=(amount*0.3f);
        		if(camera.zoom < 0.3f)
        			camera.zoom = 0.3f;
        		return true;
        	}
        	
        	@Override
        	public boolean keyTyped(char character) {
        		float step = +25;
        		switch(character){
        		case 's':
        			camera.position.set(camera.position.x, camera.position.y - step, 0);
        			break;
				case 'w':
					camera.position.set(camera.position.x, camera.position.y + step, 0);
					break;
				case 'a':
					camera.position.set(camera.position.x - step, camera.position.y, 0);
					break;
				case 'd':
					camera.position.set(camera.position.x +step, camera.position.y, 0);
					break;
        		case 'v':
        	        camera.position.set(1470.0f, 1480.0f, 0);
        	        camera.zoom = 5.5f;
        			break;
        		}
        		return true; 	
        	}
        });
        
    }

    @Override
    public void dispose() {
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
