import {createStore} from "vuex";

export default createStore({
    state: {
        user: {},
        simulations: [],
        currentSimulation: {},
    },
    getters: {},
    mutations: {
        setUser(state, user) {
            state.user = user;
        },
        setSimulations(state, simulations) {
            state.simulations = simulations;
        },
        addSimulation(state, simulation) {
            state.simulations.push(simulation);
        },
        updateSimulation(state, simulation) {
            const index = state.simulations.findIndex(sim => sim.id === simulation.id);
            if (index !== -1) {
                state.simulations[index] = simulation;
            }
        },
            deleteSimulation(state, simulation) {
            state.simulations = state.simulations.filter(sim => sim.id !== simulation.id);
        },
        setCurrentSimulation(state, simulation) {
            this.state.currentSimulation = simulation;
            localStorage.setItem("currentSimulation", JSON.stringify(simulation));
        }
    },
    actions: {
        async getSimulation(context, simulationId) {
            try {
                const res = await fetch(`http://localhost:8080/simulations/${simulationId}`, {
                    credentials: "include"
                });
                if (!res.ok) {
                    throw new Error("Failed to get simulation. Status code: " + res.status);
                }
                return await res.json();
            } catch (error) {
                console.log(error.message);
                return null;
            }
        },
        async getSimulationsByUserId(context, userId) {
            try {
                const res = await fetch(`http://localhost:8080/simulations/users/${userId}`, {
                    credentials: "include"
                });
                if (!res.ok) {
                    throw new Error("Failed to get simulation. Status code: " + res.status);
                }
                const simulations = await res.json();
                context.commit("setSimulations", simulations);
                return simulations;
            } catch (error) {
                console.log(error.message);
                return null
            }
        },
        async getPublicSimulations() {
            try {
                const res = await fetch("http://localhost:8080/simulations", {
                    credentials: "include",
                });
                if (!res.ok) {
                    throw new Error("Failed to get simulation. Status code: " + res.status);
                }
                return await res.json();
            } catch (error) {
                console.log(error.message);
                return null;
            }
        },
        async createSimulation(context, {simulation, userId}) {
            try {
                const res = await fetch(`http://localhost:8080/simulations/users/${userId}`, {
                    method: "POST",
                    credentials: "include",
                    headers: {"Content-Type": "application/json"},
                    body: JSON.stringify(simulation),
                });
                if (!res.ok) {
                    throw new Error("Failed to create simulation. Status code: " + res.status);
                }
                let createdSim = await res.json();
                context.commit("addSimulation", createdSim);
                return createdSim;
            } catch (error) {
                console.log(error.message);
                return null
            }
        },
        async updateSimulation(context, {update, simulationId}) {
            try {
                const res = await fetch(`http://localhost:8080/simulations/${simulationId}`, {
                    credentials: "include",
                    method: "PATCH",
                    headers: {"Content-Type": "application/json"},
                    body: JSON.stringify(update),
                });
                if (!res.ok) {
                    throw new Error("Failed to update simulation. Status code: " + res.status);
                }
                let updatedSim = await res.json();
                context.commit("updateSimulation", updatedSim);
                return updatedSim;
            } catch (error) {
                console.log(error.message);
                return null;
            }
        },
        async updateLocalSimulationSetting(context, {update, simulationId, settingType}) {
            const endpointMap = {
                constructionCost: 'localConstructionCostLimit',
                rentCost: 'localRentCostLimit'
            };

            const endpoint = `http://localhost:8080/simulations/${endpointMap[settingType]}/${simulationId}`;

            try {
                const res = await fetch(endpoint, {
                    credentials: "include",
                    method: "PATCH",
                    headers: {"Content-Type": "application/json"},
                    body: JSON.stringify(update),
                });

                if (!res.ok) {
                    throw new Error(`Failed to update simulation. Status code: ${res.status}`);
                }

                let updatedSim = await res.json();
                context.commit("updateSimulation", updatedSim);
                return updatedSim;
            } catch (error) {
                console.log(error.message);
                return null;
            }
        },
        async updateConstructionCostLimit(context, {update, simulationId}) {
            return await context.dispatch('updateLocalSimulationSetting', {
                update,
                simulationId,
                settingType: 'constructionCost'
            });
        },
        async updateRentCostLimit(context, {update, simulationId}) {
            return await context.dispatch('updateLocalSimulationSetting', {
                update,
                simulationId,
                settingType: 'rentCost'
            });
        },
        async updateZoningRestrictions(context, {update, simulationId}) {
            try {
                const res = await fetch(`http://localhost:8080/simulations/zoningRestrictions/${simulationId}`, {
                    credentials: "include",
                    method: "PATCH",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(update),
                });
                if (!res.ok) {
                    throw new Error("Failed to update zones. Status code: " + res.status);
                }
                let updatedSim = await res.json();
                context.commit("updateSimulation", updatedSim);
                return updatedSim;
            } catch (error) {
                console.log(error.message);
                return null;
            }
        },
        async deleteSimulation(context, simulationId) {
            try {
                const res = await fetch(`http://localhost:8080/simulations/${simulationId}`, {
                    method: "DELETE",
                    credentials: "include",
                });
                if (!res.ok) {
                    throw new Error("Failed to delete simulation. Status code: " + res.status);
                }
                let deletedSim = await res.json();
                context.commit("deleteSimulation", deletedSim);
                return deletedSim
            } catch (error) {
                console.log(error.message);
                return null
            }
        },
        async createUser(context, user) {
            await fetch("http://localhost:8080/users", {
                method: "POST",
                credentials: "include",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(user),
            });
        },
        async getUsers() {
            const res = await fetch("http://localhost:8080/users", {
                credentials: "include",
            });
            return await res.json();
        },
        async loginMe(context) {

            const res = await fetch("http://localhost:8080/users/login", {
                credentials: "include",
            });
            if (res.status === 403) {
                window.location.href =
                    "http://localhost:8080/oauth2/authorization/switch-edu-id";
            }
            const user = await res.json();
            return context.commit("setUser", user);
        },
        async formatDate(context, dateString) {
            if (dateString == null) {
                return '';
            }
            ;
            // Parse the string to an array
            const [year, month, day, hour, minute, second] = dateString;
            // Create a Date object using the user's timezone
            const userTimeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
            const date = new Date(Date.UTC(year, month - 1, day, hour, minute, second));
            const options = {
                year: 'numeric', month: 'numeric', day: 'numeric',
                hour: '2-digit', minute: '2-digit', second: '2-digit',
                hour12: false,
                timeZone: userTimeZone
            };

            return new Intl.DateTimeFormat('en-GB', options).format(date);
        },
    },
    modules: {},
});
