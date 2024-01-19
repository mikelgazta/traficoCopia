#arrancar docker

docker compose up --build -d

echo "Se ha construido correctamente"

cd AppElectron

npm install

cd TraficoEscritorio

npm run dev

