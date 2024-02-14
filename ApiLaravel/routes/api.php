<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\CameraController;
use App\Http\Controllers\IncidenciaController;
use App\Http\Middleware\CorsMiddleware;


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

// Rutas que no requieren autenticación
Route::middleware([CorsMiddleware::class])->group(function () {
    // Rutas que no requieren autenticación ni CORS
    Route::post('/register', [App\Http\Controllers\API\AuthController::class, 'register']);
    Route::post('/login', [App\Http\Controllers\API\LoginController::class, 'login'])->name('login');
    Route::post('/logout', [App\Http\Controllers\API\LoginController::class, 'logout']);

    Route::middleware('auth:api')->group(function () {

        Route::post('/enviarCorreo', [App\Http\Controllers\CorreoController::class, 'enviarCorreo']);
        // Rutas que requieren autenticación y CORS
        Route::get('/listaIncidencias', [IncidenciaController::class, 'verIncidencias']);
        Route::post('/crearIncidencia', [App\Http\Controllers\IncidenciaController::class, 'crearIncidencia']);
        Route::get('/incidencias/{id}', [App\Http\Controllers\IncidenciaController::class, 'verIncidencia']);
        Route::put('/incidencias/{id}', [App\Http\Controllers\IncidenciaController::class, 'actualizarIncidencia']);
        Route::delete('/incidencias/{id}', [App\Http\Controllers\IncidenciaController::class, 'eliminarIncidencia']);

        Route::get('/listaCamaras', [CameraController::class, 'verCamaras']);
        Route::post('/crearCamaras', [CameraController::class, 'crearCamara']);
        Route::get('/camaras/{id}', [CameraController::class, 'verCamara']);
        Route::put('/camaras/{id}', [CameraController::class, 'actualizarCamara']);
        Route::delete('/camaras/{id}', [CameraController::class, 'eliminarCamara']);
    });
});



// Ruta para obtener información del usuario autenticado
Route::middleware('auth:api')->get('/user', function (Request $request) {
    return $request->user();
});
