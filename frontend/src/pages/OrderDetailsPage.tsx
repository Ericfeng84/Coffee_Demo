import { useParams, useNavigate } from 'react-router-dom';
import {
  Container,
  Typography,
  Box,
  CircularProgress,
  Alert,
  Card,
  CardContent,
  Divider,
  Button,
  Chip,
} from '@mui/material';
import { useOrder, useMarkReady, useCompleteOrder, useCancelOrder } from '../hooks/useOrders';
import { OrderStatusBadge } from '../components/order/OrderStatusBadge';
import { formatCurrency, formatDate } from '../utils/formatters';
import { ORDER_TYPE_LABELS } from '../utils/constants';

export function OrderDetailsPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { data: order, isLoading, error } = useOrder(id || '');
  const markReady = useMarkReady();
  const completeOrder = useCompleteOrder();
  const cancelOrder = useCancelOrder();

  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error || !order) {
    return (
      <Container maxWidth="md">
        <Alert severity="error" sx={{ mt: 4 }}>
          加载订单错误
        </Alert>
      </Container>
    );
  }

  const handleMarkReady = () => {
    markReady.mutate(order.id);
  };

  const handleComplete = () => {
    completeOrder.mutate(order.id);
  };

  const handleCancel = () => {
    if (window.confirm('确定要取消此订单吗？')) {
      cancelOrder.mutate(order.id);
    }
  };

  const canMarkReady = order.status === 'PAID' || order.status === 'PREPARING';
  const canComplete = order.status === 'READY';
  const canCancel = order.status === 'CREATED' || order.status === 'PAID';

  return (
    <Container maxWidth="md">
      <Box sx={{ mb: 3, display: 'flex', alignItems: 'center', gap: 2 }}>
        <Button variant="outlined" onClick={() => navigate('/orders')}>
          返回订单列表
        </Button>
      </Box>

      <Card>
        <CardContent>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
            <Typography variant="h4">订单 #{order.id}</Typography>
            <OrderStatusBadge status={order.status} />
          </Box>

          <Divider sx={{ my: 2 }} />

          <Box sx={{ mb: 3 }}>
            <Typography variant="h6" gutterBottom>
              客户信息
            </Typography>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
              <Typography variant="body1">
                <strong>客户:</strong> {order.customerName}
              </Typography>
              <Typography variant="body1">
                <strong>类型:</strong> {ORDER_TYPE_LABELS[order.orderType]}
              </Typography>
              <Typography variant="body1">
                <strong>创建时间:</strong> {formatDate(order.createdAt)}
              </Typography>
              <Typography variant="body1">
                <strong>更新时间:</strong> {formatDate(order.updatedAt)}
              </Typography>
            </Box>
          </Box>

          {order.address && (
            <>
              <Divider sx={{ my: 2 }} />
              <Box sx={{ mb: 3 }}>
                <Typography variant="h6" gutterBottom>
                  配送地址
                </Typography>
                <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                  <Typography variant="body1">{order.address.street}</Typography>
                  <Typography variant="body1">
                    {order.address.city}, {order.address.postalCode}
                  </Typography>
                  <Typography variant="body1">{order.address.country}</Typography>
                </Box>
              </Box>
            </>
          )}

          <Divider sx={{ my: 2 }} />

          <Box sx={{ mb: 3 }}>
            <Typography variant="h6" gutterBottom>
              订单项目
            </Typography>
            {order.items.map((item, index) => (
              <Box
                key={index}
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
                    {item.productName}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {item.quantity} x {formatCurrency(item.unitPrice)}
                  </Typography>
                </Box>
                <Typography variant="body1" fontWeight="bold">
                  {formatCurrency(item.totalPrice)}
                </Typography>
              </Box>
            ))}
          </Box>

          <Divider sx={{ my: 2 }} />

          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <Typography variant="h5">总计</Typography>
            <Typography variant="h5" color="primary" fontWeight="bold">
              {formatCurrency(order.totalPrice)}
            </Typography>
          </Box>

          <Divider sx={{ my: 2 }} />

          <Box sx={{ display: 'flex', gap: 2, flexWrap: 'wrap' }}>
            {canMarkReady && (
              <Button
                variant="contained"
                onClick={handleMarkReady}
                disabled={markReady.isPending}
              >
                {markReady.isPending ? '更新中...' : '标记为就绪'}
              </Button>
            )}
            {canComplete && (
              <Button
                variant="contained"
                color="success"
                onClick={handleComplete}
                disabled={completeOrder.isPending}
              >
                {completeOrder.isPending ? '完成中...' : '完成订单'}
              </Button>
            )}
            {canCancel && (
              <Button
                variant="outlined"
                color="error"
                onClick={handleCancel}
                disabled={cancelOrder.isPending}
              >
                {cancelOrder.isPending ? '取消中...' : '取消订单'}
              </Button>
            )}
          </Box>
        </CardContent>
      </Card>
    </Container>
  );
}
