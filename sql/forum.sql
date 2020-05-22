/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.5.62 : Database - forum
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`forum` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `forum`;

/*Table structure for table `tb_admin_login_info` */

DROP TABLE IF EXISTS `tb_admin_login_info`;

CREATE TABLE `tb_admin_login_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `ip` varchar(255) NOT NULL COMMENT 'ip地址信息',
  `info` varchar(255) NOT NULL COMMENT '获取得到的地址信息',
  `time` varchar(255) NOT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`,`ip`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_categories` */

DROP TABLE IF EXISTS `tb_categories`;

CREATE TABLE `tb_categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `category` varchar(256) NOT NULL COMMENT '分类名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `tb_categories` */

insert  into `tb_categories`(`id`,`category`) values (1,'开发语言');
insert  into `tb_categories`(`id`,`category`) values (2,'平台框架');
insert  into `tb_categories`(`id`,`category`) values (3,'服务器');
insert  into `tb_categories`(`id`,`category`) values (4,'数据库');
insert  into `tb_categories`(`id`,`category`) values (5,'开发工具');
insert  into `tb_categories`(`id`,`category`) values (6,'其它');

/*Table structure for table `tb_category_tag` */

DROP TABLE IF EXISTS `tb_category_tag`;

CREATE TABLE `tb_category_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `category_id` int(11) NOT NULL COMMENT '分类ID',
  `tag_id` int(11) DEFAULT NULL COMMENT '标签ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

/*Data for the table `tb_category_tag` */

insert  into `tb_category_tag`(`id`,`category_id`,`tag_id`) values (1,1,1);
insert  into `tb_category_tag`(`id`,`category_id`,`tag_id`) values (2,1,2);
insert  into `tb_category_tag`(`id`,`category_id`,`tag_id`) values (3,1,3);
insert  into `tb_category_tag`(`id`,`category_id`,`tag_id`) values (4,3,4);
insert  into `tb_category_tag`(`id`,`category_id`,`tag_id`) values (5,4,5);
insert  into `tb_category_tag`(`id`,`category_id`,`tag_id`) values (6,2,6);
insert  into `tb_category_tag`(`id`,`category_id`,`tag_id`) values (7,2,7);
insert  into `tb_category_tag`(`id`,`category_id`,`tag_id`) values (8,5,9);
insert  into `tb_category_tag`(`id`,`category_id`,`tag_id`) values (9,4,8);
insert  into `tb_category_tag`(`id`,`category_id`,`tag_id`) values (10,6,10);

/*Table structure for table `tb_collection` */

DROP TABLE IF EXISTS `tb_collection`;

CREATE TABLE `tb_collection` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `u_id` int(11) NOT NULL COMMENT '收藏者Id',
  `question_id` int(11) NOT NULL COMMENT '收藏的问题ID',
  `publisher_id` int(11) NOT NULL COMMENT '发帖者ID',
  `time` varchar(255) NOT NULL COMMENT '收藏时间',
  `status` int(2) NOT NULL COMMENT '收藏状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;


/*Table structure for table `tb_comment` */

DROP TABLE IF EXISTS `tb_comment`;

CREATE TABLE `tb_comment` (
  `c_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `comment` text NOT NULL COMMENT '评论内容',
  `time` varchar(255) NOT NULL COMMENT '评论时间',
  `uid` int(11) NOT NULL COMMENT '评论者ID',
  `question_id` int(11) NOT NULL COMMENT '评论对应的问题ID',
  PRIMARY KEY (`c_id`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8;


/*Table structure for table `tb_comment_like` */

DROP TABLE IF EXISTS `tb_comment_like`;

CREATE TABLE `tb_comment_like` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `comment_id` int(11) NOT NULL COMMENT '评论ID',
  `comment_uid` int(11) NOT NULL COMMENT '评论者ID',
  `like_id` int(11) NOT NULL COMMENT '点赞者ID',
  `time` varchar(255) NOT NULL COMMENT '点赞时间',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '点赞状态 0-未点赞 1-已点赞',
  `question_id` int(11) NOT NULL COMMENT '关联的问题ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;



/*Table structure for table `tb_comment_notice` */

DROP TABLE IF EXISTS `tb_comment_notice`;

CREATE TABLE `tb_comment_notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `parent_comment_id` int(11) NOT NULL COMMENT '父评论ID',
  `comment_id` int(11) NOT NULL COMMENT '回复者ID',
  `notice_id` int(11) NOT NULL COMMENT '被回复者ID',
  `content` text NOT NULL COMMENT '回复内容',
  `time` varchar(255) NOT NULL COMMENT '回复时间',
  `question_id` int(11) NOT NULL COMMENT '关联的问题ID',
  `status` int(2) NOT NULL DEFAULT '0' COMMENT '是否被阅读过，0-未读，1-已读',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_comment_reply` */

DROP TABLE IF EXISTS `tb_comment_reply`;

CREATE TABLE `tb_comment_reply` (
  `r_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评论回复ID',
  `r_content` text,
  `r_time` varchar(255) CHARACTER SET latin1 NOT NULL COMMENT '回复时间',
  `parent_comment_id` int(11) NOT NULL COMMENT '父评论ID',
  `touid` int(11) NOT NULL COMMENT '目标用户id，回复给谁',
  `r_uid` int(11) NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`r_id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_email` */

DROP TABLE IF EXISTS `tb_email`;

CREATE TABLE `tb_email` (
  `e_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `u_id` int(11) NOT NULL COMMENT '对应的用户ID',
  `email` varchar(255) DEFAULT NULL COMMENT '对应的用户邮箱',
  `check` int(11) NOT NULL DEFAULT '0' COMMENT '是否激活邮箱，0-未验证 1-已验证',
  PRIMARY KEY (`e_id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;


/*Table structure for table `tb_follow` */

DROP TABLE IF EXISTS `tb_follow`;

CREATE TABLE `tb_follow` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` int(11) NOT NULL COMMENT '被关注者的用户ID',
  `follow_id` int(11) NOT NULL COMMENT '点击关注者的ID',
  `time` varchar(255) NOT NULL COMMENT '关注时间',
  `status` int(2) NOT NULL COMMENT '关注状态 0-未关注 1-已关注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;


/*Table structure for table `tb_question` */

DROP TABLE IF EXISTS `tb_question`;

CREATE TABLE `tb_question` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '问题id',
  `title` varchar(50) NOT NULL COMMENT '标题',
  `description` longtext NOT NULL COMMENT '问题补充内容',
  `gmt_create` varchar(255) NOT NULL COMMENT '发帖时间',
  `gmt_modified` varchar(255) NOT NULL COMMENT '修改时间',
  `publisher_id` int(11) NOT NULL COMMENT '发帖用户ID',
  `comment_count` int(11) NOT NULL DEFAULT '0' COMMENT '评论数',
  `view_count` int(11) NOT NULL DEFAULT '0' COMMENT '阅读数',
  `like_count` int(11) NOT NULL DEFAULT '0' COMMENT '点赞数',
  `tag` varchar(255) DEFAULT NULL COMMENT '问题标签',
  `is_ding` int(1) NOT NULL DEFAULT '0' COMMENT '0-未置顶 1-置顶',
  `is_jing` int(1) NOT NULL DEFAULT '0' COMMENT '0-未加精 1-加精',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '0-正常 1-删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8;


/*Table structure for table `tb_question_report` */

DROP TABLE IF EXISTS `tb_question_report`;

CREATE TABLE `tb_question_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` int(11) NOT NULL COMMENT '举报用户ID',
  `r_user_id` int(11) NOT NULL COMMENT '被举报用户ID',
  `r_question_id` int(11) NOT NULL COMMENT '被举报问题ID',
  `report_reason` varchar(255) NOT NULL COMMENT '举报理由',
  `is_process` int(1) NOT NULL COMMENT '是否已处理，0-否，1-是',
  `process_result` varchar(255) NOT NULL COMMENT '处理结果判决',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_question_tag` */

DROP TABLE IF EXISTS `tb_question_tag`;

CREATE TABLE `tb_question_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `tag_id` int(11) NOT NULL COMMENT '标签对应的ID',
  `question_id` int(11) DEFAULT NULL COMMENT '问题对应的ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8;


/*Table structure for table `tb_tags` */

DROP TABLE IF EXISTS `tb_tags`;

CREATE TABLE `tb_tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `tags_name` varchar(256) NOT NULL COMMENT '标签名',
  `category_id` int(11) NOT NULL COMMENT '对应的分类ID',
  `is_origin_tag` int(1) NOT NULL COMMENT '是否为默认标签',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8;


/*Table structure for table `tb_user` */

DROP TABLE IF EXISTS `tb_user`;

CREATE TABLE `tb_user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) CHARACTER SET latin1 NOT NULL COMMENT '用户密码',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=utf8;


/*Table structure for table `tb_user_info` */

DROP TABLE IF EXISTS `tb_user_info`;

CREATE TABLE `tb_user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `u_id` int(11) NOT NULL COMMENT '用户ID',
  `login_name` varchar(50) NOT NULL COMMENT '登录用户名，不可更改',
  `username` varchar(50) NOT NULL COMMENT '昵称，可更改',
  `sex` varchar(5) DEFAULT '1' COMMENT '性别 1-男 2-女',
  `birthday` varchar(255) DEFAULT '1970-01-01' COMMENT '生日',
  `email` varchar(255) CHARACTER SET latin1 DEFAULT NULL COMMENT '邮箱',
  `area` varchar(50) DEFAULT NULL COMMENT '所在地',
  `hobby` varchar(50) DEFAULT NULL COMMENT '兴趣爱好',
  `comment` varchar(50) DEFAULT NULL COMMENT '个人简介',
  `avatar` varchar(255) CHARACTER SET latin1 DEFAULT NULL COMMENT '用户头像',
  `site` varchar(255) DEFAULT NULL COMMENT '个人网站',
  `github` varchar(255) DEFAULT NULL COMMENT 'github',
  `weibo` varchar(255) DEFAULT NULL COMMENT '微博链接',
  `university` varchar(255) DEFAULT NULL COMMENT '毕业学校',
  `majority` varchar(255) DEFAULT NULL COMMENT '所学专业',
  `company` varchar(255) DEFAULT NULL COMMENT '公司名称',
  `job_title` varchar(255) DEFAULT NULL COMMENT '公司职位',
  `register_date` varchar(255) DEFAULT NULL COMMENT '注册时间',
  `isOpen` varchar(10) NOT NULL DEFAULT '1' COMMENT '是否公开信息，0-不公开，1-公开',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;


/*Table structure for table `tb_user_rate` */

DROP TABLE IF EXISTS `tb_user_rate`;

CREATE TABLE `tb_user_rate` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `rate` int(11) NOT NULL DEFAULT '0' COMMENT '用户积分',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
