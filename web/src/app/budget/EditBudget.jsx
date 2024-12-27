import React, { useEffect, useState } from 'react';
import { Helmet } from 'react-helmet';
import { useForm, Controller } from 'react-hook-form';
import * as dayjs from 'dayjs';
import * as yup from 'yup';
import { BudgetCatgory } from './BudgetCatgory';
import { Messages } from 'primereact/messages';
import { Card } from 'primereact/card';
import { Button } from 'primereact/button';
import { Calendar } from 'primereact/calendar';
import { Dropdown } from 'primereact/dropdown';
import { useLocation } from "react-router-dom";

import { budgetApiEndpoints } from './../../API';
import axios from './../../Axios';

let messages;

const budgetValidationSchema = yup.object().shape({
  category: yup.string().required('category field is required'),
  amount: yup.number().typeError('amount must be a number').required('amount field is required'),
});

const budgetValidationSchemaForDate = yup.object().shape({
  date: yup.string().required('date field is required'),
});


const EditBudget = (props) => {

  const { register, handleSubmit, setValue, errors, setError, reset, control, watch } = useForm({
    validationSchema: budgetValidationSchemaForDate
  });

  const [budgetCategoryList, setBudgetCategoryList] = useState([]);
  const [submitting, setSubmitting] = useState(false);
  const [categories, setCategories] = useState([]);

  const location = useLocation();

  useEffect(() => {
    initializeData()
  }, []);

  const initializeData = async () => {
    requestBudgetInfo();
  }

  const requestbudgetCategory = async (budgetEntries) => {
    const selectedBudgetCategoryList = budgetEntries.map((entry) => entry.category);
    const newbudgetCategoryList = BudgetCatgory.filter(category => !selectedBudgetCategoryList.includes(category));
    setBudgetCategoryList(newbudgetCategoryList);
  };

  const requestBudgetInfo = async () => {
    const budget = location.state?.budget;
    if (budget) {
      const updatedCategories = budget.budgetEntries.map(entry => ({
        category: entry.category,
        value: entry.amount
      }));
      setCategories(updatedCategories);
      setValue("date", new Date(budget.year, budget.month-1))
      requestbudgetCategory(budget.budgetEntries);
    } else {
      requestBudgetInfoFromServer()
    }
  };

  const requestBudgetInfoFromServer = async () => {
    await axios.get(budgetApiEndpoints.budget + '/' + props.match.params.budget_id, {})
      .then(response => {
        const updatedCategories = response.data.budgetEntries.map(entry => ({
          category: entry.category,
          value: entry.amount
        }));
        setCategories(updatedCategories);
        setValue("date", new Date(response.data.year, response.data.month-1))
        requestbudgetCategory(response.data.budgetEntries);
      })
      .catch(error => {
        console.log('error', error.response);
        messages.show({
          severity: 'error',
          detail: 'Something went wrong. Try again.',
          sticky: true,
          closable: true,
          life: 5000
        });
      })
  }

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
    axios.patch(budgetApiEndpoints.budget+`/${props.match.params.budget_id}`, JSON.stringify(payload))
      .then(response => {
        if (response.status === 201) {
          setSubmitting(false);
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
      <Helmet title="Edit Budget" />
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
              <div className="p-card-title p-grid p-nogutter p-justify-between">Edit Budget</div>
              <div className="p-card-subtitle">Edit selected expense information below.</div>
            </div>
            <br />
            <form onSubmit={handleSubmit(submitBudget)}>
              <div className="p-fluid">
                <Controller
                  name="date"
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
                      disabled={true}
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

      </div>
    </div>

  )
}

export default React.memo(EditBudget);
