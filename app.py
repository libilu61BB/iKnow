from flask import Flask
from flask import render_template, redirect, url_for, request, session
#import config
from functools import wraps
from datetime import datetime
from sqlalchemy import or_, and_
from werkzeug.security import generate_password_hash, check_password_hash  # 密码保护，使用hash方法
from flask_sqlalchemy import SQLAlchemy
import json

app = Flask(__name__)
# app.config.from_object('config')
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

    def __init__(self, username, password, nickname, department):
        self.username = username
        self.department = department
        self.nickname = nickname
        self.set_password(password)

    def set_password(self, password):
        self.password = generate_password_hash(password)

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
    activityno = db.Column('ACVTIVITYNO',db.Integer, db.ForeignKey('ACTIVITY.ACTIVITYNO'))
    userid = db.Column('USER_ID',db.Integer, db.ForeignKey('USER.USER_ID'))

# 注册
@app.route('/updateData/addNewUser', methods=['GET', 'POST'])
def register():
    if request.method == 'GET':
        return 'wrong method'
    else:
        # data = request.get_json()
        data = request.get_data()
        data = json.loads(data)
        username = data['Username']
        password = data['Password']
        nickname = data['Nickname']
        department = data['Department']
        user = User.query.filter(User.username == username).first()
        if user:
            return 'username existed'
        else:
            user = User(username=username, password=password, nickname=nickname, department=department)
            db.session.add(user)  # 数据库操作
            db.session.commit()
            return 'register succeed'#redirect(url_for('login'))  # 重定向到登录页

# 添加新活动
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
        tag3 = data['ThemeLabel']
        url = data['Url']
        activity = Activity(year=year,month=month,day=day,start_hour=start_hour,department=department,\
        start_minute=start_minute, end_hour=end_hour,end_minute=end_minute,name=name,
        address=address,holder=holder,introduction=introduction,tag1=tag1,tag2=tag2,tag3=tag3,url=url)
        db.session.add(activity)  # 数据库操作
        db.session.commit()
        return 'activity add succeed'  # 重定向到登录页


# 修改用户标签
@app.route('/updateData/changeTag', methods=['GET', 'POST'])
def changetag():
    if request.method == 'GET':
        return 'wrong method'# 重定向到注册页
    else:
        data = request.get_data()
        data = json.loads(data)
        username = data['Username']
        tag1 = data['Tag1']
        tag2 = data['Tag2']
        tag3 = data['Tag3']
        tag4 = data['Tag4']
        tag5 = data['Tag5']
        tag6 = data['Tag6']
        tag7 = data['Tag7']
        tag8 = data['Tag8']
        tag9 = data['Tag9']
        tag10 = data['Tag10']
        db.session.query(User).filter(User.username == username).update({"tag1" : tag1, "tag2" : tag2, "tag3" : tag3, "tag4" : tag4, "tag5" : tag5, "tag6" : tag6, "tag7" : tag7, "tag8" : tag8, "tag9" : tag9, "tag10" : tag10})
        db.session.commit()
        #db.session.user.update({"password" : generate_password_hash(newpassword)})
        return 'change tag succeed'  # 登陆成功

# 检索个人日历
@app.route('/downloadData/getPrivateActivity', methods=['GET', 'POST'])
def getPrivateAc():
    if request.method == 'GET':
        return 'Failed'
    else:
        data = request.get_data()
        data = json.loads(data)
        username = data['Username']

        user = User.query.filter(User.username == username).first() #根据用户名字检索用户ID
        print(user)
        PrivateAcID = Calendar.query.filter(Calendar.userid == user.id)      #根据用户ID检索事件ID

        Activity.order_by(Activity.year,asc(),Activity.month.asc(),Activity.day.asc())

        for a in range(0,len(PrivateAcID)):
            PrivateAc = []
            PrivateAc.append(Activity.query.filter(Activity.id == PrivateAcID[a-1].activityno))

            return jsonify(PrivateAc)


 # 登录页面，用户将登录账号密码提交到数据库，如果数据库中存在该用户的用户名及id，返回首页
@app.route('/', methods=['GET', 'POST'])
def login():
    if request.method == 'GET':
        return 'wrong method'
    else:
        data = request.get_data()
        data = json.loads(data)
        userid = data['UserID']
        password = data['Password']
        user = User.query.filter(User.id == userid).first()
        if user:
            if user.check_password(password):
                session['user'] = username
                session['id'] = user.id
                session.permanent = True
                return 'login succeed'  # 登陆成功
            else:
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
        userid = data['UserID']
        password = data['Password']
        newpassword = data['NewPassword']
        #user = User.query.filter(User.id == userid).first()
        aaa=User.query.filter(User.id == userid)
        user=aaa.first()
        if user.check_password(password):
            db.session.query(User).filter(User.id == userid).update({"password" : generate_password_hash(newpassword)})
            #db.session.user.update({"password" : generate_password_hash(newpassword)})
            return 'change password succeed'  # 登陆成功
        else:
            return 'password is wrong'

# 修改院系
@app.route('/updateData/changeDepartment', methods=['GET', 'POST'])
def edit_department():
    if request.method == 'GET':
        return 'wrong method'
    else:
        data = request.get_data()
        data = json.loads(data)
        userid = data['UserID']
        password = data['Password']
        newdepartment = data['NewDepartment']
        user = User.query.filter(User.id == userid).first()
        #aaa=User.query.filter(User.id == userid)
        #user=aaa.first()
        db.session.query(User).filter(User.id == userid).update({"department" : newdepartment})
        return 'change department succeed'

# 增加数据
# user = User(username='vae', password='5201314')
# db.session.add(user)
# db.session.commit()
# #
# # 查询数据
# user = User.query.filter(User.username == 'vae').first()
# print(user.username,user.password)
#
# #修改数据
# user.password = '250250'
# db.session.commit()

# db.create_all()

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


# 个人中心
@app.route('/usercenter/<user_id>/<tag>')
@loginFrist
def usercenter(user_id, tag):
    user = User.query.filter(User.id == user_id).first()
    context = {
        'user': user
    }
    if tag == '1':
        return render_template('usercenter1.html', **context)
    elif tag == '2':
        return render_template('usercenter2.html', **context)
    else:
        return render_template('usercenter3.html', **context)


# 搜索框带参数搜素显示在首页
@app.route('/search/')
def search():
    qu = request.args.get('q')
    qus = request.args.get('p')
    ques = Question.query.filter(
        or_(
            Question.title.contains(qu),
            Question.detail.contains(qu),
            Question.classify.contains(qus)
            )


    ).order_by('-time')
    return render_template('index.html', questions=ques)


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
