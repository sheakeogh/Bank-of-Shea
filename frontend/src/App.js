import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./Pages/Login";
import Register from "./Pages/Register";
import GetAccounts from "./Pages/GetAccounts";
import ProtectedRoute from "./Components/ProtectedRoute";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/getAccounts" element={<ProtectedRoute><GetAccounts /></ProtectedRoute>} />
      </Routes>
    </Router>
  );
}

export default App;
