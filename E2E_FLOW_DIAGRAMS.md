# Popzii — Visual Architecture & Flow Diagrams

## System Architecture

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              POPZII PLATFORM                                    │
└─────────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────┐  ┌──────────────────────┐  ┌──────────────────────┐
│   MOBILE CLIENT      │  │ VENDOR DASHBOARD     │  │  ADMIN DASHBOARD     │
│  (React Native)      │  │  (React Web)         │  │   (React Web)        │
│                      │  │                      │  │                      │
│ • HomeScreen        │  │ • Dashboard          │  │ • Dashboard          │
│ • RestaurantScreen  │  │ • Products           │  │ • Vendors            │
│ • CartScreen        │  │ • Orders             │  │ • Products           │
│ • CheckoutScreen    │  │ • Analytics          │  │ • Orders             │
│ • TrackingScreen    │  │                      │  │ • Analytics          │
│ • ProfileScreen     │  │ Base: localhost:3001 │  │ Base: localhost:3002 │
│                      │  │                      │  │                      │
│ Base: 10.0.2.2:4000 │  │                      │  │                      │
│ (emulator)           │  │                      │  │                      │
└────────┬─────────────┘  └────────┬─────────────┘  └────────┬─────────────┘
         │                         │                         │
         │ axios/fetch             │ axios                   │ axios
         │ (JSON)                  │ (JSON)                  │ (JSON)
         │                         │                         │
         └─────────────────────────┴─────────────────────────┘
                                   │
                                   ▼ (All: http://localhost:4000)
         ┌───────────────────────────────────────────────────────┐
         │         SPRING BOOT BACKEND (Java 17)                 │
         │         Microservices Architecture                    │
         ├───────────────────────────────────────────────────────┤
         │                                                       │
         │  ┌──────────────┐  ┌──────────────┐  ┌────────────┐ │
         │  │   auth/      │  │   user/      │  │ payment/   │ │
         │  │ • AuthSvc    │  │ • UserSvc    │  │ • PaymentSvc (mocked)
         │  │ • JwtFilter  │  │ • Roles      │  │            │ │
         │  └──────────────┘  └──────────────┘  └────────────┘ │
         │                                                       │
         │  ┌──────────────────┐  ┌──────────────────────────┐ │
         │  │   instamart/     │  │   restaurant/            │ │
         │  │ • CategoriesCtrl │  │ • RestaurantCtrl         │ │
         │  │ • ProductsCtrl   │  │ • MenuCategoriesCtrl     │ │
         │  │ • VariantsCtrl   │  │ • MenuItemsCtrl          │ │
         │  │ • InstamartSvc   │  │ • RestaurantCatalogSvc   │ │
         │  └──────────────────┘  └──────────────────────────┘ │
         │                                                       │
         │  ┌──────────────────┐  ┌──────────────────────────┐ │
         │  │   cart/          │  │   foodorder/             │ │
         │  │ • CartCtrl       │  │ • FoodOrderCtrl          │ │
         │  │ • CartSvc        │  │ • FoodOrderSvc           │ │
         │  │ • CartRepo       │  │ • FoodOrderRepo          │ │
         │  └──────────────────┘  └──────────────────────────┘ │
         │                                                       │
         │  ┌──────────────┐  ┌──────────────┐  ┌────────────┐ │
         │  │  vendor/     │  │   order/     │  │ notification/ │
         │  │ • VendorSvc  │  │ • OrderSvc   │  │ • NotifSvc (mocked)
         │  │              │  │              │  │            │ │
         │  └──────────────┘  └──────────────┘  └────────────┘ │
         │                                                       │
         │  ┌────────────────────────────────────────────────┐ │
         │  │  common/ — Shared utilities, DTOs, ApiResponse │ │
         │  └────────────────────────────────────────────────┘ │
         │                                                       │
         └───────────────────────────┬───────────────────────────┘
                                     │
                                     ▼
         ┌───────────────────────────────────────────────────────┐
         │              PostgreSQL / H2 Database                |
         │              (Liquibase Migration)                    │
         ├───────────────────────────────────────────────────────┤
         │                                                       │
         │  ┌───────────────┐       ┌───────────────────────┐  │
         │  │  Core Tables  │       │  Order Flow Tables    │  │
         │  ├───────────────┤       ├───────────────────────┤  │
         │  │ • users       │       │ • carts               │  │
         │  │   (PK: id)    │       │ • cart_items          │  │
         │  │ • vendors     │       │ • food_orders         │  │
         │  │ • restaurants │       │ • food_order_items    │  │
         │  └───────────────┘       └───────────────────────┘  │
         │                                                       │
         │  ┌───────────────┐       ┌───────────────────────┐  │
         │  │ Instamart     │       │  Restaurant Menu      │  │
         │  ├───────────────┤       ├───────────────────────┤  │
         │  │ • categories  │       │ • categories          │  │
         │  │ • products    │       │ • menu_items          │  │
         │  │ • variants    │       │ •inventory_logs       │  │
         │  │ • inv_logs    │       │                       │  │
         │  └───────────────┘       └───────────────────────┘  │
         │                                                       │
         └───────────────────────────────────────────────────────┘
```

---

## Data Flow: INSTAMART

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    CUSTOMER INSTAMART JOURNEY                               │
└─────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────┐
│  STEP 1: DISCOVER PRODUCTS                                                  │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  HomeScreen (Initial)                                                       │
│  ├─ useEffect() triggers                                                    │
│  ├─ dispatch(fetchCategories())                                             │
│  │  └─ api.get('/api/instamart/categories?page=0&size=20')                 │
│  │     └─ Backend: InstamartCategoryController.list()                       │
│  │        └─ DB: SELECT * FROM instamart_categories                         │
│  │        └─ Returns: [{ id: 1, name: 'Dairy' }, ...]                      │
│  │     └─ Redux: categoriesSlice.fulfilled({ items: [...] })               │
│  │                                                                           │
│  └─ dispatch(fetchProducts())                                               │
│     └─ api.get('/api/instamart/products?categoryId=1&page=0&size=100')    │
│        └─ Backend: InstamartProductController.list()                        │
│           └─ DB: SELECT * FROM instamart_products WHERE category_id=1       │
│           └─ Returns: [{ id: 1, name: 'Milk', basePrice: 2.40 }, ...]     │
│        └─ Redux: productsSlice.fulfilled({ items: [...] })                 │
│                                                                              │
│  Screen State:                                                              │
│  ├─ categories.items = [{ id: 1, name: 'Dairy' }, ...]                    │
│  ├─ products.items = [{ id: 1, name: 'Milk', basePrice: 2.40 }, ...]      │
│  └─ Display: Category chips + Product cards                                │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────┐
│  STEP 2: BUILD CART (LOCAL)                                                 │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  User tabs "Add to Cart" on Milk (basePrice: $2.40)                        │
│  ├─ dispatch(addToCart({ id: 1, name: 'Milk', price: 2.40 }))            │
│  │  └─ Redux cartSlice:                                                     │
│  │     └─ state.items = [{ id: 1, name: 'Milk', price: 2.40, qty: 1 }]    │
│  │                                                                           │
│  User adds another 2x Milk:                                                 │
│  ├─ dispatch(addToCart({ id: 1, ... })) twice                             │
│  │  └─ Redux checks existing, increments qty                               │
│  │     └─ state.items = [{ id: 1, name: 'Milk', price: 2.40, qty: 3 }]    │
│  │                                                                           │
│  User browses more products, adds Butter (basePrice: $4.50)                │
│  ├─ state.items = [                                                         │
│  │   { id: 1, name: 'Milk', price: 2.40, qty: 3 },                        │
│  │   { id: 2, name: 'Butter', price: 4.50, qty: 1 }                       │
│  │ ]                                                                         │
│  │                                                                           │
│  └─ Total: (2.40 × 3) + (4.50 × 1) = $11.70                               │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────┐
│  STEP 3: CHECKOUT                                                           │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  CartScreen                                                                 │
│  ├─ Displays cart items from Redux state.cart.items                        │
│  ├─ User updates qty: -1 button → dispatch(updateQty({ id: 1, qty: 2 }))  │
│  │  └─ Redux: state.items[0].qty = 2                                       │
│  ├─ User removes Butter: → dispatch(removeFromCart({ id: 2 }))            │
│  │  └─ Redux: filters out item                                              │
│  │                                                                           │
│  Final cart:                                                                 │
│  ├─ [{ id: 1, name: 'Milk', price: 2.40, qty: 2 }]                        │
│  └─ Total: $4.80                                                            │
│                                                                              │
│  User hits "Checkout" button:                                               │
│  ├─ handleCheckout() → api.post('/api/carts/checkout', {                  │
│  │   items: [                                                               │
│  │     { productId: 1, name: 'Milk', price: 2.40, quantity: 2 }           │
│  │   ]                                                                      │
│  │ })                                                                       │
│  │                                                                           │
│  └─ Backend: CartController.checkout()                                     │
│     └─ CartServiceImpl.checkout(request)                                    │
│        └─ 1. CREATE cart record:                                           │
│        │     INSERT INTO carts (user_id, status, created_at, updated_at)  │
│        │     VALUES (123, 'CHECKED_OUT', now(), now())                    │
│        │     RETURNING id → 456                                            │
│        │                                                                    │
│        └─ 2. CREATE cart_items:                                            │
│             INSERT INTO cart_items (cart_id, product_id, name, qty, ...)  │
│             VALUES (456, 1, 'Milk', 2, ...)                               │
│                                                                              │
│        └─ 3. RETURN CartResponse:                                          │
│              {                                                              │
│                "id": 456,                                                   │
│                "userId": 123,                                               │
│                "status": "CHECKED_OUT",                                     │
│                "items": [{ id: 789, name: 'Milk', qty: 2 }],              │
│                "createdAt": "2026-02-22T10:30:00"                         │
│              }                                                               │
│                                                                              │
│  Frontend receives response:                                                │
│  ├─ order = resp.data?.data = { id: 456, ... }                            │
│  ├─ dispatch(addOrder(order)) — add to orders state                       │
│  ├─ dispatch(clearCart()) — empty local cart                              │
│  └─ navigation.navigate('OrderTracking', {                                 │
│       orderId: 456,                                                         │
│       orderType: 'cart'                                                     │
│     })                                                                      │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────┐
│  STEP 4: REAL-TIME TRACKING                                                 │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  OrderTrackingScreen({ orderId: 456, orderType: 'cart' })                  │
│                                                                              │
│  Initial load:                                                              │
│  ├─ GET /api/carts/456                                                     │
│  │  └─ Backend: CartController.get(456)                                    │
│  │     └─ DB: SELECT * FROM carts WHERE id=456                             │
│  │     └─ Returns: { id: 456, status: 'CHECKED_OUT', ... }                │
│  ├─ setStatus('CHECKED_OUT')                                               │
│  └─ Display: "Order #456 ▶ Status: CHECKED_OUT"                           │
│                                                                              │
│  Simulated real-time updates (every 4 seconds):                            │
│  ├─ Sequence: CHECKED_OUT → CONFIRMED → PREPARING →                       │
│  │             OUT_FOR_DELIVERY → DELIVERED                                │
│  │                                                                           │
│  │ Each update:                                                             │
│  │ ├─ setStatus('CONFIRMED')                                               │
│  │ ├─ dispatch(updateOrderStatus({ id: 456, status: 'CONFIRMED' }))      │
│  │ ├─ PATCH /api/carts/456/status?status=CONFIRMED                       │
│  │ │  └─ Backend updates DB:                                               │
│  │ │     UPDATE carts SET status='CONFIRMED', updated_at=now()           │
│  │ │     WHERE id=456                                                      │
│  │ ├─ Screen re-renders: "Order #456 ▶ Status: CONFIRMED"               │
│  │ └─ Timeline updates visually                                            │
│  │                                                                           │
│  └─ Final: "Order #456 ▶ Status: DELIVERED" (timeline complete)          │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘

```

---

## Data Flow: FOODORDER

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                   CUSTOMER FOOD ORDER JOURNEY                               │
└─────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────┐
│  STEP 1: DISCOVER RESTAURANTS                                               │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  RestaurantsScreen                                                          │
│  ├─ User enters filters:                                                    │
│  │  ├─ Search: "Indian"                                                     │
│  │  ├─ Cuisine: "N/A"                                                       │
│  │  └─ Min Rating: "4.0"                                                    │
│  │                                                                           │
│  ├─ loadRestaurants() → api.get(                                           │
│  │   '/api/restaurants?page=0&size=50'                                     │
│  │ )                                                                        │
│  │  └─ Backend: RestaurantController.list()                               │
│  │     └─ DB: SELECT * FROM restaurants WHERE is_active=true              │
│  │     └─ Returns: [                                                        │
│  │         { id: 1, name: 'Taj', cuisine: 'Indian', rating: 4.8 },        │
│  │         { id: 2, name: 'Moon', cuisine: 'Chinese', rating: 4.2 }       │
│  │       ]                                                                  │
│  │                                                                           │
│  ├─ setRestaurants([...])                                                  │
│  └─ Display: [Taj card, Moon card, ...]                                    │
│                                                                              │
│  User taps "Taj" restaurant card:                                           │
│  ├─ navigation.navigate('RestaurantMenu', { restaurant: tajObj })          │
│  └─ Pass restaurant object to next screen                                   │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────┐
│  STEP 2: BROWSE MENU & BUILD LOCAL CART                                    │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  RestaurantMenuScreen({ restaurant: { id: 1, name: 'Taj' } })             │
│                                                                              │
│  Load menu structure:                                                       │
│  ├─ GET /api/restaurants/menu-categories?restaurantId=1&page=0&size=50   │
│  │  └─ Backend: RestaurantMenuCategoryController.list()                   │
│  │     └─ DB: SELECT * FROM restaurant_menu_categories                    │
│  │            WHERE restaurant_id=1 AND is_active=true                    │
│  │     └─ Returns: [                                                        │
│  │         { id: 10, name: 'Appetizers', restId: 1 },                     │
│  │         { id: 11, name: 'Mains', restId: 1 }                           │
│  │       ]                                                                  │
│  │                                                                           │
│  ├─ setCategories([...])                                                   │
│  ├─ setSelectedCategoryId(10)  — Default to first category                │
│  └─ Display: Category chips [Appetizers | Mains]                           │
│                                                                              │
│  User selects "Appetizers" category (keep catId=10):                      │
│  ├─ GET /api/restaurants/menu-items?categoryId=10&page=0&size=50        │
│  │  └─ Backend: RestaurantMenuItemController.list()                       │
│  │     └─ DB: SELECT * FROM restaurant_menu_items                         │
│  │            WHERE category_id=10 AND is_available=true                  │
│  │     └─ Returns: [                                                        │
│  │         { id: 100, name: 'Samosa', price: 2.50, isVeg: true },         │
│  │         { id: 101, name: 'Paneer Tikka', price: 6.00, isVeg: true }    │
│  │       ]                                                                  │
│  │                                                                           │
│  ├─ setItems([...])                                                        │
│  └─ Display: Item cards with Add buttons                                   │
│                                                                              │
│  User adds items (local cart state, NOT backend):                          │
│  ├─ Tap "Add" on Samosa:                                                   │
│  │  └─ addItem({ id: 100, name: 'Samosa', price: 2.50 })                 │
│  │     └─ setCart(prev => [...prev, { ...item, qty: 1 }])                │
│  │        └─ cart = [{ id: 100, name: 'Samosa', price: 2.50, qty: 1 }]   │
│  │                                                                           │
│  ├─ Tap "Add" on Paneer Tikka:                                             │
│  │  └─ setCart([                                                           │
│  │       { id: 100, name: 'Samosa', price: 2.50, qty: 1 },               │
│  │       { id: 101, name: 'Paneer Tikka', price: 6.00, qty: 1 }          │
│  │     ])                                                                  │
│  │                                                                           │
│  │  Calculate total: (2.50 × 1) + (6.00 × 1) = $8.50                      │
│  │                                                                           │
│  └─ Button: "View Cart · $8.50"                                            │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────┐
│  STEP 3: CHECKOUT (PLACE ORDER)                                             │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  RestaurantCheckoutScreen({                                                │
│    restaurant: { id: 1, name: 'Taj' },                                     │
│    cart: [                                                                  │
│      { id: 100, name: 'Samosa', price: 2.50, qty: 1 },                   │
│      { id: 101, name: 'Paneer Tikka', price: 6.00, qty: 1 }              │
│    ],                                                                       │
│    total: 8.50                                                              │
│  })                                                                         │
│                                                                              │
│  Display review:                                                            │
│  ├─ Restaurant: "Taj"                                                       │
│  ├─ Items:                                                                  │
│  │  ├─ Samosa ×1 = $2.50                                                   │
│  │  ├─ Paneer Tikka ×1 = $6.00                                             │
│  │  └─ Total = $8.50                                                        │
│  │                                                                           │
│  └─ [Checkout button]                                                      │
│                                                                              │
│  User taps "Place order":                                                   │
│  ├─ handlePlaceOrder() → api.post('/api/foodorders', {                    │
│  │   restaurantId: 1,                                                      │
│  │   items: [                                                              │
│  │     { menuItemId: 100, name: 'Samosa', price: 2.50, quantity: 1 },   │
│  │     { menuItemId: 101, name: 'Paneer Tikka', price: 6.00, quantity: 1 }
│  │   ]                                                                      │
│  │ })                                                                       │
│  │                                                                           │
│  └─ Backend: FoodOrderController.create()                                  │
│     └─ FoodOrderServiceImpl.createOrder(request)                            │
│        └─ 1. CREATE food_order record:                                     │
│        │     INSERT INTO food_orders (user_id, restaurant_id, status,     │
│        │                               payment_status, total_amount, ...)  │
│        │     VALUES (123, 1, 'PLACED', 'PENDING', 8.50, ...)            │
│        │     RETURNING id → 789                                            │
│        │                                                                    │
│        └─ 2. CREATE food_order_items:                                      │
│        │     INSERT INTO food_order_items (order_id, menu_item_id, ...)   │
│        │     VALUES (789, 100, ...), (789, 101, ...)                      │
│        │                                                                    │
│        └─ 3. RETURN FoodOrderResponse:                                     │
│              {                                                              │
│                "id": 789,                                                   │
│                "userId": 123,                                               │
│                "restaurantId": 1,                                           │
│                "status": "PLACED",                                          │
│                "paymentStatus": "PENDING",                                 │
│                "totalAmount": 8.50,                                         │
│                "items": [                                                  │
│                  { id: 1001, name: 'Samosa', qty: 1 },                    │
│                  { id: 1002, name: 'Paneer Tikka', qty: 1 }               │
│                ],                                                           │
│                "createdAt": "2026-02-22T11:00:00"                         │
│              }                                                              │
│                                                                              │
│  Frontend response handling:                                                │
│  ├─ order = resp.data?.data                                                │
│  ├─ dispatch(addOrder(order)) — Add to Redux orders state                 │
│  ├─ dispatch(clearCart()) — (if using Redux cart)                         │
│  └─ navigation.navigate('OrderTracking', {                                 │
│       orderId: 789,                                                         │
│       orderType: 'food'                                                     │
│     })                                                                      │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────┐
│  STEP 4: REAL-TIME TRACKING                                                 │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  OrderTrackingScreen({ orderId: 789, orderType: 'food' })                  │
│                                                                              │
│  Initial load:                                                              │
│  ├─ GET /api/foodorders/789                                                │
│  │  └─ Backend: FoodOrderController.get(789)                              │
│  │     └─ DB: SELECT * FROM food_orders WHERE id=789                      │
│  │     └─ Returns: { id: 789, status: 'PLACED', ... }                     │
│  ├─ setStatus('PLACED')                                                    │
│  └─ Display: "Order #789 ▶ Status: PLACED"                                │
│                                                                              │
│  Simulated progression (every 4 seconds):                                   │
│  ├─ T+4s: PLACED → CONFIRMED                                              │
│  │         PATCH /api/foodorders/789/status?status=CONFIRMED             │
│  │         UPDATE food_orders SET status='CONFIRMED' WHERE id=789         │
│  │         Screen: "Order #789 ▶ Status: CONFIRMED"                      │
│  │                                                                          │
│  ├─ T+8s: CONFIRMED → PREPARING                                           │
│  │         PATCH /api/foodorders/789/status?status=PREPARING             │
│  │         UPDATE food_orders SET status='PREPARING' WHERE id=789         │
│  │         Screen: "Order #789 ▶ Status: PREPARING"                      │
│  │                                                                          │
│  ├─ T+12s: PREPARING → OUT_FOR_DELIVERY                                  │
│  │          PATCH /api/foodorders/789/status?status=OUT_FOR_DELIVERY    │
│  │          UPDATE food_orders SET status='OUT_FOR_DELIVERY' WHERE...    │
│  │          Screen: "Order #789 ▶ Status: OUT_FOR_DELIVERY"             │
│  │                                                                          │
│  └─ T+16s: OUT_FOR_DELIVERY → DELIVERED                                  │
│           PATCH /api/foodorders/789/status?status=DELIVERED             │
│           UPDATE food_orders SET status='DELIVERED' WHERE id=789         │
│           Screen: "Order #789 ▶ Status: DELIVERED" ✓ Timeline complete  │
│           Interval cleared                                                 │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘

```

---

## Redux State Lifecycle (Mobile)

```
INITIALIZATION:
├─ App.js loads
├─ Provider store={store}
├─ store = configureStore({
│   reducer: {
│     products: productsReducer,    // { items: [], status: 'idle' }
│     categories: categoriesReducer,// { items: [], status: 'idle' }
│     cart: cartReducer,             // { items: [] }
│     orders: ordersReducer          // { items: [] }
│   }
│ })
└─ RootNavigator mounts

INSTAMART FLOW STATE CHANGES:
┌─ HomeScreen mounts
│
├─ dispatch(fetchCategories())
│  ├─ productsSlice.pending → { status: 'loading' }
│  ├─ API call succeeds
│  └─ categoriesSlice.fulfilled → { items: [...], status: 'succeeded' }
│
├─ dispatch(fetchProducts({ categoryId: 1 }))
│  ├─ productsSlice.pending → { status: 'loading' }
│  ├─ API call succeeds
│  └─ productsSlice.fulfilled → { items: [...], status: 'succeeded' }
│
├─ User adds product
│  └─ dispatch(addToCart({ id, name, price }))
│     └─ cartSlice.reducer → cart.items.push() or increment qty
│
├─ Navigation to CartScreen
│  └─ useSelector(s => s.cart.items) → displays current cart
│
├─ User hits checkout
│  ├─ API POST /api/carts/checkout
│  └─ dispatch(addOrder(response))
│     └─ ordersSlice.reducer → orders.items.push()
│
├─ dispatch(clearCart())
│  └─ cartSlice.reducer → cart.items = []
│
└─ Navigation to OrderTrackingScreen
   ├─ useSelector(s => s.orders.items.find(o => o.id === orderId))
   └─ dispatch(updateOrderStatus({ id, status })) × 5 times (every 4s)
      └─ Update Redux + Backend

FOOD ORDER FLOW STATE CHANGES:
┌─ RestaurantsScreen mounts
│  └─ api.get('/api/restaurants') → local state (not Redux)
│
├─ Navigation → RestaurantMenuScreen
│  └─ api.get('/api/restaurants/menu-categories') → local state
│  └─ api.get('/api/restaurants/menu-items') → local state
│
├─ User adds items (local component state, not Redux)
│  └─ setCart([...])
│
├─ Navigation → RestaurantCheckoutScreen
│  └─ Display cart items from route.params
│
├─ User places order
│  ├─ api.post('/api/foodorders')
│  └─ dispatch(addOrder(response))
│     └─ ordersSlice.reducer → orders.items.push()
│
└─ Navigation → OrderTrackingScreen
   ├─ useSelector(s => s.orders.items.find(o => o.id === orderId))
   └─ dispatch(updateOrderStatus({ id, status })) × 5 times

```

---

## API Response Contract

```javascript
All Backend Responses Follow:

SUCCESS (200):
{
  "status": "success",
  "message": "Checkout completed",
  "data": {
    "id": 456,
    "userId": 123,
    "status": "CHECKED_OUT",
    "items": [...],
    "createdAt": "2026-02-22T10:30:00"
  }
}

ERROR (4xx/5xx):
{
  "status": "error",
  "message": "Cart not found",
  "data": null
}

Frontend Usage:
const resp = await api.post('/api/carts/checkout', payload);
const order = resp.data?.data;  // Extract actual payload
dispatch(addOrder(order));
```

---

## Database Relationship Diagram

```
                          users
                            │
                ┌───────────┼────────────┐
                │           │            │
            vendors    restaurants   cart_user_id
                │           │       (foreign key)
                │        menu_cat  
                │           │
                │        menu_item
                │           │
                │           │
    ┌───────────┴───────────┴───────────────────┐
    │                                           │
    │     INSTAMART PATH                       │
    │                                           │
    ├─ instamart_categories                    │
    │   └─ instamart_products                  │
    │      └─ instamart_product_variants       │
    │      └─ instamart_inventory_logs         │
    │                                           │
    │     FOOD ORDER PATH                      │
    │                                           │
    ├─ food_orders ←─────────────────────────┐ │
    │   └─ food_order_items                 │ │
    │      └─ restaurant_menu_items ────────┤ │
    │         └─ restaurant_menu_categories │ │
    │            └─ restaurants ────────────┤ │
    │               └─ users ───────────────┼─┤
    │                  └─ vendors           │ │
    │                                        │ │
    │     CART PATH                         │ │
    │                                        │ │
    └─ carts ──────────────────────────────┐ │
        └─ cart_items                      │ │
           └─ instamart_products ──────────┘ │
              └─ instamart_product_variants  │
                                             │
Indices Created On:                         │
├─ restaurants.owner_id ──────────────────┘ │
├─ restaurants.cuisine                      │
├─ instamart_products.category_id           │
├─ instamart_product_variants.product_id    │
├─ instamart_product_variants.sku           │
├─ restaurant_menu_categories.restaurant_id │
├─ restaurant_menu_items.category_id        │
└─ (+ FK constraints for data integrity)
```

---

## Error Handling Flow

```
┌─ Mobile calls API
│
├─ api.post('/api/carts/checkout', payload)
│  ├─ Network Error?
│  │  └─ throw new Error(`HTTP error! status: ${status}`)
│  │     └─ iOS: rejected promise
│  │     └─ Frontend catch block (currently minimal)
│  │
│  ├─ Backend Exception?
│  │  └─ @RestControllerAdvice GlobalExceptionHandler catches it
│  │     └─ Return: { status: 'error', message: '...', data: null }
│  │
│  └─ Success?
│     └─ Return: { status: 'success', data: {...} }
│
└─ No Redux error state currently
   └─ TODO: Add error handling to slices
```

---

## Performance Considerations

```
FRONTEND:
✅ Redux caching prevents redundant API calls after first load
✅ React memo on ProductCard (if implemented) prevents re-renders
✅ Local state for restaurant menu cart (no extra API calls)
⚠️  No pagination on product lists (loads all into Redux)
⚠️  No image lazy loading
⚠️  No request debouncing/cancellation

BACKEND:
✅ Database indices on foreign keys & search columns
✅ Liquibase migration for schema management
✅ Microservices isolation (each service independent)
⚠️  No caching layer (Redis) implemented
⚠️  No database connection pooling config visible
⚠️  No rate limiting

REAL-TIME:
✅ Simulated status updates work client-side
⚠️  Not using WebSocket (polling-based simulation)
⚠️  4-second interval is arbitrary
△  Would benefit from true event streaming
```

