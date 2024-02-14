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

            $destinatario = $request->input('destinatario');
            $asunto = $request->input('asunto');
            $cuerpo = $request->input('cuerpo');

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
