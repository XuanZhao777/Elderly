import React, { useState } from 'react';
import { Card, Form, Input, Button, message, Upload, Spin } from 'antd';
import { UploadOutlined } from '@ant-design/icons';
import checkInService from '../services/checkInService';
import '../styles/CheckIn.css';

const CheckIn: React.FC = () => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [photos, setPhotos] = useState<File[]>([]);

  const handleSubmit = async (values: any) => {
    try {
      setLoading(true);

      // 这里需要实现文件上传逻辑
      // 对于简化版本，我们只发送基本数据

      const response = await checkInService.completeCheckIn(
        values.checkInId,
        values.duration,
        values.feedback
      );

      message.success(response.data.data.message);
      form.resetFields();
      setPhotos([]);
    } catch (error) {
      message.error('Failed to complete check-in');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="checkin-container">
      <Card title="打卡记录">
        <Spin spinning={loading}>
          <Form form={form} layout="vertical" onFinish={handleSubmit}>
            <Form.Item
              name="checkInId"
              label="打卡ID"
              rules={[{ required: true, message: 'Please input check-in ID' }]}
            >
              <Input placeholder="输入打卡ID" />
            </Form.Item>

            <Form.Item
              name="duration"
              label="志愿时长（分钟）"
              rules={[{ required: true, message: 'Please input duration' }]}
            >
              <Input type="number" placeholder="输入时长" min={0} />
            </Form.Item>

            <Form.Item
              name="feedback"
              label="反馈"
              rules={[{ required: true, message: 'Please input feedback' }]}
            >
              <Input.TextArea
                rows={4}
                placeholder="分享您与老人的开心时刻..."
              />
            </Form.Item>

            <Form.Item label="上传照片">
              <Upload
                multiple
                beforeUpload={(file) => {
                  setPhotos([...photos, file]);
                  return false;
                }}
              >
                <Button icon={<UploadOutlined />}>上传照片</Button>
              </Upload>
            </Form.Item>

            <Button type="primary" htmlType="submit" block size="large">
              完成打卡
            </Button>
          </Form>
        </Spin>
      </Card>
    </div>
  );
};

export default CheckIn;
