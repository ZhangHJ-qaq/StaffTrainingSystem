# 数据库设计 Project

19302010021 张皓捷

18300120031 周思瑜

## 设计的ER图

![ER_Diagram_PNG](/ER图/ER_Diagram_PNG.png)

pdf版可见“ER图”目录下的ER_Diagram.pdf文件

## 表结构

### 表结构图示

![表结构示意图](表结构和索引\表结构示意图.png)

### 关于表结构的解释

#### department表

<u>deptID</u>：部门的编号

dept_name：部门名字

#### role表

<u>role_name</u>：角色的名字

role_desc：角色的描述

#### employee表

<u>employee_id</u>：员工编号

name：员工姓名

password：密码

gender：性别

arrival_time：员工入职公司的时间

email：邮件

phone_number：员工的手机号

role：员工的角色

dept_id：员工所属的部门的编号

#### course表

<u>course_id</u>：课程的编号

course_type：课程的类型

course_name：课程的名字

course_content：课程的内容

#### log表

<u>log_id</u>：日志编号

content：日志的内容

time：产生日志的时间

operator：这个日志是由谁的操作产生的

#### dept_course表

*记录部门和课程的关联信息*

<u>dept_id</u>：部门编号

<u>course_id</u>：课程编号

compulsory：在部门dept_id中，课程course_id是否是必修课。

#### dept_manager表

<u>dept_manager_id</u> ：部门经理的employee_id，和employee表中的employee_id是在同一个地址空间内的

#### student表

<u>student_id</u> ：学员的employee_id，和employee表中的employee_id是在同一个地址空间内的

#### instructor表

<u>instructor_id</u> ：教员的employee_id，和employee表中的employee_id是在同一个地址空间内的

#### manage表

*记录部门经理对部门的管理*

<u>dept_id</u>：部门编号

dept_manager_id：部门经理的employee_id

#### teach表

*记录教员对课程的教授信息*

<u>course_id</u>：课程编号

instructor_id：这门课程的教员的employee_id

#### participate表

*记录学员参加课程的信息。此表中每一行都表示学生”参与“了一次课程，及这次”参与“的相关信息*

<u>participate_id</u>：自增主键，用来区分不同的”参与“

start_date：开始日期

finished：这门课程是否结课

score：这门课程的成绩

instructor_id：学生的这次”参与“对应的教员是谁。一门课的教员可能会多次变动，但是学生的参与记录中instructor_id是不会变的

student_id：学生自己的employee_id

course_id：学生参与的课程的编号

### 表结构脚本(.sql)

在”表结构和索引“目录下，有两个.sql脚本文件，分别是table_structure.sql和data.sql。前者可以在数据库中创建表结构，后者可以导入少量必要的数据（比如部门id和超级管理员的账户信息）。创建数据库后，以此导入table_structure.sql和data.sql就能创建好表结构和导入必要的数据。

## 索引

### couse表

#### course_course_id_uindex UNIQUE INDEX course (course_id ASC)

在course表的主键course_id上建立的唯一索引

#### course_course_name_index INDEX course (course_name ASC)

在course表的course_name（课程名字）上建立的索引

### department表

#### department_dept_id_uindex UNIQUE INDEX department (dept_id ASC)

在department表的主键dept_id上建立的唯一索引

### employee表

#### employee_employee_id_uindex UNIQUE INDEX employee (employee_id ASC)

在employee表的主键employee_id上建立的唯一索引

#### employee_name_index INDEX employee (name ASC)

在employee的name（员工名字）上建立索引

### manage表

#### manage_dept_id_uindex UNIQUE INDEX manage (dept_id ASC)

在manager表上的dept_id建立唯一索引，限制一个部门只能有一个部门经理

#### manage_dept_manager_dept_manager_id_fk UNIQUE INDEX manage (dept_manager_id ASC) 

在manager表上的dept_manager_id建立唯一索引，限制一个部门经理只能管理一个部门

### participate表

#### participate_participate_id_uindex UNIQUE INDEX participate (participate_id ASC)

在participate的编号上建立唯一索引

### teach表

#### teach_course_id_uindex UNIQUE INDEX teach (course_id ASC)

在teach表的course_id上建立唯一索引，指定一门课程只能由一个教员上

## 核心SQL语句的说明

### 员工

#### 查看自己被分配到的课程以及教员信息

```sql
select c.course_name,e.name as instructor_name, p.start_date, p.finished, p.score
from participate p
         left join course c on c.course_id = p.course_id
         left join employee e on p.instructor_id = e.employee_id
where p.student_id = ?
```

#### 查看自己历史成绩

```sql
select c.course_name, e.name as instructor_name, p.start_date, p.finished, p.score
from participate p
         left join course c on c.course_id = p.course_id
         left join employee e on p.instructor_id = e.employee_id
where p.student_id = ?
  and p.finished = true
```

### 教员

#### 查看教员教授的员工的信息

```sql
select c.course_name,e.name as instructor_name, p.start_date, p.finished, p.score
from participate p
         left join employee e on e.employee_id = p.student_id
        left join course c on c.course_id = p.course_id
where p.instructor_id = ?
```

### 部门经理

#### 查找员工转到新部门以后要修的课程

```sql
select dc.course_id
from dept_course dc
where dc.dept_id = ?
  and compulsory = true
  and dc.course_id not in (select p.course_id
                           from participate p
                           where p.student_id = ?
                             and (p.finished = false or (p.finished = true and p.score >= 60)))
```

第一个？：目标要转入的部门的编号

第二个？：员工的编号

方法是先找到新部门的必修课，再减去员工已经通过或者正在修的课程

#### 查看员工是否符合转部门要求

```sql
select *
from participate p
where (p.finished = false or p.score < 60)
  and p.student_id = ?
```

第一个？：要查询的员工的编号

如果没有输出，则说明符合；如果有输出，则说明不符合

#### 查看某个部门下的所有员工是否符合转部门要求

```sql
select e.employee_id,
       e.name,
       not exists(
               select *
               from participate p
               where (p.finished = false or p.score < 60)
                 and p.student_id = e.employee_id) as can_transfer
from employee e
where e.dept_id = ?
  and e.role = 'student'
```

第一个？：要查询的部门的部门编号

遍历部门中的每一个员工，检查他们是否有未完成的课程即可。

#### 查询某次课程未通过三次以上的学生

```sql
select e.employee_id, e.name
from employee e
where e.employee_id in (
    select p.student_id
    from participate p
    where p.course_id = ?
      and p.student_id = e.employee_id
      and p.score < 60
    having count(p.participate_id) >= 3
)
```

第一个？：课程的编号

方法是遍历每一个员工，检查他们是否有在对应的课程中的三次及以上的不及格记录。

## 存储过程和触发器

无

## 关于项目

### 后台

后台使用SpringBoot框架。修改/src/main/resources/applications.properties中的这几行

```
server.port=8888
spring.datasource.url=jdbc:mysql://localhost:3306/staff_training_system
spring.datasource.username=root
spring.datasource.password=root
```

使之符合你的数据库信息，然后运行即可。

### 前台

使用VUE编写。

### 展示

1.在8888端口上后端SpringBoot项目

2.在8080端口上运行前端项目

```
npm run serve
```

3.配置nginx

```
    server {
       listen       80;
       listen       127.0.0.1:80;

       location ^~/api/ {
           proxy_pass http://127.0.0.1:8888/;
       }

       location ^~/ {
           proxy_pass http://127.0.0.1:8080/;
       }
    }
```

4.打开浏览器，访问127.0.0.1即可看到