/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import physics.*;

public class Game {
    private Table table;
    private Player p1;
    private Player p2;
    private Player activePlayer;
    private GameState gameState = GameState.WAITING_FOR_HIT;
    private List<V2D> ballPositions = new ArrayList<>();
    
    private static Ranking ranking = new Ranking();
    
    private boolean areRedsOnTable = true;
    private int firstBallHit;
    private boolean newMove = true;
    private boolean potted = false;
    
    private boolean gameOver = false;
    
    public Game(String player1Name, String player2Name){
        this.table = new Table(20, 0.99, 20, 2.54, 1.27);
        this.setP1(new Player(player1Name));
        this.setP2(new Player(player2Name));
        this.activePlayer = p1;
        
        ranking = ranking.load();
        if(ranking == null)
            ranking = new Ranking();
    }
    
    
    //getter - setter
    public Player getP1() { return p1; }
    
    public void setP1 (Player p1) { this.p1 = p1; }
    
    public Player getP2() { return p2; }
    
    public void setP2(Player p2)  { this.p2 = p2; }
    
    public Table getTable() { return table; }
    
    public Cue getCue() { return table.getCue(); } 
    
    public GameState getGameState() { return gameState; }
    
    public void setGameState(GameState gameState) { this.gameState = gameState; }
    
    public boolean getAreRedsOnTable() { return areRedsOnTable; }
    
    
    public void setAreRedsOnTable(){
        for(int i=1; i < 16; ++i)
            if(!table.getBallSet().get(i).isPotted())
                return;
        
        areRedsOnTable = false;
    }
    
    public List<V2D> getBallPosition(){
        return ballPositions;
    }
    
    public void setBallPositions(List<V2D> ballPositions){
        this.ballPositions = ballPositions;
    }
    
    public Player getActivePlayer() { return activePlayer; }
    
    public void changeActivePlayer(){
        if(activePlayer == p1)
            activePlayer = p2;
        else 
            activePlayer = p1;
    }
    
    public Ranking getRanking(){
        return ranking;
    }
    
    public boolean gameOver(){
        return gameOver;
    }
    
    
    //physics
    public void cueHit(){
        Cue cue = getCue();
        double forceMod = cue.getOffset() * cue.getStrength() / Cue.getMaxOffset();
        
        V2D forceToApply = new V2D(table.getWhiteBall().getPosition());
        forceToApply.subtract(getCue().getPosition());
        
        forceToApply.normalize();
        
        forceToApply.multiply(forceMod);
        
        table.getWhiteBall().setForce(forceToApply);
    }
    
    public void updatePhysics(double dt, V2D initialClothPosition, V2D finalClothPosition){
        
        //local variable
        Vector<Ball> balls = table.getBallSet();
        Vector<V2D> holes  = table.getCloth().getHoles();
        double frictionMod = table.getCloth().getFriction();
        
        if(!allStopped()){
            
            //handling collisions
            for(int i =0; i < balls.size(); ++i){
                Ball a = balls.get(i);
                if(a.isPotted())
                    continue;
                
                Collisions.handleBorderCollision(a, initialClothPosition, finalClothPosition);
                Ball ball = Collisions.handlePotting(a, holes, potted);
                if(ball != null){
                    activePlayer.setBallPotted(ball);
                    setAreRedsOnTable();
                    potted = true;
                }
                
                for(int j=0; j < balls.size(); ++j){
                    if(i >= j)
                        continue;
                    
                    Ball b = balls.get(j);
                    if(b.isPotted())
                        continue;
                    
                    int temp = Collisions.handleBallCollision(a, b);
                    
                    if(temp != -1){
                        if(newMove){
                            firstBallHit = temp;
                            newMove =false;
                        }
                    }
                }
            }
            
            for(Ball a : balls){
                if(a.isPotted())
                    continue;
                
                a.updateForce(frictionMod);
                a.updateVelocity();
                a.updatePosition(dt);
            }
            
            if(allStopped())
                checkGameFaults();
        }
        else
            gameState = GameState.WAITING_FOR_HIT;
        
        if(verifyGameOver()){
            checkGameFaults();
            gameOver = true;
        }
    }
    
    private boolean verifyGameOver(){
        Vector<Ball> balls = getTable().getBallSet();
        for(int i =1; i < balls.size(); i++)
            if(!balls.get(i).isPotted())
                return false;
        
        return true;
    }
    
    public boolean allStopped(){
        Vector<Ball> balls = table.getBallSet();
        
        for(Ball a : balls)
            if(a.isMoving())
                return false;
        
        return true;
    }
    
    //rules/ faults
    
    private void checkGameFaults(){
        boolean fault = false;
        
        if(whiteBallReposition())
            fault = true;
        
        boolean invalidHit = invalidBallHit();
        if(invalidHit)
            fault = true;
        
        colorBallReposition(invalidHit);
        
        if(fault){
            changeTurn();
            activePlayer.updateScore(4);
        }
        else{
            Ball ball = activePlayer.getBallPotted();
            
            if(ball == null){
                changeTurn();
            }
            else{
                activePlayer.updateScore(ball.getValue());
                nextMove();
            }
        }
    }
    
    private boolean invalidBallHit(){
        if(!activePlayer.getValidBalls().contains(firstBallHit))
            return true;
        
        return false;
    }
    
    private boolean whiteBallReposition(){
        Ball white = getTable().getWhiteBall();
        
        if(white.isPotted()){
            V2D pos = new V2D(ballPositions.get(0));
            System.out.println(pos);
            
            while(!validSpot(pos))
                findNewSpot(pos);
            
            white.setPosition(pos);
            white.setPotted(false);
            return true;
        }
        else
            return false;
    }
    
    private void colorBallReposition(boolean invalidHit){
        Ball pottedBall = activePlayer.getBallPotted();
        int pottedValue;
        
        if(pottedBall == null)
            return;
        
        else{
            pottedValue = pottedBall.getValue();
            if(pottedValue < 2)
                return;
            
            else if(areRedsOnTable || invalidHit){
                V2D pos = new V2D(ballPositions.get(pottedValue));
                System.out.println(pos);
                
                while(!validSpot(pos))
                    findNewSpot(pos);
                
                pottedBall.setPosition(pos);
                pottedBall.setPotted(false);
            }
        }
    }
    
    private boolean validSpot(V2D pos){
        final Vector<Ball> balls = getTable().getBallSet();
        for(Ball a : balls){
            Ball temp = new Ball(BallColor.WHITE);
            temp.setPosition(pos);
            if(Collisions.ballsColliding(a, temp))
                return false;
        }
        return true;
    }
    
    private void findNewSpot(V2D pos){
        double r = Ball.getRadius();
        pos.add(new V2D(r, 0));
    }
    
    // player turns
    private void nextMove(){
        activePlayer.getNextBall(areRedsOnTable, table.getBallSet());
        
        newMove = true;
        firstBallHit = -1;
        
        potted = false;
        activePlayer.setBallPotted(null);
    }
    
    private void changeTurn(){
        changeActivePlayer();
        activePlayer.setLastBallWasRed(false);
        activePlayer.getNextBall(areRedsOnTable, table.getBallSet());
        
        newMove = true;
        firstBallHit = -1;
        potted = false;
        activePlayer.setBallPotted(null);
    }
}
