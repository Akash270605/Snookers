/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

/**
 *
 * @author Leveno
 */
public class V2D {
    private double x;
    private double y;
    private double norm;
    
    public V2D(){
        this.x = 0.0;
        this.y = 0.0;
    }
    
    public V2D(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public V2D(V2D p){
        this.x = p.getX();
        this.y = p.getY();
    }
    
    
    //getter - setter
    public double getX() { return x; }
    
    public void setX(double x) { this.x = x; }
    
    public double getY() { return y; }
    
    public void setY(double y) { this.y = y; }
    
    public boolean isNull() { return x == 0 && y == 0; }
    
    
    //operations
    public double norm() { return Math.sqrt(x*x + y*y); }
    
    public void add(V2D v1) { x += v1.x; y+= v1.y; }
    
    public void subtract(V2D v1) { x -= v1.x; y -= v1.y; }
    
    public static V2D subract(V2D v1, V2D v2) { return new V2D (v1.x - v2.x, v1.y - v2.y); }
    
    public static V2D multiply(V2D v1, double n) { return new V2D(v1.x*n, v1.y*n); }
    
    public void normalize() { multiply(1/norm()); } 
    
    public static V2D normalize(V2D v1){ return new V2D(V2D.multiply(v1, 1.0/v1.norm())); }
    
    public static V2D invertXY(V2D v1) { return new V2D(-v1.x, -v1.y); }
    
    public void invertXY() { x *= -1; y *= -1; }
    
    public void invertX() { x *= -1; }
    
    public void invertY() { y *= -1; }
    
    public void multiply(double n) { x *= n; y *= n; }
    
    public static double dotProduct(V2D v1, V2D v2) { return (v1.x*v2.x + v1.y*v2.y); }
    
    public static double cos(V2D v1, V2D v2) { return dotProduct(v1, v2) / (v1.norm() * v2.norm()); }
    
    public static double angle(V2D v1, V2D v2) { return Math.acos(cos(v1, v2)); }
    
    public static double sin(V2D v1, V2D v2) { return Math.sin(angle(v1, v2)); }
    
    public String toString(){
        return "(" + x + "," + y + ")";
    }
    
    public double getNorm(){
        return norm;
    }
    
    public void setNorm(double norm){
        this.norm = norm;
    }
}
