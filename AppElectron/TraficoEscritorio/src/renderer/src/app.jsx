import React, { useState, useEffect } from 'react';
import Navbar from './components/Navbar';
import './assets/App.css';
import Dashboard from './components/Dashboard';

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
    const iniciarSesionAutomatico = async () => {
      try {
        const credenciales = {
          email: 'mikelgazta@plaiaundi.com',
          password: '123456',
        };

        const response = await fetch('http://127.0.0.1:8000/api/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(credenciales),
        });

        if (!response.ok) {
          throw new Error('Error al iniciar sesión');
        }

        const data = await response.json();
        setToken(data.user.TOKEN);
      
      } catch (error) {
        console.error('Error al iniciar sesión:', error);
      }
    };

    iniciarSesionAutomatico();
  }, []); // El segundo parámetro [] indica que esta función se ejecutará solo una vez al montar el componente
  // Renderiza el componente adecuado en función del estado currentComponent
  const renderComponent = () => {
    switch (currentComponent) {
      case 'dashboard':
        return (
          <div>
            <Navbar currentUser="Usuario Ejemplo" navigateTo={navigateTo} />
            <Dashboard token={token} />
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
