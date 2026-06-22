# 📱 API 设计文档

## 基础信息

- **Base URL**: `http://localhost:8080/api/v1`
- **认证**: JWT Token (Header: `Authorization: Bearer {token}`)
- **内容格式**: JSON
- **时区**: UTC+8 (香港/中国)

---

## 1. 用户认证 API

### 1.1 用户注册
```
POST /auth/register
Content-Type: application/json

{
  "username": "string",
  "email": "string",
  "password": "string",
  "phoneNumber": "string",
  "userType": "VOLUNTEER" | "ELDERLY" | "ADMIN"
}

Response 201:
{
  "id": "uuid",
  "username": "string",
  "email": "string",
  "userType": "VOLUNTEER",
  "token": "jwt_token",
  "createdAt": "2024-01-01T00:00:00Z"
}
```

### 1.2 用户登录
```
POST /auth/login
Content-Type: application/json

{
  "email": "string",
  "password": "string"
}

Response 200:
{
  "id": "uuid",
  "username": "string",
  "token": "jwt_token",
  "userType": "VOLUNTEER",
  "expiresIn": 86400
}
```

### 1.3 获取用户信息
```
GET /auth/me
Authorization: Bearer {token}

Response 200:
{
  "id": "uuid",
  "username": "string",
  "email": "string",
  "avatar": "url",
  "phoneNumber": "string",
  "userType": "VOLUNTEER",
  "location": {
    "latitude": 22.3193,
    "longitude": 114.1694
  },
  "createdAt": "2024-01-01T00:00:00Z"
}
```

---

## 2. 老人管理 API

### 2.1 获取老人列表（首页）
```
GET /elderly?latitude=22.3193&longitude=114.1694&radius=10
Authorization: Bearer {token}

Response 200:
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "id": "uuid",
      "name": "李阿姨",
      "age": 75,
      "avatar": "url",
      "riskLevel": "RED", // RED, ORANGE, GREEN
      "distance": 2.5, // km
      "daysWithoutCompanion": 7,
      "supportersCount": 3,
      "isFollowed": false,
      "tags": ["无子", "独居", "高龄"],
      "healthStatus": "FAIR"
    }
  ],
  "pagination": {
    "page": 1,
    "pageSize": 20,
    "total": 100
  }
}
```

### 2.2 获取老人详情
```
GET /elderly/{elderlyId}
Authorization: Bearer {token}

Response 200:
{
  "id": "uuid",
  "name": "李阿姨",
  "age": 75,
  "avatar": "url",
  "address": "香港九龙旺角弥顿道100号",
  "phoneNumber": "852-9876-5432",
  "riskLevel": "RED",
  "daysWithoutCompanion": 7,
  "supportersCount": 3,
  "tags": ["无子", "独居", "高龄", "残疾"],
  "healthStatus": "FAIR",
  "description": "李阿姨是一位独居老人...",
  "supporters": [
    {
      "id": "uuid",
      "name": "张志愿者",
      "avatar": "url",
      "totalVolunteerHours": 120,
      "feedback": "李阿姨很温暖，很高兴能帮助她"
    }
  ],
  "similarElderly": [
    {
      "id": "uuid",
      "name": "王阿姨",
      "avatar": "url",
      "riskLevel": "RED",
      "matchScore": 0.85
    }
  ]
}
```

### 2.3 关注老人
```
POST /elderly/{elderlyId}/follow
Authorization: Bearer {token}

Response 201:
{
  "code": 201,
  "message": "Successfully followed",
  "data": {
    "followId": "uuid",
    "followedAt": "2024-01-01T00:00:00Z"
  }
}
```

### 2.4 取消关注老人
```
DELETE /elderly/{elderlyId}/follow
Authorization: Bearer {token}

Response 204:
```

---

## 3. 打卡 API

### 3.1 获取待打卡老人信息
```
GET /checkin/{elderlyId}
Authorization: Bearer {token}

Response 200:
{
  "id": "uuid",
  "elderlyId": "uuid",
  "elderlyName": "李阿姨",
  "address": "香港九龙旺角弥顿道100号",
  "avatar": "url",
  "checkInStartTime": "2024-01-01T10:00:00Z"
}
```

### 3.2 完成打卡
```
POST /checkin
Authorization: Bearer {token}
Content-Type: multipart/form-data

{
  "elderlyId": "uuid",
  "duration": 120, // 分钟
  "feedback": "李阿姨今天精神很好，我们聊天2小时",
  "photos": [File, File],
  "videos": [File]
}

Response 201:
{
  "code": 201,
  "message": "Check-in successful",
  "data": {
    "checkInId": "uuid",
    "pointsEarned": 1,
    "totalPoints": 145,
    "message": "感谢您的陪伴！您获得了1亲情值"
  }
}
```

### 3.3 获取打卡历史
```
GET /checkin/history?page=1&pageSize=20
Authorization: Bearer {token}

Response 200:
{
  "code": 200,
  "data": [
    {
      "id": "uuid",
      "elderlyId": "uuid",
      "elderlyName": "李阿姨",
      "duration": 120,
      "feedback": "string",
      "photos": ["url"],
      "createdAt": "2024-01-01T12:00:00Z"
    }
  ]
}
```

---

## 4. 积分系统 API

### 4.1 获取用户积分信息
```
GET /points/me
Authorization: Bearer {token}

Response 200:
{
  "code": 200,
  "data": {
    "userId": "uuid",
    "totalPoints": 245,
    "availablePoints": 245,
    "usedPoints": 0,
    "rank": 5,
    "rankPercentage": 98.5,
    "level": "GOLD", // BRONZE, SILVER, GOLD, PLATINUM
    "badges": [
      {
        "id": "uuid",
        "name": "第一次打卡",
        "icon": "url",
        "unlockedAt": "2024-01-01T00:00:00Z"
      }
    ]
  }
}
```

### 4.2 获取排行榜
```
GET /points/leaderboard?period=month&page=1&pageSize=50
Authorization: Bearer {token}

// period: week, month, year, all

Response 200:
{
  "code": 200,
  "data": [
    {
      "rank": 1,
      "userId": "uuid",
      "username": "志愿者A",
      "avatar": "url",
      "points": 500,
      "badge": "🥇 PLATINUM",
      "isCurrentUser": false
    }
  ]
}
```

### 4.3 获取奖励商城
```
GET /rewards?category=all&page=1&pageSize=20
Authorization: Bearer {token}

Response 200:
{
  "code": 200,
  "data": [
    {
      "id": "uuid",
      "name": "星巴克优惠券",
      "description": "50元消费券",
      "requiredPoints": 100,
      "image": "url",
      "category": "COUPON",
      "stock": 50,
      "exchangeCount": 120
    }
  ]
}
```

### 4.4 兑换奖励
```
POST /rewards/{rewardId}/exchange
Authorization: Bearer {token}

Response 201:
{
  "code": 201,
  "message": "Reward exchanged successfully",
  "data": {
    "exchangeId": "uuid",
    "rewardCode": "STARBUCKS-2024-001",
    "expiresAt": "2024-02-01T00:00:00Z"
  }
}
```

---

## 5. 个人中心 API

### 5.1 获取个人统计信息
```
GET /profile/stats
Authorization: Bearer {token}

Response 200:
{
  "code": 200,
  "data": {
    "volunteerHours": 480,
    "points": 245,
    "elderlyCaredCount": 8,
    "friendsCount": 15,
    "joinedDays": 180
  }
}
```

### 5.2 获取个人关注的老人
```
GET /profile/following-elderly?page=1&pageSize=20
Authorization: Bearer {token}

Response 200:
{
  "code": 200,
  "data": [
    {
      "id": "uuid",
      "name": "李阿姨",
      "avatar": "url",
      "riskLevel": "RED",
      "daysWithoutCompanion": 3
    }
  ]
}
```

### 5.3 获取朋友网络
```
GET /profile/friends
Authorization: Bearer {token}

Response 200:
{
  "code": 200,
  "data": {
    "friends": [
      {
        "id": "uuid",
        "name": "张志愿者",
        "avatar": "url",
        "location": {
          "latitude": 22.3193,
          "longitude": 114.1694
        },
        "connectedAt": "2024-01-01T00:00:00Z"
      }
    ],
    "map": {
      "friendsLocations": [
        {
          "friendId": "uuid",
          "name": "张志愿者",
          "latitude": 22.3193,
          "longitude": 114.1694,
          "marker": "red_dot"
        }
      ],
      "center": {
        "latitude": 22.3193,
        "longitude": 114.1694
      }
    }
  }
}
```

### 5.4 获取感动瞬间
```
GET /profile/moments?page=1&pageSize=20
Authorization: Bearer {token}

Response 200:
{
  "code": 200,
  "data": [
    {
      "id": "uuid",
      "mediaUrl": "url",
      "type": "PHOTO" | "VIDEO",
      "caption": "与李阿姨温暖的下午",
      "createdAt": "2024-01-01T12:00:00Z",
      "likes": 25,
      "isLiked": false
    }
  ]
}
```

### 5.5 上传感动瞬间
```
POST /profile/moments
Authorization: Bearer {token}
Content-Type: multipart/form-data

{
  "media": File,
  "caption": "string",
  "type": "PHOTO" | "VIDEO"
}

Response 201:
{
  "code": 201,
  "data": {
    "id": "uuid",
    "mediaUrl": "url",
    "createdAt": "2024-01-01T12:00:00Z"
  }
}
```

---

## 6. 社交网络 API

### 6.1 连接好友
```
POST /social/connect
Authorization: Bearer {token}
Content-Type: application/json

{
  "targetUserId": "uuid",
  "message": "让我们一起照顾老人们"
}

Response 201:
{
  "code": 201,
  "message": "Connection sent"
}
```

### 6.2 接受连接
```
POST /social/accept/{connectionId}
Authorization: Bearer {token}

Response 200:
{
  "code": 200,
  "message": "Connection accepted"
}
```

---

## 7. 数据分析 API

### 7.1 获取老人分析数据
```
GET /analytics/elderly/{elderlyId}?startDate=2024-01-01&endDate=2024-01-31
Authorization: Bearer {token}

Response 200:
{
  "code": 200,
  "data": {
    "elderlyId": "uuid",
    "name": "李阿姨",
    "visitFrequency": {
      "totalVisits": 10,
      "averageVisitsPerWeek": 2.5,
      "trend": "UP" | "DOWN" | "STABLE"
    },
    "healthStatus": {
      "current": "FAIR",
      "trend": "IMPROVING" | "DECLINING" | "STABLE"
    },
    "riskFactors": [
      "Days without companion: 7",
      "Age: 75",
      "Health status: Fair"
    ],
    "riskScore": 75,
    "riskLevel": "RED"
  }
}
```

### 7.2 获取系统分析仪表板
```
GET /analytics/dashboard
Authorization: Bearer {token} (ADMIN only)

Response 200:
{
  "code": 200,
  "data": {
    "totalElderly": 500,
    "totalVolunteers": 1200,
    "highRiskElderly": 150,
    "mediumRiskElderly": 200,
    "lowRiskElderly": 150,
    "totalVolunteerHours": 12000,
    "averageVisitFrequency": 2.5,
    "topVolunteers": [...]
  }
}
```

---

## 错误响应

```json
{
  "code": 400,
  "message": "Invalid request",
  "errors": [
    {
      "field": "email",
      "message": "Email format is invalid"
    }
  ]
}
```

### 常见错误码
- `200`: 成功
- `201`: 创建成功
- `204`: 删除成功
- `400`: 请求参数错误
- `401`: 未授权
- `403`: 禁止访问
- `404`: 资源不存在
- `500`: 服务器错误
