import React, { useEffect, useState } from 'react';
import { Helmet } from 'react-helmet';
import { useForm, Controller } from 'react-hook-form';
import * as dayjs from 'dayjs';
import * as yup from 'yup';
import { ExpenseCatgory } from './ExpenseCategory';
import { Messages } from 'primereact/messages';
import { Card } from 'primereact/card';
import { Button } from 'primereact/button';
import { Calendar } from 'primereact/calendar';
import { Dropdown } from 'primereact/dropdown';
import { useLocation } from "react-router-dom";

import { expenseApiEndpoints } from './../../API';
import axios from './../../Axios';
import { useTracked } from './../../Store';

let messages;

const editExpenseValidationSchema = yup.object().shape({
  date: yup.string().required('Expense date field is required'),
  category: yup.string().required('Expense category field is required'),
  amount: yup.number().required('Expense amount field is required'),
  title: yup.string().required('Spent on field is required').max(100, 'Spent on must be at most 100 characters'),
  description: yup.string().max(200, 'description must be at most 200 characters'),
});

const EditExpense = (props) => {

  const [state, setState] = useTracked();
  const { register, handleSubmit, errors, setError, setValue, control } = useForm({
    validationSchema: editExpenseValidationSchema
  });
  const [submitting, setSubmitting] = useState(false);
  const [expenseCategories, setExpenseCategories] = useState([]);
  const location = useLocation();

  useEffect(() => {
    requestExpenseCategory();
    requestExpenseInfo();
  }, []);

  const requestExpenseCategory = async () => {
    setExpenseCategories(ExpenseCatgory);
  };

  const requestExpenseInfo = async () => {
    const expense = location.state?.expense;
    if(expense){
        setValue([
          { id: expense.id },
          { date: dayjs(expense.date).toDate() },
          { category: expense.category },
          { amount: expense.amount },
          { title:expense.title },
          { description: expense.description },
        ]);
      }else{
        requestExpenseInfoFromServer()
      }
  };

  const requestExpenseInfoFromServer = async () => {
    await axios.get(expenseApiEndpoints.expense + '/' + props.match.params.expense_id, {})
      .then(response => {
        setValue([
          { id: response.data.id },
          { date: dayjs(response.data.date).toDate() },
          { category: response.data.category },
          { title: response.data.title },
          { amount: response.data.amount },
          { description: response.data.description },
        ]);
      })
      .catch(error => {
        console.log('error', error.response);

        if (error.response.status === 401) {
          messages.show({
            severity: 'error',
            detail: 'Something went wrong. Try again.',
            sticky: true,
            closable: true,
            life: 5000
          });
        }

      })
  }

  const submitUpdateExpense = (data) => {

    data.date = dayjs(data.date).format('YYYY-MM-DD HH:mm:ss');

    axios.patch(expenseApiEndpoints.expense + '/' + props.match.params.expense_id, JSON.stringify(data))
      .then(response => {
        if (response.status === 201) {
          setSubmitting(false);

          messages.show({
            severity: 'success',
            detail: 'Your expense info updated successfully.',
            sticky: false,
            closable: false,
            life: 5000
          });
        }

      })
      .catch(error => {
        console.log('error');
        console.log(error.response);

        setSubmitting(false);

        messages.clear();

        if (error.response.status === 422) {
          let errors = Object.entries(error.response.data).map(([key, value]) => {
            return { name: key, message: value[0] }
          });
          setError(errors);
        }
        else{
          messages.show({
            severity: 'error',
            detail: 'Something went wrong. Try again.',
            sticky: true,
            closable: true,
            life: 5000
          });
        }

      })
  };

  return (
    <div>
      <Helmet title="Edit Expense" />
      <div className="p-grid p-nogutter">
        <div className="p-col-12">
          <div className="p-fluid">
            <Messages ref={(el) => messages = el} />
          </div>
        </div>
      </div>

      <div className="p-grid">

        <div className="p-col-12">
          <Card className="rounded-border">
            <div>
              <div className="p-card-title p-grid p-nogutter p-justify-between">Edit Expense</div>
              <div className="p-card-subtitle">Edit selected expense information below.</div>
            </div>
            <br />
            <form onSubmit={handleSubmit(submitUpdateExpense)}>
              <div className="p-fluid">
                <label>Date</label>
                <Controller
                  name="date"
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
                      touchUI={window.innerWidth < 768}
                    />
                  }
                />
                <p className="text-error">{errors.date?.message}</p>
              </div>
              <div className="p-fluid">
                <label>Expense Category</label>
                <Controller
                  name="category"
                  onChange={([e]) => {
                    return e.value;
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
                <label>Amount</label>
                <div className="p-inputgroup">
                  <input type="text" ref={register} placeholder="Amount" name="amount" className="p-inputtext p-component p-filled" />
                </div>
                <p className="text-error">{errors.amount?.message}</p>
              </div>
              <div className="p-fluid">
                <label>Title </label>
                <input type="text" ref={register} name="title" className="p-inputtext p-component p-filled" />
                <p className="text-error">{errors.title?.message}</p>
              </div>
              <div className="p-fluid">
                <label>Description </label>
                <textarea ref={register} rows={5} placeholder="" name="description" className="p-inputtext p-inputtextarea p-component p-inputtextarea-resizable" />
                <p className="text-error">{errors.description?.message}</p>
              </div>
              <div className="p-fluid">
                <Button disabled={submitting} type="submit" label="Save Changes" icon="pi pi-save"
                  className="p-button-raised" />
              </div>
            </form>
          </Card>
        </div>

      </div>
    </div>

  )
}

export default React.memo(EditExpense);
