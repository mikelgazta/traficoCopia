import React from 'react';
import { Link, useLocation } from 'react-router-dom';

function Navbar({ currentUser, navigateTo }) {
  const location = useLocation();

  // Función para determinar la visibilidad del enlace activo
  const isActive = (path) => location.pathname.includes(path) ? 'active' : '';

  return (
    <nav className="navbar">
      <ul className="navbar-nav">
        <li className={`nav-item ${isActive('/dashboard')}`}>
          <Link to="/dashboard" className="nav-link" onClick={() => navigateTo('dashboard')}>
            Dashboard
          </Link>
        </li>
        <li className={`nav-item ${isActive('/incidentForm')}`}>
          <Link to="/incidentForm" className="nav-link" onClick={() => navigateTo('incidentForm')}>
            Incidencias
          </Link>
        </li>
        <li className={`nav-item ${isActive('/settings')}`}>
          <Link to="/settings" className="nav-link" onClick={() => navigateTo('settings')}>
            Settings
          </Link>
        </li>
      </ul>
      <div className="user-info">
        {currentUser && <span>Bienvenido, {currentUser}</span>}
      </div>
    </nav>
  );
}
export default Navbar;
