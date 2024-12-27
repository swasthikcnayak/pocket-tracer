import React from 'react';
import { Card } from 'primereact/card';
import dayjs from 'dayjs';

const ExpenseListItem = (props) => {
  const itemDetail = props.itemDetail;

  return (
    <Card>
      <div>
        <div className="" style={{ display: 'flex', justifyContent: 'space-between', fontWeight: 'bold', fontSize: 16 }}>{itemDetail.title}<div className="color-title">{itemDetail.amount.toLocaleString()}</div></div>
        <div className="color-link" style={{ fontSize: 12 }}>{itemDetail.category.toLocaleString()}</div>
        <div className="color-title" style={{ fontSize: 12 }}>{dayjs(itemDetail.date).format('YYYY-MM-DD hh:mma')}</div>
      </div>
    </Card>
  );
}

export default React.memo(ExpenseListItem);
