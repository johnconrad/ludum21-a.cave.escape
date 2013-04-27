package info.johnconrad.game.ludum21.entities;

import info.johnconrad.game.engine.GameEngine;
import info.johnconrad.game.engine.graphics.Animation;
import info.johnconrad.game.engine.tilemap.entities.WalkingEntity;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Arrays;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.Texture;

public class Skelleton extends WalkingEntity {
	private static Texture texture = null;
	
	public Skelleton() {
		setAttributes();
		loadResources();
	}
	
	private void setAttributes() {
		sizeInTiles = new Dimension(2, 4);
		walkingSpeed = 20f;
		jumpStrength = 20f;
		maxHealth = 6;
		fallDistanceForDamage = 13;
		damageGracePeriod = 500;
	}

	@Override
	public void walk(float speed) {
		super.walk(speed);
	}
	
	@Override
	public void attack() {
		super.attack();
		
		if (dead) return;
		
		Bullet bullet = new Bullet(direction == Direction.RIGHT);
		
		Vector2f pos = new Vector2f(position);
		pos.y += 1.45f;
		if (direction == Direction.RIGHT)
			pos.x += sizeInTiles.width + 1;
		else 
			pos.x -= 1;

		bullet.setPosition(pos);
		
		if (aim == Aim.UP) bullet.getVelocity().y += 15;
		else if (aim == Aim.DOWN) bullet.getVelocity().y -= 40;
		
		map.getEntities().add(bullet);
		bullet.setMap(map);
	}
	
	@Override
	public void die() {
		super.die();
		sizeInTiles = new Dimension(2, 1);
	}
	@Override 
	public void reset() {
		super.reset();
		sizeInTiles = new Dimension(2, 4);
	}
	
	@Override
	public void render(long timeElapsed) {
		super.render(timeElapsed);
	}
	
	@Override
	public void update(long timeElapsed) {
		super.update(timeElapsed);
		
		// for live debuging. REMOVE LATER!
		//setAttributes();
	}

	@Override
	protected void loadResources() {
		super.loadResources();
		
		if (texture == null) {
			System.out.println("Missing texture for the skelleton!");
			return;
		}
		
		standingAnimation = new Animation(texture, 0, 248, 40, 80, 1, 1000);
		walkingAnimation = new Animation(texture, 0, 248, 40, 80, 5, 800);
		walkingAnimation.getFrameOrder().clear();
		walkingAnimation.getFrameOrder().addAll(Arrays.asList(new Integer[] {0, 1, 2, 1, 0, 3, 4, 3}));
		
		attackAnimation = new Animation(texture, 0, 248, 40, 80, 9, 400);
		attackAnimation.getFrameOrder().clear();
		attackAnimation.getFrameOrder().addAll(Arrays.asList(new Integer[] {4, 8, 8, 4}));
		
		deathAnimation = new Animation(texture, 210, 248, 40, 80, 3, 300);
		deathAnimation.getFrameOrder().clear();
		deathAnimation.getFrameOrder().addAll(Arrays.asList(new Integer[] {0, 1, 2}));

		try {
			attackSound = AudioLoader.getAudio("WAV", GameEngine.class.getResourceAsStream("/music/shot.wav"));
			deathSound = AudioLoader.getAudio("WAV", GameEngine.class.getResourceAsStream("/music/thud.wav"));
		} catch (IOException e) {
			System.out.println("Failed loading gun shot sound effect!");
		}

	}

	public static void setTexture(Texture texture) {
		Skelleton.texture = texture;
	}
}
