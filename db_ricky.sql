-- MySQL dump 10.13  Distrib 5.5.53, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: db_ricky
-- ------------------------------------------------------
-- Server version	5.5.53-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tb_mobil`
--

DROP TABLE IF EXISTS `tb_mobil`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_mobil` (
  `no_mobil` varchar(10) NOT NULL,
  `id_user` varchar(35) NOT NULL,
  `nama_mobil` varchar(35) NOT NULL,
  `warna_mobil` varchar(15) NOT NULL,
  `harga_mobil` int(11) NOT NULL,
  `keterangan_mobil` text NOT NULL,
  `status_mobil` varchar(10) NOT NULL,
  `gambar_mobil` text NOT NULL,
  PRIMARY KEY (`no_mobil`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_mobil`
--

LOCK TABLES `tb_mobil` WRITE;
/*!40000 ALTER TABLE `tb_mobil` DISABLE KEYS */;
INSERT INTO `tb_mobil` VALUES ('bm8776mn','USR-161206-003','apaan','hitam',700000,'','busy','20161206_134843.jpg'),('bm8775cu','USR-161203-001','anu','hitam',700000,'','standby','20161206_012400.jpg');
/*!40000 ALTER TABLE `tb_mobil` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sewa`
--

DROP TABLE IF EXISTS `tb_sewa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sewa` (
  `id_sewa` varchar(35) NOT NULL,
  `id_user` varchar(35) NOT NULL,
  `id_mobil` varchar(35) NOT NULL,
  `tgl_sewa` date NOT NULL,
  `status_sewa` varchar(15) NOT NULL,
  PRIMARY KEY (`id_sewa`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sewa`
--

LOCK TABLES `tb_sewa` WRITE;
/*!40000 ALTER TABLE `tb_sewa` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sewa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_testimoni`
--

DROP TABLE IF EXISTS `tb_testimoni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_testimoni` (
  `id_testimoni` varchar(25) NOT NULL,
  `id_mobil` varchar(25) NOT NULL,
  `nama_testimoni` varchar(25) NOT NULL,
  `tgl_testimoni` date NOT NULL,
  `pesan_testimoni` text NOT NULL,
  PRIMARY KEY (`id_testimoni`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_testimoni`
--

LOCK TABLES `tb_testimoni` WRITE;
/*!40000 ALTER TABLE `tb_testimoni` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_testimoni` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_users`
--

DROP TABLE IF EXISTS `tb_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_users` (
  `id_user` varchar(30) NOT NULL,
  `nama_user` varchar(30) NOT NULL,
  `email_user` text NOT NULL,
  `hp_user` varchar(15) NOT NULL,
  `password_user` text NOT NULL,
  `alamat_user` text NOT NULL,
  `token_user` text NOT NULL,
  PRIMARY KEY (`id_user`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_users`
--

LOCK TABLES `tb_users` WRITE;
/*!40000 ALTER TABLE `tb_users` DISABLE KEYS */;
INSERT INTO `tb_users` VALUES ('USR-161206-003','agung','agung@gmail.com','082388715656','6ecd0c780a3ce620a423bbcf6ed3a9deb254028a','','flB6bTuUncw:APA91bFbPn7MfTjxsib6yUC_ZQuHB40VXFrJuZ9ubqzhuH5GdewkpADEyUsHwiV_te3bkiWKE3Z2_xJF6mUgjDGadY7E0bFmS_aOmEcqnEAew-SSTwXYa0OoImodd3rxS26etiJzv5Zx'),('USR-161206-002','ardi','ardiansyah@gmail.com','082388715555','6ecd0c780a3ce620a423bbcf6ed3a9deb254028a','','e8Jj_jGlxHA:APA91bH2_My3Sz4umOGGZFP5Fonz2xKx52zo17QmzjCaTLKz9LuSthHBSEzJ9XcImCqHQ7Ld_jSJkbNlVKcs1JZ6PbkhE_MWV3Rk3P8qC2fg1fjJGm1vzbWUVFoNSm8_1aRjAcfnmLwr'),('USR-161203-001','gandhi wibowo','gandhiw@ymail.com','082388715998','6ecd0c780a3ce620a423bbcf6ed3a9deb254028a','jl. fajar ujung','fgKKMUKIS_M:APA91bEquZcQvSF03KtV-0unVLZ8Xq-LwRFv8PXxgi5aeXXByLE3zDq3-HQ0Zb7a7kW3tZ4cUmEHpYPu5JjyhNnjvWCyQCvbGNTZAjqLrcpNVjp6nJG8WHurWMzDSk-41yS0toA9Xq7F');
/*!40000 ALTER TABLE `tb_users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-12-28 20:14:15
