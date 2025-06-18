/*
 * @Author: wangyuanjie
 * @Date: 2024-04-01 18:17:50
 * @LastEditTime: 2024-05-28 14:10:25
 * @LastEditors: wangyuanjie
 */
import { useDictStore } from "@/store";

export default function useListFormat() {
  const useDict = useDictStore();
  const dictList = useDict.dict;

  const returnList = (type) => {
    return dictList[type];
  };
  const formatList = (type, value) => {
    const listArr = dictList[type];
    const item = listArr.find((item) => item.value == value);
    if (!item) return value;
    return item.label;
  };
  const formatColor = (type, value) => {
    const listArr = dictList[type];
    if (!isArrTree(listArr)) {
      const item = listArr.find(
        (item) => item.value == value && item.colorType
      );
      if (!item) return false;
      return item.colorType;
    } else {
      const arr = flattenTree(listArr);
      const item = arr.find((item) => item.value == value && item.colorType);
      if (!item) return false;
      return item.colorType;
    }
  };

  const flattenTree = (tree, result = []) => {
    tree.forEach((node) => {
      result.push({ ...node, children: undefined });
      if (node.children && node.children.length > 0) {
        flattenTree(node.children, result);
      }
    });
    return result;
  };

  const isArrTree = (data) => {
    if (!data || !Array.isArray(data)) {
      return false; // 如果不满足条件，直接返回 false，或者根据实际需求进行处理
    }

    try {
      // 使用 some 替代 forEach，以提高效率和表达意图
      return data.some((item) => {
        // 检查 item 是否具有 children 属性且不为空
        return item.children && item.children.length > 0;
      });
    } catch (error) {
      // 异常情况下返回 false 或者根据实际需求处理
      return false;
    }
  };
  return {
    returnList,
    formatList,
    formatColor,
  };
}
