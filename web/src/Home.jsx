import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './Home.css';
import { useAuth } from './contexts/AuthContext.jsx';
import AuthModal from './AuthModal.jsx';

const Home = () => {
    const { user, isAuthenticated, logout } = useAuth();
    const navigate = useNavigate();

    const [authModalMode, setAuthModalMode] = useState(null); // null, 'login', 'register'

    const openLoginModal = () => {
        setAuthModalMode('login');
    };

    const openRegisterModal = () => {
        setAuthModalMode('register');
    };

    const closeAuthModal = () => {
        setAuthModalMode(null);
    };

    // Обработчик для защищенных ссылок
    const handleProtectedLink = (e, path) => {
        if (!isAuthenticated) {
            e.preventDefault();
            openLoginModal();
        } else {
            navigate(path);
        }
    };

    return (
        <div className="home-container">
            <div className="top-bar">
                <div className="logo-container">
                    <Link to="/" className="logo">
                        TKO Fitness
                    </Link>
                </div>

                <div className="navigation-links">
                    <Link
                        to="/catalog"
                        className="nav-link"
                        onClick={(e) => handleProtectedLink(e, '/catalog')}
                    >
                        Тренировки
                    </Link>
                    <Link
                        to="/programm"
                        className="nav-link"
                        onClick={(e) => handleProtectedLink(e, '/programm')}
                    >
                        Программы
                    </Link>
                    <Link
                        to="/kcal"
                        className="nav-link"
                        onClick={(e) => handleProtectedLink(e, '/kcal')}
                    >
                        Расчет ккал
                    </Link>
                </div>

                {isAuthenticated ? (
                    <div className="user-info">
                        <img src="/assets/Group%202.svg" alt="User Icon" />
                        <Link to="/profile" className="user-name">{user?.name}</Link>
                        <button onClick={logout} className="auth-button">Выйти</button>
                    </div>
                ) : (
                    <div className="auth-buttons">
                        <button onClick={openRegisterModal} className="auth-button">Регистрация</button>
                        <button onClick={openLoginModal} className="auth-button">Вход</button>
                    </div>
                )}
            </div>

            {/* Модальное окно авторизации */}
            {authModalMode && (
                <AuthModal
                    mode={authModalMode}
                    onClose={closeAuthModal}
                    onSwitchMode={setAuthModalMode}
                    onSuccess={closeAuthModal}
                />
            )}

            <div className="main-section">
                <div className="left-content">
                    <p className="main-text">Total<br />Kinetic<br />Output</p>
                    <img src="/assets/play.svg" alt="Google Play" className="google-play" width={200} />
                </div>
                <div className="right-content">
                    <img src="/assets/Composition.svg" alt="Mockup" className="mockup" />
                </div>
            </div>
        </div>
    );
};

export default Home;
