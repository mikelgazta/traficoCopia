<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Flujo extends Model
{
    use HasFactory;

    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'FLUJOS';

    /**
     * The primary key associated with the table.
     *
     * @var string
     */
    protected $primaryKey = 'ID';

    /**
     * The attributes that are mass assignable.
     *
     * @var array<int, string>
     */
    protected $fillable = [
        'FECHA',
        'RANGO_TIEMPO',
        'MEDIA_VELOCIDAD',
        'TOTAL_VEHICULOS',
        'LATITUD',
        'LONGITUD',
        'USUARIO',
    ];

    /**
     * The attributes that should be cast.
     *
     * @var array<string, string>
     */
    protected $casts = [
        'FECHA' => 'datetime',
    ];

    // Definir relaciones y otros métodos de la clase aquí...

    /**
     * Get the user associated with the flow.
     */
    public function usuario()
    {
        return $this->belongsTo(Usuario::class, 'USUARIO', 'EMAIL');
    }
}
