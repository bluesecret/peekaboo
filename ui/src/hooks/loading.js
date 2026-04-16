/*
 * @Author: wangyuanjie
 * @Date: 2024-03-28 17:38:01
 * @LastEditTime: 2024-03-28 17:38:02
 * @LastEditors: wangyuanjie
 */
import { ref } from "vue";

export default function useLoading(initValue = false) {
  const loading = ref(initValue);
  const setLoading = (value) => {
    loading.value = value;
  };
  const toggle = () => {
    loading.value = !loading.value;
  };
  return {
    loading,
    setLoading,
    toggle,
  };
}
