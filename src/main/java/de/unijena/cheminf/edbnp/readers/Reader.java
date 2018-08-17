package de.unijena.cheminf.edbnp.readers;


import org.apache.commons.math3.util.Pair;
import org.openscience.cdk.interfaces.IAtomContainer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public interface Reader {

    Pair readFile(File file);

    IAtomContainer checkMolecule(IAtomContainer molecule);


    ArrayList<IAtomContainer> returnCorrectMolecules();

}
