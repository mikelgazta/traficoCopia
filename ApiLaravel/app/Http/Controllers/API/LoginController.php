<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Auth;
use App\Models\Usuario;

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
            if (Auth::check()) {
                // El usuario está autenticado, procede con la actualización del token
                $user = Auth::user();
            

                // Obtener el dominio del correo electrónico
                $emailParts = explode('@', $user->EMAIL);
                $domain = end($emailParts);

                // Verificar si el dominio es "plaiaundi.com"
                if ($domain === 'plaiaundi.com') {
                    $user->SN_ADMIN = 'S';
                } else {
                    $user->SN_ADMIN = 'N';
                }
            
                // Actualiza el token y SN_ADMIN en la base de datos
                $user->TOKEN = $user->createToken('authToken')->accessToken;
                
                //$user->SN_ADMIN = "S";
                
                // Actualizar el token de acceso en la tabla USUARIOS
                Usuario::where('ID', $user->ID)->update(['TOKEN' => $user->TOKEN]);
                Usuario::where('ID', $user->ID)->update(['SN_ADMIN' => $user->SN_ADMIN]);

                
                if ($user->save()) {
                    // Guardado con éxito
                    return response()->json([
                        'message' => 'Inicio de sesión exitoso',
                        'user' => $user,
                    ]);
                } else {
                    // Error al guardar
                    return response()->json(['error' => 'Error al actualizar el usuario'], 500);
                }
            } else {
                return response()->json(['error' => 'Usuario no autenticado'], 401);
            }
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
