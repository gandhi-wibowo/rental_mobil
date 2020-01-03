<?php
class Model_gambar extends CI_Model{

  function InsertFoto($foto_id,$content,$namaFoto){
    $data = array(
      'id_foto'=>$foto_id,
      'id_content'=>$content,
      'nama_foto'=>$namaFoto
    );
    $exe = $this->db->insert('tb_foto',$data);
    if($exe){
      return TRUE;
    }
    else{
      return FALSE;
    }
  }

  function UpdateFoto($id,$namaFoto){
    $data = array(
      'nama_foto'=>$namaFoto
    );
    $this->db->where('id_foto',$id);
    if($this->db->update('tb_foto',$data)){
      return TRUE;
    }
    else{
      return FALSE;
    }
  }

}
