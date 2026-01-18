import {
  Card,
  CardContent,
  CardActions,
  Typography,
  Box,
  Button,
  Chip,
  Divider,
} from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import type { Order } from '../../types';
import { OrderStatusBadge } from './OrderStatusBadge';
import { formatCurrency, formatDate } from '../../utils/formatters';
import { ORDER_TYPE_LABELS } from '../../utils/constants';

interface OrderCardProps {
  order: Order;
}

export function OrderCard({ order }: OrderCardProps) {
  return (
    <Card>
      <CardContent>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
          <Typography variant="h6" component="div">
            订单 #{order.id}
          </Typography>
          <OrderStatusBadge status={order.status} />
        </Box>

        <Box sx={{ mb: 2 }}>
          <Typography variant="body2" color="text.secondary">
            客户: {order.customerName}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            类型: {ORDER_TYPE_LABELS[order.orderType]}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            创建时间: {formatDate(order.createdAt)}
          </Typography>
        </Box>

        <Divider sx={{ my: 2 }} />

        <Box sx={{ mb: 2 }}>
          <Typography variant="subtitle2" gutterBottom>
            项目:
          </Typography>
          {order.items.map((item, index) => (
            <Box key={index} sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
              <Typography variant="body2">
                {item.quantity}x {item.productName}
              </Typography>
              <Typography variant="body2">
                {formatCurrency(item.totalPrice)}
              </Typography>
            </Box>
          ))}
        </Box>

        <Divider sx={{ my: 2 }} />

        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Typography variant="h6" color="primary">
            总计: {formatCurrency(order.totalPrice)}
          </Typography>
        </Box>
      </CardContent>
      <CardActions>
        <Button
          size="small"
          component={RouterLink}
          to={`/orders/${order.id}`}
          variant="contained"
        >
          查看详情
        </Button>
      </CardActions>
    </Card>
  );
}
