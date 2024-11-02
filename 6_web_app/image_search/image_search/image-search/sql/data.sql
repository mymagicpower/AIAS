-- MySQL dump 10.13  Distrib 8.0.33, for macos13 (x86_64)
--
-- Host: localhost    Database: image-search
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `image-search`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `image-search` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `image-search`;

--
-- Table structure for table `code_column_config`
--

DROP TABLE IF EXISTS `code_column_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `code_column_config` (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `table_name` varchar(255) DEFAULT NULL,
  `column_name` varchar(255) DEFAULT NULL,
  `column_type` varchar(255) DEFAULT NULL,
  `dict_name` varchar(255) DEFAULT NULL,
  `extra` varchar(255) DEFAULT NULL,
  `form_show` bit(1) DEFAULT NULL,
  `form_type` varchar(255) DEFAULT NULL,
  `key_type` varchar(255) DEFAULT NULL,
  `list_show` bit(1) DEFAULT NULL,
  `not_null` bit(1) DEFAULT NULL,
  `query_type` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `date_annotation` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`column_id`) USING BTREE,
  KEY `idx_table_name` (`table_name`)
) ENGINE=InnoDB AUTO_INCREMENT=191 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='代码生成字段信息存储';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `code_column_config`
--

LOCK TABLES `code_column_config` WRITE;
/*!40000 ALTER TABLE `code_column_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `code_column_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `code_gen_config`
--

DROP TABLE IF EXISTS `code_gen_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `code_gen_config` (
  `config_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `table_name` varchar(255) DEFAULT NULL COMMENT '表名',
  `author` varchar(255) DEFAULT NULL COMMENT '作者',
  `cover` bit(1) DEFAULT NULL COMMENT '是否覆盖',
  `module_name` varchar(255) DEFAULT NULL COMMENT '模块名称',
  `pack` varchar(255) DEFAULT NULL COMMENT '至于哪个包下',
  `path` varchar(255) DEFAULT NULL COMMENT '前端代码生成的路径',
  `api_path` varchar(255) DEFAULT NULL COMMENT '前端Api文件路径',
  `prefix` varchar(255) DEFAULT NULL COMMENT '表前缀',
  `api_alias` varchar(255) DEFAULT NULL COMMENT '接口名称',
  PRIMARY KEY (`config_id`) USING BTREE,
  KEY `idx_table_name` (`table_name`(100))
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='代码生成器配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `code_gen_config`
--

LOCK TABLES `code_gen_config` WRITE;
/*!40000 ALTER TABLE `code_gen_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `code_gen_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image_info`
--

DROP TABLE IF EXISTS `image_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image_info` (
  `image_id` bigint NOT NULL AUTO_INCREMENT,
  `group_id` bigint DEFAULT NULL,
  `uuid` varchar(50) DEFAULT NULL COMMENT '图片uuid',
  `pre_name` varchar(50) DEFAULT NULL,
  `img_uri` varchar(255) DEFAULT NULL COMMENT '图片相对路径',
  `full_path` varchar(255) DEFAULT NULL,
  `type` tinyint(1) DEFAULT NULL COMMENT '1: 本地url，0: 远程图片url',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`image_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15442 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image_info`
--

LOCK TABLES `image_info` WRITE;
/*!40000 ALTER TABLE `image_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `image_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image_log`
--

DROP TABLE IF EXISTS `image_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image_log` (
  `log_id` bigint NOT NULL AUTO_INCREMENT,
  `storage_id` bigint NOT NULL COMMENT '文件存储ID',
  `file_name` varchar(50) NOT NULL COMMENT '图片ZIP包名称',
  `image_list` json NOT NULL COMMENT '上传图片清单',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(50) NOT NULL COMMENT '创建人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image_log`
--

LOCK TABLES `image_log` WRITE;
/*!40000 ALTER TABLE `image_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `image_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mnt_app`
--

DROP TABLE IF EXISTS `mnt_app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mnt_app` (
  `app_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) DEFAULT NULL COMMENT '应用名称',
  `upload_path` varchar(255) DEFAULT NULL COMMENT '上传目录',
  `deploy_path` varchar(255) DEFAULT NULL COMMENT '部署路径',
  `backup_path` varchar(255) DEFAULT NULL COMMENT '备份路径',
  `port` int DEFAULT NULL COMMENT '应用端口',
  `start_script` varchar(4000) DEFAULT NULL COMMENT '启动脚本',
  `deploy_script` varchar(4000) DEFAULT NULL COMMENT '部署脚本',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`app_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='应用管理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mnt_app`
--

LOCK TABLES `mnt_app` WRITE;
/*!40000 ALTER TABLE `mnt_app` DISABLE KEYS */;
/*!40000 ALTER TABLE `mnt_app` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mnt_database`
--

DROP TABLE IF EXISTS `mnt_database`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mnt_database` (
  `db_id` varchar(50) NOT NULL COMMENT 'ID',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `jdbc_url` varchar(255) NOT NULL COMMENT 'jdbc连接',
  `user_name` varchar(255) NOT NULL COMMENT '账号',
  `pwd` varchar(255) NOT NULL COMMENT '密码',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`db_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='数据库管理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mnt_database`
--

LOCK TABLES `mnt_database` WRITE;
/*!40000 ALTER TABLE `mnt_database` DISABLE KEYS */;
/*!40000 ALTER TABLE `mnt_database` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mnt_deploy`
--

DROP TABLE IF EXISTS `mnt_deploy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mnt_deploy` (
  `deploy_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `app_id` bigint DEFAULT NULL COMMENT '应用编号',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`deploy_id`) USING BTREE,
  KEY `FK6sy157pseoxx4fmcqr1vnvvhy` (`app_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='部署管理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mnt_deploy`
--

LOCK TABLES `mnt_deploy` WRITE;
/*!40000 ALTER TABLE `mnt_deploy` DISABLE KEYS */;
/*!40000 ALTER TABLE `mnt_deploy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mnt_deploy_history`
--

DROP TABLE IF EXISTS `mnt_deploy_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mnt_deploy_history` (
  `history_id` varchar(50) NOT NULL COMMENT 'ID',
  `app_name` varchar(255) NOT NULL COMMENT '应用名称',
  `deploy_date` datetime NOT NULL COMMENT '部署日期',
  `deploy_user` varchar(50) NOT NULL COMMENT '部署用户',
  `ip` varchar(20) NOT NULL COMMENT '服务器IP',
  `deploy_id` bigint DEFAULT NULL COMMENT '部署编号',
  PRIMARY KEY (`history_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='部署历史管理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mnt_deploy_history`
--

LOCK TABLES `mnt_deploy_history` WRITE;
/*!40000 ALTER TABLE `mnt_deploy_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `mnt_deploy_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mnt_deploy_server`
--

DROP TABLE IF EXISTS `mnt_deploy_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mnt_deploy_server` (
  `deploy_id` bigint NOT NULL COMMENT '部署ID',
  `server_id` bigint NOT NULL COMMENT '服务ID',
  PRIMARY KEY (`deploy_id`,`server_id`) USING BTREE,
  KEY `FKeaaha7jew9a02b3bk9ghols53` (`server_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='应用与服务器关联';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mnt_deploy_server`
--

LOCK TABLES `mnt_deploy_server` WRITE;
/*!40000 ALTER TABLE `mnt_deploy_server` DISABLE KEYS */;
/*!40000 ALTER TABLE `mnt_deploy_server` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mnt_server`
--

DROP TABLE IF EXISTS `mnt_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mnt_server` (
  `server_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `account` varchar(50) DEFAULT NULL COMMENT '账号',
  `ip` varchar(20) DEFAULT NULL COMMENT 'IP地址',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `port` int DEFAULT NULL COMMENT '端口',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`server_id`) USING BTREE,
  KEY `idx_ip` (`ip`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='服务器管理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mnt_server`
--

LOCK TABLES `mnt_server` WRITE;
/*!40000 ALTER TABLE `mnt_server` DISABLE KEYS */;
/*!40000 ALTER TABLE `mnt_server` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dept` (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` bigint DEFAULT NULL COMMENT '上级部门',
  `sub_count` int DEFAULT '0' COMMENT '子部门数目',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `dept_sort` int DEFAULT '999' COMMENT '排序',
  `enabled` bit(1) NOT NULL COMMENT '状态',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE,
  KEY `inx_pid` (`pid`),
  KEY `inx_enabled` (`enabled`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='部门';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` VALUES (2,7,0,'研发部',1,_binary '','admin','admin','2021-06-14 13:53:36','2021-06-14 13:53:36'),(5,7,0,'运维部',2,_binary '','admin','admin','2021-06-14 13:53:43','2021-06-14 13:53:43'),(7,NULL,2,'总部',0,_binary '','admin','admin','2019-03-25 11:04:50','2021-06-14 13:53:11');
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict`
--

DROP TABLE IF EXISTS `sys_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict` (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) NOT NULL COMMENT '字典名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dict_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='数据字典';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict`
--

LOCK TABLES `sys_dict` WRITE;
/*!40000 ALTER TABLE `sys_dict` DISABLE KEYS */;
INSERT INTO `sys_dict` VALUES (1,'user_status','用户状态',NULL,NULL,'2021-06-14 13:53:11',NULL),(4,'dept_status','部门状态',NULL,NULL,'2021-06-14 13:53:11',NULL),(5,'job_status','岗位状态',NULL,NULL,'2019-10-27 20:31:36',NULL);
/*!40000 ALTER TABLE `sys_dict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_detail`
--

DROP TABLE IF EXISTS `sys_dict_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_detail` (
  `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dict_id` bigint DEFAULT NULL COMMENT '字典id',
  `label` varchar(255) NOT NULL COMMENT '字典标签',
  `value` varchar(255) NOT NULL COMMENT '字典值',
  `dict_sort` int DEFAULT NULL COMMENT '排序',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`detail_id`) USING BTREE,
  KEY `FK5tpkputc6d9nboxojdbgnpmyb` (`dict_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='数据字典详情';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_detail`
--

LOCK TABLES `sys_dict_detail` WRITE;
/*!40000 ALTER TABLE `sys_dict_detail` DISABLE KEYS */;
INSERT INTO `sys_dict_detail` VALUES (1,1,'激活','true',1,NULL,NULL,'2021-06-14 13:53:11',NULL),(2,1,'禁用','false',2,NULL,NULL,NULL,NULL),(3,4,'启用','true',1,NULL,NULL,NULL,NULL),(4,4,'停用','false',2,NULL,NULL,'2021-06-14 13:53:11',NULL),(5,5,'启用','true',1,NULL,NULL,NULL,NULL),(6,5,'停用','false',2,NULL,NULL,'2021-06-14 13:53:11',NULL);
/*!40000 ALTER TABLE `sys_dict_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_job`
--

DROP TABLE IF EXISTS `sys_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_job` (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) NOT NULL COMMENT '岗位名称',
  `enabled` bit(1) NOT NULL COMMENT '岗位状态',
  `job_sort` int DEFAULT NULL COMMENT '排序',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`job_id`) USING BTREE,
  UNIQUE KEY `uniq_name` (`name`),
  KEY `inx_enabled` (`enabled`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='岗位';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_job`
--

LOCK TABLES `sys_job` WRITE;
/*!40000 ALTER TABLE `sys_job` DISABLE KEYS */;
INSERT INTO `sys_job` VALUES (11,'全栈开发',_binary '',2,NULL,'admin','2021-06-14 13:53:11','2021-06-14 13:53:11'),(12,'软件测试',_binary '',5,NULL,'admin','2021-06-14 13:53:11','2021-06-14 13:53:11');
/*!40000 ALTER TABLE `sys_job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_log`
--

DROP TABLE IF EXISTS `sys_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_log` (
  `log_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `description` varchar(255) DEFAULT NULL,
  `log_type` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `params` text,
  `request_ip` varchar(255) DEFAULT NULL,
  `time` bigint DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `browser` varchar(255) DEFAULT NULL,
  `exception_detail` text,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `log_create_time_index` (`create_time`),
  KEY `inx_log_type` (`log_type`)
) ENGINE=InnoDB AUTO_INCREMENT=3806 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='系统日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_log`
--

LOCK TABLES `sys_log` WRITE;
/*!40000 ALTER TABLE `sys_log` DISABLE KEYS */;
INSERT INTO `sys_log` VALUES (3537,'删除定时任务','INFO','me.calvin.modules.quartz.rest.QuartzJobController.delete()','[3]','192.168.43.107',164,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-13 20:26:16'),(3538,'删除定时任务','INFO','me.calvin.modules.quartz.rest.QuartzJobController.delete()','[6]','192.168.43.107',37,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-13 20:26:18'),(3539,'删除定时任务','INFO','me.calvin.modules.quartz.rest.QuartzJobController.delete()','[5]','192.168.43.107',38,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-13 20:26:20'),(3540,'删除菜单','INFO','me.calvin.modules.system.rest.MenuController.delete()','[21]','192.168.43.107',367,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-13 20:26:58'),(3541,'删除菜单','INFO','me.calvin.modules.system.rest.MenuController.delete()','[10]','192.168.43.107',409,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-13 20:27:00'),(3542,'新增菜单','INFO','me.calvin.modules.system.rest.MenuController.create()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"icon\":\"image\",\"updateTime\":1623588904155,\"title\":\"人像搜索\",\"type\":1,\"subCount\":0,\"path\":\"facesearch\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623588904155,\"iFrame\":false,\"id\":118,\"menuSort\":1}','192.168.43.107',405,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-13 20:55:04'),(3543,'修改菜单','INFO','me.calvin.modules.system.rest.MenuController.update()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"icon\":\"system\",\"title\":\"系统管理\",\"type\":0,\"subCount\":7,\"path\":\"system\",\"createTime\":1545117089000,\"iFrame\":false,\"id\":1,\"menuSort\":2}','192.168.43.107',157,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-13 20:55:12'),(3544,'修改角色菜单','INFO','me.calvin.modules.system.rest.RoleController.updateMenu()','{\"level\":3,\"dataScope\":\"本级\",\"id\":1,\"menus\":[{\"subCount\":0,\"id\":97,\"menuSort\":999},{\"subCount\":0,\"id\":98,\"menuSort\":999},{\"subCount\":0,\"id\":102,\"menuSort\":999},{\"subCount\":0,\"id\":103,\"menuSort\":999},{\"subCount\":0,\"id\":104,\"menuSort\":999},{\"subCount\":0,\"id\":105,\"menuSort\":999},{\"subCount\":0,\"id\":106,\"menuSort\":999},{\"subCount\":0,\"id\":107,\"menuSort\":999},{\"subCount\":0,\"id\":108,\"menuSort\":999},{\"subCount\":0,\"id\":109,\"menuSort\":999},{\"subCount\":0,\"id\":110,\"menuSort\":999},{\"subCount\":0,\"id\":111,\"menuSort\":999},{\"subCount\":0,\"id\":112,\"menuSort\":999},{\"subCount\":0,\"id\":113,\"menuSort\":999},{\"subCount\":0,\"id\":114,\"menuSort\":999},{\"subCount\":0,\"id\":116,\"menuSort\":999},{\"subCount\":0,\"id\":118,\"menuSort\":999},{\"subCount\":0,\"id\":1,\"menuSort\":999},{\"subCount\":0,\"id\":2,\"menuSort\":999},{\"subCount\":0,\"id\":3,\"menuSort\":999},{\"subCount\":0,\"id\":5,\"menuSort\":999},{\"subCount\":0,\"id\":6,\"menuSort\":999},{\"subCount\":0,\"id\":7,\"menuSort\":999},{\"subCount\":0,\"id\":9,\"menuSort\":999},{\"subCount\":0,\"id\":14,\"menuSort\":999},{\"subCount\":0,\"id\":18,\"menuSort\":999},{\"subCount\":0,\"id\":19,\"menuSort\":999},{\"subCount\":0,\"id\":28,\"menuSort\":999},{\"subCount\":0,\"id\":30,\"menuSort\":999},{\"subCount\":0,\"id\":32,\"menuSort\":999},{\"subCount\":0,\"id\":35,\"menuSort\":999},{\"subCount\":0,\"id\":36,\"menuSort\":999},{\"subCount\":0,\"id\":37,\"menuSort\":999},{\"subCount\":0,\"id\":38,\"menuSort\":999},{\"subCount\":0,\"id\":39,\"menuSort\":999},{\"subCount\":0,\"id\":41,\"menuSort\":999},{\"subCount\":0,\"id\":44,\"menuSort\":999},{\"subCount\":0,\"id\":45,\"menuSort\":999},{\"subCount\":0,\"id\":46,\"menuSort\":999},{\"subCount\":0,\"id\":48,\"menuSort\":999},{\"subCount\":0,\"id\":49,\"menuSort\":999},{\"subCount\":0,\"id\":50,\"menuSort\":999},{\"subCount\":0,\"id\":52,\"menuSort\":999},{\"subCount\":0,\"id\":53,\"menuSort\":999},{\"subCount\":0,\"id\":54,\"menuSort\":999},{\"subCount\":0,\"id\":56,\"menuSort\":999},{\"subCount\":0,\"id\":57,\"menuSort\":999},{\"subCount\":0,\"id\":58,\"menuSort\":999},{\"subCount\":0,\"id\":60,\"menuSort\":999},{\"subCount\":0,\"id\":61,\"menuSort\":999},{\"subCount\":0,\"id\":62,\"menuSort\":999},{\"subCount\":0,\"id\":64,\"menuSort\":999},{\"subCount\":0,\"id\":65,\"menuSort\":999},{\"subCount\":0,\"id\":66,\"menuSort\":999},{\"subCount\":0,\"id\":73,\"menuSort\":999},{\"subCount\":0,\"id\":74,\"menuSort\":999},{\"subCount\":0,\"id\":75,\"menuSort\":999},{\"subCount\":0,\"id\":77,\"menuSort\":999},{\"subCount\":0,\"id\":78,\"menuSort\":999},{\"subCount\":0,\"id\":79,\"menuSort\":999},{\"subCount\":0,\"id\":80,\"menuSort\":999},{\"subCount\":0,\"id\":82,\"menuSort\":999},{\"subCount\":0,\"id\":90,\"menuSort\":999},{\"subCount\":0,\"id\":92,\"menuSort\":999},{\"subCount\":0,\"id\":93,\"menuSort\":999},{\"subCount\":0,\"id\":94,\"menuSort\":999}]}','192.168.43.107',1303,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-13 20:55:35'),(3546,'新增菜单','INFO','me.calvin.modules.system.rest.MenuController.create()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"pid\":118,\"updateTime\":1623589265259,\"title\":\"搜索\",\"type\":1,\"subCount\":0,\"path\":\"search\",\"component\":\"facesearch/search/index\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623589265235,\"iFrame\":false,\"id\":119,\"componentName\":\"PersonImageTable\",\"menuSort\":999}','192.168.43.107',87,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-13 21:01:05'),(3547,'修改角色菜单','INFO','me.calvin.modules.system.rest.RoleController.updateMenu()','{\"level\":3,\"dataScope\":\"本级\",\"id\":1,\"menus\":[{\"subCount\":0,\"id\":97,\"menuSort\":999},{\"subCount\":0,\"id\":98,\"menuSort\":999},{\"subCount\":0,\"id\":102,\"menuSort\":999},{\"subCount\":0,\"id\":103,\"menuSort\":999},{\"subCount\":0,\"id\":104,\"menuSort\":999},{\"subCount\":0,\"id\":105,\"menuSort\":999},{\"subCount\":0,\"id\":106,\"menuSort\":999},{\"subCount\":0,\"id\":107,\"menuSort\":999},{\"subCount\":0,\"id\":108,\"menuSort\":999},{\"subCount\":0,\"id\":109,\"menuSort\":999},{\"subCount\":0,\"id\":110,\"menuSort\":999},{\"subCount\":0,\"id\":111,\"menuSort\":999},{\"subCount\":0,\"id\":112,\"menuSort\":999},{\"subCount\":0,\"id\":113,\"menuSort\":999},{\"subCount\":0,\"id\":114,\"menuSort\":999},{\"subCount\":0,\"id\":116,\"menuSort\":999},{\"subCount\":0,\"id\":118,\"menuSort\":999},{\"subCount\":0,\"id\":119,\"menuSort\":999},{\"subCount\":0,\"id\":1,\"menuSort\":999},{\"subCount\":0,\"id\":2,\"menuSort\":999},{\"subCount\":0,\"id\":3,\"menuSort\":999},{\"subCount\":0,\"id\":5,\"menuSort\":999},{\"subCount\":0,\"id\":6,\"menuSort\":999},{\"subCount\":0,\"id\":7,\"menuSort\":999},{\"subCount\":0,\"id\":9,\"menuSort\":999},{\"subCount\":0,\"id\":14,\"menuSort\":999},{\"subCount\":0,\"id\":18,\"menuSort\":999},{\"subCount\":0,\"id\":19,\"menuSort\":999},{\"subCount\":0,\"id\":28,\"menuSort\":999},{\"subCount\":0,\"id\":30,\"menuSort\":999},{\"subCount\":0,\"id\":32,\"menuSort\":999},{\"subCount\":0,\"id\":35,\"menuSort\":999},{\"subCount\":0,\"id\":36,\"menuSort\":999},{\"subCount\":0,\"id\":37,\"menuSort\":999},{\"subCount\":0,\"id\":38,\"menuSort\":999},{\"subCount\":0,\"id\":39,\"menuSort\":999},{\"subCount\":0,\"id\":41,\"menuSort\":999},{\"subCount\":0,\"id\":44,\"menuSort\":999},{\"subCount\":0,\"id\":45,\"menuSort\":999},{\"subCount\":0,\"id\":46,\"menuSort\":999},{\"subCount\":0,\"id\":48,\"menuSort\":999},{\"subCount\":0,\"id\":49,\"menuSort\":999},{\"subCount\":0,\"id\":50,\"menuSort\":999},{\"subCount\":0,\"id\":52,\"menuSort\":999},{\"subCount\":0,\"id\":53,\"menuSort\":999},{\"subCount\":0,\"id\":54,\"menuSort\":999},{\"subCount\":0,\"id\":56,\"menuSort\":999},{\"subCount\":0,\"id\":57,\"menuSort\":999},{\"subCount\":0,\"id\":58,\"menuSort\":999},{\"subCount\":0,\"id\":60,\"menuSort\":999},{\"subCount\":0,\"id\":61,\"menuSort\":999},{\"subCount\":0,\"id\":62,\"menuSort\":999},{\"subCount\":0,\"id\":64,\"menuSort\":999},{\"subCount\":0,\"id\":65,\"menuSort\":999},{\"subCount\":0,\"id\":66,\"menuSort\":999},{\"subCount\":0,\"id\":73,\"menuSort\":999},{\"subCount\":0,\"id\":74,\"menuSort\":999},{\"subCount\":0,\"id\":75,\"menuSort\":999},{\"subCount\":0,\"id\":77,\"menuSort\":999},{\"subCount\":0,\"id\":78,\"menuSort\":999},{\"subCount\":0,\"id\":79,\"menuSort\":999},{\"subCount\":0,\"id\":80,\"menuSort\":999},{\"subCount\":0,\"id\":82,\"menuSort\":999},{\"subCount\":0,\"id\":90,\"menuSort\":999},{\"subCount\":0,\"id\":92,\"menuSort\":999},{\"subCount\":0,\"id\":93,\"menuSort\":999},{\"subCount\":0,\"id\":94,\"menuSort\":999}]}','192.168.43.107',905,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-13 21:01:23'),(3548,'新增菜单','INFO','me.calvin.modules.system.rest.MenuController.create()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"pid\":118,\"updateTime\":1623590427238,\"title\":\"通用搜索\",\"type\":1,\"subCount\":0,\"path\":\"common\",\"component\":\"facesearch/common/index\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623590427238,\"iFrame\":false,\"id\":120,\"componentName\":\"CommonImageTable\",\"menuSort\":999}','192.168.43.107',181,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-13 21:20:27'),(3549,'新增菜单','INFO','me.calvin.modules.system.rest.MenuController.create()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"updateTime\":1623590456885,\"title\":\"图片信息\",\"type\":1,\"subCount\":0,\"path\":\"imageinfo\",\"component\":\"facesearch/imageinfo/index\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623590456885,\"iFrame\":false,\"id\":121,\"componentName\":\"ImageInfo\",\"menuSort\":999}','192.168.43.107',60,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-13 21:20:57'),(3550,'修改菜单','INFO','me.calvin.modules.system.rest.MenuController.update()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"pid\":118,\"updateTime\":1623590457000,\"title\":\"图片信息\",\"type\":1,\"subCount\":0,\"path\":\"imageinfo\",\"component\":\"facesearch/imageinfo/index\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623590457000,\"iFrame\":false,\"id\":121,\"componentName\":\"ImageInfo\",\"menuSort\":999}','192.168.43.107',136,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-13 21:21:11'),(3551,'修改角色','INFO','me.calvin.modules.system.rest.RoleController.update()','{\"level\":1,\"description\":\"-\",\"updateTime\":1623589283000,\"dataScope\":\"全部\",\"updateBy\":\"admin\",\"createTime\":1542942277000,\"name\":\"超级管理员\",\"id\":1,\"depts\":[]}','192.168.43.107',92,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-13 21:21:20'),(3552,'修改角色菜单','INFO','me.calvin.modules.system.rest.RoleController.updateMenu()','{\"level\":3,\"dataScope\":\"本级\",\"id\":1,\"menus\":[{\"subCount\":0,\"id\":97,\"menuSort\":999},{\"subCount\":0,\"id\":98,\"menuSort\":999},{\"subCount\":0,\"id\":102,\"menuSort\":999},{\"subCount\":0,\"id\":103,\"menuSort\":999},{\"subCount\":0,\"id\":104,\"menuSort\":999},{\"subCount\":0,\"id\":105,\"menuSort\":999},{\"subCount\":0,\"id\":106,\"menuSort\":999},{\"subCount\":0,\"id\":107,\"menuSort\":999},{\"subCount\":0,\"id\":108,\"menuSort\":999},{\"subCount\":0,\"id\":109,\"menuSort\":999},{\"subCount\":0,\"id\":110,\"menuSort\":999},{\"subCount\":0,\"id\":111,\"menuSort\":999},{\"subCount\":0,\"id\":112,\"menuSort\":999},{\"subCount\":0,\"id\":113,\"menuSort\":999},{\"subCount\":0,\"id\":114,\"menuSort\":999},{\"subCount\":0,\"id\":116,\"menuSort\":999},{\"subCount\":0,\"id\":118,\"menuSort\":999},{\"subCount\":0,\"id\":119,\"menuSort\":999},{\"subCount\":0,\"id\":120,\"menuSort\":999},{\"subCount\":0,\"id\":121,\"menuSort\":999},{\"subCount\":0,\"id\":1,\"menuSort\":999},{\"subCount\":0,\"id\":2,\"menuSort\":999},{\"subCount\":0,\"id\":3,\"menuSort\":999},{\"subCount\":0,\"id\":5,\"menuSort\":999},{\"subCount\":0,\"id\":6,\"menuSort\":999},{\"subCount\":0,\"id\":7,\"menuSort\":999},{\"subCount\":0,\"id\":9,\"menuSort\":999},{\"subCount\":0,\"id\":14,\"menuSort\":999},{\"subCount\":0,\"id\":18,\"menuSort\":999},{\"subCount\":0,\"id\":19,\"menuSort\":999},{\"subCount\":0,\"id\":28,\"menuSort\":999},{\"subCount\":0,\"id\":30,\"menuSort\":999},{\"subCount\":0,\"id\":32,\"menuSort\":999},{\"subCount\":0,\"id\":35,\"menuSort\":999},{\"subCount\":0,\"id\":36,\"menuSort\":999},{\"subCount\":0,\"id\":37,\"menuSort\":999},{\"subCount\":0,\"id\":38,\"menuSort\":999},{\"subCount\":0,\"id\":39,\"menuSort\":999},{\"subCount\":0,\"id\":41,\"menuSort\":999},{\"subCount\":0,\"id\":44,\"menuSort\":999},{\"subCount\":0,\"id\":45,\"menuSort\":999},{\"subCount\":0,\"id\":46,\"menuSort\":999},{\"subCount\":0,\"id\":48,\"menuSort\":999},{\"subCount\":0,\"id\":49,\"menuSort\":999},{\"subCount\":0,\"id\":50,\"menuSort\":999},{\"subCount\":0,\"id\":52,\"menuSort\":999},{\"subCount\":0,\"id\":53,\"menuSort\":999},{\"subCount\":0,\"id\":54,\"menuSort\":999},{\"subCount\":0,\"id\":56,\"menuSort\":999},{\"subCount\":0,\"id\":57,\"menuSort\":999},{\"subCount\":0,\"id\":58,\"menuSort\":999},{\"subCount\":0,\"id\":60,\"menuSort\":999},{\"subCount\":0,\"id\":61,\"menuSort\":999},{\"subCount\":0,\"id\":62,\"menuSort\":999},{\"subCount\":0,\"id\":64,\"menuSort\":999},{\"subCount\":0,\"id\":65,\"menuSort\":999},{\"subCount\":0,\"id\":66,\"menuSort\":999},{\"subCount\":0,\"id\":73,\"menuSort\":999},{\"subCount\":0,\"id\":74,\"menuSort\":999},{\"subCount\":0,\"id\":75,\"menuSort\":999},{\"subCount\":0,\"id\":77,\"menuSort\":999},{\"subCount\":0,\"id\":78,\"menuSort\":999},{\"subCount\":0,\"id\":79,\"menuSort\":999},{\"subCount\":0,\"id\":80,\"menuSort\":999},{\"subCount\":0,\"id\":82,\"menuSort\":999},{\"subCount\":0,\"id\":90,\"menuSort\":999},{\"subCount\":0,\"id\":92,\"menuSort\":999},{\"subCount\":0,\"id\":93,\"menuSort\":999},{\"subCount\":0,\"id\":94,\"menuSort\":999}]}','192.168.43.107',772,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-13 21:21:27'),(3553,'查询ImageInfoService','INFO','me.calvin.modules.facesearch.rest.ImageInfoController.query()','','192.168.43.107',311,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-13 21:24:30'),(3554,'删除菜单','INFO','me.calvin.modules.system.rest.MenuController.delete()','[90]','192.168.43.107',2136,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-13 21:26:05'),(3555,'删除菜单','INFO','me.calvin.modules.system.rest.MenuController.delete()','[14]','192.168.43.107',103,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-13 21:26:56'),(3556,'删除菜单','INFO','me.calvin.modules.system.rest.MenuController.delete()','[19]','192.168.43.107',134,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-13 21:27:03'),(3557,'zip包解压缩并提取特征值','INFO','me.calvin.modules.facesearch.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"10\"}','192.168.43.107',878,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-13 21:32:38'),(3558,'zip包解压缩并提取特征值','INFO','me.calvin.modules.facesearch.rest.ImageInfoController.extractFeatures()','{\"type\":\"1\",\"id\":\"10\"}','192.168.43.107',303,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-13 21:34:47'),(3559,'删除用户','INFO','me.calvin.modules.system.rest.UserController.delete()','[2]','192.168.43.107',217,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 13:52:24'),(3560,'修改部门','INFO','me.calvin.modules.system.rest.DeptController.update()','{\"updateTime\":1591589336000,\"enabled\":true,\"deptSort\":0,\"subCount\":2,\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1553483090000,\"name\":\"总部\",\"id\":7}','192.168.43.107',215,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 13:53:11'),(3561,'删除部门','INFO','me.calvin.modules.system.rest.DeptController.delete()','[8]','192.168.43.107',178,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 13:53:16'),(3562,'删除部门','INFO','me.calvin.modules.system.rest.DeptController.delete()','[17]','192.168.43.107',76,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 13:53:26'),(3563,'修改部门','INFO','me.calvin.modules.system.rest.DeptController.update()','{\"pid\":7,\"updateTime\":1596350927000,\"enabled\":true,\"deptSort\":1,\"subCount\":0,\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1553476532000,\"name\":\"研发部\",\"id\":2}','192.168.43.107',125,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 13:53:36'),(3564,'修改部门','INFO','me.calvin.modules.system.rest.DeptController.update()','{\"pid\":7,\"updateTime\":1589696847000,\"enabled\":true,\"deptSort\":2,\"subCount\":0,\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1553476844000,\"name\":\"运维部\",\"id\":5}','192.168.43.107',85,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 13:53:43'),(3565,'修改用户','INFO','me.calvin.modules.system.rest.UserController.update()','{\"gender\":\"男\",\"nickName\":\"管理员\",\"roles\":[],\"jobs\":[{\"updateTime\":1588649623000,\"enabled\":true,\"jobSort\":2,\"updateBy\":\"admin\",\"createTime\":1554010770000,\"name\":\"全栈开发\",\"id\":11}],\"avatarPath\":\"/Users/jie/Documents/work/me/admin/eladmin/~/avatar/avatar-20200806032259161.png\",\"updateTime\":1599273811000,\"dept\":{\"subCount\":0,\"name\":\"研发部\",\"id\":7},\"isAdmin\":false,\"enabled\":true,\"avatarName\":\"avatar-20200806032259161.png\",\"phone\":\"18888888888\",\"pwdResetTime\":1588495111000,\"updateBy\":\"admin\",\"createTime\":1534986716000,\"id\":1,\"email\":\"201507802@qq.com\",\"username\":\"admin\"}','192.168.43.107',917,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 13:54:00'),(3566,'修改角色','INFO','me.calvin.modules.system.rest.RoleController.update()','{\"level\":1,\"description\":\"超级管理员\",\"updateTime\":1623590487000,\"dataScope\":\"全部\",\"updateBy\":\"admin\",\"createTime\":1542942277000,\"name\":\"超级管理员\",\"id\":1,\"depts\":[]}','192.168.43.107',198,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 13:54:40'),(3567,'删除岗位','INFO','me.calvin.modules.system.rest.JobController.delete()','[8]','192.168.43.107',78,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 13:56:55'),(3568,'删除岗位','INFO','me.calvin.modules.system.rest.JobController.delete()','[10]','192.168.43.107',61,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 13:56:58'),(3569,'修改用户','INFO','me.calvin.modules.system.rest.UserController.update()','{\"gender\":\"男\",\"nickName\":\"管理员\",\"roles\":[],\"jobs\":[{\"updateTime\":1588649623000,\"enabled\":true,\"jobSort\":2,\"updateBy\":\"admin\",\"createTime\":1554010770000,\"name\":\"全栈开发\",\"id\":11}],\"avatarPath\":\"/Users/jie/Documents/work/me/admin/eladmin/~/avatar/avatar-20200806032259161.png\",\"updateTime\":1623650039000,\"dept\":{\"subCount\":0,\"name\":\"总部\",\"id\":7},\"isAdmin\":false,\"enabled\":true,\"avatarName\":\"avatar-20200806032259161.png\",\"phone\":\"18888888888\",\"pwdResetTime\":1588495111000,\"updateBy\":\"admin\",\"createTime\":1534986716000,\"id\":1,\"email\":\"179209347@qq.com\",\"username\":\"admin\"}','192.168.43.107',1004,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 13:57:43'),(3570,'zip包解压缩并提取特征值','INFO','me.calvin.modules.facesearch.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"10\"}','192.168.43.107',1708,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 15:06:30'),(3571,'zip包解压缩并提取特征值','INFO','me.calvin.modules.facesearch.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"10\"}','192.168.43.107',57536,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 15:50:38'),(3572,'初始化向量引擎','INFO','me.calvin.modules.facesearch.rest.SearchController.initSearchEngine()','','192.168.43.107',1719,'','内网IP','PostmanRuntime 7.26.8',NULL,'2021-06-14 16:16:09'),(3573,'zip包解压缩并提取特征值','INFO','me.calvin.modules.facesearch.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"10\"}','192.168.43.107',59015,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 16:21:46'),(3574,'zip包解压缩并提取特征值','INFO','me.calvin.modules.facesearch.rest.ImageInfoController.extractFeatures()','{\"type\":\"1\",\"id\":\"10\"}','192.168.43.107',186853,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 16:26:47'),(3575,'zip包解压缩并提取特征值','INFO','me.calvin.modules.facesearch.rest.ImageInfoController.extractFeatures()','{\"type\":\"1\",\"id\":\"10\"}','192.168.43.107',169648,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 16:38:34'),(3576,'初始化向量引擎','INFO','me.calvin.modules.facesearch.rest.SearchController.initSearchEngine()','','192.168.43.107',89,'','内网IP','PostmanRuntime 7.26.8',NULL,'2021-06-14 16:55:41'),(3577,'zip包解压缩并提取特征值','INFO','me.calvin.modules.facesearch.rest.ImageInfoController.extractFeatures()','{\"type\":\"1\",\"id\":\"10\"}','192.168.43.107',174053,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 17:13:11'),(3578,'删除菜单','INFO','me.calvin.modules.system.rest.MenuController.delete()','[30]','192.168.43.107',513,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 19:55:20'),(3579,'删除菜单','INFO','me.calvin.modules.system.rest.MenuController.delete()','[82]','192.168.43.107',136,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 19:55:29'),(3580,'删除菜单','INFO','me.calvin.modules.system.rest.MenuController.delete()','[116]','192.168.43.107',125,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 19:55:35'),(3581,'修改菜单','INFO','me.calvin.modules.system.rest.MenuController.update()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"icon\":\"image\",\"updateTime\":1623588904000,\"title\":\"搜索管理\",\"type\":1,\"subCount\":3,\"path\":\"facesearch\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623588904000,\"iFrame\":false,\"id\":118,\"menuSort\":1}','192.168.43.107',186,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 19:56:30'),(3582,'修改菜单','INFO','me.calvin.modules.system.rest.MenuController.update()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"pid\":118,\"updateTime\":1623589265000,\"title\":\"人像搜索\",\"type\":1,\"subCount\":0,\"path\":\"search\",\"component\":\"facesearch/search/index\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623589265000,\"iFrame\":false,\"id\":119,\"componentName\":\"PersonImageTable\",\"menuSort\":999}','192.168.43.107',195,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 19:56:40'),(3583,'修改菜单','INFO','me.calvin.modules.system.rest.MenuController.update()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"pid\":118,\"updateTime\":1623671800000,\"title\":\"人像搜索\",\"type\":1,\"subCount\":0,\"path\":\"search\",\"component\":\"facesearch/search/index\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623589265000,\"iFrame\":false,\"id\":119,\"componentName\":\"PersonImageTable\",\"menuSort\":1}','192.168.43.107',159,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 19:56:52'),(3584,'修改菜单','INFO','me.calvin.modules.system.rest.MenuController.update()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"pid\":118,\"updateTime\":1623590427000,\"title\":\"通用搜索\",\"type\":1,\"subCount\":0,\"path\":\"common\",\"component\":\"facesearch/common/index\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623590427000,\"iFrame\":false,\"id\":120,\"componentName\":\"CommonImageTable\",\"menuSort\":2}','192.168.43.107',167,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 19:57:00'),(3585,'修改菜单','INFO','me.calvin.modules.system.rest.MenuController.update()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"pid\":118,\"updateTime\":1623590471000,\"title\":\"图片信息\",\"type\":1,\"subCount\":0,\"path\":\"imageinfo\",\"component\":\"facesearch/imageinfo/index\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623590457000,\"iFrame\":false,\"id\":121,\"componentName\":\"ImageInfo\",\"menuSort\":3}','192.168.43.107',160,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 19:57:07'),(3586,'修改菜单','INFO','me.calvin.modules.system.rest.MenuController.update()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"icon\":\"search\",\"updateTime\":1623671790000,\"title\":\"搜索管理\",\"type\":1,\"subCount\":3,\"path\":\"facesearch\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623588904000,\"iFrame\":false,\"id\":118,\"menuSort\":1}','192.168.43.107',109,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 19:58:11'),(3587,'修改菜单','INFO','me.calvin.modules.system.rest.MenuController.update()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"icon\":\"peoples\",\"pid\":118,\"updateTime\":1623671812000,\"title\":\"人像搜索\",\"type\":1,\"subCount\":0,\"path\":\"search\",\"component\":\"facesearch/search/index\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623589265000,\"iFrame\":false,\"id\":119,\"componentName\":\"PersonImageTable\",\"menuSort\":1}','192.168.43.107',1191,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 19:58:47'),(3588,'修改菜单','INFO','me.calvin.modules.system.rest.MenuController.update()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"icon\":\"image\",\"pid\":118,\"updateTime\":1623671820000,\"title\":\"通用搜索\",\"type\":1,\"subCount\":0,\"path\":\"common\",\"component\":\"facesearch/common/index\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623590427000,\"iFrame\":false,\"id\":120,\"componentName\":\"CommonImageTable\",\"menuSort\":2}','192.168.43.107',139,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 19:59:39'),(3589,'修改菜单','INFO','me.calvin.modules.system.rest.MenuController.update()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"icon\":\"icon\",\"pid\":118,\"updateTime\":1623671978000,\"title\":\"通用搜索\",\"type\":1,\"subCount\":0,\"path\":\"common\",\"component\":\"facesearch/common/index\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623590427000,\"iFrame\":false,\"id\":120,\"componentName\":\"CommonImageTable\",\"menuSort\":2}','192.168.43.107',130,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:00:38'),(3590,'修改菜单','INFO','me.calvin.modules.system.rest.MenuController.update()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"icon\":\"education\",\"pid\":118,\"updateTime\":1623671827000,\"title\":\"图片信息\",\"type\":1,\"subCount\":0,\"path\":\"imageinfo\",\"component\":\"facesearch/imageinfo/index\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623590457000,\"iFrame\":false,\"id\":121,\"componentName\":\"ImageInfo\",\"menuSort\":3}','192.168.43.107',136,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:02:02'),(3591,'查询ImageInfoService','INFO','me.calvin.modules.facesearch.rest.ImageInfoController.query()','','192.168.43.107',298,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:02:13'),(3592,'修改菜单','INFO','me.calvin.modules.system.rest.MenuController.update()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"icon\":\"peoples\",\"pid\":118,\"updateTime\":1623671926000,\"title\":\"人像搜索\",\"type\":1,\"subCount\":0,\"path\":\"search\",\"component\":\"imagesearch/search/index\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623589265000,\"iFrame\":false,\"id\":119,\"componentName\":\"PersonImageTable\",\"menuSort\":1}','192.168.43.107',192,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:04:51'),(3593,'修改菜单','INFO','me.calvin.modules.system.rest.MenuController.update()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"icon\":\"icon\",\"pid\":118,\"updateTime\":1623672038000,\"title\":\"通用搜索\",\"type\":1,\"subCount\":0,\"path\":\"common\",\"component\":\"imagesearch/common/index\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623590427000,\"iFrame\":false,\"id\":120,\"componentName\":\"CommonImageTable\",\"menuSort\":2}','192.168.43.107',163,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:04:57'),(3594,'修改菜单','INFO','me.calvin.modules.system.rest.MenuController.update()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"icon\":\"education\",\"pid\":118,\"updateTime\":1623672121000,\"title\":\"图片信息\",\"type\":1,\"subCount\":0,\"path\":\"imageinfo\",\"component\":\"imagesearch/imageinfo/index\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623590457000,\"iFrame\":false,\"id\":121,\"componentName\":\"ImageInfo\",\"menuSort\":3}','192.168.43.107',154,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:05:03'),(3595,'新增菜单','INFO','me.calvin.modules.system.rest.MenuController.create()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"icon\":\"log\",\"pid\":118,\"updateTime\":1623672384231,\"title\":\"图像日志\",\"type\":1,\"subCount\":0,\"path\":\"imagelog\",\"component\":\"imagelog/index\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623672384231,\"iFrame\":false,\"id\":122,\"componentName\":\"ImageLog\",\"menuSort\":4}','192.168.43.107',68,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:06:24'),(3596,'修改角色菜单','INFO','me.calvin.modules.system.rest.RoleController.updateMenu()','{\"level\":3,\"dataScope\":\"本级\",\"id\":1,\"menus\":[{\"subCount\":0,\"id\":35,\"menuSort\":999},{\"subCount\":0,\"id\":36,\"menuSort\":999},{\"subCount\":0,\"id\":37,\"menuSort\":999},{\"subCount\":0,\"id\":38,\"menuSort\":999},{\"subCount\":0,\"id\":39,\"menuSort\":999},{\"subCount\":0,\"id\":41,\"menuSort\":999},{\"subCount\":0,\"id\":44,\"menuSort\":999},{\"subCount\":0,\"id\":45,\"menuSort\":999},{\"subCount\":0,\"id\":46,\"menuSort\":999},{\"subCount\":0,\"id\":48,\"menuSort\":999},{\"subCount\":0,\"id\":49,\"menuSort\":999},{\"subCount\":0,\"id\":50,\"menuSort\":999},{\"subCount\":0,\"id\":52,\"menuSort\":999},{\"subCount\":0,\"id\":53,\"menuSort\":999},{\"subCount\":0,\"id\":54,\"menuSort\":999},{\"subCount\":0,\"id\":118,\"menuSort\":999},{\"subCount\":0,\"id\":119,\"menuSort\":999},{\"subCount\":0,\"id\":56,\"menuSort\":999},{\"subCount\":0,\"id\":120,\"menuSort\":999},{\"subCount\":0,\"id\":57,\"menuSort\":999},{\"subCount\":0,\"id\":121,\"menuSort\":999},{\"subCount\":0,\"id\":58,\"menuSort\":999},{\"subCount\":0,\"id\":122,\"menuSort\":999},{\"subCount\":0,\"id\":60,\"menuSort\":999},{\"subCount\":0,\"id\":61,\"menuSort\":999},{\"subCount\":0,\"id\":62,\"menuSort\":999},{\"subCount\":0,\"id\":64,\"menuSort\":999},{\"subCount\":0,\"id\":1,\"menuSort\":999},{\"subCount\":0,\"id\":65,\"menuSort\":999},{\"subCount\":0,\"id\":2,\"menuSort\":999},{\"subCount\":0,\"id\":66,\"menuSort\":999},{\"subCount\":0,\"id\":3,\"menuSort\":999},{\"subCount\":0,\"id\":5,\"menuSort\":999},{\"subCount\":0,\"id\":6,\"menuSort\":999},{\"subCount\":0,\"id\":7,\"menuSort\":999},{\"subCount\":0,\"id\":9,\"menuSort\":999},{\"subCount\":0,\"id\":73,\"menuSort\":999},{\"subCount\":0,\"id\":74,\"menuSort\":999},{\"subCount\":0,\"id\":75,\"menuSort\":999},{\"subCount\":0,\"id\":77,\"menuSort\":999},{\"subCount\":0,\"id\":78,\"menuSort\":999},{\"subCount\":0,\"id\":79,\"menuSort\":999},{\"subCount\":0,\"id\":80,\"menuSort\":999},{\"subCount\":0,\"id\":18,\"menuSort\":999},{\"subCount\":0,\"id\":28,\"menuSort\":999},{\"subCount\":0,\"id\":32,\"menuSort\":999}]}','192.168.43.107',661,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:06:36'),(3597,'查询ImageLogService','INFO','me.calvin.modules.facesearch.rest.ImageLogController.query()','','192.168.43.107',124,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:06:42'),(3598,'查询ImageLogService','INFO','me.calvin.modules.facesearch.rest.ImageLogController.query()','','192.168.43.107',100,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:08:01'),(3599,'查询ImageLogService','INFO','me.calvin.modules.facesearch.rest.ImageLogController.query()','','192.168.43.107',804,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:09:19'),(3600,'修改菜单','INFO','me.calvin.modules.system.rest.MenuController.update()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"icon\":\"peoples\",\"pid\":118,\"updateTime\":1623672291000,\"title\":\"人像搜索\",\"type\":1,\"subCount\":0,\"path\":\"search\",\"component\":\"facesearch/search/index\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623589265000,\"iFrame\":false,\"id\":119,\"componentName\":\"PersonImageTable\",\"menuSort\":1}','192.168.43.107',250,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:10:50'),(3601,'修改菜单','INFO','me.calvin.modules.system.rest.MenuController.update()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"icon\":\"icon\",\"pid\":118,\"updateTime\":1623672297000,\"title\":\"通用搜索\",\"type\":1,\"subCount\":0,\"path\":\"common\",\"component\":\"facesearch/common/index\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623590427000,\"iFrame\":false,\"id\":120,\"componentName\":\"CommonImageTable\",\"menuSort\":2}','192.168.43.107',161,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:10:56'),(3602,'修改菜单','INFO','me.calvin.modules.system.rest.MenuController.update()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"icon\":\"education\",\"pid\":118,\"updateTime\":1623672303000,\"title\":\"图片信息\",\"type\":1,\"subCount\":0,\"path\":\"imageinfo\",\"component\":\"facesearch/imageinfo/index\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623590457000,\"iFrame\":false,\"id\":121,\"componentName\":\"ImageInfo\",\"menuSort\":3}','192.168.43.107',171,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:11:03'),(3603,'查询ImageInfoService','INFO','me.calvin.modules.facesearch.rest.ImageInfoController.query()','','192.168.43.107',161,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:11:07'),(3604,'查询ImageLogService','INFO','me.calvin.modules.facesearch.rest.ImageLogController.query()','','192.168.43.107',109,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:12:53'),(3605,'查询ImageLogService','INFO','me.calvin.modules.facesearch.rest.ImageLogController.query()','','192.168.43.107',102,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:13:42'),(3606,'查询ImageLogService','INFO','me.calvin.modules.facesearch.rest.ImageLogController.query()','','192.168.43.107',118,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:14:32'),(3607,'查询ImageLogService','INFO','me.calvin.modules.facesearch.rest.ImageLogController.query()','','192.168.43.107',203,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:21:46'),(3608,'查询ImageLogService','INFO','me.calvin.modules.facesearch.rest.ImageLogController.query()','','192.168.43.107',208,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:22:34'),(3609,'修改菜单','INFO','me.calvin.modules.system.rest.MenuController.update()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"icon\":\"education\",\"pid\":118,\"updateTime\":1623672662000,\"title\":\"图片信息\",\"type\":1,\"subCount\":0,\"path\":\"imageinfo\",\"component\":\"imagesearch/imageinfo/index\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623590457000,\"iFrame\":false,\"id\":121,\"componentName\":\"ImageInfo\",\"menuSort\":3}','192.168.43.107',184,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:23:19'),(3610,'查询ImageInfoService','INFO','me.calvin.modules.facesearch.rest.ImageInfoController.query()','','192.168.43.107',164,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:23:28'),(3611,'查询ImageLogService','INFO','me.calvin.modules.facesearch.rest.ImageLogController.query()','','192.168.43.107',276,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:25:10'),(3612,'删除菜单','INFO','me.calvin.modules.system.rest.MenuController.delete()','[122]','192.168.43.107',206,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:26:20'),(3613,'查询ImageInfoService','INFO','me.calvin.modules.facesearch.rest.ImageInfoController.query()','','192.168.43.107',180,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:27:50'),(3614,'zip包解压缩并提取特征值','INFO','me.calvin.modules.facesearch.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"11\"}','192.168.43.107',61738,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 20:34:24'),(3615,'查询ImageInfoService','INFO','me.calvin.modules.search.rest.ImageInfoController.query()','','192.168.1.4',359,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 23:46:02'),(3616,'修改菜单','INFO','me.calvin.modules.system.rest.MenuController.update()','{\"cache\":false,\"hidden\":false,\"roles\":[],\"icon\":\"search\",\"updateTime\":1623671890000,\"title\":\"搜索管理\",\"type\":1,\"subCount\":3,\"path\":\"imagesearch\",\"createBy\":\"admin\",\"updateBy\":\"admin\",\"createTime\":1623588904000,\"iFrame\":false,\"id\":118,\"menuSort\":1}','192.168.1.4',424,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 23:46:56'),(3617,'查询ImageInfoService','INFO','me.calvin.modules.search.rest.ImageInfoController.query()','','192.168.1.4',209,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 23:47:04'),(3618,'查询ImageInfoService','INFO','me.calvin.modules.search.rest.ImageInfoController.query()','','192.168.1.4',150,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-14 23:47:13'),(3619,'初始化向量引擎','INFO','me.calvin.modules.search.rest.SearchController.initSearchEngine()','','fe80:0:0:0:aede:48ff:fe00:1122%4',3165,'','内网IP','PostmanRuntime 7.26.8',NULL,'2021-06-15 10:53:45'),(3620,'查询ImageInfoService','INFO','me.calvin.modules.search.rest.ImageInfoController.query()','','fe80:0:0:0:aede:48ff:fe00:1122%4',459,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-15 12:58:18'),(3621,'查询ImageInfoService','INFO','me.calvin.modules.search.rest.ImageInfoController.query()','','10.130.92.178',620,'admin','内网IP','Chrome 91.0.4472.77',NULL,'2021-06-15 14:42:22'),(3622,'查询ImageInfoService','INFO','me.calvin.modules.search.rest.ImageInfoController.query()','','192.168.43.107',79,'admin','内网IP','Chrome 91.0.4472.106',NULL,'2021-06-20 20:34:26'),(3623,'查询ImageInfoService','INFO','me.calvin.modules.search.rest.ImageInfoController.query()','','192.168.43.107',111,'admin','内网IP','Chrome 91.0.4472.106',NULL,'2021-06-20 21:07:55'),(3624,'查询ImageInfoService','INFO','me.calvin.modules.search.rest.ImageInfoController.query()','','192.168.1.150',37,'admin','内网IP','Chrome 96.0.4664.55',NULL,'2021-12-06 15:58:10'),(3625,'查询ImageInfoService','INFO','me.calvin.modules.search.rest.ImageInfoController.query()','','192.168.1.150',19,'admin','内网IP','Chrome 96.0.4664.55',NULL,'2021-12-06 16:02:06'),(3626,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"12\"}','192.168.1.150',52177,'admin','内网IP','Chrome 96.0.4664.55',NULL,'2021-12-06 18:21:34'),(3627,'删除菜单','INFO','me.calvin.modules.system.rest.MenuController.delete()','[119]','192.168.1.100',443,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-04 16:29:42'),(3628,'查询ImageInfoService','INFO','me.calvin.modules.search.rest.ImageInfoController.query()','','192.168.1.100',509,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-04 16:29:51'),(3629,'查询ImageInfoService','INFO','me.calvin.modules.search.rest.ImageInfoController.query()','','192.168.1.100',172,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-04 17:22:32'),(3630,'查询ImageInfoService','INFO','me.calvin.modules.search.rest.ImageInfoController.query()','','192.168.1.100',391,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-05 09:54:50'),(3631,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[12]','192.168.1.100',180,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-05 09:55:59'),(3635,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"13\"}','192.168.1.100',20932,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-05 10:09:59'),(3636,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"13\"}','192.168.1.100',21564,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-05 10:42:31'),(3637,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"13\"}','192.168.1.100',21378,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-05 10:50:57'),(3638,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"13\"}','192.168.1.100',22294,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-05 11:28:39'),(3639,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"13\"}','192.168.1.100',20381,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-05 11:31:14'),(3640,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"14\"}','192.168.1.100',73,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-05 11:32:32'),(3641,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"14\"}','192.168.1.100',90,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-05 11:33:26'),(3642,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"14\"}','192.168.1.100',37,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-05 11:33:59'),(3643,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"14\"}','192.168.1.100',59,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-05 11:34:22'),(3644,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"14\"}','192.168.1.100',28,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-05 11:34:25'),(3645,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"14\"}','192.168.1.100',23,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-05 11:34:26'),(3646,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"14\"}','192.168.1.100',64,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-05 11:34:26'),(3647,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"14\"}','192.168.1.100',29,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-05 11:34:26'),(3648,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"14\"}','192.168.1.100',33,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-05 11:34:27'),(3649,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"14\"}','192.168.1.100',24,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-05 11:34:27'),(3650,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"13\"}','192.168.1.100',31529,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-05 11:42:25'),(3651,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"13\"}','192.168.1.100',20997,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-05 11:45:23'),(3652,'查询ImageInfoService','INFO','me.calvin.modules.search.rest.ImageInfoController.query()','','192.168.1.4',336,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-06 16:21:03'),(3653,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[13]','192.168.1.4',294,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-06 17:01:24'),(3654,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[14]','192.168.1.4',42,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-06 17:01:33'),(3655,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"15\"}','192.168.1.4',8419,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-06 17:03:37'),(3656,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"15\"}','192.168.1.4',10734,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-06 17:20:43'),(3657,'删除图片特征及其信息','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteFeatures()','{}','192.168.1.4',97637,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-06 18:18:03'),(3658,'删除图片特征及其信息','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteFeatures()','{}','192.168.1.4',82,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-06 18:18:43'),(3659,'删除图片特征及其信息','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteFeatures()','{}','192.168.1.4',41,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-06 18:18:45'),(3660,'删除图片特征及其信息','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteFeatures()','{}','192.168.1.4',53,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-06 18:18:46'),(3661,'删除图片特征及其信息','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteFeatures()','{}','192.168.1.4',43,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-06 18:18:46'),(3662,'删除图片特征及其信息','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteFeatures()','{}','192.168.1.4',31,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-06 18:18:46'),(3663,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"15\"}','192.168.1.4',7365,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-06 18:19:08'),(3664,'删除图片特征及其信息','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteFeatures()','{}','192.168.1.4',449,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-06 18:24:26'),(3665,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"15\"}','192.168.1.4',7047,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-06 18:24:51'),(3666,'删除图片特征及其信息','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteFeatures()','{}','192.168.1.4',489,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-06 18:32:10'),(3667,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"15\"}','192.168.1.4',6493,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-06 18:32:29'),(3668,'删除图片特征及其信息','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteFeatures()','{}','192.168.1.4',482,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-06 19:05:33'),(3669,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"15\"}','192.168.1.4',28180,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-06 19:08:00'),(3670,'删除图片特征及其信息','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteFeatures()','{}','192.168.1.4',687,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-06 19:24:09'),(3671,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"15\"}','192.168.1.4',7200,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-06 19:24:25'),(3672,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"16\"}','192.168.1.150',8935,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-07 16:45:04'),(3673,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"16\"}','192.168.1.150',6371,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-07 16:58:55'),(3674,'zip包解压缩并提取特征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"type\":\"0\",\"id\":\"15\"}','192.168.1.150',6068,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-07 17:00:32'),(3675,'新增用户','INFO','me.calvin.modules.system.rest.UserController.create()','{\"gender\":\"男\",\"nickName\":\"aaaaa\",\"roles\":[{\"level\":3,\"dataScope\":\"本级\",\"id\":1}],\"jobs\":[{\"id\":11}],\"updateTime\":1644392357204,\"dept\":{\"subCount\":0,\"id\":7},\"isAdmin\":false,\"enabled\":true,\"password\":\"$2a$10$kBgg2Ck/uSQOOJMQ7P/IWOCd0eff9ftkTjibQcoEjTgkfPKYbiXfe\",\"createBy\":\"admin\",\"phone\":\"13800138000\",\"updateBy\":\"admin\",\"createTime\":1644392357204,\"id\":3,\"email\":\"1231@qq.com\",\"username\":\"aaaa\"}','192.168.1.150',1162,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-09 15:39:17'),(3676,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[21,20,19,18,17]','192.168.1.150',380,'aaaa','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-09 18:29:58'),(3677,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[22,16,15]','192.168.1.150',154,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 16:36:06'),(3678,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[23]','192.168.1.150',109,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 16:47:12'),(3679,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[24]','192.168.1.150',132,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 16:55:25'),(3680,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[26,25]','192.168.1.150',196,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 17:04:54'),(3681,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[27]','192.168.1.150',119,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 17:05:59'),(3682,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[28]','192.168.1.150',130,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 17:15:07'),(3683,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[30,31,32]','192.168.1.150',182,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 17:30:56'),(3684,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[29]','192.168.1.150',99,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 17:31:30'),(3685,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[33]','192.168.1.150',124,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 17:44:50'),(3686,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[35,34]','192.168.1.150',250,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 18:05:10'),(3687,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[36]','192.168.1.150',195,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 18:25:23'),(3688,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"37\"}','192.168.1.150',138921,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 18:37:48'),(3689,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"5\"}]','192.168.1.150',9846,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 18:53:26'),(3690,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"5\"}]','192.168.1.150',147515,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 18:56:24'),(3691,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"5\"}]','192.168.1.150',905,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 18:57:10'),(3692,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"10\"}]','192.168.1.150',327,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 18:57:17'),(3693,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"37\"}','192.168.1.150',14379,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 18:58:07'),(3694,'查询ImageInfoService','INFO','me.calvin.modules.search.rest.ImageInfoController.query()','','192.168.1.150',192,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 18:58:27'),(3695,'查询ImageInfoService','INFO','me.calvin.modules.search.rest.ImageInfoController.query()','','192.168.1.150',351,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 18:59:40'),(3696,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"5\"}]','192.168.1.150',1307,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 19:00:33'),(3697,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"2\"}]','192.168.1.150',896,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 19:00:53'),(3698,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"1\"}]','192.168.1.150',520,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 19:00:56'),(3699,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"20\"}]','192.168.1.150',1454,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 19:01:09'),(3700,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"20\"}]','192.168.1.150',1661,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 19:01:09'),(3701,'查询ImageInfoService','INFO','me.calvin.modules.search.rest.ImageInfoController.query()','','192.168.1.150',223,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 19:01:18'),(3702,'查询ImageInfoService','INFO','me.calvin.modules.search.rest.ImageInfoController.query()','','192.168.1.150',374,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 19:02:22'),(3703,'查询ImageInfoService','INFO','me.calvin.modules.search.rest.ImageInfoController.query()','','192.168.1.150',185,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 19:03:46'),(3704,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"5\"}]','192.168.1.150',1740,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 19:10:43'),(3705,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"6\"}]','192.168.1.150',696,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 19:10:48'),(3706,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"1\"}]','192.168.1.150',541,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 19:10:52'),(3708,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[40]','192.168.1.150',122,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 19:26:19'),(3709,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[39]','192.168.1.150',26914,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 19:28:03'),(3711,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[43]','192.168.1.150',25257,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 19:35:24'),(3712,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[42,41]','192.168.1.150',74,'admin','内网IP','Chrome 97.0.4692.99',NULL,'2022-02-10 19:35:30'),(3713,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"44\"}','192.168.1.150',6733,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-11 17:19:57'),(3714,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"44\"}','192.168.1.150',6552,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-11 17:21:04'),(3715,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"44\"}','192.168.1.150',5437,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-11 17:25:50'),(3716,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[44]','192.168.1.150',148,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-11 17:28:40'),(3717,'删除图片特征及其信息','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteFeatures()','{}','192.168.1.150',7876,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-11 17:42:11'),(3718,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[45]','192.168.1.150',1000,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-11 17:42:18'),(3719,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[46]','192.168.1.150',177,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-11 18:07:21'),(3720,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[47]','192.168.1.150',140,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-11 18:13:02'),(3721,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[49]','192.168.1.150',125,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-11 18:49:37'),(3722,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[50]','192.168.1.150',460,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-11 18:51:10'),(3723,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[51]','192.168.1.150',467,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-11 19:47:32'),(3724,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[48]','192.168.1.150',287,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-11 19:47:36'),(3725,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[52]','192.168.1.150',481,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-11 19:51:32'),(3726,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[55,54,53]','192.168.1.150',363,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-11 20:09:25'),(3727,'删除图片特征及其信息','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteFeatures()','{}','192.168.1.4',45177,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-11 23:19:00'),(3728,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[56]','192.168.1.4',870,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-11 23:19:27'),(3729,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"57\"}','192.168.1.4',28876,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-11 23:33:11'),(3730,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"57\"}','192.168.1.4',29703,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 00:37:54'),(3731,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"57\"}','192.168.1.4',31615,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 00:44:21'),(3732,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"57\"}','192.168.1.4',47425,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 00:57:51'),(3733,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"57\"}','192.168.1.4',27546,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 00:58:24'),(3734,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"57\"}','192.168.1.4',14707,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 14:29:13'),(3735,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"57\"}','192.168.1.4',12287,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 14:29:53'),(3736,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"57\"}','192.168.1.4',11567,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 14:30:38'),(3737,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"57\"}','192.168.1.4',11714,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 14:30:55'),(3738,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"57\"}','192.168.1.4',11361,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 14:31:19'),(3739,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"57\"}','192.168.1.4',11640,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 14:31:36'),(3740,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"57\"}','192.168.1.4',11554,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 14:31:52'),(3741,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"58\"}','192.168.1.4',68395,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 14:34:38'),(3742,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"58\"}','192.168.1.4',67955,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 14:36:18'),(3743,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"58\"}','192.168.1.4',69966,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 14:37:55'),(3744,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"5\"}]','192.168.1.4',1322,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 14:39:01'),(3745,'删除图片特征及其信息','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteFeatures()','{}','192.168.1.4',212,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:04:31'),(3746,'删除图片特征及其信息','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteFeatures()','{}','192.168.1.4',29,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:04:43'),(3747,'删除文件','INFO','me.calvin.rest.LocalStorageController.delete()','[58,57]','192.168.1.4',447,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:04:48'),(3748,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"59\"}','192.168.1.4',75061,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:08:12'),(3749,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"5\"}]','192.168.1.4',786,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:08:58'),(3750,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"5\"}]','192.168.1.4',660,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:09:13'),(3751,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"5\"}]','192.168.1.4',506,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:09:40'),(3752,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"5\"}]','192.168.1.4',646,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:10:52'),(3753,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"100\"}]','192.168.1.4',1042,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:11:00'),(3754,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"1200\"}]','192.168.1.4',6768,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:11:13'),(3755,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"60\"}','192.168.1.4',2472,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:27:57'),(3756,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"61\"}','192.168.1.4',1726,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:28:01'),(3757,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"62\"}','192.168.1.4',19878,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:28:47'),(3760,'删除所有图片信息(含特征)','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteImages()','[60]','192.168.1.4',65209,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:38:21'),(3761,'删除所有图片信息(含特征)','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteImages()','[61]','192.168.1.4',314,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:38:39'),(3762,'删除所有图片信息(含特征)','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteImages()','[62]','192.168.1.4',2475,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:38:48'),(3763,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"5\"}]','192.168.1.4',975,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:39:12'),(3764,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"2000\"}]','192.168.1.4',6131,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:39:22'),(3765,'删除图片特征','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteFeatures()','{}','192.168.1.4',5552,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:39:53'),(3766,'删除所有图片信息(含特征)','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteImages()','[59]','192.168.1.4',29367,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:41:11'),(3767,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"5\"}]','192.168.1.4',581,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:41:21'),(3768,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"63\"}','192.168.1.4',15495,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:45:46'),(3769,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"500\"}]','192.168.1.4',1521,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:46:02'),(3770,'删除图片特征','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteFeatures()','{}','192.168.1.4',1076,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:46:11'),(3771,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"63\"}','192.168.1.4',14542,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:50:42'),(3772,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"63\"}','192.168.1.4',15547,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:54:57'),(3773,'删除图片特征','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteFeatures()','{}','192.168.1.4',1005,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:55:03'),(3774,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"63\"}','192.168.1.4',16704,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:56:09'),(3775,'删除图片特征','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteFeatures()','{}','192.168.1.4',1095,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:56:26'),(3776,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"63\"}','192.168.1.4',14316,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:56:44'),(3777,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"5\"}]','192.168.1.4',589,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:57:02'),(3778,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"500\"}]','192.168.1.4',1674,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 15:57:07'),(3779,'查询ImageInfoService','INFO','me.calvin.modules.search.rest.ImageInfoController.query()','','192.168.1.4',155,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 16:01:51'),(3780,'删除菜单','INFO','me.calvin.modules.system.rest.MenuController.delete()','[121]','192.168.1.4',242,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 16:03:07'),(3781,'删除所有ERROR日志','INFO','me.calvin.rest.LogController.delAllErrorLog()','','192.168.1.4',36,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-12 16:05:21'),(3782,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"66\"}','192.168.43.107',2534,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-16 12:48:55'),(3783,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"69\"}','192.168.43.107',78875,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-16 12:51:57'),(3784,'删除图片特征','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteFeatures()','{}','192.168.43.107',6588,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-16 12:52:28'),(3785,'删除所有图片信息(含特征)','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteImages()','[66]','192.168.43.107',602,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-16 12:52:36'),(3786,'删除所有图片信息(含特征)','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteImages()','[63,64,65]','192.168.43.107',3431,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-16 12:52:48'),(3787,'删除所有图片信息(含特征)','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteImages()','[67,68]','192.168.43.107',10740,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-16 12:53:07'),(3788,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"5\"}]','192.168.1.4',11061,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-19 18:31:55'),(3789,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"5\"}]','192.168.1.4',6911,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-19 18:32:07'),(3790,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"5\"}]','192.168.1.4',6006,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-19 18:32:35'),(3791,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"5\"}]','192.168.1.4',6530,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-19 18:32:53'),(3792,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"70\"}','192.168.1.4',1853,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-19 18:33:24'),(3793,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"5\"}]','192.168.1.4',678,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-19 18:33:33'),(3794,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"5\"}]','192.168.1.4',1048,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-19 18:48:41'),(3795,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"69\"}','192.168.1.4',74411,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-19 19:38:24'),(3796,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"69\"}','192.168.1.4',73839,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-19 19:41:14'),(3797,'搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"500\"}]','192.168.1.4',3535,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-19 19:41:33'),(3798,'删除用户','INFO','me.calvin.modules.system.rest.UserController.delete()','[3]','192.168.1.4',310,'admin','内网IP','Chrome 98.0.4758.80',NULL,'2022-02-19 19:43:45'),(3799,'删除图片特征','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteFeatures()','{}','192.168.9.215',505,'admin','内网IP','Chrome 114',NULL,'2023-07-06 16:44:44'),(3800,'删除图片特征','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteFeatures()','{}','192.168.9.215',168,'admin','内网IP','Chrome 114',NULL,'2023-07-06 16:44:52'),(3801,'删除所有图片信息(含特征)','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteImages()','[70]','192.168.9.215',104,'admin','内网IP','Chrome 114',NULL,'2023-07-06 16:44:57'),(3802,'删除所有图片信息(含特征)','INFO','me.calvin.modules.search.rest.ImageInfoController.deleteImages()','[69]','192.168.9.215',625,'admin','内网IP','Chrome 114',NULL,'2023-07-06 16:45:02'),(3803,'批量提取特文件夹图片征值','INFO','me.calvin.modules.search.rest.ImageInfoController.extractFeatures()','{\"id\":\"71\"}','192.168.9.215',7720,'admin','内网IP','Chrome 114',NULL,'2023-07-06 16:45:40'),(3804,'管理端搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"50\"}]','192.168.9.215',358,'admin','内网IP','Chrome 114',NULL,'2023-07-06 16:45:53'),(3805,'管理端搜索图片','INFO','me.calvin.modules.search.rest.SearchController.searchImage()','[{\"file\":{}},{\"topK\":\"50\"}]','192.168.9.215',542,'admin','内网IP','Chrome 114',NULL,'2023-07-06 16:46:29');
/*!40000 ALTER TABLE `sys_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` bigint DEFAULT NULL COMMENT '上级菜单ID',
  `sub_count` int DEFAULT '0' COMMENT '子菜单数目',
  `type` int DEFAULT NULL COMMENT '菜单类型',
  `title` varchar(255) DEFAULT NULL COMMENT '菜单标题',
  `name` varchar(255) DEFAULT NULL COMMENT '组件名称',
  `component` varchar(255) DEFAULT NULL COMMENT '组件',
  `menu_sort` int DEFAULT NULL COMMENT '排序',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标',
  `path` varchar(255) DEFAULT NULL COMMENT '链接地址',
  `i_frame` bit(1) DEFAULT NULL COMMENT '是否外链',
  `cache` bit(1) DEFAULT b'0' COMMENT '缓存',
  `hidden` bit(1) DEFAULT b'0' COMMENT '隐藏',
  `permission` varchar(255) DEFAULT NULL COMMENT '权限',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`menu_id`) USING BTREE,
  UNIQUE KEY `uniq_title` (`title`),
  UNIQUE KEY `uniq_name` (`name`),
  KEY `inx_pid` (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='系统菜单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,NULL,7,0,'系统管理',NULL,NULL,2,'system','system',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,'admin','2021-06-14 13:53:11','2021-06-14 13:53:11'),(2,1,3,1,'用户管理','User','system/user/index',2,'peoples','user',_binary '\0',_binary '\0',_binary '\0','user:list',NULL,NULL,'2021-06-14 13:53:11',NULL),(3,1,3,1,'角色管理','Role','system/role/index',3,'role','role',_binary '\0',_binary '\0',_binary '\0','roles:list',NULL,NULL,'2021-06-14 13:53:11',NULL),(5,1,3,1,'菜单管理','Menu','system/menu/index',5,'menu','menu',_binary '\0',_binary '\0',_binary '\0','menu:list',NULL,NULL,'2021-06-14 13:53:11',NULL),(6,NULL,5,0,'系统监控',NULL,NULL,10,'monitor','monitor',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'2021-06-14 13:53:11',NULL),(7,6,0,1,'操作日志','Log','monitor/log/index',11,'log','logs',_binary '\0',_binary '',_binary '\0',NULL,NULL,'admin','2021-06-14 13:53:11','2021-06-14 13:53:11'),(9,6,0,1,'SQL监控','Sql','monitor/sql/index',18,'sqlMonitor','druid',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'2021-06-14 13:53:11',NULL),(18,36,3,1,'存储管理','Storage','tools/storage/index',34,'qiniu','storage',_binary '\0',_binary '\0',_binary '\0','storage:list',NULL,NULL,'2021-06-14 13:53:11',NULL),(28,1,3,1,'任务调度','Timing','system/timing/index',999,'timing','timing',_binary '\0',_binary '\0',_binary '\0','timing:list',NULL,NULL,'2021-06-14 13:53:11',NULL),(32,6,0,1,'异常日志','ErrorLog','monitor/log/errorLog',12,'error','errorLog',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'2021-06-14 13:53:11',NULL),(35,1,3,1,'部门管理','Dept','system/dept/index',6,'dept','dept',_binary '\0',_binary '\0',_binary '\0','dept:list',NULL,NULL,'2021-06-14 13:53:11',NULL),(36,NULL,2,0,'系统工具',NULL,'',30,'sys-tools','sys-tools',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'2021-06-14 13:53:11',NULL),(37,1,3,1,'岗位管理','Job','system/job/index',7,'Steve-Jobs','job',_binary '\0',_binary '\0',_binary '\0','job:list',NULL,NULL,'2021-06-14 13:53:11',NULL),(38,36,0,1,'接口文档','Swagger','tools/swagger/index',36,'swagger','swagger2',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'2021-06-14 13:53:11',NULL),(39,1,3,1,'字典管理','Dict','system/dict/index',8,'dictionary','dict',_binary '\0',_binary '\0',_binary '\0','dict:list',NULL,NULL,'2021-06-14 13:53:11',NULL),(41,6,0,1,'在线用户','OnlineUser','monitor/online/index',10,'Steve-Jobs','online',_binary '\0',_binary '\0',_binary '\0',NULL,NULL,NULL,'2021-06-14 13:53:11',NULL),(44,2,0,2,'用户新增',NULL,'',2,'','',_binary '\0',_binary '\0',_binary '\0','user:add',NULL,NULL,'2021-06-14 13:53:11',NULL),(45,2,0,2,'用户编辑',NULL,'',3,'','',_binary '\0',_binary '\0',_binary '\0','user:edit',NULL,NULL,'2021-06-14 13:53:11',NULL),(46,2,0,2,'用户删除',NULL,'',4,'','',_binary '\0',_binary '\0',_binary '\0','user:del',NULL,NULL,'2021-06-14 13:53:11',NULL),(48,3,0,2,'角色创建',NULL,'',2,'','',_binary '\0',_binary '\0',_binary '\0','roles:add',NULL,NULL,'2021-06-14 13:53:11',NULL),(49,3,0,2,'角色修改',NULL,'',3,'','',_binary '\0',_binary '\0',_binary '\0','roles:edit',NULL,NULL,'2021-06-14 13:53:11',NULL),(50,3,0,2,'角色删除',NULL,'',4,'','',_binary '\0',_binary '\0',_binary '\0','roles:del',NULL,NULL,'2021-06-14 13:53:11',NULL),(52,5,0,2,'菜单新增',NULL,'',2,'','',_binary '\0',_binary '\0',_binary '\0','menu:add',NULL,NULL,'2021-06-14 13:53:11',NULL),(53,5,0,2,'菜单编辑',NULL,'',3,'','',_binary '\0',_binary '\0',_binary '\0','menu:edit',NULL,NULL,'2021-06-14 13:53:11',NULL),(54,5,0,2,'菜单删除',NULL,'',4,'','',_binary '\0',_binary '\0',_binary '\0','menu:del',NULL,NULL,'2021-06-14 13:53:11',NULL),(56,35,0,2,'部门新增',NULL,'',2,'','',_binary '\0',_binary '\0',_binary '\0','dept:add',NULL,NULL,'2021-06-14 13:53:11',NULL),(57,35,0,2,'部门编辑',NULL,'',3,'','',_binary '\0',_binary '\0',_binary '\0','dept:edit',NULL,NULL,'2021-06-14 13:53:11',NULL),(58,35,0,2,'部门删除',NULL,'',4,'','',_binary '\0',_binary '\0',_binary '\0','dept:del',NULL,NULL,'2021-06-14 13:53:11',NULL),(60,37,0,2,'岗位新增',NULL,'',2,'','',_binary '\0',_binary '\0',_binary '\0','job:add',NULL,NULL,'2021-06-14 13:53:11',NULL),(61,37,0,2,'岗位编辑',NULL,'',3,'','',_binary '\0',_binary '\0',_binary '\0','job:edit',NULL,NULL,'2021-06-14 13:53:11',NULL),(62,37,0,2,'岗位删除',NULL,'',4,'','',_binary '\0',_binary '\0',_binary '\0','job:del',NULL,NULL,'2021-06-14 13:53:11',NULL),(64,39,0,2,'字典新增',NULL,'',2,'','',_binary '\0',_binary '\0',_binary '\0','dict:add',NULL,NULL,'2021-06-14 13:53:11',NULL),(65,39,0,2,'字典编辑',NULL,'',3,'','',_binary '\0',_binary '\0',_binary '\0','dict:edit',NULL,NULL,'2021-06-14 13:53:11',NULL),(66,39,0,2,'字典删除',NULL,'',4,'','',_binary '\0',_binary '\0',_binary '\0','dict:del',NULL,NULL,'2021-06-14 13:53:11',NULL),(73,28,0,2,'任务新增',NULL,'',2,'','',_binary '\0',_binary '\0',_binary '\0','timing:add',NULL,NULL,'2021-06-14 13:53:11',NULL),(74,28,0,2,'任务编辑',NULL,'',3,'','',_binary '\0',_binary '\0',_binary '\0','timing:edit',NULL,NULL,'2021-06-14 13:53:11',NULL),(75,28,0,2,'任务删除',NULL,'',4,'','',_binary '\0',_binary '\0',_binary '\0','timing:del',NULL,NULL,'2021-06-14 13:53:11',NULL),(77,18,0,2,'上传文件',NULL,'',2,'','',_binary '\0',_binary '\0',_binary '\0','storage:add',NULL,NULL,'2021-06-14 13:53:11',NULL),(78,18,0,2,'文件编辑',NULL,'',3,'','',_binary '\0',_binary '\0',_binary '\0','storage:edit',NULL,NULL,'2021-06-14 13:53:11',NULL),(79,18,0,2,'文件删除',NULL,'',4,'','',_binary '\0',_binary '\0',_binary '\0','storage:del',NULL,NULL,'2021-06-14 13:53:11',NULL),(80,6,0,1,'服务监控','ServerMonitor','monitor/server/index',14,'codeConsole','server',_binary '\0',_binary '\0',_binary '\0','monitor:list',NULL,'admin','2021-06-14 13:53:11','2021-06-14 13:53:11'),(118,NULL,1,1,'搜索管理',NULL,NULL,1,'search','imagesearch',_binary '\0',_binary '\0',_binary '\0',NULL,'admin','admin','2021-06-14 13:53:11','2021-06-14 23:46:56'),(120,118,0,1,'通用搜索','CommonImageTable','imagesearch/common/index',2,'icon','common',_binary '\0',_binary '\0',_binary '\0',NULL,'admin','admin','2021-06-14 13:53:11','2021-06-14 20:10:56');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_quartz_job`
--

DROP TABLE IF EXISTS `sys_quartz_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_quartz_job` (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `bean_name` varchar(255) DEFAULT NULL COMMENT 'Spring Bean名称',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT 'cron 表达式',
  `is_pause` bit(1) DEFAULT NULL COMMENT '状态：1暂停、0启用',
  `job_name` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `method_name` varchar(255) DEFAULT NULL COMMENT '方法名称',
  `params` varchar(255) DEFAULT NULL COMMENT '参数',
  `description` varchar(255) DEFAULT NULL COMMENT '备注',
  `person_in_charge` varchar(100) DEFAULT NULL COMMENT '负责人',
  `email` varchar(100) DEFAULT NULL COMMENT '报警邮箱',
  `sub_task` varchar(100) DEFAULT NULL COMMENT '子任务ID',
  `pause_after_failure` bit(1) DEFAULT NULL COMMENT '任务失败后是否暂停',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`job_id`) USING BTREE,
  KEY `inx_is_pause` (`is_pause`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='定时任务';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_quartz_job`
--

LOCK TABLES `sys_quartz_job` WRITE;
/*!40000 ALTER TABLE `sys_quartz_job` DISABLE KEYS */;
INSERT INTO `sys_quartz_job` VALUES (2,'testTask','0/5 * * * * ?',_binary '','测试1','run1','test','带参测试，多参使用json','测试',NULL,NULL,NULL,NULL,'admin','2021-06-14 13:53:11','2020-05-24 13:58:33');
/*!40000 ALTER TABLE `sys_quartz_job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_quartz_log`
--

DROP TABLE IF EXISTS `sys_quartz_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_quartz_log` (
  `log_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `bean_name` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `cron_expression` varchar(255) DEFAULT NULL,
  `exception_detail` text,
  `is_success` bit(1) DEFAULT NULL,
  `job_name` varchar(255) DEFAULT NULL,
  `method_name` varchar(255) DEFAULT NULL,
  `params` varchar(255) DEFAULT NULL,
  `time` bigint DEFAULT NULL,
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=151 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='定时任务日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_quartz_log`
--

LOCK TABLES `sys_quartz_log` WRITE;
/*!40000 ALTER TABLE `sys_quartz_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_quartz_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `level` int DEFAULT NULL COMMENT '角色级别',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `data_scope` varchar(255) DEFAULT NULL COMMENT '数据权限',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE KEY `uniq_name` (`name`),
  KEY `role_name_index` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'超级管理员',1,'超级管理员','全部',NULL,'admin','2021-06-14 13:53:11','2021-06-14 20:06:36'),(2,'普通用户',2,'-','本级',NULL,'admin','2021-06-14 13:53:11','2021-06-14 13:53:11');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_roles_depts`
--

DROP TABLE IF EXISTS `sys_roles_depts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_roles_depts` (
  `role_id` bigint NOT NULL,
  `dept_id` bigint NOT NULL,
  PRIMARY KEY (`role_id`,`dept_id`) USING BTREE,
  KEY `FK7qg6itn5ajdoa9h9o78v9ksur` (`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='角色部门关联';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_roles_depts`
--

LOCK TABLES `sys_roles_depts` WRITE;
/*!40000 ALTER TABLE `sys_roles_depts` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_roles_depts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_roles_menus`
--

DROP TABLE IF EXISTS `sys_roles_menus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_roles_menus` (
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`menu_id`,`role_id`) USING BTREE,
  KEY `FKcngg2qadojhi3a651a5adkvbq` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='角色菜单关联';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_roles_menus`
--

LOCK TABLES `sys_roles_menus` WRITE;
/*!40000 ALTER TABLE `sys_roles_menus` DISABLE KEYS */;
INSERT INTO `sys_roles_menus` VALUES (1,1),(2,1),(3,1),(5,1),(6,1),(7,1),(9,1),(18,1),(28,1),(32,1),(35,1),(36,1),(37,1),(38,1),(39,1),(41,1),(44,1),(45,1),(46,1),(48,1),(49,1),(50,1),(52,1),(53,1),(54,1),(56,1),(57,1),(58,1),(60,1),(61,1),(62,1),(64,1),(65,1),(66,1),(73,1),(74,1),(75,1),(77,1),(78,1),(79,1),(80,1),(118,1),(120,1),(1,2),(2,2),(6,2),(7,2),(9,2),(32,2),(36,2),(80,2);
/*!40000 ALTER TABLE `sys_roles_menus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门名称',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `nick_name` varchar(255) DEFAULT NULL COMMENT '昵称',
  `gender` varchar(2) DEFAULT NULL COMMENT '性别',
  `phone` varchar(255) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `avatar_name` varchar(255) DEFAULT NULL COMMENT '头像地址',
  `avatar_path` varchar(255) DEFAULT NULL COMMENT '头像真实路径',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `is_admin` bit(1) DEFAULT b'0' COMMENT '是否为admin账号',
  `enabled` bigint DEFAULT NULL COMMENT '状态：1启用、0禁用',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `pwd_reset_time` datetime DEFAULT NULL COMMENT '修改密码的时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `UK_kpubos9gc2cvtkb0thktkbkes` (`email`) USING BTREE,
  UNIQUE KEY `username` (`username`) USING BTREE,
  UNIQUE KEY `uniq_username` (`username`),
  UNIQUE KEY `uniq_email` (`email`),
  KEY `FK5rwmryny6jthaaxkogownknqp` (`dept_id`) USING BTREE,
  KEY `FKpq2dhypk2qgt68nauh2by22jb` (`avatar_name`) USING BTREE,
  KEY `inx_enabled` (`enabled`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='系统用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,7,'admin','管理员','男','18888888888','179209347@qq.com','avatar-20210614054350662.png','/Users/calvin/Downloads/eladmin-master/~/avatar/avatar-20210614054350662.png','$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa',_binary '',1,NULL,'admin','2020-05-03 16:38:31','2018-08-23 09:11:56','2021-06-14 17:43:51');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_users_jobs`
--

DROP TABLE IF EXISTS `sys_users_jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_users_jobs` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `job_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_users_jobs`
--

LOCK TABLES `sys_users_jobs` WRITE;
/*!40000 ALTER TABLE `sys_users_jobs` DISABLE KEYS */;
INSERT INTO `sys_users_jobs` VALUES (1,11);
/*!40000 ALTER TABLE `sys_users_jobs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_users_roles`
--

DROP TABLE IF EXISTS `sys_users_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_users_roles` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE,
  KEY `FKq4eq273l04bpu4efj0jd0jb98` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='用户角色关联';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_users_roles`
--

LOCK TABLES `sys_users_roles` WRITE;
/*!40000 ALTER TABLE `sys_users_roles` DISABLE KEYS */;
INSERT INTO `sys_users_roles` VALUES (1,1);
/*!40000 ALTER TABLE `sys_users_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tool_alipay_config`
--

DROP TABLE IF EXISTS `tool_alipay_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tool_alipay_config` (
  `config_id` bigint NOT NULL COMMENT 'ID',
  `app_id` varchar(255) DEFAULT NULL COMMENT '应用ID',
  `charset` varchar(255) DEFAULT NULL COMMENT '编码',
  `format` varchar(255) DEFAULT NULL COMMENT '类型 固定格式json',
  `gateway_url` varchar(255) DEFAULT NULL COMMENT '网关地址',
  `notify_url` varchar(255) DEFAULT NULL COMMENT '异步回调',
  `private_key` text COMMENT '私钥',
  `public_key` text COMMENT '公钥',
  `return_url` varchar(255) DEFAULT NULL COMMENT '回调地址',
  `sign_type` varchar(255) DEFAULT NULL COMMENT '签名方式',
  `sys_service_provider_id` varchar(255) DEFAULT NULL COMMENT '商户号',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='支付宝配置类';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tool_alipay_config`
--

LOCK TABLES `tool_alipay_config` WRITE;
/*!40000 ALTER TABLE `tool_alipay_config` DISABLE KEYS */;
INSERT INTO `tool_alipay_config` VALUES (1,'2016091700532697','utf-8','JSON','https://openapi.alipaydev.com/gateway.do','http://api.auauz.net/api/aliPay/notify','MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC5js8sInU10AJ0cAQ8UMMyXrQ+oHZEkVt5lBwsStmTJ7YikVYgbskx1YYEXTojRsWCb+SH/kDmDU4pK/u91SJ4KFCRMF2411piYuXU/jF96zKrADznYh/zAraqT6hvAIVtQAlMHN53nx16rLzZ/8jDEkaSwT7+HvHiS+7sxSojnu/3oV7BtgISoUNstmSe8WpWHOaWv19xyS+Mce9MY4BfseFhzTICUymUQdd/8hXA28/H6osUfAgsnxAKv7Wil3aJSgaJczWuflYOve0dJ3InZkhw5Cvr0atwpk8YKBQjy5CdkoHqvkOcIB+cYHXJKzOE5tqU7inSwVbHzOLQ3XbnAgMBAAECggEAVJp5eT0Ixg1eYSqFs9568WdetUNCSUchNxDBu6wxAbhUgfRUGZuJnnAll63OCTGGck+EGkFh48JjRcBpGoeoHLL88QXlZZbC/iLrea6gcDIhuvfzzOffe1RcZtDFEj9hlotg8dQj1tS0gy9pN9g4+EBH7zeu+fyv+qb2e/v1l6FkISXUjpkD7RLQr3ykjiiEw9BpeKb7j5s7Kdx1NNIzhkcQKNqlk8JrTGDNInbDM6inZfwwIO2R1DHinwdfKWkvOTODTYa2MoAvVMFT9Bec9FbLpoWp7ogv1JMV9svgrcF9XLzANZ/OQvkbe9TV9GWYvIbxN6qwQioKCWO4GPnCAQKBgQDgW5MgfhX8yjXqoaUy/d1VjI8dHeIyw8d+OBAYwaxRSlCfyQ+tieWcR2HdTzPca0T0GkWcKZm0ei5xRURgxt4DUDLXNh26HG0qObbtLJdu/AuBUuCqgOiLqJ2f1uIbrz6OZUHns+bT/jGW2Ws8+C13zTCZkZt9CaQsrp3QOGDx5wKBgQDTul39hp3ZPwGNFeZdkGoUoViOSd5Lhowd5wYMGAEXWRLlU8z+smT5v0POz9JnIbCRchIY2FAPKRdVTICzmPk2EPJFxYTcwaNbVqL6lN7J2IlXXMiit5QbiLauo55w7plwV6LQmKm9KV7JsZs5XwqF7CEovI7GevFzyD3w+uizAQKBgC3LY1eRhOlpWOIAhpjG6qOoohmeXOphvdmMlfSHq6WYFqbWwmV4rS5d/6LNpNdL6fItXqIGd8I34jzql49taCmi+A2nlR/E559j0mvM20gjGDIYeZUz5MOE8k+K6/IcrhcgofgqZ2ZED1ksHdB/E8DNWCswZl16V1FrfvjeWSNnAoGAMrBplCrIW5xz+J0Hm9rZKrs+AkK5D4fUv8vxbK/KgxZ2KaUYbNm0xv39c+PZUYuFRCz1HDGdaSPDTE6WeWjkMQd5mS6ikl9hhpqFRkyh0d0fdGToO9yLftQKOGE/q3XUEktI1XvXF0xyPwNgUCnq0QkpHyGVZPtGFxwXiDvpvgECgYA5PoB+nY8iDiRaJNko9w0hL4AeKogwf+4TbCw+KWVEn6jhuJa4LFTdSqp89PktQaoVpwv92el/AhYjWOl/jVCm122f9b7GyoelbjMNolToDwe5pF5RnSpEuDdLy9MfE8LnE3PlbE7E5BipQ3UjSebkgNboLHH/lNZA5qvEtvbfvQ==','MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAut9evKRuHJ/2QNfDlLwvN/S8l9hRAgPbb0u61bm4AtzaTGsLeMtScetxTWJnVvAVpMS9luhEJjt+Sbk5TNLArsgzzwARgaTKOLMT1TvWAK5EbHyI+eSrc3s7Awe1VYGwcubRFWDm16eQLv0k7iqiw+4mweHSz/wWyvBJVgwLoQ02btVtAQErCfSJCOmt0Q/oJQjj08YNRV4EKzB19+f5A+HQVAKy72dSybTzAK+3FPtTtNen/+b5wGeat7c32dhYHnGorPkPeXLtsqqUTp1su5fMfd4lElNdZaoCI7osZxWWUo17vBCZnyeXc9fk0qwD9mK6yRAxNbrY72Xx5VqIqwIDAQAB','http://api.auauz.net/api/aliPay/return','RSA2','2088102176044281');
/*!40000 ALTER TABLE `tool_alipay_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tool_email_config`
--

DROP TABLE IF EXISTS `tool_email_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tool_email_config` (
  `config_id` bigint NOT NULL COMMENT 'ID',
  `from_user` varchar(255) DEFAULT NULL COMMENT '收件人',
  `host` varchar(255) DEFAULT NULL COMMENT '邮件服务器SMTP地址',
  `pass` varchar(255) DEFAULT NULL COMMENT '密码',
  `port` varchar(255) DEFAULT NULL COMMENT '端口',
  `user` varchar(255) DEFAULT NULL COMMENT '发件者用户名',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='邮箱配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tool_email_config`
--

LOCK TABLES `tool_email_config` WRITE;
/*!40000 ALTER TABLE `tool_email_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `tool_email_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tool_local_storage`
--

DROP TABLE IF EXISTS `tool_local_storage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tool_local_storage` (
  `storage_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `real_name` varchar(255) DEFAULT NULL COMMENT '文件真实的名称',
  `name` varchar(255) DEFAULT NULL COMMENT '文件名',
  `suffix` varchar(255) DEFAULT NULL COMMENT '后缀',
  `root_path` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL COMMENT '路径',
  `type` varchar(255) DEFAULT NULL COMMENT '类型',
  `size` varchar(255) DEFAULT NULL COMMENT '大小',
  `status` varchar(255) DEFAULT NULL,
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `time` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`storage_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='本地存储';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tool_local_storage`
--

LOCK TABLES `tool_local_storage` WRITE;
/*!40000 ALTER TABLE `tool_local_storage` DISABLE KEYS */;
/*!40000 ALTER TABLE `tool_local_storage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tool_qiniu_config`
--

DROP TABLE IF EXISTS `tool_qiniu_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tool_qiniu_config` (
  `config_id` bigint NOT NULL COMMENT 'ID',
  `access_key` text COMMENT 'accessKey',
  `bucket` varchar(255) DEFAULT NULL COMMENT 'Bucket 识别符',
  `host` varchar(255) NOT NULL COMMENT '外链域名',
  `secret_key` text COMMENT 'secretKey',
  `type` varchar(255) DEFAULT NULL COMMENT '空间类型',
  `zone` varchar(255) DEFAULT NULL COMMENT '机房',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='七牛云配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tool_qiniu_config`
--

LOCK TABLES `tool_qiniu_config` WRITE;
/*!40000 ALTER TABLE `tool_qiniu_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `tool_qiniu_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tool_qiniu_content`
--

DROP TABLE IF EXISTS `tool_qiniu_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tool_qiniu_content` (
  `content_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `bucket` varchar(255) DEFAULT NULL COMMENT 'Bucket 识别符',
  `name` varchar(255) DEFAULT NULL COMMENT '文件名称',
  `size` varchar(255) DEFAULT NULL COMMENT '文件大小',
  `type` varchar(255) DEFAULT NULL COMMENT '文件类型：私有或公开',
  `url` varchar(255) DEFAULT NULL COMMENT '文件url',
  `suffix` varchar(255) DEFAULT NULL COMMENT '文件后缀',
  `update_time` datetime DEFAULT NULL COMMENT '上传或同步的时间',
  PRIMARY KEY (`content_id`) USING BTREE,
  UNIQUE KEY `uniq_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='七牛云文件存储';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tool_qiniu_content`
--

LOCK TABLES `tool_qiniu_content` WRITE;
/*!40000 ALTER TABLE `tool_qiniu_content` DISABLE KEYS */;
/*!40000 ALTER TABLE `tool_qiniu_content` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-07-06 16:54:28
