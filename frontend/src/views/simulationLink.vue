<template>
  <div class="flex overflow-hidden">
    <div class="w-1/5"></div>
    <!-- Simulation List Column -->


    <!-- Main Simulation Display Column -->
    <div v-if="!curr_sim" class="w-3/5 p-4 pt-7 border rounded-lg">
      <div class="font-bold text-2xl mb-4">Simulation <span class="text-red-600">not found</span> or set to <span
          class="text-blue-600 font-mono">private</span> by the creator
      </div>

    </div>

    <div v-else class="w-3/5 p-4 pt-7 border rounded-lg">
      <div class="font-bold text-2xl mb-4">"{{ curr_sim.name }}" by: {{ creatorName }}</div>
      <div class="h-full border p-4 rounded-lg">
        <MetroBlocksScene :city-spec="citySpec" class="rounded-lg" @tile-select="tileSelect($event)"/>
        <div v-if="showPopup" :style="{ left: popupPos.x + 'px', top: popupPos.y + 'px' }"
             class="absolute bg-white p-4 border-black border rounded shadow-lg">
          <div>Type: {{ tileInfo.type }}</div>
          <div>X: {{ tileInfo.x }}, Z: {{ tileInfo.z }}</div>
          <div>Population: {{ tileInfo.population }}</div>
          <div>Floor Count: {{ tileInfo.floorCount }}</div>
          <div>Cost of Renting: {{ tileInfo.costOfRenting }}</div>
        </div>
      </div>
    </div>

    <!-- Settings Column -->

    <div class="flex pt-7 flex-col w-1/5 p-4 space-y-4">
      <div class="flex justify-between items-center font-bold">

        <a href="/profile">
          <div
              class="w-10 h-10 bg-gray-500 cursor-pointer rounded-full border-2 border-stone-400 flex justify-center items-center text-white">
            {{ userInitial }}
          </div>
        </a>
      </div>
      <div v-if="!curr_sim">
        :[
      </div>
      <div v-else class="border p-4 flex-grow space-y-4 rounded-lg">
        <button class="bg-green-700 hover:bg-green-600 text-white font-bold py-2 px-4 rounded" @click="addSimulation">
          Copy Simulation
        </button>

      </div>
    </div>
  </div>
</template>

<script>
import MetroBlocksScene from "@usi-si-teaching/metroblocks";
import "@usi-si-teaching/metroblocks/style";

export default {
  data() {
    return {
      citySpec: {},
      search: "",
      sortOrder: 'asc',
      user: this.$store.state.user,
      simulations: this.simulations,
      filteredSimulations: this.simulations,

      curr_sim: {},

      selected_tile: {},

      viewedCityBlocks: [],


      creatorName: "",

      tileInfo: {
        type: "",
        x: 0,
        z: 0,
        population: 0,
        floorCount: 0,
        costOfRenting: 0,
      },
      showPopup: false,

      mousePos: {x: 0, y: 0},

      storedMousePos: {x: 0, y: 0},

      popupPos: {x: -1, y: -1},
    };


  },
  async mounted() {
    window.addEventListener("mousemove", (event) =>
        this.updateMousePosition(event)
    );
    window.addEventListener("click", (event) =>
        this.updateTooltipPosition(event)
    );


    await this.$store.dispatch("loginMe");
    this.user = this.$store.state.user;
    let publicSimulation = await this.$store.dispatch("getPublicSimulations");

    let userList = await this.$store.dispatch("getUsers");

    this.curr_sim = publicSimulation.find(
        (simulation) => simulation.id === this.$route.params.id
    );
    if (this.curr_sim) {
      this.creatorName = userList.find(
          (user) => user.id === this.curr_sim.userId
      ).name;
    } else
      return;

    if (!this.curr_sim || Object.keys(this.curr_sim).length === 0 || this.curr_sim == {} || this.curr_sim === null || this.curr_sim === undefined) {
      this.citySpec = {};
    } else {
      this.viewSimulation(this.curr_sim);
    }
  },
  components: {
    MetroBlocksScene,
  },


  beforeUnmount() {
    window.removeEventListener("mousemove", this.updateMousePosition);
    window.removeEventListener("click", this.updateTooltipPosition);
  },

  computed: {
    userInitial() {
      return this.user.username ? this.user.name.charAt(0).toUpperCase() : "";
    },
  },
  methods: {
    updateTooltipPosition(event) {
      this.storedMousePos = {x: event.clientX, y: event.clientY};
    },

    updateMousePosition(event) {
      this.mousePos = {x: event.clientX, y: event.clientY};

      this.showPopup = !(Math.abs(this.mousePos.x - this.popupPos.x) > 30 ||
          Math.abs(this.mousePos.y - this.popupPos.y) > 30);
    },

    async addSimulation() {

      this.new_sim = false;
      let simulation = {
        name: this.curr_sim.name + " (copy)",
        averageIncome: this.curr_sim.averageIncome,
        commuteCost: this.curr_sim.commuteCost,
        population: this.curr_sim.population,
        citySize: this.curr_sim.citySize,
        minUtility: this.curr_sim.minUtility,
        alpha: this.curr_sim.alpha,
        costZero: this.curr_sim.costZero,
        delta: this.curr_sim.delta,
        rentEdge: this.curr_sim.rentEdge,
        housing: this.curr_sim.housing,
        isPublic: this.curr_sim.isPublic,
        userId: this.user.id,
        creationDate: new Date().toISOString(),
      };

      let createdSim = await this.$store.dispatch("createSimulation", {simulation, userId: this.user.id});
      if (createdSim === null) {
        return;
      }
      this.simulations = this.$store.state.simulations;
      this.$store.commit("setCurrentSimulation", createdSim);
      this.setCitySpec();
      this.$router.push("/");
    },


    viewSimulation(simulation) {
      this.$store.commit("setCurrentSimulation", simulation);

      this.viewedCityBlocks = simulation.city.blocks;

      this.setCitySpec();

    },

    findTileInBlocks(tile) {
      for (let firstArray of this.curr_sim.city.blocks) {
        for (let secondArray of firstArray) {
          if (
              secondArray.coordinate.x === tile.position.x &&
              secondArray.coordinate.y === tile.position.z
          ) {
            return secondArray;
          }
        }
      }
      return null;
    },

    tileSelect(tile) {
      this.popupPos = {x: this.mousePos.x, y: this.mousePos.y};

      this.selected_tile = this.findTileInBlocks(tile);

      this.tileInfo = {
        type: tile.tileType,
        x: tile.position.x,
        z: tile.position.z,
        population: this.selected_tile.population,
        floorCount: this.selected_tile.floorCount,
        costOfRenting: this.selected_tile.costOfRenting,
      };
      this.showPopup = true;
    },


    setCitySpec() {
      const officeTile = {
        name: "Office Building Tile",
        type: "BuildingOffice",
        meshNameToPath: {
          officeBase: "Office_base.glb",
          officeFloor: "Office_floor.glb",
          officeRoof: "Office_roof.glb",
          palm1: "Palm_tree_01.glb",
          palm2: "Palm_tree_02.glb",
          tree1: "Tree_01.glb",
          tree2: "Tree_02.glb",
          tree3: "Tree_03.glb",
        },
        randomProps: {
          propsMeshes: ["palm1", "palm2", "tree1", "tree2", "tree3"],
          maxCount: 5,
        },
        hasBuilding: true,
        building: {
          center: {x: 0, z: 0},
          floors: [
            {type: "Single", mesh: "officeBase"},
            {type: "Stacked", mesh: "officeFloor", count: "numberOfFloors"},
            {type: "Single", mesh: "officeRoof"},
          ],
        },
      };

      const parkTile = {
        name: "Park tile",
        type: "Park",
        hasBuilding: false,
        meshNameToPath: {
          tree1: "Tree_01.glb",
          tree2: "Tree_02.glb",
          tree3: "Tree_03.glb",
          picNic: "Pic-nic_table.glb",
        },
        randomProps: {
          propsMeshes: [
            "tree1",
            "tree2",
            "tree3",
            "tree1",
            "tree2",
            "tree3",
            "tree1",
            "tree2",
            "tree3",
            "picNic",
          ],
          maxCount: 25,
        },
      };

      const waterTile = {
        name: "Water tile",
        type: "Water",
        hasBuilding: false,
        meshNameToPath: {},
      };

      let res_instances = [];
      let park_instances = [];
      let water_instances = [];

      for (const key in this.curr_sim.city.blocks) {
        let block = this.curr_sim.city.blocks[key];
        let values = key.split(',');
        let coordinate = {x: parseInt(values[0]), y: parseInt(values[1])};
        if (block.blockType === "RESIDENTIAL") {
          res_instances.push({
            floorCount: block.floorCount,
            position: {x: coordinate.x, z: coordinate.y},
          });
        } else if (block.blockType === "PARK") {
          park_instances.push({
            position: {x: coordinate.x, z: coordinate.y},
          });
        } else if (block.blockType === "WATER") {
          water_instances.push({
            position: {x: coordinate.x, z: coordinate.y},
          });
        }
      }
      this.citySpec = {
        id: this.curr_sim.id,
        name: this.curr_sim.name,
        radius: this.curr_sim.city.size,
        tileSpecs: [officeTile, parkTile, waterTile],
        tileInstanceGroups: [
          {
            tileSpecName: officeTile.name,
            instances: res_instances,
          },
          {
            tileSpecName: parkTile.name,
            instances: park_instances,
          },
          {
            tileSpecName: waterTile.name,
            instances: water_instances
          },
        ],
      };
    },
  },
};
</script>
