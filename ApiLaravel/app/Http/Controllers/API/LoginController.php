<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Auth;

class LoginController extends Controller
{
    /**
     * Iniciar sesión.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function login(Request $request)
    {
        // Validación de los datos del formulario
        $validatedData = $request->validate([
            'email' => 'required|string|email',
            'password' => 'required|string',
        ]);

        // Intento de inicio de sesión
        if (Auth::attempt($validatedData)) {
            $user = Auth::user();
            $token = $user->createToken('authToken')->accessToken;

            //$user->update(['TOKEN' => $token]);

            $user->TOKEN = $token;
            $user->save();


            return response()->json([
                'message' => 'Inicio de sesión exitoso',
                'user' => $user,
                'access_token' => $token,
            ]);

        } else {
            return response()->json(['error' => 'Credenciales inválidas'], 401);
        }
    }

    /**
     * Cerrar sesión (revocar el token).
     *
     * @return \Illuminate\Http\JsonResponse
     */
    public function logout()
    {
        Auth::user()->token()->revoke();

        return response()->json(['message' => 'Sesión cerrada exitosamente']);
    }
}
