import React, { useEffect, useState } from 'react';
import { Card, Row, Col, Statistic, Button, Modal, Avatar, Tabs, Spin, message } from 'antd';
import { QrcodeOutlined, HeartOutlined } from '@ant-design/icons';
import pointsService from '../services/pointsService';
import checkInService from '../services/checkInService';
import '../styles/Profile.css';

interface UserStats {
  volunteerHours: number;
  points: number;
  elderlyCaredCount: number;
  friendsCount: number;
}

const Profile: React.FC = () => {
  const [stats, setStats] = useState<UserStats | null>(null);
  const [points, setPoints] = useState<any>(null);
  const [loading, setLoading] = useState(false);
  const [qrcodeModalVisible, setQrcodeModalVisible] = useState(false);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      setLoading(true);
      const [hoursRes, pointsRes] = await Promise.all([
        checkInService.getTotalVolunteerHours(),
        pointsService.getMyPoints(),
      ]);

      setStats({
        volunteerHours: hoursRes.data.data.totalHours || 0,
        points: pointsRes.data.data.totalPoints || 0,
        elderlyCaredCount: 0, // 需要从API获取
        friendsCount: 0, // 需要从API获取
      });
      setPoints(pointsRes.data.data);
    } catch (error) {
      message.error('Failed to load profile data');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="profile-container">
      <Spin spinning={loading}>
        {/* 个人信息卡 */}
        <Card className="profile-header">
          <Row gutter={24} align="middle">
            <Col xs={24} md={4}>
              <Avatar size={80} icon={<HeartOutlined />} />
            </Col>
            <Col xs={24} md={20}>
              <h2>志愿者用户</h2>
              <Button
                type="primary"
                icon={<QrcodeOutlined />}
                onClick={() => setQrcodeModalVisible(true)}
              >
                二维码
              </Button>
            </Col>
          </Row>
        </Card>

        {/* 统计信息 */}
        <Card style={{ marginTop: '24px' }}>
          <Row gutter={16}>
            <Col xs={12} md={6}>
              <Statistic
                title="志愿时长"
                value={stats?.volunteerHours || 0}
                suffix="小时"
              />
            </Col>
            <Col xs={12} md={6}>
              <Statistic
                title="亲情值"
                value={stats?.points || 0}
                valueStyle={{ color: '#ff7a45' }}
              />
            </Col>
            <Col xs={12} md={6}>
              <Statistic
                title="帮助老人"
                value={stats?.elderlyCaredCount || 0}
              />
            </Col>
            <Col xs={12} md={6}>
              <Statistic
                title="好友"
                value={stats?.friendsCount || 0}
              />
            </Col>
          </Row>
        </Card>

        {/* 等级信息 */}
        {points && (
          <Card title="用户等级" style={{ marginTop: '24px' }}>
            <p>当前等级: {points.level}</p>
            <p>排行榜排名: {points.currentRank}</p>
            <p>排名百分比: {points.rankPercentage}%</p>
          </Card>
        )}
      </Spin>

      {/* 二维码弹窗 */}
      <Modal
        title="个人二维码"
        visible={qrcodeModalVisible}
        onOk={() => setQrcodeModalVisible(false)}
        onCancel={() => setQrcodeModalVisible(false)}
      >
        <div style={{ textAlign: 'center' }}>
          <img src="https://via.placeholder.com/200" alt="QR Code" />
        </div>
      </Modal>
    </div>
  );
};

export default Profile;
