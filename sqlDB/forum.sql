/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 80021
Source Host           : localhost:3306
Source Database       : forum

Target Server Type    : MYSQL
Target Server Version : 80021
File Encoding         : 65001

Date: 2024-12-13 11:29:46
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for forumuser
-- ----------------------------
DROP TABLE IF EXISTS `forumuser`;
CREATE TABLE `forumuser` (
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` char(255) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of forumuser
-- ----------------------------
INSERT INTO `forumuser` VALUES ('admin', 'admin11');
INSERT INTO `forumuser` VALUES ('bluemoon', 'moon11');
INSERT INTO `forumuser` VALUES ('jqx', 'woshijqx');
INSERT INTO `forumuser` VALUES ('xiaoming', '121212');
INSERT INTO `forumuser` VALUES ('老王', 'laowang12');
INSERT INTO `forumuser` VALUES ('阿芳', '789456');

-- ----------------------------
-- Table structure for index
-- ----------------------------
DROP TABLE IF EXISTS `index`;
CREATE TABLE `index` (
  `num` int DEFAULT NULL,
  `title` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `username` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of index
-- ----------------------------
INSERT INTO `index` VALUES ('1', '青春校园，活力绽放——校园生活的多彩瞬间', '阿芳', '2');
INSERT INTO `index` VALUES ('2', '学术之舟，扬帆起航——探索校园知识的海洋', '阿芳', '2');
INSERT INTO `index` VALUES ('3', '社团风采，魅力无限——展现校园社团的独特魅力', 'jqx', '4');

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `num` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `sn` int DEFAULT '0',
  `title` varchar(255) DEFAULT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci,
  `reply` int DEFAULT '0',
  PRIMARY KEY (`num`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of post
-- ----------------------------
INSERT INTO `post` VALUES ('1', '阿芳', '1', '青春校园，活力绽放——校园生活的多彩瞬间', '校园青春节目马上就要开始啦，欢迎各位同学踊跃报名哦', '0');
INSERT INTO `post` VALUES ('2', '阿芳', '0', '学术之舟，扬帆起航——探索校园知识的海洋', '欢迎其他学校的来宾前来本校莅临指导', '0');
INSERT INTO `post` VALUES ('4', '老王', '1', '集贤阁的饭好难吃', '怎么会那么难吃啊啊啊啊', '0');
INSERT INTO `post` VALUES ('5', 'jqx', '0', '社团风采，魅力无限——展现校园社团的独特魅力', '欢迎各位校友加入丰富多彩的校园社团活动', '0');
INSERT INTO `post` VALUES ('7', 'xiaoming', '0', '我是小明', '我叫小明，很高兴认识你们', '0');
INSERT INTO `post` VALUES ('8', 'xiaoming', '0', '有没有同学有英语四级资料', '急需，有的同学联系一下我', '0');
INSERT INTO `post` VALUES ('9', 'admin', '1', '校园论坛守则', '该论坛内所有内容发布禁止人身攻击，禁止任何色情、赌博、政治话题，禁止挑动矛盾，禁止发布谣言、恶意泄露他人的身份信息等其他各种不当行为', null);

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
  `r_id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `r_num` int DEFAULT '0',
  `t_num` int DEFAULT '0',
  PRIMARY KEY (`r_id`),
  KEY `r_num` (`r_num`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of review
-- ----------------------------
INSERT INTO `review` VALUES ('1', '集贤阁太好吃了', '超级飞侠', '4', null);
INSERT INTO `review` VALUES ('2', '青春在哪', '超级飞侠', '1', null);
INSERT INTO `review` VALUES ('3', '不学不练', '超级单词侠', '2', null);
INSERT INTO `review` VALUES ('4', '懒得去社团', '深夜激情哥', '5', null);
INSERT INTO `review` VALUES ('5', '青春在这里', '飞天小女警', '1', null);
INSERT INTO `review` VALUES ('15', '我们是青春的代言人', 'xiaoming', '1', null);
INSERT INTO `review` VALUES ('16', '你好小明', '超级飞侠', '14', null);
INSERT INTO `review` VALUES ('17', '报名有什么条件吗', '快乐小狗', '1', '0');
INSERT INTO `review` VALUES ('18', '希望推出穷鬼套餐', 'xiaoming', '1', '0');
INSERT INTO `review` VALUES ('19', '面食我觉得可以', 'xiaoming', '4', '0');
INSERT INTO `review` VALUES ('20', '我也觉得难吃', 'lqx', '4', '0');
INSERT INTO `review` VALUES ('21', '希望学校能够改善一下', '老王', '4', '0');
INSERT INTO `review` VALUES ('22', '可以多人参赛吗', 'xiaoming', '1', '0');
INSERT INTO `review` VALUES ('23', '能吃就行', 'lqx', '4', '0');
INSERT INTO `review` VALUES ('27', '我想和我的小伙伴一起参赛', 'xiaoming', '1', '0');
INSERT INTO `review` VALUES ('28', '我才是小明', 'xiaoming', '6', '0');
INSERT INTO `review` VALUES ('29', '晚上去吃吃看', 'xiaoming', '4', '0');
INSERT INTO `review` VALUES ('30', '顶帖顶帖顶贴', 'xiaoming', '1', '0');
INSERT INTO `review` VALUES ('31', '薯条吃多了好腻啊啊啊', 'xiaoming', '0', '1');
INSERT INTO `review` VALUES ('32', '欢迎各位前来指导', 'xiaoming', '2', '0');
INSERT INTO `review` VALUES ('33', '推荐大家参加电影社团，有好多好玩的活动', 'jqx', '5', '0');
INSERT INTO `review` VALUES ('34', '还推荐音乐社团，里面的学长学姐免费教学，想学什么都可以教', 'jqx', '5', '0');
INSERT INTO `review` VALUES ('35', '我也收集了很多马克杯和咖啡杯', 'xiaoming', '0', '2');
INSERT INTO `review` VALUES ('36', '爆炸肌肉会不会太夸张', 'xiaoming', '0', '3');
INSERT INTO `review` VALUES ('37', '怎么知道的', 'xiaoming', '0', '4');
INSERT INTO `review` VALUES ('38', '我兼职的的时候看见老板挑不新鲜的进货', 'xiaoming', '0', '4');
INSERT INTO `review` VALUES ('39', '你是怎么知道的', 'xiaoming', '0', '5');
INSERT INTO `review` VALUES ('40', '我觉得还行', 'xiaoming', '4', '0');
INSERT INTO `review` VALUES ('41', '诚挚欢迎您，贵校的老师，与我们共同分享教育智慧。', 'xiaoming', '2', '0');
INSERT INTO `review` VALUES ('42', 'beizhi', 'xiaoming', '0', '2');
INSERT INTO `review` VALUES ('43', '有没有喜欢打球的朋友', 'xiaoming', '1', '0');
INSERT INTO `review` VALUES ('44', '想去篮球社', 'xiaoming', '5', '0');

-- ----------------------------
-- Table structure for secret
-- ----------------------------
DROP TABLE IF EXISTS `secret`;
CREATE TABLE `secret` (
  `num` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`num`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of secret
-- ----------------------------
INSERT INTO `secret` VALUES ('1', '我爱吃薯条', '我能吃一辈子薯条', '老王');
INSERT INTO `secret` VALUES ('2', '马克杯和咖啡杯', '各种各样能见到的杯子都收藏了', 'lqx');
INSERT INTO `secret` VALUES ('3', '健身哥', '练出无敌爆炸爆炸肌肉', 'bluemoon');
INSERT INTO `secret` VALUES ('5', '水果店', '水果店的水果不新鲜', null);
INSERT INTO `secret` VALUES ('6', 'zhong', 'wu', null);
