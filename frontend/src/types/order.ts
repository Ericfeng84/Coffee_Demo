export interface Order {
  id: string;
  customerName: string;
  orderType: OrderType;
  status: OrderStatus;
  items: OrderItem[];
  totalPrice: number;
  createdAt: string;
  updatedAt: string;
  address?: Address;
}

export interface OrderItem {
  productName: string;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
}

export interface Address {
  street: string;
  city: string;
  postalCode: string;
  country: string;
}

export interface CreateOrderCommand {
  customerName: string;
  orderType: OrderType;
  items: OrderItem[];
  street?: string;
  city?: string;
  postalCode?: string;
  country?: string;
}

export type OrderType = 'DINE_IN' | 'DELIVERY';

export type OrderStatus =
  | 'CREATED'
  | 'PAID'
  | 'PREPARING'
  | 'READY'
  | 'COMPLETED'
  | 'CANCELLED';

export interface OrderStatusOption {
  value: OrderStatus;
  label: string;
  color: string;
}
