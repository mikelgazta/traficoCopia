<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Mail;

class CorreoController extends Controller
{
    public function enviarCorreo(Request $request)
    {
        try {
            $remitenteCorreo = 'ikccy@plaiaundi.net'; // Reemplaza con tu dirección de correo
            $contrasenaCorreo = 'gaztainazpi'; // Reemplaza con tu contraseña de correo
    
            $datos = $request->json()->all();
    
            $destinatario = $datos['destinatario']; // Esta es la dirección de correo electrónico del usuario registrado
            $asunto = $datos['asunto'];
            $cuerpo = $datos['cuerpo'];
    
            Mail::send([], [], function ($message) use ($remitenteCorreo, $destinatario, $asunto, $cuerpo) {
                $message->from($remitenteCorreo);
                $message->to($destinatario);
                $message->subject($asunto);
                $message->setBody($cuerpo);
            });
    
            return response()->json(['message' => 'Correo enviado exitosamente.'], 200);
        } catch (\Exception $e) {
            return response()->json(['message' => 'Error al enviar el correo: ' . $e->getMessage()], 400);
        }
    }
    
}
