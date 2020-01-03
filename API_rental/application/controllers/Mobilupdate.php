<?php
defined('BASEPATH') OR exit('No direct script access allowed');
require APPPATH . '/libraries/REST_Controller.php';

class Mobilupdate extends REST_Controller {
  function __construct($config = 'rest') {
			parent::__construct($config);
	}

  function index_post(){
    $no_mobil = $this->input->post('no_mobil');
    $id_user = $this->input->post('id_user');
    $warna = $this->input->post('warna');
    $nama_mobil = $this->input->post('nama_mobil');
    $harga_mobil = $this->input->post('harga_mobil');
    $ket_mobil = $this->input->post('ket');
    // terima data
    $image = $this->input->post('images');
    $name = uplodGambar();
    if($name == "no_img.jpg"){
      // kalo gk ada gambarnya
      $data = array(
        'nama_mobil'=>$nama_mobil,
        'warna_mobil'=>$warna,
        'harga_mobil'=>$harga_mobil,
        'keterangan_mobil'=>$ket_mobil
      );
      $this->db->where('no_mobil', $no_mobil);
      $update = $this->db->update('tb_mobil', $data);
      if ($update) {
          $this->response($data, 200);
      } else {
          $this->response(array('status' => 'fail', 502));
      }
    }
    else{
      $data = array(
        'nama_mobil'=>$nama_mobil,
        'warna_mobil'=>$warna,
        'harga_mobil'=>$harga_mobil,
        'keterangan_mobil'=>$ket_mobil,
        'gambar_mobil'=>$name
      );
      $this->db->where('no_mobil', $no_mobil);
      $update = $this->db->update('tb_mobil', $data);
      if ($update) {
          $this->response($data, 200);
      } else {
          $this->response(array('status' => 'fail', 502));
      }
    }
  }

}
