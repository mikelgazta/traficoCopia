<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Support\Facades\Auth;

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

        // Intentar autenticar al usuario utilizando el token
        if (!Auth::onceUsingId($token)) {
            return response()->json(['error' => 'Unauthorized'], 401);
        }

        // Si la autenticación es exitosa, permitir que la solicitud continúe hacia el controlador
        return $next($request);
    }
}
