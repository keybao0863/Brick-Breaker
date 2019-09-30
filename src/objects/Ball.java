package objects;

import java.awt.Color;
import java.awt.Graphics2D;

public class Ball {
	private double ballposX = 120;
	private double ballposY = 350;
	private double ballXdir = -1;
	private double ballYdir = -2;
	
	public Ball() {
		
	}
	
	public Ball(double x, double y, double xDir, double yDir) {
		this.ballposX = x;
		this.ballposY = y;
		this.ballXdir = xDir;
		this.ballYdir = yDir;
		
	}
	
	public void reverseXDir() {
		this.ballXdir = -this.ballXdir;
	}
	
	public void reverseYDir() {
		this.ballYdir = -this.ballYdir;
	}
	
	public void updatePos() {
		
		this.setBallposX(this.getBallposX() + this.getBallXdir());
		this.setBallposY(this.getBallposY() + this.getBallYdir());
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.yellow);
		g.fillOval( (int) ballposX, (int) ballposY, 20, 20);
	}
	
	public double getBallposX() {
		return ballposX;
	}
	public void setBallposX(double ballposX) {
		this.ballposX = ballposX;
	}
	public double getBallposY() {
		return ballposY;
	}
	public void setBallposY(double ballposY) {
		this.ballposY = ballposY;
	}
	public double getBallXdir() {
		return ballXdir;
	}
	public void setBallXdir(double ballXdir) {
		this.ballXdir = ballXdir;
	}
	public double getBallYdir() {
		return ballYdir;
	}
	public void setBallYdir(double ballYdir) {
		this.ballYdir = ballYdir;
	}
}
