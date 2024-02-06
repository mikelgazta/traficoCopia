import React, { useState, useEffect } from 'react';
import Navbar from './components/Navbar';
import './components/App.css';
import CrearIncidencia from './components/CrearIncidencia';

function App() {
  const [currentComponent, setCurrentComponent] = useState('dashboard');
  const [incidents, setIncidents] = useState([]);
  const [token, setToken] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const navigateTo = (component) => {
    setCurrentComponent(component);
  };

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
        setLoading(false);

        setToken(data.user.TOKEN);
      
      } catch (error) {
        setError('Error al iniciar sesión: ' + error.message);
        setLoading(false);
      }
    };

    login();
  }, []); 

  const renderComponent = () => {
    if (loading) {
      return <p>Cargando...</p>;
    }

    if (error) {
      return <p>{error}</p>;
    }

  // Llamada a la API para obtener las incidencias
  useEffect(() => {
    const fetchIncidents = async () => {
      try {
        const response = await fetch('http://127.0.0.1:8000/api/listaIncidencias', {
          headers: {
            Authorization: token,
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
