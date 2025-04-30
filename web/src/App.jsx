// App.jsx
import React, {useState} from 'react';
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import Home from './Home';
import Catalog from './Catalog';
import Programm from './Programm';
import Kcal from './Kcal';
import { AuthProvider } from './contexts/AuthContext.jsx';
import RegisterForm from './RegisterForm.jsx';
import LoginForm from './LoginForm.jsx';
import Profile from "./Profile.jsx";
import WorkoutDetail from "./WorkoutDetail.jsx";
import ExercisePage from "./ExercisePage.jsx";
import Trein from "./Trein.jsx";

const App = () => {
    const [authMode, setAuthMode] = useState('login');

    return (
        <AuthProvider>
            <Router>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/catalog" element={<Catalog />} />
                    <Route path="/programm" element={<Programm />} />
                    <Route path="/kcal" element={<Kcal />} />
                    <Route path="/register" element={<RegisterForm />} />
                    <Route path="/login" element={<LoginForm />} />
                    <Route path="/profile" element={<Profile />} />
                    <Route path="/workout/:id" element={<WorkoutDetail />} />
                    <Route path="/exercise/:id" element={<ExercisePage />} />
                    <Route path="/trein/:id" element={<Trein />} />


                </Routes>
            </Router>
        </AuthProvider>
    );
};


export default App;
