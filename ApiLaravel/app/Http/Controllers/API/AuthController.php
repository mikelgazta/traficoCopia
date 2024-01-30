<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Models\Usuario;
use Illuminate\Http\Request;

class AuthController extends Controller
{
    public function register(Request $request)
    {
        // Validación de los datos del formulario
        $validatedData = $request->validate([
            'email' => 'required|string|email|unique:USUARIOS|max:250',
            'password' => 'required|string|min:6',
            // Agrega aquí las reglas de validación para otros campos si es necesario
        ]);

        // Crear el nuevo usuario
        $user = Usuario::create([
            'EMAIL' => $validatedData['email'],
            'CONTRASENA' => bcrypt($validatedData['password']),
            // Puedes asignar valores predeterminados para otros campos si es necesario
        ]);

        // Generar token de acceso
        //$token = $user->createToken('authToken')->accessToken;

        return response()->json([
            'user' => $user,
            //'access_token' => $token,
        ]);
    }

}
