import { useEffect, useState } from 'react';

function Hello() {
  const [message, setMessage] = useState('Chargement...');

  useEffect(() => {
    fetch('http://localhost:8080/hello')
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
