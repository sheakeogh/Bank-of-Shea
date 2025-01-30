import React from 'react';
import ReactDOM from 'react-dom/client';
import Register from './Register';
import { AuthenticationProvider } from './AuthenticationContext';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <AuthenticationProvider>
      <Register />
    </AuthenticationProvider>
  </React.StrictMode>,
);