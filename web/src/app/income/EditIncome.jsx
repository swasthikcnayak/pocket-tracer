import React, { useEffect, useState } from 'react';
import { Helmet } from 'react-helmet';
import { useForm, Controller } from 'react-hook-form';
import * as dayjs from 'dayjs';
import * as yup from 'yup';

import { Messages } from 'primereact/messages';
import { Card } from 'primereact/card';
import { Button } from 'primereact/button';
import { Calendar } from 'primereact/calendar';
import { Dropdown } from 'primereact/dropdown';
import { IncomeCategory } from './IncomeCategory';
import { incomeApiEndpoints } from './../../API';
import axios from './../../Axios';
import { useTracked } from './../../Store';
import { useLocation } from "react-router-dom";

let messages;

const editIncomeValidationSchema = yup.object().shape({
  date: yup.string().required('Income date field is required'),
  category: yup.string().required('Income category field is required'),
  title: yup.string().required('Spent on field is required').max(100, 'Spent on must be at most 100 characters'),
  amount: yup.number().required('Income amount field is required'),
  description: yup.string().max(200, 'description must be at most 200 characters'),
});

const EditIncome = (props) => {

  const [state, setState] = useTracked();
  const { register, handleSubmit, errors, setError, setValue, control } = useForm({
    validationSchema: editIncomeValidationSchema
  });
  const [submitting, setSubmitting] = useState(false);
  const [incomeCategories, setIncomeCategories] = useState([]);
  const location = useLocation();

  useEffect(() => {
    requestIncomeCategory();
    requestIncomeInfo();
  }, []);

  const requestIncomeCategory = async () => {
    setIncomeCategories(IncomeCategory);
  };

  const requestIncomeInfo = async () => {
    const income = location.state?.income;
    if(income){
      setValue([
        { id: income.id },
        { date: dayjs(income.date).toDate() },
        { category: income.category },
        { title: income.title },
        { amount: income.amount },
        { description: income.description },
      ]);
    }else{
      requestIncomeInfoFromServer()
    }
  }

  const requestIncomeInfoFromServer = async () => {
    await axios.get(incomeApiEndpoints.income + '/' + props.match.params.income_id, {})
      .then(response => {
        console.log(response);
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
          messages.show({
            severity: 'error',
            detail: 'Something went wrong. Try again.',
            sticky: true,
            closable: true,
            life: 5000
          });

      })
  };

  const submitUpdateIncome = (data) => {

    data.date = dayjs(data.date).format('YYYY-MM-DD HH:mm:ss');

    console.log(data);

    axios.patch(incomeApiEndpoints.income + '/' + props.match.params.income_id, JSON.stringify(data))
      .then(response => {

        if (response.status === 201) {
          setSubmitting(false);

          messages.show({
            severity: 'success',
            detail: 'Your income info updated successfully.',
            sticky: false,
            closable: false,
            life: 5000
          });
        }

      })
      .catch(error => {
        console.log('error', error.response);

        setSubmitting(false);

        messages.clear();

        if (error.response.status === 422) {
          let errors = Object.entries(error.response.data).map(([key, value]) => {
            return { name: key, message: value[0] }
          });
          setError(errors);
        }
        else if (error.response.status === 401) {
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
      <Helmet title="Edit Income" />

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
              <div className="p-card-title p-grid p-nogutter p-justify-between">Edit Income</div>
              <div className="p-card-subtitle">Edit selected income information below.</div>
            </div>
            <br />
            <form onSubmit={handleSubmit(submitUpdateIncome)}>
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
                <p className="text-error">{errors.income_date?.message}</p>
              </div>
              <div className="p-fluid">
                <label>Income Category</label>
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
                      options={incomeCategories}
                      style={{ width: '100%' }}
                      placeholder="Income Category"
                    />
                  }
                />
                <p className="text-error">{errors.category?.message}</p>
              </div>
              <div className="p-fluid">
                <label>Title</label>
                <input type="text" ref={register} name="title" className="p-inputtext p-component p-filled" />
                <p className="text-error">{errors.source?.message}</p>
              </div>
              <div className="p-fluid">
                <label>Amount</label>
                <div className="p-inputgroup">
                  <input type="text" ref={register} placeholder="Amount" name="amount" className="p-inputtext p-component p-filled" />
                </div>
                <p className="text-error">{errors.amount?.message}</p>
              </div>
              <div className="p-fluid">
                <label>Income Description</label>
                <textarea ref={register} rows={5} placeholder="" name="description" className="p-inputtext p-inputtextarea p-component p-inputtextarea-resizable" />
                <p className="text-error">{errors.notes?.message}</p>
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

export default React.memo(EditIncome);
