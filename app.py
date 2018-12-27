from flask import Flask
from flask import render_template, redirect, url_for, request, session
from functools import wraps
from datetime import datetime
from sqlalchemy import or_, and_
from werkzeug.security import generate_password_hash, check_password_hash  # 密码保护，使用hash方法
from flask_sqlalchemy import SQLAlchemy
import json

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://iknow:Eephu7ch@127.0.0.1/iknow'
db = SQLAlchemy(app)


#
class User(db.Model):
    __tablename__ = 'USER'
    id = db.Column('USER_ID',db.Integer, primary_key=True, autoincrement=True)
    username = db.Column('USER_NAME',db.String(20), nullable=False)
    password = db.Column('PASSWORD',db.String(128), nullable=False)  # 内部使用
    department = db.Column('DEPARTMENT',db.String(15), nullable=False)
    tag1 = db.Column('TAG1',db.String(20))
    tag2 = db.Column('TAG2',db.String(20))
    tag3 = db.Column('TAG3',db.String(20))
    tag4 = db.Column('TAG4',db.String(20))
    tag5 = db.Column('TAG5',db.String(20))
    tag6 = db.Column('TAG6',db.String(20))
    tag7 = db.Column('TAG7',db.String(20))
    tag8 = db.Column('TAG8',db.String(20))
    tag9 = db.Column('TAG9',db.String(20))
    tag10 = db.Column('TAG10',db.String(20))

    #initilization of User
    def __init__(self, username, password, nickname, department):
        self.username = username
        self.department = department
        self.nickname = nickname
        self.set_password(password)

    #password encrypting
    def set_password(self, password):
        self.password = generate_password_hash(password)

    #check password encrypting
    def check_password(self, password):
        result = check_password_hash(self.password, password)
        return result

#
class Activity(db.Model):
    __tablename__ = 'ACTIVITY'
    id = db.Column('ACTIVITYNO',db.Integer, primary_key=True, autoincrement=True)
    year = db.Column('YEAR',db.Integer)
    month = db.Column('MONTH',db.Integer)
    day = db.Column('DAY',db.Integer)
    start_hour = db.Column('START_HOUR',db.Integer)
    start_minute = db.Column('START_MINUTE',db.Integer)
    end_hour = db.Column('END_HOUR',db.Integer)
    end_minute = db.Column('END_MINUTE',db.Integer)
    name = db.Column('NAME',db.String(30),nullable=False)
    address = db.Column('ADDRESS',db.String(20),nullable=False)
    holder = db.Column('HOLDER',db.String(20),nullable=False)
    department = db.Column('DEPARTMENT',db.String(15),nullable=False)
    tag1 = db.Column('TAG1',db.String(20))
    tag2 = db.Column('TAG2',db.String(20))
    tag3 = db.Column('TAG3',db.String(20))
    introduction = db.Column('INTRODUCTION',db.String(100))
    picture = db.Column('PICTURE',db.String(100))
    url = db.Column('URL',db.String(100))

#
class Calendar(db.Model):
    __tablename__ = 'CALENDAR'
    id = db.Column('CALENDARNO',db.Integer, primary_key=True, autoincrement=True)
    activityno = db.Column('ACTIVITYNO',db.Integer, db.ForeignKey('ACTIVITY.ACTIVITYNO'))
    userid = db.Column('USER_ID',db.Integer, db.ForeignKey('USER.USER_ID'))

# register
@app.route('/updateData/addNewUser', methods=['GET', 'POST'])
def register():
    if request.method == 'GET':
        return 'wrong method'
    else:
        data = request.get_data()
        data = json.loads(data)
        username = data['Username']
        password = data['Password']
        nickname = data['Nickname']
        department = data['Department']

        print()
        print('新的用户注册:')
        print('Username: ' + username + '  Password: ' + password)
        print('Nickname: ' + nickname + '  Department: ' + department)

        user = User.query.filter(User.username == username).first()

        if user:
            print('用户已经存在,注册失败。')
            return 'username existed'
        else:
            user = User(username=username, password=password, nickname=nickname, department=department)
            db.session.add(user)#add new user to the database
            db.session.commit()
            print('用户注册成功。')
            return 'register succeed'

# add new activity
@app.route('/updateData/addActivity', methods=['GET', 'POST'])
def newac():
    if request.method == 'GET':
        return 'wrong method'
    else:
        data = request.get_data()
        data = json.loads(data)
        name = data['Name']
        year = data['Year']
        month = data['Month']
        day = data['Day']
        start_hour = data['StartHour']
        start_minute = data['StartMin']
        end_hour = data['EndHour']
        end_minute = data['EndMin']
        address = data['Address']
        holder = data['Holder']
        department = data['Department']
        introduction = data['Introduction']
        tag1 = data['MainLabel']
        tag2 = data['SecondLabel']
        if tag2 == '-': tag2 = '无'
        tag3 = data['ThemeLabel']
        if tag3 == '-': tag3 = '无'
        url = data['Url']
        activity = Activity(year=year,month=month,day=day,start_hour=start_hour,department=department,\
        start_minute=start_minute, end_hour=end_hour,end_minute=end_minute,name=name,
        address=address,holder=holder,introduction=introduction,tag1=tag1,tag2=tag2,tag3=tag3,url=url)
        db.session.add(activity)  # add new activity to databse
        db.session.commit()

        #print detail
        print()
        print('新的活动添加:')
        print('Name: '+name)
        print('Date: '+ str(year) + str(month).zfill(2) + str(day).zfill(2) + '  Time: ' + str(start_hour).zfill(2)+ ':' + str(start_minute).zfill(2) + ' - ' + str(end_hour).zfill(2) + ':' + str(end_minute).zfill(2))
        print('Holder: '+holder+'  Department: '+department)
        print('Tag: ' + tag1 + ' ' + tag2 + ' ' + tag3)
        print('Introduction: ' + introduction)
        print('Url: '+url)


        return 'activity add succeed'  # 重定向到登录页


# change user tag
@app.route('/updateData/changeTag', methods=['GET', 'POST'])
def changetag():
    if request.method == 'GET':
        return 'wrong method'# 重定向到注册页
    else:
        data = request.get_data()
        data = json.loads(data)
        username = data['Username']
        tag = ['0']*16
        for i in range(0,15):
            tag[i]=data['Tag'+str(i+1)]

        db.session.query(User).filter(User.username == username).update({"tag1" : tag[0], "tag2" : tag[1], "tag3" : tag[2], "tag4" : tag[3], "tag5" : tag[4], "tag6" : tag[5], "tag7" : tag[6], "tag8" : tag[7], "tag9" : tag[8], "tag10" : tag[9]})
        db.session.commit()

        print()
        print('用户'+username+'修改了标签！')

        text = ''
        for a in range(0,16):
            if tag[a] != '0': text  = text + tag[a] + '、'

        if text =='':
            print('用户现在无标签。')
        else:
            text = text[0:-1]
            print('用户' + username + '现有标签是' + text)
        return 'change tag succeed'  # 登陆成功

# get user activity (+listening)
@app.route('/downloadData/getPrivateActivity', methods=['GET', 'POST'])
def getPrivateAc():
    if request.method == 'GET':
        return 'Failed'
    else:
        data = request.get_data()
        data = json.loads(data)
        username = data['Username']

        user = User.query.filter(User.username == username).first() #根据用户名字检索用户ID
        PrivateAcID = Calendar.query.filter(Calendar.userid == user.id).all()      #根据用户ID检索事件ID
        PrivateAc = []
        for a in range(0,len(PrivateAcID)):
            PrivateAc.append(Activity.query.filter(Activity.id == PrivateAcID[a-1].activityno).first())

        def getMyKey(elem):
            str1 = str(elem.year)
            str2 = str(elem.month).zfill(2)
            str3 = str(elem.day).zfill(2)
            str4 = str(elem.start_hour).zfill(2)
            str5 = str(elem.start_minute).zfill(2)
            return str1 + str2 + str3 + str4 + str5

        PrivateAc.sort(key = getMyKey)


        js_PrivateAc = {}
        for a in range(0,len(PrivateAc)):
            js_temp = {}
            Id = PrivateAc[a].id
            Year = PrivateAc[a].year
            Month = PrivateAc[a].month
            Day = PrivateAc[a].day
            Start_hour = PrivateAc[a].start_hour
            Start_minute = PrivateAc[a].start_minute
            End_hour = PrivateAc[a].end_hour
            End_minute = PrivateAc[a].end_minute
            Name = PrivateAc[a].name
            Address = PrivateAc[a].address
            Holder = PrivateAc[a].holder
            Department = PrivateAc[a].department
            Tag1 = PrivateAc[a].tag1
            Tag2 = PrivateAc[a].tag2
            Tag3 = PrivateAc[a].tag3
            Introduction = PrivateAc[a].introduction
            Picture = PrivateAc[a].picture
            Url = PrivateAc[a].url
            js_temp['Id'] = Id
            js_temp['Year'] = Year
            js_temp['Month'] = Month
            js_temp['Day'] = Day
            js_temp['Start_hour'] = Start_hour
            js_temp['Start_minute'] = Start_minute
            js_temp['End_hour'] = End_hour
            js_temp['End_minute'] = End_minute
            js_temp['Name'] = Name
            js_temp['Address'] = Address
            js_temp['Holder'] = Holder
            js_temp['Department'] = Department
            js_temp['Tag1'] = Tag1
            js_temp['Tag2'] = Tag2
            js_temp['Tag3'] = Tag3
            js_temp['Introduction'] = Introduction
            js_temp['Picture'] = Picture
            js_temp['Url'] = Url
            js_PrivateAc['Activity'+str(a+1)] = js_temp

        print()
        print('用户' + username + '下载了个人日历，共'+str(len(PrivateAc))+'个活动！')
        #print(js_PrivateAc)
        return json.dumps(js_PrivateAc)


# 登录读取数据 (+监听)
@app.route('/downloadData/loginDownload', methods=['GET', 'POST'])
def loginDownload():
    if request.method == 'GET':
        return 'Failed'
    else:
        data2 = request.get_data()
        data2 = json.loads(data2)
        username = data2['Username']

        user = User.query.filter(User.username == username).first() #根据用户名字检索用户ID

        PrivateAcID = Calendar.query.filter(Calendar.userid == user.id).all()      #根据用户ID检索事件ID

        PrivateAc = []
        for a in range(0,len(PrivateAcID)):
            PrivateAc.append(Activity.query.filter(Activity.id == PrivateAcID[a-1].activityno).first())

        def getMyKey(elem):
            str1 = str(elem.year)
            str2 = str(elem.month)
            if len(str2) == 1:
                str2 = '0' + str2
            str3 = str(elem.day)
            if len(str3) == 1:
                str3 = '0' + str3
            str4 = str(elem.start_hour)
            if len(str4) == 1:
                str4 = '0' + str4
            str5 = str(elem.start_minute)
            if len(str5) == 1:
                str5 = '0' + str5
            return str1 + str2 + str3 + str4 + str5

        PrivateAc.sort(key = getMyKey)

        js_user = {}
        js_PrivateAc = {}
        js_all = {}

        js_user['Department'] = user.department
        js_user['Tag1'] = user.tag1
        js_user['Tag2'] = user.tag2
        js_user['Tag3'] = user.tag3
        js_user['Tag4'] = user.tag4
        js_user['Tag5'] = user.tag5
        js_user['Tag6'] = user.tag6
        js_user['Tag7'] = user.tag7
        js_user['Tag8'] = user.tag8
        js_user['Tag9'] = user.tag9
        js_user['Tag10'] = user.tag10


        for a in range(0,len(PrivateAc)):
            js_temp = {}
            Id = PrivateAc[a].id
            Year = PrivateAc[a].year
            Month = PrivateAc[a].month
            Day = PrivateAc[a].day
            Start_hour = PrivateAc[a].start_hour
            Start_minute = PrivateAc[a].start_minute
            End_hour = PrivateAc[a].end_hour
            End_minute = PrivateAc[a].end_minute
            Name = PrivateAc[a].name
            Address = PrivateAc[a].address
            Holder = PrivateAc[a].holder
            Department = PrivateAc[a].department
            Tag1 = PrivateAc[a].tag1
            Tag2 = PrivateAc[a].tag2
            Tag3 = PrivateAc[a].tag3
            Introduction = PrivateAc[a].introduction
            Picture = PrivateAc[a].picture
            Url = PrivateAc[a].url
            js_temp['Id'] = Id
            js_temp['Year'] = Year
            js_temp['Month'] = Month
            js_temp['Day'] = Day
            js_temp['Start_hour'] = Start_hour
            js_temp['Start_minute'] = Start_minute
            js_temp['End_hour'] = End_hour
            js_temp['End_minute'] = End_minute
            js_temp['Name'] = Name
            js_temp['Address'] = Address
            js_temp['Holder'] = Holder
            js_temp['Department'] = Department
            js_temp['Tag1'] = Tag1
            js_temp['Tag2'] = Tag2
            js_temp['Tag3'] = Tag3
            js_temp['Introduction'] = Introduction
            js_temp['Picture'] = Picture
            js_temp['Url'] = Url
            js_PrivateAc['Activity'+str(a+1)] = js_temp

        js_all['User'] = js_user
        js_all['ActivityNumber'] = len(PrivateAc)
        js_all['Activity'] = js_PrivateAc


        print()
        print('用户'+username+'下载了所有个人数据：')
        print(js_all)
        return json.dumps(js_all)



# 事件详情 (+监听)
@app.route('/downloadData/getDetail', methods=['GET', 'POST'])
def getDetail():
    if request.method == 'GET':
        return 'Failed'
    else:
        data = request.get_data()
        data = json.loads(data)
        username = data['Username']
        ActivityID = data['ActivityID']

        Ac = Activity.query.filter(Activity.id == ActivityID).first()

        js_temp = {}
        Id = Ac.id
        Year = Ac.year
        Month = Ac.month
        Day = Ac.day
        Start_hour = Ac.start_hour
        Start_minute = Ac.start_minute
        End_hour = Ac.end_hour
        End_minute = Ac.end_minute
        Name = Ac.name
        Address = Ac.address
        Holder = Ac.holder
        Department = Ac.department
        Tag1 = Ac.tag1
        Tag2 = Ac.tag2
        Tag3 = Ac.tag3
        Introduction = Ac.introduction
        Picture = Ac.picture
        Url = Ac.url
        js_temp['Id'] = Id
        js_temp['Year'] = Year
        js_temp['Month'] = Month
        js_temp['Day'] = Day
        js_temp['Start_hour'] = Start_hour
        js_temp['Start_minute'] = Start_minute
        js_temp['End_hour'] = End_hour
        js_temp['End_minute'] = End_minute
        js_temp['Name'] = Name
        js_temp['Address'] = Address
        js_temp['Holder'] = Holder
        js_temp['Department'] = Department
        js_temp['Tag1'] = Tag1
        js_temp['Tag2'] = Tag2
        js_temp['Tag3'] = Tag3
        js_temp['Introduction'] = Introduction
        js_temp['Picture'] = Picture
        js_temp['Url'] = Url

        js_all = {}

        user = User.query.filter(User.username == username).first()
        calendar = Calendar.query.filter(Calendar.activityno == ActivityID,Calendar.userid == user.id).first()

        if calendar: js_all['Private'] = 'True'
        else:  js_all['Private'] = 'False'

        js_all['Activity'] = js_temp


        print()
        print('用户'+username+'读取了事件ID为'+str(ActivityID)+'的事件详情')
        print(js_all)
        return json.dumps(js_all)

# 反馈 (+监听)
@app.route('/updateData/feedback', methods=['GET', 'POST'])
def feedback():
    if request.method == 'GET':
        return 'Failed'
    else:
        data = request.get_data()
        data = json.loads(data)
        type1 = data['功能建议']
        type2 = data['使用问题']
        type3 = data['内容相关']
        content = data['反馈内容']


        print()
        print('收到反馈：')
        group = '类型包括'
        if(type1 == 'true'):
            group = group + '功能建议、'
        else:
            group = group + '、'

        if(type2 == 'true'):
            group = group + '使用问题、'

        if(type3 == 'true'): group = group + '内容相关'

        print(group)
        print('具体内容为：'+content)
        return 'Succeeded'


# 根据标签检索事件（公共日历） (+监听)
@app.route('/downloadData/getAcByTag', methods=['GET', 'POST'])
def getAcByTag():
    if request.method == 'GET':
        return 'Failed'
    else:
        data = request.get_data()
        data = json.loads(data)
        username = data['Username']
        date = data['Date']



        ac_year = int(date[0:4])
        ac_month = int(date[4:6])
        ac_day = int(date[6:8])

        print()
        print('用户'+username+'搜索了'+date+'的公共日历')


        user = User.query.filter(User.username == username).first() #根据用户名字检索用户ID

        Ac = []
        Ac = (Activity.query.filter(
           or_(

                Activity.tag1 == user.tag1, Activity.tag1 == user.tag2, Activity.tag1 == user.tag3, Activity.tag1 == user.tag4, Activity.tag1 == user.tag5, Activity.tag1 == user.tag6, Activity.tag1 == user.tag7, Activity.tag1 == user.tag8, Activity.tag1 == user.tag9, Activity.tag1 == user.tag10,
                Activity.tag2 == user.tag1, Activity.tag2 == user.tag2, Activity.tag2 == user.tag3, Activity.tag2 == user.tag4, Activity.tag2 == user.tag5, Activity.tag2 == user.tag6, Activity.tag2 == user.tag7, Activity.tag2 == user.tag8, Activity.tag2 == user.tag9, Activity.tag2 == user.tag10,
                Activity.tag3 == user.tag1, Activity.tag3 == user.tag2, Activity.tag3 == user.tag3, Activity.tag3 == user.tag4, Activity.tag3 == user.tag5, Activity.tag3 == user.tag6, Activity.tag3 == user.tag7, Activity.tag3 == user.tag8, Activity.tag3 == user.tag9, Activity.tag3 == user.tag10,
                Activity.department == user.department

            ),
            Activity.year == ac_year,
            Activity.month == ac_month,
            Activity.day == ac_day

        ).all())


        def getMyKey(elem):
            str4 = str(elem.start_hour)
            if len(str4) == 1:
                str4 = '0' + str4
            str5 = str(elem.start_minute)
            if len(str5) == 1:
                str5 = '0' + str5
            return str4 + str5

        Ac.sort(key = getMyKey)

        js_Ac = {}
        js_all = {}
        for a in range(0,len(Ac)):
            js_temp = {}
            Id = Ac[a].id
            Year = Ac[a].year
            Month = Ac[a].month
            Day = Ac[a].day
            Start_hour = Ac[a].start_hour
            Start_minute = Ac[a].start_minute
            End_hour = Ac[a].end_hour
            End_minute = Ac[a].end_minute
            Name = Ac[a].name
            Address = Ac[a].address
            Holder = Ac[a].holder
            Department = Ac[a].department
            Tag1 = Ac[a].tag1
            Tag2 = Ac[a].tag2
            Tag3 = Ac[a].tag3
            Introduction = Ac[a].introduction
            Picture = Ac[a].picture
            Url = Ac[a].url

            js_temp['Id'] = Id
            js_temp['Year'] = Year
            js_temp['Month'] = Month
            js_temp['Day'] = Day
            js_temp['Start_hour'] = Start_hour
            js_temp['Start_minute'] = Start_minute
            js_temp['End_hour'] = End_hour
            js_temp['End_minute'] = End_minute
            js_temp['Name'] = Name
            js_temp['Address'] = Address
            js_temp['Holder'] = Holder
            js_temp['Department'] = Department
            js_temp['Tag1'] = Tag1
            js_temp['Tag2'] = Tag2
            js_temp['Tag3'] = Tag3
            js_temp['Introduction'] = Introduction
            js_temp['Picture'] = Picture
            js_temp['Url'] = Url
            js_Ac['Activity'+str(a+1)] = js_temp



        js_all['ActivityNumber'] = len(Ac)
        js_all['Activity'] = js_Ac

        if len(Ac)>0:
            print('总共搜索到'+str(len(Ac))+'个活动:')
            print(js_Ac)
        else:
            print('无搜索结果')


        return json.dumps(js_all)


# 根据关键词检索事件
@app.route('/downloadData/getAcByKeywords', methods=['GET', 'POST'])
def getAcByKeywords():
    if request.method == 'GET':
        return 'Failed'
    else:
        data = request.get_data()
        data = json.loads(data)
        keywords = data['Keywords'] #字符串
        date = data['Date']  #8位 int
        group = data['Group'] #4位 int
        #1:日期：全部，0-7,7-15,15up
        #2:时间：全部时长，0-1,1-3,3up
        #3:标签：全部标签，16种标签

        date = int(date)
        group = int(group)



        if int(group/1000) == 0:
            Gr1_1 = date-1
            Gr1_2 = 99999999
        elif int(group/1000) == 1:
            Gr1_1 = date-1
            Gr1_2 = date+6
        elif  int(group/1000) == 2:
            Gr1_1 = date+7
            Gr1_2 = date+14
        else :
            Gr1_1 = date+15
            Gr1_2 = 99999999


        def editDate(a):
            if int(a) == 99999999:
                return 99999999

            y = int(a/10000)
            m = int((a - y* 10000)/100)
            d = int(a%100)

            if m == 1 or m ==3 or m==5 or m ==7 or m==8 or m==10 or m==12:
                m_d = 31
            elif m ==4 or m ==6 or m==9 or m==11:
                m_d = 30
            elif m ==2 and ((y%4 == 0 and y%100 != 0 ) or y%400 == 0):
                m_d = 29
            else: m_d = 28

            if d>m_d:
                d = d-m_d
                m = m + 1

            if m > 12:
                m = m-12
                y = y+1

            return y*10000 + m*100 + d


        Date1 = editDate(Gr1_1)
        Date2 = editDate(Gr1_2)


        if int(group%1000/100) == 0:
            Gr2_1 = 0
            Gr2_2 = 1440
        elif int(group%1000/100) == 1:
            Gr2_1 = 0
            Gr2_2 = 60
        elif int(group%1000/100) == 2:
            Gr2_1 = 61
            Gr2_2 = 180
        else:
            Gr2_1 = 181
            Gr2_2 = 1440

        def calculateTime(s1,s2,e1,e2):
                return e2 + e1*60 - s2 - s1*60

        TagList = ['科创','计算机','体育','实践','外语','经济','创业','文学','电影','志愿','艺术','讲座','学生节','展览','赛事','演出','全部']
        Gr3 = TagList[group%100]


        print()
        print('搜索了关键词为'+keywords+'的所有活动：')



        keywords = str(keywords)

        Ac = []
        Ac = (Activity.query.filter(
           or_(
                Activity.name.contains(keywords),
                Activity.id.contains(keywords),
                Activity.year.contains(keywords),
                Activity.month.contains(keywords),
                Activity.day.contains(keywords),
                Activity.address.contains(keywords),
                Activity.holder.contains(keywords),
                Activity.department.contains(keywords),
                Activity.introduction.contains(keywords)
            ),

            Activity.year * 10000 + Activity.month * 100 + Activity.day >= Date1,
            Activity.year * 10000 + Activity.month * 100 + Activity.day <= Date2,
            calculateTime(Activity.start_hour,Activity.start_minute,Activity.end_hour,Activity.end_minute) > Gr2_1,
            calculateTime(Activity.start_hour,Activity.start_minute,Activity.end_hour,Activity.end_minute) <= Gr2_2,

            or_(
                Activity.tag1 == Gr3,
                Activity.tag2 == Gr3,
                Activity.tag3 == Gr3,
                Gr3 == '全部'
            )

        ).all())

    def getMyKey(elem):
            str1 = str(elem.year)
            str2 = str(elem.month)
            if len(str2) == 1:
                str2 = '0' + str2
            str3 = str(elem.day)
            if len(str3) == 1:
                str3 = '0' + str3
            str4 = str(elem.start_hour)
            if len(str4) == 1:
                str4 = '0' + str4
            str5 = str(elem.start_minute)
            if len(str5) == 1:
                str5 = '0' + str5
            return str1 + str2 + str3 + str4 + str5

    Ac.sort(key = getMyKey)

    js_Ac = {}
    for a in range(0,len(Ac)):
        js_temp = {}
        Id = Ac[a].id
        Year = Ac[a].year
        Month = Ac[a].month
        Day = Ac[a].day
        Start_hour = Ac[a].start_hour
        Start_minute = Ac[a].start_minute
        End_hour = Ac[a].end_hour
        End_minute = Ac[a].end_minute
        Name = Ac[a].name
        Address = Ac[a].address
        Holder = Ac[a].holder
        Department = Ac[a].department
        Tag1 = Ac[a].tag1
        Tag2 = Ac[a].tag2
        Tag3 = Ac[a].tag3
        Introduction = Ac[a].introduction
        Picture = Ac[a].picture
        Url = Ac[a].url

        js_temp['Id'] = Id
        js_temp['Year'] = Year
        js_temp['Month'] = Month
        js_temp['Day'] = Day
        js_temp['Start_hour'] = Start_hour
        js_temp['Start_minute'] = Start_minute
        js_temp['End_hour'] = End_hour
        js_temp['End_minute'] = End_minute
        js_temp['Name'] = Name
        js_temp['Address'] = Address
        js_temp['Holder'] = Holder
        js_temp['Department'] = Department
        js_temp['Tag1'] = Tag1
        js_temp['Tag2'] = Tag2
        js_temp['Tag3'] = Tag3
        js_temp['Introduction'] = Introduction
        js_temp['Picture'] = Picture
        js_temp['Url'] = Url
        js_Ac['Activity'+str(a+1)] = js_temp

    if len(Ac)>0:
        print('总共搜索到'+str(len(Ac))+'个活动:')
        print(js_Ac)
    else:
        print('无搜索结果')

    js_all = {}
    js_all['ActivityNumber'] = len(Ac)
    js_all['Activity'] = js_Ac

    return json.dumps(js_all)




# 登录页面，用户将登录账号密码提交到数据库，如果数据库中存在该用户的用户名及id，返回首页
@app.route('/', methods=['GET', 'POST'])
def login():
    if request.method == 'GET':
        return 'wrong method'
    else:
        data = request.get_data()
        data = json.loads(data)
        username = data['Username']
        password = data['Password']
        user = User.query.filter(User.username == username).first()

        if user:
            if user.check_password(password):
                #session['user'] = username
                #session['id'] = user.id
                #session.permanent = True
                print()
                print('用户'+username+'登录成功！')
                return 'login succeed'  # 登陆成功
            else:
                print()
                print('用户'+username+'登录失败，密码错误！')
                return 'password is wrong'
        else:
            return 'username is not existed'



# 修改密码
@app.route('/updateData/changePassword', methods=['GET', 'POST'])
def edit_password():
    if request.method == 'GET':
        return 'wrong method'
    else:
        data = request.get_data()
        data = json.loads(data)
        print()
        print(data)
        username = data['Username']
        password = data['Password']
        newpassword = data['NewPassword']
        user = User.query.filter(User.username == username).first()

        if user.check_password(password):
            db.session.query(User).filter(User.username == username).update({"password" : generate_password_hash(newpassword)})
            #db.session.user.update({"password" : generate_password_hash(newpassword)})
            db.session.commit()

            print('用户'+username+'修改了密码！')
            return 'change password succeed'  # 登陆成功
        else:
            print('用户'+username+'修改密码失败！')

            return 'password is wrong'

# 删除个人日历
@app.route('/updateData/deletePrivateActivity', methods=['GET', 'POST'])
def deletePrivateActivity():
    if request.method == 'GET':
        return 'wrong method'
    else:
        data = request.get_data()
        data = json.loads(data)
        username = data['Username']
        activityID = data['ActivityID']

        user = User.query.filter(User.username == username).first()

        calendar = Calendar.query.filter(Calendar.userid == user.id,Calendar.activityno == activityID).first()
        db.session.delete(calendar)
        db.session.commit()

        print()
        print('用户'+str(username)+'在个人日历中删除了事件ID为'+str(activityID)+'的活动！')

        return 'delete succeed'


# 添加个人日历
@app.route('/updateData/addPrivateActivity', methods=['GET', 'POST'])
def addPrivateActivity():
    if request.method == 'GET':
        return 'wrong method'
    else:
        data = request.get_data()
        data = json.loads(data)
        username = data['Username']
        activityID = data['ActivityID']

        user = User.query.filter(User.username == username).first()

        c = Calendar.query.filter(Calendar.activityno == activityID, Calendar.userid == user.id).first()
        print(c)


        if c:

            print()
            print('用户'+username+'的个人日历中已经存在事件ID为'+str(activityID)+'的事件！')
            print('请管理员检查数据库！')

            return 'add failed'


        else:
            calendar = Calendar(activityno = activityID, userid = user.id)
            db.session.add(calendar)
            db.session.commit()

            print()
            print('用户'+username+'在个人日历中添加了事件ID为'+str(activityID)+'的事件！')
            return 'add succeed'






# 修改院系
@app.route('/updateData/changeDepartment', methods=['GET', 'POST'])
def edit_department():
    if request.method == 'GET':
        return 'wrong method'
    else:
        data = request.get_data()
        data = json.loads(data)
        username = data['Username']
        newdepartment = data['NewDepartment']

        #aaa=User.query.filter(User.id == userid)
        #user=aaa.first()
        db.session.query(User).filter(User.username == username).update({"department" : newdepartment})
        db.session.commit()

        print()
        print('用户'+username+'修改院系成功！')

        return 'change department succeed'

# 取得院系信息
@app.route('/downloadData/getDepartment', methods=['GET', 'POST'])
def getDepartment():
    if request.method == 'GET':
        return 'wrong method'
    else:
        data = request.get_data()
        data = json.loads(data)
        username = data['Username']

        user = User.query.filter(User.username == username).first()

        print()
        print('用户'+username+'获取了院系信息！')

        js = {}
        js['Department'] = user.department

        return json.dumps(js)

# 将数据库查询结果传递到前端页面 Question.query.all(),问答排序
@app.route('/')
def index():
    context = {
        'questions': Question.query.order_by('-time').all()
    }
    return render_template('index.html', **context)

# 定义上下文处理器
@app.context_processor
def mycontext():
    usern = session.get('user')
    if usern:
        return {'username': usern}
    else:
        return {}


# 定义发布前登陆装饰器
def loginFrist(func):
    @wraps(func)
    def wrappers(*args, **kwargs):
        if session.get('user'):
            return func(*args, **kwargs)
        else:
            return redirect(url_for('login'))

    return wrappers


@app.route('/logout')
def logout():
    session.clear()
    return redirect(url_for('index'))





# 问答页面
@app.route('/question', methods=['GET', 'POST'])
@loginFrist
def question():
    if request.method == 'GET':
        return render_template('question.html')
    else:
        title = request.form.get('title')
        detail = request.form.get('detail')
        classify = request.form.get('classify')
        author_id = User.query.filter(User.username == session.get('user')).first().id
        question = Question(title=title, detail=detail,classify=classify, author_id=author_id)
        db.session.add(question)
        db.session.commit()
    return redirect(url_for('index'))  # 重定向到登录页


@app.route('/detail/<question_id>')
def detail(question_id):
    quest = Question.query.filter(Question.id == question_id).first()
    comments = Comment.query.filter(Comment.question_id == question_id).all()
    return render_template('detail.html', ques=quest, comments=comments)


# 读取前端页面数据，保存到数据库中
@app.route('/comment/', methods=['POST'])
@loginFrist
def comment():
    comment = request.form.get('new_comment')
    ques_id = request.form.get('question_id')
    auth_id = User.query.filter(User.username == session.get('user')).first().id
    comm = Comment(author_id=auth_id, question_id=ques_id, detail=comment)
    db.session.add(comm)
    db.session.commit()
    return redirect(url_for('detail', question_id=ques_id))


# 修改密码
@app.route('/edit_password/', methods=['GET', 'POST'])
def aedit_password():
    if request.method == 'GET':
        return render_template("edit_password.html")
    else:
        newpassword = request.form.get('password')
        user = User.query.filter(User.id == session.get('id')).first()
        user.password = newpassword
        db.session.commit()
        return redirect(url_for('index'))


# 等待
@app.route('/wait')
def wait():
    if request.method == 'GET':
        return render_template("wait.html")


if __name__ == '__main__':
    app.run(host='127.0.0.1', port=12345, debug=True)
