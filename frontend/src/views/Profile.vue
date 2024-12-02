<template>
  <div class="flex p-6 overflow-hidden">
    <UserSidebar
        :buttons="sidebarButtons"
        :selected-section="selectedSection"
        :user-initial="userInitial"
        @update-section="selectedSection = $event"
    />
    <div class="flex p-5 self-start flex-col w-full">
      <div v-if="selectedSection === 'details'" class="flex w-full">
        <div class="flex flex-col text-left">
          <h1 class="text-5xl w-full flex mb-12"><b>User Details</b></h1>
          <div class="mb-6">
            <h3 class="text-3xl underline text-left">Name:</h3>
            <span class="text-left text-lg pb-8 ml-0">{{ get_name }}</span>
          </div>
          <div class="mb-6">
            <h3 class="text-3xl underline text-left">Email:</h3>
            <span class="text-left text-lg pb-8 ml-0">{{ user.email }}</span>
          </div>
          <div class="mb-6">
            <h3 class="text-3xl underline text-left">Username:</h3>
            <span class="text-left text-lg pb-8 ml-0">{{ user.username }}</span>
          </div>
          <div class="mb-6">
            <h3 class="text-2xl underline text-left">Currently owned simulations:</h3>
            <span class="text-left text-lg pb-8 ml-0">{{ this.simulations.length }}</span>
          </div>
        </div>
      </div>

      <div v-if="selectedSection === 'simulations'" class="simulations-content">
        <h2 class="text-3xl p-3 m-5 border border-gray rounded-full bg-blue-100">My simulations</h2>
        <div>
          <table aria-describedby="User Simulations" class="table-auto w-full text-sm text-left text-gray-500">
            <thead class="text-xs text-gray-700 uppercase bg-gray-50">
            <tr>
              <th
                  class="px-6 py-3 text-xl bg-gray-200 cursor-pointer hover:bg-gray-100 transition-colors duration-700 ease-in-out"
                  @click="sortTable('name')">Simulation Name
              </th>
              <th
                  class="px-6 py-3 text-xl bg-gray-300 cursor-pointer hover:bg-gray-100 transition-colors duration-700 ease-in-out"
                  @click="sortTable('population')">Population
              </th>
              <th
                  class="px-6 py-3 text-xl bg-gray-200 cursor-pointer hover:bg-gray-100 transition-colors duration-700 ease-in-out"
                  @click="sortTable('date')">Date of Creation
              </th>
              <th class="px-6 py-3 text-xl bg-gray-300"></th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="simulation in simulations" :key="simulation.id" class="bg-white border-b w-full">
              <td class="px-6 py-3 text-xl">{{ simulation.name }}</td>
              <td class="px-6 py-3 text-xl">{{ simulation.population }}</td>
              <td class="px-6 py-3 text-xl"> {{ simulation.creationDate }}</td>
              <td class="px-6 py-3 text-xl">
                <button
                    class="view-button bg-blue-400 text-white px-2 py-1 rounded-lg hover:bg-white hover:text-blue-400 transition-colors duration-700 ease-in-out"
                    @click="viewSimulation(simulation)">
                  <b>View</b>
                </button>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import CalendarHeatmap from 'vue3-calendar-heatmap';
import UserSidebar from '../components/UserSidebar.vue';

export default {
  data() {
    return {
      user: this.$store.state.user,
      simulations: this.$store.state.simulations,
      selectedSection: 'details', // default section to show
      sidebarButtons: [
        {text: 'Details', section: 'details'},
        {text: 'Simulations', section: 'simulations'},
      ],
      sortKey: '',
      sortOrder: 'asc',
      simulationDates: [],
    }
  },
  components: {
    CalendarHeatmap,
    UserSidebar
  },
  async mounted() {
    await this.$store.dispatch("loginMe");
    this.user = this.$store.state.user;
    await this.$store.dispatch("getSimulationsByUserId", this.user.id)
    this.simulations = this.$store.state.simulations
    this.simulations.forEach(async (simulation) => {
      simulation.creationDate = await this.$store.dispatch("formatDate", simulation.creationDate);
    });
  },
  computed: {
    get_name() {
      return this.user.name ? this.user.name : 'Anonymous';
    },
    userInitial() {
      return this.user.name ? this.user.name.charAt(0).toUpperCase() : '';
    },
  },
  methods: {
    viewSimulation(simulation) {
      // Define method to view a simulation
      // Set the curr_sim in the store
      this.$store.commit('setCurrentSimulation', simulation);
      this.$router.push('/home');
    },

    sortTable(sortKey) {
      if (this.sortKey === sortKey) {
        // if the same key was clicked twice, reverse the sortOrder
        this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';
      } else {
        this.sortOrder = 'asc';
      }
      this.sortKey = sortKey;
      this.simulations.sort((a, b) => {
        let result = 0;
        if (a[sortKey] < b[sortKey]) {
          result = -1;
        } else if (a[sortKey] > b[sortKey]) {
          result = 1;
        }
        return this.sortOrder === 'asc' ? result : -result;
      });
    },

  }
}
</script>

<style scoped>

body {
  overflow: hidden;
}
</style>
