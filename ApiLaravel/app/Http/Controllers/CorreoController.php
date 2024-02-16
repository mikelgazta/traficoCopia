<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Mail;

class CorreoController extends Controller
{
    public function enviarCorreo(Request $request)
    {
        $destinatario = $request->input('destinatario');
        $asunto = $request->input('asunto');
        $cuerpo = $request->input('cuerpo');

        Mail::raw($cuerpo, function ($message) use ($destinatario, $asunto) {
            $message->to($destinatario)
                    ->subject($asunto);
        });

        return response()->json(['message' => 'Correo enviado exitosamente.']);
    }
}

