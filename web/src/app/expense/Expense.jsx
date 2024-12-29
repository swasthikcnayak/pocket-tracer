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
import { expenseApiEndpoints } from './../../API';
import { useTracked } from './../../Store';
import { ExpenseCatgory } from './ExpenseCategory';


const StyledSwal = Swal.mixin({
  customClass: {
    container: 'container-class',
    popup: 'popup-class',
    header: 'header-class',
    title: 'p-card-title',
    content: 'content-class',
    closeButton: 'close-button-class',
    image: 'image-class',
    input: 'input-class',
    actions: 'actions-class',
    confirmButton: 'p-button p-button-raised p-button-danger p-button-text-icon-left',
    cancelButton: 'p-button p-button-raised p-button-info p-button-text-icon-left',
    footer: 'footer-class'
  },
  buttonsStyling: false
});

let messages;

const addExpenseValidationSchema = yup.object().shape({
  date: yup.string().required('Expense date field is required'),
  category: yup.string().required('Expense category field is required'),
  amount: yup.string().required('Expense amount field is required'),
  title: yup.string().required('Spent on field is required').max(100, 'Spent on must be at most 100 characters'),
  description: yup.string().max(200, 'description must be at most 200 characters'),
});

const Expense = (props) => {

  const { register, handleSubmit, setValue, errors, setError, reset, control } = useForm({
    validationSchema: addExpenseValidationSchema
  });
  const [datatable, setDatatable] = useState({
    sortField: 'date',
    sortOrder: 0, //desccending
    rowsPerPage: 10,
    currentPage: 1
  });
  const [prevMonthExpenseSummary, setPrevMonthExpenseSummary] = useState({});
  const [currentMonthExpenseSummary, setCurrentMonthExpenseSummary] = useState({});
  const [submitting, setSubmitting] = useState(false);
  const [expenseCategories, setExpenseCategories] = useState([]);
  const [expense, setExpense] = useState({ expenses: [], total: 0, fetching: true });

  useEffect(() => {
    requestExpenseSummary();
    requestExpenseCategory();
  }, []);

  useEffect(() => {
    requestExpense();
  }, [datatable]);

  const requestExpenseCategory = async () => {
    setExpenseCategories(ExpenseCatgory);
  };

  const requestExpense = async () => {
    setExpense({ ...expense, fetching: true });
    await axios.get(expenseApiEndpoints.expense + '?page=' + datatable.currentPage + '&size=' + datatable.rowsPerPage + '&sort=' + datatable.sortField + '&order=' + (datatable.sortOrder > 0 ? 'asc' : 'desc'), {})
      .then(response => {
        if (response.data) {
          setExpense({
            total: response.data.totalElements,
            expenses: response.data.content,
            fetching: false
          });
        }
        else {
          setExpense({
            ...expense,
            fetching: false
          });
        }
      })
      .catch(error => {
        console.log(error);
      });
  };

  const requestExpenseSummary = async () => {
    const now = new Date();
    const previousMonth = now.getMonth() - 1;
    const year = previousMonth < 0 ? now.getFullYear() - 1 : now.getFullYear();
    const month = (previousMonth + 12) % 12;
    await axios.get(expenseApiEndpoints.analytics + `/${month + 1}/${year}`, {})
      .then(response => {
        setPrevMonthExpenseSummary({ expense: response.data });
      })
      .catch(error => {
        console.log(error);
      });

    await axios.get(expenseApiEndpoints.analytics + `/${now.getMonth() + 1}/${now.getFullYear()}`, {})
      .then(response => {
        setCurrentMonthExpenseSummary({ expense: response.data });
      })
      .catch(error => {
        console.log(error);
      });
  };

  const deleteExpense = (data) => {
    StyledSwal.fire({
      title: 'Are you sure?',
      text: `Confirm to delete`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: '<span class="pi pi-trash p-button-icon-left"></span><span class="p-button-text">Delete</span>',
      cancelButtonText: '<span class="pi pi-ban p-button-icon-left"></span><span class="p-button-text">No</span>',
      // confirmButtonColor: '#f76452',
      // cancelButtonColor: '#3085d6',
      focusConfirm: false,
      focusCancel: true
    })
      .then((result) => {
        if (result.value) {
          axios.delete(expenseApiEndpoints.expense + '/' + data.id, {})
            .then(response => {
              if (response.status === 202) {
                requestExpense();
                requestExpenseSummary();
                messages.show({
                  severity: 'success',
                  detail: 'Your expense  deleted successfully.',
                  sticky: false,
                  closable: false,
                  life: 5000
                });
              }

            })
            .catch(error => {
              console.log('error', error.response);
              messages.clear();
              messages.show({
                severity: 'error',
                detail: 'Something went wrong. Try again.',
                sticky: true,
                closable: true,
                life: 5000
              });

            });
        }
      });
  };

  const submitExpense = (data) => {
    setSubmitting(true);
    console.log("Expense submit" + data);
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

        if (error.response.status === 400) {
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

  const renderExpenseSummary = (data) => {
    if (data && data.length > 0) {
      return data.map((item, index) => {
        return <div key={index}>
          <div className="color-link text-center">{item.category}:{item.totalAmount.toLocaleString()} </div>
          <hr />
        </div>
      })
    }
    else {
      return <div>
        <div className="text-center">No Expense data found.</div>
        <hr />
      </div>
    }
  };

  return (
    <div>
      <Helmet title="Expense" />
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
              <div className="p-col-6">
                <div className="p-panel p-component">
                  <div className="p-panel-titlebar"><span className="color-title text-bold">Expense Last Month</span>
                  </div>
                  <div className="p-panel-content-wrapper p-panel-content-wrapper-expanded" id="pr_id_1_content"
                    aria-labelledby="pr_id_1_label" aria-hidden="false">
                    <div className="p-panel-content">
                      {renderExpenseSummary(prevMonthExpenseSummary.expense)}
                    </div>
                  </div>
                </div>
              </div>

              <div className="p-col-6">
                <div className="p-panel p-component">
                  <div className="p-panel-titlebar"><span className="color-title text-bold">Expense Current Month</span></div>
                  <div className="p-panel-content-wrapper p-panel-content-wrapper-expanded" id="pr_id_1_content"
                    aria-labelledby="pr_id_1_label" aria-hidden="false">
                    <div className="p-panel-content">
                      {renderExpenseSummary(currentMonthExpenseSummary.expense)}
                    </div>
                  </div>
                </div>
              </div>
            </div>

          </div>
        </div>
      </div>

      <div className="p-grid">

        <div className="p-col-12 p-md-6">
          <Card className="rounded-border">
            <div>
              <div className="p-card-title p-grid p-nogutter p-justify-between">Add Expense</div>
              <div className="p-card-subtitle">Add your expense information below.</div>
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

        <div className="p-col-12 p-md-6">
          <Card className="rounded-border">
            <div className='p-grid'>
              <div className='p-col-6'>
                <div className="p-card-title p-grid p-nogutter p-justify-between">View Expenses</div>
                <div className="p-card-subtitle">Here are few expenses you've added.</div>
              </div>
              <div className="p-col-6" align="right">
                {expense.fetching ? <ProgressSpinner style={{ height: '25px', width: '25px' }} strokeWidth={'4'} /> : ''}
              </div>
            </div>
            <br />
            <DataTable
              value={expense.expenses}
              sortField={datatable.sortField}
              sortOrder={datatable.sortOrder}
              responsive={true}
              paginator={true}
              rows={datatable.rowsPerPage}
              rowsPerPageOptions={[5, 10, 20]}
              totalRecords={expense.total}
              lazy={true}
              first={(datatable.currentPage - 1) * datatable.rowsPerPage}
              onPage={(e) => {
                console.log(e);
                setDatatable({
                  ...datatable,
                  currentPage: (e.page + 1),
                  rowsPerPage: e.rows,
                });
              }}
              onSort={e => {
                // console.log(e);
                setDatatable({
                  ...datatable,
                  sortField: e.sortField,
                  sortOrder: e.sortOrder,
                });
              }}
              className="text-center"
            >
              {/* <Column field="id" header="Serial" sortable={true} /> */}
              <Column field="title" header="Title" sortable={true} />
              <Column field="category" header="Category" sortable={true} />
              <Column field="amount" header="Amount" sortable={true}
                body={(rowData, column) => {
                  return rowData.amount.toLocaleString()
                }}
              />
              <Column field="date" header="Date" sortable={true}
                body={(rowData, column) => {
                  return dayjs(rowData.date).format('YYYY-MM-DD hh:mm a')
                }}
              />
              <Column
                body={(rowData, column) => {
                  return (
                    <div>
                      <Link to={{ pathname: `/expense/${rowData.id}/edit`, state: { expense: rowData } }} >
                        <Button label="Edit" value={rowData.id}
                          icon="pi pi-pencil"
                          className="p-button-raised p-button-rounded p-button-info" />
                      </Link>
                      <Button label="Delete"
                        onClick={() => deleteExpense(rowData)}
                        icon="pi pi-trash"
                        className="p-button-raised p-button-rounded p-button-danger" />
                    </div>
                  )
                }}
                header="Action"
                style={{ textAlign: 'center', width: '8em' }}
              />
            </DataTable>
          </Card>
        </div>

      </div>
    </div>

  )
}

export default Expense;
