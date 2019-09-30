package objects;
import java.awt.Color;
import java.awt.Graphics2D;

public class NormBrick{
	private boolean visible = false;
	
	public boolean isVisible() {
		return visible;
	}

	private int row;
	private int col;
	private int brickWidth;
	private int brickHeight;
	private String type;
	
	

	public NormBrick(int col, int row, int width, int height, String type) {
		this.col = col;
		this.row = row;
		this.brickWidth = width;
		this.brickHeight = height;
		this.setVisible(true);
		this.type= type;
		//System.out.println("column : " + this.col + " row: " + this.row + " height " + this.brickHeight + "  width" + this.brickWidth);
		
	}
	
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}


	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		this.visible=b;
		
	}

	
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		if (type.equals("normal")){
			g.setColor(Color.white);
		}
		else if (type.equals("longPaddle")) {
			g.setColor(Color.GREEN);
		}
		else if (type.equals("slowBall")) {
			g.setColor(Color.ORANGE);
		}
		else if (type.equals("multipleBall")) {
			g.setColor(Color.CYAN);
		}
		else if (type.equals("fastBall")) {
			g.setColor(Color.RED);
		}
		
		g.fillRect(col  * brickWidth + 50 , row* brickHeight + 50, brickWidth-7, brickHeight-7);
		
		
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


}
