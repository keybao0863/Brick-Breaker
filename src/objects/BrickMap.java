package objects;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import javax.swing.JPanel;

public class BrickMap extends JPanel{

	public NormBrick[][] map;
	private int nCol;
	private int nRow;
	private int brickWidth;
	private int brickHeight;
	
	public BrickMap(int nCol, int nRow) {
		this.nCol = nCol;
		this.nRow = nRow;
		brickWidth = 540 / nCol;
		brickHeight = 150 / nRow;
		map = new NormBrick[nRow][nCol];
		for (int i = 0 ; i < map.length;  i++) {
			for (int j =0; j < map[0].length; j++) {
				
					map[i][j] = new NormBrick(j, i, brickWidth, brickHeight, randomBrick());
				
			}
		}
		
		
	}
	
	private String randomBrick() {
		Random rand =new Random();
		int randInt = rand.nextInt(30);
		if (randInt == 1) {
			return "longPaddle";
		}
		else if (randInt == 2) {
			return "slowBall";
			
		}
		else if (randInt == 3) {
			return "multipleBall";
		}
		else if (randInt == 4 || randInt==5) {
			return "fastBall";
		}
		else {
			return "normal";
		}
	}
	
	public int getnCol() {
		return nCol;
	}

	public void setnCol(int nCol) {
		this.nCol = nCol;
	}

	public int getnRow() {
		return nRow;
	}

	public void setnRow(int nRow) {
		this.nRow = nRow;
	}

	public void draw(Graphics2D g) {
		
		for (int i = 0 ; i <  map.length;  i++) {
			for (int j =0; j < map[0].length; j++) {
				//System.out.println(i + " " + j + " " + map[i][j]);
				if (map[i][j].isVisible()==true ) {
//					System.out.println("Drawing brick!");
					map[i][j].draw(g);
//					g.setColor(Color.white);
//					g.fillRect(j  * brickWidth + 50 , i* brickHeight + 50, brickWidth-3, brickHeight-3);
				}
			}
		} 
	}
	
	public int getWidth() {
		return brickWidth;
	}
	
	public int getHeight() {
		return brickHeight;
	}
	
	/**
	 * addLayer() adds a row of bricks on top of the current brickmap.
	 * All the previous blocks are moved down one row
	 */
	public void addLayer() {
		//initialize new map, size + 1
		NormBrick[][] newMap = new NormBrick[this.getnRow()+1][this.getnCol()];
		this.setnRow(this.getnRow()+1);
		this.setnCol(this.getnCol());
		
		//copy old bricks in here
		for (int i = 0 ; i < map.length;  i++) {
			for (int j =0; j < map[0].length; j++) {
				newMap[i+1][j] = map[i][j];
				NormBrick curBrick = newMap[i+1][j];
				curBrick.setRow(curBrick.getRow()+1);
			}
			
		}
		
		//add new block
		for (int col = 0; col < newMap[0].length; col++) {
			newMap[0][col] = new NormBrick(col,0, brickWidth, brickHeight, randomBrick());
		}
		
		this.map = newMap;
		
	}
}
