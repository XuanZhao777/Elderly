import React, { useEffect, useState } from 'react';
import { Card, Row, Col, Tag, Button, Spin, message } from 'antd';
import { HeartOutlined, HeartFilled, EnvironmentOutlined } from '@ant-design/icons';
import elderlyService from '../services/elderlyService';
import '../styles/Home.css';

interface Elderly {
  id: string;
  name: string;
  age: number;
  avatar: string;
  riskLevel: string;
  distance: number;
  supportersCount: number;
  isFollowed: boolean;
}

const Home: React.FC = () => {
  const [elderly, setElderly] = useState<Elderly[]>([]);
  const [loading, setLoading] = useState(false);
  const [location, setLocation] = useState<{ lat: number; lon: number } | null>(null);

  useEffect(() => {
    // 获取用户位置
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition((position) => {
        setLocation({
          lat: position.coords.latitude,
          lon: position.coords.longitude,
        });
      });
    }
  }, []);

  useEffect(() => {
    if (location) {
      fetchNearbyElderly();
    }
  }, [location]);

  const fetchNearbyElderly = async () => {
    if (!location) return;
    try {
      setLoading(true);
      const response = await elderlyService.getNearbyElderly(location.lat, location.lon, 10);
      setElderly(response.data.data);
    } catch (error) {
      message.error('Failed to load elderly list');
    } finally {
      setLoading(false);
    }
  };

  const getRiskColor = (level: string) => {
    const colors: { [key: string]: string } = {
      RED: '#ff4d4f',
      ORANGE: '#faad14',
      GREEN: '#52c41a',
    };
    return colors[level] || '#1890ff';
  };

  const handleFollow = async (elderlyId: string, isFollowed: boolean) => {
    try {
      if (isFollowed) {
        await elderlyService.unfollowElderly(elderlyId);
      } else {
        await elderlyService.followElderly(elderlyId);
      }
      fetchNearbyElderly();
      message.success(isFollowed ? 'Unfollowed' : 'Followed');
    } catch (error) {
      message.error('Operation failed');
    }
  };

  return (
    <div className="home-container">
      <h1>附近需要陪伴的老人</h1>
      <Spin spinning={loading}>
        <Row gutter={[16, 16]}>
          {elderly.map((person) => (
            <Col key={person.id} xs={24} sm={12} md={8}>
              <Card
                hoverable
                className="elderly-card"
                cover={<img alt={person.name} src={person.avatar} height="200px" />}
              >
                <div className="elderly-info">
                  <h3>{person.name}</h3>
                  <div className="risk-badge">
                    <div
                      className="risk-circle"
                      style={{ backgroundColor: getRiskColor(person.riskLevel) }}
                    />
                    <span>{person.riskLevel}</span>
                  </div>
                  <p className="distance">
                    <EnvironmentOutlined /> {person.distance?.toFixed(2)} km
                  </p>
                  <p>年龄: {person.age}</p>
                  <p>关注者: {person.supportersCount}</p>
                </div>
                <Button
                  type="primary"
                  block
                  onClick={() => handleFollow(person.id, person.isFollowed)}
                >
                  {person.isFollowed ? <HeartFilled /> : <HeartOutlined />}
                  {person.isFollowed ? '已关注' : '关注'}
                </Button>
              </Card>
            </Col>
          ))}
        </Row>
      </Spin>
    </div>
  );
};

export default Home;
