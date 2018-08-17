package de.unijena.cheminf.edbnp.database;

import de.unijena.cheminf.edbnp.database.model.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smiles.SmiFlavor;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import org.apache.commons.lang3.StringUtils;


import java.util.HashMap;
import java.util.Map;

public class MoleculeCreator {
    SmilesGenerator smilesGenerator;

    public MoleculeCreator(){


        this.smilesGenerator = new SmilesGenerator(SmiFlavor.Absolute);

    }



    public Molecule createMolecule(IAtomContainer container){
        Molecule nmol = new Molecule();

        nmol.setFormula( MolecularFormulaManipulator.getMolecularFormula(container).toString()  );


        try {

            // generate InChi
            InChIGenerator gen = InChIGeneratorFactory.getInstance().getInChIGenerator(container);
            nmol.setInChi( gen.getInchi() );

            //generate SMILES
            nmol.setSMILES( this.smilesGenerator.create(container)  );


        } catch (CDKException e) {
            e.printStackTrace();
        }


        Map proprieties =  container.getProperties();
        for(Object p:proprieties.keySet()){
            if(   StringUtils.containsIgnoreCase(p.toString(),
                    "name")   ){

            }
        }


        return nmol;
    }


}
