package de.unijena.cheminf.edbnp.readers;



import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.math3.util.Pair;
import org.openscience.cdk.interfaces.IAtomContainer;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;


import java.io.File;


/**
 * @author mSorok
 */




public class ReadWorker {

    File inFile;

    private boolean acceptFileFormat = false;
    private String submittedFileFormat ;


    private ArrayList<IAtomContainer> readMolecules ;



    private Pair<Integer, Integer> fileStats ; //0=total number of molecules in file, 1=number of valid molecules to be integrated




    public ReadWorker(String loadedFile){
        File file = new File(loadedFile);
        System.out.println("\n\n Working on: "+file.getName() + "\n\n");
        System.out.println("\n\n Working on: "+file.getAbsolutePath() + "\n\n");

        inFile = file;

        acceptFileFormat = acceptFile(loadedFile);


        //TODO options (add options later)




    }




    private boolean acceptFile(String filename) {
        filename = filename.toLowerCase();
        if (filename.endsWith("sdf") || filename.toLowerCase().contains("sdf".toLowerCase())) {
            this.submittedFileFormat="sdf";
            return true;
        } else if (filename.endsWith("smi")  ||
                filename.toLowerCase().contains("smi".toLowerCase()) ||
                filename.toLowerCase().contains("smiles".toLowerCase()) ||
                filename.toLowerCase().contains("smile".toLowerCase())) {
            this.submittedFileFormat="smi";
            return true;
        } else if (filename.endsWith("json")) {
            return false;
        }
        else if (filename.endsWith("mol")  ||
                filename.toLowerCase().contains("mol".toLowerCase())
                || filename.toLowerCase().contains("molfile".toLowerCase())) {
            this.submittedFileFormat="mol";
            return true;
        }


        return false;
    }






    public boolean startWorker(){


        if(acceptFileFormat){
            this.readMolecules = doWork();
            return true;
        }
        else{
            return false;
        }

    }



    public ArrayList<IAtomContainer> doWork(){

        Reader reader = null ;

        if(this.submittedFileFormat.equals("mol")){
            reader = new MOLReader();
        }
        else if(this.submittedFileFormat.equals("sdf")){
            reader = new SDFReader();
        }
        else if(this.submittedFileFormat.equals("smi")){
            reader = new SMILESReader();
        }

        this.fileStats = reader.readFile(this.inFile);
        this.readMolecules = reader.returnCorrectMolecules();

        return readMolecules;
    }



    public Pair<Integer, Integer> getFileStats() {
        return fileStats;
    }



}
