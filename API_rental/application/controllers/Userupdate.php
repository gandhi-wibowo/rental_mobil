<?php
defined('BASEPATH') OR exit('No direct script access allowed');
require APPPATH . '/libraries/REST_Controller.php';

class Userupdate extends REST_Controller {
  function __construct($config = 'rest') {
			parent::__construct($config);
	}

  function index_post(){
    $updPwd = $this->input->post("ubahPwd");
    $updProfil = $this->input->post("ubahProfil");
    $nama = $this->input->post('nama');
    $hp = $this->input->post('hp');
    $alamat = $this->input->post('alamat');

    if(!empty($updProfil)){
      $data = array(
        'nama_user'=>$nama,
        'hp_user'=>$hp,
        'alamat_user'=>$alamat
      );
      $this->db->where(array('id_user' => $updProfil));
      $update = $this->db->update('tb_users', $data);
      if ($update) {
          $this->response($data, 200);
      } else {
          $this->response(array('status' => 'fail', 502));
      }
    }
    else if(!empty($updPwd)){

      function cekPwd($id,$pwd){
        $ini =& get_instance();
        $ini->db->where(array('id_user'=> $id,'password_user'=>$pwd));
        $cek = $ini->db->get('tb_users');
        if($cek->num_rows() > 0){
          return True;
        }
        else{
          return False;
        }
      }

      $oldPwd = $this->input->post('oldPwd');
      $newPwd = $this->input->post('newPwd');
      $this->db->where(array('id_user'=> $updPwd,'password_user'=>$oldPwd));
      $cek = $this->db->get('tb_users');
      if(cekPwd($updPwd,sha1($oldPwd))){
        // kalau password ada
        if(!empty($newPwd)){
          $data = array('password_user'=>sha1($newPwd));
          $this->db->where(array('id_user'=>$updPwd));
          $update = $this->db->update('tb_users', $data);
          if ($update) {
              $this->response($data, 200);
          } else {
              $this->response(array('status' => 'fail', 502));
          }
        }else{
          $this->response(array('status' => 'fail', 502));
        }
      }
      else{
        $this->response(array('status' => 'fail', 502));
      }
    }
  }

}
