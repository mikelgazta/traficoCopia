<?php

namespace App\Http\Middleware;

use Closure;

class AddContentSecurityPolicy
{
    public function handle($request, Closure $next)
    {
        $response = $next($request);

        $response->header('Content-Security-Policy', "default-src 'self'; connect-src 'self' http://127.0.0.1:8000");

        return $response;
    }
}
