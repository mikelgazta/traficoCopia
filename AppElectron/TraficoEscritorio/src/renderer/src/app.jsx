// app.js
import React, { useState, useEffect } from 'react';
import BarChart from './BarChart';

const App = () => {
  const [tipoInforme, setTipoInforme] = useState('incidencias');
  const [datosIncidencias, setDatosIncidencias] = useState([]);

  // Función para cambiar el tipo de informe
  const cambiarTipoInforme = (nuevoTipo) => {
    setTipoInforme(nuevoTipo);
  };

  // Función para obtener datos de incidencias desde la API
  const obtenerDatosIncidencias = async () => {
    try {
      const response = await fetch('https://opendata.euskadi.eus/api-traffic/?api=traffic');
      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Error al obtener datos de la API:', error);
      return [];
    }
  };

  // Obtén los datos de incidencias al cargar el componente
  useEffect(() => {
    const obtenerDatos = async () => {
      const datos = await obtenerDatosIncidencias();
      setDatosIncidencias(datos);
    };

    obtenerDatos();
  }, []); // El segundo argumento [] asegura que se ejecute solo una vez al montar el componente

  return (
    <div>
      <h1>Tu Aplicación de Informes</h1>
      <button onClick={() => cambiarTipoInforme('incidencias')}>Incidencias</button>
      <button onClick={() => cambiarTipoInforme('otrosTiposDeInforme')}>Otros Tipos</button>

      {tipoInforme === 'incidencias' && (
        <BarChart
          data={datosIncidencias}
          color={['#3498db']}
          height={300}
          width={600}
          cornerRadius={5}
        />
      )}

      {tipoInforme === 'otrosTiposDeInforme' && (
        <div>Otros Tipos de Informe</div>
      )}
    </div>
  );
};


export default App;
