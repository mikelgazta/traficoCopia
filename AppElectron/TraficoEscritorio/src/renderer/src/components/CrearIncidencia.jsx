import React from 'react';
import SideMenu from './SideMenu';
import IncidentForm from './IncidentForm';
import './CrearIncidencia.css';

function CrearIncidencia() {
  return (
    <div className="crear-incidencia-container">
      <div className="side-menu-container">
        <SideMenu />
      </div>
      <div className="incident-form-container">
        <IncidentForm />
      </div>
    </div>
  );
}

export default CrearIncidencia;
