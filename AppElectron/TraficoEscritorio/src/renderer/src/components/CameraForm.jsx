// CameraForm.jsx
import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import '../assets/CameraForm.css';
import useToken from '../Store/useStore';

// Función para transformar los datos de la cámara
function transformCameraData(cameraData) {
    const transformedData = {};
    for (const key in cameraData) {
        if (
            cameraData[key] &&
            typeof cameraData[key] === 'object' &&
            'string' in cameraData[key]
        ) {
            transformedData[key] = cameraData[key].string;
        } else if (
            cameraData[key] &&
            typeof cameraData[key] === 'object' &&
            !('string' in cameraData[key])
        ) {
            transformedData[key] = ''; 
        } else {
            transformedData[key] = cameraData[key];
        }
    }
    return transformedData;
}

// Componente funcional CameraForm
function CameraForm() {
    const location = useLocation();
    const isEditing = location.state && location.state.camera;
    const { token } = useToken();

    // Estado inicial de la cámara
    const initialState = {
        nombre: '',
        url: '',
        imagen: null,
        latitud: '',
        longitud: '',
        carretera: '',
        kilometro: '', 
        usuario: 'mikelgazta@plaiaundi.com' 
    };
    const [camera, setCamera] = useState(initialState);

    // Efecto para cargar datos de edición
    useEffect(() => {
        if (isEditing && location.state.camera) {
            const transformedCamera = transformCameraData(location.state.camera);
            setCamera(transformedCamera);
        }
    }, [location, isEditing]);

    // Manejar cambios en los campos del formulario
    const handleChange = (e) => {
        const { name, value } = e.target;
        setCamera(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    // Manejar cambio de archivo de imagen
    const handleFileChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onloadend = () => {
                setCamera(prevState => ({
                    ...prevState,
                    imagen: reader.result,
                    url: file.name
                }));
            };
        }
    };

    // Manejar envío del formulario
    const handleSubmit = async (e) => {
        e.preventDefault();
        const method = isEditing ? 'PUT' : 'POST';
        const url = `http://127.0.0.1:8080/api${isEditing ? '/camaras/' + camera.id : '/crearCamaras'}`;

        try {
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `${token}`
                },
                body: JSON.stringify(camera)
            });

            if (response.ok) {
                // Procesar respuesta
                if (!isEditing)
                    setCamera(initialState);
            } else {
                console.error('Error en la respuesta del servidor:', response.status);
            }
        } catch (error) {
            console.error('Error al enviar el formulario:', error);
        }
    };

    // Manejar eliminación de cámara
    const handleDelete = async () => {
        if (isEditing) {
            try {
                const deleteData = { id: camera.id };

                const response = await fetch(`http://127.0.0.1:8000/api/camaras/${camera.id}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `${token}`
                    },
                    body: JSON.stringify(deleteData)
                });

                if (response.ok) {
                    // Procesar respuesta
                    setCamera(initialState);
                } else {
                    console.error('Error en la respuesta del servidor:', response.status);
                }
            } catch (error) {
                console.error('Error al intentar borrar:', error);
            }
        }
    };


    return (
        <div className="camera-form">
            <h1 className='camera-text'>Gestion de Trafico - Camaras</h1>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Nombre</label>
                    <input
                        type="text"
                        name="nombre"
                        value={camera.nombre}
                        onChange={handleChange}
                    />
                </div>
                <div className="form-group file-input-group">
                    <label htmlFor="url">Imagen</label>
                    <div className="input-wrapper">
                        <input
                            id="url"
                            type="text"
                            name="url"
                            value={camera.url}
                            onChange={handleChange}

                        />
                        <label htmlFor="imagen" className="file-upload-icon">
                            📷
                        </label>
                    </div>
                    <input
                        id="imagen"
                        type="file"
                        name="imagen"
                        accept="image/*"
                        onChange={handleFileChange}
                        style={{ display: 'none' }}
                    />
                </div>
                <div className="form-group">
                    <label>Latitud</label>
                    <input
                        type="text"
                        name="latitud"
                        value={camera.latitud}
                        onChange={handleChange}
                    />
                </div>
                <div className="form-group">
                    <label>Longitud</label>
                    <input
                        type="text"
                        name="longitud"
                        value={camera.longitud}
                        onChange={handleChange}
                    />
                </div>
                <div className="form-group">
                    <label>Carretera</label>
                    <input
                        type="text"
                        name="carretera"
                        value={camera.carretera}
                        onChange={handleChange}
                    />
                </div>
                <div className="form-group">
                    <label>Kilómetro</label>
                    <input
                        type="text"
                        name="kilometro"
                        value={camera.kilometro}
                        onChange={handleChange}
                    />
                </div>
                <div className="form-actions">
                    {isEditing && (
                        <button type="button" className="delete-button" onClick={handleDelete}>
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
export default CameraForm;
