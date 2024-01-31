import React from 'react';
import './navbar.css';

const Navbar = ({ currentUser }) => {
  return (
    <nav className="navbar">
      <ul className="navbar-nav">
        <li className="nav-item">
          <a href="#" className="nav-link">Dashboard</a>
        </li>
        <li className="nav-item">
          <a href="#" className="nav-link">Crear Incidencia</a>
        </li>
        <li className="nav-item">
          <a href="#" className="nav-link">Settings</a>
        </li>
      </ul>
      <div className="user-info">
        {currentUser && <span>Bienvenido, {currentUser}</span>}
      </div>
    </nav>
  );
};

export default Navbar;
