import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Card, Tag, Button, Spin, message, Row, Col, Avatar } from 'antd';
import { ArrowLeftOutlined, HeartOutlined, HeartFilled } from '@ant-design/icons';
import elderlyService from '../services/elderlyService';
import '../styles/ElderlyDetail.css';

interface Supporter {
  id: string;
  name: string;
  avatar: string;
  feedback: string;
}

interface ElderlyDetail {
  id: string;
  name: string;
  age: number;
  avatar: string;
  address: string;
  riskLevel: string;
  daysWithoutCompanion: number;
  supportersCount: number;
  tags: string[];
  description: string;
  supporters: Supporter[];
}

const ElderlyDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [elderly, setElderly] = useState<ElderlyDetail | null>(null);
  const [loading, setLoading] = useState(false);
  const [isFollowed, setIsFollowed] = useState(false);

  useEffect(() => {
    fetchElderlyDetail();
  }, [id]);

  const fetchElderlyDetail = async () => {
    if (!id) return;
    try {
      setLoading(true);
      const response = await elderlyService.getElderlyById(id);
      setElderly(response.data.data);
      setIsFollowed(response.data.data.isFollowed);
    } catch (error) {
      message.error('Failed to load elderly details');
    } finally {
      setLoading(false);
    }
  };

  const handleFollow = async () => {
    if (!id) return;
    try {
      if (isFollowed) {
        await elderlyService.unfollowElderly(id);
      } else {
        await elderlyService.followElderly(id);
      }
      setIsFollowed(!isFollowed);
      message.success(isFollowed ? 'Unfollowed' : 'Followed');
    } catch (error) {
      message.error('Operation failed');
    }
  };

  if (loading || !elderly) {
    return <Spin />
  }

  return (
    <div className="elderly-detail-container">
      <Button
        type="text"
        icon={<ArrowLeftOutlined />}
        onClick={() => navigate(-1)}
      >
        返回
      </Button>

      <Card>
        <Row gutter={24}>
          <Col xs={24} md={8}>
            <img src={elderly.avatar} alt={elderly.name} style={{ width: '100%' }} />
          </Col>
          <Col xs={24} md={16}>
            <h2>{elderly.name}</h2>
            <p>年龄: {elderly.age}</p>
            <p>地址: {elderly.address}</p>
            <p>无人陪伴: {elderly.daysWithoutCompanion} 天</p>
            <p>关注者: {elderly.supportersCount} 人</p>
            <div className="tags">
              {elderly.tags.map((tag) => (
                <Tag key={tag} color="blue">
                  {tag}
                </Tag>
              ))}
            </div>
            <Button
              type="primary"
              size="large"
              onClick={handleFollow}
            >
              {isFollowed ? <HeartFilled /> : <HeartOutlined />}
              {isFollowed ? '已关注' : '关注老人'}
            </Button>
          </Col>
        </Row>
      </Card>

      <Card title="Supporter反馈" style={{ marginTop: '24px' }}>
        <Row gutter={16}>
          {elderly.supporters?.map((supporter) => (
            <Col key={supporter.id} xs={24} md={12}>
              <Card>
                <div style={{ display: 'flex', alignItems: 'center', marginBottom: '8px' }}>
                  <Avatar src={supporter.avatar} size="large" />
                  <span style={{ marginLeft: '8px' }}>{supporter.name}</span>
                </div>
                <p>{supporter.feedback}</p>
              </Card>
            </Col>
          ))}
        </Row>
      </Card>
    </div>
  );
};

export default ElderlyDetail;
