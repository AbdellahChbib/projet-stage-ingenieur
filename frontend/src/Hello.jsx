import { useEffect, useState } from 'react';

function Hello() {
  const [message, setMessage] = useState('Chargement...');

  useEffect(() => {
    fetch('https://azbane-backend-app-dhc7c4h5a5ayg2hc.francecentral-01.azurewebsites.net/hello')
      .then((res) => res.text())
      .then((data) => setMessage(data))
      .catch((err) => setMessage('Erreur : ' + err.message));
  }, []);

  return (
    <div>
      <h2>Message depuis le backend :</h2>
      <p>{message}</p>
    </div>
  );
}

export default Hello;
