#!/bin/bash

# Espera a que la base de datos esté lista
echo "Esperando a la base de datos..."
while ! nc -z db 3306; do
  sleep 1
done
echo "Base de datos iniciada"

# Ejecuta las migraciones y la instalación de Passport
echo "Ejecutando migraciones..."
php /var/www/html/artisan migrate --force

echo "Instalando Laravel Passport..."
php /var/www/html/artisan passport:install

# Inicia Apache
apache2-foreground
