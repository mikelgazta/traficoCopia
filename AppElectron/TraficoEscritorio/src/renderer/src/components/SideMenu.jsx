// SideMenu.jsx
import React from 'react';
import './SideMenu.css';

const SideMenu = ({ activeItem, onItemClick }) => {
  return (
    <nav className="side-menu">
      <ul className="side-menu-list">
        <li className={`side-menu-item ${activeItem === 'dashboard' && 'active'}`}>
          <a href="#" className="side-menu-link" onClick={() => onItemClick('dashboard')}>
            Lista de Incidencias
          </a>
        </li>
        <li className={`side-menu-item ${activeItem === 'createIncident' && 'active'}`}>
          <a href="#" className="side-menu-link" onClick={() => onItemClick('createIncident')}>
            Crear Incidencia
          </a>
        </li>
        <li className={`side-menu-item ${activeItem === 'settings' && 'active'}`}>
          <a href="#" className="side-menu-link" onClick={() => onItemClick('settings')}>
            Settings
          </a>
        </li>
      </ul>
    </nav>
  );
};

export default SideMenu;
