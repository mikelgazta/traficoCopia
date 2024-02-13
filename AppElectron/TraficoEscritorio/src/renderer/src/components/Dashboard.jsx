import React from 'react';
import { BrowserRouter as Router, Routes, Route, NavLink, useParams } from 'react-router-dom';
import IncidentForm from './IncidentForm';
import CameraForm from './CameraForm';
import IncidentList from './IncidentList';
import CameraList from './CameraList';
import Navbar from '../assets/navbar.css'; // Asegúrate de importar tu componente Navbar
import '../assets/Dashboard.css'; // Importa tus estilos CSS

function DynamicForm() {
    const { section, action, id } = useParams();
    const forms = {
        crear: {
            incidencias: <IncidentForm />,
            camaras: <CameraForm />
        },
        modificar: {
            incidencias: <IncidentList id={id} />,
            camaras: <CameraList id={id} />
        }
    };

    return forms[action][section] || <div>Error: Formulario no encontrado</div>;
}

function Dashboard() {
    return (
        <Router>
            <div className="traffic-management">
                <div className="sidebar">
                    <ul>
                        <li><NavLink to="/incidencias/modificar" activeClassName="active">Incidencias</NavLink></li>
                        <li><NavLink to="/camaras/modificar" activeClassName="active">Camaras</NavLink></li>
                    </ul>
                </div>
                <div className="main-content">
                    <div className="navbar">
                        <h2>Título del Dashboard</h2>
                    </div>
                    <Routes>
                        <Route path="/incidencias" element={<IncidentForm />} />
                        <Route path="/camaras" element={<CameraForm />} />
                        <Route path="/:section/:action" element={<DynamicForm />} />
                        <Route path="/:section/:action/:id" element={<DynamicForm />} />
                    </Routes>
                </div>
            </div>
        </Router>
    );
}

export default Dashboard;
