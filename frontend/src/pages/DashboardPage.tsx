import { useMemo } from 'react';
import {
  Container,
  Typography,
  Box,
  Card,
  CardContent,
  Grid,
  CircularProgress,
  Alert,
} from '@mui/material';
import { useOrders } from '../hooks/useOrders';
import { formatCurrency } from '../utils/formatters';
import { ORDER_STATUS_OPTIONS } from '../utils/constants';

export function DashboardPage() {
  const { data: orders, isLoading, error } = useOrders();

  const stats = useMemo(() => {
    if (!orders) return null;

    const totalOrders = orders.length;
    const totalRevenue = orders.reduce((sum, order) => sum + order.totalPrice, 0);
    const completedOrders = orders.filter((order) => order.status === 'COMPLETED').length;
    const pendingOrders = orders.filter(
      (order) => order.status !== 'COMPLETED' && order.status !== 'CANCELLED'
    ).length;

    const statusDistribution = ORDER_STATUS_OPTIONS.map((statusOption) => ({
      ...statusOption,
      count: orders.filter((order) => order.status === statusOption.value).length,
    }));

    return {
      totalOrders,
      totalRevenue,
      completedOrders,
      pendingOrders,
      statusDistribution,
    };
  }, [orders]);

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
          加载仪表板数据错误
        </Alert>
      </Container>
    );
  }

  if (!stats) return null;

  return (
    <Container maxWidth="lg">
      <Typography variant="h4" component="h1" gutterBottom>
        仪表板
      </Typography>

      <Box sx={{ mb: 4, display: 'flex', gap: 3, flexWrap: 'wrap' }}>
        <Card sx={{ flex: 1, minWidth: 240, mb: 2 }}>
          <CardContent>
            <Typography variant="body2" color="text.secondary" gutterBottom>
              总订单数
            </Typography>
            <Typography variant="h4" color="primary">
              {stats.totalOrders}
            </Typography>
          </CardContent>
        </Card>

        <Card sx={{ flex: 1, minWidth: 240, mb: 2 }}>
          <CardContent>
            <Typography variant="body2" color="text.secondary" gutterBottom>
              总收入
            </Typography>
            <Typography variant="h4" color="primary">
              {formatCurrency(stats.totalRevenue)}
            </Typography>
          </CardContent>
        </Card>

        <Card sx={{ flex: 1, minWidth: 240, mb: 2 }}>
          <CardContent>
            <Typography variant="body2" color="text.secondary" gutterBottom>
              已完成订单
            </Typography>
            <Typography variant="h4" color="success.main">
              {stats.completedOrders}
            </Typography>
          </CardContent>
        </Card>

        <Card sx={{ flex: 1, minWidth: 240, mb: 2 }}>
          <CardContent>
            <Typography variant="body2" color="text.secondary" gutterBottom>
              待处理订单
            </Typography>
            <Typography variant="h4" color="warning.main">
              {stats.pendingOrders}
            </Typography>
          </CardContent>
        </Card>
      </Box>

      <Card sx={{ mb: 4 }}>
        <CardContent>
          <Typography variant="h6" gutterBottom>
            订单状态分布
          </Typography>
          <Box sx={{ display: 'flex', gap: 2, flexWrap: 'wrap' }}>
            {stats.statusDistribution.map((status) => (
              <Box
                key={status.value}
                sx={{
                  p: 2,
                  borderRadius: 2,
                  backgroundColor: status.color,
                  color: '#fff',
                  minWidth: 120,
                  textAlign: 'center',
                }}
              >
                <Typography variant="body2" gutterBottom>
                  {status.label}
                </Typography>
                <Typography variant="h5">{status.count}</Typography>
              </Box>
            ))}
          </Box>
        </CardContent>
      </Card>

      <Card>
        <CardContent>
          <Typography variant="h6" gutterBottom>
            最近订单
          </Typography>
          {!orders || orders.length === 0 ? (
            <Alert severity="info">暂无订单</Alert>
          ) : (
            <Box>
              {orders.slice(0, 5).map((order) => (
                <Box
                  key={order.id}
                  sx={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    py: 1,
                    borderBottom: '1px solid #e0e0e0',
                  }}
                >
                  <Box>
                    <Typography variant="body1" fontWeight="medium">
                      #{order.id} - {order.customerName}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      {order.items.length} 项
                    </Typography>
                  </Box>
                  <Typography variant="body1" fontWeight="bold" color="primary">
                    {formatCurrency(order.totalPrice)}
                  </Typography>
                </Box>
              ))}
            </Box>
          )}
        </CardContent>
      </Card>
    </Container>
  );
}
