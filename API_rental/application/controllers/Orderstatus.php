<?php
defined('BASEPATH') OR exit('No direct script access allowed');
require APPPATH . '/libraries/REST_Controller.php';

class Orderstatus extends REST_Controller {
  function __construct($config = 'rest') {
			parent::__construct($config);
	}

  function index_post(){
    function getIdMobil($idSewa){
      $ini =& get_instance();
      $query = $ini->db->query("SELECT id_mobil FROM tb_sewa WHERE id_sewa= '$idSewa' ");
      $row = $query->row();
      return $row->id_mobil;
    }
    function getSewa($idMobil){
      $ini =& get_instance();
      $query = $ini->db->query("SELECT id_sewa FROM tb_sewa WHERE id_mobil='$idMobil' AND status_sewa='order'")->result();
      return $query;
    }
    function updateMobil($noMobil,$value){
      $ini =& get_instance();
      $ini->db->where(array('no_mobil'=>$noMobil));
      $data = array('status_mobil'=>$value);
      $ini->db->update('tb_mobil', $data);
    }
    function updateStatus($idRental,$status){
      $ini =& get_instance();
      $data = array('status_sewa'=>"$status");
      $ini->db->where('id_sewa', $idRental);
      $update = $ini->db->update('tb_sewa', $data);

    }

    $idAccept = $this->input->post('id_accept');
    $idCancel = $this->input->post('id_cancel');
    $idSelesai = $this->input->post('id_selesai');
    if(!empty($idAccept)){

      updateMobil(getIdMobil($idAccept),"busy");
      updateStatus($idAccept,"disetujui");
      $idSewa = getSewa(getIdMobil($idAccept));
      foreach ($idSewa as $key) {
        $this->db->where('id_sewa',$key->id_sewa);
        $update = $this->db->update('tb_sewa',array('status_sewa'=>'dibatalkan'));
      }
      $this->response(TRUE,200);
    }
    else if(!empty($idCancel)){
      updateMobil(getIdMobil($idCancel),"standby");
      updateStatus($idCancel,"dibatalkan");
    }
    else if(!empty($idSelesai)){
      updateMobil(getIdMobil($idSelesai),"standby");
      updateStatus($idSelesai,"selesai");
    }
    $this->response(array("status"=>'fail'),501);
  }
  function index_put(){ // update data
  }
  function index_get(){ // tampilkan data

  }
  function index_delete(){ // hapus data

  }

}
