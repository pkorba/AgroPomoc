package com.piotrkorba.agropomoc;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity class that represents single product in the database.
 */
@Entity(tableName = "product_table")
public class Product {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String nazwa;
    private String nrZezw;
    private long terminZezw;
    private long terminDoSprzedazy;
    private long terminDoStosowania;
    private String rodzaj;
    private String substancjaCzynna;
    private String uprawa;
    private String agrofag;
    private String dawka;
    private String termin;
    private String nazwaGrupy;
    private String maloobszarowe;
    private String zastosowanieUzytkownik;
    private String etykieta;

    public Product(@NonNull String nazwa, String nrZezw, long terminZezw, long terminDoSprzedazy, long terminDoStosowania,
                   String rodzaj, String substancjaCzynna, String uprawa, String agrofag, String dawka, String termin,
                   String nazwaGrupy, String maloobszarowe, String zastosowanieUzytkownik, String etykieta) {
        this.nazwa = nazwa;
        this.nrZezw = nrZezw;
        this.terminZezw = terminZezw;
        this.terminDoSprzedazy = terminDoSprzedazy;
        this.terminDoStosowania = terminDoStosowania;
        this.rodzaj = rodzaj;
        this.substancjaCzynna = substancjaCzynna;
        this.uprawa = uprawa;
        this.agrofag = agrofag;
        this.dawka = dawka;
        this.termin = termin;
        this.nazwaGrupy = nazwaGrupy;
        this.maloobszarowe = maloobszarowe;
        this.zastosowanieUzytkownik = zastosowanieUzytkownik;
        this.etykieta = etykieta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public String getNrZezw() {
        return nrZezw;
    }

    public long getTerminZezw() {
        return terminZezw;
    }

    public long getTerminDoSprzedazy() {
        return terminDoSprzedazy;
    }

    public long getTerminDoStosowania() {
        return terminDoStosowania;
    }

    public String getRodzaj() {
        return rodzaj;
    }

    public String getSubstancjaCzynna() {
        return substancjaCzynna;
    }

    public String getUprawa() {
        return uprawa;
    }

    public String getAgrofag() {
        return agrofag;
    }

    public String getDawka() {
        return dawka;
    }

    public String getTermin() {
        return termin;
    }

    public String getNazwaGrupy() {
        return nazwaGrupy;
    }

    public String getMaloobszarowe() {
        return maloobszarowe;
    }

    public String getZastosowanieUzytkownik() {
        return zastosowanieUzytkownik;
    }

    public String getEtykieta() {
        return etykieta;
    }
}
