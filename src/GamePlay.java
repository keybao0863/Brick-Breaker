import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;
import javax.swing.Timer;

import objects.Ball;
import objects.BrickMap;
import objects.NormBrick;



public class GamePlay extends JPanel implements KeyListener, ActionListener, MouseMotionListener{

	
	private boolean play = false;
	private int score = 0 ;
	
	private int totalBricks;
	
	private int nRow =9;
	private int nCol=7;
	
	private Timer timer;
	private Timer addLayerTimer;
	private int delay = 5;
	private int addLayerDelay = 10000;
	
	private int playerX =310;
	
//	private double ballposX = 120;
//	private double ballposY = 350;
//	private double ballXdir = -1;
//	private double ballYdir = -2;
	
	private List<Ball> balls = new ArrayList<Ball>();
	private int paddleWidth = 50;
	
	private BrickMap brickMap;
	
	//audio
	
	private Clip clip;
	
	public GamePlay() {
		// TODO Auto-generated constructor stub
		
		//initiate game
		brickMap = new BrickMap(nRow, nCol);
		totalBricks = brickMap.getnCol() * brickMap.getnRow();
		balls.add(new Ball());
		addKeyListener(this);
		addMouseMotionListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
		
		//add layer of bricks every 7500 seconds
		ActionListener taskAddLayer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				brickMap.addLayer();
				totalBricks += brickMap.getnCol();
			}
		};
		addLayerTimer = new Timer(addLayerDelay, taskAddLayer);
		addLayerTimer.start();
		
		//play BGM
		try{
			File f = new File("src/theme1.wav");			
			AudioInputStream ais = AudioSystem.getAudioInputStream(f.getAbsoluteFile());
			clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start( );
			clip.loop(Clip.LOOP_CONTINUOUSLY); 			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Visualization every frame
	 */
	public void paint(Graphics g) {
		
		//background
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);
		
		//border
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		//bricks
		brickMap.draw((Graphics2D) g);
		
		//paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 550, paddleWidth, 8);
		
//		//ball
		for (Ball ball:balls) {
			ball.draw((Graphics2D) g);
		}
	
		//score
		g.setColor(Color.white);
		g.setFont(new Font("Helvetica", Font.BOLD, 20));
		g.drawString("Score: " + Integer.toString(score), 570, 550);
		
		//ball fall off stage
		for (Ball ball:balls) {
			if (ball.getBallposY() > 600) {
				if (balls.size() == 1) {
					loseGame(g);
				}
				else {
					balls.remove(ball);
				}
				
			}
		}
		//if no block left, add 5 layers
		if (totalBricks==0) {
			for (int i=0;i<5;i++) {
				brickMap.addLayer();
				totalBricks+=brickMap.getnCol();
			}
		}
		//If bricks reach bottom, lose game
		for (int i = 0 ; i <  brickMap.map.length;  i++) {
			for (int j =0; j < brickMap.map[0].length; j++) {
				if (brickMap.map[i][j].isVisible()==true && i* brickMap.getHeight() + 50 > 540) {
					loseGame(g);
		}
		}
		}
		
	}
	
	/**
	 * When game is lost. display message.
	 * @param g
	 */
	private void loseGame(Graphics g) {
		addLayerTimer.stop();
		play = false;
		clip.stop();
		g.setColor(Color.red);
		g.drawString("Game Over, Press Enter to restart", 300, 350);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		timer.start();
		if (play) {
//			System.out.println("ball direction X: " + ballXdir);
			
			//if ball touches paddle, reverse
			for (Ball b : balls) {
				Rectangle ball = new Rectangle((int) b.getBallposX(), (int) b.getBallposY(), 20, 20);
				Rectangle paddle = new Rectangle(playerX, 550, paddleWidth, 8);
				if (ball.intersects(paddle)) {
					touchPaddle(b);
					b.setBallYdir(-b.getBallYdir());
				}

				b.updatePos();			
				ballBorderInteraction(b);

				//check if ball touches brick, if so, set brick to invisible
				for (int i = 0; i < brickMap.map.length; i++) {
					for (int j = 0; j < brickMap.map[0].length; j++) {
						Rectangle curBrick = new Rectangle(j * brickMap.getWidth() + 50, i * brickMap.getHeight() + 50,
								brickMap.getWidth() - 3, brickMap.getHeight() - 3);

						if (touch(ball, curBrick)) {

							if (brickMap.map[i][j].isVisible() == true) {
								// if ball touches on top and bot, change y direction
								// if ball touches on left and right, change x direction
								if (b.getBallposX() + 19 <= j * brickMap.getWidth() + 50
										||  b.getBallposX() + 1 >= j * brickMap.getWidth() + 50 + brickMap.getWidth() - 3) {
									b.reverseXDir();
								}
								else {
									b.reverseYDir();
								}
								
								//add scores
								score += 5;
								totalBricks -= 1;
								// System.out.println("setting invisible");
								brickMap.map[i][j].setVisible(false);

								brickEffect(b, brickMap.map[i][j]);

							}

						}
					}
				}
			}
		}
		
		
		repaint();
		
	}
	
	/**
	 * Check if ball touches border, if so, change direction
	 * @param ball
	 */
	private void ballBorderInteraction(Ball ball) {
		if (ball.getBallposX() < 4) {
			ball.setBallXdir(-ball.getBallXdir());
		}
		
		if (ball.getBallposX() > 680) {
			ball.setBallXdir(-ball.getBallXdir());
		}
		
		if (ball.getBallposY() <0 ) {
			ball.setBallYdir(-ball.getBallYdir());
		}
		
	}
	/**
	 * Helper Method: Fulfill the effect of bricks.
	 * @param brick
	 */
	private void brickEffect(Ball b, NormBrick brick) {
		if (brick.getType().equals("normal")) {
			return;
		}
		if (brick.getType().equals("longPaddle")) {
			paddleWidth  = paddleWidth*2;
			//System.out.println("adding layer!");
			new java.util.Timer().schedule(new java.util.TimerTask() {
				@Override
				public void run() {
					paddleWidth = paddleWidth/2;
					
				}
			}, 10000);
			return;
		}
		else if (brick.getType().equals("slowBall")) {
			for (Ball ball : balls) {
				ball.setBallYdir(ball.getBallYdir() / 1.5);
				// revert after 10 sec
				new java.util.Timer().schedule(new java.util.TimerTask() {
					@Override
					public void run() {
						ball.setBallYdir(ball.getBallYdir() * 1.5);
						//System.out.println("speed reversed!");
					}
				}, 10000);
			}
			return;
		}
		else if (brick.getType().equals("fastBall")) {
			for (Ball ball : balls) {
				ball.setBallYdir(ball.getBallYdir() * 1.5);
				// revert after 10 sec
				new java.util.Timer().schedule(new java.util.TimerTask() {
					@Override
					public void run() {
						ball.setBallYdir(ball.getBallYdir() / 1.5);
						//System.out.println("speed reversed!");
					}
				}, 10000);
			}
			return;
		}
		else if (brick.getType().equals("multipleBall")) {
			ArrayList<Ball> temp = new ArrayList<Ball>(balls);
			
			temp.add(new Ball(b.getBallposX(), b.getBallposY(), 1, -1));
			
			balls = temp;
			
		}
	}
	
	/**
	 * Method to change x-direction of ball based on which part of the paddle the ball contacts
	 * 
	 */
	private void touchPaddle(Ball b) {
		int paddleLeft = playerX;
		int paddleRight = playerX+paddleWidth;
		int paddleMid = (paddleLeft+paddleRight)/2;
		
		//if ball is on the left side of the paddle, ball direction <0.
		double relativePos = (double) (b.getBallposX()+10 - paddleMid)/ (double) paddleWidth *2;
		
		b.setBallXdir(relativePos);

	}
	
	private boolean touch(Rectangle ball, Rectangle brick) {
		if (ball.intersects(brick)) {
			return true;
		}
		
		return false;
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if ( e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (playerX >= 692 - paddleWidth) {
				playerX=692 - paddleWidth;
			}
			else {
				moveRight();
			}
		}
		
		if ( e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (playerX < 10 ) {
				playerX = 10;
			}
			else {
				moveLeft();
			}
		}
		
		if ( e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (play==false) {
				brickMap = new BrickMap(nRow, nCol);
				balls = new ArrayList<Ball>();
				balls.add(new Ball());
				clip.start();
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				totalBricks =  brickMap.getnCol() * brickMap.getnRow();
				score=0;
				paddleWidth=50;
				play=true;
				addLayerTimer.start();
				
			}
			
		}
		
		
	}
	
	
	private void moveRight() {
		play = true;
		playerX+= 20;
	}
	
	private void moveLeft() {
		play = true;
		playerX-= 20;
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		int mousePosX = e.getX();
		playerX = mousePosX;
		// TODO Auto-generated method stub
		
	}
	

}
