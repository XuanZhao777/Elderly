# 🏡 老年人关怀养老平台系统

一个AI驱动的老年人关怀与陪伴网络平台，连接爱心志愿者与需要陪伴的老年人，形成智能养老网络。

## 📋 项目特性

### 1. 首页功能
- 📸 老人信息卡片展示（照片、名字、风险等级、距离）
- 🎯 风险等级智能计算（红/橙/绿标记）
- ❤️ 关注/取消关注老人
- 📍 基于GPS的距离计算

### 2. 老人详情页
- 👤 个人信息完整展示
- 📅 无人陪伴天数统计
- 🤝 关注者(Supporters)数量
- 🏷️ 个人标签系统（无子、独居、高龄、残疾等）
- 📝 Supporter反馈展示
- 🔗 相似老人推荐算法

### 3. 打卡系统
- ✅ 志愿时长自动计算
- 📝 反馈表单
- 📸 图片/视频上传
- 🎁 积分奖励机制(+1分)
- 🙏 感谢页面展示

### 4. 个人中心
- 👤 用户统计信息（志愿时长、积分、帮助老人数）
- 🗺️ 香港地图人脉网络可视化
- 👨‍👩‍👧 关注的老人列表
- 🤝 好友关注的老人列表
- 📸 感动瞬间相册（时间轴滚动播放）

### 5. 人际网连接
- 🔗 爱心人士相互连接
- 🏘️ 形成养老网络
- 📊 社交推荐系统

### 6. 积分激励模块
- 💰 "亲情值"累积与消耗规则
- 🎁 奖励兑换商城（实物、优惠券）
- 🏆 排行榜系统
- 🥇 成就徽章体系

### 7. 数据分析模块
- 📊 老人多维度分析
- 🔴 高风险长者智能识别
- 📈 访问频率统计
- 💪 身体状况评估

## 🏗️ 技术架构

```
elderly-care-platform/
├── backend/                    # Java Spring Boot 后端
│   ├── src/main/java/com/elderly/
│   │   ├── controller/        # API控制层
│   │   ├── service/           # 业务逻辑层
│   │   ├── repository/        # 数据访问层
│   │   ├── entity/            # 数据模型
│   │   ├── dto/               # 数据传输对象
│   │   ├── algorithm/         # 算法模块
│   │   │   ├── RiskAssessment.java
│   │   │   ├── RecommendationEngine.java
│   │   │   └── AnalyticsEngine.java
│   │   ├── util/              # 工具类
│   │   └── ElderlyApplication.java
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   └── db/
│   │       └── schema.sql
│   └── pom.xml
│
├── frontend/                   # React + TypeScript 前端
│   ├── src/
│   │   ├── pages/
│   │   │   ├── Home/
│   │   │   ├── ElderlyDetail/
│   │   │   ├── CheckIn/
│   │   │   ├── Profile/
│   │   │   └── Leaderboard/
│   │   ├── components/
│   │   ├── services/
│   │   ├── hooks/
│   │   ├── types/
│   │   └── App.tsx
│   ├── package.json
│   └── tsconfig.json
│
└── docs/
    ├── API_DESIGN.md
    ├── DATABASE_SCHEMA.md
    └── ARCHITECTURE.md
```

## 🛠️ 技术栈

### 后端
- **框架**: Spring Boot 2.7+
- **数据库**: MySQL 8.0
- **ORM**: JPA/Hibernate
- **API**: Spring Web MVC
- **认证**: Spring Security + JWT
- **缓存**: Redis (可选)
- **文件存储**: AWS S3 或本地存储

### 前端
- **框架**: React 18+
- **语言**: TypeScript 4.9+
- **UI库**: Material-UI 或 Ant Design
- **地图**: 高德地图API
- **状态管理**: Redux 或 Zustand
- **HTTP**: Axios
- **样式**: Tailwind CSS

### 数据库
- **关系数据库**: MySQL 8.0
- **表结构**: 用户、老人、关系、打卡记录、积分、反馈等

## 📦 安装与运行

### 前置要求
- Java 11+
- Node.js 16+
- MySQL 8.0+
- npm 或 yarn

### 后端启动
\`\`\`bash
cd backend
mvn clean install
mvn spring-boot:run
\`\`\`

### 前端启动
\`\`\`bash
cd frontend
npm install
npm start
\`\`\`

## 📱 API文档

详见 \`docs/API_DESIGN.md\`

## 💾 数据库设计

详见 \`docs/DATABASE_SCHEMA.md\`

## 📊 核心算法

### 1. 风险评估算法
根据以下维度计算老人风险等级：
- 年龄（Age）
- 健康状况（Health Status）
- 无人陪伴天数（Days without Companion）
- 支持者数量（Number of Supporters）
- 身体残疾度（Disability Level）

**风险等级**：
- 🟢 低风险（Green）: 综合分数 < 40
- 🟠 中风险（Orange）: 综合分数 40-70
- 🔴 高风险（Red）: 综合分数 > 70

### 2. 推荐引擎
基于个人标签、位置、兴趣等多维度推荐相似老人和志愿者。

### 3. 数据分析引擎
提供老人访问频率、身体状况趋势等多维度分析。

## 👥 团队与贡献

欢迎提交Issue和Pull Request！

## 📄 许可证

MIT License

## 📞 联系方式

- 📧 Email: support@elderly-care.com
- 🐛 Issue Tracker: GitHub Issues
