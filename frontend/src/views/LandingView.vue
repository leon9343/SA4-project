<template>
  <div :style="{ backgroundImage: `url(${backgroundImage})` }" class="relative min-h-screen bg-cover bg-center">
    <div class="absolute inset-0 flex items-center justify-center">
      <div class="bg-white bg-opacity-75 p-8 rounded-lg shadow-lg text-center">
        <h1 class="text-6xl font-bold mb-6 text-gray-900">Metroblocks</h1>
        <button class="bg-blue-500 hover:bg-blue-400 text-white font-bold py-2 px-4 rounded"
                onclick="location.href = 'http://localhost:8080/oauth2/authorization/switch-edu-id';">
          Start Here!
        </button>
        <h3 class="text-l font-medium mt-6 text-gray-700">Powered by Tier Zero</h3>

      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'landing',
  data() {
    return {
      backgroundImage: 'https://images3.alphacoders.com/205/thumb-1920-205715.jpg', // Outsourcing Eheheheheheheheh
    };
  },
};
</script>
