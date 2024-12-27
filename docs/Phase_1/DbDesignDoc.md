## Database Design

##### Date : 23/12/2024

### Choosing the database : 

Using MySQL db : Because our data has a fixed structure. Working with structured data. And has relationships.

### Tables
- User
  - id
  - email
  - password
  
- Expense
  - id
  - user_id
  - Description
  - Amount
  - title
  - Category
  - datetime
  
- Income
  - id
  - user_id
  - Description
  - Amount
  - Category
  - title
  - datetime
  
- Budget
  - id
  - user_id
  - month
  - year
  - List of Budget_Entries

- Budget_Entries
  - budget_id
  - category
  - amount