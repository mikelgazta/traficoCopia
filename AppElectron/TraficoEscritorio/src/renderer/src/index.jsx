import React from 'react';
import ReactDOM from 'react-dom';
import Navbar from './components/Navbar'; // Importa el componente Navbar

const App = () => {
  const currentUser = 'UsuarioEjemplo'; // Aquí debes obtener el nombre de usuario de tu aplicación
  return (
    <div className="app">
      <Navbar currentUser={currentUser} /> {/* Utiliza el componente Navbar aquí */}
      {/* Otros componentes de tu aplicación */}
    </div>
  );
};

ReactDOM.render(<App />, document.getElementById('root'));
