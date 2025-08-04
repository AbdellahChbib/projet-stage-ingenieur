import Hello from './Hello';

function App() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-road-50 via-white to-construction-50">
      {/* Header avec thème routier */}
      <header className="navbar px-6 py-4">
        <div className="max-w-7xl mx-auto">
          <h1 className="text-3xl font-bold text-shadow flex items-center gap-3">
            <span className="text-construction-400">🛣️</span>
            AZBANE - Gestion Routière
          </h1>
          <p className="text-road-200 mt-2">
            Plateforme de gestion des projets d'infrastructure routière
          </p>
        </div>
      </header>

      {/* Contenu principal */}
      <main className="max-w-7xl mx-auto px-6 py-12">
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Section d'information */}
          <div className="card">
            <div className="flex items-center gap-3 mb-4">
              <span className="text-2xl">🚧</span>
              <h2 className="text-2xl font-bold text-road-800">
                Système Intégré
              </h2>
            </div>
            <p className="text-road-600 mb-6">
              Notre plateforme combine la puissance de React avec Spring Boot 
              pour offrir une solution complète de gestion des projets routiers.
            </p>
            <div className="space-y-3">
              <div className="flex items-center gap-3">
                <span className="w-2 h-2 bg-construction-500 rounded-full"></span>
                <span className="text-road-700">Cartographie interactive avec OpenLayers</span>
              </div>
              <div className="flex items-center gap-3">
                <span className="w-2 h-2 bg-construction-500 rounded-full"></span>
                <span className="text-road-700">Visualisation 3D des maquettes BIM</span>
              </div>
              <div className="flex items-center gap-3">
                <span className="w-2 h-2 bg-construction-500 rounded-full"></span>
                <span className="text-road-700">Authentification sécurisée JWT</span>
              </div>
            </div>
          </div>

          {/* Section de connexion backend */}
          <div className="card">
            <div className="flex items-center gap-3 mb-4">
              <span className="text-2xl">⚡</span>
              <h2 className="text-2xl font-bold text-road-800">
                Connexion Backend
              </h2>
            </div>
            <Hello />
          </div>
        </div>

        {/* Section des fonctionnalités */}
        <div className="mt-12">
          <h2 className="text-3xl font-bold text-center text-road-800 mb-8">
            Fonctionnalités Principales
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="card text-center hover:shadow-xl transition-shadow duration-300">
              <div className="text-4xl mb-4">🗺️</div>
              <h3 className="text-xl font-semibold text-road-800 mb-2">
                Cartographie
              </h3>
              <p className="text-road-600">
                Visualisation interactive des réseaux routiers avec OpenLayers
              </p>
            </div>
            
            <div className="card text-center hover:shadow-xl transition-shadow duration-300">
              <div className="text-4xl mb-4">🏗️</div>
              <h3 className="text-xl font-semibold text-road-800 mb-2">
                Modélisation 3D
              </h3>
              <p className="text-road-600">
                Intégration et visualisation des maquettes BIM en 3D
              </p>
            </div>
            
            <div className="card text-center hover:shadow-xl transition-shadow duration-300">
              <div className="text-4xl mb-4">📊</div>
              <h3 className="text-xl font-semibold text-road-800 mb-2">
                Analytics
              </h3>
              <p className="text-road-600">
                Tableaux de bord et analyses avancées des projets
              </p>
            </div>
          </div>
        </div>
      </main>

      {/* Footer */}
      <footer className="bg-road-800 text-road-200 py-8 mt-16">
        <div className="max-w-7xl mx-auto px-6 text-center">
          <p>&copy; 2024 AZBANE - Excellence en Infrastructure Routière</p>
        </div>
      </footer>
    </div>
  );
}

export default App;
