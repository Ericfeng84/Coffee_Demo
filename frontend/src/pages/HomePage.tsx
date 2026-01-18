import { Container, Typography, Button, Box, Card, CardContent } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import LocalCafeIcon from '@mui/icons-material/LocalCafe';
import DeliveryDiningIcon from '@mui/icons-material/DeliveryDining';
import RestaurantIcon from '@mui/icons-material/Restaurant';
import AssignmentIcon from '@mui/icons-material/Assignment';

export function HomePage() {
  return (
    <Container maxWidth="lg">
      <Box sx={{ textAlign: 'center', my: 8 }}>
        <LocalCafeIcon sx={{ fontSize: 80, color: 'primary.main', mb: 2 }} />
        <Typography variant="h2" component="h1" gutterBottom>
          欢迎来到蓝山咖啡
        </Typography>
        <Typography variant="h5" color="text.secondary" paragraph>
          优质咖啡，卓越服务
        </Typography>
        <Box sx={{ mt: 4, display: 'flex', gap: 2, justifyContent: 'center' }}>
          <Button
            variant="contained"
            size="large"
            component={RouterLink}
            to="/orders/create"
            startIcon={<RestaurantIcon />}
          >
            创建订单
          </Button>
          <Button
            variant="outlined"
            size="large"
            component={RouterLink}
            to="/orders"
            startIcon={<AssignmentIcon />}
          >
            查看订单
          </Button>
        </Box>
      </Box>

      <Box sx={{ display: 'flex', gap: 4, flexWrap: 'wrap', my: 8 }}>
        <Card sx={{ flex: 1, minWidth: 300, mb: 2 }}>
          <CardContent sx={{ textAlign: 'center' }}>
            <RestaurantIcon sx={{ fontSize: 48, color: 'primary.main', mb: 2 }} />
            <Typography variant="h6" gutterBottom>
              堂食
            </Typography>
            <Typography variant="body2" color="text.secondary">
              享受我们舒适的环境和新鲜冲泡的咖啡
            </Typography>
          </CardContent>
        </Card>
        <Card sx={{ flex: 1, minWidth: 300, mb: 2 }}>
          <CardContent sx={{ textAlign: 'center' }}>
            <DeliveryDiningIcon sx={{ fontSize: 48, color: 'primary.main', mb: 2 }} />
            <Typography variant="h6" gutterBottom>
              外送
            </Typography>
            <Typography variant="body2" color="text.secondary">
              将您喜爱的咖啡送到家门口
            </Typography>
          </CardContent>
        </Card>
        <Card sx={{ flex: 1, minWidth: 300, mb: 2 }}>
          <CardContent sx={{ textAlign: 'center' }}>
            <LocalCafeIcon sx={{ fontSize: 48, color: 'primary.main', mb: 2 }} />
            <Typography variant="h6" gutterBottom>
              优质品质
            </Typography>
            <Typography variant="body2" color="text.secondary">
              选用全球最优质的咖啡豆
            </Typography>
          </CardContent>
        </Card>
      </Box>
    </Container>
  );
}
