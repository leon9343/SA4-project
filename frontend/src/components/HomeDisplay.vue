<template>
  <div class="w-3/5 p-5 pt-6 pb-12 border rounded-lg">
    <div class="font-bold text-2xl mt-0 mb-4">
      <div v-if="filteredSimulations && filteredSimulations.length">
        {{ currSim.name }}
      </div>
      <div v-else>
        Simulation Display
      </div>
    </div>

    <div v-if="filteredSimulations == null || filteredSimulations.length === 0" class="h-full p-4 rounded-lg">
      No simulations
    </div>
    <div v-else class="h-full p-4 rounded-lg">
      <MetroBlocksScene :city-spec="citySpec" class="rounded-lg" @tile-select="tileSelect"
                        @camera-move="handleCameraMove"/>
      <div v-if="showPopup" :style="{ left: popupPos.x + 'px', top: popupPos.y + 'px' }"
           class="absolute text-blue-900 p-4 bg-blue-300 border-2 border-blue-500 rounded-xl shadow-lg">
        <div>Type: {{ tileInfo.type }}</div>
        <div>X: {{ tileInfo.x }}, Z: {{ tileInfo.z }}</div>
        <div v-if="tileInfo.floorCount !== undefined">Floor Count: {{ tileInfo.floorCount }}</div>
        <div v-if="tileInfo.oldFloorCount !== undefined && isInComparisonMode"
             :class="{'text-green-900': (tileInfo.costOfRenting - tileInfo.oldCostOfRenting) > 0, 'text-red-500': (tileInfo.costOfRenting - tileInfo.oldCostOfRenting) < 0}"
             style="font-weight: bold;">Change in Floors: {{
            tileInfo.floorCount - tileInfo.oldFloorCount
          }}
        </div>
        <div v-if="tileInfo.costOfRenting !== undefined">Cost of Renting: {{
            (Math.round(tileInfo.costOfRenting * 100)
                / 100).toFixed(2)
          }} CHF
        </div>

        <div v-if="tileInfo.oldCostOfRenting !== undefined && isInComparisonMode"
             :class="{'text-green-900': (tileInfo.costOfRenting - tileInfo.oldCostOfRenting) > 0, 'text-red-500': (tileInfo.costOfRenting - tileInfo.oldCostOfRenting) < 0}"
             style="font-weight: bold;">Change in Rent: {{
            (Math.round((tileInfo.costOfRenting - tileInfo.oldCostOfRenting) * 100)
                / 100).toFixed(2)
          }} CHF
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import MetroBlocksScene from "@usi-si-teaching/metroblocks";

export default {
  props: {
    currSim: Object,
    filteredSimulations: Array,
    citySpec: Object,
    showPopup: Boolean,
    popupPos: Object,
    tileInfo: Object,
    isInComparisonMode: Boolean
  },
  components: {
    MetroBlocksScene
  },
  methods: {
    tileSelect(tile) {
      this.$emit('tile-select', tile);
    },
    handleCameraMove(event) {
      this.$emit('camera-move', event);
    }
  }
};
</script>