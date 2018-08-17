package de.unijena.cheminf.edbnp.readers;

import de.unijena.cheminf.edbnp.misc.MoleculeChecker;
import org.apache.commons.math3.util.Pair;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smiles.SmilesParser;


import java.io.*;
import java.util.ArrayList;
import java.util.Map;


public class SMILESReader implements Reader {

    File file;
    ArrayList<IAtomContainer> listOfMolecules;

    private LineNumberReader smilesReader;



    public SMILESReader(){

        this.listOfMolecules = new ArrayList<IAtomContainer>();




    }





    @Override
    public Pair readFile(File file) {

        this.file = file;
        int count = 1;
        String line;

        System.out.println("\n\n Working on: "+this.file.getName() + "\n\n");
        System.out.println("\n\n Working on: "+this.file.getAbsolutePath() + "\n\n");



        try {
            smilesReader = new LineNumberReader(new InputStreamReader(new FileInputStream(file)));
            System.out.println("SMILES reader creation");


            while ((line = smilesReader.readLine()) != null) {
                String smiles_names = line;
                if(!line.contains("smiles")) {
                    try {
                        String[] splitted = smiles_names.split("\\s+"); //splitting the canonical smiles format: SMILES \s mol name
                        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());

                        IAtomContainer molecule = null;
                        try {
                            molecule = sp.parseSmiles(splitted[0]);
                            Map properties = molecule.getProperties();

                            //List<IAtomContainer> fragments = score(molecule);

                            molecule.setProperty("MOL_NUMBER_IN_FILE", Integer.toString(count));
                            molecule.setProperty("ID", splitted[1]);
                            molecule.setID(splitted[1]);


                            listOfMolecules.add(molecule);
                        } catch (InvalidSmilesException e) {
                            e.printStackTrace();
                            smilesReader.skip(count - 1);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    count++;
                }


            }
            smilesReader.close();



        } catch (IOException ex) {
            System.out.println("Oops ! File not found. Please check if the -in file or -out directory is correct");
            ex.printStackTrace();
            System.exit(0);
        }
        return new Pair<Integer, Integer>(count-1, listOfMolecules.size());

    }

    @Override
    public IAtomContainer checkMolecule(IAtomContainer molecule) {

        MoleculeChecker mc = new MoleculeChecker(molecule);
        molecule = mc.checkMolecule();
        return molecule;
    }

    @Override
    public ArrayList<IAtomContainer> returnCorrectMolecules() {
        return this.listOfMolecules;
    }
}
