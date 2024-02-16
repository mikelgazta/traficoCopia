import React, { useState, useEffect } from 'react';
import { useLocation, useParams } from 'react-router-dom';
import '../assets/IncidentForm.css';
import useToken from '../Store/useStore'; // Importa el store

// Función para transformar los datos de las Incidencias
function transformIncidentData(incidentData) {
  const transformedData = {};
  for (const key in incidentData) {
    if (
      incidentData[key] &&
      typeof incidentData[key] === 'object' &&
      'string' in incidentData[key]
    ) {
      transformedData[key] = incidentData[key].string;
    } else if (
      incidentData[key] &&
      typeof incidentData[key] === 'object' &&
      !('string' in incidentData[key])
    ) {
      transformedData[key] = ''; // Si es un objeto sin propiedad 'string', establece el campo como una cadena vacía
    } else {
      transformedData[key] = incidentData[key];
    }
  }
  return transformedData;
}
function IncidentForm() {
  const location = useLocation();
  const isEditing = location.state && location.state.incident;
  const { token } = useToken();
  const { id } = useParams();

  const initialState = {
    id:'',
    TIPO: '', // Asegúrate de que todos los campos estén inicializados
    CAUSA: '',
    COMIENZO: '',
    NVL_INCIDENCIA: '',
    CARRETERA: '',
    DIRECCION: '',
    LATITUD: '',
    LONGITUD: '',
    USUARIO: 'mikelgazta@plaiaundi.com'
  };



  const formatDate = (dateString) => {
    const parts = dateString.match(/(\d{2})-(\w{3})-(\d{2})/);
    if (!parts) return '';

    const months = {
      'JAN': '01', 'FEB': '02', 'MAR': '03', 'APR': '04', 'MAY': '05', 'JUN': '06',
      'JUL': '07', 'AUG': '08', 'SEP': '09', 'OCT': '10', 'NOV': '11', 'DEC': '12'
    };
    const year = '20' + parts[3];
    const month = months[parts[2].toUpperCase()];
    const day = parts[1];

    return `${year}-${month}-${day}`;
  };

  const [incident, setIncident] = useState(initialState);

  useEffect(() => {
    if (isEditing && location.state.incident) {
      // Primero transforma los datos para obtener solo los valores 'string'
      const transformedIncident = transformIncidentData(location.state.incident);

      // Luego, formatea la fecha de comienzo si es necesario
      if (transformedIncident.comienzo) {
        transformedIncident.comienzo = formatDate(transformedIncident.comienzo);
      }

      // Establece el estado con los datos transformados y formateados
      setIncident(transformedIncident);
    }
  }, [location, isEditing]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setIncident((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const method = isEditing ? 'PUT' : 'POST';
    const url = isEditing
      ? `http://127.0.0.1:8000/api/incidencias/${incident.id}`
      : 'http://127.0.0.1:8000/api/crearIncidencia';
  
    try {
      const response = await fetch(url, {
        method: method,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': token,
        },
        body: JSON.stringify(incident),
      });
  
      const responseData = await response.json();
  
      if (response.ok) {
        if (!isEditing) {
          // Si la creación fue exitosa, actualiza el estado con la nueva incidencia, incluyendo su id
          setIncident({ ...incident, id: responseData.id });
        } else {
          // Procesar actualización de la incidencia aquí
        }
      } else {
        console.error('Error en la respuesta del servidor:', responseData);
      }
    } catch (error) {
      console.error('Error al enviar el formulario:', error);
    }
  };
  
  
  

const handleDelete = async () => {
  if (isEditing) {
    try {
      const response = await fetch(`http://127.0.0.1:8000/api/incidencias/${incident.id}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': token
        }
      });

      const responseData = await response.json(); // Parsea la respuesta como JSON

      if (response.ok) {
        // Procesar respuesta
        setIncident(initialState);
      } else {
        console.error('Error en la respuesta del servidor:', responseData); // Loguea el mensaje de error
      }
    } catch (error) {
      console.error('Error al intentar borrar:', error);
    }
  }
};


return (
  <div className="incident-form">
    <h1 className='incidencia-text'>Incidencias</h1>
    <form onSubmit={handleSubmit}>
      <div className="form-group">
        <label>Tipo</label>
        <input
          type="text"
          name="TIPO" // Cambiado para coincidir con la API
          value={incident.TIPO} // Asegúrate de ajustar también el estado
          onChange={handleChange}
        />
      </div>
      <div className="form-group">
        <label>Causa</label>
        <input
          type="text"
          name="CAUSA" // Cambiado para coincidir con la API
          value={incident.CAUSA} // Ajusta el estado
          onChange={handleChange}
        />
      </div>
      <div className="form-group">
        <label>Fecha de Comienzo</label>
        <input
          type="date"
          name="COMIENZO" // Cambiado para coincidir con la API
          value={incident.COMIENZO} // Ajusta el estado
          onChange={handleChange}
        />
      </div>
      <div className="form-group">
        <label>Nivel de Incidencia</label>
        <input
          type="text"
          name="NVL_INCIDENCIA" // Cambiado para coincidir con la API
          value={incident.NVL_INCIDENCIA} // Ajusta el estado
          onChange={handleChange}
        />
      </div>
      <div className="form-group">
        <label>Carretera</label>
        <input
          type="text"
          name="CARRETERA" // Cambiado para coincidir con la API
          value={incident.CARRETERA} // Ajusta el estado
          onChange={handleChange}
        />
      </div>
      <div className="form-group">
        <label>Direccion</label>
        <input
          type="text"
          name="DIRECCION" // Cambiado para coincidir con la API
          value={incident.DIRECCION} // Ajusta el estado
          onChange={handleChange}
        />
      </div>
      <div className="form-group">
        <label>Latitud</label>
        <input
          type="text"
          name="LATITUD" // Cambiado para coincidir con la API
          value={incident.LATITUD} // Ajusta el estado
          onChange={handleChange}
        />
      </div>
      <div className="form-group">
        <label>Longitud</label>
        <input
          type="text"
          name="LONGITUD" // Cambiado para coincidir con la API
          value={incident.LONGITUD} // Ajusta el estado
          onChange={handleChange}
        />
      </div>
      {/* Asegúrate de manejar el campo 'USUARIO' si es necesario */}
      <div className="form-actions">
        {isEditing && (
          <button type="button" onClick={handleDelete}>
            Borrar
          </button>
        )}
        <button type="submit" className="submit-button">
          {isEditing ? 'Actualizar' : 'Crear'}
        </button>
      </div>
    </form>
  </div>
);


}

export default IncidentForm;
