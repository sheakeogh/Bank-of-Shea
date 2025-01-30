import React, { createContext, useState } from 'react';

export const AuthenticationContext = createContext();

export const AuthenticationProvider = ({ children }) => {
  const [authenticationState, setAuthenticationState] = useState({
    accessToken: "",
    refreshToken: "",
    message: ""
  });

  const setAuthenticationTokens = (data) => {
    setAuthenticationState({
      accessToken: data.accessToken,
      refreshToken: data.refreshToken,
      message: data.message
    });
  };

  return (
    <AuthenticationContext.Provider value={{ authenticationState, setAuthenticationTokens }}>
      {children}
    </AuthenticationContext.Provider>
  );
};