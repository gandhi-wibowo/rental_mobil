<?php
defined('BASEPATH') OR exit('No direct script access allowed');
require APPPATH . '/libraries/REST_Controller.php';

class Cekuser extends REST_Controller {
  function __construct($config = 'rest') {
			parent::__construct($config);
	}

  function index_post(){
    $hp = $this->input->post('hp');
    $email = $this->input->post('email');
    $this->db->where(array('hp_user'=> $hp,'email_user'=>$email));
    $user = $this->db->get('tb_users');
    if($user->num_rows() > 0){
      $this->response($user->result(),200);
    }
    else{
      $this->response($user->result(),200);
    }
  }
  function index_put(){ // update data
    $id = $this->put('id');

  }
  function index_get(){ // tampilkan data
    $id = $this->get('id');

  }
  function index_delete(){ // hapus data
    $id = $this->delete('id');

  }
}
