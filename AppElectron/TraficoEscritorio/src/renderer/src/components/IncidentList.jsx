import React, { useState, useEffect } from 'react';
import axios from 'axios';
import IncidentList from './IncidentList';

function IncidentList() {
  const [incidents, setIncidents] = useState([]);
  const [token, setToken] = useState('');

  // Llamada a la API para iniciar sesión y obtener el token
  useEffect(() => {
    const login = async () => {
      try {
        const response = await axios.post('http://127.0.0.1:8000/api/login', {
          email: 'mikelgazta@plaiaundi.com',
          password: '123456',
        });

        const data = response.data;
        setToken(data.token);
      } catch (error) {
        console.error('Error al iniciar sesión:', error);
      }
    };

    login();
  }, []); // Se ejecuta solo una vez al cargar el componente

  // Llamada a la API para obtener las incidencias
  useEffect(() => {
    const fetchIncidents = async () => {
      try {
        const response = await axios.get('http://127.0.0.1:8000/api/listaIncidencias', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        setIncidents(response.data);
      } catch (error) {
        console.error('Error al obtener las incidencias:', error);
      }
    };

    if (token) {
      fetchIncidents();
    }
  }, [token]); // Se ejecuta cuando el token cambia

  return (
    <div className="container">
      <IncidentList incidents={incidents} />
    </div>
  );
}

export default IncidentList;
