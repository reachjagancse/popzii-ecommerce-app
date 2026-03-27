import React from 'react';
import { BrowserRouter as Router, Routes, Route, NavLink } from 'react-router-dom';
import Dashboard from './components/Dashboard';
import Vendors from './components/Vendors';
import Orders from './components/Orders';
import Products from './components/Products';

function App() {
  return (
    <Router>
      <div className="admin-shell">
        <aside className="sidebar">
          <div className="brand">Popzii <span>Admin</span></div>
          <nav className="nav">
            <NavLink to="/dashboard">Dashboard</NavLink>
            <NavLink to="/vendors">Vendors</NavLink>
            <NavLink to="/products">Products</NavLink>
            <NavLink to="/orders">Orders</NavLink>
          </nav>
          <div className="sidebar-footer">Last sync: just now</div>
        </aside>

        <main className="content">
          <Routes>
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/vendors" element={<Vendors />} />
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