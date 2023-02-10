/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tinycompiler;
/**
 *
 * @author tolan
 */
import java.util.ArrayList;
public class Scanner {
    private enum State {
        START, INCOMMENT, INNUM, INID, INASSIGN, DONE
    }
    private String file;
    private int index;
    private ArrayList<Token> tokens = new ArrayList<Token>();
    private int pos;
    private int tokenStart;
    private int fileSize;
    private State state;
    private String tok;
    
    public Scanner(String data){
        file = data;
        pos = 0;
        index = 0;
        tokenStart = 0;
        fileSize = file.length();
        state = State.START;
        while(pos<fileSize){
            char c = file.charAt(pos);
            switch(state){
                case START:
                    switch(c){
                        case ' ':
                            pos++;
                            continue;
                        case '+':
                            tokens.add(new Token("PLUS", "+", pos));
                            pos++;
                            continue;
                        case '-':
                            tokens.add(new Token("MINUS", "-", pos));
                            pos++;
                            continue;
                        case '*':
                            tokens.add(new Token("MULT", "*", pos));
                            pos++;
                            continue;
                        case '/':
                            tokens.add(new Token("DIV", "/", pos));
                            pos++;
                            continue;
                        case '=':
                            tokens.add(new Token("EQUAL", "=", pos));
                            pos++;
                            continue;
                        case '<':
                            tokens.add(new Token("LESSTHAN", "<", pos));
                            pos++;
                            continue;
                        case '>':
                            tokens.add(new Token("GREATERTHAN", ">", pos));
                            pos++;
                            continue;
                        case '(':
                            tokens.add(new Token("OPENBRACKET", "(", pos));
                            pos++;
                            continue;
                        case ')':
                            tokens.add(new Token("CLOSEDBRACKET", ")", pos));
                            pos++;
                            continue;
                        case ';':
                            tokens.add(new Token("SEMICOLON", ";", pos));
                            pos++;
                            continue;
                        case ':':
                            state = State.INASSIGN;
                            tokenStart = pos;
                            pos++;
                            continue;
                        case '{':
                            state = State.INCOMMENT;
                            tokenStart = pos;
                            pos++;
                            continue;
                        default:
                            if(Character.isAlphabetic(c)){
                                state = State.INID;
                                tokenStart = pos;
                                pos++;
                            }else if(Character.isDigit(c)){
                                state = State.INNUM;
                                tokenStart = pos;
                                pos++;
                            }else{
                                pos++;
                            }
                    }
                    continue;
                case INNUM:
                    if(Character.isDigit(c)){
                        pos++;
                    }else{
                    tok = file.substring(tokenStart, pos);
                    tokens.add(new Token("NUMBER", tok, pos-1));
                    state = State.START;
                    }
                    continue;
                case INID:
                    if(Character.isAlphabetic(c)){
                        pos++;
                    }else{
                        tok = file.substring(tokenStart, pos);
                        if("if".equals(tok)){
                            tokens.add(new Token("IF", tok, pos));
                            state = State.START;
                        }else if("then".equals(tok)){
                            tokens.add(new Token("THEN", tok, pos));
                            state = State.START;
                        }else if("else".equals(tok)){
                            tokens.add(new Token("ELSE", tok, pos));
                            state = State.START;
                        }else if("repeat".equals(tok)){
                            tokens.add(new Token("REPEAT", tok, pos));
                            state = State.START;
                        }else if("until".equals(tok)){
                            tokens.add(new Token("UNTIL", tok, pos));
                            state = State.START;
                        }else if("end".equals(tok)){
                            tokens.add(new Token("END", tok, pos));
                            state = State.START;
                        }else if("write".equals(tok)){
                            tokens.add(new Token("WRITE", tok, pos));
                            state = State.START;
                        }else if("read".equals(tok)){
                            tokens.add(new Token("READ", tok, pos));
                            state = State.START;
                        }else{
                            Token t = new Token("IDENTIFIER", tok, pos);
                            tokens.add(t);
                            state = State.START;
                        }
                    }
                    continue;
                case INASSIGN:
                    if(c == '='){
                        tokens.add(new Token("ASSIGN", ":=", pos));
                        pos++;
                        state = State.START;
                    }else{
                        state = State.START;
                    }
                    continue;
                case INCOMMENT:
                    if(c == '}'){
                        state = State.START;
                    }
                    pos++;
            } 
        }
        switch(state){
            case INID:
                tok = file.substring(tokenStart, pos);
                    if("if".equals(tok)){
                        tokens.add(new Token("IF", tok, pos));
                    }else if("then".equals(tok)){
                        tokens.add(new Token("THEN", tok, pos));
                    }else if("else".equals(tok)){
                        tokens.add(new Token("ELSE", tok, pos));
                    }else if("repeat".equals(tok)){
                        tokens.add(new Token("REPEAT", tok, pos));
                    }else if("until".equals(tok)){
                        tokens.add(new Token("UNTIL", tok, pos));
                    }else if("end".equals(tok)){
                        tokens.add(new Token("END", tok, pos));
                    }else if("write".equals(tok)){
                        tokens.add(new Token("WRITE", tok, pos));
                    }else if("read".equals(tok)){
                        tokens.add(new Token("READ", tok, pos));
                    }else if("end".equals(tok)){
                        tokens.add(new Token("END", tok, pos));
                    }else{
                        tokens.add(new Token("IDENTIFIER", tok, pos));
                    }
                    break;
            case INNUM:
                tok = file.substring(tokenStart, pos);
                tokens.add(new Token("NUMBER", tok, pos-1));
                break;
        }
    }
    
    public String getTokens(){
        String tokenData = "";
        for(int i = 0; i < tokens.size(); i++){
            tokenData += tokens.get(i).getTokenVal() + ", " + tokens.get(i).getTokenType() + "\n";
        }
        return tokenData;
    }
    
    public Token nextToken(){
        if(index>=tokens.size()){
            return new Token("EOF", "", -1);
        }
        return tokens.get(index++);
    }

}
