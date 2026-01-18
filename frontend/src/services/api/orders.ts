import apiClient from './client';
import type { Order, CreateOrderCommand } from '../../types';

export const ordersApi = {
  // Get all orders
  getAll: () => apiClient.get<Order[]>('/orders'),

  // Get order by ID
  getById: (id: string) => apiClient.get<Order>(`/orders/${id}`),

  // Get orders by status
  getByStatus: (status: string) =>
    apiClient.get<Order[]>(`/orders/status/${status}`),

  // Create order
  create: (data: CreateOrderCommand) =>
    apiClient.post<Order>('/orders', data),

  // Update order status
  updateStatus: (id: string, status: string) =>
    apiClient.put<Order>(`/orders/${id}/status`, null, {
      params: { status },
    }),

  // Mark as ready
  markReady: (id: string) =>
    apiClient.put<Order>(`/orders/${id}/ready`),

  // Complete order
  complete: (id: string) =>
    apiClient.put<Order>(`/orders/${id}/complete`),

  // Cancel order
  cancel: (id: string) =>
    apiClient.delete<Order>(`/orders/${id}`),
};
