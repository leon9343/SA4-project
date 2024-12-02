<template>
  <div>
    <div class="flex p-6 overflow-hidden">

      <UserSidebar :user="user"
                   :user-initial="userInitial"
      />


      <table aria-describedby="Currently Online Users" class="text-left whitespace-nowrap w-2/4">
        <thead>
        <tr>
          <th
              class="px-6 py-3 text-xl justify-center text-center bg-gray-200 cursor-pointer hover:bg-gray-100 transition-colors duration-700 ease-in-out"
              @click="sortTable('name')">Currently online
          </th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="user in userList" :key="user.name">
          <td class="w-64 px-6 py-4 text-center text-lg">{{ user.name }}</td>
        </tr>
        </tbody>
      </table>
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
      userList: [],
      user: this.$store.state.user,
      sortOrder: 'asc',
    };
  },
  computed: {
    userInitial() {
      return this.user && this.user.name ? this.user.name.charAt(0).toUpperCase() : '';
    },
  },
  async mounted() {
    await this.$store.dispatch("loginMe");
    this.user = this.$store.state.user;
    this.userList = await this.$store.dispatch("getUsers");
  },
  methods: {
    sortTable(sortKey) {
      if (this.sortOrder === 'asc') {
        this.userList.sort((a, b) => a[sortKey].localeCompare(b[sortKey]));
        this.sortOrder = 'desc';
      } else {
        this.userList.sort((a, b) => b[sortKey].localeCompare(a[sortKey]));
        this.sortOrder = 'asc';
      }
    }
  },
};
</script>