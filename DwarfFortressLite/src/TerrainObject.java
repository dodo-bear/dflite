import java.awt.Color;

public class TerrainObject extends GridObject{
	
	private Color backColor;
	
	public TerrainObject(String name, int count, String description, String type, int viewPriority, char displayChar, Color displayColor, Color backColor, GridSquare square) {
		super(name, count, description, type, viewPriority, displayChar, displayColor, square);
		this.backColor = backColor;
	}

	public Color getBackColor() {
		return backColor;
	}
	
}
