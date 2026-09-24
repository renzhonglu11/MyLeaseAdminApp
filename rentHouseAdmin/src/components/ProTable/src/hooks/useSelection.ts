
import { computed, ref } from 'vue'
/**
 * @description 表格多选数据操作
 * @param {String} rowKey 当表格可以多选时，所指定的 id
 * */
export const useSelection = (rowKey = 'id') => {
  const isSelected = ref<boolean>(false)
  const selectedList = ref([])
  const selectedListIds = computed((): string[] => {
    const ids: string[] = []
    selectedList.value.forEach((item) => {
      ids.push(item[rowKey])
    })
    return ids
  })

  /**
   * @description 多选操作
   * @param {Array} rowArr 当前选择的所有数据
   * @return void
   */
  const selectionChange = (rowArr: any) => {
    console.log(rowArr)

    rowArr.length === 0 ? (isSelected.value = false) : (isSelected.value = true)
    selectedList.value = rowArr
  }

  return {
    isSelected,
    selectedList,
    selectedListIds,
    selectionChange,
  }
}
