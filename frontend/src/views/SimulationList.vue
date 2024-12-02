<template>
  <div class="flex p-6 overflow-hidden">
    <!-- User sidebar -->
    <UserSidebar :user="user"
                 :user-initial="userInitial"
    />

    <!-- Central table -->
    <table aria-describedby="Public Simulations" class="text-left whitespace-nowrap w-2/4">
      <thead>
      <tr>
        <th
            class="px-6 py-3 text-xl bg-gray-200 cursor-pointer hover:bg-gray-100 transition-colors duration-700 ease-in-out"
            @click="sortByName">Name
        </th>
        <th
            class="px-6 py-3 text-xl bg-gray-200 cursor-pointer hover:bg-gray-100 transition-colors duration-700 ease-in-out"
            @click="sortByCreatedBy">Created by
        </th>
        <th
            class="px-6 py-3 text-xl bg-gray-200 cursor-pointer hover:bg-gray-100 transition-colors duration-700 ease-in-out"
            @click="sortByDate">Date
        </th>
        <th class="px-6 py-3 text-xl bg-gray-300"></th>
        <th class="px-6 py-3 text-xl bg-gray-300"></th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="simulation in simulationList" :key="simulation.id"
          class="bg-blue-200 hover:bg-blue-100 hover:border-blue-300 border-2 border-blue-100 rounded-full transition-colors duration-600 ease-in-out">
        <td class="w-64 px-6 py-4">{{ simulation.name }}</td>
        <td class="w-64 px-6 py-4">{{ this.searchUser(simulation.userId) }}</td>
        <td class="w-64 px-6 py-4">{{ simulation.creationDate }}</td>

        <td class="w-64 px-6 py-4">
          <button
              class="view-button bg-blue-400 text-white px-2 py-1 rounded-lg hover:bg-white hover:text-blue-400 transition-colors duration-700 ease-in-out"
              @click="viewSimulation(simulation)">
            <b>View</b>
          </button>
        </td>
        <td class="w-64 px-6 py-4">
          <button
              class="view-button bg-blue-400 text-white px-2 py-1 rounded-lg hover:bg-white hover:text-blue-400 transition-colors duration-700 ease-in-out"
              @click="copyToClipboard(simulation)">
            <b>Copy link</b>
          </button>
        </td>
      </tr>
      </tbody>
    </table>

    <!-- Empty column on the right -->
    <div class="w-1/4">
      <div v-if="this.alertMessage != ''">
        <br>
        {{ this.alertMessage[0] }}
        <br>
        Name: {{ this.alertMessage[1] }}
        <br>
        Created by: {{ this.alertMessage[2] }}
      </div>
    </div>
  </div>
</template>

<script>
import UserSidebar from '../components/UserSidebar.vue';

export default {
  components: {
    UserSidebar,
  },
  data() {
    return {
      simulationList: [],
      userList: [],
      sortKey: '',
      sortOrder: 'asc',
      alertMessage: [],
      user: this.$store.state.user,
    };
  },
  async mounted() {
    await this.$store.dispatch("loginMe");
    this.user = this.$store.state.user;
    this.simulationList = await this.$store.dispatch("getPublicSimulations");
    // creation date format
    for (const simulation of this.simulationList) {
      simulation.creationDate = await this.$store.dispatch("formatDate", simulation.creationDate);
    }

    this.userList = await this.$store.dispatch("getUsers");
  },
  computed: {
    userInitial() {
      return this.user && this.user.name ? this.user.name.charAt(0).toUpperCase() : '';
    },
  },
  methods: {
    viewSimulation(simulation) {

      if (simulation.userId === this.$store.state.user.id) {
        this.$store.commit("setCurrentSimulation", simulation);
        this.$router.push('/');
      } else {
        this.$store.commit("setCurrentSimulation", simulation);
        this.$router.push('/simulationLink/' + simulation.id);
      }
    },
    copyToClipboard(simulation) {
      navigator.clipboard.writeText(window.location.origin + '/simulationLink/' + simulation.id);
      this.alertMessage[0] = 'Simulation link copied';
      this.alertMessage[1] = simulation.name;
      this.alertMessage[2] = this.searchUser(simulation.userId);
    },

    searchUser(userId) {
      let usr = this.userList.find(user => user.id === userId);
      return usr ? usr.name : 'Anonymous';
    },

    sortByCreatedBy() {
      this.simulationList = this.simulationList.sort((a, b) => {
        // Find the names of the users who created the simulations
        const userA = this.userList.find(user => user.id === a.userId);
        const userB = this.userList.find(user => user.id === b.userId);

        // Compare the names of the users
        return this.sortOrder === 'asc' ? userA.name.localeCompare(userB.name) : userB.name.localeCompare(userA.name);
      });

      this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';
    },

    sortByDate() {
      this.simulationList = this.simulationList.sort((a, b) => {
        // Construct Date objects from arrays representing dates
        const dateA = new Date(a.creationDate[0], a.creationDate[1] - 1, a.creationDate[2], a.creationDate[3], a.creationDate[4], a.creationDate[5]);
        const dateB = new Date(b.creationDate[0], b.creationDate[1] - 1, b.creationDate[2], b.creationDate[3], b.creationDate[4], b.creationDate[5]);

        // Compare the timestamps of the Date objects
        return this.sortOrder === 'asc' ? dateA.getTime() - dateB.getTime() : dateB.getTime() - dateA.getTime();
      });

      this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';
    },
    sortByName() {
      this.simulationList = this.simulationList.sort((a, b) =>
          this.sortOrder === "asc"
              ? a.name.localeCompare(b.name)
              : b.name.localeCompare(a.name)
      );

      this.sortOrder = this.sortOrder === "asc" ? "desc" : "asc";
    },
  }
};
</script>
