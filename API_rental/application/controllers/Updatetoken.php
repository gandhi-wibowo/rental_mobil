<?php
defined('BASEPATH') OR exit('No direct script access allowed');
require APPPATH . '/libraries/REST_Controller.php';

class Updatetoken extends REST_Controller {
  function __construct($config = 'rest') {
			parent::__construct($config);
	}

  function index_post(){
    $idUser = $this->input->post('id_user');
    $token = $this->input->post('token');
    $data = array('token_user'=>$token);
    $this->db->where(array('id_user'=>$idUser));
    $update = $this->db->update('tb_users', $data);
    if ($update) {
        $this->response($data, 200);
    } else {
        $this->response(array('status' => 'fail', 502));
    }
  }
}
