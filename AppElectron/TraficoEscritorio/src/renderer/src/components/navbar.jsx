import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import '../assets/navbar.css'; // Importa el archivo CSS

function Navbar({ currentUser, navigateTo }) {
  const location = useLocation();

  // Función para determinar la visibilidad del enlace activo
  const base = location.pathname.split('/')[1];
  const createPath = `/${base}/crear`;
  const modifyPath = `/${base}/modificar`;
  const isActive = (path) => location.pathname.includes(path) ? 'active' : '';

  return (
    <nav className="navbar">
      <div className="navbar-nav">
        <Link className={`nav-item nav-link ${isActive('/crear')}`} to={createPath}>Crear</Link>
        <Link className={`nav-item nav-link ${isActive('/modificar')}`} to={modifyPath}>Modificar</Link>
      </div>
    </nav>
  );
}
export default Navbar;
