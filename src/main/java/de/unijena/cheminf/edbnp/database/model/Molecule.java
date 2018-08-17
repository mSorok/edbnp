package de.unijena.cheminf.edbnp.database.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Molecule {





    @Id
    @GeneratedValue(generator = "np-id-generator") //TODO
    @GenericGenerator(name = "np-id-generator", //TODO
            parameters = @Parameter(name = "prefix", value = "NP"),
            strategy = "de.unijena.cheminf.edbnp.database.misc.NPIdentifierGenerator")
    private String uniqueMID;

    private String Name;

    private String SMILES;

    private String InChi;

    private int isANP = 1;

    private String formula;

    private String creation_date;

    private String last_modified;

    private String level_of_curation;





    public String getUniqueMID() {
        return uniqueMID;
    }

    public void setUniqueMID(String uniqueMID) {
        this.uniqueMID = uniqueMID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSMILES() {
        return SMILES;
    }

    public void setSMILES(String SMILES) {
        this.SMILES = SMILES;
    }

    public String getInChi() {
        return InChi;
    }

    public void setInChi(String inChi) {
        InChi = inChi;
    }

    public int getIsANP() {
        return isANP;
    }

    public void setIsANP(int isANP) {
        this.isANP = isANP;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(String last_modified) {
        this.last_modified = last_modified;
    }

    public String getLevel_of_curation() {
        return level_of_curation;
    }

    public void setLevel_of_curation(String level_of_curation) {
        this.level_of_curation = level_of_curation;
    }








}
