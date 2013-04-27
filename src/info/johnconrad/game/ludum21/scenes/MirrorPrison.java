package info.johnconrad.game.ludum21.scenes;

import info.johnconrad.game.engine.scenes.Scene;
import info.johnconrad.game.engine.tilemap.entities.WalkingEntity.Aim;
import info.johnconrad.game.engine.tilemap.entities.WalkingEntity.Direction;
import info.johnconrad.game.ludum21.MirrorPrisonTileMap;

import org.lwjgl.input.Keyboard;

public class MirrorPrison extends Scene {
	MirrorPrisonTileMap map;
	
	@Override
	public void render(long timeElapsed) {
		beginOrthoRender();
		
		map.render(timeElapsed);
		
		endOrthoRender();
	}

	@Override
	public void update(long timeElapsed) {
		map.update(timeElapsed);
	}

	@Override
	public void processInput() {
		boolean lockedMovement = false;
		
		// controls for free scrolling the level
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				lockedMovement = true;
				map.getViewPos().y += 1;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				lockedMovement = true;
				map.getViewPos().y -= 1;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				lockedMovement = true;
				map.getViewPos().x -= 1;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				map.getViewPos().x += 1;
				lockedMovement = true;
			}
		}
		
		if (!lockedMovement && Keyboard.isKeyDown(Keyboard.KEY_LEFT)) 
			map.getPlayer().walk(Direction.LEFT);
		if (!lockedMovement && Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) 
			map.getPlayer().walk(Direction.RIGHT);

		
		while (Keyboard.next()) {
			boolean released = !Keyboard.getEventKeyState();
			
			switch (Keyboard.getEventKey()) {
			// player controls
			case Keyboard.KEY_SPACE:
				if (!released)
					map.getPlayer().jump();
				break;
			case Keyboard.KEY_D:
				if (!released)
					map.getPlayer().attack();
				break;
			case Keyboard.KEY_LEFT:
				if (released)
					map.getPlayer().walk(0f);
				break;
			case Keyboard.KEY_RIGHT:
				if (released)
					map.getPlayer().walk(0f);
				break;
			case Keyboard.KEY_UP:
				if (released) map.getPlayer().setAim(Aim.STRAIT);
				else map.getPlayer().setAim(Aim.UP);
				break;
			case Keyboard.KEY_DOWN:
				if (released) map.getPlayer().setAim(Aim.STRAIT);
				else map.getPlayer().setAim(Aim.DOWN);
				break;

				
			case Keyboard.KEY_TAB:
				if (!released) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						map.controlPreviousThing();
					} else {
						map.controlNextThing();
					}
				}
				break;
			case Keyboard.KEY_F12:
				if (!released) {
					map.resetEntities();
				}
				break;
			case Keyboard.KEY_F11:
				if (!released) {
					map.setDrawBoundingBoxes(!map.isDrawBoundingBoxes());
				}
				break;
			}
		}
	}

	@Override
	public void loadResources() {
		map = new MirrorPrisonTileMap();
	}

	@Override
	public void freeResources() {
		// TODO Auto-generated method stub

	}

}
