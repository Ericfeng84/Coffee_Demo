import { useState, useMemo } from 'react';
import {
  Container,
  Typography,
  Box,
  CircularProgress,
  Alert,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from '@mui/material';
import { useOrders } from '../hooks/useOrders';
import { OrderCard } from '../components/order/OrderCard';
import { ORDER_STATUS_OPTIONS } from '../utils/constants';

export function OrderListPage() {
  const { data: orders, isLoading, error } = useOrders();
  const [statusFilter, setStatusFilter] = useState<string>('ALL');

  const filteredOrders = useMemo(() => {
    if (!orders) return [];
    if (statusFilter === 'ALL') return orders;
    return orders.filter((order) => order.status === statusFilter);
  }, [orders, statusFilter]);

  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Container maxWidth="lg">
        <Alert severity="error" sx={{ mt: 4 }}>
          加载订单错误: {error.message}
        </Alert>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg">
      <Typography variant="h4" component="h1" gutterBottom>
        订单列表
      </Typography>

      <Box sx={{ mb: 4 }}>
        <FormControl sx={{ minWidth: 200 }}>
          <InputLabel>按状态筛选</InputLabel>
          <Select
            value={statusFilter}
            label="按状态筛选"
            onChange={(e) => setStatusFilter(e.target.value)}
          >
            <MenuItem value="ALL">全部订单</MenuItem>
            {ORDER_STATUS_OPTIONS.map((option) => (
              <MenuItem key={option.value} value={option.value}>
                {option.label}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      </Box>

      {filteredOrders.length === 0 ? (
        <Alert severity="info" sx={{ mt: 4 }}>
          未找到订单
        </Alert>
      ) : (
        <Box sx={{ display: 'flex', gap: 3, flexWrap: 'wrap' }}>
          {filteredOrders.map((order) => (
            <Box key={order.id} sx={{ flex: 1, minWidth: 300, mb: 2 }}>
              <OrderCard order={order} />
            </Box>
          ))}
        </Box>
      )}
    </Container>
  );
}
