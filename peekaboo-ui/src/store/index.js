import { createPinia } from "pinia";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";
import useUserStore from "./modules/user";
import useAppStore from "./modules/app";
import useTagStore from "./modules/tag";
import useKeepAliveStore from "./modules/keepAlive";
import useIframeStore from "./modules/iframe";
import useConfigStore from "./modules/config";
import useMessageStore from "./modules/message";
import useFormStore from "./modules/form";
import useDictStore from "./modules/dict";

const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);

export {
  useUserStore,
  useAppStore,
  useTagStore,
  useKeepAliveStore,
  useIframeStore,
  useConfigStore,
  useMessageStore,
  useFormStore,
  useDictStore,
};
export default pinia;
