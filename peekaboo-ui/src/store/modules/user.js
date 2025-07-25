import { defineStore } from "pinia";
import loginApi from "@/api/login";
import tool from "@/utils/tool";
import router from "@/router";
import webRouter from "@/router/webRouter";
import { homePage } from "@/router/homePageRoutes";
import { useAppStore, useTagStore, useDictStore } from "@/store";

const useUserStore = defineStore("user", {
  state: () => ({
    codes: undefined,
    roles: undefined,
    routers: undefined,
    user: undefined,
    menus: undefined,
  }),

  getters: {
    setUserInfo(state) {
      return { ...state };
    },
  },

  actions: {
    setToken(token) {
      tool.local.set(import.meta.env.VITE_APP_TOKEN_PREFIX, token);
    },

    getToken() {
      return tool.local.get(import.meta.env.VITE_APP_TOKEN_PREFIX);
    },

    clearToken() {
      tool.local.remove(import.meta.env.VITE_APP_TOKEN_PREFIX);
    },

    setInfo(data) {
      this.$patch(data);
    },

    resetUserInfo() {
      this.$reset();
    },

    setMenu(data) {
      const routers = flatAsyncRoutes(filterAsyncRouter(data));
      console.log(routers);
      routers.map((item) => router.addRoute("layout", item));
    },

    requestUserInfo() {
      const useDict = useDictStore();
      return new Promise((resolve, reject) => {
        loginApi
          .getInfo()
          .then(async (response) => {
            if (!response || !response.data) {
              this.clearToken();
              await router.push({ name: "login" });
              reject(false);
            } else {
              this.setInfo(formatUserInfo(response.data));
              homePage.children = webRouter[0].children;
              this.setMenu(this.routers);
              this.routers = removeButtonMenu(this.routers);
              this.routers.unshift(homePage);
              // await this.setApp();
              const flag = await useDict.requestDictList();
              if (flag) {
                resolve(response.data);
              } else {
                reject(false);
              }
            }
          })
          .catch(async (err) => {
            this.clearToken();
            await router.push({ name: "login" });
            reject(false);
          });
      });
    },

    login(form) {
      return loginApi
        .login(form)
        .then(async (r) => {
          if (r.code == "0") {
            this.setToken(r.data);
            return true;
          } else {
            return false;
          }
        })
        .catch((e) => {
          console.error(e);
          return false;
        });
    },

    async logout() {
      await loginApi.logout();
      const tagStore = useTagStore();
      tool.local.remove("tags");
      tagStore.clearTags();
      this.clearToken();
      this.resetUserInfo();
    },

    async setApp() {
      const appStore = useAppStore();
      const setting =
        typeof this.user.backend_setting === "string"
          ? JSON.parse(this.user.backend_setting)
          : this.user.backend_setting;
      if (setting) {
        appStore.toggleMode(setting.mode);
        appStore.toggleMenu(setting.menuCollapse);
        appStore.toggleTag(setting.tag);
        appStore.changeMenuWidth(setting.menuWidth);
        appStore.changeLayout(setting.layout);
        appStore.useSkin(setting.skin);
        appStore.changeColor(setting.color);
      }
    },
  },
});

//路由扁平化
const flatAsyncRoutes = (routes, breadcrumb = []) => {
  let res = [];
  routes.forEach((route) => {
    const tmp = { ...route };
    if (tmp.children) {
      let childrenBreadcrumb = [...breadcrumb];
      childrenBreadcrumb.push(route);
      let tmpRoute = { ...route };
      tmpRoute.meta.breadcrumb = childrenBreadcrumb;
      delete tmpRoute.children;
      res.push(tmpRoute);
      let childrenRoutes = flatAsyncRoutes(tmp.children, childrenBreadcrumb);
      childrenRoutes.map((item) => {
        res.push(item);
      });
    } else {
      let tmpBreadcrumb = [...breadcrumb];
      tmpBreadcrumb.push(tmp);
      tmp.meta.breadcrumb = tmpBreadcrumb;
      res.push(tmp);
    }
  });
  return res;
};
const formatUserInfo = (userInfo) => {
  const userInfoData = {};
  userInfoData.user = {
    username: userInfo.username,
    id: userInfo.userId,
    user_type: "100",
    nickname: userInfo.realname,
    phone: userInfo.phone,
    email: userInfo.email,
    avatar: userInfo.avatar,
    remark: userInfo.remark,
    dashboard: "statistics",
    deptname: userInfo.deptname,
    deptId: userInfo.deptId,
  };
  userInfoData.roles = userInfo.roles.map((item) => {
    return item.code;
  });
  userInfoData.codes = userInfo.auth_key;
  userInfoData.routers = formatRouter(userInfo.menuList);
  return userInfoData;
};

const formatRouter = (router) => {
  return router.map((item) => {
    item.parent_id = item.parentId;
    item.componentName = item.redirect;
    item.name = item.permission;
    item.meta = {};
    if (item.meteInfo.type == "1") {
      item.meta.type = "M";
    } else if (item.meteInfo.type == "2") {
      item.meta.type = "B";
    } else if (item.meteInfo.type == "3") {
      item.meta.type = "L";
    } else {
      item.meta.type = "I";
    }
    item.meta.title = item.meteInfo.name;
    item.meta.icon = item.meteInfo.icon;
    if (item.meteInfo.visible == 0) {
      item.meta.hidden = false;
      item.meta.hiddenBreadcrumb = false;
    } else {
      item.meta.hidden = true;
      item.meta.hiddenBreadcrumb = true;
    }
    item.meta.keepAlive = item.meteInfo.keepAlive == 1 ? false : true;
    delete item.meteInfo;
    delete item.componentName;
    delete item.parentId;
    delete item.permission;
    if (item.children) {
      item.children = formatRouter(item.children);
    }
    return item;
  });
};

const views = import.meta.glob("../../views/**/**.vue");
const empty = import.meta.glob("../../layout/empty.vue");

// 菜单转换路由
const filterAsyncRouter = (routerMap) => {
  const accessedRouters = [];
  routerMap.forEach((item) => {
    if (item.meta.type !== "B") {
      if (item.meta.type === "I") {
        item.meta.url = item.path;
        item.path = `/maIframe/${item.name}`;
      }

      const route = {
        path: item.path,
        name: item.name,
        hidden: item.hidden === 1,
        meta: item.meta,
        children: item.children ? filterAsyncRouter(item.children) : null,
        component: views[`../../views/${item.component}.vue`],
      };
      accessedRouters.push(route);
    }
  });
  return accessedRouters;
};

// 去除按钮菜单
const removeButtonMenu = (routers) => {
  let handlerAfterRouters = [];
  routers.forEach((item) => {
    if (item.meta.type !== "B" && !item.meta.hidden) {
      let route = item;
      if (item.children && item.children.length > 0) {
        route.children = removeButtonMenu(item.children);
      }
      handlerAfterRouters.push(route);
    }
  });
  return handlerAfterRouters;
};
export default useUserStore;
