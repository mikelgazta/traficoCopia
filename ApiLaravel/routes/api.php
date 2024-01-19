<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "api" middleware group. Make something great!
|
*/

Route::middleware('auth:api')->get('/user', function (Request $request) {
    return $request->user();
});

// Ejemplo de ruta para el registro utilizando el controlador AuthController
Route::post('/register', [App\Http\Controllers\API\AuthController::class, 'register']);

Route::middleware('auth:api')->group(function () {
    // Rutas protegidas que requieren autenticación
        // Ruta para iniciar sesión
        Route::post('/login', [LoginController::class, 'login']);

        // Ruta para cerrar sesión
        Route::post('/logout', [LoginController::class, 'logout']);
});

