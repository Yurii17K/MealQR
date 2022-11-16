![example workflow](https://github.com/Yurii17K/MealQR/actions/workflows/maven.yml/badge.svg)

# MealQR
 
## Git flow
For each feature create a new branch from **dev** branch with a name **feature-{feature-name}**

## 🛠 Backend Endpoints (THIS LIST IS NOT UPDATED)

**To view and try out all the UP TO DATE endpoints navigate to `{host}/swagger-ui/index.html`**

# -- NOT UP TO DATE --
### 🔐 Users:
- Create a customer:
  - **POST** `{host}/users/customer?userName={value}&userLastName={value}&userCity={value}&allergies={value}&userEmail={value}&userPass={value}`
- Create a restaurant employee:
  - **POST** `{host}/users/rest-emp?userName={value}&userLastName={value}&userCity={value}&restaurantName={value}&userEmail={value}&userPass={value}`
- Sign in:
  - **POST** `{host}/users/login?userEmail={value}&userPass={value}`
- Update customer allergies:
  - **PATCH** `{host}/users/customer/allergies?userEmail={value}&allergies={value}`

### 🛒 Cart items:
- Get the list of cart items in customer cart: 
  - **GET** `{host}/cart-items?userEmail={value}`
- Get the cost of cart items in customer cart: 
  - **GET** `{host}/cart-items/cost?userEmail={value}`
- Add dish to customer cart: 
  - **POST** `{host}/cart-items?userEmail={value}&dishName={value}&restaurantName={value}`
- Change dish quantity in customer cart: 
  - **PATCH** `{host}/cart-items?userEmail={value}&dishName={value}&restaurantName={value}&quantity={value}`
- Delete dish from customer cart: 
  - **DELETE**`{host}/cart-items?userEmail={value}&dishName={value}&restaurantName={value}`
- Clear customer cart: 
  - **DELETE**`{host}/cart-items/clear?userEmail={value}`

### 🍲 Dishes:
- Get all dishes in restaurant:
  - **GET** `{host}/dishes?restaurantName={value}`
- Get all dishes in restaurant with comments and average ratings for each dish + suitable for the user (no allergies and preferences included):
  - **GET** `{host}/dishes/preferences?userEmail={value}&restaurantName={value}`
- Get random dish:
  - **GET** `{host}/dishes/random`
- Get random dish in restaurant:
  - **GET** `{host}/dishes/random/{restaurantName}`
- Add dish to restaurant offer:
  - **POST** `{host}/dishes/restaurant?dishName={value}&restaurantName={value}&dishImg={value}&dishPrice={value}&dishDescription={value}`
- Update dish in restaurant offer:
  - **PUT** `{host}/dishes/restaurant?dishName={value}&restaurantName={value}&dishImg={value}&dishPrice={value}&dishDescription={value}`
- Remove dish from restaurant offer:
  - **DELETE** `{host}/dishes/restaurant?dishName={value}&restaurantName={value}`

### 💬 Comments and ratings:
- Add or update comment to dish:
  - **POST** `{host}/opinions/comments?userEmail={value}&dishName={value}&restaurantName={value}&comment={value}`
- Add or update rating to dish:
  - **POST** `{host}/opinions/ratings?userEmail={value}&dishName={value}&restaurantName={value}&rating={value}`

### 📓 QR:
- Generate data for QR code from customer cart:
  - **GET** `{host}/qr?userEmail={value}`
