import { mount } from '@vue/test-utils';
import HomeDisplay from '@/components/HomeDisplay.vue';
import MetroBlocksScene from '@usi-si-teaching/metroblocks';

const mockFilteredSimulations = [
    { id: 1, name: 'City Build', citySpec: { size: 10 } },
    { id: 2, name: 'Forest Simulation', citySpec: { size: 20 } }
];

const propsData = {
    currSim: mockFilteredSimulations[0],
    filteredSimulations: mockFilteredSimulations,
    citySpec: mockFilteredSimulations[0].citySpec,
    showPopup: true,
    popupPos: { x: 150, y: 200 },
    tileInfo: { type: 'Commercial', x: 1, z: 2, costOfRenting: 500 },
    isInComparisonMode: true
};

describe('HomeDisplay.vue', () => {
    let wrapper;

    beforeEach(() => {
        wrapper = mount(HomeDisplay, {
            propsData,
            global: {
                provide: {
                    $emit: jest.fn()
                },
                stubs: {
                    MetroBlocksScene: true
                }
            }
        });
    });

    it('displays "No simulations" when there are no simulations', async () => {
        await wrapper.setProps({ filteredSimulations: [] });
        expect(wrapper.text()).toContain('No simulations');
    });

    it('displays "No simulations" when there are no simulations', async () => {
        await wrapper.setProps({ filteredSimulations: [] });
        expect(wrapper.text()).toContain('No simulations');
    });

    it('displays the current simulation', () => {
        expect(wrapper.text()).toContain(mockFilteredSimulations[0].name);
    });

    it('renders popup with correct information when showPopup is true', () => {
        expect(wrapper.find('.absolute').exists()).toBe(true);
        expect(wrapper.text()).toContain(propsData.tileInfo.type);
        expect(wrapper.text()).toContain(`X: ${propsData.tileInfo.x}, Z: ${propsData.tileInfo.z}`);
        expect(wrapper.text()).toContain(`${propsData.tileInfo.costOfRenting.toFixed(2)} CHF`);
    });

    it('emits "tile-select" event when a tile is selected', async () => {
        const tileData = { id: 1, type: 'Residential' };
        wrapper.vm.tileSelect(tileData);
        expect(wrapper.emitted()).toHaveProperty('tile-select');
        expect(wrapper.emitted('tile-select')[0]).toEqual([tileData]);
    });

    it('emits "camera-move" event with correct parameters', async () => {
        const cameraData = { dx: 10, dy: 20 };
        wrapper.vm.handleCameraMove(cameraData);
        expect(wrapper.emitted()).toHaveProperty('camera-move');
        expect(wrapper.emitted('camera-move')[0]).toEqual([cameraData]);
    });

    it('updates the display when the citySpec changes', async () => {
        const newCitySpec = { size: 30 };
        await wrapper.setProps({ citySpec: newCitySpec });
        expect(wrapper.vm.citySpec.size).toBe(30);
    });

});
