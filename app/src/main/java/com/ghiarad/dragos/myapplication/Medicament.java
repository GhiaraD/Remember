package com.ghiarad.dragos.myapplication;

public class Medicament {

    private String nume,data,ora;

    public Medicament()
    {

    }

    public Medicament(String nume, String data, String ora)
    {
        this.nume=nume;
        this.data=data;
        this.ora=ora;
    }

    public String getnume() {
        return nume;
    }
    public String getdata() {
        return data;
    }
    public String getora() {
        return ora;
    }

    public void setnume(String nume) { this.nume = nume; }
    public void setdata(String data) { this.data = data; }
    public void setora(String ora) { this.ora = ora; }

}

