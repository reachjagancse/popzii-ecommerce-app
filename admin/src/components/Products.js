import React, { useEffect, useState } from 'react';
import api from '../api';

function Products() {
  const [products, setProducts] = useState([]);
  const [form, setForm] = useState({ name: '', categoryId: '', basePrice: '' });

  const loadProducts = async () => {
    const resp = await api.get('/api/instamart/products?page=0&size=200');
    setProducts(resp.data?.data || []);
  };

  useEffect(() => {
    loadProducts();
  }, []);

  const handleCreate = async () => {
    if (!form.name || !form.categoryId || !form.basePrice) return;
    await api.post('/api/instamart/products', {
      categoryId: Number(form.categoryId),
      name: form.name,
      basePrice: Number(form.basePrice)
    });
    setForm({ name: '', categoryId: '', basePrice: '' });
    loadProducts();
  };

  return (
    <div>
      <div className="topbar">
        <h1>Manage Products</h1>
        <button className="button" type="button" onClick={handleCreate}>Add product</button>
      </div>

      <section className="panel">
        <h2>Instamart catalog</h2>
        <div className="table" style={{ marginBottom: 16 }}>
          <div className="table-row">
            <input
              placeholder="Product name"
              value={form.name}
              onChange={e => setForm({ ...form, name: e.target.value })}
              style={{ padding: 8, borderRadius: 8, border: '1px solid #ece7e2' }}
            />
            <input
              placeholder="Category ID"
              value={form.categoryId}
              onChange={e => setForm({ ...form, categoryId: e.target.value })}
              style={{ padding: 8, borderRadius: 8, border: '1px solid #ece7e2' }}
            />
            <input
              placeholder="Base price"
              value={form.basePrice}
              onChange={e => setForm({ ...form, basePrice: e.target.value })}
              style={{ padding: 8, borderRadius: 8, border: '1px solid #ece7e2' }}
            />
            <span />
          </div>
        </div>
        <div className="table">
          <div className="table-header">
            <span>Product</span>
            <span>Category</span>
            <span>Price</span>
            <span>Status</span>
          </div>
          {products.map(p => (
            <div className="table-row" key={p.id}>
              <span>{p.name}</span>
              <span>{p.categoryId}</span>
              <span>${Number(p.basePrice || 0).toFixed(2)}</span>
              <span className={p.isActive ? 'badge good' : 'badge'}>{p.isActive ? 'Active' : 'Inactive'}</span>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}

export default Products;
