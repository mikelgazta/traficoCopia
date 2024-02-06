import React from 'react';
import './Navbar.css';

const Navbar = ({ currentUser, navigateTo }) => {
  return (
    <nav className="navbar">
      <ul className="navbar-nav">
        <li className="nav-item">
          <a href="#" className="nav-link" onClick={() => navigateTo('dashboard')}>
            Dashboard
          </a>
        </li>
        <li className="nav-item">
          <a href="#" className="nav-link" onClick={() => navigateTo('incidentForm')}>
            Incidencias
          </a>
        </li>
        <li className="nav-item">
          <a href="#" className="nav-link" onClick={() => navigateTo('settings')}>
            Settings
          </a>
        </li>
      </ul>
      <div className="user-info">
        {currentUser && <span>Bienvenido, {currentUser}</span>}
      </div>
    </nav>
  );
};

export default Navbar;
