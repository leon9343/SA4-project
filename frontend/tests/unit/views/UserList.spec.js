import {mount} from '@vue/test-utils'
import {createStore} from 'vuex'
import SimulationList from '@/views/UserList.vue'
import UserSidebar from '@/components/UserSidebar.vue'

const mockUsers = [
    {id: 1, name: 'User 1'},
    {id: 2, name: 'User 2'}
]

const mockLoggedInUser = {id: 1, name: 'Test User'}

const store = createStore({
    state() {
        return {
            user: mockLoggedInUser,
            users: mockUsers
        }
    },
    actions: {
        loginMe: jest.fn(),
        getUsers: jest.fn().mockResolvedValue(mockUsers)
    }
})


describe('UserList.vue', () => {
    let wrapper

    beforeEach(async () => {
        wrapper = mount(SimulationList, {
            global: {
                plugins: [store],
                components: {
                    UserSidebar
                }
            }
        })
    })

    it('renders the component correctly', () => {
        expect(wrapper.find('table').exists()).toBe(true);
        expect(wrapper.findAll('tr').length).toBeGreaterThan(1);
        expect(wrapper.find('th').text()).toBe('Currently online');
    });

    it('sorts the users by name', async () => {
        await wrapper.find('th').trigger('click')
        expect(wrapper.vm.userList[0].name).toBe('User 1')
        await wrapper.find('th').trigger('click')
        expect(wrapper.vm.userList[0].name).toBe('User 2')
    })
})