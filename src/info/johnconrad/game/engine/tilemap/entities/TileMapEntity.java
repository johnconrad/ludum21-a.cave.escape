package info.johnconrad.game.engine.tilemap.entities;

import info.johnconrad.game.engine.graphics.Renderable2D;
import info.johnconrad.game.engine.tilemap.TileMap;

import java.awt.Dimension;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public abstract class TileMapEntity extends Renderable2D {
	public static class BoundingBox {
		public float bottomY;
		public float topY;
		public float leftX;
		public float rightX;
		
		public boolean collidesWith(BoundingBox otherBox) {
			for(int x = (int)leftX; x <= rightX; x++) {
				for (int y = (int)bottomY; y <= topY; y++) {
					if (x >= otherBox.leftX && x <= otherBox.rightX &&
						y >= otherBox.bottomY && y <= otherBox.topY)

						return true;
				}
			}
			return false;
		}
		
		public String toString() {
			return "[" + leftX + ", " + bottomY + " : " + rightX + ", " + topY + "]";
		}
	}
	
	protected TileMap map;
	protected Vector2f startingPosition;
	protected Dimension sizeInTiles;	
	
	protected boolean scheduledForRemoval = false;

	
	public void render(long timeElapsed) {
		if (map.isDrawBoundingBoxes()) {
			BoundingBox box = getBoundingBox();
			 
			GL11.glBegin(GL11.GL_LINE_LOOP); { 
				GL11.glColor4f(1f, 0f, 0f, 1f);
				GL11.glVertex2f((box.leftX - map.getViewPos().x) * map.getTileSize().x, (box.bottomY - map.getViewPos().y) * map.getTileSize().y);
				GL11.glVertex2f((box.rightX - map.getViewPos().x) * map.getTileSize().x, (box.bottomY - map.getViewPos().y) * map.getTileSize().y);
				GL11.glVertex2f((box.rightX - map.getViewPos().x) * map.getTileSize().x, (box.topY - map.getViewPos().y) * map.getTileSize().y);
				GL11.glVertex2f((box.leftX - map.getViewPos().x) * map.getTileSize().x, (box.topY - map.getViewPos().y) * map.getTileSize().y);
				
			}
			GL11.glEnd();
			
			GL11.glBegin(GL11.GL_LINES); { 
				GL11.glColor4f(0f, 1f, 0f, 1f);
				GL11.glVertex2f(((float)Math.floor(box.leftX) - map.getViewPos().x) * map.getTileSize().x, (box.bottomY - map.getViewPos().y) * map.getTileSize().y);
				GL11.glVertex2f(((float)Math.ceil(box.rightX) - map.getViewPos().x) * map.getTileSize().x, (box.bottomY - map.getViewPos().y) * map.getTileSize().y);
				
			}
			GL11.glEnd();	
			
		}
	}
	
	public void reset() {
		if (startingPosition == null) {
			scheduledForRemoval = true;
			return;
		}
		
		position = new Vector2f(startingPosition);
	}

	public void collide(TileMapEntity otherObj) {
	}
	
	public Vector2f getWorldPosition() {
		Vector2f pos = new Vector2f(map.getPosition());
		pos.x += (position.x - map.getViewPos().x) * map.getTileSize().x;
		pos.y += (position.y - map.getViewPos().y) * map.getTileSize().y;
		
		return pos;
	}
	
	public BoundingBox getBoundingBox() {
		BoundingBox box = new BoundingBox();
		box.bottomY = position.y;
		box.topY = box.bottomY + sizeInTiles.height;
		box.leftX = position.x;
		box.rightX = position.x + sizeInTiles.width;
		return box;
	}
	
	public TileMap getMap() {
		return map;
	}
	
	public void setMap(TileMap map) {
		this.map = map;
	}
	
	public Vector2f getStartingPosition() {
		return startingPosition;
	}
	
	public void setStartingPosition(Vector2f startingPosition) {
		this.startingPosition = startingPosition;
	}

	public Dimension getSizeInTiles() {
		return sizeInTiles;
	}

	public boolean isScheduledForRemoval() {
		return scheduledForRemoval;
	}

	public void setScheduledForRemoval(boolean scheduledForRemoval) {
		this.scheduledForRemoval = scheduledForRemoval;
	}
}
