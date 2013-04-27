package info.johnconrad.game.engine.tilemap;

import info.johnconrad.game.engine.graphics.Renderable2D;

public class TileMapTile {
	String name;
	boolean solid;
	boolean dangerous;

	boolean platform = false;

	Renderable2D artwork;

	public TileMapTile(String name, boolean solid, boolean dangerous) {
		this.name = name;
		this.solid = solid;
		this.dangerous = dangerous;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSolid() {
		return solid;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public boolean isDangerous() {
		return dangerous;
	}

	public void setDangerous(boolean dangerous) {
		this.dangerous = dangerous;
	}

	public boolean isPlatform() {
		return platform;
	}

	public void setPlatform(boolean platform) {
		this.platform = platform;
	}

	public Renderable2D getArtwork() {
		return artwork;
	}

	public void setArtwork(Renderable2D artwork) {
		this.artwork = artwork;
	}
	
	
}
