package info.johnconrad.game.engine.graphics;

import info.johnconrad.game.engine.GameEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

public class Sprite extends Renderable2D {
	public static class SpriteColor {
		float red = 1f;
		float green = 1f;
		float blue = 1f;
		float alpha = 1f;
		
		public SpriteColor() {
		}

		public SpriteColor(SpriteColor color) {
			red = color.red;
			green = color.green;
			blue = color.blue;
			alpha = color.alpha;
		}

		
		public SpriteColor(float red, float green, float blue, float alpha) {
			this.red = red;
			this.blue = blue;
			this.green = green;
			this.alpha = alpha;
		}

	}
	
	// standard current state of the sprite variables
	protected SpriteColor color;
	protected SpriteColor colorOverride = null;
	
	protected Texture  texture;
	protected float    s;
	protected float    t;
	protected float    r;
	protected float    q;
	
	protected boolean  centered;
	
	// for fading functionality
	boolean fading = false;
	long startFadingTime;
	long fadingDuration;
	protected SpriteColor originalColor;
	protected SpriteColor finalColor;

	boolean drawFrame = false;
	
	public Sprite(Texture texture) {
		this(texture, 0, 0, 1, 1);
	}
	
	public Sprite(Texture texture, float s, float t, float r, float q) {
		this.texture = texture;
		this.s = s;
		this.t = t;
		this.r = r;
		this.q = q;
		
		position = new Vector2f(0, 0);
		color = new SpriteColor();
		size = new Vector2f(2, 2);
		
		centered = false;
	}

	public void fadeToColor(SpriteColor finalColor, long duration) {
		fading = true;
		startFadingTime = GameEngine.getTime();
		fadingDuration = duration;
		originalColor = new SpriteColor(color);
		this.finalColor = finalColor;
	}

	public void render(long timeElapsed) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
		
		float xOffset = 0;
		float yOffset = 0;
		
		if (centered) {
			xOffset = size.x / 2.0f;
			yOffset = size.y / 2.0f;
		}
		
		float sa = mirrored ? r : s;
		float ra = mirrored ? s : r;
		float ta = flipped ? q : t;
		float qa = flipped ? t : q;
		
		SpriteColor usedColor = colorOverride == null ? color : colorOverride;
		
		//System.out.println(color.red + ", " + color.green + ", " + color.blue + ", " + color.alpha);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(usedColor.red, usedColor.green, usedColor.blue, usedColor.alpha);
		GL11.glTexCoord2f(sa, qa);
		GL11.glVertex2f(position.x - xOffset, position.y - yOffset);
		GL11.glTexCoord2f(ra, qa);
		GL11.glVertex2f(position.x + size.x - xOffset, position.y - yOffset);
		GL11.glTexCoord2f(ra, ta);
		GL11.glVertex2f(position.x + size.x - xOffset, position.y + size.y - yOffset);
		GL11.glTexCoord2f(sa, ta);
		GL11.glVertex2f(position.x - xOffset, position.y + size.y - yOffset);
		GL11.glEnd();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		if (drawFrame) {
			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glColor4f(1f, 0f, 0f, 1f);
			GL11.glVertex2f(position.x - xOffset, position.y - yOffset);
			GL11.glVertex2f(position.x + size.x - xOffset, position.y - yOffset);
			GL11.glVertex2f(position.x + size.x - xOffset, position.y + size.y - yOffset);
			GL11.glVertex2f(position.x - xOffset, position.y + size.y - yOffset);
			GL11.glEnd();
		}
	}
	
	@Override
	public void update(long timeElapsed) {
		// fading logic
		if (fading) {
			long now = GameEngine.getTime();
			
			// if we are done, set final color and quit
			if (now > startFadingTime + fadingDuration) {
				color = finalColor;
				fading = false;
			}
			
			// otherwise set the scaled fading value
			else {
				float progress = (float) (now - startFadingTime) / (float) fadingDuration;
				
				color.red = ((finalColor.red - originalColor.red) * progress) + originalColor.red;
				color.green = ((finalColor.green - originalColor.green) * progress) + originalColor.green;
				color.blue = ((finalColor.blue - originalColor.blue) * progress) + originalColor.blue;
				color.alpha = ((finalColor.alpha - originalColor.alpha) * progress) + originalColor.alpha;
				
				//System.out.println(progress + ": " + originalColor.alpha + " -> " + finalColor.alpha + " (" + color.alpha + ")");
			}
		}
	}

	public boolean isDrawFrame() {
		return drawFrame;
	}

	public void setDrawFrame(boolean drawFrame) {
		this.drawFrame = drawFrame;
	}
	
	
	public boolean isCentered() {
		return centered;
	}

	public void setCentered(boolean centered) {
		this.centered = centered;
	}

	public SpriteColor getColor() {
		return color;
	}

	public void setColor(SpriteColor color) {
		this.color = color;
	}

	public SpriteColor getColorOverride() {
		return colorOverride;
	}

	public void setColorOverride(SpriteColor colorOverride) {
		this.colorOverride = colorOverride;
	}

	public boolean isFading() {
		return fading;
	}	
}
