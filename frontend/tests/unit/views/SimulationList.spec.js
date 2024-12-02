import {mount} from '@vue/test-utils'
import {createStore} from 'vuex'
import {createRouter, createWebHistory} from 'vue-router'
import SimulationList from '@/views/SimulationList.vue'
import UserSidebar from '@/components/UserSidebar.vue'

const mockSimulations = [
    {id: 1, name: 'Simulation 1', userId: 1, creationDate: [2023, 6, 5, 14, 30, 0]},
    {id: 2, name: 'Simulation 2', userId: 2, creationDate: [2023, 6, 6, 14, 30, 0]}
]
const mockUsers = [
    {id: 1, name: 'User 1'},
    {id: 2, name: 'User 2'}
]
const mockUser = {id: 1, name: 'Test User'}

const store = createStore({
    state() {
        return {
            user: mockUser,
            simulations: mockSimulations,
            users: mockUsers
        }
    },
    actions: {
        loginMe: jest.fn(),
        getPublicSimulations: jest.fn().mockResolvedValue(mockSimulations),
        formatDate: jest.fn((_, date) => Promise.resolve(date)),
        getUsers: jest.fn().mockResolvedValue(mockUsers)
    },
    mutations: {
        setCurrentSimulation: jest.fn()
    }
})

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {path: '/', name: 'Home'},
        {path: '/simulationLink/:id', name: 'SimulationLink'}
    ]
})

describe('SimulationList.vue', () => {
    let wrapper

    beforeEach(async () => {
        wrapper = mount(SimulationList, {
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
        expect(wrapper.find('table').exists()).toBe(true)
        expect(wrapper.find('thead').exists()).toBe(true)
        expect(wrapper.find('tbody').exists()).toBe(true)
        expect(wrapper.find('th').exists()).toBe(true)
        expect(wrapper.find('th').text()).toBe('Name')
        expect(wrapper.findAll('th').at(1).text()).toBe('Created by')
        expect(wrapper.findAll('th').at(2).text()).toBe('Date')
    })

    it('displays the correct number of simulations', () => {
        const rows = wrapper.findAll('tbody tr')
        expect(rows.length).toBe(mockSimulations.length)
    })

    it('displays simulation details correctly', () => {
        const firstRow = wrapper.findAll('tbody tr').at(0)
        const cells = firstRow.findAll('td')
        expect(cells.at(0).text()).toBe('Simulation 1')
        expect(cells.at(1).text()).toBe('User 1')
        const receivedDateArray = JSON.parse(cells.at(2).text())
        expect(receivedDateArray).toEqual([2023, 6, 5, 14, 30, 0])
    })

    it('sorts simulations by name', async () => {
        await wrapper.find('th').trigger('click')
        expect(wrapper.vm.simulationList[0].name).toBe('Simulation 1')
        await wrapper.find('th').trigger('click')
        expect(wrapper.vm.simulationList[0].name).toBe('Simulation 2')
    })

    it('sorts simulations by created by', async () => {
        await wrapper.findAll('th').at(1).trigger('click')
        expect(wrapper.vm.simulationList[0].userId).toBe(1)
        await wrapper.findAll('th').at(1).trigger('click')
        expect(wrapper.vm.simulationList[0].userId).toBe(2)
    })

    it('sorts simulations by date', async () => {
        await wrapper.findAll('th').at(2).trigger('click')
        expect(wrapper.vm.simulationList[0].creationDate).toEqual([2023, 6, 5, 14, 30, 0])
        await wrapper.findAll('th').at(2).trigger('click')
        expect(wrapper.vm.simulationList[0].creationDate).toEqual([2023, 6, 6, 14, 30, 0])
    })

    it('calls viewSimulation on button click', async () => {
        jest.spyOn(wrapper.vm, 'viewSimulation')
        await wrapper.findAll('.view-button').at(0).trigger('click')
        expect(wrapper.vm.viewSimulation).toHaveBeenCalledWith(mockSimulations[0])
    })

    it('copies simulation link to clipboard', async () => {
        const originalClipboard = {...navigator.clipboard};
        const writeTextMock = jest.fn();
        Object.defineProperty(navigator, 'clipboard', {
            writable: true,
            value: {writeText: writeTextMock}
        });

        jest.spyOn(navigator.clipboard, 'writeText')
        await wrapper.findAll('.view-button').at(1).trigger('click')
        expect(navigator.clipboard.writeText).toHaveBeenCalledWith(`${window.location.origin}/simulationLink/2`)

        Object.defineProperty(navigator, 'clipboard', {
            writable: true,
            value: originalClipboard
        });
    })
})
