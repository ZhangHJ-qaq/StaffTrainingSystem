## Tips

后端SpringBoot项目运行在8888端口上，前端如果用VUE实现，可以先运行在8080端口上。

用Nginx监听80端口，如果为/api开头，就转发到后端8888端口上；否则就转发到8080端口上。

例如，127.0.0.1/api/login，就转发到127.0.0.1:8888/login（后端），127.0.0.1/index就转发到127.0.0.1:8080/index（前端）。

```
    server {
       listen       80;
       listen       127.0.0.1:80;
	
	#前端还没配置好
       location ^~/api/ {
           proxy_pass http://127.0.0.1:8888/;
       }
    }
```

以下给出的端口地址，前端在调用的时候都需要在最前面加上/api，发给80端口，由nginx转发到后端的8888端口上。

### 错误处理

如果HTTP的响应码不是200，则响应体的格式一律为

```
{
    "timestamp": "Sun Dec 12 17:43:30 CST 2021",
    "status": int,
    "message": string
}
```



## 用户登录(finished)

#### 位置 /auth/login

#### 权限：无需任何权限

#### 方法 POST

### 请求体

```
{
    "employeeID":"admin", ##员工编号
    "password":"Zhj001006" ##员工密码
}
```

### 返回体

#### 200/401/403

```
{
    "timestamp": "Sun Dec 12 17:43:30 CST 2021",
    "status": 200,
    "message": "登录成功！"
}
```

### HTTP响应中的头部

HTTP响应中有一个名字为Authorization的头部，例如

```
Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6ImFkbWluIiwidXNlcm5hbWUiOiJhZG1pbiIsImlzcyI6Ikh1YUp1YW4iLCJpYXQiOjE2MzkzMDIyMTAsImV4cCI6MTYzOTMwNTgxMH0.pxO70pizQUOVU-9RON8sTbjFEgFprAGK4aEDRmRqrIQ
```

其中，Bearer及空格后面的部分为JWT。Base-64解码以后可以得到类似以下

```
{
  "roles": "admin", #用户的角色
  "username": "admin", #用户名
  "iss": "HuaJuan",
  "iat": 1639302210,
  "exp": 1639305810
}
```





# 管理员部分

## 获得所有的部门信息(finished)

#### 位置 /all/all_department_info

#### 权限：无

#### 方法 GET

### 请求参数：无

### 返回体

```
{
	all_department_info:[
		{
			deptID:int,
			deptName:string
		}......
	]
}
```





## 管理员获取所有用户的信息(finished)

#### 位置 /admin/all_employee_info

#### 权限：管理员

#### 方法 GET

### 请求参数：无

### 返回体

```
{
	all_employee_info:[
		{
			employeeID:string,
			name:string,
			password:string,
			gender:string,
			arrivalTime:string,
			email:string,
			phoneNumber:string,
			role:string,
			department:string,
			deptID:int
		}......
	]
}
```



## 管理员增加/删除员工(finished)

#### 位置 /admin/modify_employee

#### 权限：管理员

#### 方法 POST

### 请求体

```
{
	employeeID:string, ##如果这个employeeID在数据库中存在，就修改对应用户的信息，反之则增加一个新的用户
	name:string,
	password:string,
	gender:string,
	arrivalTime:string,
	phoneNumber:string,
	role:string,  ##admin或student或instructor或department_manager，四选一，在前端做一个下拉框
	deptID:int ##在前端做一个部门的下拉框，选择部门并上传deptID
}
```

### 返回体

```
{
    "timestamp": string,
    "status": int,
    "message": string
}
```



## 管理员查询所有培训课程的信息(finished)

#### 位置 /admin/all_course_info

#### 权限：管理员

#### 方法 GET

### 请求参数：无

### 返回体

```
{
	all_course_info:[
		{
			courseID:string,
			courseName:string,
			courseContent:string,
			courseType:string,
			instructorID:string,
			instructorName:string,
			dept:[ #与这门课程相关联的部门
				{
					deptID:int,
					deptName:string,
					compulsory:boolean
				}
			]
		}......
	]
}
```



## 管理员增加/修改课程(finished)

#### 位置 /admin/modify_course

#### 权限 管理员

#### 方法 POST

### 请求体

```
{
	courseID:string,
	courseName:string,
	courseContent:string,
	courseType:string,
	instructorID:string,
	instructorName:string, #后端无视这个属性
	dept:[
	 #与这门课程相关联的部门
		{
			deptID:int,
			deptName:string, #后端无视这个属性
			compulsory:boolean
		}
	]
}
```



## 管理员根据姓名或员工号查询用户的个人基本信息和培训成绩信息

#### 位置 /admin/employee_info

#### 权限 管理员

#### 方法 GET

### 请求参数

```
employeeID:string
name:string

##注：如果两者都有则后台只考虑employeeID
```

### 返回体

```
{
	employeeID:string,
	name:string,
	gender:string,
	arrivalTime:string,
	email:string,
	phoneNumber:string,
	role:string,
	department:string,
	deptID,
	scores:[
		{
			participateID:int,
			courseID:string,
			courseName:string,
			startDate:string,
			finished:boolean,
			instructorName:string,
			score:int
		}
	]
}
```



## 管理员查看全部日志信息(finished)

#### 位置 /admin/all_log

#### 权限 管理员

#### 方法 GET

### 请求参数

无

### 返回体

```
[
	{
		logID:int,
		content:string,
		time:string,
		operator:string
	}
]
```



# 员工（学员）部分

## 员工（学员）查询自己的信息

#### 位置 /student/my_info

#### 权限 员工（学员）

#### 方法 GET

### 请求参数

无

### 返回体

```
{
	employeeID:string,
	name:string,
	gender:string,
	arrivalTime:string,
	email:string,
	phoneNumber:string,
	role:string,
	department:string,
	deptID:int
}
```



## 员工（学员）修改自己的信息

#### 位置 /student/modify_my_info

#### 权限 员工（学员）

#### 方法 POST

### 请求体

```
{
	name:string,
	gender:string,
	email:string,
	phoneNumber:string,
}
```

### 返回体

```
{
    "timestamp": string,
    "status": int,
    "message": string
}
```



## 员工查询自己被分配到的课程及教员信息（此处是还在进行中的课程）

#### 位置 /student/my_current_courses

#### 权限 员工（学员）

#### 方法 GET

### 请求参数

无

### 返回体

```
{
	[
		participateID:int,
		courseID:string,
		courseName:string,
		startDate:string,
		finished:boolean, ##一定为false，可以不显示
		instructorName:string,
		score:int ##一定为null，可以不显示
	]

}
```





## 员工查询自己历史的课程和成绩

#### 位置 /student/my_history_courses

#### 权限 员工（学员）

#### 方法 GET

### 请求参数

无

### 返回体

```
{
	participateID:int,
	courseID:string,
	courseName:string,
	startDate:string,
	finished:boolean, ##一定为true，可以不显示
	instructorName:string,
	score:int 
}
```



# 教员部分

## 教员查询自己教授的员工信息

#### 位置 /instructor/my_students

#### 权限 教员

#### 方法 GET

### 请求参数

无

### 返回体

```
[
	{
		courseID:int,
		courseName:string,
		[
			{
				participateID:int
				studentID:string,
				name:string,
				isFinished:boolean,
				score:int
			}
		]
	}
]
```



## 教员录入分数

#### 位置 /instructor/register_score

#### 权限 教员

#### 方法 POST

### 请求体

```
{
	participateID:int,
	score:int
}
```

### 返回体

```
{
    "timestamp": string,
    "status": int,
    "message": string
}
```

