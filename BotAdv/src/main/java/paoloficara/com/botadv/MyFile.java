/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paoloficara.com.botadv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ficara_paolo
 */
public class MyFile {

    private String filename;

    public MyFile(String filename) {
        this.filename = filename;
    }

    public List<String[]> leggi() {
        List<String[]> strings = new ArrayList<String[]>();

        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);

            String line;
            while (true) {
                line = br.readLine();
                if (line == null) {
                    break;
                }
                strings.add(line.split(";"));
            }
        } catch (IOException ex) {
            Logger.getLogger(MyFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        return strings;
    }

    public void scrivi(String p) {
        try {
            FileWriter fw = new FileWriter(filename, true);
            fw.append(p.toString() + '\n');
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(MyFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void riscriviFile(List<String[]> strings) {
        clear();
        try {
            FileWriter fw = new FileWriter(filename, true);
            for(String[] x : strings){
                for(String s : x){
                    fw.append(s);
                }
                fw.append("\n");
            }
                
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(MyFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clear() {
        PrintWriter writer;
        try {
            writer = new PrintWriter(filename);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MyFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
