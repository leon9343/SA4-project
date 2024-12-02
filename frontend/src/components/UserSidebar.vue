<template>
  <div class="p-6 h-screen align-middle bg-gradient-to-b from-white to-blue-100 rounded-lg">
    <div
        class="w-52 h-52 bg-black cursor-pointer rounded-full border-4 border-stone-400 flex justify-center items-center text-white text-9xl">
      <h2 class="text-8xl text-white">{{ userInitial }}</h2>
    </div>
    <button
        v-for="button in buttons"
        :key="button.text"
        :class="button.section === selectedSection ? 'bg-gray-500 text-gray-100' : 'bg-gray-200'"
        class="block mt-6 px-6 w-full text-middle text-xl rounded-md"
        @click="$emit('update-section', button.section)">
      {{ button.text }}
    </button>
    <hr class="h-px my-8 bg-gray-200 border-0 dark:bg-gray-700">
    Implemented by:<br>
    <div class="text-gray-500">Tier Zero <br>
      <span class="text-black">USI</span> development group
    </div>
  </div>
</template>

<script>
export default {
  props: {
    userInitial: String,
    selectedSection: String,
    buttons: Array
  },
}
</script>
