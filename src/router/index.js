import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Home',
        redirect: '/systemManager/ManageUsers',
        component: () => import('@/views/Home'),
        children: [
            {
                path: '/login',
                name: 'Login',
                component: () => import('@/views/Login')
            },
            {
                path: '/systemManager',
                name: 'SystemManager',
                // redirect: '/systemManager/manageUsers',
                component: () => import('@/views/SystemManager'),
                children: [
                    {
                        path: 'manageUsers',
                        name: 'ManageUsers',
                        component: () => import('@/components/SystemManager/ManageUsers')
                    },
                    {
                        path: 'manageCourses',
                        name: 'ManageCourses',
                        component: () => import('@/components/SystemManager/ManageCourses')
                    },
                    {
                        path: 'manageLog',
                        name: 'ManageLog',
                        component: () => import('@/components/SystemManager/ManageLog')
                    }
                ]
            },
            {
                path: '/student',
                name: 'Student',
                redirect:'/student/editUserInfos',
                component: () => import('@/views/Student'),
                children:[
                    {
                        path: 'editUserInfos',
                        name: 'EditUserInfos',
                        component: () => import('@/components/Student/EditUserInfos')
                    },
                    {
                        path: 'viewCourseInfos',
                        name: 'ViewCourseInfos',
                        component: () => import('@/components/Student/ViewCourseInfos')
                    },
                    {
                        path: 'viewScores',
                        name: 'ViewScores',
                        component: () => import('@/components/Student/ViewScores')
                    }
                ]
            },
            {
                path:'/departmentManager',
                name:'DepartmentManager',
                component: () => import('@/views/DepartmentManager'),
                redirect:'/departmentManager/viewStaffInfos',
                children:[
                    {
                        path:'viewStaffInfos',
                        name:'ViewStaffInfos',
                        component: () => import('@/components/DepartmentManager/ViewStaffInfos'),
                    },
                    {
                        path:'viewStudentInfos',
                        name:'ViewStudentInfos',
                        component: () => import('@/components/Instructor/ViewStudentInfos'),
                    }
                ]
            },
            {
                path:'/instructor',
                name:'Instructor',
                component: () => import('@/views/Instructor'),
                redirect:'/instructor/recordScores',
                children:[
                    {
                        path:'recordScores',
                        name:'RecordScores',
                        component: () => import('@/components/Instructor/RecordScores'),
                    },
                    {
                        path:'viewStudentInfos',
                        name:'ViewStudentInfos',
                        component: () => import('@/components/Instructor/ViewStudentInfos'),
                    }
                ]
            }
        ]
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router
