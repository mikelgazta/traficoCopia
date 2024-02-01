import React, { useState, useEffect } from 'react';
import axios from 'axios';
import IncidentList from './IncidentList';

function App() {
  const [incidents, setIncidents] = useState([]);

  useEffect(() => {
    const fetchIncidents = async () => {
      try {
        const response = await axios.get('URL_DE_TU_API/incidents');
        setIncidents(response.data);
      } catch (error) {
        console.error('Error al obtener las incidencias:', error);
      }
    };

    fetchIncidents();
  }, []); // El array vacío [] indica que este efecto se ejecuta solo una vez al montar el componente

  return (
    <div className="container">
      <IncidentList incidents={incidents} />
    </div>
  );
}

export default App;
