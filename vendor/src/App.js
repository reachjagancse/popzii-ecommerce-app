import React from 'react';
import { BrowserRouter as Router, Routes, Route, NavLink } from 'react-router-dom';
import Dashboard from './components/Dashboard';
import Products from './components/Products';
import Orders from './components/Orders';

function App() {
  return (
    <Router>
      <div className="vendor-shell">
        <aside className="sidebar">
          <div className="brand">Popzii <span>Vendor</span></div>
          <nav className="nav">
            <NavLink to="/dashboard">Dashboard</NavLink>
            <NavLink to="/products">Products</NavLink>
            <NavLink to="/orders">Orders</NavLink>
          </nav>
          <div className="sidebar-footer">Status: Online</div>
        </aside>
        <main className="content">
          <Routes>
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/products" element={<Products />} />
            <Route path="/orders" element={<Orders />} />
            <Route path="/" element={<Dashboard />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;