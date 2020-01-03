<?php
defined('BASEPATH') OR exit('No direct script access allowed');
require APPPATH . '/libraries/REST_Controller.php';

class Mobil extends REST_Controller {
  function __construct($config = 'rest') {
			parent::__construct($config);
	}

  function index_post(){
    $no_mobil = str_replace(' ', '', $this->input->post('no_mobil'));
    $id_user = $this->input->post('id_user');
    $warna = $this->input->post('warna');
    $nama_mobil = $this->input->post('nama_mobil');
    $harga_mobil = $this->input->post('harga_mobil');
    $ket_mobil = $this->input->post('ket');
    // terima data
    $image = $this->input->post('images');
    $name = uplodGambar();
    $data = array(
      'no_mobil'=>$no_mobil,
      'id_user'=>$id_user,
      'nama_mobil'=>$nama_mobil,
      'warna_mobil'=>$warna,
      'harga_mobil'=>$harga_mobil,
      'keterangan_mobil'=>$ket_mobil,
      'status_mobil'=>'standby',
      'gambar_mobil'=>$name
    );

    $insert = $this->db->insert('tb_mobil', $data);
    if ($insert) {
        $this->response($data, 200);
    } else {
        $this->response(array('status' => 'fail', 502));
    }
  }
  function index_put(){ // update data
    $id = $this->put('id');
  }
  function index_get(){ // tampilkan data
    $id = $this->get('id');
    $cari = $this->get("cari");
    if($cari == null){
      if($id == null){
        $this->db->where(array('status_mobil'=>'standby'));
        $mobil = $this->db->get('tb_mobil')->result();
      }
      else{
        $this->db->where(array('status_mobil'=>'standby','id_user'=>$id));
        $mobil = $this->db->get('tb_mobil')->result();
      }
    }
    else{
      $this->db->or_like(array('no_mobil' =>$cari,'nama_mobil' =>$cari,'harga_mobil' =>$cari));
      $this->db->where(array('status_mobil'=>'standby'));
      $this->db->order_by("harga_mobil", "ASC");
      $mobil = $this->db->get('tb_mobil')->result();
     // cari di database yang ada kata kunci carinya
    }
    $this->response($mobil, 200);

  }

  public function index_delete(){

  }
}
