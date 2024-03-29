<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Incidencia extends Model
{
    use HasFactory;

    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'INCIDENCIAS';

    /**
     * The primary key associated with the table.
     *
     * @var string
     */
    protected $primaryKey = 'ID';

    /**
     * Indicates if the model should be timestamped.
     *
     * @var bool
     */
    public $timestamps = false;

    /**
     * The attributes that are mass assignable.
     *
     * @var array<int, string>
     */
    protected $fillable = [
        'TIPO',
        'CAUSA',
        'COMIENZO',
        'NVL_INCIDENCIA',
        'CARRETERA',
        'DIRECCION',
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
        'COMIENZO' => 'datetime',
    ];

    // Definir relaciones y otros métodos de la clase aquí...

    /**
     * Get the user associated with the incident.
     */
    public function usuario()
    {
        return $this->belongsTo(Usuario::class, 'USUARIO', 'EMAIL');
    }
}
