package com.example.gandhi.rental.model;

public class Testimonis {

    public Testimonis() {
    }


    private String idTestimoni,namaTestimoni,tglTestimoni,isiTestimoni;

    public String getIdTestimoni(){
        return idTestimoni;
    }
    public void setIdTestimoni(String idTestimoni){
        this.idTestimoni = idTestimoni;
    }

    public String getNamaTestimoni(){
        return namaTestimoni;
    }
    public void setNamaTestimoni(String namaTestimoni){
        this.namaTestimoni = namaTestimoni;
    }
    public String getTglTestimoni(){
        return tglTestimoni;
    }
    public void setTglTestimoni(String tglTestimoni){
        this.tglTestimoni = tglTestimoni;
    }
    public String getIsiTestimoni(){
        return isiTestimoni;
    }
    public void setIsiTestimoni(String isiTestimoni){
        this.isiTestimoni = isiTestimoni;
    }




	public Testimonis(String idTestimoni ,
                      String namaTestimoni ,
                      String tglTestimoni ,
                      String isiTestimoni ) {
        this.idTestimoni = idTestimoni;
        this.namaTestimoni = namaTestimoni;
        this.tglTestimoni = tglTestimoni;
        this.isiTestimoni = isiTestimoni;
	}



}
