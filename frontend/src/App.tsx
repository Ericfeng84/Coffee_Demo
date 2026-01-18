import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider, CssBaseline } from '@mui/material';
import { Layout } from './components/layout/Layout';
import { HomePage } from './pages/HomePage';
import { OrderListPage } from './pages/OrderListPage';
import { CreateOrderPage } from './pages/CreateOrderPage';
import { OrderDetailsPage } from './pages/OrderDetailsPage';
import { DashboardPage } from './pages/DashboardPage';
import theme from './styles/theme';
import './styles/global.css';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      refetchOnWindowFocus: false,
    },
  },
});

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <BrowserRouter>
          <Layout>
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/orders" element={<OrderListPage />} />
              <Route path="/orders/create" element={<CreateOrderPage />} />
              <Route path="/orders/:id" element={<OrderDetailsPage />} />
              <Route path="/dashboard" element={<DashboardPage />} />
              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </Layout>
        </BrowserRouter>
      </ThemeProvider>
    </QueryClientProvider>
  );
}

export default App;
