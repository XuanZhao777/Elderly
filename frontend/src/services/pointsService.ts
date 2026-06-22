import axiosInstance from './api';

export interface UserPoints {
  userId: string;
  totalPoints: number;
  availablePoints: number;
  usedPoints: number;
  level: 'BRONZE' | 'SILVER' | 'GOLD' | 'PLATINUM';
  currentRank: number;
  rankPercentage: number;
}

const pointsService = {
  // 获取我的积分
  getMyPoints: () =>
    axiosInstance.get('/points/me'),

  // 获取排行榜
  getLeaderboard: (page: number = 0, size: number = 50) =>
    axiosInstance.get('/points/leaderboard', {
      params: { page, size },
    }),
};

export default pointsService;
