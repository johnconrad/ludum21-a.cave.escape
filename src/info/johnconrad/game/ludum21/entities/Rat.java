package info.johnconrad.game.ludum21.entities;

import info.johnconrad.game.engine.GameEngine;
import info.johnconrad.game.engine.graphics.Animation;
import info.johnconrad.game.engine.tilemap.entities.TileMapEntity;
import info.johnconrad.game.engine.tilemap.entities.WalkingEntity;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Arrays;

import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.Texture;

public class Rat extends WalkingEntity {
	private static Texture texture = null;
	
	public Rat() {
		setAttributes();
		loadResources();
	}
	
	private void setAttributes() {
		sizeInTiles = new Dimension(2, 1);
		walkingSpeed = 20f;
		jumpStrength = 10f;
		maxHealth = 3;
		fallDistanceForDamage = 8;
	}

	@Override
	public void walk(float speed) {
		super.walk(speed);
	}
	
	public void collide(TileMapEntity otherObj) {
		super.collide(otherObj);
		
		if (otherObj instanceof WalkingEntity && velocity.length() != 0) {
			((WalkingEntity)otherObj).damage(1);
			velocity.x = velocity.x / -2;
		}
	}
	
	@Override
	public void render(long timeElapsed) {
		super.render(timeElapsed);
	}
	
	@Override
	public void update(long timeElapsed) {
		super.update(timeElapsed);
		
		// for live debuging. REMOVE LATER!
		setAttributes();
	}

	protected void loadResources() {
		super.loadResources();
		
		if (texture == null) {
			System.out.println("Missing texture for the rat!");
			return;
		}
		
		walkingAnimation = new Animation(texture, 0, 124, 40, 40, 2, 200);
		standingAnimation = new Animation(texture, 0, 124, 40, 40, 1, 200);
		attackAnimation = standingAnimation;
		deathAnimation = new Animation(texture, 84, 124, 40, 40, 3, 1000);
		deathAnimation.getFrameOrder().clear();
		deathAnimation.getFrameOrder().addAll(Arrays.asList(new Integer[] {0, 1, 2, 2, 2, 2, 2, 2}));

		try {
			attackSound = AudioLoader.getAudio("WAV", GameEngine.class.getResourceAsStream("/music/squeek.wav"));
			deathSound = AudioLoader.getAudio("WAV", GameEngine.class.getResourceAsStream("/music/splat.wav"));
		} catch (IOException e) {
			System.out.println("Failed loading mouse squeek sound effect!");
		}
	}

	public static void setTexture(Texture texture) {
		Rat.texture = texture;
	}
}
