import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../assets/IncidentList.css';
import useToken from '../Store/useStore'; // Importa el store

function App() {
  const navigate = useNavigate();
  const [incidents, setIncidents] = useState([]);
  const { token } = useToken();

  console.log("IncidentListToken: " + token);
  // Llamada a la API para obtener las incidencias
  useEffect(() => {
    const fetchIncidents = async () => {
      try {
        const headers = {
          'Authorization': token, // Aquí no incluyas el prefijo "Bearer"
        };
        const response = await fetch('http://127.0.0.1:8000/api/listaIncidencias', { headers: headers });

        console.log(headers);
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
    console.log("Token: " + token);

    if (token) {
      fetchIncidents();
    }
  }, [token]);
  // Se ejecuta cuando el token cambia
  const handleEdit = (incident) => {
    navigate(`/incidencias/editar/${incident.id}`);
  };
  

  return (
    <div className="incidentList">
      <h2>Modificar Incidencias</h2>
      {incidents.length > 0 ? incidents.map((incident, index) => (
        <div key={index} className="incidentItem" onClick={() => handleEdit(incident)}>
          <p>Tipo: {incident.TIPO}</p>
          <p>Causa: {incident.CAUSA}</p>
          <p>Latitud: {incident.LATITUD}</p>
          <p>Longitud: {incident.LONGITUD}</p>
        </div>
      )) : <p>No hay incidencias para mostrar.</p>}

    </div>
  );
}

export default App;
