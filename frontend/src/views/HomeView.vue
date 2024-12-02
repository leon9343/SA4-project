<template>
  <div class="flex overflow-hidden h-[90vh]">
    <HomeSidebar
        :curr_sim="curr_sim"
        :filteredSimulations="filteredSimulations"
        :simulations="simulations"
        @sort-simulations="sortSimulations"
        @delete-simulation="deleteSimulation"
        @view-simulation="viewSimulation"
        @search-simulations="searchSimulations"
    />

    <HomeDisplay
        :citySpec="citySpec"
        :currSim="curr_sim"
        :filteredSimulations="filteredSimulations"
        :showPopup="showPopup"
        :popupPos="popupPos"
        :tileInfo="tileInfo"
        :isInComparisonMode="isInComparisonMode"
        @tile-select="tileSelect"
        @camera-move="handleCameraMove"
    />

    <!-- Settings Column -->
    <div class="flex flex-col w-1/5 p-4 space-y-4">
      <div class="flex justify-between items-center font-bold">
        Settings
        <a href="/profile">
          <div
              class="w-10 h-10 bg-gray-500 cursor-pointer rounded-full border-2 border-stone-400 flex justify-center items-center text-white">
            {{ userInitial }}
          </div>
        </a>
      </div>
      <div :class="{
        'bg-cyan-200 border-cyan-400': isInZoningMode && !isInComparisonMode,
        'bg-blue-200 border-blue-400': new_sim && !isInComparisonMode,
        'bg-green-200 border-green-400': !new_sim && !isInComparisonMode && !isInZoningMode,
        'bg-yellow-200 border-yellow-400': isInComparisonMode && !isInZoningMode
      }" class="border-2 p-4 flex-grow space-y-4 rounded-lg transition-colors duration-700 overflow-auto">

        <div v-if="!isInComparisonMode && !isInZoningMode" class="flex flex-col space-y-3">
          <div v-if="new_sim" class="font-bold">Create Simulation</div>
          <div v-else class="font-bold">Editing Simulation {{ this.curr_sim.name }}</div>
          <div v-for="key in Object.keys(settings).filter(k => k !== 'population')" :key="key"
               class="flex items-center">
            <label class="w-1/3">{{ settings[key].label }}</label>
            <div v-if="key === 'modelType' && settings[key].type === 'select'" class="w-2/3">
              <select v-model="settings[key].value" class="p-2 border w-full rounded-lg">
                <option v-for="option in settings[key].options" :key="option.value" :value="option.value">{{
                    option.label
                  }}
                </option>
              </select>
            </div>
            <div v-else-if="settings[key].type === 'checkbox'"
                 :class="['w-11', 'h-5', 'bg-gray-300', 'rounded-full', 'relative', 'shadow-inner', 'transition', 'duration-300', 'ml-1', 'px-2', { 'bg-green-500': isToggled }]">
              <input v-model="isToggled" class="appearance-none w-full h-full rounded-full shadow-inner relative z-10"
                     type="checkbox"/>
              <div :class="{ 'transform translate-x': isToggled }"
                   class="dot w-4 h-4 bg-white rounded-full shadow-md absolute left-0.5 top-0.5 transition-transform duration-300"></div>
            </div>
            <input v-else v-model="settings[key].value" :max="settings[key].max" :min="settings[key].min"
                   :step="settings[key].step" :type="settings[key].type" class="p-2 border w-2/3 rounded-lg"/>
          </div>

          <div class="flex items-center">
            <div v-if="settings.modelType.value === 'CLOSED'">
              <label class="w-1/3">{{ settings.population.label }}</label>
              <input v-model="settings.population.value" :min="settings.population.min" :type="settings.population.type"
                     class="p-2 border w-2/3 rounded-lg ml-4"/>
            </div>
          </div>

          <div class="bg-gray-100 border-2 pt-4 pb-4 border-gray-500 rounded-2xl">
            <span class="text-blue-500 font-mono">Const</span> fields of the simulation
            <hr class="ml-2 mr-2 border-gray-500 border-t-2">
            <div class="font-mono p-1 pl-4 flex justify-start">Min Utility: {{ this.minUtility }}</div>
            <div class="font-mono p-1 pl-4 flex justify-start">Alpha: {{ this.alpha }}</div>
            <div class="font-mono p-1 pl-4 flex justify-start">Delta: {{ this.delta }}</div>
            <div class="font-mono p-1 pl-4 flex justify-start">C0: {{ this.costZero }}</div>
            <div class="font-mono p-1 pl-4 flex justify-start">Rent Edge: {{ this.rentEdge }}</div>
            <hr v-if="!this.new_sim" class="border-gray-500 border m-2">
            <div v-if="!this.new_sim" class="font-mono p-1 pl-4 flex justify-start"> Population: {{ this.population }}
            </div>
          </div>
        </div>
        <!-- Comparison mode -->
        <div v-else-if="isInComparisonMode && !isInZoningMode" class="flex flex-col space-y-3">

          <div class="font-bold ">Comparison mode</div>

          <!-- Name comparison -->
          <div v-if="this.oldFields.name !== this.curr_sim.name"
               class="bg-white border-2 border-blue-600 p-2 rounded-full"><span
              class="text-red-700 font-bold">Old:</span>{{ this.oldFields.name }} <span
              class="text-green-700 font-bold">New:</span> {{ this.curr_sim.name }}
          </div>
          <div v-else class="bg-white border-2 border-blue-600 p-2 rounded-full"><span class="font-bold">Name unchanged:
            </span>{{ this.curr_sim.name }}
          </div>

          <!-- isPublic comparison -->
          <div v-if="this.oldFields.isPublic === this.curr_sim.isPublic"
               class="bg-white border-2 border-blue-600 p-2 rounded-full"><span class="font-bold">isPublic is
              unchanged:</span> <span class="font-mono text-blue-600">{{ this.curr_sim.isPublic }}</span></div>
          <div v-else class="bg-white border-2 border-blue-600 p-2 rounded-full"><span class="font-bold">Changed from
            </span> <span class="font-mono text-blue-600">{{ this.oldFields.isPublic }}</span> <span
              class="font-bold">to </span> <span class="font-mono text-blue-600">{{ this.curr_sim.isPublic }}</span>
          </div>

          <!-- Wage comparison -->
          <div v-if="this.oldFields.parameters.wage !== this.curr_sim.parameters.wage" :class="{
            'text-red-700 font-bold': this.oldFields.parameters.wage > this.curr_sim.parameters.wage,
            'text-green-700 font-bold': this.oldFields.parameters.wage < this.curr_sim.parameters.wage
          }" class="bg-white border-2 border-blue-600 p-2 rounded-full">
            Changed wage: <span v-if="this.oldFields.parameters.wage < this.curr_sim.parameters.wage">+</span>{{
              this.curr_sim.parameters.wage - this.oldFields.parameters.wage
            }}
          </div>
          <div v-else class="bg-white border-2 border-blue-600 p-2 rounded-full"><span class="font-bold">This parameter
              is unchanged: </span>{{ this.curr_sim.parameters.wage }}
          </div>

          <!-- transportCost comparison -->
          <div v-if="this.oldFields.parameters.transportCost !== this.curr_sim.parameters.transportCost" :class="{
            'text-red-700 font-bold': this.oldFields.parameters.transportCost > this.curr_sim.parameters.transportCost,
            'text-green-700 font-bold': this.oldFields.parameters.transportCost < this.curr_sim.parameters.transportCost
          }" class="bg-white border-2 border-blue-600 p-2 rounded-full">Changed transport cost: <span
              v-if="this.oldFields.parameters.transportCost < this.curr_sim.parameters.transportCost">+</span>{{
              this.curr_sim.parameters.transportCost - this.oldFields.parameters.transportCost
            }}
          </div>
          <div v-else class="bg-white border-2 border-blue-600 p-2 rounded-full"><span class="font-bold">This parameter
              is unchanged: </span>{{ this.curr_sim.parameters.transportCost }}
          </div>

          <!-- Population comparison -->
          <div v-if="this.oldFields.population !== this.curr_sim.city.population"
               class="bg-white border-2 border-blue-600 p-2 rounded-full">
            <span class="font-bold">Old Population: </span>{{ this.oldFields.population }}
            <br>
            <span class="font-bold">New Population: </span>{{ this.curr_sim.city.population }}
          </div>
          <div v-else class="bg-white border-2 border-blue-600 p-2 rounded-full"><span class="font-bold">Population is
              unchanged: </span>{{ this.curr_sim.city.population }}
          </div>

          <!-- Rent comparison -->
          <div v-if="this.oldFields.rentCost !== this.curr_sim.city.rentCost"
               class="bg-white border-2 border-blue-600 p-2 rounded-full">
            <span class="font-bold">Old rent: </span>{{ this.oldFields.rentCost }}
            <br>
            <span class="font-bold">New rent: </span>{{ this.curr_sim.city.rentCost }}
          </div>
          <div v-else class="bg-white border-2 border-blue-600 p-2 rounded-full"><span class="font-bold">Population is
              unchanged: </span>{{ this.curr_sim.city.rentCost }}
          </div>

          <!-- TO CHANGE AS SOON AS WE HAVE PROPER PARAMETERS -->
          <div class="bg-white border-2 border-blue-600 p-2 rounded-full">{{
              this.oldFields.parameters.globalConstructionCostLimit
            }} vs {{
              this.curr_sim.parameters.globalConstructionCostLimit
            }}
          </div>
          <div class="bg-white border-2 border-blue-600 p-2 rounded-full">{{
              this.oldFields.parameters.globalRentCostLimit
            }} vs {{ this.curr_sim.parameters.globalRentCostLimit }}
          </div>

          <!-- add Population, properly implement the global limits :] -->
        </div>

        <!-- Zoning mode -->
        <div v-else-if="isInZoningMode && !isInComparisonMode" class="flex flex-col">
          <span class="font-bold mb-3">Zoning type</span>
          <div class="flex flex-col space-y-2">
            <button v-if="(!zoneClick && !rentClick && !constrClick) || zoneClick"
                    class="text-white bg-teal-700 border-2 border-teal-900 rounded font-bold py-2 hover:bg-teal-300 hover:border-teal-500 hover:text-teal-900 transition-colors duration-600 ease-in-out"
                    @click="activateZoning('zone')">
              Zone
            </button>
            <button v-if="(!zoneClick && !rentClick && !constrClick) || rentClick"
                    class="text-white bg-teal-700 border-2 border-teal-900 rounded font-bold py-2 hover:bg-teal-300 hover:border-teal-500 hover:text-teal-900 transition-colors duration-600 ease-in-out"
                    @click="activateZoning('rent')">
              Rent Limit
            </button>
            <button v-if="(!zoneClick && !rentClick && !constrClick) || constrClick"
                    class="text-white bg-teal-700 border-2 border-teal-900 rounded font-bold py-2 hover:bg-teal-300 hover:border-teal-500 hover:text-teal-900 transition-colors duration-600 ease-in-out"
                    @click="activateZoning('construction')">
              Construction Limit
            </button>

            <table v-if="rentClick || zoneClick || constrClick" aria-describedby="Zoning mode"
                   class="table-auto flex justify-center">
              <thead>
              <tr>
                <th v-for="j in this.range" :key="'header-' + j" scope="col">
                </th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="i in this.range" :key="i">
                <td v-for="j in this.range" :key="j">

                  <div v-if="this.coordinatesArray.some(item => item.x === j && item.y === i)" :class="{
            'w-4 h-4 rounded': this.range.length < 17,
            'h-2 w-2': this.range.length >= 17
          }" class="bg-gray-600 border-2 hover:bg-gray-600 border-gray-600" @click="gridSelect(i, j)">
                  </div>
                  <div v-else-if="i === 0 && j === 0" :class="{
            'w-4 h-4 rounded': this.range.length < 17,
            'h-2 w-2': this.range.length >= 17
          }" class="bg-red-500 border-2">
                  </div>
                  <div v-else :class="{
            'w-4 h-4 rounded': this.range.length < 17,
            'h-2 w-2': this.range.length >= 17
          }" class="bg-gray-300 border-2 hover:bg-gray-600 border-gray-600 " @click="gridSelect(i, j)">
                  </div>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div v-if="alertMessage && !isInZoningMode" class="text-red-500">{{ alertMessage }}</div>

        <div class="flex justify-center space-x-2">


          <button v-if="new_sim"
                  class="bg-blue-700 hover:bg-blue-100 hover:text-blue-600 transition-colors duration-600 ease-in-out text-white font-bold py-2 px-4 rounded"
                  @click="addSimulation">
            ADD
          </button>

          <div class="flex flex-col justify-center space-y-2">
            <button v-if="isChangeClicked && !isInComparisonMode && !isInZoningMode"
                    class="border-2 bg-yellow-300 text-gray-700 hover:bg-yellow-100 transition-colors duration-600 ease-in-out font-bold py-2 px-4 rounded"
                    @click="toComparison">
              Comparison Mode
            </button>

            <button v-else-if="isChangeClicked && isInComparisonMode && !isInZoningMode"
                    class="m-3 border-2 bg-yellow-300 text-gray-700 hover:bg-yellow-100 transition-colors duration-600 ease-in-out font-bold py-2 px-4 rounded"
                    @click="toComparison">
              Back to Editor Mode
            </button>

            <button v-if="!new_sim && !isInComparisonMode && !isInZoningMode"
                    class="w-full bg-green-700 text-white hover:bg-green-100 hover:text-green-600 transition-colors duration-600 ease-in-out font-bold py-2 px-4 rounded"
                    @click="changeSettings">
              Change Settings
            </button>
            <div v-if="!rentClick && zoneClick && !constrClick" class="flex flex-col mb-5">
              <button
                  v-if="(waterClick && !parkClick && !residentialClick) || (!waterClick && !parkClick && !residentialClick)"
                  class="m-1 bg-blue-600 hover:bg-blue-400 font-bold text-white hover:text-blue-800 rounded p-2 transition-colors ease-in-out duration-600"
                  @click="selectZoneType('water')">
                Water
              </button>

              <button
                  v-if="(!waterClick && parkClick && !residentialClick) || (!waterClick && !parkClick && !residentialClick)"
                  class="m-1 bg-green-800 hover:bg-green-600 font-bold text-white hover:text-green-800 rounded p-2 transition-colors ease-in-out duration-600"
                  @click="selectZoneType('park')">
                Park
              </button>

              <button
                  v-if="(!waterClick && !parkClick && residentialClick) || (!waterClick && !parkClick && !residentialClick)"
                  class="m-1 bg-gray-600 hover:bg-gray-400 font-bold text-white hover:text-gray-800 rounded p-2 transition-colors ease-in-out duration-600"
                  @click="selectZoneType('residential')">
                Residential
              </button>

              <div v-if="alertMessage" class="text-red-500">{{ alertMessage }}</div>
            </div>

            <input v-if="(rentClick || constrClick) && !zoneClick" v-model="limit" class="p-2 border rounded-lg w-full"
                   placeholder="Set limit for selected blocks" type="number" @input="limitAssign"/>
            <div v-if="this.alertMessage && isInZoningMode && ((rentClick || constrClick) && !zoneClick)"
                 class="text-red-500">{{ this.alertMessage }}
            </div>


            <button v-if="rentClick || zoneClick || constrClick"
                    class="bg-green-600 hover:bg-green-400 font-bold text-white hover:text-green-800 rounded p-2 transition-colors ease-in-out duration-600"
                    @click="confirmSelection">
              Confirm
            </button>

            <button v-if="rentClick || zoneClick || constrClick"
                    class="bg-red-600 hover:bg-red-400 font-bold text-white hover:text-red-800 rounded p-2 transition-colors ease-in-out duration-600"
                    @click="resetFlagsAndValues(false)">
              Reset
            </button>

            <button v-if="!isInComparisonMode && !new_sim && !isInZoningMode"
                    class="w-full text-white font-bold bg-cyan-500 rounded py-2 px-4 hover:bg-cyan-300 hover:text-cyan-700 transition-colors duration-600 ease-in-out"
                    @click="toZoning">
              Zoning
            </button>


            <button v-else-if="!isInComparisonMode && !new_sim && isInZoningMode"
                    class="w-full text-white font-bold bg-cyan-500 rounded py-2 px-4 hover:bg-cyan-300 hover:text-cyan-700 transition-colors duration-600 ease-in-out"
                    @click="toZoning">
              Back to Editor Mode
            </button>
          </div>
        </div>

        <div v-if="!new_sim" class="bg-blue-200 flex flex-col border-blue-600 border-2 rounded p-3">
          Create a new simulation
          <button
              class="m-3 bg-blue-700 text-white hover:bg-blue-100 hover:text-blue-600 transition-colors duration-600 ease-in-out font-bold py-2 px-4 rounded"
              @click="triggerForm">
            + SIM
          </button>
        </div>

      </div>

    </div>
    <!--    end setting column-->
  </div>
</template>

<script>
import MetroBlocksScene from "@usi-si-teaching/metroblocks";
import HomeSidebar from "../components/HomeSidebar.vue";
import HomeDisplay from "../components/HomeDisplay.vue";

export default {
  data() {
    return {
      citySpec: {},
      sortOrder: 'asc',
      user: this.$store.state.user,
      simulations: this.$store.state.simulations,
      filteredSimulations: this.simulations,
      alertMessage: "",
      isOverflowing: false,
      isAtBottom: false,
      isDateClicked: false,
      isNameClicked: false,
      isPopClicked: false,
      isToggled: true,
      triggerAdd: false,
      isAdditionalDisplayed: false,

      // us9
      isChangeClicked: false,
      isInComparisonMode: false,
      oldFields: {},

      // zoning for ety
      isInZoningMode: false,
      zoneClick: false,
      rentClick: false,
      constrClick: false,
      waterClick: false,
      parkClick: false,
      residentialClick: false,
      gridSize: 1,
      coordinatesArray: [],
      zoningType: "",
      zoneObject: {},
      localLimit: 0,


      minUtility: 0.0,
      alpha: 0.0,
      costZero: 0.0,
      delta: 0.0,
      rentEdge: 0.0,
      new_sim: false,
      curr_sim: {},
      population: 0.0,

      defaultSettings: {
        modelType: {
          label: "Model", value: "OPEN", type: "select",
          options: [
            {label: "Open", value: "OPEN"},
            {label: "Closed", value: "CLOSED"}
          ]
        },
        name: {label: "Name", value: "Sim 1", type: "text"},
        wage: {label: "Wage", value: 1000, type: "number", min: 0, max: 10000, step: 500},
        transportCost: {label: "Transport Cost", value: 100, type: "number", min: 0, max: 1000, step: 100},
        isPublic: {label: "Public", value: true, type: "checkbox"},
        globalRentCostLimit: {label: "Global Rent Limit", value: 10000, type: "number", min: 0, step: 500},
        globalConstructionCostLimit: {
          label: "Global Construction Limit",
          value: 10000,
          type: "number",
          min: 0,
          step: 500
        },
        population: {label: "Population", value: 1000, type: "number", min: 0, step: 100}
      },

      settings: {},

      selected_tile: {},

      viewedCityBlocks: [],

      // oldCityBlocks: [],

      tileInfo: {
        type: "",
        x: 0,
        z: 0,
        floorCount: 0,
        oldFloorCount: 0,
        costOfRenting: 0,
        oldCostOfRenting: 0,
      },
      showPopup: false,

      mousePos: {x: 0, y: 0},

      storedMousePos: {x: 0, y: 0},

      popupPos: {x: -1, y: -1},
    };
  },
  created() {
    // We can't directly reference other data properties defined within the same data() function. We need create()
    this.settings = this.defaultSettings

  },

  watch: {
    // Watch for changes in the filteredSimulations array (assuming this is what affects overflow)
    filteredSimulations: {
      handler() {
        // Use nextTick to ensure the DOM has been updated
        this.$nextTick(() => {
          // Check for overflow
          // this.checkOverFlow();
        });
      },
      deep: true, // Deep watch for changes in nested arrays/objects
    },
  },

  async mounted() {

    // TOOLTIP //
    window.addEventListener("mousemove", (event) =>
        this.updateMousePosition(event)
    );
    window.addEventListener("click", (event) =>
        this.updateTooltipPosition(event)
    );


    // SCROLL SIM LIST
    // this.checkOverFlow();
    // this.$refs.simulationsContainer.addEventListener('scroll', this.checkScrollPosition);


    this.new_sim = true;

    //LOGIN AND GET THE SIMULATIONS OF A USER//
    await this.$store.dispatch("loginMe");
    this.user = this.$store.state.user;
    await this.$store.dispatch("getSimulationsByUserId", this.user.id);
    this.simulations = this.$store.state.simulations;


    // Don't modify this otherwise we want to pass the contents not the reference otherwise we get nasty bugs
    this.filteredSimulations = [...this.simulations];


    //SET THE VIEWING SIMULATION//
    let localSim = localStorage.getItem("currentSimulation")
    if (localSim === undefined || localSim === null) {
      this.curr_sim = {};
    } else {
      this.curr_sim = localSim;
    }
    // if the simulation is not in the storage then it returns undefined
    // this is why we get error in json.parse every single time you open the website
    if (this.user != {}) {
      this.curr_sim = localStorage.getItem("currentSimulation") ? JSON.parse(localStorage.getItem("currentSimulation")) : {};
    }

    if (!this.curr_sim || Object.keys(this.curr_sim).length === 0 || this.curr_sim === null || this.curr_sim === undefined) {
      this.citySpec = {};
    } else {
      let simulation = this.simulations.find(
          (simulation) => simulation.id === this.curr_sim.id
      );
      // If you remove the simulation in mongo but not in local storage then it returns simulation as undefined
      if (simulation !== undefined) {
        this.viewSimulation(simulation);
      }
    }
  },
  components: {
    MetroBlocksScene,
    HomeSidebar,
    HomeDisplay
  },


  beforeUnmount() {
    window.removeEventListener("mousemove", this.updateMousePosition);
    window.removeEventListener("click", this.updateTooltipPosition);
  },

  computed: {
    userInitial() {
      return this.user.username ? this.user.name.charAt(0).toUpperCase() : "";
    },
    // auto update this.simulations when a change is detected in the store... (doesn't work though in practice)
    simulations() {
      return this.$store.getters.simulations;
    },

    //to fix, since the grid is mirrored on the x = y-axis
    range: function () {
      const rangeArray = [];
      let sz = this.curr_sim.city.size;
      for (let i = -sz; i <= sz; i++) {
        rangeArray.push(i);
      }
      return rangeArray.reverse();
    }

  },
  methods: {

    // SET THE CONST PARAMETERS OF THE SIMULATION
    setConstParameters(simulation) {
      let constants = simulation.constants;
      this.minUtility = constants.minUtility;
      this.alpha = constants.alpha;
      this.costZero = constants.costZero;
      this.delta = constants.delta;
      this.rentEdge = constants.rentEdge;
    },

    // SCROLL SIM LIST
    // checkScrollPosition() {
    //   const container = this.$refs.simulationsContainer;
    //   this.isAtBottom = (container.scrollHeight - container.scrollTop) <= container.clientHeight + 1;
    // },
    // checkOverFlow() {
    //   const container = this.$refs.simulationsContainer;
    //
    //   this.isOverflowing = container.scrollHeight > container.clientHeight;
    // },

    // TOOLTIP POSITIONING
    updateTooltipPosition(event) {
      this.storedMousePos = {x: event.clientX, y: event.clientY};
    },
    updateMousePosition(event) {
      this.mousePos = {x: event.clientX, y: event.clientY};

      this.showPopup = !(Math.abs(this.mousePos.x - this.popupPos.x) > 30 ||
          Math.abs(this.mousePos.y - this.popupPos.y) > 30);
    },

    // CHANGE SETTINGS
    triggerForm() {
      this.new_sim = true;

      let currSettings = this.settings;
      this.settings = this.defaultSettings;

      this.settings.name = currSettings.name;
      this.settings.wage = currSettings.wage;
      this.settings.transportCost = currSettings.transportCost;

      this.isAdditionalDisplayed = false;
      this.triggerAdd = false;

      //us 9
      this.isChangeClicked = false;

      this.isInZoningMode = false;
      this.isInComparisonMode = false;
      this.rentClick = false;
      this.zoneClick = false;
      this.constrClick = false;
    },

    //us 9
    toComparison() {
      this.isInComparisonMode = !this.isInComparisonMode;
    },

    //zoning for ety
    toZoning() {
      this.isInZoningMode = !this.isInZoningMode;
      this.zoneClick = false;
      this.rentClick = false;
      this.constrClick = false;

      this.waterClick = false;
      this.parkClick = false;
      this.residentialClick = false;
      this.alertMessage = "";
    },

    activateZoning(type) {
      const zoningType = {
        zone: 'zoneClick',
        rent: 'rentClick',
        construction: 'constrClick'
      };
      this.zoneClick = false;
      this.rentClick = false;
      this.constrClick = false;
      this[zoningType[type]] = true;

      this.gridGenerator();
    },

    gridSelect(j, i) {
      if (this.coordinatesArray.some(item => item.x === i && item.y === j)) {
        for (let item of this.coordinatesArray) {
          if (item.x === i && item.y === j) {
            this.coordinatesArray.splice(this.coordinatesArray.indexOf(item), 1);
          }
        }
      } else {
        this.coordinatesArray.push({x: i, y: j});
      }
    },

    selectZoneType(type) {
      const types = ['water', 'park', 'residential'];
      types.forEach(t => this[`${t}Click`] = (t === type));
      this.zoningType = type.toUpperCase();
    },

    limitAssign() {
      this.localLimit = this.limit;
    },

    async confirmSelection() {
      if (!this.isValidSelection()) {
        return false;
      }

      this.setupZoneObject();

      if (this.coordinatesArray.length === 0) {
        this.alertMessage = "No blocks selected";
        return false;
      }

      if (await this.updateSimulation()) {
        this.resetFlagsAndValues(true);
      }
    },

    isValidSelection() {
      if (this.zoneClick || this.rentClick || this.constrClick) {
        if (this.zoneClick && !this.waterClick && !this.parkClick && !this.residentialClick) {
          this.alertMessage = "Please select a block type";
          return false;
        }
        if ((this.rentClick || this.constrClick) && this.localLimit === 0) {
          this.alertMessage = "Please set a limit";
          return false;
        }
        return true;
      } else {
        this.alertMessage = "Invalid action";
        return false;
      }
    },

    setupZoneObject() {
      if (this.rentClick) {
        this.zoningType = "RENT";
        this.limitKey = "localRentLimit";
      } else if (this.constrClick) {
        this.zoningType = "CONSTRUCTION";
        this.limitKey = "localConstructionCostLimit";
      }

      this.zoneObject = {
        blockType: this.zoningType,
        coordinates: this.coordinatesArray,
        localLimit: undefined
      };

      if (this.localLimit) {
        this.zoneObject.localLimit = this.localLimit;
      }
      if (this.rentClick || this.constrClick) {
        this.zoneObject[this.limitKey] = this.localLimit;
      }
    },

    async updateSimulation() {
      let actionType;
      if (this.zoneClick) {
        actionType = "updateZoningRestrictions";
      } else if (this.rentClick) {
        actionType = "updateRentCostLimit";
      } else {
        actionType = "updateConstructionCostLimit";
      }

      try {
        let updatedSim = await this.$store.dispatch(actionType, {
          update: this.zoneObject,
          simulationId: this.curr_sim.id
        });
        if (updatedSim === null) {
          return false;
        }
        this.simulations = this.$store.state.simulations;
        this.$store.commit("setCurrentSimulation", updatedSim);
        this.curr_sim = updatedSim;
        this.filteredSimulations = [...this.simulations];
        this.setCitySpec();
        return true;
      } catch (error) {
        console.log(error.message);
        return false;
      }
    },

    resetFlagsAndValues(exit) {
      if (exit) {
        this.isInZoningMode = this.zoneClick = this.rentClick = this.constrClick = false;
      }
      this.waterClick = this.parkClick = this.residentialClick = false;
      this.coordinatesArray = [];
      this.gridSize = 1;
      this.alertMessage = "";
      this.localLimit = 0;
    },

    gridGenerator() {
      const size = this.curr_sim.city.size;
      this.gridSize = (size * 2) + 1;
    },

    validateSettings() {
      for (const key of Object.keys(this.settings)) {
        let setting = this.settings[key];

        if (setting.label === "Transport Cost") {
          if (setting.value < 0 || setting.value > 1000) {
            this.alertMessage = "Transport Cost must be between 0 and 1000";
            return false;
          } else if (setting.value === "") {
            this.alertMessage = "Transport Cost can not be empty";
            return false;
          }
        }

        if (setting.label === "Wage") {
          if (setting.value < 0 || setting.value > 10000) {
            this.alertMessage = "Wage must be between 0 and 10000";
            return false;
          } else if (setting.value === "") {
            this.alertMessage = "Wage can not be empty";
            return false;
          }
        }


      }
      this.alertMessage = "";
      return true;
    },

    //SORTING SIMULATIONS
    sortSimulations(key, isDate = false) {
      if (isDate) {
        this.filteredSimulations.sort((a, b) => {
          const dateA = new Date(a.creationDate[0], a.creationDate[1] - 1, a.creationDate[2], a.creationDate[3], a.creationDate[4], a.creationDate[5]);
          const dateB = new Date(b.creationDate[0], b.creationDate[1] - 1, b.creationDate[2], b.creationDate[3], b.creationDate[4], b.creationDate[5]);
          return this.sortOrder === 'asc' ? dateA - dateB : dateB - dateA;
        });
      } else {
        this.filteredSimulations.sort((a, b) =>
            this.sortOrder === "asc" ? a[key].localeCompare(b[key]) : b[key].localeCompare(a[key])
        );
      }
      this.updateSortState(key);
    },
    updateSortState(sortedKey) {
      this.isNameClicked = sortedKey === 'name';
      this.isDateClicked = sortedKey === 'date';
      this.isPopClicked = sortedKey === 'population';
      this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';
    },

    /*
    still useful, when population will be implemented properly,
    uncomment, this will need change based on the population passed in the city object
    pulled from the backend.

    Please DO NOT DELETE
    */

    // sortByPopulation() {
    // this.filteredSimulations = this.filteredSimulations.sort((a, b) =>
    //     this.sortOrder === "asc"
    //       ? a.population - b.population
    //       : b.population - a.population
    //   );
    //   this.isPopClicked = !this.isPopClicked; // Toggle the clicked state
    //   this.isDateClicked = false;
    //   this.isNameClicked = false;
    //   this.sortOrder = this.sortOrder === "asc" ? "desc" : "asc";
    // },


    // ADD SIMULATION
    async addSimulation() {
      if (!this.validateSettings()) {
        return;
      }
      let name = this.new_sim
          ? this.settings.name.value
          : this.settings.name.value + " (copy)";

      this.new_sim = false;

      let newSimulation = {
        name: name,
        isPublic: this.isToggled,
        creationDate: new Date().toISOString(),
        parameters: {
          wage: this.settings.wage.value,
          transportCost: this.settings.transportCost.value,
          globalRentCostLimit: this.settings.globalRentCostLimit.value,
          globalConstructionCostLimit: this.settings.globalConstructionCostLimit.value,
          modelType: this.settings.modelType.value,
          population: this.settings.population.value
        }
      };

      //zoning for ety
      this.isInZoningMode = false;
      this.zoneClick = false;
      this.rentClick = false;
      this.constrClick = false;
      this.coordinatesArray = [];
      this.gridSize = 1;

      try {
        let createdSim = await this.$store.dispatch("createSimulation", {
          simulation: newSimulation,
          userId: this.user.id
        });
        if (createdSim === null) {
          return;
        }
        this.simulations = this.$store.state.simulations;
        this.filteredSimulations.push(createdSim);
        this.$store.commit("setCurrentSimulation", createdSim);
        this.curr_sim = createdSim;
        this.setCitySpec();
        // To view the created simulation as soon as it is created
        this.viewSimulation(createdSim);
      } catch (error) {
        console.log(error.message);
      }
    },


    // CHANGE SETTINGS
    async changeSettings() {
      if (!this.validateSettings()) {
        return;
      }
      let updatedFields = {
        name: this.settings.name.value,
        isPublic: this.isToggled,
        parameters: {
          wage: this.settings.wage.value,
          transportCost: this.settings.transportCost.value,
          globalRentCostLimit: this.settings.globalRentCostLimit.value,
          globalConstructionCostLimit: this.settings.globalConstructionCostLimit.value,
          modelType: this.settings.modelType.value,
          population: this.settings.population.value
        }
      };

      //us 9
      this.isChangeClicked = true;
      this.oldFields = {
        name: this.curr_sim.name,
        isPublic: this.curr_sim.isPublic,
        parameters: {
          wage: this.curr_sim.parameters.wage,
          transportCost: this.curr_sim.parameters.transportCost,

          globalRentCostLimit: this.curr_sim.parameters.globalRentCostLimit,
          globalConstructionCostLimit: this.curr_sim.parameters.globalConstructionCostLimit,
          oldCityBlocks: this.curr_sim.city.blocks
        },
        population: this.curr_sim.city.population,
        rentCost: this.curr_sim.city.rentCost
      };

      try {
        let updatedSim = await this.$store.dispatch("updateSimulation", {
          update: updatedFields,
          simulationId: this.curr_sim.id
        });
        if (updatedSim === null) {
          return;
        }

        this.simulations = this.$store.state.simulations;
        this.$store.commit("setCurrentSimulation", updatedSim);
        this.curr_sim = updatedSim;
        this.filteredSimulations = [...this.simulations];
        this.setCitySpec();

      } catch (error) {
        console.log(error.message);
      }

    },

    // SET VIEWING SIMULATION
    viewSimulation(simulation) {
      this.settings.modelType.value = simulation.parameters.modelType;
      this.settings.name.value = simulation.name;
      this.settings.wage.value = simulation.parameters.wage;
      this.settings.transportCost.value = simulation.parameters.transportCost;
      this.settings.isPublic.value = simulation.isPublic;
      this.settings.globalRentCostLimit.value = simulation.parameters.globalRentCostLimit;
      this.settings.globalConstructionCostLimit.value = simulation.parameters.globalConstructionCostLimit;
      this.settings.population.value = simulation.city.population;

      this.isToggled = simulation.isPublic;
      this.$store.commit("setCurrentSimulation", simulation);
      this.curr_sim = simulation;
      this.viewedCityBlocks = simulation.city.blocks;
      this.setConstParameters(simulation);

      this.isAdditionalDisplayed = true;
      this.new_sim = false;
      this.population = simulation.city.population;

      //us 9
      this.isChangeClicked = false;
      this.isInComparisonMode = false;
      this.oldFields = {};

      //zoning for ety
      this.isInZoningMode = false;
      this.zoneClick = false;
      this.rentClick = false;
      this.constrClick = false;
      this.coordinatesArray = [];
      this.gridSize = 1;

      this.setCitySpec();
    },

    tileSelect(tile) {
      const xIndex = tile.position.x;
      const zIndex = tile.position.z;

      const actualSize = this.curr_sim.city.size * 2;

      if (xIndex <= actualSize && zIndex <= actualSize) {
        const cityBlocks = this.curr_sim.city.blocks;

        const oldCityBlocks = this.isChangeClicked && this.oldFields.parameters.oldCityBlocks
            ? this.oldFields.parameters.oldCityBlocks
            : cityBlocks;

        this.popupPos = {x: this.mousePos.x, y: this.mousePos.y};


        let key = `${xIndex}, ${zIndex}`;
        let block = cityBlocks[key];

        let oldBlock = oldCityBlocks && oldCityBlocks.hasOwnProperty(key) ? oldCityBlocks[key] : block;

        const coordinate = {x: xIndex, y: zIndex};
        this.selected_tile = block;

        this.tileInfo = {
          type: block.blockType,
          x: coordinate.x,
          z: coordinate.y,

          floorCount: block.floorCount,
          oldFloorCount: this.isChangeClicked ? oldBlock.floorCount : block.floorCount,
          costOfRenting: block.rentCost,
          oldCostOfRenting: this.isChangeClicked ? oldBlock.rentCost : block.rentCost,
        };

        this.showPopup = true;
      }
    },

    handleCameraMove(cameraMove) {
      this.showPopup = false;
    },

    // SEARCH SIMULATIONS
    searchSimulations(search) {
      const searchTerm = search.trim()
      if (!searchTerm) {
        this.filteredSimulations = this.simulations;
      } else {
        this.filteredSimulations = this.simulations.filter((simulation) =>
            simulation.name.toLowerCase().includes(searchTerm.toLowerCase())
        );
      }
    },

    // DELETE SIMULATION
    async deleteSimulation(simulationId) {
      let deletedSim = await this.$store.dispatch("deleteSimulation", simulationId);
      this.simulations = this.$store.state.simulations;
      this.filteredSimulations = this.filteredSimulations.filter((sim) => sim.id !== deletedSim.id);

      //us 9
      this.isChangeClicked = false;
      //pessicurezza
      this.isInComparisonMode = false;

      //zoning for ety
      this.isInZoningMode = false;
      this.zoneClick = false;
      this.rentClick = false;
      this.constrClick = false;
      this.coordinatesArray = [];
      this.gridSize = 1;

      if (this.simulations.length !== 0) {
        this.curr_sim = this.simulations[0];
        this.$store.commit("setCurrentSimulation", this.curr_sim);
        this.setCitySpec();
      } else {
        this.triggerForm();
        this.curr_sim = {};
      }
      localStorage.removeItem("currentSimulation");
    },

    // SET CITY SPEC
    setCitySpec() {

      const commonMeshPaths = {
        palm1: "Palm_tree_01.glb",
        palm2: "Palm_tree_02.glb",
        tree1: "Tree_01.glb",
        tree2: "Tree_02.glb",
        tree3: "Tree_03.glb",
      };

      const createTileSpec = (name, type, hasBuilding, meshPaths, buildingFloors, maxPropsCount, propsMeshes) => ({
        name,
        type,
        meshNameToPath: {...commonMeshPaths, ...meshPaths},
        randomProps: {
          propsMeshes: propsMeshes || Object.keys(commonMeshPaths),
          maxCount: maxPropsCount,
        },
        hasBuilding,
        building: hasBuilding ? {
          center: {x: 0, z: 0},
          floors: buildingFloors,
        } : undefined,
      });

      const buildingBaseConfig = {type: "Single", mesh: "buildingBase"};
      const buildingRoofConfig = {type: "Single", mesh: "buildingRoof"};
      const floorConfig = {type: "Stacked", mesh: "buildingFloor", count: "numberOfFloors"};
      const officeBaseConfig = {type: "Single", mesh: "officeBase"};
      const officeRoofConfig = {type: "Single", mesh: "officeRoof"};
      const officeFloorConfig = {type: "Stacked", mesh: "officeFloor", count: "numberOfFloors"};

      const officeTile = createTileSpec(
          "Office Building Tile",
          "BuildingResidential",
          true,
          {buildingBase: "Building_base.glb", buildingFloor: "Building_floor.glb", buildingRoof: "Building_roof.glb"},
          [buildingBaseConfig, floorConfig, buildingRoofConfig],
          5
      );
      const businessTile = createTileSpec(
          "OfficeTile",
          "BuildingOffice",
          true,
          {officeBase: "Office_base.glb", officeFloor: "Office_floor.glb", officeRoof: "Office_roof.glb"},
          [officeBaseConfig, officeFloorConfig, officeRoofConfig],
          5
      );
      const parkTile = createTileSpec(
          "Park tile",
          "Park",
          false,
          {picNic: "Pic-nic_table.glb"},
          null,
          25,
          ["tree1", "tree2", "tree3", "tree1", "tree2", "tree3", "tree1", "tree2", "tree3", "picNic"]
      );
      const waterTile = createTileSpec("Water tile", "Water", false, {}, null, 0);

      let res_instances = [];
      let park_instances = [];
      let water_instances = [];
      let bus_instances = [];

      for (const key in this.curr_sim.city.blocks) {
        let block = this.curr_sim.city.blocks[key];
        let values = key.split(',');
        let coordinate = {x: parseInt(values[0]), y: parseInt(values[1])};

        if (block.blockType === "RESIDENTIAL") {
          if (coordinate.x === 0 && coordinate.y === 0) {
            bus_instances.push({floorCount: block.floorCount, position: {x: coordinate.x, z: coordinate.y}});
          } else {
            res_instances.push({floorCount: block.floorCount, position: {x: coordinate.x, z: coordinate.y}});
          }
        } else if (block.blockType === "PARK") {
          park_instances.push({position: {x: coordinate.x, z: coordinate.y}});
        } else if (block.blockType === "WATER") {
          water_instances.push({position: {x: coordinate.x, z: coordinate.y}});
        }
      }

      this.citySpec = {
        id: this.curr_sim.id,
        name: this.curr_sim.name,
        radius: this.curr_sim.city.size,
        tileSpecs: [officeTile, businessTile, parkTile, waterTile],
        tileInstanceGroups: [
          {tileSpecName: officeTile.name, instances: res_instances},
          {tileSpecName: parkTile.name, instances: park_instances},
          {tileSpecName: waterTile.name, instances: water_instances},
          {tileSpecName: businessTile.name, instances: bus_instances}
        ],
      };
    },
  },
};
</script>

<style>
.dot {
  transition: transform 0.3s ease-in-out;
}

input:checked ~ .dot {
  transform: translateX(24px);
}
</style>
