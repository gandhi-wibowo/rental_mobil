<?php
defined('BASEPATH') OR exit('No direct script access allowed');
require APPPATH . '/libraries/REST_Controller.php';

class Daftar extends REST_Controller {
  function __construct($config = 'rest') {
			parent::__construct($config);
	}

  function index_post(){
    $hp = $this->input->post('hp');
    $pwd = sha1($this->input->post('password'));
    $nama = $this->input->post('nama');
    $alamat = $this->input->post('alamat');
    $email = $this->input->post('email');
    $id = Code('USR',LastCode('tb_users','id_user'),11,3);

    if(cekHp($hp)){
      // kalau belum terdaftar
      $data = array(
  			'id_user'=>$id,
  			'nama_user'=>$nama,
  			'hp_user'=>$hp,
  			'password_user'=>$pwd,
  			'alamat_user'=>$alamat,
        'email_user'=>$email
  		);
  			$insert = $this->db->insert('tb_users', $data);
  			if ($insert) {
  					$this->response($data, 200);
  			} else {
  					$this->response(array('status' => 'fail', 502));
  			}
    }
    else{
      $this->response(array('status' => 'Sudah terdaftar', 502));
    }
  }
  function index_put(){ // update data
    $id = $this->put('id');

  }
  function index_get(){ // tampilkan data
    $id = $this->get('id');
    if($id == ""){
      $user = $this->db->get('tb_users')->result();
    }
    else{
      $this->db->where("id_user",$id);
      $user = $this->db->get('tb_users')->result();
    }
    $this->response($user,200);
  }
  function index_delete(){ // hapus data
    $id = $this->delete('id');

  }

}
