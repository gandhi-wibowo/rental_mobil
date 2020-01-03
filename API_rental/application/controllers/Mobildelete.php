<?php
defined('BASEPATH') OR exit('No direct script access allowed');
require APPPATH . '/libraries/REST_Controller.php';

class Mobildelete extends REST_Controller {
  function __construct($config = 'rest') {
			parent::__construct($config);
	}

  function index_post(){
    $id = $this->input->post('id');
    $this->db->where('no_mobil', $id);
    $delete = $this->db->delete('tb_mobil');
    if ($delete) {
        $this->response($delete, 201);
    } else {
        $this->response(array('status' => 'fail', 502));
    }
  }
  
  public function index_get(){

  }
}
