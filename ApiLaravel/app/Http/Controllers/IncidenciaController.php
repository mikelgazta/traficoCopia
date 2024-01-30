<?php

namespace App\Http\Controllers;

use App\Models\Incidencia;
use Illuminate\Http\Request;
use App\Http\Controllers\Controller;

class IncidenciaController extends Controller
{
    public function verIncidencias()
    {
        $incidencias = Incidencia::all();
        return response()->json($incidencias);
    }

    public function crearIncidencia(Request $request)
    {
        $data = $request->validate([
            'TIPO' => 'required|string',
            'CAUSA' => 'required|string',
            'COMIENZO' => 'required|date',
            'NVL_INCIDENCIA' => 'required|string',
            'CARRETERA' => 'required|string',
            'DIRECCION' => 'required|string',
            'LATITUD' => 'required|numeric',
            'LONGITUD' => 'required|numeric',
            'USUARIO' => 'required|string', // Ajusta el tipo de dato según corresponda
            // Agrega aquí validaciones adicionales si es necesario
        ]);

        $incidencia = Incidencia::create($data);
        return response()->json($incidencia, 201);
    }

    public function verIncidencia($id)
    {
        $incidencia = Incidencia::findOrFail($id);
        return response()->json($incidencia);
    }

    public function actualizarIncidencia(Request $request, $id)
    {
        $incidencia = Incidencia::findOrFail($id);
        $data = $request->validate([
            'TIPO' => 'string',
            'CAUSA' => 'string',
            'COMIENZO' => 'date',
            'NVL_INCIDENCIA' => 'string',
            'CARRETERA' => 'string',
            'DIRECCION' => 'string',
            'LATITUD' => 'numeric',
            'LONGITUD' => 'numeric',
            'USUARIO' => 'string', // Ajusta el tipo de dato según corresponda
            // Agrega aquí validaciones adicionales si es necesario
        ]);

        $incidencia->update($data);
        return response()->json($incidencia, 200);
    }

    public function eliminarIncidencia($id)
    {
        $incidencia = Incidencia::findOrFail($id);
        $incidencia->delete();
        return response()->json(null, 204);
    }
}
