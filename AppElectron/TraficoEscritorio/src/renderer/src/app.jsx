import React, { useEffect } from 'react';
import useStore from './Store/useStore'; // Importa el store
import Dashboard from './components/Dashboard';

function App() {
  const { token, error, setToken, setError } = useStore(); // Obtiene el estado y las funciones

  useEffect(() => {
    const iniciarSesionAutomatico = async () => {
      try {
        const credenciales = {
          email: 'ikccy@plaiaundi.net',
          password: '123456',
        };

        const response = await fetch('http://127.0.0.1:8000/api/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(credenciales),
        });

        if (response.ok) {
          const responseData = await response.json(); // Parsea la respuesta JSON
          const token = responseData.user.TOKEN; // Extrae el token del objeto JSON
          setToken(token); // Actualiza el token en el store
          setError('');
        }else {
          setError('Las credenciales son incorrectas.');
        }
      } catch (error) {
        setError('Error al iniciar sesión.' + error);
      }
    };

    iniciarSesionAutomatico(); // Llama a la función dentro del useEffect

  }, []); // El arreglo vacío indica que este efecto se ejecutará solo una vez al montar el componente

  return (
    <div>
      <Dashboard />
    </div>
  );
}

export default App;
