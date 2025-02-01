import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { saveTokens } from "../Utils/Authentication";

function Register() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [userRole, setUserRole] = useState("");
    const navigate = useNavigate("");

    const handleRegister = async () => {
        const response = await fetch("http://localhost:8080/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password, userRole }),
        });

        if (response.ok) {
            const data = await response.json();
            saveTokens(data.access_token, data.refresh_token);
            console.log(data.message);
            navigate("/getAccounts");
        }
        else {
            alert("Registration Failed. Please try again!");
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
            <input
                type="text"
                placeholder="User Role"
                value={userRole}
                onChange={(e) => setUserRole(e.target.value)}
            /> 
            <button onClick={handleRegister}>Register</button>
            <button onClick={navigate("/login")}>Login</button>
        </div>
    );
};

export default Register;