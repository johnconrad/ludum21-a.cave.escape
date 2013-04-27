package info.johnconrad.game.engine.tilemap;

import info.johnconrad.game.engine.GameEngine;
import info.johnconrad.game.engine.graphics.Renderable2D;
import info.johnconrad.game.engine.graphics.Sprite;
import info.johnconrad.game.engine.graphics.Sprite.SpriteColor;
import info.johnconrad.game.engine.tilemap.entities.TileMapEntity;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

public abstract class TileMap extends Renderable2D {
	
	public static class TileProperties {
		public float tint;
	}
	
	protected Random random;
	
	protected Texture artworkTexture;
	protected Texture mapTexture;
	protected byte[] rawMapData;

	protected TileMapTile[][] map;
	protected TileProperties[][] mapProperties;
	
	protected Dimension mapSize = new Dimension(0, 0);
	protected Vector2f tileSize = new Vector2f(0, 0);

	protected ArrayList<TileMapEntity> entities;
	
	protected ArrayList<Class<? extends TileMapEntity>> entityTypeList;
	protected ArrayList<TileMapTile> tileList;
	
	protected HashMap<Integer, Class<? extends TileMapEntity>> entityLookup;
	protected HashMap<Integer, TileMapTile> tileLookup;
	
	protected Vector2f viewPos = new Vector2f(0, 0);
	protected Dimension viewSize = new Dimension(32, 24);
	
	protected boolean drawBoundingBoxes = false;
	
	public Dimension getMapSize() {
		return mapSize;
	}

	public void setMapSize(Dimension mapSize) {
		this.mapSize = mapSize;
	}

	public Vector2f getTileSize() {
		return tileSize;
	}

	public void setTileSize(Vector2f tileSize) {
		this.tileSize = tileSize;
		updateDrawingDimensions();
	}

	public void updateDrawingDimensions() {
		for(TileMapTile currTile: tileList) { 
			if (currTile == null) continue;
			
			currTile.getArtwork().setSize(new Vector2f(tileSize.x, tileSize.y));
		}
	}

	public Vector2f getViewPos() {
		return viewPos;
	}

	public void setViewPos(Vector2f viewPos) {
		this.viewPos = viewPos;
	}

	public Dimension getViewSize() {
		return viewSize;
	}

	public void setViewSize(Dimension viewSize) {
		this.viewSize = viewSize;
	}

	public TileMapTile[][] getMap() {
		return map;
	}

	public TileMap() {
		random = new Random(GameEngine.getTime());
	}
	
	public void resetEntities() {
		for(TileMapEntity entity: entities) {
			entity.reset();
		}
	}
	
	protected void initialize() {
		rawMapData = mapTexture.getTextureData();
		
		loadTiles();
		loadEntitiesTypes();
		loadMap();
		
		rawMapData = null;

	}
	
	public void setTileArtwork(Texture artworkTexture, int x, int y, int tileWidth, int tileHeight) {
		this.artworkTexture = artworkTexture;
		
		int imageWidth = artworkTexture.getImageWidth();
		int imageHeight = artworkTexture.getImageHeight();

		tileSize = new Vector2f(tileWidth, tileHeight);

		for(int i = 0; i < tileList.size(); i++) {
			TileMapTile currTile = tileList.get(i);
			if (currTile == null) continue;
			
			int row = i / (imageWidth / (tileWidth + 2));
			int column = i % (imageHeight / (tileHeight + 2));
			
			int xOffset = (tileWidth + 2) * column;
			int yOffset = (tileHeight + 2) * row;
			float s = (x + xOffset + 1) / (float) imageWidth;
			float t = (y + yOffset + 1) / (float) imageHeight;
			float r = (x + tileWidth + xOffset) / (float) imageWidth;
			float q = (y + tileHeight + yOffset) / (float) imageHeight;
			
			Sprite newSprite = new Sprite(artworkTexture, s, t, r, q);
			newSprite.setSize(new Vector2f(tileWidth, tileHeight));
			newSprite.setCentered(false);
			currTile.setArtwork(newSprite);
		}
	}
	
	protected abstract void buildTileList();
	
	protected abstract void buildEntityList();
	
	private void loadTiles() {
		buildTileList();
		
		Queue<TileMapTile> tiles = new LinkedList<TileMapTile>(tileList);
		tileLookup = new HashMap<Integer, TileMapTile>();
		
		int pos = 0;
		while (tiles.size() > 0) {
			tileLookup.put(getKey(rawMapData[pos], rawMapData[pos+1], rawMapData[pos+2]), tiles.remove());
			pos += 3;
		}
	}
	
	private void loadEntitiesTypes() {
		buildEntityList();
		
		Queue<Class<? extends TileMapEntity>> entities = new LinkedList<Class<? extends TileMapEntity>>(entityTypeList);
		entityLookup = new HashMap<Integer, Class<? extends TileMapEntity>>();

		int pos = mapTexture.getTextureWidth() * 3; 
		while (entities.size() > 0) {
			Class<? extends TileMapEntity> entityType = entities.remove();
			if (TileMapEntity.class.isAssignableFrom(entityType))
				entityLookup.put(getKey(rawMapData[pos], rawMapData[pos+1], rawMapData[pos+2]), entityType);
			pos += 3;
		}
		
	}
	
	private void loadMap() {
		final int keyRows = 2;
		
		mapSize.width = mapTexture.getTextureWidth();
		int texHeight = mapTexture.getTextureHeight();
		mapSize.height = texHeight - keyRows;
		
		entities = new ArrayList<TileMapEntity>();
		map = new TileMapTile[mapSize.width][mapSize.height];
		mapProperties = new TileProperties[mapSize.width][mapSize.height];
		
		// offset of proper level data
		int offset = (mapSize.width * 3) * keyRows;
		
		for (int y = 0; y < mapSize.height; y++) {
			for(int x = 0; x < mapSize.width; x++)  {
				
				int pos = offset + (x * 3) + (y * 3) * texHeight;
				int key = getKey(rawMapData[pos], rawMapData[pos+1], rawMapData[pos+2]);
				
				TileMapTile tile = tileLookup.get(key);
				Class<? extends TileMapEntity> entityType = entityLookup.get(key);
				if (entityType != null) {
					try {
						// this position on the map was an entity.
						// so we will use the first proper tile for this location
						tile = tileList.get(1);
						
						// and create our monster or whatever and set it's position
						TileMapEntity entity = entityType.newInstance();
						entity.setPosition(new Vector2f(x, mapSize.height - y - 1));
						entity.setStartingPosition(new Vector2f(entity.getPosition()));
						entity.setMap(this);
						entities.add(entity);

					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				
				TileProperties prop = new TileProperties();
				prop.tint = 1f - (float)random.nextDouble() * 0.04f;
				
				map[x][mapSize.height - y - 1] = tile;
				mapProperties[x][mapSize.height - y - 1] = prop;
			}
		}
	}
	
	@Override
	public void render(long timeElapsed) {

		// determine where in our tilemap we are drawing from
		Vector2f bounds = new Vector2f();
		bounds.x = Math.min(mapSize.width, viewPos.x + viewSize.width);
		bounds.y = Math.min(mapSize.height, viewPos.y + viewSize.height);
		
		// draw our tiles
		for (float y = (float) Math.floor(viewPos.y); y < bounds.y; y++) {
			for(float x = (float) Math.floor(viewPos.x); x < bounds.x; x++)  {
				if (x < 0 || y < 0) continue;
				
				TileMapTile tile = map[(int)x][(int)y];
				TileProperties prop = mapProperties[(int)x][(int)y];
				if (tile == null) continue;
				
				Renderable2D art = tile.getArtwork();
				Vector2f drawPos = new Vector2f(position.x + ((x - viewPos.x) * tileSize.x), 
	                                            position.y + ((y - viewPos.y) * tileSize.y));

				
				((Sprite)art).setColor(new SpriteColor(prop.tint, prop.tint, prop.tint, 1));
				
				art.setPosition(drawPos);
				art.render(timeElapsed);
			}
		}
		
		// draw our creepies if they are visible
		for (TileMapEntity entity: entities) {
			if (entity.getPosition().x >= viewPos.x - entity.getSizeInTiles().width && 
				entity.getPosition().x < bounds.x &&
				entity.getPosition().y >= viewPos.y - entity.getSizeInTiles().width && 
				entity.getPosition().y < bounds.y) {
			
				entity.render(timeElapsed);
			}
			
		}
	}

	@Override
	public void update(long timeElapsed) {
		// update entities
		ArrayList<TileMapEntity> entitiesToRemove = new ArrayList<TileMapEntity>();
		for (TileMapEntity entity: entities) {
			entity.update(timeElapsed);
			if (entity.isScheduledForRemoval())
				entitiesToRemove.add(entity);
		}
		
		// check for entity collisions
		for (int i = 0; i < entities.size(); i++) {
			for (int j = i+1; j < entities.size(); j++) {
				TileMapEntity objA = entities.get(i);
				TileMapEntity objB = entities.get(j);
				if (objA.getBoundingBox().collidesWith(objB.getBoundingBox())) {
					objA.collide(objB);
					objB.collide(objA);
				}
				
			}
		}
		
		// remove dead entities
		entities.removeAll(entitiesToRemove);
	}

	public ArrayList<TileMapEntity> getEntities() {
		return entities;
	}
	
	public boolean isDrawBoundingBoxes() {
		return drawBoundingBoxes;
	}

	public void setDrawBoundingBoxes(boolean drawBoundingBoxes) {
		this.drawBoundingBoxes = drawBoundingBoxes;
	}

	public static int getKey(byte r, byte g, byte b) {
		return (((((int)r) << 8) ^ (int)g) << 8) ^ (int)b;
	}
}
