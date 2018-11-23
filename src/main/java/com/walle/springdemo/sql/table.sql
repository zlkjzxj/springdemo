#数据库设计

CREATE TABLE `miaosha_user` (
  id              BIGINT(20)   NOT NULL
  COMMENT '用户id,手机号码',
  nickname        VARCHAR(255) NOT NULL,
  PASSWORD        VARCHAR(32)  DEFAULT NULL
  COMMENT 'md5(md5(password明文+固定salt)+salt)',
  salt            VARCHAR(10)  DEFAULT NULL,
  head            VARCHAR(128) DEFAULT NULL
  COMMENT '头像,云存储的id',
  register_date   DATETIME     DEFAULT NULL
  COMMENT '注册时间',
  last_login_date DATETIME     DEFAULT NULL
  COMMENT '上次登录时间',
  login_count     INT(11)      DEFAULT '0'
  COMMENT '登录次数',
  PRIMARY KEY (id)
)
  ENGINE = INNODB
  DEFAULT CHARSET = utf8;

# 商品表
CREATE TABLE goods (
  id           BIGINT(20)                    NOT NULL AUTO_INCREMENT
  COMMENT '商品ID',
  goods_name   VARCHAR(16)                   NOT NULL DEFAULT ''
  COMMENT '商品名称',
  goods_title  VARCHAR(64)                   NOT NULL DEFAULT ''
  COMMENT '商品标题',
  goods_img    LONGTEXT COMMENT '商品图片',
  goods_detail LONGTEXT COMMENT '商品详情介绍',
  goods_price  DECIMAL(10, 2) DEFAULT '0.00' NULL
  COMMENT '商品单价',
  goods_stock  INT(11)                                DEFAULT '0'
  COMMENT '商品库存，-1表示没有限制',
  PRIMARY KEY (id)
)
  ENGINE = INNODB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8;

INSERT INTO goods VALUES (1, 'honor10', '华为（HUAWEI） honor/荣耀V10 智能手机 官方旗舰店 炫影蓝 全网通6GB ', '/img/honor.png', '华为（HUAWEI） honor/荣耀V10 智能手机 官方旗舰店 幻夜黑 全网通8GB RAM+128GB ROM
【返400元话费卡或加油卡】部分可立减50元 【精选服务】送价值208元碎屏保1年延保服务 【点购买荣耀10享200红包】【双11买手机选头', 2700, -1);

#秒杀商品表
CREATE TABLE miaosha_goods (
  id            BIGINT(20)                    NOT NULL AUTO_INCREMENT
  COMMENT '秒杀商品ID',
  goods_id      BIGINT(20)                    NOT NULL
  COMMENT '商品id',
  miaosha_price DECIMAL(10, 2) DEFAULT '0.00' NULL
  COMMENT '秒杀价',
  stock_count   INT(11)                                DEFAULT NULL
  COMMENT '库存数量',
  start_date    DATETIME                               DEFAULT NULL
  COMMENT '秒杀开始时间',
  end_date      DATETIME                               DEFAULT NULL
  COMMENT '秒杀结束时间',
  PRIMARY KEY (id)
)
  ENGINE = INNODB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8;

INSERT INTO miaosha_goods VALUES (1, 1, 1800, 10, '2018-11-16 12:00:00', '2018-11-16 12:01:00');
#订单表
CREATE TABLE order_info (
  id               BIGINT(20) NOT NULL AUTO_INCREMENT,
  user_id          BIGINT(20) NOT NULL
  COMMENT '用户id',
  goods_id         BIGINT(20) NOT NULL
  COMMENT '商品id',
  delivery_addr_id BIGINT(20)
  COMMENT '收货地址id',
  goods_name       VARCHAR(16)         DEFAULT NULL
  COMMENT '冗余过来的商品名称',
  goods_count      INT(11)             DEFAULT '0'
  COMMENT '商品数量',
  goods_price      DECIMAL(10, 2)      DEFAULT '0.00'
  COMMENT '商品单价',
  order_channel    TINYINT(4)          DEFAULT '0'
  COMMENT '订单渠道： 1:pc,2:android,3.ios',
  status           TINYINT(4)          DEFAULT '0'
  COMMENT '订单状态： 0:新建未支付，1；已支付，2：已发货，3：已收货，4：已退款，5：已完成',
  create_date      DATETIME            DEFAULT NULL
  COMMENT '订单创建时间',
  pay_date         DATETIME            DEFAULT NULL
  COMMENT '订单支付时间',

  PRIMARY KEY (id)
)
  ENGINE = INNODB
  AUTO_INCREMENT = 12
  DEFAULT CHARSET = utf8;

#秒杀订单表
CREATE TABLE miaosha_order (
  id       BIGINT(20) NOT NULL AUTO_INCREMENT,
  user_id  BIGINT(20) NOT NULL
  COMMENT '用户ID',
  order_id BIGINT(20) NOT NULL
  COMMENT '订单ID',
  goods_id BIGINT(20) NOT NULL
  COMMENT '商品ID',
  PRIMARY KEY (id)
)
  ENGINE = INNODB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8;