<template>
  <div class="vue3-cron-div">
    <a-tabs type="card">
      <a-tab-pane key="1">
        <template #title>
          <div class="tab-title">
            <icon-calendar /> {{ state.text.Seconds.name }}
          </div>
        </template>
        <div class="tab-container">
          <a-radio-group v-model="state.second.cronEvery" direction="vertical">
            <a-radio value="1">
              {{ state.text.Seconds.every }}
            </a-radio>
            <a-radio value="2">
              {{ state.text.Seconds.interval[0] }}
              <a-input-number
                v-model="state.second.incrementIncrement"
                mode="button"
                size="medium"
                :min="1"
                :max="60"
                class="w-110"
              ></a-input-number>
              {{ state.text.Seconds.interval[1] || "" }}
              <a-input-number
                v-model="state.second.incrementStart"
                mode="button"
                size="medium"
                :min="0"
                :max="59"
                class="w-110"
              ></a-input-number>
              {{ state.text.Seconds.interval[2] || "" }}
            </a-radio>
            <a-radio value="3">
              {{ state.text.Seconds.specific }}
              <a-select
                v-model="state.second.specificSpecific"
                size="medium"
                allow-search
                multiple
                class="w-350"
              >
                <a-option
                  v-for="(val, index) in 60"
                  :key="index"
                  :value="val - 1"
                  >{{ val - 1 }}</a-option
                >
              </a-select>
            </a-radio>
            <a-radio value="4">
              {{ state.text.Seconds.cycle[0] }}
              <a-input-number
                v-model="state.second.rangeStart"
                mode="button"
                size="medium"
                :min="1"
                :max="60"
                class="w-110"
              ></a-input-number>
              {{ state.text.Seconds.cycle[1] || "" }}
              <a-input-number
                v-model="state.second.rangeEnd"
                mode="button"
                size="medium"
                :min="0"
                :max="59"
                class="w-110"
              ></a-input-number>
              {{ state.text.Seconds.cycle[2] || "" }}
            </a-radio>
          </a-radio-group>
        </div>
      </a-tab-pane>
      <a-tab-pane key="2">
        <template #title>
          <div class="tab-title">
            <icon-calendar /> {{ state.text.Minutes.name }}
          </div>
        </template>
        <div class="tab-container">
          <a-radio-group v-model="state.minute.cronEvery" direction="vertical">
            <a-radio value="1">
              {{ state.text.Minutes.every }}
            </a-radio>
            <a-radio value="2">
              {{ state.text.Minutes.interval[0] }}
              <a-input-number
                v-model="state.minute.incrementIncrement"
                mode="button"
                size="medium"
                :min="1"
                :max="60"
                class="w-110"
              ></a-input-number>
              {{ state.text.Minutes.interval[1] || "" }}
              <a-input-number
                v-model="state.minute.incrementStart"
                mode="button"
                size="medium"
                :min="0"
                :max="59"
                class="w-110"
              ></a-input-number>
              {{ state.text.Minutes.interval[2] || "" }}
            </a-radio>
            <a-radio value="3">
              {{ state.text.Minutes.specific }}
              <a-select
                v-model="state.minute.specificSpecific"
                size="medium"
                allow-search
                multiple
                class="w-350"
              >
                <a-option
                  v-for="(val, index) in 60"
                  :key="index"
                  :value="val - 1"
                  >{{ val - 1 }}</a-option
                >
              </a-select>
            </a-radio>
            <a-radio value="4">
              {{ state.text.Minutes.cycle[0] }}
              <a-input-number
                v-model="state.minute.rangeStart"
                mode="button"
                size="medium"
                :min="1"
                :max="60"
                class="w-110"
              ></a-input-number>
              {{ state.text.Minutes.cycle[1] }}
              <a-input-number
                v-model="state.minute.rangeEnd"
                mode="button"
                size="medium"
                :min="0"
                :max="59"
                class="w-110"
              ></a-input-number>
              {{ state.text.Minutes.cycle[2] }}
            </a-radio>
          </a-radio-group>
        </div>
      </a-tab-pane>
      <a-tab-pane key="3">
        <template #title>
          <div class="tab-title">
            <icon-calendar /> {{ state.text.Hours.name }}
          </div>
        </template>
        <div class="tab-container">
          <a-radio-group v-model="state.hour.cronEvery" direction="vertical">
            <a-radio value="1">
              {{ state.text.Hours.every }}
            </a-radio>
            <a-radio value="2">
              {{ state.text.Hours.interval[0] }}
              <a-input-number
                v-model="state.hour.incrementIncrement"
                mode="button"
                size="medium"
                :min="0"
                :max="23"
                class="w-110"
              ></a-input-number>
              {{ state.text.Hours.interval[1] || "" }}
              <a-input-number
                v-model="state.hour.incrementStart"
                mode="button"
                size="medium"
                :min="0"
                :max="23"
                class="w-110"
              ></a-input-number>
              {{ state.text.Hours.interval[2] || "" }}
            </a-radio>
            <a-radio value="3">
              {{ state.text.Hours.specific }}
              <a-select
                v-model="state.hour.specificSpecific"
                size="medium"
                allow-search
                multiple
                class="w-350"
              >
                <a-option
                  v-for="(val, index) in 24"
                  :key="index"
                  :value="val - 1"
                  >{{ val - 1 }}</a-option
                >
              </a-select>
            </a-radio>
            <a-radio value="4">
              {{ state.text.Hours.cycle[0] }}
              <a-input-number
                v-model="state.hour.rangeStart"
                mode="button"
                size="medium"
                :min="0"
                :max="23"
                class="w-110"
              ></a-input-number>
              {{ state.text.Hours.cycle[1] || "" }}
              <a-input-number
                v-model="state.hour.rangeEnd"
                mode="button"
                size="medium"
                :min="0"
                :max="23"
                class="w-110"
              ></a-input-number>
              {{ state.text.Hours.cycle[2] || "" }}
            </a-radio>
          </a-radio-group>
        </div>
      </a-tab-pane>
      <a-tab-pane key="4">
        <template #title>
          <div class="tab-title">
            <icon-calendar /> {{ state.text.Day.name }}
          </div>
        </template>
        <div class="tab-container">
          <a-radio-group v-model="state.day.cronEvery" direction="vertical">
            <a-radio value="1">
              {{ state.text.Day.every }}
            </a-radio>
            <a-radio value="2">
              {{ state.text.Day.intervalWeek[0] }}
              <a-input-number
                v-model="state.week.incrementIncrement"
                mode="button"
                size="medium"
                :min="1"
                :max="7"
                class="w-110"
              ></a-input-number>
              {{ state.text.Day.intervalWeek[1] }}
              <a-select
                v-model="state.week.incrementStart"
                size="medium"
                class="w-192"
              >
                <a-option
                  v-for="(val, index) in 7"
                  :key="index"
                  :label="state.text.Week[val - 1]"
                  :value="val"
                ></a-option>
              </a-select>
              {{ state.text.Day.intervalWeek[2] }}
            </a-radio>
            <a-radio value="3">
              {{ state.text.Day.intervalDay[0] }}
              <a-input-number
                v-model="state.day.incrementIncrement"
                mode="button"
                size="medium"
                :min="1"
                :max="31"
                class="w-110"
              ></a-input-number>
              {{ state.text.Day.intervalDay[1] }}
              <a-input-number
                v-model="state.day.incrementStart"
                mode="button"
                size="medium"
                :min="1"
                :max="31"
                class="w-110"
              ></a-input-number>
              {{ state.text.Day.intervalDay[2] }}
            </a-radio>
            <a-radio value="4">
              {{ state.text.Day.specificWeek }}
              <a-select
                v-model="state.week.specificSpecific"
                size="medium"
                allow-search
                multiple
                class="w-350"
              >
                <a-option
                  v-for="(val, index) in 7"
                  :key="index"
                  :label="state.text.Week[val - 1]"
                  :value="
                    ['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT'][val - 1]
                  "
                ></a-option>
              </a-select>
            </a-radio>
            <a-radio value="5">
              {{ state.text.Day.specificDay }}
              <a-select
                v-model="state.day.specificSpecific"
                size="medium"
                allow-search
                multiple
                class="w-350"
              >
                <a-option
                  v-for="(val, index) in 31"
                  :key="index"
                  :value="val"
                  >{{ val }}</a-option
                >
              </a-select>
            </a-radio>
            <a-radio value="6">
              {{ state.text.Day.lastDay }}
            </a-radio>
            <a-radio value="7">
              {{ state.text.Day.lastWeekday }}
            </a-radio>
            <a-radio value="8">
              {{ state.text.Day.lastWeek[0] }}
              <a-select
                v-model="state.day.cronLastSpecificDomDay"
                size="medium"
                allow-search
                class="w-192"
              >
                <a-option
                  v-for="(val, index) in 7"
                  :key="index"
                  :label="state.text.Week[val - 1]"
                  :value="val"
                ></a-option>
              </a-select>
            </a-radio>
            <a-radio value="9">
              <a-input-number
                v-model="state.day.cronDaysBeforeEomMinus"
                mode="button"
                size="medium"
                :min="1"
                :max="31"
                class="w-110"
              ></a-input-number>
              {{ state.text.Day.beforeEndMonth[0] }}
            </a-radio>
            <a-radio value="10">
              {{ state.text.Day.nearestWeekday[0] }}
              <a-input-number
                v-model="state.day.cronDaysNearestWeekday"
                mode="button"
                size="medium"
                :min="1"
                :max="31"
                class="w-110"
              ></a-input-number>
              {{ state.text.Day.nearestWeekday[1] }}
            </a-radio>
            <a-radio value="11">
              {{ state.text.Day.someWeekday[0] }}
              <a-input-number
                v-model="state.week.cronNthDayNth"
                mode="button"
                size="medium"
                :min="1"
                :max="5"
                class="w-110"
              ></a-input-number>
              {{ state.text.Day.someWeekday[1] }}
              <a-select
                v-model="state.week.cronNthDayDay"
                size="medium"
                allow-search
                class="w-192"
              >
                <a-option
                  v-for="(val, index) in 7"
                  :key="index"
                  :label="state.text.Week[val - 1]"
                  :value="val"
                ></a-option>
              </a-select>
            </a-radio>
          </a-radio-group>
        </div>
      </a-tab-pane>
      <a-tab-pane key="5">
        <template #title>
          <div class="tab-title">
            <icon-calendar /> {{ state.text.Month.name }}
          </div>
        </template>
        <div class="tab-container">
          <a-radio-group v-model="state.month.cronEvery" direction="vertical">
            <a-radio value="1">
              {{ state.text.Month.every }}
            </a-radio>
            <a-radio value="2">
              {{ state.text.Month.interval[0] }}
              <a-input-number
                v-model="state.month.incrementIncrement"
                mode="button"
                size="medium"
                :min="0"
                :max="12"
                class="w-110"
              ></a-input-number>
              {{ state.text.Month.interval[1] || "" }}
              <a-input-number
                v-model="state.month.incrementStart"
                mode="button"
                size="medium"
                :min="0"
                :max="12"
                class="w-110"
              ></a-input-number>
              {{ state.text.Month.interval[2] || "" }}
            </a-radio>
            <a-radio value="3">
              {{ state.text.Month.specific }}
              <a-select
                v-model="state.month.specificSpecific"
                size="medium"
                allow-search
                multiple
                class="w-350"
              >
                <a-option
                  v-for="(val, index) in 12"
                  :key="index"
                  :value="val"
                  >{{ val }}</a-option
                >
              </a-select>
            </a-radio>
            <a-radio value="4">
              {{ state.text.Month.cycle[0] }}
              <a-input-number
                v-model="state.month.rangeStart"
                mode="button"
                size="medium"
                :min="1"
                :max="12"
                class="w-110"
              ></a-input-number>
              {{ state.text.Month.cycle[1] }}
              <a-input-number
                v-model="state.month.rangeEnd"
                mode="button"
                size="medium"
                :min="1"
                :max="12"
                class="w-110"
              ></a-input-number>
              {{ state.text.Month.cycle[2] }}
            </a-radio>
          </a-radio-group>
        </div>
      </a-tab-pane>
      <a-tab-pane key="6">
        <template #title>
          <div class="tab-title">
            <icon-calendar /> {{ state.text.Year.name }}
          </div>
        </template>
        <div class="tab-container">
          <a-radio-group v-model="state.year.cronEvery" direction="vertical">
            <a-radio value="1">
              {{ state.text.Year.every }}
            </a-radio>
            <a-radio value="2">
              {{ state.text.Year.interval[0] }}
              <a-input-number
                v-model="state.year.incrementIncrement"
                mode="button"
                size="medium"
                :min="1"
                :max="99"
                class="w-110"
              ></a-input-number>
              {{ state.text.Year.interval[1] || "" }}
              <a-input-number
                v-model="state.year.incrementStart"
                mode="button"
                size="medium"
                :min="2018"
                :max="2118"
                class="w-110"
              ></a-input-number>
              {{ state.text.Year.interval[2] || "" }}
            </a-radio>
            <a-radio value="3">
              {{ state.text.Year.specific }}
              <a-select
                v-model="state.year.specificSpecific"
                size="medium"
                allow-search
                multiple
                class="w-350"
              >
                <a-option
                  v-for="(val, index) in 100"
                  :key="index"
                  :value="val + 2017"
                  >{{ val + 2017 }}</a-option
                >
              </a-select>
            </a-radio>
            <a-radio value="4">
              {{ state.text.Year.cycle[0] }}
              <a-input-number
                v-model="state.year.rangeStart"
                mode="button"
                size="medium"
                :min="2018"
                :max="2118"
                class="w-110"
              ></a-input-number>
              {{ state.text.Year.cycle[1] || "" }}
              <a-input-number
                v-model="state.year.rangeEnd"
                mode="button"
                size="medium"
                :min="2018"
                :max="2118"
                class="w-110"
              ></a-input-number>
              {{ state.text.Year.cycle[2] || "" }}
            </a-radio>
          </a-radio-group>
        </div>
      </a-tab-pane>
    </a-tabs>
    <div class="bottom">
      <div class="value">
        <span> cron预览: </span>
        <a-tag type="primary" color="blue" size="large">
          {{ state.cron }}
        </a-tag>
      </div>
      <div class="buttonDiv mt-10">
        <a-button type="primary" class="mr-28" @click.stop="handleChange">{{
          state.text.Save
        }}</a-button>
        <a-button type="primary" @click="close">{{
          state.text.Close
        }}</a-button>
      </div>
    </div>
  </div>
</template>

<script>
import { reactive, computed, defineComponent, onMounted } from "vue";
import Language from "./language";

export default defineComponent({
  name: "VueCron",
  props: {
    cronValue: {
      type: Object,
      default: () => ({}),
    },
  },
  emits: ["close", "changeCron"],
  setup(props, context) {
    const state = reactive({
      language: "cn",
      second: {
        cronEvery: "1",
        incrementStart: 3,
        incrementIncrement: 5,
        rangeStart: 1,
        rangeEnd: 0,
        specificSpecific: [],
      },
      minute: {
        cronEvery: "1",
        incrementStart: 3,
        incrementIncrement: 5,
        rangeStart: 1,
        rangeEnd: 0,
        specificSpecific: [],
      },
      hour: {
        cronEvery: "1",
        incrementStart: 3,
        incrementIncrement: 5,
        rangeStart: 0,
        rangeEnd: 0,
        specificSpecific: [],
      },
      day: {
        cronEvery: "1",
        incrementStart: 1,
        incrementIncrement: 1,
        rangeStart: 0,
        rangeEnd: 0,
        specificSpecific: [],
        cronLastSpecificDomDay: 1,
        cronDaysBeforeEomMinus: 1,
        cronDaysNearestWeekday: 1,
      },
      week: {
        cronEvery: "1",
        incrementStart: 1,
        incrementIncrement: 1,
        specificSpecific: [],
        cronNthDayDay: 1,
        cronNthDayNth: 1,
      },
      month: {
        cronEvery: "1",
        incrementStart: 3,
        incrementIncrement: 5,
        rangeStart: 0,
        rangeEnd: 0,
        specificSpecific: [],
      },
      year: {
        cronEvery: "1",
        incrementStart: 2017,
        incrementIncrement: 1,
        rangeStart: 0,
        rangeEnd: 0,
        specificSpecific: [],
      },
      output: {
        second: "",
        minute: "",
        hour: "",
        day: "",
        month: "",
        Week: "",
        year: "",
      },
      text: computed(() => {
        if (Language instanceof Array) {
          return Language[state.language];
        }
        return Language.cn;
      }),
      secondsText: computed(() => {
        let seconds = "";
        const { cronEvery } = state.second;
        switch (cronEvery.toString()) {
          case "1":
            seconds = "*";
            break;
          case "2":
            seconds = `${state.second.incrementStart}/${state.second.incrementIncrement}`;
            break;
          case "3":
            state.second.specificSpecific.forEach((val) => {
              seconds += `${val},`;
            });
            seconds = seconds.slice(0, -1);
            break;
          case "4":
            seconds = `${state.second.rangeStart}-${state.second.rangeEnd}`;
            break;
          default:
        }
        return seconds;
      }),
      minutesText: computed(() => {
        let minutes = "";
        const { cronEvery } = state.minute;
        switch (cronEvery.toString()) {
          case "1":
            minutes = "*";
            break;
          case "2":
            minutes = `${state.minute.incrementStart}/${state.minute.incrementIncrement}`;
            break;
          case "3":
            state.minute.specificSpecific.forEach((val) => {
              minutes += `${val},`;
            });
            minutes = minutes.slice(0, -1);
            break;
          case "4":
            minutes = `${state.minute.rangeStart}-${state.minute.rangeEnd}`;
            break;
          default:
        }
        return minutes;
      }),
      hoursText: computed(() => {
        let hours = "";
        const { cronEvery } = state.hour;
        switch (cronEvery.toString()) {
          case "1":
            hours = "*";
            break;
          case "2":
            hours = `${state.hour.incrementStart}/${state.hour.incrementIncrement}`;
            break;
          case "3":
            state.hour.specificSpecific.forEach((val) => {
              hours += `${val},`;
            });
            hours = hours.slice(0, -1);
            break;
          case "4":
            hours = `${state.hour.rangeStart}-${state.hour.rangeEnd}`;
            break;
          default:
        }
        return hours;
      }),
      daysText: computed(() => {
        let days = "";
        const { cronEvery } = state.day;
        switch (cronEvery.toString()) {
          case "1":
            break;
          case "2":
          case "4":
          case "8":
          case "11":
            days = "?";
            break;
          case "3":
            days = `${state.day.incrementStart}/${state.day.incrementIncrement}`;
            break;
          case "5":
            state.day.specificSpecific.forEach((val) => {
              days += `${val},`;
            });
            days = days.slice(0, -1);
            break;
          case "6":
            days = "L";
            break;
          case "7":
            days = "LW";
            break;
          // case '8':
          //   days = `${state.day.cronLastSpecificDomDay}L`
          //   break
          case "9":
            days = `L-${state.day.cronDaysBeforeEomMinus}`;
            break;
          case "10":
            days = `${state.day.cronDaysNearestWeekday}W`;
            break;
          default:
        }
        return days;
      }),
      weeksText: computed(() => {
        let weeks = "";
        const { cronEvery } = state.day;
        switch (cronEvery.toString()) {
          case "2":
            weeks = `${state.week.incrementStart}/${state.week.incrementIncrement}`;
            break;
          case "4":
            state.week.specificSpecific.forEach((val) => {
              weeks += `${val},`;
            });
            weeks = weeks.slice(0, -1);
            break;
          case "1":
          case "3":
          case "5":
          case "6":
          case "7":
          case "9":
          case "10":
            weeks = "?";
            break;
          case "8":
            weeks = `${state.day.cronLastSpecificDomDay}L`;
            break;
          case "11":
            weeks = `${state.week.cronNthDayDay}#${state.week.cronNthDayNth}`;
            break;
          default:
        }
        return weeks;
      }),
      monthsText: computed(() => {
        let months = "";
        const { cronEvery } = state.month;
        switch (cronEvery.toString()) {
          case "1":
            months = "*";
            break;
          case "2":
            months = `${state.month.incrementStart}/${state.month.incrementIncrement}`;
            break;
          case "3":
            state.month.specificSpecific.forEach((val) => {
              months += `${val},`;
            });
            months = months.slice(0, -1);
            break;
          case "4":
            months = `${state.month.rangeStart}-${state.month.rangeEnd}`;
            break;
          default:
        }
        return months;
      }),
      yearsText: computed(() => {
        let years = "";
        const { cronEvery } = state.year;
        switch (cronEvery.toString()) {
          case "1":
            years = "*";
            break;
          case "2":
            years = `${state.year.incrementStart}/${state.year.incrementIncrement}`;
            break;
          case "3":
            state.year.specificSpecific.forEach((val) => {
              years += `${val},`;
            });
            years = years.slice(0, -1);
            break;
          case "4":
            years = `${state.year.rangeStart}-${state.year.rangeEnd}`;
            break;
          default:
        }
        return years;
      }),
      cron: computed(() => {
        return `${state.secondsText || "*"} ${state.minutesText || "*"} ${
          state.hoursText || "*"
        } ${state.daysText || "*"} ${state.monthsText || "*"} ${
          state.weeksText || "?"
        } ${state.yearsText || "*"}`;
      }),
    });
    const getValue = () => {
      return state.cron;
    };
    const close = () => {
      context.emit("close");
    };
    const handleChange = () => {
      context.emit("changeCron", [state.cron, state]);
      close();
    };
    onMounted(() => {
      console.log(props.cronValue);
      if (props.cronValue.userSetting !== null) {
        Object.keys(props.cronValue.userSetting).forEach((item) => {
          if (
            ![
              "text",
              "secondsText",
              "minutesText",
              "hoursText",
              "daysText",
              "weeksText",
              "monthsText",
              "yearsText",
              "cron",
            ].includes(item)
          )
            state[item] = props.cronValue.userSetting[item];
        });
      }
    });
    return {
      state,
      getValue,
      close,
      handleChange,
    };
  },
});
</script>

<style>
.mt-10 {
  margin-top: 10px !important;
}

.mr-28 {
  margin-right: 28px !important;
}
.w-110 {
  width: 110px !important;
}
.w-192 {
  width: 192px !important;
}
.w-350 {
  width: 350px !important;
}
</style>

<style lang="less" scoped>
.vue3-cron-div {
  width: 700px;
  .tab-title {
    height: 30px;
    width: 45px;
    display: flex;
    gap: 4px;
    justify-content: center;
    align-items: center;
    font-size: 15px;
  }
  :deep(.arco-tabs-content) {
    padding-top: 0px !important;
  }
  :deep(.arco-radio) {
    margin-top: 10px;
  }
  :deep(.arco-radio .arco-radio-label) {
    display: flex;
    align-items: center;
    font-weight: bold;
    gap: 10px;
    flex-wrap: wrap;
  }
  :deep(.arco-radio-checked span) {
    color: #409eff;
  }
  .tab-container {
    height: 246px;
    max-height: 450px;
    padding: 10px;
    box-sizing: border-box;
    overflow-y: auto;
    &::-webkit-scrollbar {
      /*滚动条整体样式*/
      width: 5px; /*高宽分别对应横竖滚动条的尺寸*/
      height: 1px;
    }
    &::-webkit-scrollbar-thumb {
      /*滚动条里面小方块*/
      border-radius: 10px;
      background-color: skyblue;
      background-image: -webkit-linear-gradient(
        45deg,
        rgba(255, 255, 255, 0.2) 25%,
        transparent 25%,
        transparent 50%,
        rgba(255, 255, 255, 0.2) 50%,
        rgba(255, 255, 255, 0.2) 75%,
        transparent 75%,
        transparent
      );
    }
    &::-webkit-scrollbar-track {
      /*滚动条里面轨道*/
      box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
      background: #ededed;
      border-radius: 10px;
    }
  }
  .bottom {
    width: 100%;
    margin-top: 5px;
    display: flex;
    align-items: center;
    justify-content: space-around;
    .value {
      float: left;
      font-size: 14px;
      vertical-align: middle;
      span:nth-child(1) {
        color: red;
      }
    }
  }
}
</style>
