import { mount } from '@vue/test-utils'
import { createStore } from 'vuex'
import { createRouter, createWebHistory } from 'vue-router'
import Profile from '@/views/Profile.vue'
import UserSidebar from '@/components/UserSidebar.vue'

const mockSimulations = [
    { id: 1, name: 'Simulation 1', population: 1000, creationDate: '2023-06-05' },
    { id: 2, name: 'Simulation 2', population: 2000, creationDate: '2023-06-06' }
]
const mockUser = { id: 1, name: 'Test User', email: 'test@example.com', username: 'testuser' }

const store = createStore({
    state() {
        return {
            user: mockUser,
            simulations: mockSimulations
        }
    },
    actions: {
        loginMe: jest.fn(),
        getSimulationsByUserId: jest.fn().mockResolvedValue(mockSimulations),
        formatDate: jest.fn((_, date) => Promise.resolve(date))
    },
    mutations: {
        setCurrentSimulation: jest.fn()
    }
})

const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/', name: 'Home' }
    ]
})

describe('Profile.vue', () => {
    let wrapper

    beforeEach(async () => {
        wrapper = mount(Profile, {
            global: {
                plugins: [store, router],
                components: {
                    UserSidebar
                }
            }
        })
        await router.isReady()
    })

    it('renders the component correctly', () => {
        expect(wrapper.find('h1').text()).toBe('User Details')
        expect(wrapper.find('.simulations-content').exists()).toBe(false)
    })

    it('displays user details correctly', () => {
        expect(wrapper.find('.text-left span').text()).toBe(mockUser.name)
        expect(wrapper.find('.text-left .mb-6:nth-child(2) span').text()).toBe(mockUser.name)
        expect(wrapper.find('.text-left .mb-6:nth-child(3) span').text()).toBe(mockUser.email)
        expect(wrapper.find('.text-left .mb-6:nth-child(4) span').text()).toBe(mockUser.username)
    })

    it('displays the correct number of simulations', async () => {
        await wrapper.setData({ selectedSection: 'simulations' })
        const rows = wrapper.findAll('tbody tr')
        expect(rows.length).toBe(mockSimulations.length)
    })

    it('sorts simulations by name', async () => {
        await wrapper.setData({ selectedSection: 'simulations' })
        await wrapper.find('th').trigger('click')
        expect(wrapper.vm.simulations[0].name).toBe('Simulation 1')
        await wrapper.find('th').trigger('click')
        expect(wrapper.vm.simulations[0].name).toBe('Simulation 2')
    })

    it('sorts simulations by population', async () => {
        await wrapper.setData({ selectedSection: 'simulations' })
        await wrapper.findAll('th').at(1).trigger('click')
        expect(wrapper.vm.simulations[0].population).toBe(1000)
        await wrapper.findAll('th').at(1).trigger('click')
        expect(wrapper.vm.simulations[0].population).toBe(2000)
    })

    it('sorts simulations by date', async () => {
        await wrapper.setData({ selectedSection: 'simulations' })
        await wrapper.findAll('th').at(2).trigger('click')
        expect(wrapper.vm.simulations[0].creationDate).toBe('2023-06-06')
    })

    it('calls viewSimulation on button click', async () => {
        await wrapper.setData({ selectedSection: 'simulations' })
        jest.spyOn(wrapper.vm, 'viewSimulation')
        await wrapper.findAll('.view-button').at(0).trigger('click')
        expect(wrapper.vm.viewSimulation).toHaveBeenCalledWith(mockSimulations[0])
    })
})
