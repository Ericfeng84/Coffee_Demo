# Blue Mountain Coffee - Frontend

A React-based frontend application for Blue Mountain Coffee Shop system, built with TypeScript, Vite, and Material-UI.

## Tech Stack

- **React 18** - UI library with hooks
- **TypeScript 5** - Type safety
- **Vite 5** - Fast build tool and dev server
- **Material-UI (MUI) 5** - Component library
- **React Query (TanStack Query)** - Server state management
- **React Router v6** - Client-side routing
- **Axios** - HTTP client

## Getting Started

### Prerequisites

- Node.js 18+ and npm
- Backend API running on `http://localhost:8080`

### Installation

1. Install dependencies:
```bash
cd frontend
npm install
```

2. Start the development server:
```bash
npm run dev
```

3. Open your browser and navigate to `http://localhost:3000`

## Available Scripts

- `npm run dev` - Start development server on port 3000
- `npm run build` - Build for production
- `npm run preview` - Preview production build
- `npm run lint` - Run ESLint

## Features

### Core Functionality

1. **Home Page**
   - Landing page with coffee shop information
   - Quick navigation to create orders or view order list

2. **Order Management**
   - Create new orders (dine-in or delivery)
   - View all orders with filtering by status
   - View detailed order information
   - Update order status (mark ready, complete, cancel)

3. **Order Creation**
   - Customer information input
   - Order type selection (dine-in/delivery)
   - Dynamic order item management
   - Address form for delivery orders
   - Real-time price calculation
   - Pre-configured menu items

4. **Dashboard**
   - Total orders count
   - Total revenue
   - Completed/pending order statistics
   - Order status distribution
   - Recent orders list

### Order Status Flow

```
CREATED → PAID → PREPARING → READY → COMPLETED
                ↓
             CANCELLED
```

## Project Structure

```
frontend/
├── src/
│   ├── components/
│   │   ├── layout/
│   │   │   ├── Header.tsx          # Navigation header
│   │   │   └── Layout.tsx           # Main layout wrapper
│   │   └── order/
│   │       ├── OrderCard.tsx        # Order summary card
│   │       └── OrderStatusBadge.tsx  # Status indicator
│   ├── pages/
│   │   ├── HomePage.tsx            # Landing page
│   │   ├── OrderListPage.tsx      # Orders list with filters
│   │   ├── CreateOrderPage.tsx     # Order creation form
│   │   ├── OrderDetailsPage.tsx    # Order details view
│   │   └── DashboardPage.tsx       # Statistics dashboard
│   ├── hooks/
│   │   └── useOrders.ts            # React Query hooks
│   ├── services/
│   │   └── api/
│   │       ├── client.ts             # Axios configuration
│   │       └── orders.ts             # API endpoints
│   ├── types/
│   │   └── order.ts               # TypeScript types
│   ├── utils/
│   │   ├── constants.ts            # App constants
│   │   └── formatters.ts          # Utility functions
│   ├── styles/
│   │   ├── theme.ts               # MUI theme
│   │   └── global.css             # Global styles
│   ├── App.tsx                     # App component with routing
│   └── main.tsx                    # Application entry point
├── package.json
├── tsconfig.json
├── vite.config.ts
└── .eslintrc.cjs
```

## API Integration

The frontend integrates with the Spring Boot backend REST API:

### Endpoints Used

- `GET /api/orders` - Fetch all orders
- `GET /api/orders/{id}` - Fetch single order
- `GET /api/orders/status/{status}` - Fetch orders by status
- `POST /api/orders` - Create new order
- `PUT /api/orders/{id}/status` - Update order status
- `PUT /api/orders/{id}/ready` - Mark order as ready
- `PUT /api/orders/{id}/complete` - Complete order
- `DELETE /api/orders/{id}` - Cancel order

### State Management

- **React Query** handles server state with automatic caching and refetching
- **React Context** for global app state (theme, notifications)
- **Local State** for form inputs and UI interactions

## Configuration

### Vite Configuration

The Vite config includes:
- Path aliases (`@/` maps to `src/`)
- API proxy to backend (`/api` → `http://localhost:8080/api`)
- Port 3000 for dev server

### TypeScript Configuration

Strict type checking enabled with:
- Path aliases configured
- Strict null checks
- No unused variables/parameters

### Material-UI Theme

Coffee-themed color palette:
- Primary: Coffee brown (#6F4E37)
- Secondary: Latte color (#C4A77D)
- Status colors for different order states

## Development

### Code Style

- ESLint with React and TypeScript rules
- Prettier for code formatting
- Component-based architecture
- Custom hooks for reusable logic

### Best Practices

- Use TypeScript types for all data
- Implement error boundaries
- Handle loading and error states
- Use React Query for API calls
- Keep components small and focused
- Follow Material-UI guidelines

## Testing

To run tests (when implemented):

```bash
npm run test
```

## Building for Production

```bash
npm run build
```

This creates an optimized production build in the `dist` folder.

## Deployment

The frontend can be deployed to any static hosting service:
- Vercel
- Netlify
- GitHub Pages
- AWS S3 + CloudFront

Ensure that the backend API URL is updated in `vite.config.ts` for production.

## Troubleshooting

### Backend Connection Issues

If you see CORS errors or connection failures:
1. Ensure that the Spring Boot backend is running on port 8080
2. Check the backend logs for errors
3. Verify the API proxy configuration in `vite.config.ts`

### Build Errors

If you encounter TypeScript errors:
1. Check that all dependencies are installed
2. Run `npm install` to ensure versions match
3. Clear node_modules and reinstall if needed

## Documentation

- [Vite Documentation](https://vitejs.dev/)
- [React Documentation](https://react.dev/)
- [Material-UI Documentation](https://mui.com/)
- [React Query Documentation](https://tanstack.com/query/latest)
- [React Router Documentation](https://reactrouter.com/)
- [TypeScript Documentation](https://www.typescriptlang.org/)

## License

This project is for educational purposes as part of the Blue Mountain Coffee Demo.
