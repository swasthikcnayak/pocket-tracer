import React, { useEffect, useState } from 'react';
import { Helmet } from 'react-helmet';
import { useForm, Controller } from 'react-hook-form';
import * as yup from 'yup';
import * as dayjs from 'dayjs';

import { Messages } from 'primereact/messages';
import { Card } from 'primereact/card';
import { Dropdown } from 'primereact/dropdown';
import { Calendar } from 'primereact/calendar';
import { Button } from 'primereact/button';
import { ProgressSpinner } from 'primereact/progressspinner';

import ExpenseListItem from './../expense/ExpenseListItem';
import IncomeListItem from './../income/IncomeListItem';

import { expenseApiEndpoints, incomeApiEndpoints, reportApiEndpoints } from './../../API';
import axios from './../../Axios';
import { useTracked } from './../../Store';
import { ExpenseCatgory } from '../expense/ExpenseCategory';

let messages;

const addExpenseValidationSchema = yup.object().shape({
  date: yup.string().required('Expense date field is required'),
  category: yup.string().required('Expense category field is required'),
  amount: yup.number().required('Expense amount field is required'),
  title: yup.string().required('Title field is required').max(100, 'Title must be at most 100 characters'),
  description: yup.string().max(200, 'Description must be at most 200 characters'),
});

const Dashboard = (props) => {

  const [state] = useTracked();
  const { register, handleSubmit, errors, setError, reset, control } = useForm({
    validationSchema: addExpenseValidationSchema
  });
  const [submitting, setSubmitting] = useState(false);
  const [recentExpense, setRecentExpense] = useState({ expense: [], expenseLoading: true });
  const [recentIncome, setRecentIncome] = useState({ income: [], incomeLoading: true });
  const [monthlyExpenseSummary, setMonthlyExpenseSummary] = useState({});
  const [monthlyIncomeSummary, setMonthlyIncomeSummary] = useState({});
  const [expenseCategories, setExpenseCategories] = useState([]);

  useEffect(() => {
    requestExpenseCategory();
    requestExpense();
    requestIncome();
    requestExpenseSummary();
    requestIncomeSummary();
  }, []);


  const requestExpenseCategory = async () => {
      setExpenseCategories(ExpenseCatgory);
  };

  const requestExpenseSummary = async () => {
    await axios.get(reportApiEndpoints.monthlyExpenseSummary, {})
      .then(response => {
        // console.log(response.data);
        setMonthlyExpenseSummary(response.data.data)
      })
      .catch(error => {
        console.log(error);
      });
  };

  const requestIncomeSummary = async () => {
    await axios.get(reportApiEndpoints.monthlyIncomeSummary, {})
      .then(response => {
        // console.log(response.data);
        setMonthlyIncomeSummary(response.data.data);
      })
      .catch(error => {
        console.log(error);
      });
  };

  const requestExpense = async () => {
    await axios.get(expenseApiEndpoints.expense + '?page=0&size=10', {})
      .then(response => {
        console.log(response.data);
        setRecentExpense({
          ...recentExpense,
          expense: response.data.content,
          expenseLoading: false
        });
      })
      .catch(error => {
        console.log('error', error);
        setRecentExpense({
          ...recentExpense,
          expenseLoading: false
        });
      });
  };

  const requestIncome = async () => {
    await axios.get(incomeApiEndpoints.income + '?page=0&size=10', {})
      .then(response => {
        // console.log(response.data);
        setRecentIncome({
          ...recentIncome,
          income: response.data.content,
          incomeLoading: false
        });
      })
      .catch(error => {
        console.log('error', error);
        setRecentIncome({
          ...recentIncome,
          incomeLoading: false
        });
      });
  };

  const submitExpense = (data) => {
    console.log("Expense submit" +data);
    data.date = dayjs(data.date).format('YYYY-MM-DD HH:mm:ss');
    console.log("Reques data", data)
    axios.post(expenseApiEndpoints.expense, JSON.stringify(data))
      .then(response => {
        console.log("Resule response ", response);
        if (response.status === 201) {
          reset();
          setSubmitting(false);
          requestExpense();
          requestExpenseSummary();

          messages.show({
            severity: 'success',
            detail: 'Your expense is added.',
            sticky: false,
            closable: false,
            life: 5000
          });
        }
      })
      .catch(error => {
        console.log('error', error);

        if (error.response.status === 401) {
          messages.clear();
          messages.show({
            severity: 'error',
            detail: 'Something went wrong. Try again.',
            sticky: true,
            closable: true,
            life: 5000
          });
        }
        else if (error.response.status === 422) {
          let errors = Object.entries(error.response.data).map(([key, value]) => {
            return { name: key, message: value[0] }
          });
          setError(errors);
        }

        setSubmitting(false)
      })
  };

  const renderRecentExpense = () => {
    if (recentExpense.expenseLoading) {
      return (
        <div className="p-grid p-nogutter p-justify-center">
          <ProgressSpinner style={{ height: '25px' }} strokeWidth={'4'} />
        </div>
      );
    }
    else {
      if (recentExpense.expense.length > 0) {
        return recentExpense.expense.map((item, index) => {
          return <ExpenseListItem key={item.id} itemDetail={item} />;
        })
      }
      else {
        return (
          <div className="p-grid p-nogutter p-justify-center">
            <h4 className="color-subtitle">Spend some cash to see recent.</h4>
          </div>
        );
      }
    }
  };

  const renderRecentIncome = () => {
    if (recentIncome.incomeLoading) {
      return (
        <div className="p-grid p-nogutter p-justify-center">
          <ProgressSpinner style={{ height: '25px' }} strokeWidth={'4'} />
        </div>
      );
    }
    else {
      if (recentIncome.income.length > 0) {
        return recentIncome.income.map((item, index) => {
          return <IncomeListItem key={item.id} itemDetail={item} />;
        })
      }
      else {
        return (
          <div className="p-grid p-nogutter p-justify-center">
            <h4 className="color-subtitle">Add some earnings to see recent.</h4>
          </div>
        );
      }
    }
  };

  const renderSummary = (data) => {
    if (data && data.length > 0) {
      return data.map((item, index) => {
        return <div key={index}>
          <div className="color-link text-center">{item.total.toLocaleString()} </div>
          <hr />
        </div>
      })
    }
    else if (typeof data === "object" && Object.values(data).length > 0) {
      return Object.values(data).map((item, index) => {
        return <div key={index}>
          <div className="color-link text-center">{item.total.toLocaleString()} </div>
          <hr />
        </div>
      })
    }
    else {
      return <div>
        <div className="text-center">No transaction data found.</div>
        <hr />
      </div>
    }
  };

  return (
    <div>
      <Helmet title="Dashboard" />

      <div className="p-grid p-nogutter">
        <div className="p-col-12">
          <div className="p-fluid">
            <Messages ref={(el) => messages = el} />
          </div>
        </div>
      </div>

      <div className="p-grid">
        <div className="p-col-12">
          <div className="p-fluid">

            <div className="p-grid">
              <div className="p-col-6 p-md-3">
                <div className="p-panel p-component">
                  <div className="p-panel-titlebar"><span className="color-title text-bold">Expense Last Month</span>
                  </div>
                  <div className="p-panel-content-wrapper p-panel-content-wrapper-expanded" id="pr_id_1_content"
                    aria-labelledby="pr_id_1_label" aria-hidden="false">
                    <div className="p-panel-content">
                      {renderSummary(monthlyExpenseSummary.expense_last_month)}
                    </div>
                  </div>
                </div>
              </div>

              <div className="p-col-6 p-md-3">
                <div className="p-panel p-component">
                  <div className="p-panel-titlebar"><span className="color-title text-bold">Expense This Month</span></div>
                  <div className="p-panel-content-wrapper p-panel-content-wrapper-expanded" id="pr_id_1_content"
                    aria-labelledby="pr_id_1_label" aria-hidden="false">
                    <div className="p-panel-content">
                      {renderSummary(monthlyExpenseSummary.expense_this_month)}
                    </div>
                  </div>
                </div>
              </div>

              <div className="p-col-6 p-md-3">
                <div className="p-panel p-component">
                  <div className="p-panel-titlebar"><span className="color-title text-bold">Income Last Month</span>
                  </div>
                  <div className="p-panel-content-wrapper p-panel-content-wrapper-expanded" id="pr_id_1_content"
                    aria-labelledby="pr_id_1_label" aria-hidden="false">
                    <div className="p-panel-content">
                      {renderSummary(monthlyIncomeSummary.income_last_month)}
                    </div>
                  </div>
                </div>
              </div>

              <div className="p-col-6 p-md-3">
                <div className="p-panel p-component">
                  <div className="p-panel-titlebar"><span className="color-title text-bold">Income This Month</span></div>
                  <div className="p-panel-content-wrapper p-panel-content-wrapper-expanded" id="pr_id_1_content"
                    aria-labelledby="pr_id_1_label" aria-hidden="false">
                    <div className="p-panel-content">
                      {renderSummary(monthlyIncomeSummary.income_this_month)}
                    </div>
                  </div>
                </div>
              </div>
            </div>

          </div>
        </div>
      </div>

      <div className="p-grid">

        <div className="p-col-12 p-md-6 p-lg-4">
          <Card className="rounded-border">
            <div>
              <div className="p-card-title p-grid p-nogutter p-justify-between">Expense Info</div>
              <div className="p-card-subtitle">Enter your expense information below.</div>
            </div>
            <br />
            <form onSubmit={handleSubmit(submitExpense)}>
              <div className="p-fluid">
                <Controller
                  name="date"
                  defaultValue={new Date()}
                  onChange={([e]) => {
                    return e.value;
                  }}
                  control={control}
                  as={
                    <Calendar
                      dateFormat="yy-mm-dd"
                      showTime={true}
                      hourFormat="12"
                      showButtonBar={true}
                      maxDate={new Date()}
                      touchUI={window.innerWidth < 768}
                    />
                  }
                />
                <p className="text-error">{errors.date?.message}</p>
              </div>
              <div className="p-fluid">
                <Controller
                  name="category"
                  onChange={([e]) => {
                    return e.value
                  }}
                  control={control}
                  as={
                    <Dropdown
                      filter={true}
                      filterPlaceholder="Search here"
                      showClear={true}
                      filterInputAutoFocus={false}
                      options={expenseCategories}
                      style={{ width: '100%' }}
                      placeholder="Expense Category"
                    />
                  }
                />
                <p className="text-error">{errors.category?.message}</p>
              </div>
              <div className="p-fluid">
                <input type="text" ref={register} placeholder="Title" name="title" className="p-inputtext p-component p-filled" />
                <p className="text-error">{errors.title?.message}</p>
              </div>
              <div className="p-fluid">
                <div className="p-inputgroup">
                  <input type="number" step="0.01" ref={register} keyfilter="money" placeholder="Amount" name="amount" className="p-inputtext p-component p-filled" />
                </div>
                <p className="text-error">{errors.amount?.message}</p>
              </div>
              <div className="p-fluid">
                <textarea ref={register} rows={5} placeholder="Description" name="description" className="p-inputtext p-inputtextarea p-component p-inputtextarea-resizable" />
                <p className="text-error">{errors.description?.message}</p>
              </div>
              <div className="p-fluid">
                <Button disabled={submitting} type="submit" label="Add Expense" icon="pi pi-plus"
                  className="p-button-raised" />
              </div>
            </form>
          </Card>
        </div>

        <div className="p-col-12 p-md-6 p-lg-4">
          <Card className="rounded-border">
            <div>
              <div className="p-card-title p-grid p-nogutter p-justify-between">Recent Expenses -</div>
              <div className="p-card-subtitle">Here are few expenses you've made.</div>
            </div>
            <br />
            <div>
              {renderRecentExpense()}
            </div>
          </Card>
        </div>

        <div className="p-col-12 p-md-6 p-lg-4">
          <Card className="rounded-border">
            <div>
              <div className="p-card-title p-grid p-nogutter p-justify-between">Recent Incomes +</div>
              <div className="p-card-subtitle">Here are few incomes you've added.</div>
            </div>
            <br />
            <div>
              {renderRecentIncome()}
            </div>
          </Card>
        </div>
      </div>
    </div>
  )
}

export default React.memo(Dashboard);
