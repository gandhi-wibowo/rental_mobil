<?php
class Model_api extends CI_Model{
  function Daftar($id,$nama,$hp,$alamat,$password){
    $data = array(
      'id_user'=>$id,
      'nama_user'=>$nama,
      'hp_user'=>$hp,
      'password_user'=>$password,
      'alamat_user'=>$alamat
    );
    $exe = $this->db->insert('tb_users',$data);
    if($exe){
      return TRUE;
    }
    else{
      return FALSE;
    }
  }
  
}
