import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api';

function Dashboard() {
  const [stats, setStats] = useState({ vendors: 0, orders: 0, revenue: 0 });

  useEffect(() => {
    const load = async () => {
      const [restaurants, orders] = await Promise.all([
        api.get('/api/restaurants?page=0&size=200'),
        api.get('/api/foodorders')
      ]);
      const vendorsCount = restaurants.data?.data?.length ?? 0;
      const ordersList = orders.data?.data ?? [];
      const revenue = ordersList.reduce((sum, o) => sum + Number(o.totalAmount || 0), 0);
      setStats({ vendors: vendorsCount, orders: ordersList.length, revenue });
    };
    load();
  }, []);

  return (
    <div>
      <div className="topbar">
        <h1>Admin Dashboard</h1>
        <span className="pill">System healthy</span>
      </div>

      <section className="grid">
        <div className="card">
          <h3>Total Vendors</h3>
          <div className="metric">{stats.vendors}</div>
        </div>
        <div className="card">
          <h3>Total Orders</h3>
          <div className="metric">{stats.orders}</div>
        </div>
        <div className="card">
          <h3>Revenue</h3>
          <div className="metric">${Number(stats.revenue || 0).toFixed(2)}</div>
        </div>
      </section>

      <section className="panel">
        <h2>Quick actions</h2>
        <div className="list">
          <div className="list-row">
            <span>Review vendor applications</span>
            <Link className="button" to="/vendors">Go to vendors</Link>
          </div>
          <div className="list-row">
            <span>Audit latest orders</span>
            <Link className="button" to="/orders">Go to orders</Link>
          </div>
        </div>
      </section>
    </div>
  );
}

export default Dashboard;