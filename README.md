# 咖啡店演示项目 - Spring Boot 教学项目

这是一个用于教学的完整全栈应用程序，通过一个支持堂食和外送的咖啡店管理系统来演示核心编程概念。项目包含：

- **后端**：基于 Spring Boot 的 REST API 服务器
- **前端**：基于 React + TypeScript 的现代 Web 应用
- **配送管理**：完整的配送批处理和生命周期管理系统

## 📚 项目概述

本项目旨在教授和演示：

### 后端技术
- **领域驱动设计（DDD）**原则
- **设计模式**（策略模式、工厂模式、状态模式、建造者模式、仓储模式）
- **面向对象编程**原则
- **Spring Boot**特性（依赖注入、REST API、事件处理）

### 前端技术
- **React 18** - 现代 UI 库
- **TypeScript** - 类型安全
- **Material-UI (MUI)** - 企业级组件库
- **React Query** - 服务器状态管理
- **React Router** - 客户端路由
- **Vite** - 快速构建工具

## 🏗️ 架构设计

### 后端架构

应用程序遵循基于 DDD 原则的清晰分层架构：

```
┌─────────────────────────────────────────────────────────┐
│                   接口层（Interface Layer）             │
│  （控制器、DTO、平台集成）                               │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│                   应用层（Application Layer）           │
│  （应用服务、组装器、工厂）                             │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│                   领域层（Domain Layer）                 │
│  （聚合、实体、值对象、领域事件）                       │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│                基础设施层（Infrastructure Layer）        │
│  （仓储、外部服务）                                     │
└─────────────────────────────────────────────────────────┘
```

### 前端架构

前端采用现代化的组件化架构：

```
┌─────────────────────────────────────────────────────────┐
│                   页面层（Pages）                        │
│  （HomePage, OrderListPage, CreateOrderPage, etc.）     │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│                   组件层（Components）                   │
│  （Header, Layout, OrderCard, OrderStatusBadge）        │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│                   服务层（Services）                     │
│  （API 客户端、React Query Hooks）                     │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│                   工具层（Utils & Types）                │
│  （常量、格式化、TypeScript 类型定义）                  │
└─────────────────────────────────────────────────────────┘
```

### 全栈架构

```
┌─────────────────────────────────────────────────────────┐
│                   前端（React + TypeScript）            │
│  http://localhost:3000                                  │
└─────────────────────────────────────────────────────────┘
                           ↓ REST API
┌─────────────────────────────────────────────────────────┐
│                   后端（Spring Boot）                   │
│  http://localhost:8080                                  │
└─────────────────────────────────────────────────────────┘
```

## 🎯 演示的设计模式

### 1. 策略模式（Strategy Pattern）
**位置**：[`PricingStrategy`](src/main/java/com/bluemountain/coffee/domain/service/PricingStrategy.java) 接口及其实现类

为堂食和外送订单提供不同的定价策略：
- [`DineInPricingStrategy`](src/main/java/com/bluemountain/coffee/domain/strategy/DineInPricingStrategy.java) - 无额外费用
- [`DeliveryPricingStrategy`](src/main/java/com/bluemountain/coffee/domain/strategy/DeliveryPricingStrategy.java) - 包含包装费（$2.00）和配送费（$5.00）

**什么是策略模式？**
策略模式定义了一系列算法，并将每个算法封装起来，使它们可以相互替换。在本项目中，我们使用策略模式来处理不同订单类型的定价逻辑，这样当需要添加新的订单类型或修改定价规则时，只需添加新的策略类，而不需要修改现有代码。

### 2. 工厂模式（Factory Pattern）
**位置**：[`PricingStrategyFactory`](src/main/java/com/bluemountain/coffee/application/PricingStrategyFactory.java)

根据订单类型创建合适的定价策略。

**什么是工厂模式？**
工厂模式提供了一种创建对象的最佳方式，在创建对象时不会对客户端暴露创建逻辑，并且是通过使用一个共同的接口来指向新创建的对象。在本项目中，工厂类负责根据订单类型（堂食或外送）返回对应的定价策略对象。

### 3. 状态模式（State Pattern）
**位置**：[`OrderStatus`](src/main/java/com/bluemountain/coffee/domain/model/enums/OrderStatus.java) 和 [`Order.transitionTo()`](src/main/java/com/bluemountain/coffee/domain/model/aggregate/Order.java:237)

管理订单状态转换并进行验证：
```
已创建 → 已支付 → 准备中 → 已完成
               ↓
            已取消
```

**什么是状态模式？**
状态模式允许对象在内部状态改变时改变它的行为。在本项目中，订单对象根据其当前状态（已创建、已支付、准备中等）来决定可以执行哪些操作以及如何转换到下一个状态。这样可以避免大量的 if-else 或 switch 语句，使代码更清晰、更易维护。

### 4. 建造者模式（Builder Pattern）
**位置**：[`Order.create()`](src/main/java/com/bluemountain/coffee/domain/model/aggregate/Order.java:117) 工厂方法

通过验证简化复杂对象的构建。

**什么是建造者模式？**
建造者模式将复杂对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示。在本项目中，我们使用建造者模式的变体（工厂方法）来创建订单对象，这样可以确保在创建订单时所有必要的字段都被正确设置，并且可以进行业务规则验证。

### 5. 仓储模式（Repository Pattern）
**位置**：[`OrderRepository`](src/main/java/com/bluemountain/coffee/domain/repository/OrderRepository.java) 接口和 [`InMemoryOrderRepository`](src/main/java/com/bluemountain/coffee/infrastructure/persistence/InMemoryOrderRepository.java)、[`DeliveryRepository`](src/main/java/com/bluemountain/coffee/domain/repository/DeliveryRepository.java) 接口和 [`InMemoryDeliveryRepository`](src/main/java/com/bluemountain/coffee/infrastructure/persistence/InMemoryDeliveryRepository.java)

抽象数据访问，允许轻松切换存储机制。

**什么是仓储模式？**
仓储模式用于将数据访问逻辑与业务逻辑分离。它提供了一个类似集合的接口来访问领域对象，而隐藏了底层数据存储的细节。在本项目中，仓储接口定义了保存、查找、删除订单和配送的方法，而具体的实现可以是内存存储、数据库存储等，这样业务代码就不需要关心数据如何存储。

### 6. 观察者模式（Observer Pattern）
**位置**：[`OrderEventListener`](src/main/java/com/bluemountain/coffee/application/OrderEventListener.java)、[`DeliveryEventListener`](src/main/java/com/bluemountain/coffee/application/DeliveryEventListener.java)

监听和处理领域事件，实现松耦合的事件驱动架构。

**什么是观察者模式？**
观察者模式定义对象间的一种一对多的依赖关系，当一个对象的状态发生改变时，所有依赖于它的对象都得到通知并被自动更新。在本项目中，当订单或配送的状态发生变化时，会发布相应的领域事件，事件监听器会订阅并处理这些事件，从而实现解耦的系统架构。

## 🎨 演示的面向对象原则

### 封装（Encapsulation）
- 值对象（[`Money`](src/main/java/com/bluemountain/coffee/domain/model/valobj/Money.java)、[`Address`](src/main/java/com/bluemountain/coffee/domain/model/valobj/Address.java)、[`RiderInfo`](src/main/java/com/bluemountain/coffee/domain/model/valobj/RiderInfo.java)）保护内部状态
- 订单和配送聚合通过私有构造函数强制执行业务规则

**什么是封装？**
封装是将数据（属性）和操作数据的方法绑定在一起，并对外隐藏对象的内部实现细节。这样可以防止外部代码直接修改对象的内部状态，必须通过对象提供的方法来访问和修改数据，从而保证数据的一致性和安全性。

### 继承（Inheritance）
- [`DomainException`](src/main/java/com/bluemountain/coffee/domain/exception/DomainException.java) 作为领域异常的基类
- 策略实现类继承自 [`PricingStrategy`](src/main/java/com/bluemountain/coffee/domain/service/PricingStrategy.java) 接口

**什么是继承？**
继承允许一个类（子类）继承另一个类（父类）的属性和方法，从而实现代码复用。子类可以扩展父类的功能，或者重写父类的方法以提供不同的实现。在本项目中，所有的领域异常都继承自基类，这样可以统一处理异常；不同的定价策略都实现了同一个接口，确保它们有相同的方法签名。

### 多态（Polymorphism）
- 多种定价策略可以互换使用
- 不同订单类型通过公共接口处理

**什么是多态？**
多态是指同一个方法调用，由于对象不同可能会有不同的行为。多态主要有两种形式：编译时多态（方法重载）和运行时多态（方法重写）。在本项目中，我们使用的是运行时多态，通过接口引用指向不同的策略实现对象，调用相同的方法时会执行不同的逻辑。

### 抽象（Abstraction）
- 仓储接口隐藏数据访问实现细节
- 服务接口定义契约而不暴露实现细节

**什么是抽象？**
抽象是指隐藏对象的复杂性，只向用户展示必要的功能。抽象可以通过抽象类或接口来实现。在本项目中，我们定义了仓储接口，只声明了需要的方法，而不关心这些方法如何实现，这样业务代码只需要调用接口方法，不需要知道具体的数据存储方式。

### SOLID 原则
- **单一职责原则**：每个类只有一个引起变化的原因
- **开闭原则**：对扩展开放（可以添加新策略），对修改关闭（不需要修改现有代码）
- **里氏替换原则**：实现可以替换它们的抽象
- **接口隔离原则**：客户端只依赖它们使用的方法
- **依赖倒置原则**：高层模块依赖于抽象而不是具体实现

## 🚀 快速开始

### 前置要求

**后端：**
- Java 17 或更高版本
- Maven 3.6 或更高版本

**前端：**
- Node.js 18+ 和 npm

### 构建和运行

#### 后端（Spring Boot）

```bash
# 克隆仓库
git clone <repository-url>
cd Coffee_Demo

# 构建项目
mvn clean install

# 运行应用
mvn spring-boot:run
```

后端应用将在 `http://localhost:8080` 启动

#### 前端（React）

```bash
# 进入前端目录
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端应用将在 `http://localhost:3000` 启动

**注意**：启动前端之前，请确保后端服务已经在运行。

## 📡 REST API 接口

### 订单管理

| 方法 | 端点 | 描述 |
|---------|-----------|-------------|
| POST | `/api/orders` | 创建新订单 |
| GET | `/api/orders/{id}` | 根据 ID 获取订单 |
| GET | `/api/orders` | 获取所有订单 |
| GET | `/api/orders/status/{status}` | 根据状态获取订单 |
| PUT | `/api/orders/{id}/status` | 更新订单状态 |
| PUT | `/api/orders/{id}/ready` | 标记咖啡已准备好 |
| PUT | `/api/orders/{id}/complete` | 完成订单 |
| DELETE | `/api/orders/{id}` | 取消订单 |

### 配送管理

| 方法 | 端点 | 描述 |
|---------|-----------|-------------|
| POST | `/api/deliveries/batch` | 创建配送批次 |
| POST | `/api/deliveries/auto-batch` | 自动批量配送 |
| POST | `/api/deliveries/{id}/assign-rider` | 分配骑手 |
| POST | `/api/deliveries/{id}/pickup` | 标记为已取货 |
| POST | `/api/deliveries/{id}/in-transit` | 标记为配送中 |
| POST | `/api/deliveries/{id}/deliver` | 标记为已送达 |
| POST | `/api/deliveries/{id}/complete` | 完成配送 |
| POST | `/api/deliveries/{id}/cancel` | 取消配送 |
| GET | `/api/deliveries/{id}` | 根据 ID 获取配送 |
| GET | `/api/deliveries/status/{status}` | 根据状态获取配送 |
| GET | `/api/deliveries/rider/{riderId}` | 根据骑手获取配送 |
| GET | `/api/deliveries/order/{orderId}` | 根据订单获取配送 |
| GET | `/api/deliveries/active` | 获取活跃配送 |
| GET | `/api/deliveries/between` | 获取日期范围内的配送 |
| GET | `/api/deliveries` | 获取所有配送 |

### 平台集成

| 方法 | 端点 | 描述 |
|---------|-----------|-------------|
| POST | `/api/platform/orders` | 从平台创建订单 |
| POST | `/api/platform/orders/{id}/ready` | 通知平台订单已准备好 |
| POST | `/api/platform/orders/{id}/complete` | 通知平台订单已完成 |

## 📝 API 使用示例

### 创建堂食订单

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "张三",
    "orderType": "DINE_IN",
    "items": [
      {
        "productName": "浓缩咖啡",
        "quantity": 2,
        "unitPrice": 3.50
      },
      {
        "productName": "卡布奇诺",
        "quantity": 1,
        "unitPrice": 4.50
      }
    ]
  }'
```

### 创建外送订单

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "李四",
    "orderType": "DELIVERY",
    "items": [
      {
        "productName": "拿铁",
        "quantity": 1,
        "unitPrice": 4.00
      }
    ],
    "street": "中山路123号",
    "city": "上海",
    "postalCode": "200000",
    "country": "中国"
  }'
```

### 获取所有订单

```bash
curl http://localhost:8080/api/orders
```

### 更新订单状态

```bash
curl -X PUT "http://localhost:8080/api/orders/{id}/ready"
```

### 创建配送批次

```bash
curl -X POST http://localhost:8080/api/deliveries/batch \
  -H "Content-Type: application/json" \
  -d '["order-id-1", "order-id-2"]'
```

### 自动批量配送

```bash
curl -X POST http://localhost:8080/api/deliveries/auto-batch
```

### 分配骑手

```bash
curl -X POST http://localhost:8080/api/deliveries/{deliveryId}/assign-rider \
  -H "Content-Type: application/json" \
  -d '{
    "riderId": "RIDER-001",
    "riderName": "张三",
    "phoneNumber": "138-0000-0000"
  }'
```

### 获取活跃配送

```bash
curl http://localhost:8080/api/deliveries/active
```

## 📦 项目结构

### 后端项目结构

```
com.bluemountain.coffee
├── CoffeeShopApplication.java              # Spring Boot 主应用类
├── interfaces
│   ├── dto
│   │   ├── CreateOrderCommand.java         # 创建订单的命令对象
│   │   ├── OrderDTO.java                 # 订单数据传输对象
│   │   ├── OrderItemDTO.java            # 订单项数据传输对象
│   │   ├── DeliveryDTO.java            # 配送数据传输对象
│   │   ├── DeliveryItemDTO.java        # 配送项数据传输对象
│   │   └── RiderInfoDTO.java          # 骑手信息数据传输对象
│   ├── web
│   │   ├── OrderController.java           # 订单 REST 控制器
│   │   └── DeliveryController.java        # 配送 REST 控制器
│   └── platform
│       └── PlatformController.java        # 平台集成控制器
├── application
│   ├── OrderAppService.java               # 订单应用服务
│   ├── OrderAssembler.java                # DTO 组装器
│   ├── PricingStrategyFactory.java         # 定价策略工厂
│   ├── OrderEventListener.java            # 订单领域事件监听器
│   ├── DeliveryAppService.java            # 配送应用服务
│   └── DeliveryEventListener.java         # 配送领域事件监听器
├── domain
│   ├── model
│   │   ├── aggregate
│   │   │   ├── Order.java               # 订单聚合根
│   │   │   ├── OrderItem.java          # 订单项实体
│   │   │   ├── Delivery.java           # 配送聚合根
│   │   │   └── DeliveryItem.java       # 配送项实体
│   │   ├── valobj
│   │   │   ├── Money.java               # 金额值对象
│   │   │   ├── Address.java             # 地址值对象
│   │   │   ├── OrderId.java           # 订单 ID 值对象
│   │   │   ├── DeliveryId.java         # 配送 ID 值对象
│   │   │   ├── RiderInfo.java          # 骑手信息值对象
│   │   │   ├── DeliverySlip.java       # 配送单值对象
│   │   │   └── DeliverySlipItem.java   # 配送单项值对象
│   │   └── enums
│   │       ├── OrderType.java            # 订单类型枚举
│   │       ├── OrderStatus.java         # 订单状态枚举
│   │       ├── DeliveryStatus.java      # 配送状态枚举
│   │       └── DeliveryItemStatus.java  # 配送项状态枚举
│   ├── service
│   │   ├── PricingStrategy.java         # 定价策略接口
│   │   └── DeliveryBatchService.java    # 配送批处理服务接口
│   ├── strategy
│   │   ├── DineInPricingStrategy.java  # 堂食定价策略
│   │   └── DeliveryPricingStrategy.java # 外送定价策略
│   ├── repository
│   │   ├── OrderRepository.java        # 订单仓储接口
│   │   └── DeliveryRepository.java     # 配送仓储接口
│   ├── event
│   │   ├── CoffeeReadyEvent.java       # 咖啡准备就绪领域事件
│   │   ├── OrderCreatedEvent.java     # 订单创建领域事件
│   │   ├── DeliveryCreatedEvent.java   # 配送创建领域事件
│   │   ├── DeliveryAssignedEvent.java  # 配送分配骑手领域事件
│   │   ├── DeliveryPickedUpEvent.java  # 配送取货领域事件
│   │   ├── DeliveryDeliveredEvent.java # 配送送达领域事件
│   │   └── DeliveryCompletedEvent.java # 配送完成领域事件
│   └── exception
│       ├── DomainException.java         # 基础领域异常
│       └── InvalidOrderStateException.java # 无效状态转换异常
└── infrastructure
    ├── persistence
    │   ├── InMemoryOrderRepository.java # 订单内存仓储实现
    │   └── InMemoryDeliveryRepository.java # 配送内存仓储实现
    └── service
        ├── PaymentService.java          # 模拟支付服务
        ├── NotificationService.java     # 模拟通知服务
        └── DeliveryBatchServiceImpl.java # 配送批处理服务实现
```

### 前端项目结构

```
frontend/
├── src/
│   ├── components/
│   │   ├── layout/
│   │   │   ├── Header.tsx          # 导航头部
│   │   │   └── Layout.tsx           # 主布局包装器
│   │   └── order/
│   │       ├── OrderCard.tsx        # 订单摘要卡片
│   │       └── OrderStatusBadge.tsx  # 状态指示器
│   ├── pages/
│   │   ├── HomePage.tsx            # 首页
│   │   ├── OrderListPage.tsx      # 订单列表页（带过滤）
│   │   ├── CreateOrderPage.tsx     # 订单创建表单
│   │   ├── OrderDetailsPage.tsx    # 订单详情视图
│   │   └── DashboardPage.tsx       # 统计仪表板
│   ├── hooks/
│   │   └── useOrders.ts            # React Query hooks
│   ├── services/
│   │   └── api/
│   │       ├── client.ts             # Axios 配置
│   │       └── orders.ts             # API 端点
│   ├── types/
│   │   └── order.ts               # TypeScript 类型
│   ├── utils/
│   │   ├── constants.ts            # 应用常量
│   │   └── formatters.ts          # 工具函数
│   ├── styles/
│   │   ├── theme.ts               # MUI 主题
│   │   └── global.css             # 全局样式
│   ├── App.tsx                     # App 组件（路由）
│   └── main.tsx                    # 应用入口点
├── package.json
├── tsconfig.json
├── vite.config.ts
└── .eslintrc.cjs
```

## 🔑 核心概念详解

### 领域驱动设计（DDD）

**什么是领域驱动设计？**
领域驱动设计（Domain-Driven Design，简称 DDD）是一种软件开发方法，通过将业务领域作为核心来构建软件系统。DDD 强调与领域专家合作，使用统一的领域语言（Ubiquitous Language），并将复杂的业务逻辑封装在领域模型中。

**本项目中体现的 DDD 概念：**

#### 1. 限界上下文（Bounded Context）
订单上下文管理订单的整个生命周期。限界上下文是 DDD 中的一个核心概念，它定义了特定领域模型的边界。在限界上下文内部，所有的术语和概念都有明确的含义，不会产生歧义。

#### 2. 聚合（Aggregates）
- [`Order`](src/main/java/com/bluemountain/coffee/domain/model/aggregate/Order.java) 作为聚合根，包含 [`OrderItem`](src/main/java/com/bluemountain/coffee/domain/model/aggregate/OrderItem.java) 实体
- [`Delivery`](src/main/java/com/bluemountain/coffee/domain/model/aggregate/Delivery.java) 作为聚合根，包含 [`DeliveryItem`](src/main/java/com/bluemountain/coffee/domain/model/aggregate/DeliveryItem.java) 实体

**什么是聚合？**
聚合是一组相关对象的集合，它们作为一个整体被对待。聚合根是聚合的入口点，外部对象只能通过聚合根来访问聚合内部的实体。这样可以确保聚合内部的数据一致性，避免直接修改内部实体导致的数据不一致问题。

#### 3. 值对象（Value Objects）
由其属性定义的不可变对象（[`Money`](src/main/java/com/bluemountain/coffee/domain/model/valobj/Money.java)、[`Address`](src/main/java/com/bluemountain/coffee/domain/model/valobj/Address.java)、[`OrderId`](src/main/java/com/bluemountain/coffee/domain/model/valobj/OrderId.java)、[`DeliveryId`](src/main/java/com/bluemountain/coffee/domain/model/valobj/DeliveryId.java)、[`RiderInfo`](src/main/java/com/bluemountain/coffee/domain/model/valobj/RiderInfo.java)、[`DeliverySlip`](src/main/java/com/bluemountain/coffee/domain/model/valobj/DeliverySlip.java)）。

**什么是值对象？**
值对象是通过其属性值来标识的对象，而不是通过唯一标识符。值对象是不可变的，一旦创建就不能修改。如果需要修改，就创建一个新的值对象。值对象通常用于描述领域中的概念，如金额、地址、日期范围等。

#### 4. 领域服务（Domain Services）
- [`PricingStrategy`](src/main/java/com/bluemountain/coffee/domain/service/PricingStrategy.java) 用于定价逻辑
- [`DeliveryBatchService`](src/main/java/com/bluemountain/coffee/domain/service/DeliveryBatchService.java) 用于配送批处理逻辑

**什么是领域服务？**
当某些业务逻辑不适合放在实体或值对象中时，就使用领域服务。领域服务是无状态的，它执行的操作通常涉及多个领域对象，或者不属于任何特定的领域对象。在本项目中，定价策略和配送批处理服务都是领域服务，因为它们涉及多个订单或配送的计算和协调。

#### 5. 领域事件（Domain Events）
- 订单相关：[`OrderCreatedEvent`](src/main/java/com/bluemountain/coffee/domain/event/OrderCreatedEvent.java)、[`CoffeeReadyEvent`](src/main/java/com/bluemountain/coffee/domain/event/CoffeeReadyEvent.java)
- 配送相关：[`DeliveryCreatedEvent`](src/main/java/com/bluemountain/coffee/domain/event/DeliveryCreatedEvent.java)、[`DeliveryAssignedEvent`](src/main/java/com/bluemountain/coffee/domain/event/DeliveryAssignedEvent.java)、[`DeliveryPickedUpEvent`](src/main/java/com/bluemountain/coffee/domain/event/DeliveryPickedUpEvent.java)、[`DeliveryDeliveredEvent`](src/main/java/com/bluemountain/coffee/domain/event/DeliveryDeliveredEvent.java)、[`DeliveryCompletedEvent`](src/main/java/com/bluemountain/coffee/domain/event/DeliveryCompletedEvent.java)

**什么是领域事件？**
领域事件是在领域内发生的、对业务有意义的事情。领域事件通常用于实现松耦合的系统架构，当某个重要事件发生时，系统可以发布事件，其他部分可以订阅并处理这些事件。在本项目中，订单创建、咖啡准备就绪以及配送的各个状态变化都是重要的业务事件。

### Spring Boot 特性

- **依赖注入**：所有依赖通过构造函数注入
- **服务层**：[`OrderAppService`](src/main/java/com/bluemountain/coffee/application/OrderAppService.java)、[`DeliveryAppService`](src/main/java/com/bluemountain/coffee/application/DeliveryAppService.java) 协调用例
- **REST 控制器**：[`OrderController`](src/main/java/com/bluemountain/coffee/interfaces/web/OrderController.java)、[`DeliveryController`](src/main/java/com/bluemountain/coffee/interfaces/web/DeliveryController.java)、[`PlatformController`](src/main/java/com/bluemountain/coffee/interfaces/platform/PlatformController.java)
- **事件处理**：[`OrderEventListener`](src/main/java/com/bluemountain/coffee/application/OrderEventListener.java)、[`DeliveryEventListener`](src/main/java/com/bluemountain/coffee/application/DeliveryEventListener.java) 处理领域事件
- **事务管理**：`@Transactional` 确保数据一致性
- **内存存储**：[`InMemoryOrderRepository`](src/main/java/com/bluemountain/coffee/infrastructure/persistence/InMemoryOrderRepository.java)、[`InMemoryDeliveryRepository`](src/main/java/com/bluemountain/coffee/infrastructure/persistence/InMemoryDeliveryRepository.java) 用于简化演示

### React 前端特性

- **组件化架构**：使用 React Hooks 和函数式组件
- **类型安全**：TypeScript 提供完整的类型检查
- **状态管理**：
  - **React Query**：管理服务器状态（API 数据）
  - **React Context**：全局应用状态（主题、通知）
  - **Local State**：表单输入和 UI 交互
- **路由**：React Router v6 提供客户端路由
- **UI 组件**：Material-UI (MUI) 提供企业级组件库
- **HTTP 客户端**：Axios 用于 API 调用
- **开发工具**：Vite 提供快速热更新和构建
- **代码规范**：ESLint 和 Prettier 确保代码质量

### 前端功能特性

1. **首页** - 咖啡店信息展示和快速导航
2. **订单管理** - 创建、查看、更新和取消订单
3. **订单创建** - 动态表单、实时价格计算、地址验证
4. **订单列表** - 按状态过滤、搜索功能
5. **订单详情** - 完整订单信息展示
6. **仪表板** - 统计数据、订单状态分布
7. **响应式设计** - 支持桌面和移动设备

### 订单状态流转

```
CREATED → PAID → PREPARING → READY → COMPLETED
                 ↓
              CANCELLED
```

### 配送状态流转

```
CREATED → ASSIGNED → PICKED_UP → IN_TRANSIT → DELIVERED → COMPLETED
           ↓
        CANCELLED
```

## 📦 配送管理功能

### 配送生命周期管理

系统提供完整的配送生命周期管理，支持以下状态转换：

1. **已创建 (CREATED)** - 配送批次创建完成，等待分配骑手
2. **已分配 (ASSIGNED)** - 骑手已分配，准备取货
3. **已取货 (PICKED_UP)** - 骑手已取货，开始配送
4. **配送中 (IN_TRANSIT)** - 配送途中
5. **已送达 (DELIVERED)** - 已送达客户
6. **已完成 (COMPLETED)** - 配送流程完成
7. **已取消 (CANCELLED)** - 配送取消（仅在取货前可取消）

### 配送批处理功能

系统支持智能配送批处理，优化配送效率：

- **地理位置优先**：相同地址的订单优先合并
- **时间窗口**：15分钟内的订单可合并
- **容量限制**：每个配送批次最多包含5个订单
- **自动批处理**：订单准备就绪时自动触发批处理
- **手动批处理**：支持手动创建配送批次

### 配送查询功能

系统提供多种查询方式：

- 根据 ID 查询配送
- 根据状态查询配送
- 根据骑手查询配送
- 根据订单 ID 查询配送
- 查询所有活跃配送
- 查询日期范围内的配送
- 查询所有配送

### 配送事件驱动架构

系统采用事件驱动架构，配送过程中的关键状态变化会发布领域事件：

- [`DeliveryCreatedEvent`](src/main/java/com/bluemountain/coffee/domain/event/DeliveryCreatedEvent.java) - 配送创建事件
- [`DeliveryAssignedEvent`](src/main/java/com/bluemountain/coffee/domain/event/DeliveryAssignedEvent.java) - 骑手分配事件
- [`DeliveryPickedUpEvent`](src/main/java/com/bluemountain/coffee/domain/event/DeliveryPickedUpEvent.java) - 取货事件
- [`DeliveryDeliveredEvent`](src/main/java/com/bluemountain/coffee/domain/event/DeliveryDeliveredEvent.java) - 送达事件
- [`DeliveryCompletedEvent`](src/main/java/com/bluemountain/coffee/domain/event/DeliveryCompletedEvent.java) - 完成事件

这些事件由 [`DeliveryEventListener`](src/main/java/com/bluemountain/coffee/application/DeliveryEventListener.java) 处理，用于日志记录、通知等后续处理。

### 配送聚合设计

[`Delivery`](src/main/java/com/bluemountain/coffee/domain/model/aggregate/Delivery.java) 作为聚合根，包含以下实体和值对象：

**实体：**
- [`DeliveryItem`](src/main/java/com/bluemountain/coffee/domain/model/aggregate/DeliveryItem.java) - 配送项，代表一个订单

**值对象：**
- [`DeliveryId`](src/main/java/com/bluemountain/coffee/domain/model/valobj/DeliveryId.java) - 配送唯一标识
- [`RiderInfo`](src/main/java/com/bluemountain/coffee/domain/model/valobj/RiderInfo.java) - 骑手信息
- [`DeliverySlip`](src/main/java/com/bluemountain/coffee/domain/model/valobj/DeliverySlip.java) - 配送单（用于打印）
- [`DeliverySlipItem`](src/main/java/com/bluemountain/coffee/domain/model/valobj/DeliverySlipItem.java) - 配送单项

**枚举：**
- [`DeliveryStatus`](src/main/java/com/bluemountain/coffee/domain/model/enums/DeliveryStatus.java) - 配送状态
- [`DeliveryItemStatus`](src/main/java/com/bluemountain/coffee/domain/model/enums/DeliveryItemStatus.java) - 配送项状态

### 配送服务层

- [`DeliveryAppService`](src/main/java/com/bluemountain/coffee/application/DeliveryAppService.java) - 配送应用服务，协调配送相关的业务用例
- [`DeliveryBatchService`](src/main/java/com/bluemountain/coffee/domain/service/DeliveryBatchService.java) - 配送批处理服务接口
- [`DeliveryBatchServiceImpl`](src/main/java/com/bluemountain/coffee/infrastructure/service/DeliveryBatchServiceImpl.java) - 配送批处理服务实现

### 配送仓储层

- [`DeliveryRepository`](src/main/java/com/bluemountain/coffee/domain/repository/DeliveryRepository.java) - 配送仓储接口
- [`InMemoryDeliveryRepository`](src/main/java/com/bluemountain/coffee/infrastructure/persistence/InMemoryDeliveryRepository.java) - 配送内存仓储实现

## 🧪 测试

### 后端测试

使用 Maven 运行测试：

```bash
mvn test
```

### 前端测试

运行前端测试（当实现时）：

```bash
cd frontend
npm run test
```

### 前端代码检查

```bash
cd frontend
npm run lint
```

## 📖 学习要点

### 后端学习要点

本项目演示了：

1. **如何使用 DDD 分层结构组织 Spring Boot 应用**
2. **何时使用设计模式以及如何正确实现它们**
3. **如何在实际场景中应用面向对象原则**
4. **如何有效使用 Spring Boot 特性**
5. **如何处理领域事件并实现事件驱动架构**
6. **如何创建具有良好错误处理的干净 REST API**
7. **如何实现复杂的聚合根和实体关系**
8. **如何设计和管理状态机（订单和配送状态流转）**
9. **如何实现批处理和优化业务流程**
10. **如何通过领域事件实现解耦的系统架构**

### 前端学习要点

本项目演示了：

1. **如何使用 React 18 和 TypeScript 构建现代前端应用**
2. **如何使用 React Query 进行服务器状态管理**
3. **如何使用 Material-UI 构建企业级 UI**
4. **如何实现客户端路由和导航**
5. **如何处理表单验证和用户输入**
6. **如何实现响应式设计和移动端适配**
7. **如何使用自定义 Hooks 复用逻辑**
8. **如何与 REST API 进行集成**

## 🎓 学习建议

- 代码中包含大量注释，解释了设计模式和面向对象原则
- 每个类都演示了特定概念，并有清晰的文档说明
- 内存仓储让重点放在概念上，而不是基础设施上
- 模拟服务展示了集成点，无需外部依赖

**学习路径建议：**

### 后端学习路径

1. **第一步**：先了解项目结构，理解 DDD 的分层架构
2. **第二步**：阅读领域模型代码，理解聚合、实体、值对象的概念
3. **第三步**：学习设计模式的实现，理解每个模式解决的问题
4. **第四步**：查看应用服务层，理解如何协调领域对象完成业务用例
5. **第五步**：研究控制器层，理解如何暴露 REST API
6. **第六步**：学习配送模块，理解复杂的聚合根设计和状态管理
7. **第七步**：研究事件驱动架构，理解领域事件的发布和订阅机制
8. **第八步**：运行项目，通过 API 测试各个功能

### 前端学习路径

1. **第一步**：了解前端项目结构，理解组件化架构
2. **第二步**：学习 React Hooks 和 TypeScript 的使用
3. **第三步**：研究 React Query 的状态管理机制
4. **第四步**：学习 Material-UI 组件库的使用
5. **第五步**：理解路由配置和页面导航
6. **第六步**：研究 API 集成和数据流
7. **第七步**：运行前端应用，体验完整功能

### 全栈学习路径

1. **第一步**：分别启动后端和前端服务
2. **第二步**：通过前端 UI 创建订单，观察后端 API 调用
3. **第三步**：理解前后端数据交互流程
4. **第四步**：修改前端或后端代码，观察变化
5. **第五步**：添加新功能或改进现有功能

## 🛠️ 可用脚本

### 前端脚本

- `npm run dev` - 启动开发服务器（端口 3000）
- `npm run build` - 构建生产版本
- `npm run preview` - 预览生产构建
- `npm run lint` - 运行 ESLint 检查

### 后端脚本

- `mvn clean install` - 清理并构建项目
- `mvn spring-boot:run` - 运行 Spring Boot 应用
- `mvn test` - 运行测试

## 📚 相关文档

### 项目文档

- [`Delivery_IMPLEMENTATION_SUMMARY.md`](plans/Delivery_IMPLEMENTATION_SUMMARY.md) - 配送批处理架构实现总结
- [`delivery-batch-architecture.md`](plans/delivery-batch-architecture.md) - 配送批处理架构设计文档
- [`implementation-plan.md`](plans/implementation-plan.md) - 项目实施计划
- [`react-frontend-architecture.md`](plans/react-frontend-architecture.md) - React 前端架构设计

### 后端文档

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Domain-Driven Design](https://martinfowler.com/tags/domain%20driven%20design.html)
- [Design Patterns](https://refactoring.guru/design-patterns)

### 前端文档

- [React Documentation](https://react.dev/)
- [TypeScript Documentation](https://www.typescriptlang.org/)
- [Material-UI Documentation](https://mui.com/)
- [React Query Documentation](https://tanstack.com/query/latest)
- [React Router Documentation](https://reactrouter.com/)
- [Vite Documentation](https://vitejs.dev/)

## 🚀 部署

### 后端部署

可以将 Spring Boot 应用部署到：
- 传统服务器（Tomcat, Jetty）
- 云平台（AWS, Azure, Google Cloud）
- 容器化环境（Docker, Kubernetes）

### 前端部署

前端可以部署到任何静态托管服务：
- Vercel
- Netlify
- GitHub Pages
- AWS S3 + CloudFront

**注意**：部署前端时，需要更新 `vite.config.ts` 中的 API 代理配置为生产环境的后端 URL。

## 🔧 故障排除

### 后端连接问题

如果无法连接到后端 API：
1. 确保后端服务正在运行（`http://localhost:8080`）
2. 检查后端日志是否有错误
3. 验证 API 端点是否正确

### 前端连接问题

如果看到 CORS 错误或连接失败：
1. 确保后端服务正在运行
2. 检查 `vite.config.ts` 中的 API 代理配置
3. 验证后端是否允许跨域请求

### 构建错误

如果遇到 TypeScript 错误：
1. 确保所有依赖已安装（`npm install`）
2. 检查 TypeScript 配置
3. 清理 `node_modules` 并重新安装

## 🎯 配送管理实现成果

配送管理模块的成功实现带来了以下收益：

1. **✅ 完整的可追溯性** - 从创建到完成的每个配送环节都可追踪
2. **✅ 批处理优化** - 智能合并订单，提高配送效率
3. **✅ 骑手管理** - 跟踪骑手分配和绩效
4. **✅ 分析就绪** - 查询配送历史，获取业务洞察
5. **✅ 灵活性** - 易于添加新的配送功能
6. **✅ DDD 合规** - 正确的聚合边界和领域建模
7. **✅ 事件驱动** - 使用领域事件的解耦架构
8. **✅ REST API** - 完整的配送管理 CRUD 操作
9. **✅ 测试覆盖** - 全面的单元测试覆盖

## 🤝 贡献

这是一个教育项目。欢迎 Fork 并修改它用于学习目的。

## 📄 许可证

本项目仅用于教育目的。

## 👨‍💻 作者

蓝山咖啡店团队

---

**祝学习愉快！☕**
