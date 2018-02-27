package cipher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 150621C
 */
public class Encryption {

    FileReader fr = null;
    FileWriter fw = null;
    BufferedReader br = null;
    BufferedWriter bw = null;
    File file;
    
    String sCurrentLine;
    String originfilepath = "";
    String encryptfile = "/media/mahesh/New Volume/Semester 5 works/Computer Security/Cipher/encrypted.txt";

    String key = "Mahesh";
    String asciistr = "";

    
    String dekey = "";

    ArrayList<CharSequence> h1;
    ArrayList<CharSequence> h2;

    public Encryption() {
        h1 = new ArrayList<>();
        h2 = new ArrayList<>();
    }

    public void read() {
        try {
            fr = new FileReader(originfilepath);
            file = new File(encryptfile);
            fw = new FileWriter(file);
            
            if (!file.exists()) {
	     file.createNewFile();
            }
            
            br = new BufferedReader(fr);
            bw = new BufferedWriter(fw);

            while ((sCurrentLine = br.readLine()) != null) {
                //System.out.println(sCurrentLine);
                encrypt(sCurrentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Cipher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void encrypt(String sCurrentLine) {
        String substitude = "";
        int asciiadd = 0;

        for (int x = 0; x < key.length(); x++) {
            int ascii = (int) key.charAt(x);
            asciistr += String.valueOf(ascii);
            asciiadd += ascii;

        }
        if (asciiadd > 96) {
            asciiadd = asciiadd % 96;
        }
        for (int i = 0; i < sCurrentLine.length(); i++) {
            int asciitext = (int) sCurrentLine.charAt(i) + asciiadd;
            if (asciitext > 126) {
                asciitext = 32 + asciitext - 126;
            }
            substitude += String.valueOf((char) asciitext);
            //System.out.print((char) asciitext);
        }
        if(substitude.length()%4 != 0){
            for(int x=0; x<substitude.length()%4; x++){
                substitude += " ";
            }
        }
        calculatedekey();
        System.out.println(substitude);
        permute(substitude);
        //substitude += "\n";
    }

    public void calculatedekey() {
        for (int y = 0; y < asciistr.length(); y++) {            
            int num = Integer.parseInt(String.valueOf(asciistr.charAt(y)));
            if (dekey.length() < 4) {
                if (num > 0 && num < 5 && dekey.indexOf(asciistr.charAt(y))<0) {
                    //System.out.println(num);
                    dekey += String.valueOf(num);
                } else {
                    int remainder = num % 5;
                    //System.out.println(num + "," + remainder);
                    if (remainder > 0 && dekey.indexOf(String.valueOf(remainder))<0) {
                        dekey += String.valueOf(remainder);
                    }
                }
            }
        }
        while(dekey.length()<4){
            if(dekey.indexOf('1')<0){
                dekey += "1";
            }
            else if (dekey.indexOf('2')<0){
                dekey += "2";
            }
            else if (dekey.indexOf('3')<0){
                dekey += "3";
            }else{
                dekey += "4";
            }
        }
        //System.out.println(dekey);                
    }
    
    public void permute(String substitude) {
        String permute = "";
        ArrayList<String> key = new ArrayList<String>();
        for(int x=0; x< dekey.length(); x++){
            key.add(String.valueOf(dekey.charAt(x)));
        }
        //System.out.println(key);
        for(int i=0; i<substitude.length(); i+=4){
            try{
                String sub = substitude.substring(i, i+4);
                //System.out.println(sub);
                
                String substr="";
                for(int x=0; x<sub.length(); x++){
                    //System.out.println(Integer.valueOf(key.get(x)));
                    substr += sub.charAt(Integer.valueOf(key.get(x))-1);
                }
                //System.out.println(substr);
                permute += substr;
                System.out.println(permute);
                

	  
            }catch(StringIndexOutOfBoundsException ex){
                System.out.println("out of bound");                
            }           
        }
        try {
                    bw.write(permute);
                    bw.newLine();
                    //System.out.println(i); 
                } catch (IOException ex) {
                    Logger.getLogger(Encryption.class.getName()).log(Level.SEVERE, null, ex);
                }
        
    }
    
}
