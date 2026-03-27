# Popzii Platform — End-to-End Flow Analysis

## Overview
Popzii is a full-stack food & grocery delivery platform with two main flows:
1. **Instamart Flow** — Grocery/instant commerce
2. **FoodOrder Flow** — Restaurant ordering

---

## System Architecture

### Technology Stack
- **Backend**: Spring Boot microservices (Java 17) with Gradle
- **Mobile**: React Native with Redux state management
- **Admin/Vendor Web**: React with client-side API
- **Database**: PostgreSQL (production) / H2 (development)
- **Real-time**: Simulated updates (comment-based polling, not WebSocket yet)

### Project Structure
```
popzii/
├── server/              # Spring Boot backend (microservices)
│   ├── app/            # Main application container
│   ├── auth/           # JWT authentication module
│   ├── cart/           # Cart/checkout service
│   ├── foodorder/      # Food order processing
│   ├── instamart/      # Grocery catalog & variants
│   ├── restaurant/     # Restaurant & menu management
│   └── [others]        # Payment, notification, user, vendor modules
├── mobile/             # React Native customer app
├── vendor/             # React vendor dashboard
└── admin/              # React admin panel
```

---

## Flow 1: INSTAMART (Grocery/Instant Commerce)

### End-to-End Journey

```
┌─────────────────────────────────────────────────────────────┐
│                    MOBILE APP (Customer)                     │
├─────────────────────────────────────────────────────────────┤
│  HomeScreen                                                   │
│  - fetchCategories() → GET /api/instamart/categories         │
│  - fetchProducts() → GET /api/instamart/products             │
│  - Redux: categoriesSlice, productsSlice                     │
│  - ProductCard: addToCart() → Redux cartSlice.addToCart()    │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│              Redux State Management                          │
├─────────────────────────────────────────────────────────────┤
│  Store:                                                      │
│  - categories.items []                                       │
│  - products.items []                                         │
│  - cart.items []  ← { id, name, price, qty }                │
│  - orders.items []                                           │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                    CartScreen                                │
├─────────────────────────────────────────────────────────────┤
│  - Display: useSelector(s => s.cart.items)                  │
│  - updateQty() / removeFromCart() / clearCart()             │
│  - handleCheckout():                                         │
│    POST /api/carts/checkout                                 │
│    payload: { items: [...] }                                │
│    result: { orderId, status, ... }                         │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│           BACKEND: CartController & CartService             │
├─────────────────────────────────────────────────────────────┤
│  CartController                                              │
│  - @PostMapping("/checkout")                                │
│    → cartService.checkout(CartCheckoutRequest)              │
│                                                              │
│  CartServiceImpl.checkout()                                  │
│  1. Create new Cart record                                  │
│     - cart.setUserId(request.getUserId())                   │
│     - cart.setStatus("CHECKED_OUT")                         │
│     - cartRepo.save(cart)                                   │
│  2. For each item, create CartItem record                   │
│     - item.setCartId(cart.getId())                          │
│     - item.setProductId(itemRequest.getProductId())         │
│     - item.setQuantity(itemRequest.getQuantity())           │
│     - itemRepo.save(item)                                   │
│  3. Return CartResponse with all items                      │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│              DATABASE: Cart Tables                           │
├─────────────────────────────────────────────────────────────┤
│  carts:                                                      │
│  - id (PK)                                                   │
│  - user_id (FK → users)                                      │
│  - status: "CHECKED_OUT" | "CONFIRMED" | "DELIVERED"        │
│  - created_at, updated_at                                   │
│                                                              │
│  cart_items:                                                │
│  - id (PK)                                                   │
│  - cart_id (FK → carts)                                      │
│  - product_id (FK → instamart_products)                      │
│  - name, price, quantity                                    │
│  - created_at, updated_at                                   │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│         MOBILE: OrderTrackingScreen                          │
├─────────────────────────────────────────────────────────────┤
│  - useSelector to find order from Redux                      │
│  - GET /api/carts/{orderId} (fetch latest status)           │
│  - Simulated polling every 4 seconds:                       │
│    Status sequence: PLACED → CONFIRMED → PREPARING          │
│                  → OUT_FOR_DELIVERY → DELIVERED             │
│  - PATCH /api/carts/{orderId}/status?status=CONFIRMED      │
│  - Redux: updateOrderStatus({ id, status })                │
│  - Display: "Order #123: Status PREPARING"                  │
└─────────────────────────────────────────────────────────────┘
```

### Data Models

**instamart_categories**
```java
- id (Long, PK)
- name (VARCHAR)
- description (VARCHAR)
- is_active (BOOLEAN, default true)
- created_at, updated_at, deleted_at (TIMESTAMP)
```

**instamart_products**
```java
- id (Long, PK)
- category_id (FK)
- name, description, brand, unit (VARCHAR)
- base_price (DECIMAL)
- discount_percent (DECIMAL)
- is_active (BOOLEAN)
- created_at, updated_at, deleted_at (TIMESTAMP)
```

**instamart_product_variants**
```java
- id (Long, PK)
- product_id (FK)
- name, sku (VARCHAR)
- price (DECIMAL)
- stock (INT)
- is_default (BOOLEAN)
- bulk_min_qty, bulk_price (for bulk discounts)
- created_at, updated_at, deleted_at (TIMESTAMP)
```

---

## Flow 2: FOODORDER (Restaurant Ordering)

### End-to-End Journey

```
┌─────────────────────────────────────────────────────────────┐
│                    MOBILE APP (Customer)                     │
├─────────────────────────────────────────────────────────────┤
│  RestaurantsScreen                                           │
│  - GET /api/restaurants?cuisine=X&minRating=Y               │
│  - setRestaurants([...])                                     │
│  - Search/filter by name, cuisine, rating                   │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│              RestaurantMenuScreen                            │
├─────────────────────────────────────────────────────────────┤
│  1. Load menu categories:                                   │
│     GET /api/restaurants/menu-categories?restaurantId=1     │
│                                                              │
│  2. Load menu items for category:                           │
│     GET /api/restaurants/menu-items?categoryId=5            │
│                                                              │
│  3. Local cart state: setCart([...])                        │
│     addItem(item) → { id, name, price, qty: 1 }            │
│                                                              │
│  4. Show "View Cart · $25.50" button                        │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│          RestaurantCheckoutScreen                            │
├─────────────────────────────────────────────────────────────┤
│  - Display cart items from route.params                     │
│  - Calculate total price                                    │
│  - handlePlaceOrder():                                      │
│    POST /api/foodorders                                     │
│    payload: {                                               │
│      restaurantId: 1,                                       │
│      items: [                                               │
│        { menuItemId, name, price, quantity }                │
│      ]                                                      │
│    }                                                        │
│  - result: FoodOrderResponse { id, userId, ... }           │
│  - Redux: dispatch(addOrder(order))                         │
│  - Navigate to OrderTrackingScreen                          │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│      BACKEND: FoodOrderController & FoodOrderService        │
├─────────────────────────────────────────────────────────────┤
│  FoodOrderController                                         │
│  - @PostMapping                                             │
│    → foodOrderService.createOrder(FoodOrderRequest)         │
│                                                              │
│  FoodOrderServiceImpl.createOrder()                          │
│  1. Create FoodOrder record:                                │
│     - order.setUserId(request.getUserId())                  │
│     - order.setRestaurantId(request.getRestaurantId())      │
│     - order.setStatus("PLACED")                             │
│     - order.setPaymentStatus("PENDING")                     │
│     - Calculate totalAmount from items                      │
│     - order.setCreatedAt(now)                               │
│  2. Save: FoodOrder saved = orderRepo.save(order)           │
│  3. For each item:                                          │
│     - FoodOrderItem item = new FoodOrderItem()              │
│     - item.setOrderId(saved.getId())                        │
│     - item.setMenuItemId(itemRequest.getMenuItemId())       │
│     - item.setQuantity()                                    │
│     - itemRepo.save(item)                                   │
│  4. Return FoodOrderResponse with order + items             │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│           DATABASE: FoodOrder Tables                         │
├─────────────────────────────────────────────────────────────┤
│  food_orders:                                               │
│  - id (PK)                                                   │
│  - user_id (FK → users)                                      │
│  - restaurant_id (FK → restaurants)                          │
│  - status: "PLACED" | "CONFIRMED" | "PREPARING"             │
│           | "OUT_FOR_DELIVERY" | "DELIVERED"               │
│  - payment_status: "PENDING" | "COMPLETED" | "FAILED"       │
│  - total_amount (DECIMAL)                                   │
│  - created_at, updated_at                                   │
│                                                              │
│  food_order_items:                                          │
│  - id (PK)                                                   │
│  - order_id (FK → food_orders)                              │
│  - menu_item_id (FK → restaurant_menu_items)                │
│  - name, price (for snapshot)                               │
│  - quantity (INT)                                           │
│  - created_at, updated_at                                   │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│         MOBILE: OrderTrackingScreen (same as Instamart)      │
├─────────────────────────────────────────────────────────────┤
│  - GET /api/foodorders/{orderId}                            │
│  - Simulated status updates every 4 seconds                 │
│  - PATCH /api/foodorders/{orderId}/status?status=CONFIRMED │
│  - Display order progress visually                          │
└─────────────────────────────────────────────────────────────┘
```

### Data Models

**restaurants**
```java
- id (Long, PK)
- owner_id (FK → users)
- name, address (VARCHAR)
- cuisine, phone (VARCHAR)
- rating (DECIMAL)
- is_active (BOOLEAN)
- created_at, updated_at, deleted_at (TIMESTAMP)
```

**restaurant_menu_categories**
```java
- id (Long, PK)
- restaurant_id (FK)
- name (VARCHAR)
- is_active (BOOLEAN)
- created_at, updated_at, deleted_at (TIMESTAMP)
```

**restaurant_menu_items**
```java
- id (Long, PK)
- category_id (FK)
- name, description (VARCHAR)
- price (DECIMAL)
- is_veg (BOOLEAN)
- is_available (BOOLEAN)
- created_at, updated_at, deleted_at (TIMESTAMP)
```

**food_orders** & **food_order_items**
```java
(See above in Flow diagram)
```

---

## API Endpoints Summary

### Authentication
- `POST /api/auth/register` — User registration
- `POST /api/auth/login` — User login

### Instamart (Grocery)
- `GET /api/instamart/categories` — List categories (pagination)
- `GET /api/instamart/products` — List products (filter by categoryId, search)
- `POST /api/instamart/products` — Create product
- `GET /api/instamart/variants` — Product variants
- `POST /api/carts/checkout` — Checkout (create cart order)
- `GET /api/carts/{id}` — Get cart details
- `PATCH /api/carts/{id}/status` — Update cart status

### Restaurants & Food Orders
- `GET /api/restaurants` — List restaurants (filter by cuisine, minRating)
- `GET /api/restaurants/menu-categories` — Menu categories for restaurant
- `GET /api/restaurants/menu-items` — Menu items for category
- `POST /api/restaurants/menu-items` — Create menu item
- `POST /api/foodorders` — Create food order
- `GET /api/foodorders` — List all food orders
- `GET /api/foodorders/{id}` — Get order details
- `PATCH /api/foodorders/{id}/status` — Update order status

### Admin & Vendor
- `GET /api/orders` — (Vendor) list their orders
- `GET /api/payment` — Payment endpoints (mocked)

---

## Redux State Management (Mobile)

### Store Structure
```javascript
{
  products: {
    items: [{ id, name, basePrice, categoryId, ... }],
    status: 'idle' | 'loading' | 'succeeded' | 'failed'
  },
  categories: {
    items: [{ id, name, description, ... }],
    status: 'idle' | 'loading' | 'succeeded' | 'failed'
  },
  cart: {
    items: [{ id, name, price, qty, ... }]  // local state
  },
  orders: {
    items: [{ id, status, totalAmount, ... }]  // placed orders
  }
}
```

### Key Redux Slices

**productsSlice.js**
```javascript
- fetchProducts({ categoryId?, search? }) — Async thunk
  GET /api/instamart/products?categoryId=X&search=Y&page=0&size=100
- Reducers: setProducts, setStatus (on fulfilled/rejected)
```

**cartSlice.js**
```javascript
- addToCart({ id, name, price, qty?: 1 }) — Add/increment item
- updateQty({ id, qty }) — Update quantity
- removeFromCart({ id }) — Remove item
- clearCart() — Clear all items (after checkout)
```

**ordersSlice.js**
```javascript
- addOrder(order) — Store placed order
- updateOrderStatus({ id, status }) — Update status
```

---

## Frontend Flow Architecture

### Mobile (React Native)
```
App.js ─────► RootNavigator
                 └─► Tabs (Bottom nav)
                      ├─► HomeScreen (Products)
                      ├─► RestaurantsScreen
                      ├─► CartScreen / RestaurantMenuScreen
                      ├─► OrderTrackingScreen
                      └─► ProfileScreen
```

**Key Components**
- `HomeScreen` — Instamart product browsing with categories
- `CartScreen` — Instamart cart management
- `RestaurantsScreen` — Restaurant discovery with filters
- `RestaurantMenuScreen` — Menu browsing + local cart
- `RestaurantCheckoutScreen` — Review & place food order
- `OrderTrackingScreen` — Real-time tracking (simulated)
- `ProductCard` — Reusable product display

### Vendor Dashboard (React Web)
```
App.js ────► Router
              ├─► /dashboard ─► Dashboard.js
              ├─► /products ─► Products.js
              ├─► /orders ─► Orders.js
              └─► /... ─► other components
```

**Features**
- View orders count, revenue, products count
- Manage products (add/edit/delete)
- Monitor order status
- Analytics

### Admin Panel (React Web)
```
App.js ────► Router
              ├─► /dashboard ─► Dashboard.js
              ├─► /vendors ─► Vendors.js
              ├─► /products ─► Products.js
              ├─► /orders ─► Orders.js
              └─► /... ─► other components
```

**Features**
- Vendor management & approval
- Order monitoring
- System analytics
- Revenue tracking

---

## Key Code Flows

### 1. Product Discovery → Add to Cart → Checkout (Instamart)

**Mobile**
```javascript
// HomeScreen.js
useEffect(() => {
  dispatch(fetchProducts({ categoryId, search })); // Triggers API call
}, [dispatch, categoryId, search]);

// On product tap
handleAdd(product) → dispatch(addToCart({
  id: product.id,
  name: product.name,
  price: product.basePrice
}));

// CartScreen checkout
handleCheckout() → api.post('/api/carts/checkout', {
  items: items.map(i => ({
    productId: i.id,
    name: i.name,
    price: i.price,
    quantity: i.qty
  }))
});

// Response
{ data: { id: 123, status: 'CHECKED_OUT', items: [...] } }
```

**Backend**
```java
@PostMapping("/checkout")
public ApiResponse<CartResponse> checkout(
  @Valid @RequestBody CartCheckoutRequest request
) {
  return ApiResponse.ok("Checkout completed", serv.checkout(request));
}

public CartResponse checkout(CartCheckoutRequest request) {
  // 1. Save cart to DB
  Cart cart = new Cart();
  cart.setUserId(request.getUserId());
  cart.setStatus("CHECKED_OUT");
  Cart saved = cartRepo.save(cart);
  
  // 2. Save each item
  request.getItems().forEach(itemReq -> {
    CartItem item = new CartItem();
    item.setCartId(saved.getId());
    item.setProductId(itemReq.getProductId());
    item.setQuantity(itemReq.getQuantity());
    itemRepo.save(item);
  });
  
  return toResponse(saved);
}
```

---

### 2. Restaurant Order Flow

**Mobile**
```javascript
// RestaurantMenuScreen.js
useEffect(() => {
  api.get(`/api/restaurants/menu-categories?restaurantId=${id}`)
    .then(resp => setCategories(resp.data?.data));
}, [restaurantId]);

addItem(item) → setCart([...cart, { ...item, qty: 1 }]);

// RestaurantCheckoutScreen.js
handlePlaceOrder() → api.post('/api/foodorders', {
  restaurantId: restaurant.id,
  items: cart.map(i => ({
    menuItemId: i.id,
    name: i.name,
    price: i.price,
    quantity: i.qty
  }))
});

dispatch(addOrder(order));
navigation.navigate('OrderTracking', { orderId: order.id });
```

**Backend**
```java
@PostMapping
public ApiResponse<FoodOrderResponse> create(
  @Valid @RequestBody FoodOrderRequest request
) {
  return ApiResponse.ok("Order created", serv.createOrder(request));
}

public FoodOrderResponse createOrder(FoodOrderRequest request) {
  // 1. Create order
  FoodOrder order = new FoodOrder();
  order.setRestaurantId(request.getRestaurantId());
  order.setStatus("PLACED");
  order.setPaymentStatus("PENDING");
  
  BigDecimal total = request.getItems().stream()
    .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
    .reduce(BigDecimal.ZERO, BigDecimal::add);
  order.setTotalAmount(total);
  
  FoodOrder saved = orderRepo.save(order);
  
  // 2. Save items
  request.getItems().forEach(itemReq -> {
    FoodOrderItem item = new FoodOrderItem();
    item.setOrderId(saved.getId());
    item.setMenuItemId(itemReq.getMenuItemId());
    item.setQuantity(itemReq.getQuantity());
    itemRepo.save(item);
  });
  
  return toResponse(saved);
}
```

---

### 3. Order Tracking (Real-time Updates - Simulated)

**Mobile**
```javascript
useEffect(() => {
  // Simulate status progression
  const seq = ['PLACED', 'CONFIRMED', 'PREPARING', 'OUT_FOR_DELIVERY', 'DELIVERED'];
  let idx = seq.indexOf(statusRef.current);
  
  const interval = setInterval(async () => {
    if (idx < seq.length - 1) {
      idx++;
      const newStatus = seq[idx];
      setStatus(newStatus);
      
      // Update backend
      if (orderType === 'food') {
        await api.patch(`/api/foodorders/${orderId}/status?status=${newStatus}`);
      } else {
        await api.patch(`/api/carts/${orderId}/status?status=${newStatus}`);
      }
      
      // Update Redux
      dispatch(updateOrderStatus({ id: orderId, status: newStatus }));
    }
  }, 4000); // Update every 4 seconds
  
  return () => clearInterval(interval);
}, [orderId, orderType]);
```

---

## Database Schema (Liquibase Changelog)

### Core Tables

```xml
changelog id="1" — users table
changelog id="20" — instamart (categories, products, variants, inventory logs)
changelog id="21" — restaurants (restaurants, menu categories, menu items)
changelog id="22" — cart (carts, cart_items)
changelog id="23" — food_orders (food_orders, food_order_items)
```

See `server/app/src/main/resources/db/changelog/db.changelog-master.xml` for full schema.

---

## Current Status

### ✅ Implemented
- Database schema (Liquibase)
- Backend controllers & services for both flows
- Mobile screens for browsing, cart, checkout, tracking
- Redux state management
- API integration (axios/fetch)
- Admin & vendor dashboards (basic)

### ⚠️ In Development / TODO
- JWT authentication & authorization (scaffolded in `auth/` module)
- WebSocket real-time updates (currently simulated via polling)
- Payment integration (mocked with Stripe/Razorpay placeholders)
- Notification system (mocked)
- Image uploads (for products/restaurants)
- Map integration (Google Maps for delivery tracking)
- Advanced search & filtering
- Order history & reorder functionality
- Ratings & reviews

---

## Running the Application

### Backend
```bash
cd server
./gradlew :app:bootRun
# Starts on http://localhost:4000
```

### Mobile
```bash
cd mobile
npm install
npm start
# Expo: press 'a' for Android, opens on http://10.0.2.2:4000 (emulator)
```

### Vendor Dashboard
```bash
cd vendor
npm install
npm start
# Opens on http://localhost:3001
```

### Admin Panel
```bash
cd admin
npm install
npm start
# Opens on http://localhost:3002
```

---

## Key Observations

1. **Two Separate E2E Flows**
   - Instamart: Product-based (multiple items, quick checkout)
   - FoodOrder: Restaurant-based (menu-centric browsing)

2. **Local State Patterns**
   - Cart uses Redux for persistence across screens (Instamart)
   - Restaurant menu cart uses local component state (FoodOrder)

3. **Backend Modularity**
   - Clear separation: `cart/`, `foodorder/`, `restaurant/`, `instamart/`
   - Each module has controller → service → repository pattern

4. **Simplified Real-time**
   - Not using WebSocket yet; simulated via polling in OrderTrackingScreen
   - Could upgrade to WebSocket for true real-time updates

5. **API Response Wrapper**
   - All responses use `ApiResponse<T>` structure for consistency

6. **Database Indexing**
   - Strategic indexes on foreign keys and frequently filtered columns

---

## Code Quality Notes

✅ **Strengths**
- Clear separation of concerns
- Consistent naming conventions
- Type-safe (Java backend, JavaScript frontend)
- Modular architecture

⚠️ **Potential Improvements**
- Error handling could be more robust (especially in Redux)
- No input validation on frontend forms
- Could benefit from React hooks for more complex screens
- No offline support in mobile app
- PaymentController is mocked but structure is clear

