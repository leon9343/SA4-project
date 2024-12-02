import { createStore } from 'vuex';
import { createRouter, createWebHistory } from 'vue-router';
import { mount } from '@vue/test-utils';
import HomeView from '@/views/HomeView.vue';
import HomeSidebar from '@/components/HomeSidebar.vue';
import HomeDisplay from '@/components/HomeDisplay.vue';

const store = createStore({
    state() {
        return {
            user: {},
            simulations: []
        };
    },
});

const router = createRouter({
    history: createWebHistory(),
    routes: [{ path: '/', name: 'Home' }]
});

describe('HomeView.vue', () => {
    let wrapper;

    beforeEach(() => {
        wrapper = mount(HomeView, {
            global: {
                plugins: [store, router],
                stubs: ['router-link', 'router-view']
            }
        });
    });

    it('renders the HomeSidebar component', () => {
        expect(wrapper.findComponent(HomeSidebar).exists()).toBe(true);
    });

    it('renders the HomeDisplay component', () => {
        expect(wrapper.findComponent(HomeDisplay).exists()).toBe(true);
    });

});

