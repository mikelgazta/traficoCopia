import React, { useState } from 'react';
import './IncidentForm.css';
import SideMenu from './SideMenu';

function IncidentForm() {
  // State para almacenar los valores de los campos del formulario
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    date: '',
    time: '',
    incidentType: '',
    image: null, // Para almacenar la imagen adjunta
  });

  // Función para manejar cambios en los campos de texto del formulario
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  // Función para manejar cambios en el campo de imagen del formulario
  const handleImageChange = (e) => {
    const imageFile = e.target.files[0];
    setFormData({
      ...formData,
      image: imageFile,
    });
  };

  // Función para manejar el envío del formulario
  const handleSubmit = (e) => {
    e.preventDefault();
    // Aquí puedes enviar formData a tu backend para procesar la información
    console.log(formData);
    // También puedes resetear el estado del formulario después de enviar los datos
    // setFormData({
    //   title: '',
    //   description: '',
    //   date: '',
    //   time: '',
    //   incidentType: '',
    //   image: null,
    // });
  };

  return (
    <div className="form-container">
      <h2>Incident Form</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label className="form-label">Titulo:</label>
          <input type="text" name="title" value={formData.title} onChange={handleChange} className="form-input" />
        </div>
        <div className="form-group">
          <label className="form-label">Descripcion:</label>
          <textarea name="description" value={formData.description} onChange={handleChange} className="form-textarea" />
        </div>
        <div className="form-group">
          <label className="form-label">Fecha:</label>
          <input type="date" name="date" value={formData.date} onChange={handleChange} className="form-input" />
        </div>
        <div className="form-group">
          <label className="form-label">Hora:</label>
          <input type="time" name="time" value={formData.time} onChange={handleChange} className="form-input" />
        </div>
        <div className="form-group">
          <label className="form-label">Tipo de incidente:</label>
          <select name="incidentType" value={formData.incidentType} onChange={handleChange} className="form-select">
            <option value="type1">Type 1</option>
            <option value="type2">Type 2</option>
            <option value="type3">Type 3</option>
          </select>
        </div>
        <div className="form-group">
          <label className="form-label">Adjuntar Imagen:</label>
          <input type="file" accept="image/*" onChange={handleImageChange} className="form-input" />
        </div>
        <button type="submit" className="form-button">Submit</button>
      </form>
    </div>
  );
}

export default IncidentForm;
