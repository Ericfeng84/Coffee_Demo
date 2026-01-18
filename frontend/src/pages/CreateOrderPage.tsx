import { useState } from 'react';
import {
  Container,
  Typography,
  Box,
  TextField,
  Button,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Card,
  CardContent,
  Divider,
  Alert,
  IconButton,
} from '@mui/material';
import { Delete as DeleteIcon, Add as AddIcon } from '@mui/icons-material';
import { useCreateOrder } from '../hooks/useOrders';
import { MENU_ITEMS } from '../utils/constants';
import type { OrderItem as OrderItemType, OrderType } from '../types';

interface FormOrderItem extends OrderItemType {
  id: string;
}

export function CreateOrderPage() {
  const createOrder = useCreateOrder();
  const [customerName, setCustomerName] = useState('');
  const [orderType, setOrderType] = useState<OrderType>('DINE_IN');
  const [items, setItems] = useState<FormOrderItem[]>([
    { id: '1', productName: '', quantity: 1, unitPrice: 0, totalPrice: 0 },
  ]);
  const [address, setAddress] = useState({
    street: '',
    city: '',
    postalCode: '',
    country: '',
  });

  const handleAddItem = () => {
    setItems([
      ...items,
      {
        id: Date.now().toString(),
        productName: '',
        quantity: 1,
        unitPrice: 0,
        totalPrice: 0,
      },
    ]);
  };

  const handleRemoveItem = (id: string) => {
    setItems(items.filter((item) => item.id !== id));
  };

  const handleItemChange = (id: string, field: keyof FormOrderItem, value: any) => {
    const newItems = items.map((item) => {
      if (item.id === id) {
        const updatedItem = { ...item, [field]: value };
        if (field === 'quantity' || field === 'unitPrice') {
          updatedItem.totalPrice = updatedItem.quantity * updatedItem.unitPrice;
        }
        return updatedItem;
      }
      return item;
    });
    setItems(newItems);
  };

  const handleMenuSelect = (index: number, productName: string, price: number) => {
    handleItemChange(items[index].id, 'productName', productName);
    handleItemChange(items[index].id, 'unitPrice', price);
  };

  const calculateTotal = () => {
    return items.reduce((sum, item) => sum + item.totalPrice, 0);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    const orderItems = items.map(({ id, ...item }) => item);

    const orderData = {
      customerName,
      orderType,
      items: orderItems,
      ...(orderType === 'DELIVERY' ? address : {}),
    };

    createOrder.mutate(orderData);
  };

  const isValid = () => {
    if (!customerName.trim()) return false;
    if (items.length === 0) return false;
    if (items.some((item) => !item.productName || item.quantity < 1 || item.unitPrice < 0)) {
      return false;
    }
    if (orderType === 'DELIVERY') {
      if (!address.street || !address.city || !address.postalCode || !address.country) {
        return false;
      }
    }
    return true;
  };

  return (
    <Container maxWidth="md">
      <Typography variant="h4" component="h1" gutterBottom>
        创建订单
      </Typography>

      {createOrder.isSuccess && (
        <Alert severity="success" sx={{ mb: 3 }}>
          订单创建成功！
        </Alert>
      )}

      {createOrder.isError && (
        <Alert severity="error" sx={{ mb: 3 }}>
          创建订单失败，请重试。
        </Alert>
      )}

      <form onSubmit={handleSubmit}>
        <Card sx={{ mb: 3 }}>
          <CardContent>
            <Typography variant="h6" gutterBottom>
              客户信息
            </Typography>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
              <TextField
                label="客户姓名"
                value={customerName}
                onChange={(e) => setCustomerName(e.target.value)}
                required
                fullWidth
              />
              <FormControl fullWidth>
                <InputLabel>订单类型</InputLabel>
                <Select
                  value={orderType}
                  label="订单类型"
                  onChange={(e) => setOrderType(e.target.value as OrderType)}
                >
                  <MenuItem value="DINE_IN">堂食</MenuItem>
                  <MenuItem value="DELIVERY">外送</MenuItem>
                </Select>
              </FormControl>
            </Box>
          </CardContent>
        </Card>

        {orderType === 'DELIVERY' && (
          <Card sx={{ mb: 3 }}>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                配送地址
              </Typography>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                <TextField
                  label="街道"
                  value={address.street}
                  onChange={(e) => setAddress({ ...address, street: e.target.value })}
                  required
                  fullWidth
                />
                <TextField
                  label="城市"
                  value={address.city}
                  onChange={(e) => setAddress({ ...address, city: e.target.value })}
                  required
                  fullWidth
                />
                <TextField
                  label="邮政编码"
                  value={address.postalCode}
                  onChange={(e) => setAddress({ ...address, postalCode: e.target.value })}
                  required
                  fullWidth
                />
                <TextField
                  label="国家"
                  value={address.country}
                  onChange={(e) => setAddress({ ...address, country: e.target.value })}
                  required
                  fullWidth
                />
              </Box>
            </CardContent>
          </Card>
        )}

        <Card sx={{ mb: 3 }}>
          <CardContent>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
              <Typography variant="h6">订单项目</Typography>
              <Button
                startIcon={<AddIcon />}
                onClick={handleAddItem}
                size="small"
              >
                添加项目
              </Button>
            </Box>

            {items.map((item, index) => (
              <Box key={item.id} sx={{ mb: 2 }}>
                <Divider sx={{ mb: 2 }} />
                <Box sx={{ display: 'flex', gap: 2, mb: 2 }}>
                  <FormControl fullWidth>
                    <InputLabel>从菜单选择</InputLabel>
                    <Select
                      label="从菜单选择"
                      onChange={(e) => {
                        const selectedItem = MENU_ITEMS.find((m) => m.name === e.target.value);
                        if (selectedItem) {
                          handleMenuSelect(index, selectedItem.name, selectedItem.price);
                        }
                      }}
                    >
                      {MENU_ITEMS.map((menuItem) => (
                        <MenuItem key={menuItem.name} value={menuItem.name}>
                          {menuItem.name} - ${menuItem.price.toFixed(2)}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                  <IconButton
                    onClick={() => handleRemoveItem(item.id)}
                    color="error"
                    disabled={items.length === 1}
                  >
                    <DeleteIcon />
                  </IconButton>
                </Box>
                <Box sx={{ display: 'flex', gap: 2 }}>
                  <TextField
                    label="产品名称"
                    value={item.productName}
                    onChange={(e) => handleItemChange(item.id, 'productName', e.target.value)}
                    required
                    fullWidth
                  />
                  <TextField
                    label="数量"
                    type="number"
                    value={item.quantity}
                    onChange={(e) =>
                      handleItemChange(item.id, 'quantity', parseInt(e.target.value) || 0)
                    }
                    required
                    inputProps={{ min: 1 }}
                    sx={{ width: 120 }}
                  />
                  <TextField
                    label="单价"
                    type="number"
                    value={item.unitPrice}
                    onChange={(e) =>
                      handleItemChange(item.id, 'unitPrice', parseFloat(e.target.value) || 0)
                    }
                    required
                    inputProps={{ min: 0, step: 0.01 }}
                    sx={{ width: 150 }}
                  />
                </Box>
              </Box>
            ))}
          </CardContent>
        </Card>

        <Card sx={{ mb: 3 }}>
          <CardContent>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <Typography variant="h6">总计: ${calculateTotal().toFixed(2)}</Typography>
              <Button
                type="submit"
                variant="contained"
                size="large"
                disabled={!isValid() || createOrder.isPending}
              >
                {createOrder.isPending ? '创建中...' : '创建订单'}
              </Button>
            </Box>
          </CardContent>
        </Card>
      </form>
    </Container>
  );
}
