/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tinycompiler;

/**
 *
 * @author tolan
 */
public class Token {
    private int index;
    private String type;
    private String tokenVal;
    
    public Token(String type, String val, int pos){
        this.tokenVal = val;
        this.type = type;
        this.index = pos;
    }
    
    public String getTokenVal(){
        return this.tokenVal;
    }
    
    public String getTokenType(){
        return this.type;
    }
}
