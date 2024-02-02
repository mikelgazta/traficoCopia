import React, { useState, useEffect } from 'react';
import Navbar from './components/Navbar';
import './components/App.css';
import CrearIncidencia from './components/CrearIncidencia';

function App() {
  // Define el estado para controlar qué componente se muestra
  const [currentComponent, setCurrentComponent] = useState('dashboard');
  const [incidents, setIncidents] = useState([]);
  const [token, setToken] = useState('');

  // Función para cambiar el componente a mostrar
  const navigateTo = (component) => {
    setCurrentComponent(component);
  };

  // Llamada a la API para iniciar sesión y obtener el token
  useEffect(() => {
    const login = async () => {
      try {
        const response = await fetch('http://127.0.0.1:8000/api/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            email: 'mikelgazta@plaiaundi.com',
            password: '123456',
          }),
        });

        if (!response.ok) {
          throw new Error('Error al iniciar sesión');
        }

        const data = await response.json();
        setToken(data.token);
      } catch (error) {
        console.error('Error al iniciar sesión:', error);
      }
    };

    login();
  }, []); // El segundo parámetro [] indica que esta función se ejecutará solo una vez al montar el componente

  // Llamada a la API para obtener las incidencias
  useEffect(() => {
    const fetchIncidents = async () => {
      try {
        const response = await fetch('http://127.0.0.1:8000/api/listaIncidencias', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (!response.ok) {
          throw new Error('Error al obtener las incidencias');
        }
        const data = await response.json();
        setIncidents(data);
      } catch (error) {
        console.error('Error al obtener las incidencias:', error);
      }
    };

    if (token) {
      fetchIncidents();
    }
  }, [token]); // Esta función se ejecutará cuando el token cambie

  // Renderiza el componente adecuado en función del estado currentComponent
  const renderComponent = () => {
    switch (currentComponent) {
      case 'dashboard':
        return (
          <div>
            <Navbar currentUser="Usuario Ejemplo" navigateTo={navigateTo} />
            <h2>Dashboard</h2>
            <ul>
              {incidents.map((incident) => (
                <li key={incident.ID}>
                  <h3>{incident.TIPO}</h3>
                  <p>{incident.CAUSA}</p>
                  {/* Agrega otros detalles de la incidencia según tus necesidades */}
                </li>
              ))}
            </ul>
          </div>
        );
      case 'incidentForm':
        return <CrearIncidencia />;
      case 'settings':
        return (
          <div>
            {/* Contenido de la configuración */}
          </div>
        );
      default:
        return (
          <div>
            <p>Página no encontrada</p>
          </div>
        );
    }
  };

  return (
    <div className="container">
      {renderComponent()}
    </div>
  );
}

export default App;
