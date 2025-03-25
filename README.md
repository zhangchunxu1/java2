# java2
# 安装java
# 安装vscode


# 安装mysql
# https://dev.mysql.com/downloads/mysql/
# https://downloads.mysql.com/archives/get/p/23/file/mysql-8.0.20-winx64.zip
# 解压-在解压的文件下-创建my.ini 如：D:\111\mysql-8.0.20-winx64\my.ini
# my.ini 填写的内容如下：注释的内容
# 创建数据的数据在 这个文件下 datadir=D:\111\mysql-8.0.20-winx64\data
<!-- [mysqld]
#ERROR 1045(28000):Access denied for userroot’@’localhost using password: NO) 加skip-grant-tables
#skip-grant-tables  不需要密码登录 记得把#去掉
# 日志文件位置  在MySQL登录时出现 Can‘t connect to MySQL server on ‘localhost‘(10061)拒绝访问错误解决方案 加shared-memory
#shared-memory    连接MySQL的协议  这个没注释只能管理员cmd运行 记得把#去掉
# 设置3306端口
port=3306
# 设置mysql的安装目录
basedir=D:\111\mysql-8.0.20-winx64
# 设置mysql数据库的数据的存放目录
datadir=D:\111\mysql-8.0.20-winx64\data
# 允许最大连接数
max_connections=200
# 允许连接失败的次数。这是为了防止有人从该主机试图攻击数据库系统
max_connect_errors=10
# 服务端使用的字符集默认为UTF8
character-set-server=utf8
# 创建新表时将使用的默认存储引擎
default-storage-engine=INNODB
# 默认使用“mysql_native_password”插件认证
default_authentication_plugin=mysql_native_password
[mysql]
# 设置mysql客户端默认字符集
default-character-set=utf8
[client]
# 设置mysql客户端连接服务端时默认使用的端口
port=3306
default-character-set=utf8 -->
# 配置mysql环境变量
# 我的电脑-属性-高级系统设置-环境变量-系统变量-新建变量名：Path 变量值：D:\111\mysql-8.0.20-winx64\bin
# 启动服务 win+R 输入 services.msc 找到 MySQL 8.0 并启动
# 登录mysql 输入 mysql -u root -p 密码 进入mysql命令行
# 删除注册表 win+R 输入 regedit 找到 计算机\HKEY LOCAL MACHINE\SYSTEM\CurrentControlSetlServices\MysQl 并删除
# 删除服务 sc delete mysql  myql你服务的名称
# 进入服务 修改密码 ALTER USER 'root'@'localhost' IDENTIFIED BY '123456';
# flush privileges; 刷新权限



# 安装mvn 去系统属性→环境变量→系统变量 变量名MAVEN_HOME  变量值D:\maven\apache-maven-3.9.6
# springboot java代的开发环境搭建 需要mvn
# git 控制面板→凭据管理器→window凭据→可修改git初始输入错误的账号密码
# yarn 安装地址 https://mirrors.huaweicloud.com/yarn/
# funalshell 下载地址 https://www.hostbuf.com/ 连接linux服务器软件
# vpn  稳连云.com