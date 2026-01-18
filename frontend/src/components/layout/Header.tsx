import { AppBar, Toolbar, Typography, Button, Box } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import LocalCafeIcon from '@mui/icons-material/LocalCafe';

export function Header() {
  return (
    <AppBar position="static" sx={{ mb: 3 }}>
      <Toolbar>
        <Box sx={{ display: 'flex', alignItems: 'center', flexGrow: 1 }}>
          <LocalCafeIcon sx={{ mr: 2, fontSize: 32 }} />
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            蓝山咖啡
          </Typography>
        </Box>
        <Box sx={{ display: 'flex', gap: 2 }}>
          <Button color="inherit" component={RouterLink} to="/">
            首页
          </Button>
          <Button color="inherit" component={RouterLink} to="/orders">
            订单列表
          </Button>
          <Button color="inherit" component={RouterLink} to="/orders/create">
            创建订单
          </Button>
          <Button color="inherit" component={RouterLink} to="/dashboard">
            仪表板
          </Button>
        </Box>
      </Toolbar>
    </AppBar>
  );
}
