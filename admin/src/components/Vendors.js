import React, { useEffect, useState } from 'react';
import api from '../api';

function Vendors() {
  const [restaurants, setRestaurants] = useState([]);
  const [form, setForm] = useState({ name: '', cuisine: '', address: '' });

  const loadRestaurants = async () => {
    const resp = await api.get('/api/restaurants?page=0&size=200');
    setRestaurants(resp.data?.data || []);
  };

  useEffect(() => {
    loadRestaurants();
  }, []);

  const handleCreate = async () => {
    if (!form.name) return;
    await api.post('/api/restaurants', {
      name: form.name,
      cuisine: form.cuisine,
      address: form.address
    });
    setForm({ name: '', cuisine: '', address: '' });
    loadRestaurants();
  };

  return (
    <div>
      <div className="topbar">
        <h1>Manage Vendors</h1>
        <button className="button" type="button" onClick={handleCreate}>Add restaurant</button>
      </div>

      <section className="panel">
        <h2>Restaurants</h2>
        <div className="table" style={{ marginBottom: 16 }}>
          <div className="table-row">
            <input
              placeholder="Restaurant name"
              value={form.name}
              onChange={e => setForm({ ...form, name: e.target.value })}
              style={{ padding: 8, borderRadius: 8, border: '1px solid #ece7e2' }}
            />
            <input
              placeholder="Cuisine"
              value={form.cuisine}
              onChange={e => setForm({ ...form, cuisine: e.target.value })}
              style={{ padding: 8, borderRadius: 8, border: '1px solid #ece7e2' }}
            />
            <input
              placeholder="Address"
              value={form.address}
              onChange={e => setForm({ ...form, address: e.target.value })}
              style={{ padding: 8, borderRadius: 8, border: '1px solid #ece7e2' }}
            />
            <span />
          </div>
        </div>
        <div className="table">
          <div className="table-header">
            <span>Restaurant</span>
            <span>Cuisine</span>
            <span>Status</span>
            <span>Address</span>
          </div>
          {restaurants.map(r => (
            <div className="table-row" key={r.id}>
              <span>{r.name}</span>
              <span>{r.cuisine || 'N/A'}</span>
              <span className={r.isActive ? 'badge good' : 'badge'}>{r.isActive ? 'Active' : 'Inactive'}</span>
              <span>{r.address || 'N/A'}</span>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}

export default Vendors;