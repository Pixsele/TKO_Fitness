// App.jsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './Home';
import Catalog from "./Catalog.jsx";


const App = () => {
    return (
        <Router>
            <Routes>
                {/* Указываем путь и компонент, который нужно отобразить */}
                <Route path="/" element={<Home />} />
                <Route path="/catalog" element={<Catalog />} />
            </Routes>
        </Router>
    );
};

export default App;
