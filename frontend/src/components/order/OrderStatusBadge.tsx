import { Chip } from '@mui/material';
import type { OrderStatus } from '../../types';
import { ORDER_STATUS_OPTIONS } from '../../utils/constants';

interface OrderStatusBadgeProps {
  status: OrderStatus;
}

export function OrderStatusBadge({ status }: OrderStatusBadgeProps) {
  const statusOption = ORDER_STATUS_OPTIONS.find((opt) => opt.value === status);

  return (
    <Chip
      label={statusOption?.label || status}
      sx={{
        backgroundColor: statusOption?.color || '#9E9E9E',
        color: '#FFFFFF',
        fontWeight: 600,
      }}
    />
  );
}
