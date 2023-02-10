/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tinycompiler;

/**
 *
 * @author tolan
 */
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class File {
    public static String readFile(String path){
        String output = "";
        try{
            FileReader reader = new FileReader(path);
            int character;
            while((character = reader.read()) != -1){
                output += (char) character;
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return output;
    }
    public static void writeFile(String data){
        try{
            FileWriter writer = new FileWriter("output.txt", true);
            writer.write(data);
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
