import React from 'react';
import { Layout, Menu, Avatar, Badge } from 'antd';
import { HomeOutlined, CheckCircleOutlined, UserOutlined } from '@ant-design/icons';
import { useNavigate, useLocation } from 'react-router-dom';
import './Layout.css';

const { Header, Content, Footer } = Layout;

interface LayoutProps {
  children: React.ReactNode;
}

const AppLayout: React.FC<LayoutProps> = ({ children }) => {
  const navigate = useNavigate();
  const location = useLocation();

  const menuItems = [
    {
      key: '/',
      icon: <HomeOutlined />,
      label: '首页',
      onClick: () => navigate('/'),
    },
    {
      key: '/checkin',
      icon: <CheckCircleOutlined />,
      label: '打卡',
      onClick: () => navigate('/checkin'),
    },
    {
      key: '/profile',
      icon: <UserOutlined />,
      label: '个人',
      onClick: () => navigate('/profile'),
    },
  ];

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Header className="header">
        <h1>🏡 老年人关怀平台</h1>
      </Header>
      <Content className="content">{children}</Content>
      <Footer className="footer">
        <Menu
          mode="horizontal"
          selectedKeys={[location.pathname]}
          items={menuItems}
          style={{ borderTop: '1px solid #f0f0f0' }}
        />
      </Footer>
    </Layout>
  );
};

export default AppLayout;
