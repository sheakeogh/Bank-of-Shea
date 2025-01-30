import { useState, useContext } from 'react';
import axios from 'axios';
import { AuthenticationContext } from './AuthenticationContext';

function App() {
  const [user, setUser] = useState({
    username: "",
    password: "",
    userRole: ""
  });

  const { authenticationState, setAuthenticationTokens } = useContext(AuthenticationContext);

  const sendRequest = e => {
    e.preventDefault();
    axios.post("http://localhost:8080/register", {
      username: user.username,
      password: user.password,
      userRole: user.userRole
    })
    .then(function (response) {
      console.log(response);
      setAuthenticationTokens({
        accessToken: response.data.access_token,
        refreshToken: response.data.refresh_token,
        message: response.data.message
      });
    })
    .catch(function (error) {
      console.log(error);
    });
  };

  return (
    <div className="App">
      <form onSubmit={sendRequest}>
        <input
          type="text"
          value={user.username}
          onChange={e => setUser({ ...user, username: e.target.value })}
          placeholder="Username"
        />
        <input
          type="password"
          value={user.password}
          onChange={e => setUser({ ...user, password: e.target.value })}
          placeholder="Password"
        />
        <input
          type="text"
          value={user.userRole}
          onChange={e => setUser({ ...user, userRole: e.target.value })}
          placeholder="User Role"
        />
        <button type="submit">Register</button>
      </form>
      <div>
        <h3>Authentication Response</h3>
        <p>Access Token: {authenticationState.accessToken}</p>
        <p>Refresh Token: {authenticationState.refreshToken}</p>
        <p>Message: {authenticationState.message}</p>
      </div>
    </div>
  );
}

export default App;