import React from 'react';
import SideMenu from './SideMenu';
import IncidentForm from './IncidentForm';
import Navbar from './Navbar';
import './CrearIncidencia.css';

function CrearIncidencia() {
  return (
    <div className="crear-incidencia-container">
      <div className="navbar-container">
        <Navbar></Navbar>
      </div>
      <div className="main-container">
        <div className="side-menu-container">
          <SideMenu></SideMenu>
        </div>
        <div className="incident-form-container">
          <IncidentForm></IncidentForm>
        </div>
      </div>
    </div>
  );
}


export default CrearIncidencia;
