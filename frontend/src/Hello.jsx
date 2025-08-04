import { useEffect, useState } from 'react';

function Hello() {
  const [message, setMessage] = useState('Chargement...');
  const [isLoading, setIsLoading] = useState(true);
  const [hasError, setHasError] = useState(false);

  useEffect(() => {
    setIsLoading(true);
    setHasError(false);
    
    fetch('https://azbane-backend-app-dhc7c4h5a5ayg2hc.francecentral-01.azurewebsites.net/hello')
      .then((res) => res.text())
      .then((data) => {
        setMessage(data);
        setIsLoading(false);
        setHasError(false);
      })
      .catch((err) => {
        setMessage('Erreur : ' + err.message);
        setIsLoading(false);
        setHasError(true);
      });
  }, []);

  return (
    <div className="space-y-4">
      <h3 className="text-lg font-semibold text-road-800 flex items-center gap-2">
        <span className="text-xl">🔗</span>
        Status de la connexion
      </h3>
      
      <div className={`p-4 rounded-lg border-l-4 ${
        isLoading 
          ? 'bg-blue-50 border-blue-400 text-blue-800'
          : hasError 
          ? 'bg-red-50 border-red-400 text-red-800'
          : 'bg-green-50 border-green-400 text-green-800'
      }`}>
        {isLoading && (
          <div className="flex items-center gap-3">
            <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-blue-600"></div>
            <span>Connexion au backend...</span>
          </div>
        )}
        
        {!isLoading && (
          <div className="flex items-center gap-3">
            <span className="text-lg">
              {hasError ? '❌' : '✅'}
            </span>
            <div>
              <p className="font-medium">
                {hasError ? 'Erreur de connexion' : 'Connexion réussie'}
              </p>
              <p className="text-sm opacity-80 mt-1">
                {message}
              </p>
            </div>
          </div>
        )}
      </div>
      
      {!isLoading && !hasError && (
        <div className="text-sm text-road-600 bg-road-50 p-3 rounded-lg">
          <span className="font-medium">Backend URL:</span><br />
          <code className="text-xs bg-white px-2 py-1 rounded border">
            https://azbane-backend-app-dhc7c4h5a5ayg2hc.francecentral-01.azurewebsites.net
          </code>
        </div>
      )}
    </div>
  );
}

export default Hello;
