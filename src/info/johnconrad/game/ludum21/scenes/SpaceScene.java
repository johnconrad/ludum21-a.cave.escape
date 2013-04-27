package info.johnconrad.game.ludum21.scenes;

import info.johnconrad.game.engine.GameEngine;
import info.johnconrad.game.engine.graphics.Sprite;
import info.johnconrad.game.engine.graphics.Sprite.SpriteColor;
import info.johnconrad.game.engine.scenes.Scene;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class SpaceScene extends Scene {
	private static final int DELAY_TIME = 2000;
	private static final int FADE_IN_TIME = 500;

	long startTime;
	
	boolean begun = false;
	boolean fadeStarted = false;
	
	
	Texture spaceTexture;
	Sprite space;
	
	@Override
	public void render(long timeElapsed) {
		beginOrthoRender();
		space.render(timeElapsed);
		endOrthoRender();
	}

	@Override
	public void update(long timeElapsed) {
		long now = GameEngine.getTime();
		
		if (!begun) {
			startTime = now;
			begun = true;
		}
		
		if (!fadeStarted && (now - startTime) > DELAY_TIME) {
			fadeStarted = true;
			space.fadeToColor(new SpriteColor(1f, 1f, 1f, 1f), FADE_IN_TIME);
		}
		
		space.update(timeElapsed);
	}

	@Override
	public void processInput() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadResources() {
		try {
			spaceTexture = TextureLoader.getTexture("PNG", GameEngine.class.getResourceAsStream("/images/interstellar.png"));
			space = new Sprite(spaceTexture, 0.0f, 0.0f, 640f / 1024f, 480f / 1024f);
		    
			space.setSize(new Vector2f(640, 480));
			space.setPosition(new Vector2f(0, 0));
			space.setCentered(false);
			space.setColor(new SpriteColor(1f, 1f, 1f, 0f));
			
		    
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void freeResources() {

	}

}
