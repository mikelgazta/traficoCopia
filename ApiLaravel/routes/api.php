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

// Ruta para iniciar sesión
Route::post('/login', [App\Http\Controllers\API\LoginController::class, 'login']);

// Rutas para obtener todas las incidencias
Route::get('/listaIncidencias', [App\Http\Controllers\IncidenciaController::class, 'verIncidencias']);

// Ruta para crear una nueva incidencia
Route::post('/crearIncidencia', [App\Http\Controllers\IncidenciaController::class, 'crearIncidencia']);

// Ruta para obtener una incidencia específica por su ID
Route::get('/incidencias/{id}', [App\Http\Controllers\IncidenciaController::class, 'verIncidencia']);

// Ruta para actualizar una incidencia específica por su ID
Route::put('/incidencias/{id}', [App\Http\Controllers\IncidenciaController::class, 'actualizarIncidencia']);

// Ruta para eliminar una incidencia específica por su ID
Route::delete('/incidencias/{id}', [App\Http\Controllers\IncidenciaController::class, 'eliminarIncidencia']);

Route::middleware('auth:api')->group(function () {
    // Rutas protegidas que requieren autenticación

        // Ruta para cerrar sesión
        Route::post('/logout', [LoginController::class, 'logout']);
});

