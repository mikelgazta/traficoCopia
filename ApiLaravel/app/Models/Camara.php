<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Camara extends Model
{
    use HasFactory;

    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'CAMARAS';

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
        'NOMBRE',
        'URL',
        'LATITUD',
        'LONGITUD',
        'CARRETERA',
        'KILOMETRO',
        'IMAGEN',
        'USUARIO',
    ];

    /**
     * The attributes that should be cast.
     *
     * @var array<string, string>
     */
    protected $casts = [
        'KILOMETRO' => 'numeric',
    ];

    // Definir relaciones y otros métodos de la clase aquí...

    /**
     * Get the user associated with the camera.
     */
    public function usuario()
    {
        return $this->belongsTo(Usuario::class, 'USUARIO', 'EMAIL');
    }
}
