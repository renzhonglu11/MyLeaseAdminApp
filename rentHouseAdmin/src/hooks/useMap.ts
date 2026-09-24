// 高德地图hooks
import AMapLoader from '@amap/amap-jsapi-loader'
import { AMAP_MAP_KEY, AMAP_MAP_SECRET_KEY } from '@/config/config'
import { ElMessage } from 'element-plus'
import { onMounted, onUnmounted, ref } from 'vue'

export const useMap = ({ domId }: { domId?: string } = {}) => {
  const AMap = ref()
  const map = ref()
  const isMapReady = ref(false)
  async function initMap() {
    if (!AMAP_MAP_KEY || !AMAP_MAP_SECRET_KEY) {
      ElMessage.error(
        'AMap key is missing. Please set VITE_AMAP_MAP_KEY and VITE_AMAP_MAP_SECRET_KEY.',
      )
      return
    }
    try {
      ;(window as any)._AMapSecurityConfig = {
        securityJsCode: AMAP_MAP_SECRET_KEY,
        // serviceHost: AMAP_MAP_SERVICE_HOST,
      }
      AMap.value = await AMapLoader.load({
        key: AMAP_MAP_KEY, // 申请好的Web端开发者Key，首次调用 load 时必填
        version: '2.0', // 指定要加载的 JS API 的版本，缺省时默认为 1.4.15
        plugins: [], // 需要使用的的插件列表，如比例尺'AMap.Scale'等
      })
      map.value = domId && new AMap.value.Map(domId)
      isMapReady.value = true
    } catch (error) {
      console.log(error)
      const mapError = error as any
      const message = mapError?.info
        ? `${mapError.info}${
            mapError.infocode ? ` (${mapError.infocode})` : ''
          }`
        : mapError?.message || 'AMap init failed'
      ElMessage.error(message)
      isMapReady.value = false
    }
  }
  function destroyMap() {
    map.value && map.value.destroy()
  }
  onMounted(() => {
    initMap()
  })
  onUnmounted(() => {
    destroyMap()
  })
  return {
    map,
    AMap,
    isMapReady,
  }
}
