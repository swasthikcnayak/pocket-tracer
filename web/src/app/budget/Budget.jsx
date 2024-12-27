import React, { useState, useEffect } from 'react';
import { Helmet } from 'react-helmet';
import * as yup from 'yup';
import * as dayjs from 'dayjs';
import Swal from 'sweetalert2';
import { Link } from 'react-router-dom';
import { useForm, Controller } from 'react-hook-form';

import { Messages } from 'primereact/messages';
import { Card } from 'primereact/card';
import { Button } from 'primereact/button';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { ProgressSpinner } from 'primereact/progressspinner';
import { Dropdown } from 'primereact/dropdown';
import { Calendar } from 'primereact/calendar';
import axios from './../../Axios';
import { BudgetCatgory } from './BudgetCatgory';
import { budgetApiEndpoints } from './../../API';

let messages;

const budgetValidationSchema = yup.object().shape({
  category: yup.string().required('category field is required'),
  amount: yup.number().typeError('amount must be a number').required('amount field is required'),
});

const budgetValidationSchemaForDate = yup.object().shape({
  date: yup.string().required('date field is required'),
});


const Budget = (props) => {

  const { register, handleSubmit, setValue, errors, setError, reset, control, watch } = useForm({
    validationSchema: budgetValidationSchemaForDate
  });
  const [budgetCategoryList, setBudgetCategoryList] = useState([]);
  const [submitting, setSubmitting] = useState(false);
  const [categories, setCategories] = useState([]);
  const [budget, setBudget] = useState({ id: -1, budgetEntries: [], total: 0, totalAmount: 0, fetching: true, month: 0, year: 0 });

  useEffect(() => {
    requestbudgetCategory();
    const currentDate = new Date();
    const month = currentDate.getMonth() + 1; // Months are zero-indexed, so add 1
    const year = currentDate.getFullYear();
    requestCurrentBudget(month, year);
  }, []);

  const requestCurrentBudget = async (month, year) => {
    setSubmitting(true);
    axios.get(budgetApiEndpoints.budget + `?month=${month}&year=${year}`)
      .then(response => {
        console.log("Result response ", response);
        if (response.status === 200) {
          setBudget({
            month: response.data.month,
            year: response.data.year,
            id: response.data.id,
            total: response.data.budgetEntries.length,
            budgetEntries: response.data.budgetEntries,
            totalAmount: response.data.budgetEntries.reduce((sum, entry) => sum + entry.amount, 0)
          })
          setSubmitting(false);
        }
      })
      .catch(error => {
        messages.show({
          severity: 'error',
          detail: 'Budget not found for the selected month',
          sticky: false,
          closable: true,
          life: 5000
        });
        setBudget({
          month: month,
          year: year,
          id: -1,
          total: 0,
          budgetEntries: [],
          totalAmount: 0
        })
        setSubmitting(false);
      });
  }

  const requestbudgetCategory = async () => {
    setBudgetCategoryList(BudgetCatgory);
  };

  const selectCategory = () => {
    const selectedCategory = watch("category");
    let selectedValue = watch("amount");
    budgetValidationSchema.validate({ category: selectedCategory, amount: selectedValue }, { abortEarly: false })
      .then(() => {
        selectedValue = Math.round(selectedValue * 100) / 100
        const updatedCategories = budgetCategoryList.filter(category => {
          return category !== selectedCategory;
        });
        setBudgetCategoryList(updatedCategories);
        setCategories([...categories, { category: selectedCategory, value: selectedValue }]);
        setValue("amount", "");
        setValue("category", "");
      })
      .catch((err) => {
        messages.show({
          severity: 'error',
          detail: 'Selected category or amount is invalid',
          sticky: false,
          closable: false,
          life: 5000
        });
      });
  };

  const removeSelectedCategory = (index) => {
    const currentCategory = categories[index].category;
    const updatedCategories = categories.filter((_, i) => i !== index);
    setCategories(updatedCategories);
    const newBudgetCategoryList = [...budgetCategoryList, currentCategory]
    setBudgetCategoryList(newBudgetCategoryList);
  };

  const submitBudget = (data) => {
    const payload = {}
    const date = new Date(data.date);
    payload.month = date.getMonth() + 1;
    payload.year = date.getFullYear();
    payload.budgetEntries = categories.map((cat) => ({
      category: cat.category,
      amount: parseFloat(cat.value)
    }));
    if (payload.budgetEntries.length === 0) {
      messages.show({
        severity: 'error',
        detail: 'Select the categories for budget',
        sticky: false,
        closable: false,
        life: 5000
      });
      return;
    }
    setSubmitting(true);
    axios.post(budgetApiEndpoints.budget, JSON.stringify(payload))
      .then(response => {
        if (response.status === 201) {
          setSubmitting(false);
          requestbudgetCategory();
          setCategories([]);
          messages.show({
            severity: 'success',
            detail: 'Your budget is created.',
            sticky: false,
            closable: false,
            life: 5000
          });
        }
      })
      .catch(error => {

        if (error.response.status === 400) {
          messages.clear();
          messages.show({
            severity: 'error',
            detail: 'Something went wrong. You can create only one budget for a month',
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

  return (
    <div>
      <Helmet title="Budget" />
      <div className="p-grid p-nogutter">
        <div className="p-col-12">
          <div className="p-fluid">
            <Messages ref={(el) => messages = el} />
          </div>
        </div>
      </div>
      <div className="p-grid">
        <div className="p-col-12 p-md-6">
          <Card className="rounded-border">
            <div>
              <div className="p-card-title p-grid p-nogutter p-justify-between">Prepare you budget</div>
              <div className="p-card-subtitle">Add your budget for selected month for the categories.</div>
            </div>
            <br />
            <form onSubmit={handleSubmit(submitBudget)}>
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
                      dateFormat="yy-mm"
                      showTime={false}
                      showButtonBar={true}
                      required={true}
                      touchUI={window.innerWidth < 768}
                    />
                  }
                />
                <p className="text-error">{errors.date?.message}</p>
              </div>

              {categories.map((cat, index) => (
                <div className='p-grid'>
                  <span className='p-col-3'>
                    {cat.category || " CATEGORY "}
                  </span>
                  <span className='p-col-3'>
                    {cat.value || " VALUE "}
                  </span>
                  <Button type="button" label="Delete" icon="pi pi-minus" onClick={() => removeSelectedCategory(index)}
                    className="p-button" />
                </div>
              ))}

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
                      options={budgetCategoryList}
                      style={{ width: '100%' }}
                      placeholder="Category"
                    />
                  }
                />
                <p className="text-error">{errors.category?.message}</p>
              </div>
              <div className="p-fluid">
                <div className="p-inputgroup">
                  <input type="number" ref={register} keyfilter="money" placeholder="Amount" name="amount" className="p-inputtext p-component p-filled" />
                </div>
                <p className="text-error">{errors.amount?.message}</p>
              </div>

              <Button type="button" label="Add category" disabled={submitting} icon="pi pi-plus" size="small"
                className="p-button-raised " onClick={selectCategory} />

              <Button disabled={submitting} type="submit" label="Make Budget" icon="pi pi-check"
                className="p-button-raised p-button-success" />
            </form>
          </Card>
        </div>

        <div className="p-col-12 p-md-6">
          <Card className="rounded-border ">
            <div className='p-grid'>
              <div className='p-col-6'>
                <div className="p-card-title p-grid p-nogutter p-justify-between">View Budget</div>
                <div className="p-card-subtitle">Here is the budget for ... </div>
              </div>
              <div className="p-col-6" align="right">
                {budget.fetching ? <ProgressSpinner style={{ height: '25px', width: '25px' }} strokeWidth={'4'} /> : ''}
              </div>
            </div>
            <br />
            <Controller
              name="opDate"
              defaultValue={new Date()}
              onChange={([e]) => {
                const date = e.value;
                if (date) {
                  const month = date.getMonth() + 1;
                  const year = date.getFullYear();
                  requestCurrentBudget(month, year);
                }
                return e.value;
              }}
              control={control}
              as={
                <Calendar
                  dateFormat="yy-mm"
                  showTime={false}
                  showButtonBar={true}
                  required={true}
                  touchUI={window.innerWidth < 768}
                />
              }
            />
          </Card>
          <br />
          {  (budget.id >= 0)  && 
          <Card className="rounded-border">
            <div className='p-grid'>
              <div className='p-col-9'>
                <div className="p-card-title p-grid p-nogutter p-justify-between">{budget.month}/{budget.year}</div>
                <div className="p-card-subtitle">Budgeted Amount : {budget.totalAmount}</div>
             
              </div>
              <div className='p-col-3'>
                <Link to={{ pathname: `/budget/${budget.id}/edit`, state: { budget: budget } }} >
                  <Button label="Edit" value={budget.id}
                    icon="pi pi-pencil"
                    className=" p-button-rounded p-button-info" />
                </Link>
              </div>
              <div className="p-col-2" align="right">
                {budget.fetching ? <ProgressSpinner style={{ height: '25px', width: '25px' }} strokeWidth={'4'} /> : ''}
              </div>
            </div>
            <br />
            <DataTable
              value={budget.budgetEntries}
              responsive={true}
              paginator={false}
              rows={budget.budgetEntries.length}
              totalRecords={budget.budgetEntries.length}
              lazy={true}
              className="text-center"
            >

              <Column field="category" header="Category" />
              <Column field="amount" header="Amount" />

            </DataTable>

          </Card>
        }
        </div>
      </div>
    </div>
  );
}

export default Budget;
