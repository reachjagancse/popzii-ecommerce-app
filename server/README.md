Popzii Server — Backend Microservices

Overview
--------
Modular Spring Boot backend for Popzii delivery platform with microservices architecture.

Modules
-------
- app/ — Main application
- auth/ — JWT authentication
- user/ — User management
- vendor/ — Vendor management
- product/ — Product/inventory
- order/ — Order processing
- payment/ — Payment integration (mocked)
- notification/ — Notifications (mocked)
- admin/ — Admin services

Prerequisites
-------------
- Java 17
- Gradle 9.3.1
- PostgreSQL (optional, H2 for dev)

Running Locally
---------------
1. Set JAVA_HOME to Java 17.
2. Run the app:
   ```
   ./gradlew :app:bootRun
   ```
3. Server starts on http://localhost:4000

Database
--------
- Dev: H2 in-memory
- Prod: PostgreSQL
- Schema: Auto-created via Liquibase

API Endpoints
-------------
- Auth: /api/auth/register, /api/auth/login
- Products: /api/products
- Orders: /api/orders
- Payments: /api/payment
- Instamart: /api/instamart/categories, /api/instamart/products, /api/instamart/variants
- Restaurants: /api/restaurants, /api/restaurants/menu-categories, /api/restaurants/menu-items
- Food orders: /api/foodorders
- Carts (Instamart checkout): /api/carts/checkout, /api/carts/{id}
- WebSocket: /ws for real-time updates

Validation
----------
- Test auth: Register/login user
- Test products: GET /api/products
- Test orders: POST /api/orders
- Check H2 console for data

Sample Requests
---------------
Instamart categories:
```
curl -X GET "http://localhost:4000/api/instamart/categories?page=0&size=20"
```

Instamart products (filter by category + search):
```
curl -X GET "http://localhost:4000/api/instamart/products?categoryId=1&search=milk&page=0&size=20"
```

Create Instamart product:
```
curl -X POST "http://localhost:4000/api/instamart/products" \
   -H "Content-Type: application/json" \
   -d '{"categoryId":1,"name":"Oats","basePrice":3.20}'
```

Restaurants (filter by cuisine + min rating):
```
curl -X GET "http://localhost:4000/api/restaurants?cuisine=Indian&minRating=4.0&page=0&size=20"
```

Create restaurant menu item:
```
curl -X POST "http://localhost:4000/api/restaurants/menu-items" \
   -H "Content-Type: application/json" \
   -d '{"categoryId":1,"name":"Veg Kebab","price":8.50,"isVeg":true}'
```

Place food order:
```
curl -X POST "http://localhost:4000/api/foodorders" \
   -H "Content-Type: application/json" \
   -d '{"restaurantId":1,"items":[{"menuItemId":1,"name":"Crispy Paneer","price":6.50,"quantity":2}]}'
```

Instamart checkout:
```
curl -X POST "http://localhost:4000/api/carts/checkout" \
   -H "Content-Type: application/json" \
   -d '{"items":[{"productId":1,"name":"Whole Milk","price":2.40,"quantity":2}]}'
```

Switch to PostgreSQL
--------------------
1. Start Postgres:
   ```
   docker run --name popzii-postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=popzii -p 5432:5432 -d postgres:15
   ```
2. Update application.yml datasource.

Production
----------
- Build: ./gradlew build
- Deploy JAR to cloud

Switch to Postgres (optional)
1. Start Postgres (Docker):

```powershell
docker run --name popzii-postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=popzii -p 5432:5432 -d postgres:15
```

2. Update `app/src/main/resources/application.yml` datasource section:

```yaml
spring:
	datasource:
		url: jdbc:postgresql://localhost:5432/popzii
		username: postgres
		password: postgres
```

3. Restart the app.

Running tests
- Run all tests across modules:

```powershell
gradle test
```

Development notes
- The project is modular: implement new features inside their modules and add `implementation project(':moduleName')` to `app/build.gradle` if needed.
- JWT auth, WebSocket order updates, and vendor/admin flows are scaffolded as modules — complete implementations should be added inside the module packages.
- Liquibase is configured: add changeSets under `app/src/main/resources/db/changelog`.

Mobile (React Native) quick run
1. Install dependencies and start Expo (from root):

```powershell
cd mobile
npm install
npm start
```

2. Open the Expo app on your device or emulator and point the API base URL to `http://<your-machine-ip>:4000` (use your local IP if testing on a device).

Need a Gradle wrapper?
- If you prefer not to install Gradle globally, I can add a `gradlew` wrapper to the `server` project — tell me and I'll add it.

If you want, I can now:
- implement JWT authentication and role-based access,
- add WebSocket order updates,
- or scaffold the React Native screens and API integration.
Popzii Server (Spring Boot + Postgres)

Requirements
- Java 17+
- Gradle (or use your IDE to import as Gradle project)
- Postgres running with a `popzii` database

Run locally

1. Create Postgres DB and user or update `src/main/resources/application.properties`.

2. From the `server` folder run:

```bash
gradle bootRun
```

API
- `GET /api/health` — health check
- `GET /api/products` — list products
- `POST /api/products` — create product (JSON body)
