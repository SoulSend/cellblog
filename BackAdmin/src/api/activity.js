import request from '@/utils/request';
// 发布活动
export function publish(activityData) {
  return request({
    url: '/activity/publish',
    method: 'post',
    data: activityData
  });
}
// 获取活动列表
export function fetchActivities() {
  return request({
    url: '/activity',
    method: 'get'
  });
}

// 删除活动
export function removeActivity(id) {
  return request({
    url: `/activity/delete/${id}`,
    method: 'delete'
  });
}

// 更新活动信息
export function updateActivity(activityData) {
  return request({
    url: '/activity/update',
    method: 'put',
    data: activityData
  });
}
// 获取活动详情
export function getActivityDetail(id) {
  return request({
    url: `/activity/detail/${id}`,
    method: 'get'
  });
}