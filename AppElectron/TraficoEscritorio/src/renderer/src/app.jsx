import React, { useEffect } from 'react';
import useStore from './Store/useStore'; // Importa el store
import Dashboard from './components/Dashboard';

function App() {
  const { token, error, setToken, setError } = useStore(); // Obtiene el estado y las funciones

  useEffect(() => {
    iniciarSesionAutomatico();
  }, []);

  const iniciarSesionAutomatico = async () => {
    try {
      const data = await iniciarSesion();
      setToken(data);
      setError('');
    } catch (error) {
      handleLoginError(error);
    }
  };

  const iniciarSesion = async () => {
    const credenciales = {
      email: 'mikelgazta@plaiaundi.com',
      contrasena: '123456',
    };

    const response = await fetch('http://127.0.0.1:8080/api/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(credenciales),
    });

    if (!response.ok) {
      throw new Error('Las credenciales son incorrectas.');
    }

    return await response.text();
  };

  const handleLoginError = (error) => {
    console.error('Error al iniciar sesión:', error);
    setError('Error al iniciar sesión: ' + error.message);
  };

  return (
    <div>
      <Dashboard />
    </div>
  );
}

export default App;
