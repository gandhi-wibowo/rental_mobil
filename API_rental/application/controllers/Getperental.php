<?php
defined('BASEPATH') OR exit('No direct script access allowed');
require APPPATH . '/libraries/REST_Controller.php';

class Getperental extends REST_Controller {

	function __construct($config = 'rest') {
			parent::__construct($config);
	}
	function index_post() {
	}
	function index_get() {
		$idMobil = $this->get('id_mobil');
		$query = "SELECT u.nama_user,u.alamat_user,u.hp_user
							FROM tb_users AS u, tb_mobil AS m
							WHERE m.no_mobil = '$idMobil'
							AND m.id_user = u.id_user";
		$exe = $this->db->query($query)->result();
		$this->response($exe,200);
	}
	function index_put() {
	}

	function index_delete() {
	}

}
