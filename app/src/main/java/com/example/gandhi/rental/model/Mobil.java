package com.example.gandhi.rental.model;

public class Mobil {

    public Mobil() {
    }

    private String noMobil,idUser,nmMobil,wrMobil,hrgMobil,ketMobil,stsMobil,thumbnailUrl,hpUser,alamatUser,namaUser,statusSewa,idSewa;

    public String getNoMobil() {
        return noMobil;
    }
    public String getNamaUser(){
        return namaUser;
    }
    public String getIdSewa(){
        return idSewa;
    }
    public void setIdSewa(String idSewa){
        this.idSewa = idSewa;
    }
    public String getStatusSewa(){
        return statusSewa;
    }
    public void setStatusSewa(String statusSewa){
        this.statusSewa = statusSewa;
    }
    public void setNamaUser(String namaUser){
        this.namaUser = namaUser;
    }

    public String getHpUser(){
        return hpUser;
    }
    public void setHpUser(String hpUser){
        this.hpUser = hpUser;
    }
    public void setAlamatUser(String alamatUser){
        this.alamatUser = alamatUser;
    }
    public String getAlamatUser(){
        return alamatUser;
    }

    public void setNoMobil(String noMobil) {
        this.noMobil = noMobil;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNmMobil() {
        return nmMobil;
    }

    public void setNmMobil(String nmMobil) {
        this.nmMobil = nmMobil;
    }

    public String getWrMobil() {
        return wrMobil;
    }

    public void setWrMobil(String wrMobil) {
        this.wrMobil = wrMobil;
    }

    public String getHrgMobil() {
        return hrgMobil;
    }

    public void setHrgMobil(String hrgMobil) {
        this.hrgMobil = hrgMobil;
    }

    public String getKetMobil() {
        return ketMobil;
    }

    public void setKetMobil(String ketMobil) {
        this.ketMobil = ketMobil;
    }

    public String getStsMobil() {
        return stsMobil;
    }

    public void setStsMobil(String stsMobil) {
        this.stsMobil = stsMobil;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }



	public Mobil(String noMobil ,
                 String idUser ,
                 String nmMobil ,
                 String wrMobil ,
                 String hrgMobil ,
                 String ketMobil,
                 String stsMobil ,
                 String thumbnailUrl) {
        this.nmMobil = nmMobil;
        this.noMobil = noMobil;
        this.idUser = idUser;
        this.wrMobil = wrMobil;
        this.hrgMobil = hrgMobil;
        this.ketMobil = ketMobil;
        this.stsMobil = stsMobil;
		this.thumbnailUrl = thumbnailUrl;
	}



}
