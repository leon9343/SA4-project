<template>
  <div class="flex flex-col pt-7 w-1/5 p-4 space-y-4 text-gray-800">
    <div class="font-bold">Simulations</div>
    <div class="flex flex-col items-center justify-center">
      <div>Sort list by:</div>
      <div class="flex justify-center">
        <button :class="{
          'bg-gray-600 text-white hover:bg-gray-100 hover:text-gray-600': !isNameClicked,
          'bg-gray-100 text-gray-600 hover:bg-gray-600 hover:text-white': isNameClicked,
          'transition-colors duration-600 ease-in-out font-bold py-1 px-2 rounded': true
        }" class="m-1" @click="sortSimulations('name')"
        >
          Name
        </button>
        <button :class="{
          'bg-gray-600 text-white  hover:bg-gray-100 hover:text-gray-600': !isDateClicked,
          'bg-gray-100 text-gray-600 hover:bg-gray-600 hover:text-white': isDateClicked,
          'transition-colors duration-600 ease-in-out font-bold py-1 px-2 rounded': true
        }" class="m-1" @click="sortSimulations('date', true)"
        >
          Date
        </button>
      </div>
    </div>
    <div ref="simulationsContainer"
         class="border p-4 flex-grow space-y-2 rounded-lg max-h-[500px] overflow-auto relative">
      <div v-for="simulation in filteredSimulations" :key="simulation.id"
           :class="{ 'bg-green-100': simulation.id === curr_sim.id, 'hover:bg-gray-100': simulation.id !== curr_sim.id }"
           class="p-2 border rounded relative">
        <div class="font-bold p-1 underline-offset-2 text-xl">
          {{ simulation.name }}
        </div>
        <button
            class="bg-red-700 hover:bg-red-100 hover:text-red-600 transition-colors duration-600 ease-in-out text-white font-bold py-1 px-2 rounded"
            @click="deleteSimulation(simulation.id)">
          DELETE
        </button>
        <button
            class="bg-blue-700 m-1 hover:bg-blue-100 hover:text-blue-600 transition-colors duration-600 ease-in-out text-white font-bold py-1 px-2 mr-2 rounded"
            @click="viewSimulation(simulation)">
          VIEW
        </button>
        <!-- Position the arrow within the relative container -->
      </div>
    </div>
    <div class="bg-gray-200 relative">
      <svg v-if="isAtBottom && isOverflowing"
           class="animate-bounce absolute bg-blue-200 rounded-full border-blue-400 border-2 bottom-2 mb-4 w-7 h-7 text-blue-500 right-2"
           fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
        <path clip-rule="evenodd"
              d="M10 17a.75.75 0 01-.75-.75v-10.5l-3.22 3.22a.75.75 0 11-1.06-1.06l4.5-4.5a.75.75 0 011.06 0l4.5 4.5a.75.75 0 11-1.06 1.06L10.75 5.75v10.5a.75.75 0 01-.75.75z"
              fill-rule="evenodd"></path>
      </svg>
    </div>
    <div class="bold text-gray-700">
      Search:
      <input v-model="search" :class="{ 'rounded-full': search.length > 0 }"
             class="p-2 border m-3 transition-all duration-700" placeholder="Search simulations..." type="text"
             @input="searchSimulations"/>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HomeSidebar',
  props: {
    simulations: Array,
    curr_sim: Object,
    filteredSimulations: Array
  },
  data() {
    return {
      search: '',
      isNameClicked: false,
      isDateClicked: false,
      isAtBottom: false,
      isOverflowing: false,
    };
  },
  methods: {
    sortSimulations(key, isDate = false) {
      this.$emit('sort-simulations', key, isDate);
    },
    deleteSimulation(id) {
      this.$emit('delete-simulation', id);
    },
    viewSimulation(simulation) {
      this.$emit('view-simulation', simulation);
    },
    searchSimulations() {
      this.$emit('search-simulations', this.search);
    }
  }
}
</script>
