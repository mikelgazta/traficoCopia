import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../assets/IncidentList.css';
import useToken from '../Store/useStore'; // Importa el store

function App() {
  const navigate = useNavigate();
  const [incidents, setIncidents] = useState([]);
  const { token } = useToken();


  // Llamada a la API para obtener las incidencias
  useEffect(() => {
    const fetchIncidents = async () => {
      try {
        const headers = {
          'Authorization': `${token}`,
          'Content-Type': 'application/json',
        };
        const response = await fetch('http://127.0.0.1:8000/api/listaIncidencias', { headers });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        console.log(data);
        setIncidents(data);
      } catch (error) {
        console.error('Error al obtener las incidencias:', error);
      }
    };
    console.log("Token: "+token);

    if (token) {
      fetchIncidents();
    }
  }, [token]); // Se ejecuta cuando el token cambia
  //}, []);
  const handleEdit = (incident) => {
    navigate('/crearIncidencia', { state: { incident } });
  };

  return (
    <div className="incidentList">
      <h2>Modificar Incidencias</h2>
      {incidents.length > 0 ? incidents.map((incident) => (
        <div key={incident.id.string} className="incidentItem" onClick={() => handleEdit(incident)}>
          <p>{incident.tipo.string}</p>
          <p>{incident.causa.string}</p>
          <p>{incident.latitud.string}</p>
          <p>{incident.longitud.string}</p>
        </div>
      )) : <p>No hay incidencias para mostrar.</p>}
    </div>
  );
}

export default App;
