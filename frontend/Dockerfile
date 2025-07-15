# Étape 1 : Utiliser Node.js pour lancer le serveur Vite en mode dev
FROM node:20

# Créer le répertoire de travail
WORKDIR /app

# Copier tous les fichiers (package.json, vite.config.js, etc.)
COPY . .

# Installer les dépendances
RUN npm install

# Exposer le port Vite (5173 par défaut)
EXPOSE 5173

# Démarrer le serveur de développement
CMD ["npm", "run", "dev"]
