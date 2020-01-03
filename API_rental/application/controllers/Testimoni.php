<?php
defined('BASEPATH') OR exit('No direct script access allowed');
require APPPATH . '/libraries/REST_Controller.php';

class Testimoni extends REST_Controller {
  function __construct($config = 'rest') {
			parent::__construct($config);
	}

  function index_post(){
    $id = Code('TES',LastCode('tb_testimoni','id_testimoni'),11,3);
    $idMobil = $this->input->post('id_mobil');
    $idUser = $this->input->post('id_user');
    $query = $this->db->query("SELECT  `nama_user` FROM  `tb_users` WHERE id_user =  '$idUser'");
    $row = $query->row();
    $nama = $row->nama_user;
    $pesan = $this->input->post('pesan');
    $tanggal = tanggal();
    $data = array(
      'id_testimoni'=>$id,
      'id_mobil'=>$idMobil,
      'nama_testimoni'=>$nama,
      'tgl_testimoni'=>$tanggal,
      'pesan_testimoni'=>$pesan
    );

    $insert = $this->db->insert('tb_testimoni', $data);
    if ($insert) {
        $this->response($data, 200);
    } else {
        $this->response(array('status' => 'fail', 502));
    }
  }

  public function index_get(){
    $id = $this->get('id_mobil');
    if($id != ''){
      $this->db->where('id_mobil',$id);
      $testimoni = $this->db->get('tb_testimoni')->result();
    }
    else {
      $testimoni = array("status"=>"fail",501);
    }

    $this->response($testimoni, 200);
  }
}
