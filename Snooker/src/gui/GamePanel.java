/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import logic.Ball;
import logic.Cloth;
import logic.Game;
import logic.GameState;
import logic.Pair;
import logic.Player;
import logic.V2D;


public class GamePanel extends JPanel implements ActionListener, Runnable,
        MouseListener, MouseMotionListener, KeyListener{
    
    private static final long serialVersionUID = 1L;

	private MainMenu mainWindow;
	private JPanel mainMenu;

	private Image cueImage;
	private List<Image> ballImages = new ArrayList<>();
	private Image scoreImage;

	private Timer timer;
	private static final int DESIRED_FPS = 100;
	private static final double dt = 1.0/DESIRED_FPS;

	private Game game;

	private V2D initialWoodPosition;
	private V2D finalWoodPosition;
	private V2D initialClothPosition;
	private V2D finalClothPosition;
	private V2D bottomVertexTriangle;
	private V2D upVertexTriangle;
	private V2D mediumVertexTriangle;
	private V2D blueBallPoint;
	private V2D blackBallPoint;
	private V2D brownBallPoint;
	private V2D greenBallPoint;
	private V2D yellowBallPoint;
	private V2D pinkBallPoint;
        
    public void loadImages() throws IOException {
		cueImage = ImageIO.read(this.getClass().getResource("res/cue.png"));
		ballImages.add(ImageIO.read(this.getClass().getResource("res/whiteBall.png")));
		ballImages.add(ImageIO.read(this.getClass().getResource("res/redBall.png")));
		ballImages.add(ImageIO.read(this.getClass().getResource("res/yellowBall.png")));
		ballImages.add(ImageIO.read(this.getClass().getResource("res/greenBall.png")));
		ballImages.add(ImageIO.read(this.getClass().getResource("res/brownBall.png")));
		ballImages.add(ImageIO.read(this.getClass().getResource("res/blueBall.png")));
		ballImages.add(ImageIO.read(this.getClass().getResource("res/pinkBall.png")));
		ballImages.add(ImageIO.read(this.getClass().getResource("res/blackBall.png")));
		scoreImage = ImageIO.read(this.getClass().getResource("res/Score.png"));
	}

	public GamePanel(JPanel mainMenu, MainMenu mainWindow) {
		setLocation((Utilities.dimScreen.width - getWidth()) / 2,
				(Utilities.dimScreen.height - getHeight()) / 2);

		this.mainWindow = mainWindow;
		this.mainMenu = mainMenu;

		this.game = new Game("Joao", "Luis");

		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);
		setLayout(null);
		setDoubleBuffered(true);
		requestFocus();
	}

	public Game getGame() {
		return game;
	}

	public void Init() throws IOException {
		loadImages();

		InitializeDrawingPoints();
		InitializeBallsPoints();
		timer = new Timer(1000 / DESIRED_FPS, (ActionListener) this);
		timer.start();
		setVisible(true);
	}

	public void InitializeBallsPoints() {
		double ballRadius = Ball.getRadius();

		// White Ball
		game.getTable().getBallSet().get(0).setPosition(new V2D(initialClothPosition.getX() + 200, blueBallPoint.getY() + 30 + ballRadius));

		// starting red balls position
		int ballsPerColumns = 5;
		int indexBall = 1;
		double ballX;
		double ballY;
		for (int columns = 0; columns < 5; columns++) {
			ballX = (upVertexTriangle.getX() - 2 * ballRadius) - (columns * 2 * ballRadius) + ballRadius - 3;
			ballY = upVertexTriangle.getY() + (2 * ballRadius) + 3 + (columns * ballRadius);

			if(columns != 0)
				ballX += 3*columns;

			for (int balls = 0; balls < ballsPerColumns; balls++) {
				game.getTable().getBallSet().get(indexBall).setPosition(new V2D(ballX, ballY));
				ballY += 2*ballRadius + 1;
				indexBall++;
			}
			ballsPerColumns--;
		}

		// Color Balls
		// Yellow ball
		game.getTable().getBallSet()
		.get(16)
		.setPosition(
				(new V2D(yellowBallPoint.getX(),
						yellowBallPoint.getY())));

		// Green Ball
		game.getTable().getBallSet()
		.get(17)
		.setPosition(
				(new V2D(greenBallPoint.getX(),
						greenBallPoint.getY())));

		// Brown Ball
		game.getTable().getBallSet()
		.get(18)
		.setPosition(
				(new V2D(brownBallPoint.getX(),
						brownBallPoint.getY())));
		// Blue Ball
		game.getTable().getBallSet()
		.get(19)
		.setPosition(
				(new V2D(blueBallPoint.getX(),
						blueBallPoint.getY())));
		// Pink Ball
		game.getTable().getBallSet()
		.get(20)
		.setPosition(
				new V2D(pinkBallPoint.getX(),
						pinkBallPoint.getY()));
		// Black Ball
		game.getTable().getBallSet()
		.get(21)
		.setPosition(
				(new V2D(blackBallPoint.getX(),
						blackBallPoint.getY())));


		List<V2D> ballpos = game.getBallPosition();
		Ball white = game.getTable().getWhiteBall();
		ballpos.add(new V2D(white.getPosition()));
		ballpos.add(new V2D());
		ballpos.add(new V2D(yellowBallPoint));
		ballpos.add(new V2D(greenBallPoint));
		ballpos.add(new V2D(brownBallPoint));
		ballpos.add(new V2D(blueBallPoint));
		ballpos.add(new V2D(pinkBallPoint));
		ballpos.add(new V2D(blackBallPoint));
	}
        
        public void InitializeDrawingPoints(){
            //Wood Points
            initialWoodPosition = new V2D(100, 20);
            finalWoodPosition = new V2D(game.getTable().getCloth().getFinalPosition().getX()-52,
                            game.getTable().getCloth().getFinalPosition().getY() - 35);
            
            //Cloth Points
            initialClothPosition = new V2D(game.getTable().getCloth()
            .getInitialPosition().getX() + 40, game.getTable().getCloth()
            .getInitialPosition().getY()- 30);
            
            finalClothPosition = new V2D(game.getTable().getCloth().getFinalPosition()
				.getX()
				- game.getTable().getCloth().getInitialPosition().getX() - 25, game.getTable()
				.getCloth().getFinalPosition().getY()
				- game.getTable().getCloth().getInitialPosition().getY() - 25);

		// Triangle Vertexs
		bottomVertexTriangle = new V2D(game.getTable().getCloth()
				.getFinalPosition().getX() - 100, game.getTable().getCloth().getHeight());
		upVertexTriangle = new V2D(game.getTable().getCloth().getFinalPosition()
				.getX() - 100, game.getTable().getCloth().getHeight() / 3 + 80);
		mediumVertexTriangle = new V2D(game.getTable().getCloth().getWidth() + 40,
				game.getTable().getCloth().getHeight()
				- ((game.getTable().getCloth().getHeight() - (game.getTable().getCloth()
						.getHeight() / 3 + 80)) / 2));

		// Point on Center Table
		blueBallPoint = new V2D((finalClothPosition.getX() / 2)
				+ initialClothPosition.getX(), (finalClothPosition.getY() / 2)
				+ initialClothPosition.getY());
        //Point before Triangle
        pinkBallPoint = new V2D(mediumVertexTriangle.getX() - 20,
                 blueBallPoint.getY());
        
        //Point to Black Ball
        blackBallPoint = new V2D(finalClothPosition.getX() + 70,
                 (finalClothPosition.getY() / 2) + initialClothPosition.getY());
        
        //Big Line Points
        brownBallPoint = new V2D(initialClothPosition.getX() 
                                + (blueBallPoint.getX() - initialClothPosition.getX()) / 2,
                                blueBallPoint.getY());
        greenBallPoint = new V2D(brownBallPoint.getX(),
                                brownBallPoint.getY() / 2 + initialWoodPosition.getY() + 10);
        yellowBallPoint = new V2D(brownBallPoint.getX(),
                                brownBallPoint.getY()
                                + (greenBallPoint.getY() - initialClothPosition.getY()));
        
        //Left Up Hole
       game.getTable().getCloth().addHole(new V2D(initialClothPosition.getX(), initialClothPosition.getY()));

		// Left Bottom Hole
		game.getTable().getCloth().addHole(new V2D(initialClothPosition.getX() - 1, finalClothPosition.getY()+60));

		// Up Center Hole
		game.getTable().getCloth().addHole(new V2D(blueBallPoint.getX(), initialClothPosition.getY()-6));

		// Bottom Center Hole
		game.getTable().getCloth().addHole(new V2D(blueBallPoint.getX() + 3, finalClothPosition.getY()+((finalWoodPosition.getY()-finalClothPosition.getY())/2) + 26));

		// Right Up Hole
		game.getTable().getCloth().addHole(new V2D(finalClothPosition.getX() + 98 + (finalWoodPosition.getX() - finalClothPosition.getX()) / 2, initialClothPosition.getY()));

		// Right Bottom Hole
		game.getTable().getCloth().addHole(new V2D(finalClothPosition.getX() + 98 + (finalWoodPosition.getX()-finalClothPosition.getX())/2, finalClothPosition.getY() + 61));

        }
        
        @Override
        public void actionPerformed(ActionEvent e){
            GameState gs = game.getGameState();
            
            if(gs == GameState.TARGET_SELECTED)
                game.getCue().updateOffset();
            
            else if(gs == GameState.HIT_DONE){
                if(game.allStopped()){
                    game.setGameState(GameState.WAITING_FOR_HIT);
                }
                
                else{
                    if(game.gameOver()){
                        String winnerPlayer = new String();
                        int winnerScore = 0;
                        Player winner;
                        
                        if(game.getP1().getScore() >= game.getP2().getScore())
                            winner = game.getP1();
                        
                        else
                            winner = game.getP2();
                        
                        winnerPlayer = winner.getName();
                        winnerScore = winner.getScore();
                        
                        JOptionPane.showMessageDialog(this, winnerPlayer + " is the winner", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
                        Pair w = new Pair(winnerPlayer, winnerScore);
                        if(game.getRanking() != null)
                            game.getRanking().addElement(w);
                        
                        game.getRanking().save();
                        
                        this.game= new Game("Joao", "Luis");
                        mainWindow.add(mainMenu);
                        mainMenu.setVisible(true);
                        setVisible(false);
                    }
                    
                    else
                        game.updatePhysics(dt, initialClothPosition, finalWoodPosition);
                    
                }
                
            }
            repaint();
        }
        
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Draw(g);
        }
        
        public void Draw(Graphics g){
            DrawTable(g);
            DrawBalls(g, Ball.getRadius());
            
            DrawBoardScore(g);
            
            if(game.getGameState() != GameState.HIT_DONE)
                DrawStick(g);
        }
        
        private void DrawBoardScore(Graphics g){
            g.drawImage(scoreImage, (int) initialWoodPosition.getX() + 480, (int) finalWoodPosition.getY() + 25, scoreImage.getWidth(null), scoreImage.getHeight(null), null);
            
            g.setColor(Color.BLACK);
            g.fillOval((int) initialWoodPosition.getX() + 300, (int) finalWoodPosition.getY() + 30, 180, 70);
            g.fillOval((int) initialWoodPosition.getX() + 580, (int) finalWoodPosition.getY() + 30, 180, 70);

            if(game.getP1() == game.getActivePlayer())
                g.setColor(Color.GREEN);
            else
                g.setColor(Color.RED);
            
            g.drawString(game.getP1().getName(), (int) initialWoodPosition.getX() + 365, (int) finalWoodPosition.getY() + 55);
            g.drawString("" + game.getP1().getScore(), (int) initialWoodPosition.getX() + 368, (int) finalWoodPosition.getY() + 70);
        
            if(game.getP2() == game.getActivePlayer())
                g.setColor(Color.GREEN);
            else
                g.setColor(Color.RED);
            
            g.drawString(game.getP2().getName(), (int) initialWoodPosition.getX() + 645, (int) finalWoodPosition.getY() + 55);
             g.drawString("" + game.getP2().getScore(), (int) initialWoodPosition.getX() + 648, (int) finalWoodPosition.getY() + 70);
        
        }
        
        private void DrawTable(Graphics g){
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            
            g.setColor(new Color(200, 100, 50).darker().darker());
            g.fill3DRect((int) initialWoodPosition.getX(),
                    (int) initialWoodPosition.getY(),
                    (int) finalWoodPosition.getX(), (int) finalWoodPosition.getY()
                    , true);
            
            g.setColor(Color.GREEN.darker().darker());
            g.fill3DRect((int) initialClothPosition.getX(), 
                    (int ) initialClothPosition.getY(),
                    (int) finalClothPosition.getX(),
                    (int) finalClothPosition.getY(), false);
            
            //Big Line Table
            g.setColor(Color.GRAY);
            g.drawLine((int) brownBallPoint.getX() + 4,
                    (int) initialClothPosition.getY(),
                    (int) brownBallPoint.getX() + 4,
                    (int) finalClothPosition.getY() + 61);
            
            //Lateral line to triangle
            g.drawLine((int) upVertexTriangle.getX(), 
                    (int) upVertexTriangle.getY(),
                    (int) bottomVertexTriangle.getX(),
                    (int) bottomVertexTriangle.getY());
            
            //Buttom Line to triangle
            g.drawLine((int) bottomVertexTriangle.getX(), 
                    (int) bottomVertexTriangle.getY(),
                    (int) mediumVertexTriangle.getX(),
                    (int) mediumVertexTriangle.getY());
            
            //Up Line to triangle
            g.drawLine((int) mediumVertexTriangle.getX(), 
                    (int) mediumVertexTriangle.getY(),
                    (int) upVertexTriangle.getX(),
                    (int) upVertexTriangle.getY());
            
            //drawing points
            g.fillOval((int) blueBallPoint.getX(), (int) blueBallPoint.getY(), 8, 8);
            g.fillOval((int) blackBallPoint.getX(), (int) blackBallPoint.getY(), 8, 8);
            g.fillOval((int) brownBallPoint.getX(), (int) brownBallPoint.getY(), 8, 8);
            g.fillOval((int) greenBallPoint.getX(), (int) greenBallPoint.getY(), 8,
				8);
		g.fillOval((int) yellowBallPoint.getX(), (int) yellowBallPoint.getY(),
				8, 8);
		g.fillOval((int) pinkBallPoint.getX(), (int) pinkBallPoint.getY(), 8, 8);
               g.drawArc(
                                (int) (2 * (greenBallPoint.getX() - (yellowBallPoint.getX() / 1.5))),
                                (int) greenBallPoint.getY() + 4,
                                (int) (yellowBallPoint.getX() / 1.5),
                                (int) (yellowBallPoint.getY() - greenBallPoint.getY()), 90, 180);
               
              //Drawing borders points
              g.setColor(new Color(200, 100, 50).brighter().brighter());
              g.fillOval((int) (initialWoodPosition.getX() + (initialClothPosition
                      .getX() - initialWoodPosition.getX()) / 2) - 4,
                      (int) brownBallPoint.getY() , 8, 8);
              g.fillOval((int) (finalClothPosition.getX() + 112 + (finalWoodPosition
				.getX() - finalClothPosition.getX()) / 2), (int) brownBallPoint
				.getY(), 8, 8);
		g.fillOval(
				(int) brownBallPoint.getX(),
				(int) (initialWoodPosition.getY()
						+ ((initialClothPosition.getY() - initialWoodPosition
								.getY()) / 2) - 2), 8, 8);
		g.fillOval((int) brownBallPoint.getX(), (int) (finalClothPosition
				.getY() + ((finalWoodPosition.getY() - finalClothPosition
						.getY()) / 2)) + 40, 8, 8);
		g.fillOval(
				(int) (finalWoodPosition.getX() - (brownBallPoint.getX() - initialWoodPosition
						.getX())) + 85,
						(int) (initialWoodPosition.getY()
								+ ((initialClothPosition.getY() - initialWoodPosition
										.getY()) / 2) - 2), 8, 8);
		g.fillOval(
				(int) (finalWoodPosition.getX() - (brownBallPoint.getX() - initialWoodPosition
						.getX())) + 85,
						(int) (finalClothPosition.getY() + ((finalWoodPosition.getY() - finalClothPosition
								.getY()) / 2)) + 40, 8, 8);
               //Drawing border holes
               g.setColor(Color.BLACK);
               g.fillArc((int) game.getTable().getCloth().getHoles().get(0).getX() - 27,
                       (int) game.getTable().getCloth().getHoles().get(0).getY() - 27,
                       2 * 27, 2 * 27 , 0, 270);
               
g.fillArc((int) game.getTable().getCloth().getHoles().get(1).getX() - 26,
				(int) game.getTable().getCloth().getHoles().get(1).getY() - 26,
				2 * 27, 2 * 27, 90, 270);

		g.fillArc((int) game.getTable().getCloth().getHoles().get(2).getX() - 22,
				(int) game.getTable().getCloth().getHoles().get(2).getY() - 34,
				2 * 27, 2 * 40, 0, 180);

		g.fillArc((int) game.getTable().getCloth().getHoles().get(3).getX() - 25,
				(int) game.getTable().getCloth().getHoles().get(3).getY() - 46,
				2 * 27, 2 * 40, 180, 180);

		g.fillArc((int) game.getTable().getCloth().getHoles().get(4).getX() - 25,
				(int) game.getTable().getCloth().getHoles().get(4).getY() - 27,
				2 * 27, 2 * 27, 270, 270);

		g.fillArc((int) game.getTable().getCloth().getHoles().get(5).getX() - 26,
				(int) game.getTable().getCloth().getHoles().get(5).getY() - 27,
				2 * 27, 2 * 27, 180, 270);
                
                //drawing holes
                g.setColor(new Color(122, 139, 139));
                
                for(int i =0; i < game.getTable().getCloth().getHoles().size(); i++){
                    int x = (int) game.getTable().getCloth().getHoles().get(i).getX();
                    int y = (int) game.getTable().getCloth().getHoles().get(i).getY();
                    
                    if(i == 3){
                        x -= (Ball.getRadius() + 3);
                        y -= (Ball.getRadius() + 3);
                    }
                    
                    if( i == 1){
                        x -= Ball.getRadius() + 3;
                        y += Ball.getRadius() - 35;
                    }
                    
                    if(i == 5){
                        x -= Ball.getRadius() + 5;
                        y += Ball.getRadius() - 35;
                    }
                    
                    if(i == 0){
                        x -= (Ball.getRadius() + 5);
                        y -= (Ball.getRadius() + 5);
                    }
                    
                    else if(i == 2){
                        x -= Ball.getRadius();
                        y -= (Ball.getRadius() + 10);
                    }
                    
                    if(i == 4){
                        x -= (Ball.getRadius() + 3);
                        y -= (Ball.getRadius() + 6);
                    }
                    
                    g.fillOval(x, y, 2 * Cloth.getHoleRadius(), 2 * Cloth.getHoleRadius());
                    
                }
        }
        
        private void DrawStick(Graphics g){
            Graphics2D g2d = (Graphics2D) g;
            double rotRad, xCorrection, yCorrection;
            int cx =0, cy =0;
            
            Ball wb = game.getTable().getWhiteBall();
            V2D wbCoords = new V2D(wb.getX(), 
                                                wb.getY());
            
           rotRad = Math.toRadians(game.getCue().getRotation());
           
           xCorrection = Math.cos(rotRad)
                                * (2 * Ball.getRadius() + game.getCue().getOffset());
              
           yCorrection = Math.sin(rotRad)
                            * (2 * Ball.getRadius() + game.getCue().getOffset());
           
           cx = (int) (wbCoords.getX());
           cy = (int) (wbCoords.getY() - cueImage.getHeight(null) / 2);
           
           game.getCue().setPosition(new V2D ((cx + cueImage.getWidth(null) * Math.cos(Math.toRadians(game.getCue().getRotation()))),
                                        (cy + cueImage.getWidth(null) * Math.sin(Math.toRadians(game.getCue().getRotation())))));
           
           g2d.translate(xCorrection, yCorrection);
           g2d.rotate(rotRad, wbCoords.getX(), wbCoords.getY());
           
           g2d.drawImage(cueImage, cx, cy, cueImage.getWidth(null), cueImage.getHeight(null),
                   null);
           
        }
        
        private void DrawBalls(Graphics g, double radiusBall){
            for(int i=0; i < game.getTable().getBallSet().size(); i++ ){
                int indexColor = game.getTable().getBallSet().get(i).getColor().ordinal();
                
                if(!game.getTable().getBallSet().get(i).isPotted()){
                    g.drawImage(ballImages.get(indexColor), (int) (game.getTable().getBallSet().get(i).getX() - radiusBall),
                            (int) (game.getTable().getBallSet().get(i).getY() - radiusBall), 2 * (int) radiusBall, 2 * (int) radiusBall, null);
                }
            }
        }
        
        public double getAlpha(double dx, double dy){
            return Math.toDegrees(Math.atan(dy / dx));
        }
        
        public void calculateRotation(int mX, int mY){
            Ball wb = game.getTable().getWhiteBall();
            V2D wbC = new V2D(wb.getX(), wb.getY());
            
            double dx, dy, alpha;
            
            if(mX > wbC.getX()){
                dx = mX - wbC.getX();
                
                if(mY < wbC.getY()){
                    dy = wbC.getY() - mY;
                    alpha = getAlpha(dx, dy);
                    game.getCue().setRotation((int) (180 - alpha));
                }else{
                    dy = mY - wbC.getY();
                    alpha = getAlpha(dx, dy);
                    game.getCue().setRotation((int) (180 + alpha));
                }
            }else{
                dx = wbC.getX() - mX;
                
                if(mY < wbC.getY()){
                    dy = wbC.getY() - mY;
                    alpha = getAlpha(dx, dy);
                    game.getCue().setRotation((int) alpha);
                }else{
                    dy = mY - wbC.getY();
                    alpha = getAlpha(dx, dy);
                    game.getCue().setRotation((int) (360 - alpha));
                }
            }
        }
        
        @Override
        public void mousePressed(MouseEvent e){
            if(game.getGameState() == GameState.WAITING_FOR_HIT){
               
                switch(e.getButton()){
                    case MouseEvent.BUTTON1:
                            game.setGameState(GameState.TARGET_SELECTED);
                            break;
                     
                    default:
                        break;
                }
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent e){
            if(game.getGameState() == GameState.TARGET_SELECTED){
                game.cueHit();
                game.getCue().resetOffset();
                game.setGameState(GameState.HIT_DONE);
            }
        }
        
        @Override
        public void mouseMoved(MouseEvent e){
            if(game.getGameState() == GameState.WAITING_FOR_HIT){
                calculateRotation(e.getX(), e.getY());
            }
            repaint();
        }
        
        @Override
        public void keyPressed(KeyEvent arg0){
            switch(arg0.getKeyCode()){
                case KeyEvent.VK_D:
                    game.getCue().incRotation();
                    if(game.getCue().getRotation() > 360)
                        game.getCue().setRotation(0);
                    break;
                    
                case KeyEvent.VK_A:
                    game.getCue().decRotation();
                    if(game.getCue().getRotation() < 0)
                        game.getCue().setRotation(360);
                    break;
                
            }
            repaint();
        }
        
        @Override
        public void mouseDragged(MouseEvent e){
            
        }
        
        @Override
        public void keyReleased(KeyEvent e){
            
        }
        
        @Override
        public void keyTyped(KeyEvent e){
            
        }
        
        @Override
        public void mouseClicked(MouseEvent e){
            
        }
        
        @Override
        public void mouseEntered(MouseEvent e){
            
        }
        
        @Override
        public void mouseExited(MouseEvent e){
            
        }
        
        @Override
          public void run(){
              
          }
}
