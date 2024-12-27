
// export const host = process.env.REACT_APP_API_HOST;
export const host = "http://localhost:8080"

export const authApiEndpoints = {
  login: '/v1/user/auth/login',
  register: '/v1/user/auth/register',
};

export const userApiEndpoints = {
  user: '/api/v1/user',
  password: '/api/v1/user/password',
  profile: '/api/v1/user/update',
  self: '/api/v1/user/profile',
};

export const expenseApiEndpoints = {
  expense: '/v1/expense',
  summary: '/api/v1/expense/summary',
};

export const incomeApiEndpoints = {
  income: '/v1/income',
  summary: '/api/v1/income/summary',
};

export const budgetApiEndpoints = {
  budget: '/v1/budget',
}

export const reportApiEndpoints = {
  monthlyExpenseSummary: '/api/v1/report/expense/months/summary',
  monthlyIncomeSummary: '/api/v1/report/income/months/summary',
  transaction: '/api/v1/report/transaction',
};

export const chartApiEndpoints = {
  incomeExpenseCategories: '/api/v1/chart/income-expense/category',
  incomeExpenseMonthWise: '/api/v1/chart/income-expense/month-wise',
  incomeExpenseCategoryWise: '/api/v1/chart/income-expense/category-wise',
};
