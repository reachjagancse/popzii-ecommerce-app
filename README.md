Popzii — Full-Stack Food & Grocery Delivery Platform

Overview
--------
Popzii is a comprehensive food & grocery delivery platform similar to Swiggy, Instamart, Blinkit, built with microservices architecture.

Features
--------
- **Customer App**: React Native with authentication, product browsing, cart, checkout, real-time tracking.
- **Vendor Dashboard**: React web app for product management, orders, analytics.
- **Admin Panel**: React web app for vendor management, order monitoring, analytics.
- **Backend**: Spring Boot microservices with JWT auth, WebSocket real-time updates, PostgreSQL/H2 database.
- **Third-Party Integrations**: Mocked Stripe/Razorpay payments, Google Maps, Firebase notifications.

Technology Stack
----------------
- Backend: Spring Boot (Java 17), Gradle 9.3.1, PostgreSQL/H2
- Frontend: React Native (Customer), React (Vendor/Admin)
- Real-time: WebSocket
- Database: PostgreSQL (prod), H2 (dev)

Project Structure
-----------------
- server/ — Spring Boot backend with microservices modules
- mobile/ — React Native customer app
- vendor/ — React vendor dashboard
- admin/ — React admin panel

Prerequisites
-------------
- Java 17
- Node.js 18+
- Gradle 9.3.1
- PostgreSQL (optional, H2 for dev)

Running the Application
-----------------------
1. **Backend**:
   ```
   cd server
   ./gradlew :app:bootRun
   ```
   - API: http://localhost:4000
   - H2 Console: http://localhost:4000/h2-console (user: sa, password: )

2. **Customer App**:
   ```
   cd mobile
   npm install
   npm start
   ```
   - Open in Expo Go or emulator

3. **Vendor Dashboard**:
   ```
   cd vendor
   npm install
   npm start
   ```
   - Open http://localhost:3001

4. **Admin Dashboard**:
   ```
   cd admin
   npm install
   npm start
   ```
   - Open http://localhost:3002

Validation & Testing
--------------------
1. **Register/Login**: Use customer app to register/login.
2. **Browse Products**: View products by vendor/category.
3. **Add to Cart & Checkout**: Add items, select payment (mock), place order.
4. **Real-time Tracking**: Monitor order status via WebSocket.
5. **Vendor Management**: Login to vendor dashboard, manage products/orders.
6. **Admin Oversight**: Login to admin panel, view orders/vendors.

API Endpoints
-------------
- POST /api/auth/register — User registration
- POST /api/auth/login — User login
- GET /api/products — List products
- POST /api/orders — Create order
- WebSocket /ws — Real-time updates

Database Schema
---------------
- users, vendors, products, orders, order_items, transactions

Production Setup
----------------
- Switch to PostgreSQL in application.yml
- Deploy backend to cloud (AWS/GCP)
- Build mobile app for stores
- Use real APIs for payments/maps/notifications

Support
-------
For issues, check logs or contact dev team.
