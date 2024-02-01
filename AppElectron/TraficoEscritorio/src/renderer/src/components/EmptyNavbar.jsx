import React from 'react';
import './EmptyNavbar.css'; // Estilos del EmptyNavbar

const EmptyNavbar = ({ currentUser }) => {
  return (
    <nav className="empty-navbar">
      <div className="user-info">
        {currentUser && <span>Bienvenido, {currentUser}</span>}
      </div>
    </nav>
  );
};

export default EmptyNavbar;
