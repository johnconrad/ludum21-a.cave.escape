package info.johnconrad.game.ludum21;

import info.johnconrad.game.engine.GameEngine;
import info.johnconrad.game.engine.tilemap.TileMap;
import info.johnconrad.game.engine.tilemap.TileMapTile;
import info.johnconrad.game.engine.tilemap.entities.TileMapEntity;
import info.johnconrad.game.engine.tilemap.entities.WalkingEntity;
import info.johnconrad.game.ludum21.entities.Rat;
import info.johnconrad.game.ludum21.entities.SadMan;
import info.johnconrad.game.ludum21.entities.Skelleton;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class MirrorPrisonTileMap extends TileMap {

	protected ArrayList<WalkingEntity> controllableThings; 
	protected int playerIndex;
	
	public MirrorPrisonTileMap() {
		super();
		
		try {
			Texture sprites = TextureLoader.getTexture("PNG", GameEngine.class.getResourceAsStream("/images/sprites-big.png"));
			Texture tiles = TextureLoader.getTexture("PNG", GameEngine.class.getResourceAsStream("/images/tiles.png"));
			mapTexture = TextureLoader.getTexture("PNG", GameEngine.class.getResourceAsStream("/images/map.png"));
			
			SadMan.setTexture(sprites);
			Rat.setTexture(sprites);
			Skelleton.setTexture(sprites);
			
			initialize();
			
			setTileArtwork(tiles, 0, 0, 20, 20);
			
			int i = 0;
			controllableThings = new ArrayList<WalkingEntity>();
			for(TileMapEntity currEntity: entities) {
				if (currEntity instanceof WalkingEntity) {
					controllableThings.add((WalkingEntity)currEntity);
					
					if (currEntity instanceof SadMan) playerIndex = i;
					i++;
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void buildEntityList() {
		if (entityTypeList != null) return;
		
		entityTypeList = new ArrayList<Class<? extends TileMapEntity>>();
		entityTypeList.add(SadMan.class);
		entityTypeList.add(Rat.class);
		entityTypeList.add(Skelleton.class);
	}

	@Override
	protected void buildTileList() {
		if (tileList != null) return;
		tileList = new ArrayList<TileMapTile>();
		
		tileList.add(null);
		tileList.add(new TileMapTile("Cavern", false, false));
		tileList.add(new TileMapTile("Wall", true, false));
		tileList.add(new TileMapTile("Topaz", true, false));
		tileList.add(new TileMapTile("Lava1", false, true));
		tileList.add(new TileMapTile("Lava2", false, true));
		tileList.add(new TileMapTile("Lava3", false, true));
		tileList.add(new TileMapTile("Grass?", true, false));
		
		TileMapTile woodPlatform = new TileMapTile("Wooden Platform", false, false);
		woodPlatform.setPlatform(true);
		tileList.add(woodPlatform);
		
		tileList.add(new TileMapTile("Spike", false, true));

	}

	public void update(long timeElapsed) {
		super.update(timeElapsed);
		
		Vector2f desiredPos = new Vector2f();
		desiredPos.x = getPlayer().getPosition().x - ((float)viewSize.width) / 2;
		desiredPos.y = getPlayer().getPosition().y - ((float)viewSize.height) / 2;
		
		Vector2f desiredDirection = new Vector2f(desiredPos.x - viewPos.x, desiredPos.y - viewPos.y);
		float distance = desiredDirection.length();
		if (distance == 0) return;
		
		float scale = (distance * timeElapsed / 200) / distance;
		
		viewPos.x += desiredDirection.x * scale; 
		viewPos.y += desiredDirection.y * scale; 
	}
	
	@Override
	public void render(long timeElapsed) {
		super.render(timeElapsed);
	}
	
	public WalkingEntity getPlayer() {
		if (playerIndex < 0) playerIndex = controllableThings.size() - 1;
		if (playerIndex >= controllableThings.size()) playerIndex = 0;
		
		return controllableThings.get(playerIndex);
	}
	
	public void controlNextThing() {
		playerIndex++;
	}
	
	public void controlPreviousThing() {
		playerIndex--;
	}

}
