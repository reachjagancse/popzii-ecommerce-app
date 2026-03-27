import React, { useEffect, useState } from 'react';
import api from '../api';

function Dashboard() {
  const [stats, setStats] = useState({ orders: 0, revenue: 0, products: 0 });

  useEffect(() => {
    const load = async () => {
      const [products, orders] = await Promise.all([
        api.get('/api/instamart/products?page=0&size=200'),
        api.get('/api/foodorders')
      ]);
      const productList = products.data?.data || [];
      const orderList = orders.data?.data || [];
      const revenue = orderList.reduce((sum, o) => sum + Number(o.totalAmount || 0), 0);
      setStats({ orders: orderList.length, revenue, products: productList.length });
    };
    load();
  }, []);

  return (
    <div>
      <div className="topbar">
        <h1>Vendor Dashboard</h1>
        <span className="pill">Live store</span>
      </div>
      <section className="grid">
        <div className="card">
          <h3>Total Orders</h3>
          <div className="metric">{stats.orders}</div>
        </div>
        <div className="card">
          <h3>Revenue</h3>
          <div className="metric">${Number(stats.revenue || 0).toFixed(2)}</div>
        </div>
        <div className="card">
          <h3>Products</h3>
          <div className="metric">{stats.products}</div>
        </div>
      </section>
    </div>
  );
}

export default Dashboard;