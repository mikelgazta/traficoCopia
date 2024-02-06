<?php

namespace App\Http\Middleware;

use Closure;

class CorsMiddleware
{
    public function handle($request, Closure $next)
    {
        // Permite el acceso desde cualquier origen
        $headers = [
            'Access-Control-Allow-Origin' => '*',
            'Access-Control-Allow-Methods' => 'GET, POST, PUT, DELETE, OPTIONS',
            'Access-Control-Allow-Headers' => 'Origin, Content-Type, X-Auth-Token, Authorization',
        ];

        // Si la solicitud es una solicitud de tipo OPTIONS, simplemente responde con los encabezados CORS
        if ($request->isMethod('OPTIONS')) {
            return response()->json('OK', 200, $headers);
        }

        // Deja pasar la solicitud al siguiente middleware
        $response = $next($request);

        // Agrega los encabezados CORS a la respuesta
        foreach ($headers as $key => $value) {
            $response->header($key, $value);
        }

        return $response;
    }
}
