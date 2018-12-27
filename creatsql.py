import MySQLdb

# 打开数据库连接
db = MySQLdb.connect("localhost", "iknow", "Eephu7ch", "iknow", charset='utf8')

# 使用cursor()方法获取操作游标
cursor = db.cursor()

# 如果数据表已经存在使用 execute() 方法删除表。
cursor.execute("DROP TABLE IF EXISTS USER")
cursor.execute("DROP TABLE IF EXISTS ACTIVITY")
cursor.execute("DROP TABLE IF EXISTS CALENDAR")

# 创建User表的SQL语句
# USER_ID -- 用户ID，自增，主键
# USER_NAME -- 用户名，不可重复，注册时填写后不可更改，必填
# PASSWORD -- 密码，不可长于16位，必填
# DEPARTMENT -- 用户院系
# TAG -- 至少一个
sql_creatUser = """CREATE TABLE USER (
         USER_ID INT(20) PRIMARY KEY NOT NULL,
         USER_NAME  VARCHAR(20) NOT NULL,
         PASSWORD VARCHAR(16)  NOT NULL,
         DEPARTMENT VARCHAR(15),
         TAG1 VARCHAR(20) NOT NULL,
         TAG2 VARCHAR(20),
         TAG3 VARCHAR(20),
         TAG4 VARCHAR(20),
         TAG5 VARCHAR(20),
         TAG6 VARCHAR(20),
         TAG7 VARCHAR(20),
         TAG8 VARCHAR(20),
         TAG9 VARCHAR(20),
         TAG10 VARCHAR(20))"""

# 创建Activity表的SQL语句
# ACTIVITYNO -- 事件编号，主值
# PLACE -- 活动地点，必填
# HOLDER -- 活动主办方，必填
# TAG -- 至少一个
# INSTRUCTION -- 活动简介，必填
# PICTURE -- 活动图片
# URL -- 活动推送链接
sql_creatActivity = """CREATE TABLE ACTIVITY (
        ACTIVITYNO INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        YEAR INT NOT NULL,
        MONTH INT NOT NULL,
        DAY INT NOT NULL,
        START_HOUR INT NOT NULL,
        START_MINUTE INT NOT NULL,
        END_HOUR INT NOT NULL,
        END_MINUTE INT NOT NULL,
        NAME VARCHAR(30) NOT NULL,
        ADDRESS VARCHAR(20) NOT NULL,
        HOLDER VARCHAR(20) NOT NULL,
        TAG1 VARCHAR(20),
        TAG2 VARCHAR(20),
        TAG3 VARCHAR(20),
        INTRODUCTION VARCHAR(100),
        PICTURE VARCHAR(100),
        URL VARCHAR(100))"""

#创建个人日历表
# CALENDARNO -- 个人日历编号，主值
# ACTIVITYNO -- 事件编号，非空
# USER_NAME -- 用户名，非空
sql_creatPersonalCalendar = """CREATE TABLE CALENDAR (
        CALENDARNO INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        ACTIVITYNO INT NOT NULL,
        USER_ID INT NOT NULL)"""


cursor.execute(sql_creatUser)
cursor.execute(sql_creatActivity)
cursor.execute(sql_creatPersonalCalendar)

# 关闭数据库连接
db.close()
