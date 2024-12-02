import {createRouter, createWebHistory} from 'vue-router'
import LandingView from '../views/LandingView.vue'
import HomeView from '../views/HomeView.vue'
import Profile from '../views/Profile.vue'
import UserList from '../views/UserList.vue'
import SimulationList from '@/views/SimulationList.vue'
import SimulationLink from '@/views/simulationLink.vue'

const routes = [
    {
        path: '/',
        name: 'landing',
        component: LandingView
    },
    {
        path: '/home',
        name: 'home',
        component: HomeView
    },
    {
        path: '/profile',
        name: 'profile',
        component: Profile
    },
    {
        path: '/userlist',
        name: 'userlist',
        component: UserList
    },

    {
        path: '/simulationlist',
        name: 'simulationlist',
        component: SimulationList
    },
    {
        path: '/simulationlink/:id',
        name: 'simulationlink',
        component: SimulationLink
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
