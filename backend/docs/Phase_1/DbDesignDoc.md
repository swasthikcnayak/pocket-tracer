## Database Design

### Choosing the database : 

Using MySQL db : Because our data has a fixed structure. Working with structured data. And has relationships.

### Tables
- User
  - id
  - email
  - password
  - name
  
- Expense
  - id
  - user_id
  - Description
  - Amount
  - title
  - Category
  - Mode
  - time_of_expense
  - created_at
  
- Income
  - id
  - user_id
  - Description
  - Amount
  - Income Stream
  - title
  - time_of_income
  - created_at
  
- Budget
  - id
  - user_id
  - created_at
  - Category
  - Budget Amount
  - Description