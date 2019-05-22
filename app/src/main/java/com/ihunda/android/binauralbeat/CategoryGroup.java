package com.ihunda.android.binauralbeat;

import java.util.ArrayList;

public class CategoryGroup {
    private String groupName;
    private String niceName;
    private ArrayList<ProgramMeta> objets;

    public ArrayList<Program> getProgram() {
        return program;
    }


    private ArrayList<Program> program;

    public CategoryGroup(String name) {
        super();
        this.groupName = name;
        this.objets = new ArrayList<ProgramMeta>();
        this.program = new ArrayList<Program>();
    }

    public String getName() {
        return groupName;
    }

    public void setName(String nom) {
        this.groupName = nom;
    }

    public ArrayList<ProgramMeta> getObjets() {
        return objets;
    }

    public void add(ProgramMeta m, Program p) {
        m.setGroup(this);
        objets.add(m);
        program.add(p);
    }

    public void setObjets(ArrayList<ProgramMeta> objets) {
        this.objets = objets;
    }

    public String getNiceName() {
        if (niceName == null)
            niceName = WordUtils.capitalize(groupName.toLowerCase());
        return niceName;
    }

    public void setNiceName(String niceName) {
        this.niceName = niceName;
    }
}
