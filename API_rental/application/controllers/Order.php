<?php
defined('BASEPATH') OR exit('No direct script access allowed');
require APPPATH . '/libraries/REST_Controller.php';

class Order extends REST_Controller {
  function __construct($config = 'rest') {
			parent::__construct($config);
	}

  function index_post(){
    function senNotif($token,$title,$message){
      $fcm = "https://fcm.googleapis.com/fcm/send";
      $headers = array('Authorization:key=AIzaSyCYkhRD5c1MMQK18nKLZhYK_HmXHavW6Qw','Content-Type:application/json');
      $fields  = array(
        'to'=>$token,
        'notification'=>array(
          'title'=>$title,
          'body'=>$message,
          'click_action'=>'com.example.gandhi.rental_TARGET_NOTIFICATION',
          'icon'=>'ic_launcher',
          'sound'=>'RingtoneManager.TYPE_NOTIFICATION'
        ));
      $payload = json_encode($fields);
      $curl_session = curl_init();
      curl_setopt($curl_session,CURLOPT_URL,$fcm);
      curl_setopt($curl_session,CURLOPT_POST,true);
      curl_setopt($curl_session,CURLOPT_HTTPHEADER,$headers);
      curl_setopt($curl_session,CURLOPT_RETURNTRANSFER,true);
      curl_setopt($curl_session,CURLOPT_SSL_VERIFYPEER,false);
      curl_setopt($curl_session,CURLOPT_IPRESOLVE,CURL_IPRESOLVE_V4);
      curl_setopt($curl_session,CURLOPT_POSTFIELDS,$payload);
      curl_exec($curl_session);
      curl_close($curl_session);
    }

    function getToken($idMobil){
      $ini =& get_instance();
      $query = "SELECT token_user
      FROM tb_users AS u, tb_mobil AS m
      WHERE m.no_mobil = '$idMobil'
      AND m.id_user = u.id_user";
      $exe = $ini->db->query($query);
      $row = $exe->row();
      return $row->token_user;
    }

    function CekOrder($idMobil){
      $ini =& get_instance();
      $query = "SELECT * from tb_sewa WHERE id_mobil='$idMobil' AND status_sewa='order'";
      $exe = $ini->db->query($query);
      if($exe->num_rows() > 0){
        return FALSE;
      }
      else{
        return TRUE;
      }
    }

    $id = Code('REN',LastCode('tb_sewa','id_sewa'),11,3);
    $id_user = $this->input->post('id_user');
    $id_mobil = $this->input->post('no_mobil');
    $tanggal = tanggal();
    $status = "order";
    $data = array(
      'id_sewa'=>$id,
      'id_user'=>$id_user,
      'id_mobil'=>$id_mobil,
      'tgl_sewa'=>$tanggal,
      'status_sewa'=>$status
    );
    if(CekOrder($id_mobil)){
      $insert = $this->db->insert('tb_sewa', $data);
      if ($insert) {
          senNotif(getToken($id_mobil),"Rental Notification","Anda mendapat pesanan ");
          $this->response($data, 200);
      } else {
          $this->response(array('status' => 'fail', 502));
      }
    }
    else{
      $this->response(array('status' => 'fail', 502));
    }

  }
  function index_put(){

  }
  function index_get(){ // tampilkan data
    $idShared = $this->get('id_shared');
    $idPemesan = $this->get('id_pemesan');
    $idStatus = $this->get('id_status');

    if(!empty($idShared)){
      // kalau id pemilik mobil tidak kosong
      // memilih sewa berdsarkan pemilik mobil
      // aku lupa, apa sih yg ditampilin disini ?
      // karena patokannya id yg nempel di hp, berarti ini nampilin mobil si pemilik hp yang dipesan !
      // makanya di tampilkan berdasarkan yang statusnya order
      $query = "SELECT
      m.no_mobil,m.nama_mobil,m.warna_mobil, m.harga_mobil, m.keterangan_mobil,m.gambar_mobil,
      au.id_user, au.nama_user, au.hp_user,au.alamat_user,
      s.status_sewa,s.id_sewa
                FROM tb_sewa AS s,
                tb_mobil AS m,
                tb_users AS u,
				        tb_users AS au
                WHERE u.id_user = '$idShared'
                AND m.id_user = u.id_user
                AND m.no_mobil = s.id_mobil
				        AND au.id_user = s.id_user
                AND s.status_sewa = 'order'
                ORDER BY s.tgl_sewa DESC";
      $sewa = $this->db->query($query)->result();
      // id user di tabel user sama dengan parameter yang dikirim
      // id user di tabel mobil sama dengan id user di tabel user
      // no mobil di tabel mobil sama dengan id mobil di tabel sewa
      // id user di tabel user sama dengan id user di tabel sewa
      //yang di tampilin yang mau make mobil
    }
    else if(!empty($idPemesan)){
      // memilih sewa berdasarkan id sewa yang tertera di sewa
      $query = "SELECT
                m.no_mobil,m.nama_mobil,m.warna_mobil, m.harga_mobil, m.keterangan_mobil,m.gambar_mobil,
                u.nama_user,u.hp_user,u.alamat_user,
                s.status_sewa,s.id_sewa

                FROM tb_mobil AS m , tb_sewa AS s, tb_users AS u
                WHERE s.id_user = '$idPemesan'
                AND s.id_mobil = m.no_mobil
                AND m.id_user = u.id_user
                AND s.status_sewa != 'dibatalkan'
                AND s.status_sewa != 'selesai'
                ORDER BY s.tgl_sewa DESC";
      $sewa = $this->db->query($query)->result();
    }
    else if(!empty($idStatus)){
      $query = "SELECT
      m.no_mobil,m.nama_mobil,m.warna_mobil, m.harga_mobil, m.keterangan_mobil,m.gambar_mobil,
      au.id_user, au.nama_user, au.hp_user,au.alamat_user,
      s.status_sewa,s.id_sewa
                FROM tb_sewa AS s,
                tb_mobil AS m,
                tb_users AS u,
				        tb_users AS au
                WHERE u.id_user = '$idStatus'
                AND m.id_user = u.id_user
                AND m.no_mobil = s.id_mobil
				        AND au.id_user = s.id_user
                AND s.status_sewa = 'disetujui'
                ORDER BY s.tgl_sewa DESC";
      $sewa = $this->db->query($query)->result();
    }
    else{
      $sewa = array("status"=>"fail",501);
    }
    $this->response($sewa);

  }
  function index_delete(){ // hapus data
    $id = $this->delete('id');

  }

}
