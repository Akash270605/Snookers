/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

/**
 *
 * @author Leveno
 */
public enum BallColor {
    
    WHITE, RED, YELLOW, GREEN, BROWN, BLUE, PINK, BLACK;
    
    public static BallColor getColorFromValue(int n) { return BallColor.values()[n]; }
    public static int getValueFromColor(BallColor c) { return c.ordinal(); } 
};
