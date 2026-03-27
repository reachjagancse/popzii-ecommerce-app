# Popzii Code Location Reference

## Quick Navigation Guide

### Frontend - React Native (Mobile)

| Feature | Path | Responsibility |
|---------|------|-----------------|
| **App Setup** | [mobile/App.js](mobile/App.js) | Redux store initialization, StatusBar |
| **Navigation** | [mobile/src/navigation/RootNavigator.js](mobile/src/navigation/RootNavigator.js) | Bottom tab navigation |
| **API Client** | [mobile/src/api/api.js](mobile/src/api/api.js) | Axios instance, HTTP methods (get/post/patch) |
| | |  Base URL: `http://10.0.2.2:4000` (emulator) |

#### Screens - Instamart Flow

| Screen | Path | Responsibilities |
|--------|------|------------------|
| **HomeScreen** | [mobile/src/screens/HomeScreen.js](mobile/src/screens/HomeScreen.js) | Browse products by category |
| | | Redux: fetchCategories(), fetchProducts() |
| | | dispatch(addToCart()) |
| **ProductDetailScreen** | [mobile/src/screens/ProductDetailScreen.js](mobile/src/screens/ProductDetailScreen.js) | Show product details |
| **CartScreen** | [mobile/src/screens/CartScreen.js](mobile/src/screens/CartScreen.js) | Manage cart items |
| | | dispatch(updateQty, removeFromCart, clearCart) |
| | | POST /api/carts/checkout |

#### Screens - Restaurant Flow

| Screen | Path | Responsibilities |
|--------|------|---|
| **RestaurantsScreen** | [mobile/src/screens/RestaurantsScreen.js](mobile/src/screens/RestaurantsScreen.js) | Search/filter restaurants |
| | | GET /api/restaurants?cuisine=X&minRating=Y |
| **RestaurantMenuScreen** | [mobile/src/screens/RestaurantMenuScreen.js](mobile/src/screens/RestaurantMenuScreen.js) | Browse menu by category |
| | | GET /api/restaurants/menu-categories |
| | | GET /api/restaurants/menu-items |
| | | Local cart state (not Redux) |
| **RestaurantCheckoutScreen** | [mobile/src/screens/RestaurantCheckoutScreen.js](mobile/src/screens/RestaurantCheckoutScreen.js) | Review order before placing |
| | | POST /api/foodorders |

#### Screens - Shared

| Screen | Path | Responsibilities |
|--------|------|---|
| **OrderTrackingScreen** | [mobile/src/screens/OrderTrackingScreen.js](mobile/src/screens/OrderTrackingScreen.js) | Real-time order status |
| | | GET /api/foodorders/{id} or /api/carts/{id} |
| | | PATCH /api/carts/{id}/status |
| | | Simulated polling every 4 seconds |
| **OrdersScreen** | [mobile/src/screens/OrdersScreen.js](mobile/src/screens/OrdersScreen.js) | View order history |
| **ProfileScreen** | [mobile/src/screens/ProfileScreen.js](mobile/src/screens/ProfileScreen.js) | User profile |

#### Components

| Component | Path | Purpose |
|-----------|------|---------|
| **ProductCard** | [mobile/src/components/ProductCard.js](mobile/src/components/ProductCard.js) | Display product tile with add button |

#### Redux Store

| Slice | Path | State Shape |
|-------|------|-------------|
| **Products** | [mobile/src/store/productsSlice.js](mobile/src/store/productsSlice.js) | `{ items: [], status: 'idle' }` |
| | | Thunk: `fetchProducts({ categoryId?, search? })` |
| **Categories** | [mobile/src/store/categoriesSlice.js](mobile/src/store/categoriesSlice.js) | `{ items: [], status: 'idle' }` |
| | | Thunk: `fetchCategories()` |
| **Cart** | [mobile/src/store/cartSlice.js](mobile/src/store/cartSlice.js) | `{ items: [...] }` |
| | | Reducers: `addToCart`, `updateQty`, `removeFromCart`, `clearCart` |
| **Orders** | [mobile/src/store/ordersSlice.js](mobile/src/store/ordersSlice.js) | `{ items: [...] }` |
| | | Reducers: `addOrder`, `updateOrderStatus` |
| **Root Store** | [mobile/src/store/index.js](mobile/src/store/index.js) | configureStore with all slices |

---

### Frontend - React Web (Vendor & Admin)

#### Vendor Dashboard

| Page | Path | API Endpoints |
|------|------|---|
| **App** | [vendor/src/App.js](vendor/src/App.js) | Router with routes |
| **Dashboard** | [vendor/src/components/Dashboard.js](vendor/src/components/Dashboard.js) | GET /api/instamart/products |
| | | GET /api/foodorders |
| | | Displays: orders count, revenue, products count |
| **Products** | [vendor/src/components/Products.js](vendor/src/components/Products.js) | Manage vendor products |
| **Orders** | [vendor/src/components/Orders.js](vendor/src/components/Orders.js) | Monitor orders |
| **API Client** | [vendor/src/api.js](vendor/src/api.js) | Axios with baseURL: localhost:4000 |

#### Admin Dashboard

| Page | Path | API Endpoints |
|------|------|---|
| **App** | [admin/src/App.js](admin/src/App.js) | Router with routes |
| **Dashboard** | [admin/src/components/Dashboard.js](admin/src/components/Dashboard.js) | GET /api/restaurants?page=0&size=200 |
| | | GET /api/foodorders |
| | | Displays: vendors count, orders count, revenue |
| **Vendors** | [admin/src/components/Vendors.js](admin/src/components/Vendors.js) | Vendor management |
| **Products** | [admin/src/components/Products.js](admin/src/components/Products.js) | Product oversight |
| **Orders** | [admin/src/components/Orders.js](admin/src/components/Orders.js) | Order monitoring |
| **API Client** | [admin/src/api.js](admin/src/api.js) | Axios with baseURL: localhost:4000 |

---

### Backend - Spring Boot Microservices

#### Project Structure

```
server/
├── app/                    # Main application entry
│   ├── build.gradle       # App-specific build config
│   └── src/main/
│       ├── resources/
│       │   ├── application.yml    # Spring Boot config
│       │   └── db/changelog/
│       │       └── db.changelog-master.xml  # Database schema
│       └── java/com/popzii/app/
│           ├── exception/GlobalExceptionHandler.java
│           ├── controller/ProductController.java
│           └── ...
│
├── auth/                   # JWT authentication
├── user/                   # User management
├── vendor/                 # Vendor management
├── product/                # Generic product service
├── cart/                   # Cart & checkout
├── foodorder/              # Food order processing
├── payment/                # Payment integration (mocked)
├── notification/           # Notifications (mocked)
├── restaurant/             # Restaurant management
├── instamart/              # Grocery catalog
├── coupon/                 # Coupon service
├── order/                  # Generic order service
├── common/                 # Shared utilities
├── integration-mock/       # Third-party mocks
│
├── settings.gradle         # Multi-module configuration
├── build.gradle            # Root build config
├── gradlew                 # Gradle wrapper
└── gradlew.bat            # Windows gradle wrapper
```

#### Authentication Module

| Component | Path | Responsibility |
|-----------|------|-----------------|
| **AuthService** | [server/auth/src/main/java/com/popzii/auth/service/AuthService.java](server/auth/src/main/java/com/popzii/auth/service/AuthService.java) | JWT token generation/validation |
| **AuthController** | [server/auth/src/main/java/com/popzii/auth/controller/AuthController.java](server/auth/src/main/java/com/popzii/auth/controller/AuthController.java) | POST /api/auth/register, /api/auth/login |

#### Cart Module (Instamart Checkout)

| Component | Path | Responsibility |
|-----------|------|---|
| **CartController** | [server/cart/src/main/java/com/popzii/cart/controller/CartController.java](server/cart/src/main/java/com/popzii/cart/controller/CartController.java) | POST /api/carts/checkout |
| | | GET /api/carts/{id} |
| | | PATCH /api/carts/{id}/status |
| **CartService** | [server/cart/src/main/java/com/popzii/cart/service/CartServiceImpl.java](server/cart/src/main/java/com/popzii/cart/service/CartServiceImpl.java) | checkout(request) → save cart + items to DB |
| **CartRepository** | [server/cart/src/main/java/com/popzii/cart/repository/CartRepository.java](server/cart/src/main/java/com/popzii/cart/repository/CartRepository.java) | JPA repository for carts table |
| **CartItemRepository** | [server/cart/src/main/java/com/popzii/cart/repository/CartItemRepository.java](server/cart/src/main/java/com/popzii/cart/repository/CartItemRepository.java) | JPA repository for cart_items table |

#### Food Order Module

| Component | Path | Responsibility |
|-----------|------|---|
| **FoodOrderController** | [server/foodorder/src/main/java/com/popzii/foodorder/controller/FoodOrderController.java](server/foodorder/src/main/java/com/popzii/foodorder/controller/FoodOrderController.java) | POST /api/foodorders |
| | | GET /api/foodorders, /api/foodorders/{id} |
| | | PATCH /api/foodorders/{id}/status |
| **FoodOrderService** | [server/foodorder/src/main/java/com/popzii/foodorder/service/FoodOrderServiceImpl.java](server/foodorder/src/main/java/com/popzii/foodorder/service/FoodOrderServiceImpl.java) | createOrder(request) |
| | | getOrder(id), listOrders() |
| | | updateStatus(id, status) |
| **FoodOrderRepository** | [server/foodorder/src/main/java/com/popzii/foodorder/repository/FoodOrderRepository.java](server/foodorder/src/main/java/com/popzii/foodorder/repository/FoodOrderRepository.java) | JPA repository for food_orders table |

#### Restaurant Module

| Component | Path | Responsibility |
|-----------|------|---|
| **RestaurantController** | [server/restaurant/src/main/java/com/popzii/restaurant/controller/RestaurantController.java](server/restaurant/src/main/java/com/popzii/restaurant/controller/RestaurantController.java) | GET /api/restaurants |
| **RestaurantMenuCategoryController** | [server/restaurant/src/main/java/com/popzii/restaurant/controller/RestaurantMenuCategoryController.java](server/restaurant/src/main/java/com/popzii/restaurant/controller/RestaurantMenuCategoryController.java) | GET /api/restaurants/menu-categories |
| **RestaurantMenuItemController** | [server/restaurant/src/main/java/com/popzii/restaurant/controller/RestaurantMenuItemController.java](server/restaurant/src/main/java/com/popzii/restaurant/controller/RestaurantMenuItemController.java) | GET /api/restaurants/menu-items |
| **RestaurantCatalogService** | [server/restaurant/src/main/java/com/popzii/restaurant/service/RestaurantCatalogServiceImpl.java](server/restaurant/src/main/java/com/popzii/restaurant/service/RestaurantCatalogServiceImpl.java) | listRestaurants(), getMenuCategories() |

#### Instamart Module

| Component | Path | Responsibility |
|-----------|------|---|
| **InstamartCategoryController** | [server/instamart/src/main/java/com/popzii/instamart/controller/InstamartCategoryController.java](server/instamart/src/main/java/com/popzii/instamart/controller/InstamartCategoryController.java) | GET /api/instamart/categories |
| **InstamartProductController** | [server/instamart/src/main/java/com/popzii/instamart/controller/InstamartProductController.java](server/instamart/src/main/java/com/popzii/instamart/controller/InstamartProductController.java) | GET /api/instamart/products |
| | | POST /api/instamart/products |
| **InstamartVariantController** | [server/instamart/src/main/java/com/popzii/instamart/controller/InstamartVariantController.java](server/instamart/src/main/java/com/popzii/instamart/controller/InstamartVariantController.java) | GET /api/instamart/variants |
| **InstamartCatalogService** | [server/instamart/src/main/java/com/popzii/instamart/service/InstamartCatalogServiceImpl.java](server/instamart/src/main/java/com/popzii/instamart/service/InstamartCatalogServiceImpl.java) | listCategories(), listProducts() |

#### Payment Module (Mocked)

| Component | Path | Responsibility |
|-----------|------|---|
| **PaymentController** | [server/payment/src/main/java/com/popzii/payment/controller/PaymentController.java](server/payment/src/main/java/com/popzii/payment/controller/PaymentController.java) | POST /api/payment (mocked Stripe/Razorpay) |

#### Common Module

| Component | Path | Responsibility |
|-----------|------|---|
| **ApiResponse** | [server/common/src/main/java/com/popzii/common/web/ApiResponse.java](server/common/src/main/java/com/popzii/common/web/ApiResponse.java) | Reusable response wrapper |
| **GlobalExceptionHandler** | [server/app/src/main/java/com/popzii/app/exception/GlobalExceptionHandler.java](server/app/src/main/java/com/popzii/app/exception/GlobalExceptionHandler.java) | Handle and return standardized error responses |

#### Database

| File | Path | Responsibility |
|------|------|---|
| **Liquibase Master Changelog** | [server/app/src/main/resources/db/changelog/db.changelog-master.xml](server/app/src/main/resources/db/changelog/db.changelog-master.xml) | Define schema changesets |
| | | changeSet id="1" — users table |
| | | changeSet id="20" — instamart (categories, products, variants, logs) |
| | | changeSet id="21" — restaurants (restaurants, menus, items) |
| | | changeSet id="22" — carts (carts, cart_items) |
| | | changeSet id="23" — food_orders (orders, order_items) |

#### Configuration

| File | Path | Purpose |
|------|------|---------|
| **application.yml** | [server/app/src/main/resources/application.yml](server/app/src/main/resources/application.yml) | Spring Boot config (DB, logging, etc.) |
| **build.gradle** (root) | [server/build.gradle](server/build.gradle) | Root Gradle config, common dependencies |
| **settings.gradle** | [server/settings.gradle](server/settings.gradle) | Module inclusion |

---

## Key File Cross-References

### Instamart Checkout Flow

1. **Mobile initiation**: [CartScreen.js](mobile/src/screens/CartScreen.js) line ~20
2. **API call**: [api.js](mobile/src/api/api.js) → POST `/api/carts/checkout`
3. **Backend route**: [CartController.java](server/cart/src/main/java/com/popzii/cart/controller/CartController.java) @ @PostMapping("/checkout")
4. **Business logic**: [CartServiceImpl.java](server/cart/src/main/java/com/popzii/cart/service/CartServiceImpl.java) @ checkout() method
5. **Data persistence**: [CartRepository.java](server/cart/src/main/java/com/popzii/cart/repository/CartRepository.java) saves to DB
6. **Schema**: [db.changelog-master.xml](server/app/src/main/resources/db/changelog/db.changelog-master.xml) changeSet id="22"
7. **Response handling**: [CartScreen.js](mobile/src/screens/CartScreen.js) line ~25-30
8. **State update**: [ordersSlice.js](mobile/src/store/ordersSlice.js) @ addOrder reducer

### Food Order Flow

1. **Mobile menu browse**: [RestaurantMenuScreen.js](mobile/src/screens/RestaurantMenuScreen.js) line ~1-30
2. **Menu API calls**:
   - Categories: GET `/api/restaurants/menu-categories`
   - Items: GET `/api/restaurants/menu-items`
   - Controllers: [RestaurantMenuCategoryController.java](server/restaurant/src/main/java/com/popzii/restaurant/controller/RestaurantMenuCategoryController.java), [RestaurantMenuItemController.java](server/restaurant/src/main/java/com/popzii/restaurant/controller/RestaurantMenuItemController.java)
3. **Place order**: [RestaurantCheckoutScreen.js](mobile/src/screens/RestaurantCheckoutScreen.js) → POST `/api/foodorders`
4. **Backend processing**: [FoodOrderController.java](server/foodorder/src/main/java/com/popzii/foodorder/controller/FoodOrderController.java) @ @PostMapping
5. **Business logic**: [FoodOrderServiceImpl.java](server/foodorder/src/main/java/com/popzii/foodorder/service/FoodOrderServiceImpl.java) @ createOrder()
6. **Schema**: [db.changelog-master.xml](server/app/src/main/resources/db/changelog/db.changelog-master.xml) changeSet id="23"

### Order Tracking

1. **Tracking screen**: [OrderTrackingScreen.js](mobile/src/screens/OrderTrackingScreen.js) line ~1-60
2. **Fetch current status**: GET `/api/foodorders/{id}` or GET `/api/carts/{id}`
3. **Update status**: PATCH `/api/carts/{id}/status?status=X`
4. **Redux sync**: [ordersSlice.js](mobile/src/store/ordersSlice.js) @ updateOrderStatus reducer

---

## Configuration Files

| File | Purpose |
|------|---------|
| [README.md](README.md) | Root project overview |
| [server/README.md](server/README.md) | Backend setup & API reference |
| [mobile/README.md](mobile/README.md) | Mobile app setup |
| [vendor/README.md](vendor/README.md) | Vendor dashboard setup |
| [admin/README.md](admin/README.md) | Admin panel setup |

---

## Development Workflow

### To Debug a Feature

1. **Identify which flow**: Instamart (cart) or FoodOrder (restaurant)?
2. **Find mobile screen**: Check `mobile/src/screens/`
3. **Find Redux slice**: Check `mobile/src/store/` for state management
4. **Find API call**: Check action/thunk in Redux slice or direct fetch in screen
5. **Find backend controller**: Check `server/[module]/controller/`
6. **Find service logic**: Check `server/[module]/service/`
7. **Find DB schema**: Check [db.changelog-master.xml](server/app/src/main/resources/db/changelog/db.changelog-master.xml)
8. **Check error handling**: [GlobalExceptionHandler.java](server/app/src/main/java/com/popzii/app/exception/GlobalExceptionHandler.java)

### To Add a New Feature

1. **Database**: Add changeSet to [db.changelog-master.xml](server/app/src/main/resources/db/changelog/db.changelog-master.xml)
2. **Backend**: Create domain entity → repository → service → controller
3. **Frontend**: Create screen/component → API integration → Redux slice (if needed)
4. **API contract**: Ensure request/response DTOs match

---

## Testing Entry Points

### Mobile Testing
- Run: `cd mobile && npm start`
- Clear Redux cache: LogBox warnings for any Redux errors
- Network requests: Check browser DevTools or React Native Debugger

### Backend Testing
- Run: `cd server && ./gradlew :app:bootRun`
- H2 Console: http://localhost:4000/h2-console (user: sa, password: blank)
- Test endpoints: Use curl or Postman (see [server/README.md](server/README.md))

### Dashboard Testing
- Admin: http://localhost:3002
- Vendor: http://localhost:3001

---

## Important Code Snippets

### Making an API Call (Mobile)

```javascript
// From ProductCard.js or screen
const resp = await api.get('/api/instamart/products?categoryId=1');
const products = resp.data?.data || [];
```

### Dispatching Redux Action

```javascript
import { useDispatch } from 'react-redux';
import { addToCart } from '../store/cartSlice';

const dispatch = useDispatch();
dispatch(addToCart({ id: 1, name: 'Item', price: 9.99 }));
```

### Async Redux Thunk

```javascript
// From productsSlice.js
export const fetchProducts = createAsyncThunk('products/fetch', async (filters) => {
  const resp = await api.get('/api/instamart/products?...');
  return resp.data?.data || [];
});
```

### Backend Controller

```java
// From CartController.java
@PostMapping("/checkout")
public ApiResponse<CartResponse> checkout(
  @Valid @RequestBody CartCheckoutRequest request
) {
  return ApiResponse.ok("Checkout completed", service.checkout(request));
}
```

### Database Access (Spring Data JPA)

```java
// From CartRepository.java or FoodOrderRepository.java
public interface CartRepository extends JpaRepository<Cart, Long> {
  // Automatically provides save(), findById(), findAll(), delete()
}
```

---

## Notes for Contributors

1. **Follow REST conventions**: GET for reads, POST for creates, PATCH for updates
2. **Use consistent naming**: Controllers end with `Controller`, Services with `Service` or `ServiceImpl`
3. **Keep DTOs separate**: request/response DTOs in `dto/` package
4. **Error handling**: Throw exceptions, let `GlobalExceptionHandler` catch them
5. **Database changes**: Always add Liquibase changeSets, never direct SQL
6. **Mobile state**: Use Redux for persistent state, local component state for temps
7. **API responses**: Always wrap in `ApiResponse<T>` for consistency

