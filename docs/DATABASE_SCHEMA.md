# 💾 数据库设计文档

## 数据库配置

- **DBMS**: MySQL 8.0+
- **字符集**: UTF8MB4
- **排序规则**: utf8mb4_unicode_ci
- **时区**: UTC+8

---

## 表结构设计

### 1. 用户表 (users)

```sql
CREATE TABLE users (
  id CHAR(36) PRIMARY KEY COMMENT '用户ID (UUID)',
  username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
  email VARCHAR(100) UNIQUE NOT NULL COMMENT '邮箱',
  password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希',
  phone_number VARCHAR(20) COMMENT '电话号码',
  avatar_url VARCHAR(255) COMMENT '头像URL',
  user_type ENUM('VOLUNTEER', 'ELDERLY', 'ADMIN') NOT NULL COMMENT '用户类型',
  location_latitude DECIMAL(10, 8) COMMENT '纬度',
  location_longitude DECIMAL(11, 8) COMMENT '经度',
  is_active BOOLEAN DEFAULT true COMMENT '是否激活',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_email (email),
  KEY idx_user_type (user_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 2. 老人信息表 (elderly_info)

```sql
CREATE TABLE elderly_info (
  id CHAR(36) PRIMARY KEY COMMENT '老人ID (UUID)',
  user_id CHAR(36) NOT NULL COMMENT '对应用户ID (可为空)',
  name VARCHAR(100) NOT NULL COMMENT '姓名',
  age INT NOT NULL COMMENT '年龄',
  gender ENUM('M', 'F', 'OTHER') COMMENT '性别',
  avatar_url VARCHAR(255) COMMENT '照片URL',
  address VARCHAR(500) NOT NULL COMMENT '详细地址',
  phone_number VARCHAR(20) COMMENT '电话号码',
  health_status ENUM('EXCELLENT', 'GOOD', 'FAIR', 'POOR') DEFAULT 'FAIR' COMMENT '健康状况',
  disability_level INT DEFAULT 0 COMMENT '残疾程度 (0-100)',
  days_without_companion INT DEFAULT 0 COMMENT '无人陪伴天数',
  last_visited_at TIMESTAMP COMMENT '最后一次被访问时间',
  location_latitude DECIMAL(10, 8) NOT NULL COMMENT '纬度',
  location_longitude DECIMAL(11, 8) NOT NULL COMMENT '经度',
  description TEXT COMMENT '个人描述',
  is_active BOOLEAN DEFAULT true,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_user_id (user_id),
  KEY idx_age (age),
  KEY idx_health_status (health_status),
  KEY idx_location (location_latitude, location_longitude)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 3. 老人标签表 (elderly_tags)

```sql
CREATE TABLE elderly_tags (
  id INT AUTO_INCREMENT PRIMARY KEY,
  elderly_id CHAR(36) NOT NULL,
  tag_name VARCHAR(50) NOT NULL COMMENT '标签: 无子, 独居, 高龄, 残疾, 孤寡, 病弱等',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (elderly_id) REFERENCES elderly_info(id) ON DELETE CASCADE,
  UNIQUE KEY unique_elderly_tag (elderly_id, tag_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 4. 关注关系表 (follow_relationships)

```sql
CREATE TABLE follow_relationships (
  id CHAR(36) PRIMARY KEY COMMENT 'ID (UUID)',
  volunteer_id CHAR(36) NOT NULL COMMENT '志愿者ID',
  elderly_id CHAR(36) NOT NULL COMMENT '老人ID',
  is_active BOOLEAN DEFAULT true COMMENT '是否活跃关注',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (volunteer_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (elderly_id) REFERENCES elderly_info(id) ON DELETE CASCADE,
  UNIQUE KEY unique_follow (volunteer_id, elderly_id),
  KEY idx_elderly_id (elderly_id),
  KEY idx_volunteer_id (volunteer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 5. 打卡记录表 (checkins)

```sql
CREATE TABLE checkins (
  id CHAR(36) PRIMARY KEY COMMENT 'ID (UUID)',
  volunteer_id CHAR(36) NOT NULL COMMENT '志愿者ID',
  elderly_id CHAR(36) NOT NULL COMMENT '老人ID',
  check_in_time TIMESTAMP NOT NULL COMMENT '打卡时间',
  check_out_time TIMESTAMP COMMENT '结束时间',
  duration_minutes INT NOT NULL COMMENT '时长 (分钟)',
  feedback TEXT COMMENT '反馈内容',
  points_earned INT DEFAULT 1 COMMENT '获得积分',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (volunteer_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (elderly_id) REFERENCES elderly_info(id) ON DELETE CASCADE,
  KEY idx_volunteer_id (volunteer_id),
  KEY idx_elderly_id (elderly_id),
  KEY idx_check_in_time (check_in_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 6. 打卡媒体表 (checkin_media)

```sql
CREATE TABLE checkin_media (
  id INT AUTO_INCREMENT PRIMARY KEY,
  checkin_id CHAR(36) NOT NULL,
  media_type ENUM('PHOTO', 'VIDEO') NOT NULL,
  media_url VARCHAR(500) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (checkin_id) REFERENCES checkins(id) ON DELETE CASCADE,
  KEY idx_checkin_id (checkin_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 7. 积分表 (points)

```sql
CREATE TABLE points (
  id CHAR(36) PRIMARY KEY,
  user_id CHAR(36) UNIQUE NOT NULL COMMENT '用户ID',
  total_points INT DEFAULT 0 COMMENT '总积分',
  available_points INT DEFAULT 0 COMMENT '可用积分',
  used_points INT DEFAULT 0 COMMENT '已用积分',
  level ENUM('BRONZE', 'SILVER', 'GOLD', 'PLATINUM') DEFAULT 'BRONZE' COMMENT '用户等级',
  current_rank INT COMMENT '当前排名',
  last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  KEY idx_total_points (total_points)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 8. 积分历史表 (points_history)

```sql
CREATE TABLE points_history (
  id CHAR(36) PRIMARY KEY,
  user_id CHAR(36) NOT NULL,
  action_type ENUM('EARNED', 'USED', 'REFUNDED', 'ADMIN') NOT NULL COMMENT '操作类型',
  points INT NOT NULL COMMENT '积分变化',
  related_id VARCHAR(100) COMMENT '关联ID (打卡ID、兑换ID等)',
  description VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  KEY idx_user_id (user_id),
  KEY idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 9. 奖励表 (rewards)

```sql
CREATE TABLE rewards (
  id CHAR(36) PRIMARY KEY,
  name VARCHAR(100) NOT NULL COMMENT '奖励名称',
  description TEXT COMMENT '描述',
  required_points INT NOT NULL COMMENT '所需积分',
  category ENUM('COUPON', 'PRODUCT', 'EXPERIENCE', 'DONATION') COMMENT '分类',
  image_url VARCHAR(255),
  stock INT NOT NULL COMMENT '库存',
  exchange_count INT DEFAULT 0 COMMENT '兑换次数',
  is_active BOOLEAN DEFAULT true,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_category (category),
  KEY idx_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 10. 奖励兑换表 (reward_exchanges)

```sql
CREATE TABLE reward_exchanges (
  id CHAR(36) PRIMARY KEY,
  user_id CHAR(36) NOT NULL,
  reward_id CHAR(36) NOT NULL,
  exchange_code VARCHAR(50) UNIQUE NOT NULL COMMENT '兑换码',
  points_spent INT NOT NULL,
  status ENUM('PENDING', 'DELIVERED', 'USED', 'EXPIRED') DEFAULT 'PENDING',
  expires_at TIMESTAMP COMMENT '过期时间',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (reward_id) REFERENCES rewards(id) ON DELETE CASCADE,
  KEY idx_user_id (user_id),
  KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 11. 徽章表 (badges)

```sql
CREATE TABLE badges (
  id CHAR(36) PRIMARY KEY,
  name VARCHAR(100) NOT NULL COMMENT '徽章名称',
  description TEXT,
  icon_url VARCHAR(255),
  condition_type VARCHAR(50) NOT NULL COMMENT '获得条件类型',
  condition_value INT COMMENT '条件值',
  is_active BOOLEAN DEFAULT true,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 12. 用户徽章表 (user_badges)

```sql
CREATE TABLE user_badges (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id CHAR(36) NOT NULL,
  badge_id CHAR(36) NOT NULL,
  unlocked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (badge_id) REFERENCES badges(id) ON DELETE CASCADE,
  UNIQUE KEY unique_user_badge (user_id, badge_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 13. 社交连接表 (social_connections)

```sql
CREATE TABLE social_connections (
  id CHAR(36) PRIMARY KEY,
  user_id_1 CHAR(36) NOT NULL COMMENT '用户1',
  user_id_2 CHAR(36) NOT NULL COMMENT '用户2',
  status ENUM('PENDING', 'ACCEPTED', 'REJECTED', 'BLOCKED') DEFAULT 'PENDING',
  message TEXT COMMENT '连接消息',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id_1) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id_2) REFERENCES users(id) ON DELETE CASCADE,
  UNIQUE KEY unique_connection (user_id_1, user_id_2),
  KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 14. 感动瞬间表 (moments)

```sql
CREATE TABLE moments (
  id CHAR(36) PRIMARY KEY,
  user_id CHAR(36) NOT NULL,
  elderly_id CHAR(36) COMMENT '相关老人ID',
  media_type ENUM('PHOTO', 'VIDEO') NOT NULL,
  media_url VARCHAR(500) NOT NULL,
  caption TEXT COMMENT '描述',
  likes_count INT DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (elderly_id) REFERENCES elderly_info(id) ON DELETE SET NULL,
  KEY idx_user_id (user_id),
  KEY idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 15. 老人反馈表 (elderly_feedback)

```sql
CREATE TABLE elderly_feedback (
  id CHAR(36) PRIMARY KEY,
  elderly_id CHAR(36) NOT NULL,
  volunteer_id CHAR(36) NOT NULL,
  feedback_text TEXT NOT NULL COMMENT '反馈内容',
  rating INT COMMENT '评分 1-5',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (elderly_id) REFERENCES elderly_info(id) ON DELETE CASCADE,
  FOREIGN KEY (volunteer_id) REFERENCES users(id) ON DELETE CASCADE,
  KEY idx_elderly_id (elderly_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 16. 风险评估表 (risk_assessments)

```sql
CREATE TABLE risk_assessments (
  id CHAR(36) PRIMARY KEY,
  elderly_id CHAR(36) UNIQUE NOT NULL,
  risk_score INT NOT NULL COMMENT '风险分数 0-100',
  risk_level ENUM('GREEN', 'ORANGE', 'RED') NOT NULL COMMENT '风险等级',
  age_factor DECIMAL(5, 2),
  health_factor DECIMAL(5, 2),
  companion_factor DECIMAL(5, 2),
  supporter_factor DECIMAL(5, 2),
  disability_factor DECIMAL(5, 2),
  last_assessed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (elderly_id) REFERENCES elderly_info(id) ON DELETE CASCADE,
  KEY idx_risk_level (risk_level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

---

## 索引设计

### 常用查询索引

1. **查询周围老人**: `elderly_info (location_latitude, location_longitude)`
2. **查询高风险老人**: `risk_assessments (risk_level, last_assessed_at)`
3. **查询用户积分排行**: `points (total_points DESC)`
4. **查询打卡历史**: `checkins (volunteer_id, created_at DESC)`
5. **查询关注关系**: `follow_relationships (volunteer_id, elderly_id)`

---

## 视图设计

### 1. 老人信息视图

```sql
CREATE VIEW elderly_with_stats AS
SELECT 
  e.id,
  e.name,
  e.age,
  e.avatar_url,
  e.address,
  e.health_status,
  r.risk_level,
  r.risk_score,
  COUNT(DISTINCT f.volunteer_id) as supporters_count,
  DATEDIFF(NOW(), COALESCE(e.last_visited_at, e.created_at)) as days_without_companion
FROM elderly_info e
LEFT JOIN risk_assessments r ON e.id = r.elderly_id
LEFT JOIN follow_relationships f ON e.id = f.elderly_id AND f.is_active = true
GROUP BY e.id;
```

---

## 数据库初始化脚本

详见 `backend/src/main/resources/db/schema.sql`
