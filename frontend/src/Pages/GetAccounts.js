import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { getAccessToken } from "../Utils/Authentication"

function GetAccounts() {
    const [accounts, setAccounts] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        const fetchAccounts = async () => {
            const response = await fetch("http://localhost:8080/accounts", {
                headers: {
                    Authorization: `Bearer ${getAccessToken()}`
                }
            });
            if (response.ok) {
                const data = await response.json();
                setAccounts(data);
            }
            else {
                console.log(response.data);
            }
        };

        fetchAccounts();
    }, [navigate]);

    return (
        <div>
            <ul>
                {accounts.map((account, index) => (
                    <li key={index}>{JSON.stringify(account)}</li>
                ))}
            </ul>
        </div>
    );
};

export default GetAccounts;