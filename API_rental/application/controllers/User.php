<?php
defined('BASEPATH') OR exit('No direct script access allowed');
require APPPATH . '/libraries/REST_Controller.php';

class User extends REST_Controller {

	function __construct($config = 'rest') {
			parent::__construct($config);
	}
	function index_post() {
		$nama = $this->input->post('nama');
		$hp = $this->input->post('hp');
		$alamat = $this->input->post('alamat');
		$password = Password($this->input->post('password'));
		$id = Code('USR',LastCode('tb_users','id_user'),11,3);
		$data = array(
			'id_user'=>$id,
			'nama_user'=>$nama,
			'hp_user'=>$hp,
			'password_user'=>$password,
			'alamat_user'=>$alamat
		);
			$insert = $this->db->insert('tb_users', $data);
			if ($insert) {
					$this->response($data, 200);
			} else {
					$this->response(array('status' => 'fail', 502));
			}
	}
	function index_get() {
			$nim = $this->get('id_user');
			if ($nim == '') {
					$mahasiswa = $this->db->get('tb_users')->result();
			} else {
					$this->db->where('id_user', $nim);
					$mahasiswa = $this->db->get('tb_users')->result();
			}
			$this->response($mahasiswa, 200);
	}
	function index_put() {
		$nama = $this->put('nama');
		$hp = $this->put('hp');
		$alamat = $this->put('alamat');
		$password = Password($this->put('password'));
		$id = $this->put('id_user');
		$data = array(
			'nama_user'=>$nama,
			'hp_user'=>$hp,
			'password_user'=>$password,
			'alamat_user'=>$alamat
		);
			$this->db->where('id_user', $id);
			$update = $this->db->update('tb_users', $data);
			if ($update) {
					$this->response($data, 200);
			} else {
					$this->response(array('status' => 'fail', 502));
			}
	}

	function index_delete() {
			$nim = $this->delete('id_user');
			$this->db->where('id_user', $nim);
			$delete = $this->db->delete('tb_users');
			if ($delete) {
					$this->response(array('status' => 'success'), 201);
			} else {
					$this->response(array('status' => 'fail', 502));
			}
	}

}
