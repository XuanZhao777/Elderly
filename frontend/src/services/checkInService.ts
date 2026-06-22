import axiosInstance from './api';

export interface CheckInResponse {
  checkInId: string;
  pointsEarned: number;
  message: string;
}

const checkInService = {
  // 开始打卡
  startCheckIn: (elderlyId: string) =>
    axiosInstance.post(`/checkin/start/${elderlyId}`),

  // 完成打卡
  completeCheckIn: (checkInId: string, durationMinutes: number, feedback: string) =>
    axiosInstance.post(`/checkin/${checkInId}/complete`, null, {
      params: { durationMinutes, feedback },
    }),

  // 获取打卡历史
  getCheckInHistory: (page: number = 0, size: number = 20) =>
    axiosInstance.get('/checkin/history', {
      params: { page, size },
    }),

  // 获取志愿时长
  getTotalVolunteerHours: () =>
    axiosInstance.get('/checkin/hours'),
};

export default checkInService;
