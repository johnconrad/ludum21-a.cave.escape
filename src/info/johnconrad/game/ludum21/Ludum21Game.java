package info.johnconrad.game.ludum21;

import info.johnconrad.game.engine.GameEngine;
import info.johnconrad.game.engine.scenes.LogoScene;
import info.johnconrad.game.engine.scenes.Scene;
import info.johnconrad.game.ludum21.scenes.MirrorPrison;

import java.util.LinkedList;

@SuppressWarnings("serial")
public class Ludum21Game extends GameEngine {

	protected void buildSceneQueue() {
		sceneQueue = new LinkedList<Scene>();
		sceneQueue.add(new LogoScene());
		sceneQueue.add(new MirrorPrison());
		//sceneQueue.add(new SpaceScene());
		
	}
	
	public static void main(String[] args) {
		Ludum21Game game = new Ludum21Game();
		game.startGame();
	}

}
