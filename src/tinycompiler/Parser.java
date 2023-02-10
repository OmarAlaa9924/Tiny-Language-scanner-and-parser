/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tinycompiler;

import GraphViz.SyntaxTree;


/**
 *
 * @author tolan
 */
public class Parser {
    
    Scanner scanner;
    Token currToken;
    private SyntaxTree tree;
    
    void match(String t) throws SyntaxException{
        if(!t.equals(currToken.getTokenType())){
            throw new SyntaxException("Syntax error near " + t);
        }
    }
    
    void consume(){
        currToken = this.scanner.nextToken();
    }
    
    public Parser(Scanner scanner){
        this.scanner = scanner;
        this.currToken = scanner.nextToken();
        this.tree = new SyntaxTree();
    }
    
    public void parse() throws SyntaxException{
        statementSequence();
        if(!(currToken.getTokenType().equals("EOF"))){
            throw new SyntaxException("missing SEMICOLON ");
        }
        tree.end();
  
    }
    public SyntaxTree getTree(){
        return tree;
    }
   
    
    private int expression() throws SyntaxException{
        int simp_exp =simpleExpression();
        Token ftoken = currToken;
        while(ftoken.getTokenType().equals("EQUAL")||ftoken.getTokenType().equals("LESSTHAN")||ftoken.getTokenType().equals("GREATERTHAN")){
            int opnode_ID = comparisonOp();
            tree.addChild(opnode_ID,simp_exp);
            tree.addChild(opnode_ID,simpleExpression());
           simp_exp = opnode_ID;
           ftoken = currToken;
        }
       return simp_exp;
    }
    private int comparisonOp() throws SyntaxException{
		Token ftoken = currToken;
		switch(ftoken.getTokenType()){
		case "LESSTHAN":
			match("LESSTHAN");
                        consume();
			return tree.makeOPNode("<");
		case "EQUAL":
			match("EQUAL");
                        consume();
			return tree.makeOPNode("=");
		case "GREATERTHAN":
			match("GREATERTHAN");
                        consume();
			return tree.makeOPNode(">");
		default:
			throw new SyntaxException("Syntax error near " +ftoken.getTokenType());
		}
	} 
    
    private int simpleExpression() throws SyntaxException{
        int term = term(); 
        while(currToken.getTokenType().equals("PLUS") || currToken.getTokenType().equals("MINUS")){
            int opnode_ID = addOp();
            tree.addChild(opnode_ID,term);
            tree.addChild(opnode_ID, term());
            term = opnode_ID;
        }
        return term;        
    }
    
    
    private int addOp() throws SyntaxException{
		Token ftoken = currToken;
		switch(ftoken.getTokenType()){
		case "PLUS":
			match("PLUS");
                        consume();
			return tree.makeOPNode("+");
		case "MINUS":
			match("MINUS");
                        consume();
			return tree.makeOPNode("-");
		default:
			throw(new SyntaxException("Syntax error near " +ftoken.getTokenType()) );
		}
	} 

    private int term() throws SyntaxException{
        int factor = factor();
        while(currToken.getTokenType().equals("MULT") || currToken.getTokenType().equals("DIV")){
            int opnode_ID = mulOp();
            tree.addChild(opnode_ID,factor);
            tree.addChild(opnode_ID,factor());
           factor = opnode_ID;
        }
        return factor;
        
    }
    
     private int mulOp() throws SyntaxException{
		Token ftoken = currToken;
		switch(ftoken.getTokenType()){
		case "DIV":
			match("DIV");
                        consume();
			return tree.makeOPNode("/");
		case "MULT":
			match("MULT");
                        consume();
			return tree.makeOPNode("*");
		default:
			throw(new SyntaxException("Syntax error near " +ftoken.getTokenType()) );
		}
	} 
     
    private int factor() throws SyntaxException{
        int result = 0 ;
        if(currToken.getTokenType().equals("OPENBRACKET")){
            consume();
            int exp = expression();
            match("CLOSEDBRACKET");
            consume();
            result =  exp;
        }else if(currToken.getTokenType().equals("NUMBER")){
            int constNode_ID = tree.makeConstNode(currToken.getTokenVal());
            consume();
            result =  constNode_ID;
        }else if(currToken.getTokenType().equals("IDENTIFIER")){
            int constNode_ID = tree.makeIDNode(currToken.getTokenVal());
            consume();
            result =  constNode_ID;
        }
        else{
            throw(new SyntaxException("Syntax error near " +currToken.getTokenType()) );
        }
        return result;
    }
  
    
    private int  statementSequence() throws SyntaxException{
        int stmt = statement();
        int stmt_seq = stmt;
        Token ftoken = currToken; 
        while(ftoken.getTokenType().equals("SEMICOLON")){
            match("SEMICOLON");
            consume();
            int new_stmt = statement();
            tree.addChild(stmt, new_stmt);
            tree.sameRank(stmt, new_stmt);
            stmt = new_stmt;
            ftoken = currToken;
        }
        return stmt_seq;
    }
    
    private int statement() throws SyntaxException{
        Token fToken = currToken;
        if(fToken.getTokenType().equals("IF")){
            return ifStatement();
        }else if(fToken.getTokenType().equals("WRITE")){
            return writeStatement();
        }else if(fToken.getTokenType().equals("READ")){
            return readStatement();
        }else if(fToken.getTokenType().equals("IDENTIFIER")){
            return assignStatement();
        }else if(fToken.getTokenType().equals("REPEAT")){
            return repeatStatement();
        }
        else {
           throw new SyntaxException("Syntax Error near  " + fToken.getTokenType());
        }
    }
    
    private int ifStatement() throws SyntaxException{
        match("IF");
        consume();
        int ifnode_ID = tree.makeIFNode();
        tree.addChild(ifnode_ID, expression());
        match("THEN");
        consume();
        tree.addChild(ifnode_ID,statementSequence());
        Token ftoken = currToken;
        if(ftoken.getTokenType().equals("ELSE")){
            match("ELSE");
            consume(); 
            tree.addChild(ifnode_ID,statementSequence());
        }
        match("END");
        consume();
        return ifnode_ID ;
    }
    
    private int writeStatement() throws SyntaxException{
         match("WRITE");
         consume();
        int writenode_ID = tree.makeWriteNode();
         tree.addChild(writenode_ID, expression());
        return writenode_ID ;
    }
    
    private int readStatement() throws SyntaxException{
        match("READ");
        consume();
        Token ftoken = currToken;
        match("IDENTIFIER");
        consume();
       int readnode_ID = tree.makeReadNode(ftoken.getTokenVal());
         
        return readnode_ID;
    }
    
    private int assignStatement() throws SyntaxException{
        Token ftoken = currToken;
        match("IDENTIFIER");
        consume();
        match("ASSIGN");
        consume();
        int assignNode_ID = tree.makeAssignNode(ftoken.getTokenVal());
        tree.addChild(assignNode_ID, expression());
        return assignNode_ID;
    }
    
    private int repeatStatement() throws SyntaxException{
        match("REPEAT");
        consume();
        int repeatnode_ID = tree.makeRepeatNode();
        int body =  statementSequence();
        match("UNTIL");
        consume();
       int test = expression();
        tree.addChild(repeatnode_ID,test);
        tree.addChild(repeatnode_ID,body);
        return repeatnode_ID;
    }
}
