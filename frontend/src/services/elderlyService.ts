import axiosInstance from './api';

export interface ElderlyUser {
  id: string;
  name: string;
  age: number;
  avatar: string;
  address: string;
  riskLevel: 'RED' | 'ORANGE' | 'GREEN';
  riskScore: number;
  distance?: number;
  supportersCount: number;
  daysWithoutCompanion: number;
  tags: string[];
  isFollowed: boolean;
}

export interface CheckInRequest {
  elderlyId: string;
  durationMinutes: number;
  feedback: string;
  photos?: File[];
}

const elderlyService = {
  // 获取附近的老人
  getNearbyElderly: (latitude: number, longitude: number, radius: number = 10) =>
    axiosInstance.get('/elderly/nearby', {
      params: { latitude, longitude, radius },
    }),

  // 获取所有老人
  getAllElderly: (page: number = 0, size: number = 20) =>
    axiosInstance.get('/elderly', {
      params: { page, size },
    }),

  // 获取老人详情
  getElderlyById: (id: string) =>
    axiosInstance.get(`/elderly/${id}`),

  // 关注老人
  followElderly: (id: string) =>
    axiosInstance.post(`/elderly/${id}/follow`),

  // 取消关注
  unfollowElderly: (id: string) =>
    axiosInstance.delete(`/elderly/${id}/follow`),
};

export default elderlyService;
