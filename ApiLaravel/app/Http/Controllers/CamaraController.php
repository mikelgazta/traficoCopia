<?php

namespace App\Http\Controllers;

use App\Models\Camara;
use Illuminate\Http\Request;

class CamaraController extends Controller
{
    public function verCamaras()
    {
        try {
            $camaras = Camara::all();
            return response()->json($camaras);
        } catch (\Exception $e) {
            return response()->json(['error' => 'Error al obtener las cámaras'], 500);
        }
    }

    public function crearCamara(Request $request)
    {
        $data = $request->validate([
            'NOMBRE' => 'required|string',
            'URL' => 'required|string',
            'LATITUD' => 'required|numeric',
            'LONGITUD' => 'required|numeric',
            'CARRETERA' => 'required|string',
            'KILOMETRO' => 'required|numeric',
            'IMAGEN' => 'required|string',
            'USUARIO' => 'required|string',
            // Agrega aquí validaciones adicionales si es necesario
        ]);

        try {
            $camara = Camara::create($data);
            return response()->json($camara, 201);
        } catch (\Exception $e) {
            return response()->json(['error' => 'Error al crear la cámara'], 500);
        }
    }

    public function verCamara($id)
    {
        try {
            $camara = Camara::findOrFail($id);
            return response()->json($camara);
        } catch (\Exception $e) {
            return response()->json(['error' => 'Cámara no encontrada'], 404);
        }
    }

    public function actualizarCamara(Request $request, $id)
    {
        $data = $request->validate([
            'NOMBRE' => 'string',
            'URL' => 'string',
            'LATITUD' => 'numeric',
            'LONGITUD' => 'numeric',
            'CARRETERA' => 'string',
            'KILOMETRO' => 'numeric',
            'IMAGEN' => 'string',
            'USUARIO' => 'string',
            // Agrega aquí validaciones adicionales si es necesario
        ]);

        try {
            $camara = Camara::findOrFail($id);
            $camara->update($data);
            return response()->json($camara, 200);
        } catch (\Exception $e) {
            return response()->json(['error' => 'Error al actualizar la cámara'], 500);
        }
    }

    public function eliminarCamara($id)
    {
        try {
            $camara = Camara::findOrFail($id);
            $camara->delete();
            return response()->json(null, 204);
        } catch (\Exception $e) {
            return response()->json(['error' => 'Error al eliminar la cámara'], 500);
        }
    }
}
