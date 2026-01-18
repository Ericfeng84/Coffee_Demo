import type { OrderStatus, OrderStatusOption } from '../types';

export const ORDER_STATUS_OPTIONS: OrderStatusOption[] = [
  { value: 'CREATED', label: '已创建', color: '#9E9E9E' },
  { value: 'PAID', label: '已支付', color: '#2196F3' },
  { value: 'PREPARING', label: '制作中', color: '#FF9800' },
  { value: 'READY', label: '已就绪', color: '#4CAF50' },
  { value: 'COMPLETED', label: '已完成', color: '#009688' },
  { value: 'CANCELLED', label: '已取消', color: '#F44336' },
];

export const ORDER_TYPE_LABELS = {
  DINE_IN: '堂食',
  DELIVERY: '外送',
};

export const MENU_ITEMS = [
  { name: '浓缩咖啡', price: 3.50 },
  { name: '美式咖啡', price: 4.00 },
  { name: '卡布奇诺', price: 4.50 },
  { name: '拿铁', price: 4.00 },
  { name: '摩卡', price: 5.00 },
  { name: '玛奇朵', price: 4.50 },
  { name: '平白咖啡', price: 4.50 },
  { name: '爱尔兰咖啡', price: 6.00 },
];
