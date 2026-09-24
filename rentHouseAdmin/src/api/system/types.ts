
export interface PageResponseInterface<T> {
  records: T
  total: number
  size: number
  current: number
  pages: number
}
export interface ReqPage {
  pageNum: number
  pageSize: number
}
export interface SysUserListParams extends ReqPage {
  name?: string
  phone?: number
}
export interface SysUserInterfaceRes {
  id?: number
  username?: string
  password?: string
  name?: string
  phone?: string
  postId?: number
  status?: number
  postName?: string
  type?: 0
  avatarUrl?: string
  additionalInfo?: string
}

export interface Role {
  id: number
  createTime: string | null
  updateTime?: string | null
  isDeleted?: number | null
  param?: Record<string, any>
  name: string
  code: string
  description: string | null
  selected?: boolean
}
// 岗位接口
export interface PostInterfacesRes {
  id: number
  postCode: string
  name: string
  description: string
  status: number
  createTime?: string
  isDeleted?: number
  param?: object
  updateTime?: string
}
// 部门接口
export interface DeptInterfacesRes {
  id: number
  createTime: string
  name: string
  parentId: number
  treePath: string
  sortValue: number
  leader: string
  phone: string
  status: number
  isDeleted?: number
  param?: object
  children: DeptInterfacesRes[]
}
export interface UserRolesListInterfaceRes {
  allRolesList: Role[]
  assignRoles: Role[]
}
export interface AssignRolesInterfaceReq {
  userId: string
  roleIdList: string[]
}
export type RoleListParamsInterfaceReq = ReqPage
// 权限列表
export interface PermissionListInterfaceRes {
  children?: PermissionListInterfaceRes[]
  id?: number | null
  createTime?: string
  updateTime?: string
  isDeleted?: number
  param?: object
  parentId?: number
  name?: string | null
  type?: number
  path?: string | null
  component?: string | null
  perms?: string
  icon?: string | null
  sortValue?: number
  status?: number
  activeMenu?: string
  isHide?: boolean | 0 | 1
  selected?: boolean
}
// 分配权限列表请求
export interface AssignPermissionInterfaceReq {
  roleId: number | string
  menuIdList: number[]
}
// 岗位搜索
export interface SysPostListParamsInterfaceReq extends ReqPage {
  name?: string
  postCode?: number
  status?: number
}
// 操作日志请求接口
export interface SysOperationLogListParamsInterfaceReq extends ReqPage {
  title?: string
  operName?: string
  operatingTime?: [createTimeBegin: string, createTimeEnd: string]
}
// 操作日志响应接口
export interface SysOperationLogInterfaceRes {
  id: number
  createTime: string
  title: string
  businessType: string
  method: string
  requestMethod: string
  operatorType: string
  operName: string
  deptName: string
  operUrl: string
  operIp: string
  operParam: string
  jsonResult: string
  status: number
  errorMsg: string
  operTime: Date | null
}
// 登录日志请求接口
export interface SysLoginLogListParamsInterfaceReq extends ReqPage {
  username?: string
  operatingTime?: [createTimeBegin: string, createTimeEnd: string]
}
// 登录日志响应接口
export interface SysLoginLogInterfaceRes {
  id: number
  createTime: string
  updateTime: string | null
  isDeleted: number
  param: object
  username: string
  ipaddr: string
  status: number
  msg: string
  accessTime: string | null
}
