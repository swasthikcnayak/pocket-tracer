# API spec Documentation

##### Date : 23/12/2024

- #### User API
    - ##### Login : v1/user/login
    - ##### Logout : v1/user/logout
    - ##### Register : v1/user/register

- #### Income API
    - ##### post : v1/income/
    - #### get : v1/income/
    - ##### patch : v1/income/id
    - ##### get : v1/income/id
    get all the income
    can update previous income
    can get previous income
- #### Budget API
    - ##### post : v1/budget/
    - ##### get : v1/budget/?page=1&size=10
    - ##### patch : v1/budget/
    - ##### get : v1/budget/{mm-yyyy}
    Cannot update the older budget: can update only the ongoing budget
    Get all the budgets
    Can get any months budget
    Can create only current months budget
- #### Expense API
    - ##### post : /v1/expense
    - ##### get : /v1/ expense/?page=1&size=10
    - ##### patch : /v1/expense/{id}
    - ##### get : /v1/expense/{id}
    Can post current expense only
    can get all the expense
    Can update the the expense by the id
    can get the details of the expense by it's id

   
