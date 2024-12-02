import store from "@/store"

describe('index.js', () => {
    it('should set the user in the state', () => {
        const user = {id: 1, name: 'John Doe'}
        store.commit('setUser', user)
        expect(store.state.user).toEqual(user)
    })

    it('should set the simulations in the state', () => {
        const simulations = [{id: 1, name: 'Simulation 1'}, {id: 2, name: 'Simulation 2'}]
        store.commit('setSimulations', simulations)
        expect(store.state.simulations).toEqual(simulations)
    })

    it('should add a simulation to the state', () => {
        const simulation = {id: 3, name: 'Simulation 3'}
        store.commit('addSimulation', simulation)
        expect(store.state.simulations).toContainEqual(simulation)
    })

    it('should update a simulation in the state', () => {
        const simulation = {id: 1, name: 'Updated Simulation'}
        store.commit('updateSimulation', simulation)
        expect(store.state.simulations[0]).toEqual(simulation)
    })

    it('should delete a simulation from the state', () => {
        const simulation = {id: 2, name: 'Simulation 2'}
        store.commit('deleteSimulation', simulation)
        expect(store.state.simulations).not.toContain(simulation)
    })

    it('should set the current simulation in the state', () => {
        const simulation = {id: 1, name: 'Current Simulation'}
        store.commit('setCurrentSimulation', simulation)
        expect(store.state.currentSimulation).toEqual(simulation)
    })

    it('should fetch and set simulations by user ID', async () => {
        const userId = 1;
        const simulations = [{id: 1, name: 'Simulation 1'}, {id: 2, name: 'Simulation 2'}];
        global.fetch = jest.fn(() =>
            Promise.resolve({
                ok: true,
                json: () => Promise.resolve(simulations)
            })
        );
        await store.dispatch('getSimulationsByUserId', userId);
        expect(store.state.simulations).toEqual(simulations);
    });

    it('should fetch a single simulation', async () => {
        const simulationId = 1;
        const simulation = {id: 1, name: 'Simulation 1'};
        global.fetch = jest.fn(() =>
            Promise.resolve({
                ok: true,
                json: () => Promise.resolve(simulation)
            })
        );
        const result = await store.dispatch('getSimulation', simulationId);
        expect(result).toEqual(simulation);
    });

    it('should create a new simulation and add it to the state', async () => {
        const userId = 1;
        const simulation = {name: 'New Simulation'};
        const createdSimulation = {id: 3, name: 'New Simulation'};
        global.fetch = jest.fn(() =>
            Promise.resolve({
                ok: true,
                json: () => Promise.resolve(createdSimulation)
            })
        );
        const result = await store.dispatch('createSimulation', {simulation, userId});
        expect(result).toEqual(createdSimulation);
        expect(store.state.simulations).toContainEqual(createdSimulation);
    });

    it('should update a simulation and modify it in the state', async () => {
        const simulationId = 1;
        const update = {name: 'Updated Simulation'};
        const updatedSimulation = {id: 1, name: 'Updated Simulation'};
        global.fetch = jest.fn(() =>
            Promise.resolve({
                ok: true,
                json: () => Promise.resolve(updatedSimulation)
            })
        );
        const result = await store.dispatch('updateSimulation', {update, simulationId});
        expect(result).toEqual(updatedSimulation);
        expect(store.state.simulations[0]).toEqual(updatedSimulation);
    });

    it('should delete a simulation from the state', async () => {
        const simulationId = 1;
        const deletedSimulation = {id: 1};

        // Mock fetch response
        global.fetch = jest.fn(() =>
            Promise.resolve({
                ok: true,
                json: () => Promise.resolve(deletedSimulation)
            })
        );

        const result = await store.dispatch('deleteSimulation', simulationId);
        expect(result).toEqual(deletedSimulation);
        expect(store.state.simulations).not.toContainEqual(deletedSimulation);
    });

    it('should handle fetch error in getSimulation', async () => {
        const simulationId = 1;
        global.fetch = jest.fn(() =>
            Promise.resolve({
                ok: false,
                status: 404
            })
        );
        const result = await store.dispatch('getSimulation', simulationId);
        expect(result).toBeNull();
    });

    it('should set the user after login', async () => {
        const user = {id: 1, name: 'John Doe'};
        global.fetch = jest.fn(() =>
            Promise.resolve({
                ok: true,
                json: () => Promise.resolve(user)
            })
        );
        await store.dispatch('loginMe');
        expect(store.state.user).toEqual(user);
    });

    it('should format a date correctly', async () => {
        const dateString = [2023, 6, 5, 14, 30, 0];
        const formattedDate = await store.dispatch('formatDate', dateString);
        // THIS WILL FAIL IF RUN LOCALLY ON A DIFFERENT TIMEZONE, IT WILL HOWEVER PASS ON CI
        expect(formattedDate).toBe('05/06/2023, 14:30:00');
    });
});
