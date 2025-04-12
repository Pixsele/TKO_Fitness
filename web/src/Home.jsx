import React from 'react';
import { Link } from 'react-router-dom';
import './Home.css';

const Home = () => {
    return (
        <div className="home-container">
                {/* Логотип, который ведет на главную страницу */}
                <div className="logo-container">
                    <Link to="/" className="logo">
                        TKO Fitness {/* Логотип — это текст */}
                    </Link>
                </div>

            {/* Три кнопки, ведущие на другие страницы */}
            <div className="navigation-buttons">
                <div className="nav-button">
                    <Link to="/catalog" className="nav-button">
                    Тренировки
                    </Link>
                </div>
                <div className="nav-button">
                    <Link to="/programm" className="nav-button">
                    Программы
                    </Link>
                </div>
                <div className="nav-button">
                    <Link to="/kcal" className="nav-button">
                    Расчет ккал
                    </Link>
                </div>
            </div>

            {/* Кнопки регистрации и входа */}
            <div className="auth-buttons">
                <button className="auth-button">Регистрация</button>
                <button className="auth-button">Вход</button>
            </div>

            {/* Два SVG изображения */}
            <div className="image-container">
                <img src="/assets/Composition.svg" alt="Image 1" className="mockup" />
                <img src="/assets/play.svg" alt="Image 2" className="google_play" />
            </div>

            {/* Текст */}
            <div className="text-container">
                <p className="main-text">Добро пожаловать на главную страницу!</p>
            </div>
        </div>
    );
};

export default Home;