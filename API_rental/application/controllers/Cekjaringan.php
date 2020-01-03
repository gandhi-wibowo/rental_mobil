<?php
defined('BASEPATH') OR exit('No direct script access allowed');
require APPPATH . '/libraries/REST_Controller.php';

class Cekjaringan extends REST_Controller {
  function __construct($config = 'rest') {
			parent::__construct($config);
	}

  function index_post(){
    $cek = $this->input->post('cek');
    if(!empty($cek)){
      $this->response(array('status'=>'ok'),201);      
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
