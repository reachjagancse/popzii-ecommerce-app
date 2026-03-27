import React, { useEffect, useState } from 'react';
import api from '../api';

function Orders() {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    const load = async () => {
      const resp = await api.get('/api/foodorders');
      setOrders(resp.data?.data || []);
    };
    load();
  }, []);

  return (
    <div>
      <div className="topbar">
        <h1>Manage Orders</h1>
        <span className="pill">Live updates on</span>
      </div>

      <section className="panel">
        <h2>Recent orders</h2>
        <div className="table">
          <div className="table-header">
            <span>Order</span>
            <span>Status</span>
            <span>Total</span>
            <span>Delivery</span>
          </div>
          {orders.map(order => (
            <div className="table-row" key={order.id}>
              <span>#{order.id}</span>
              <span className={order.status === 'DELIVERED' ? 'badge good' : 'badge'}>{order.status}</span>
              <span>${Number(order.totalAmount || 0).toFixed(2)}</span>
              <span>{order.createdAt ? new Date(order.createdAt).toLocaleString() : 'N/A'}</span>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}

export default Orders;