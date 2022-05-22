# MealQR
 
## Git flow
For each feature create a new branch from **dev** branch with a name **feature-{feature-name}**

## 🛠 Backend Endpoints

### 🔐 Users:
- Create a customer:
  - **POST** `{host}/users/customer?userName={value}&userLastName={value}&userCity={value}&allergies={value}&userEmail={value}&userPass={value}`
- Create a restaurant employee:
  - **POST** `{host}/users/rest-emp?userName={value}&userLastName={value}&userCity={value}&restaurantName={value}&userEmail={value}&userPass={value}`
- Sign in:
  - **POST** `{host}/users/login?userEmail={value}&userPass={value}`
- Update customer allergies:
  - **POST** `{host}/users/customer/alergies?userEmail={value}&allergies={value}`

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
- Get all dishes in restaurant with comments and average ratings for each dish:
  - **GET** `{host}/dishes/opinions?restaurantName={value}`
- Get random dish:
  - **GET** `{host}/dishes/random`
- Get random dish in restaurant:
  - **GET** `{host}/dishes/restaurant/random?restaurantName={value}`
- Add dish to restaurant offer:
  - **POST** `{host}/dishes/restaurant?dishName={value}&restaurantName={value}&dishImg={value}&dishPrice={value}&dishDescription={value}`
- Update dish in restaurant offer:
  - **PATCH** `{host}/dishes/restaurant?dishName={value}&restaurantName={value}&dishImg={value}&dishPrice={value}&dishDescription={value}`
- Remove dish from restaurant offer:
  - **DELETE** `{host}/dishes/restaurant?dishName={value}&restaurantName={value}`

### 💬 Comments and ratings:
- Add comment to dish:
  - **POST** `{host}/opinions/comments?userEmail={value}&dishName={value}&restaurantName={value}&comment={value}`
- Add rating to dish:
  - **POST** `{host}/opinions/ratings?userEmail={value}&dishName={value}&restaurantName={value}&rating={value}`
- Update dish comment:
  - **PATCH** `{host}/opinions/comments?userEmail={value}&dishName={value}&restaurantName={value}&comment={value}`
- Update dish rating:
  - **PATCH** `{host}/opinions/ratings?userEmail={value}&dishName={value}&restaurantName={value}&rating={value}`

### 📓 QR:
- Generate data for QR code from customer cart:
  - **GET** `{host}/qr?userEmail={value}`
