import { mount } from '@vue/test-utils';
import HomeSidebar from '@/components/HomeSidebar.vue';

const mockSimulations = [
    { id: 1, name: 'Sim One', date: '2021-01-01' },
    { id: 2, name: 'Sim Two', date: '2021-02-01' }
];

const mockCurrSim = { id: 1, name: 'Sim One', date: '2021-01-01' };

describe('HomeSidebar.vue', () => {
    let wrapper;

    beforeEach(() => {
        wrapper = mount(HomeSidebar, {
            propsData: {
                simulations: mockSimulations,
                curr_sim: mockCurrSim,
                filteredSimulations: mockSimulations
            }
        });
    });


    it('emits search-simulations event on input in the search field', async () => {
        const input = wrapper.find('input[type="text"]');
        await input.setValue('Sim');
        await input.trigger('input');
        expect(wrapper.emitted('search-simulations')).toBeTruthy();
        expect(wrapper.emitted('search-simulations')[0]).toEqual(['Sim']);
    });
});
