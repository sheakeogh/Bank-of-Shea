import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { saveTokens } from "../Utils/Authentication";

function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate("");

    const handleLogin = async () => {
        const response = await fetch("http://localhost:8080/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password }),
        });

        if (response.ok) {
            const data = await response.json();
            saveTokens(data.access_token, data.refresh_token);
            console.log(data.message);
            navigate("/getAccounts");
        }
        else {
            alert("Login Failed. Please try again!");
        }
    };

    return (
        <div>
            <input
                type="text"
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
            /> 
            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            /> 
            <button onClick={handleLogin}>Login</button>
            <button onClick={navigate("/register")}>Register</button>
        </div>
    );
};

export default Login;