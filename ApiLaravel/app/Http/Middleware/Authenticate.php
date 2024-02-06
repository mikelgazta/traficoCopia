<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Support\Facades\Auth;
use App\Models\Usuario; // Ajusta la ruta del modelo según sea necesario


class Authenticate
{
    public function handle($request, Closure $next)
    {
        // Verificar si el token de autorización está presente en la solicitud
        if (!$request->header('Authorization')) {
            return response()->json(['error' => 'Unauthorized'], 401);
        }
    
        // Obtener el token de autorización de la solicitud
        $token = $request->header('Authorization');
    
        // Buscar el usuario por el token en la base de datos
        $usuario = Usuario::where('TOKEN', $token)->first();
    
        // Verificar si se encontró un usuario con el token dado
        if (!$usuario) {
            return response()->json(['error' => 'Unauthorized'], 401);
        }
    
        // Autenticar al usuario
        Auth::login($usuario);
    
        // Permitir que la solicitud continúe hacia el controlador
        return $next($request);
    }
}
