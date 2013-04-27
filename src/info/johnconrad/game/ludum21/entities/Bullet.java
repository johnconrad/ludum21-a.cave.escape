package info.johnconrad.game.ludum21.entities;

import info.johnconrad.game.engine.tilemap.entities.FreeMovingEntity;
import info.johnconrad.game.engine.tilemap.entities.TileMapEntity;
import info.johnconrad.game.engine.tilemap.entities.WalkingEntity;

import java.awt.Dimension;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class Bullet extends FreeMovingEntity {

	public static final int DAMAGE = 3;
	
	public Bullet(boolean fireToRight) {
		sizeInTiles = new Dimension(2, 2);
		
		setVelocity(new Vector2f(fireToRight ? 100 : -100, 3));
	}
	
	public void collide(TileMapEntity otherObj) {
		super.collide(otherObj);
		
		if (Math.abs(velocity.x) < 1) return;
		
		if (otherObj instanceof WalkingEntity) {
			((WalkingEntity)otherObj).damage(DAMAGE);
			scheduledForRemoval = true;
		}
	}

	
	@Override
	public void render(long timeElapsed) {
		Vector2f pos = getWorldPosition();
		
		if (velocity.length() > 20) {
			GL11.glPointSize(6f);
			GL11.glBegin(GL11.GL_POINTS);
			GL11.glColor4f(1, 0, 0, 0.4f);
			GL11.glVertex3f(pos.x, pos.y, 0);
			GL11.glEnd();
		}
		
		GL11.glPointSize(3f);
		GL11.glBegin(GL11.GL_POINTS);
		GL11.glColor4f(0.1f, 0, 0, 1f);
		GL11.glVertex3f(pos.x, pos.y, 0);
		GL11.glEnd();
	}
	
	@Override
	protected void floorCollision() {
		scheduledForRemoval = true;
	}

	@Override
	protected void wallCollision() {
		//scheduledForRemoval = true;
	}

}
