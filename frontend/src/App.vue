<template>
  <nav v-if="showHeader" id="header" class="relative flex justify-center items-center px-10 py-5 text-3xl w-full">
    <div class="absolute top-0 left-6 px-8 py-3 text-5xl font-bold">
      SimUrbs
    </div>
    <div class="flex space-x-10">
      <router-link class="hover:text-blue-500" to="/home">Home</router-link>
      <router-link class="hover:text-blue-500" to="/profile">My Profile</router-link>
      <router-link class="hover:text-blue-500" to="/userlist">Users</router-link>
      <router-link class="hover:text-blue-500" to="/simulationlist">Public Simulations</router-link>
    </div>
  </nav>
  <router-view/>
</template>

<script>
import {computed} from 'vue';
import {useRoute} from 'vue-router';

export default {
  methods: {},
  setup() {
    const route = useRoute();
    const showHeader = computed(() => route.name !== 'landing');
    return {
      showHeader
    };
  }
}
</script>

<style>

nav a {
  font-weight: bold;
  color: #2c3e50;
}


@media (max-width: 1200px) {
  nav#header {
    display: none;
  }
}
</style>